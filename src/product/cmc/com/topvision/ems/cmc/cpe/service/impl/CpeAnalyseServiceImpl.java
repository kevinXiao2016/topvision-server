/***********************************************************************
 * $ CpeAnalyseServiceImpl.java,v1.0 2013-6-20 14:02:54 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.topvision.ems.cmc.constants.CpeAnalyseConstants;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;
import com.topvision.ems.facade.util.LocalFileData;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.utils.EponIndex;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cpe.dao.CpeAnalyseDao;
import com.topvision.ems.cmc.cpe.service.CpeAnalyseService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.cpe.service.job.CmCpeCollectDataPolicyJob;
import com.topvision.ems.cmc.cpe.service.job.CmCpeStatisticJob;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CmStatusPerfResult;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.performance.domain.CpeStatusPerfResult;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.service.PerformanceStatistics;
import com.topvision.framework.common.CollectTimeRange;
import com.topvision.framework.common.CollectTimeUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.platform.service.SchedulerService;

/**
 * @author jay
 * @created @2013-6-20-14:02:54
 */
@Service("cpeAnalyseService")
public class CpeAnalyseServiceImpl extends CmcBaseCommonService implements CpeAnalyseService, BeanFactoryAware {
    private final Logger logger = LoggerFactory.getLogger(CpeAnalyseServiceImpl.class);
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cpeService")
    private CpeService cpeService;
    @Resource(name = "cpeAnalyseDao")
    private CpeAnalyseDao cpeAnalyseDao;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private PerformanceStatistics performanceStatistics;
    private LocalFileData<PerformanceResult<OperClass>> localFileDataForCmAction;
    private LocalFileData<PerformanceResult<OperClass>> localFileDataForCpeAction;
//    private ArrayBlockingQueue<CmStatusPerfResult> cmStatusPerfResults = new ArrayBlockingQueue<CmStatusPerfResult>(
//            10000);
//    private ArrayBlockingQueue<CpeStatusPerfResult> cpeStatusPerfResults = new ArrayBlockingQueue<CpeStatusPerfResult>(
//            10000);
    private Map<Long, CmNum> lastCmNum = Collections.synchronizedMap(new HashMap<Long, CmNum>());

    @Value("${CmAction.maxPoolsize:20}")
    private int maxPoolsize;
    @Value("${CmAction.blockingQueueSize:4000}")
    private int blockingQueueSize;
    @Value("${CpeAnalyseService.sleep:10}")
    private Integer sleep;
    private ThreadPoolExecutor cmActionThreadPoolExecutor;
    private ThreadPoolExecutor cpeActionThreadPoolExecutor;
    private BeanFactory beanFactory;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    public void start () {
        Thread cmActionWorker = new Thread() {
            @Override
            public void run() {
                setName("CmActionWorker");
                while (true) {
                    try {
                        CmStatusPerfResult cmStatusPerfResult = (CmStatusPerfResult) localFileDataForCmAction.takeFirst();
                        localFileDataForCmAction.remove();
                        logger.debug("start cmActionWorker ");
                        CollectTimeUtil ctu = CollectTimeUtil.getCollectTimeUtil(CollectTimeUtil.CmStatus);
                        CollectTimeRange ctr = ctu.getCollectTimeRange(cmStatusPerfResult.getDt());
                        Set<Long> macList = new HashSet<Long>();
                        if (cmStatusPerfResult.getCmAttributes() == null) {
                            cmStatusPerfResult.setCmAttributes(new ArrayList<CmAttribute>());
                        }
                        for (CmAttribute cmAttribute : cmStatusPerfResult.getCmAttributes()) {
                            macList.add(new MacUtils(cmAttribute.getStatusMacAddress()).longValue());
                            CmActionAnalyseThreadPool cmActionAnalyseThreadPool = (CmActionAnalyseThreadPool) beanFactory.getBean("cmActionAnalyseThreadPool");
                            cmActionAnalyseThreadPool.setEntityId(cmStatusPerfResult.getEntityId());
                            cmActionAnalyseThreadPool.setCmAttribute(cmAttribute);
                            cmActionAnalyseThreadPool.setRealTimeLong(cmStatusPerfResult.getDt());
                            cmActionAnalyseThreadPool.setEndTimeLong(ctr.getEndTimeLong());
                            cmActionThreadPoolExecutor.execute(cmActionAnalyseThreadPool);
                        }
                        List<CmAct> cmActs = cpeAnalyseDao.getCmLastStatusByEntityId(cmStatusPerfResult.getEntityId());
                        for (CmAct cmAct : cmActs) {
                            if (!macList.contains(cmAct.getCmmac()) && CmAct.ONLINE.equals(cmAct.getAction())) {
                                cmAct.setAction(CmAct.OFFLINE);
                                cmAct.setRealtimeLong(cmStatusPerfResult.getDt());
                                cmAct.setTimeLong(ctr.getEndTimeLong());
                                cpeAnalyseDao.insertCmAct(cmAct);
                            }
                        }
                        logger.debug("end cmActionWorker ");
                    } catch (ClassNotFoundException e) {
                        logger.error("", e);
                    } catch (IOException e) {
                        logger.error("", e);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
                }
            }
        };
        cmActionWorker.start();
        Thread cpeActionWorker = new Thread() {
            @Override
            public void run() {
                setName("CpeActionWorker");
                while (true) {
                    try {
                        CpeStatusPerfResult cpeStatusPerfResult = (CpeStatusPerfResult) localFileDataForCpeAction.takeFirst();
                        localFileDataForCpeAction.remove();
                        logger.debug("start cpeActionWorker ");
                        CollectTimeUtil ctu = CollectTimeUtil.getCollectTimeUtil(CollectTimeUtil.CmStatus);
                        CollectTimeRange ctr = ctu.getCollectTimeRange(cpeStatusPerfResult.getDt());
                        Set<Long> macList = new HashSet<Long>();
                        if (cpeStatusPerfResult.getCpeAttributes() == null) {
                            cpeStatusPerfResult.setCpeAttributes(new ArrayList<TopCpeAttribute>());
                        }
                        for (TopCpeAttribute cpeAttribute : cpeStatusPerfResult.getCpeAttributes()) {
                            macList.add(new MacUtils(cpeAttribute.getTopCmCpeMacAddress().toString()).longValue());
                            CpeActionAnalyseThreadPool cpeActionAnalyseThreadPool = (CpeActionAnalyseThreadPool) beanFactory.getBean("cpeActionAnalyseThreadPool");
                            cpeActionAnalyseThreadPool.setEntityId(cpeStatusPerfResult.getEntityId());
                            cpeActionAnalyseThreadPool.setCpeAttribute(cpeAttribute);
                            cpeActionAnalyseThreadPool.setRealTimeLong(cpeStatusPerfResult.getDt());
                            cpeActionAnalyseThreadPool.setEndTimeLong(ctr.getEndTimeLong());
                            cpeActionThreadPoolExecutor.execute(cpeActionAnalyseThreadPool);
                        }
                        List<CpeAct> cpeActs = cpeAnalyseDao.getCpeLastStatusByEntityId(cpeStatusPerfResult
                                .getEntityId());
                        for (CpeAct cpeAct : cpeActs) {
                            if (!macList.contains(cpeAct.getCpemac()) && CpeAct.ONLINE.equals(cpeAct.getAction())) {
                                cpeAct.setAction(CpeAct.OFFLINE);
                                cpeAct.setRealtimeLong(cpeStatusPerfResult.getDt());
                                cpeAct.setTimeLong(ctr.getEndTimeLong());
                                cpeAnalyseDao.insertCpeAct(cpeAct);
                            }
                        }
                        logger.debug("end cpeActionWorker ");
                    } catch (ClassNotFoundException e) {
                        logger.error("", e);
                    } catch (IOException e) {
                        logger.error("", e);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
                }
            }
        };
        cpeActionWorker.start();
    }

    @Override
    @PostConstruct
    public void initialize() {

        try {
            localFileDataForCmAction = LocalFileData.createLocalFileData("CmAction");
            localFileDataForCpeAction = LocalFileData.createLocalFileData("CpeAction");
        } catch (IOException e) {
            logger.debug("",e);
        }
        lastCmNum = Collections.synchronizedMap(cpeAnalyseDao.selectAllDeviceLastCmNum());
        cmActionThreadPoolExecutor = new ThreadPoolExecutor(maxPoolsize, maxPoolsize, maxPoolsize, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(blockingQueueSize));
        cpeActionThreadPoolExecutor = new ThreadPoolExecutor(maxPoolsize, maxPoolsize, maxPoolsize, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(blockingQueueSize));

        try {
            JobDetail job = newJob(CmCpeStatisticJob.class).withIdentity("CmCpeStatisticJob", "Default").build();
            job.getJobDataMap().put("cpeAnalyseService", this);
            job.getJobDataMap().put("cpeAnalyseDao", cpeAnalyseDao);
            job.getJobDataMap().put("cpeService", cpeService);
            job.getJobDataMap().put("entityService", entityService);
            job.getJobDataMap().put("entityTypeService", entityTypeService);
            job.getJobDataMap().put("performanceStatistics", performanceStatistics);
            job.getJobDataMap().put("endTime", System.currentTimeMillis());

            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(),
                    job.getKey().getGroup()).withSchedule(repeatSecondlyForever(10));
            schedulerService.scheduleJob(job, builder.build());

            job = newJob(CmCpeCollectDataPolicyJob.class).withIdentity("CmCpeCollectDataPolicyJob", "Default").build();
            job.getJobDataMap().put("cpeAnalyseDao", cpeAnalyseDao);
            job.getJobDataMap().put("cpeService", cpeService);

            builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup()).withSchedule(
                    repeatSecondlyForever(300));
            schedulerService.scheduleJob(job, builder.build());
        } catch (SchedulerException se) {
            logger.error("", se);
        }
    }

    @Override
    public void append(CmStatusPerfResult cmStatusPerfResult) {
        try {
            localFileDataForCmAction.add(cmStatusPerfResult);
        } catch (IOException e) {
            logger.debug("", e);
        }
//        cmStatusPerfResults.add(cmStatusPerfResult);
    }

    @Override
    public void append(CpeStatusPerfResult cpeStatusPerfResult) {
        try {
            localFileDataForCpeAction.add(cpeStatusPerfResult);
        } catch (IOException e) {
            logger.debug("", e);
        }
//        cpeStatusPerfResults.add(cpeStatusPerfResult);
    }

//    public ArrayBlockingQueue<CmStatusPerfResult> getCmStatusPerfResults() {
//        return cmStatusPerfResults;
//    }
//
//    public void setCmStatusPerfResults(ArrayBlockingQueue<CmStatusPerfResult> cmStatusPerfResults) {
//        this.cmStatusPerfResults = cmStatusPerfResults;
//    }
//
//    public ArrayBlockingQueue<CpeStatusPerfResult> getCpeStatusPerfResults() {
//        return cpeStatusPerfResults;
//    }
//
//    public void setCpeStatusPerfResults(ArrayBlockingQueue<CpeStatusPerfResult> cpeStatusPerfResults) {
//        this.cpeStatusPerfResults = cpeStatusPerfResults;
//    }


    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Map<Long, CmNum> getLastCmNum() {
        return cpeAnalyseDao.selectAllDeviceLastCmNum();
    }

    @Override
    public void updateDeviceCmNumLast(CmNum cmNum) {
        lastCmNum.put(cmNum.getEntityId(), cmNum);
        cpeAnalyseDao.updateDeviceCmNumLast(cmNum);
    }

    @Override
    public void updatePonCmNumLast(CmNum cmNum) {
        cpeAnalyseDao.updatePonCmNumLast(cmNum);
    }

    @Override
    public void updateCmtsCmNumLast(CmNum cmNum) {
        cpeAnalyseDao.updateCmtsCmNumLast(cmNum);
    }

    @Override
    public void updatePortCmNumLast(CmNum cmNum) {
        cpeAnalyseDao.updatePortCmNumLast(cmNum);
    }

    @Override
    public void updateDeviceAllCmNumLast(Long entityId,Map<Long, Map<String, Map<Long, CmNum>>> cmtsListMap, Boolean allDevice) {
        for (Long cmtsIndex : cmtsListMap.keySet()) {
            CmNum cmtsNum = new CmNum();
            Map<String,Map<Long,CmNum>> oneCmtsMap = cmtsListMap.get(cmtsIndex);
            Map<Long,CmNum> upMap = oneCmtsMap.get(CpeAnalyseConstants.UPSTREAM);
            for (Long upChannelIndex : upMap.keySet()) {
                CmNum upCmNum = upMap.get(upChannelIndex);
                upCmNum.setEntityId(entityId);
                upCmNum.setCcIfIndex(cmtsIndex);
                upCmNum.setPortIfIndex(upChannelIndex);
                upCmNum.setPortType(CmNum.UPCHANNEL);
                cmtsNum = sumCmNum(cmtsNum,upCmNum);
                cpeAnalyseDao.deletePortCmNumLast(upCmNum);
                cpeAnalyseDao.insertPortCmNum(upCmNum);
            }
            Map<Long,CmNum> downMap = oneCmtsMap.get(CpeAnalyseConstants.DOWNSTREAM);
            for (Long downChannelIndex : downMap.keySet()) {
                CmNum downCmNum = downMap.get(downChannelIndex);
                downCmNum.setEntityId(entityId);
                downCmNum.setCcIfIndex(cmtsIndex);
                downCmNum.setPortIfIndex(downChannelIndex);
                downCmNum.setPortType(CmNum.DOWNCHANNEL);
                cpeAnalyseDao.deletePortCmNumLast(downCmNum);
                cpeAnalyseDao.insertPortCmNum(downCmNum);
            }
            cmtsNum.setEntityId(entityId);
            cmtsNum.setCcIfIndex(cmtsIndex);
            cpeAnalyseDao.deleteCmtsCmNumLast(cmtsNum);
            cpeAnalyseDao.insertCmtsCmNum(cmtsNum);
        }
        cpeAnalyseDao.deletePonLastCmNum(entityId);
        insertPonCmNumLastByCmtsCmNum(entityId);
        cpeAnalyseDao.deleteDeviceCmNumLast(entityId);
        cpeAnalyseDao.insertDeviceCmNumLastByPonNum(entityId);
    }

    private void insertPonCmNumLastByCmtsCmNum(Long entityId) {
        List<CmNum> cmtsCmNumList = cpeAnalyseDao.selectCmtsCmNumByEntityId(entityId);
        Map<Long,CmNum> ponMap = new HashMap<>();
        for (CmNum cmNum : cmtsCmNumList) {
            Long slotNo = CmcIndexUtils.getSlotNo(cmNum.getCcIfIndex());
            Long ponNo = CmcIndexUtils.getPonNo(cmNum.getCcIfIndex());
            Long ponIndex = EponIndex.getPonIndex(slotNo.intValue(),ponNo.intValue());
            cmNum.setPonIndex(ponIndex);
            CmNum ponCmNum;
            if (ponMap.containsKey(ponIndex)) {
                ponCmNum = ponMap.get(ponIndex);
            } else {
                ponCmNum = new CmNum();
            }
            ponCmNum = sumCmNum(ponCmNum,cmNum);
            ponMap.put(ponIndex,ponCmNum);
        }
        for (CmNum cmNum : ponMap.values()) {
            cpeAnalyseDao.insertPonCmNum(cmNum);
        }
    }

    private CmNum sumCmNum(CmNum c1, CmNum c2) {
        CmNum cmNum = new CmNum();
        cmNum.setEntityId(c2.getEntityId());
        cmNum.setCmcId(c2.getCmcId());
        cmNum.setCcIfIndex(c2.getCcIfIndex());
        cmNum.setPonIndex(c2.getPonIndex());
        cmNum.setPortIfIndex(c2.getPortIfIndex());
        cmNum.setPortType(c2.getPortType());
        cmNum.setTimeLong(c2.getTimeLong());
        cmNum.setRealTimeLong(c2.getRealTimeLong());
        cmNum.setBroadbandNum(c1.getBroadbandNum() + c2.getBroadbandNum());
        cmNum.setIntegratedNum(c1.getIntegratedNum() + c2.getIntegratedNum());
        cmNum.setInteractiveNum(c1.getInteractiveNum() + c2.getInteractiveNum());
        cmNum.setMtaNum(c1.getMtaNum() + c2.getMtaNum());
        cmNum.setOnlineNum(c1.getOnlineNum() + c2.getOnlineNum());
        cmNum.setOfflineNum(c1.getOfflineNum() + c2.getOfflineNum());
        cmNum.setOtherNum(c1.getOtherNum() + c2.getOtherNum());
        cmNum.setCpeBroadbandNum(c1.getCpeBroadbandNum() + c2.getCpeBroadbandNum());
        cmNum.setCpeInteractiveNum(c1.getCpeInteractiveNum() + c2.getCpeInteractiveNum());
        cmNum.setCpeMtaNum(c1.getCpeMtaNum() + c2.getCpeMtaNum());
        cmNum.setCpeNum(c1.getCpeNum() + c2.getCpeNum());
        cmNum.setAllNum(c1.getAllNum() + c2.getAllNum());
        return cmNum;
    }

    public void setLastCmNum(Map<Long, CmNum> lastCmNum) {
        this.lastCmNum = lastCmNum;
    }

    protected void addCmActionLocalFileData(PerformanceResult<OperClass> result) {
    }
}
