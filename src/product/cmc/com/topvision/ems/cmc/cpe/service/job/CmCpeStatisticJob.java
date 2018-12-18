/***********************************************************************
 * $Id: CmCpeStatisticJob.java,v1.0 2013-12-7 上午11:21:51 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.service.job;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.topvision.ems.cmc.cpe.dao.CpeAnalyseDao;
import com.topvision.ems.cmc.cpe.service.CpeAnalyseService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.domain.CpeCollectConfig;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.InitialDataCmAction;
import com.topvision.ems.cmc.performance.domain.InitialDataCpeAction;
import com.topvision.ems.cmc.performance.handle.UnusualCmNumberHandle;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.service.PerformanceStatistics;
import com.topvision.framework.common.CollectTimeRange;
import com.topvision.framework.common.CollectTimeUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Victor
 * @created @2013-12-7-上午11:21:51
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CmCpeStatisticJob extends AbstractJob {
    public static String CMTS_UNUSUALCMNUMBER_FLAG = "CMTS_UNUSUAL_CMNUM";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        CpeAnalyseService cpeAnalyseService = (CpeAnalyseService) jobDataMap.get("cpeAnalyseService");
        CpeAnalyseDao cpeAnalyseDao = (CpeAnalyseDao) jobDataMap.get("cpeAnalyseDao");
        CpeService cpeService = (CpeService) jobDataMap.get("cpeService");
        EntityService entityService = (EntityService) jobDataMap.get("entityService");
        EntityTypeService entityTypeService = (EntityTypeService) jobDataMap.get("entityTypeService");
        PerformanceStatistics performanceStatistics = (PerformanceStatistics) jobDataMap.get("performanceStatistics");
        Map<Long, CmNum> lastCmNum = cpeAnalyseService.getLastCmNum();
        Long endTime = (Long) jobDataMap.get("endTime");
        boolean b = false;
        try {
            b = cpeAnalyseDao.isAllCmMonitorHasCollect(endTime);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (logger.isTraceEnabled()) {
            logger.trace("cmCpeStatisticWorker.isAllCmMonitorHasCollect[" + sdf.format(endTime) + "][" + b + "]");
        }
        try {
            if (b) {
                Long maxCollectTime = cpeAnalyseDao.getCmMonitorMaxCollectTime();
                if (maxCollectTime != null) {
                    endTime = maxCollectTime + 2000;
                    jobDataMap.put("endTime", endTime);
                    CmCollectConfig cmCollectConfig = cpeService.getCmCollectConfig();
                    CpeCollectConfig cpeCollectConfig = cpeService.getCpeCollectConfig();
                    CollectTimeUtil ctu = CollectTimeUtil.getCollectTimeUtil(CollectTimeUtil.CmStatus);
                    CollectTimeRange ctr = ctu.getCollectTimeRange(maxCollectTime);
                    long collectTime = ctu.getCollectTime(maxCollectTime);
                    if (logger.isDebugEnabled()) {
                        logger.debug("CpeAnalyseServiceImpl now["
                                + sdf.format(new Date(maxCollectTime))
                                + "] startTime["
                                + sdf.format(new Date(ctr.getStartTimeLong()))
                                + "] endTime["
                                + sdf.format(new Date(ctr.getEndTimeLong()))
                                + "] collectTime["
                                + sdf.format(new Date(collectTime))
                                + "] ctu.getStartTime["
                                + sdf.format(new Date(ctu.getStartTime()))
                                + "] ctu.period["
                                + ctu.getPeriod()
                                + "] range["
                                + (maxCollectTime - ctu.getStartTime())
                                + "] offset["
                                + (maxCollectTime - ctu.getStartTime())
                                % ctu.getPeriod()
                                + "] st["
                                + (maxCollectTime - (maxCollectTime - ctu.getStartTime()) % ctu.getPeriod()
                                        - ctu.getPeriod() - 1000L)
                                + "] re["
                                + ((maxCollectTime - (maxCollectTime - ctu.getStartTime()) % ctu.getPeriod()
                                        - ctu.getPeriod() - 1000L) + ctu.getPeriod() / 2) + "]");
                    }
                    if (ctr.getStartTimeLong() < ctr.getEndTimeLong()) {
                        List<InitialDataCmAction> initialDataCmActions = cpeAnalyseDao
                                .getInitialDataCmActionByTimeRange(ctr);
                        Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap = null;
                        if (CmCollectConfig.START.intValue() == cmCollectConfig.getCmTypeStatisticStatus().intValue()
                                || CpeCollectConfig.START.intValue() == cpeCollectConfig.getCpeNumStatisticStatus()
                                        .intValue()) {
                            initialDataCpeActionMap = cpeAnalyseDao.getInitialDataCpeActionByTimeRange(ctr);
                        }
                        boolean typeStatistic = CmCollectConfig.START.intValue() == cmCollectConfig
                                .getCmTypeStatisticStatus().intValue();
                        boolean cpeNumStatistic = CmCollectConfig.START.intValue() == cpeCollectConfig
                                .getCpeNumStatisticStatus().intValue();
                        Map<Long, Map<Long, Map<Long, CmNum>>> upMap = new HashMap<Long, Map<Long, Map<Long, CmNum>>>();
                        Map<Long, Map<Long, Map<Long, CmNum>>> downMap = new HashMap<Long, Map<Long, Map<Long, CmNum>>>();
                        Map<Long, Map<Long, CmNum>> cmtsMap = new HashMap<Long, Map<Long, CmNum>>();
                        Map<Long, Map<Long, CmNum>> ponMap = new HashMap<Long, Map<Long, CmNum>>();
                        Map<Long, CmNum> deviceMap = new HashMap<Long, CmNum>();
                        Map<Long, Set<Long>> cmMacMap = new HashMap<Long, Set<Long>>();
                        for (InitialDataCmAction initialDataCmAction : initialDataCmActions) {
                            try {
                                Set<Long> macSet;
                                if (cmMacMap.containsKey(initialDataCmAction.getEntityId())) {
                                    macSet = cmMacMap.get(initialDataCmAction.getEntityId());
                                } else {
                                    macSet = new HashSet<Long>();
                                    cmMacMap.put(initialDataCmAction.getEntityId(), macSet);
                                }
                                if (macSet.contains(initialDataCmAction.getCmmac())) {
                                    continue;
                                } else {
                                    macSet.add(initialDataCmAction.getCmmac());
                                }
                                addUpCmNum(upMap, initialDataCmAction, initialDataCpeActionMap, typeStatistic,
                                        cpeNumStatistic, ctr);
                                addDownCmNum(downMap, initialDataCmAction, initialDataCpeActionMap, typeStatistic,
                                        cpeNumStatistic, ctr);
                                addCmtsCmNum(cmtsMap, initialDataCmAction, initialDataCpeActionMap, typeStatistic,
                                        cpeNumStatistic, ctr);
                                addPonCmNum(ponMap, initialDataCmAction, initialDataCpeActionMap, typeStatistic,
                                        cpeNumStatistic, ctr);
                                addDeviceCmNum(deviceMap, initialDataCmAction, initialDataCpeActionMap, typeStatistic,
                                        cpeNumStatistic, ctr);
                            } catch (Exception e) {
                                logger.error("", e);
                            }
                        }
                        // 删除掉lastCmNum中所有不在entity表中的数据
                        @SuppressWarnings("unused")
                        List<Long> ids = new ArrayList<Long>();
                        List<Long> onlineIds = cpeService.getAllOnlineIds();
                        for (Iterator<Long> iterator = lastCmNum.keySet().iterator(); iterator.hasNext();) {
                            Long entityId = iterator.next();
                            if (!onlineIds.contains(entityId)) {
                                cpeAnalyseDao.deleteDeviceCmNumLast(entityId);
                                cpeAnalyseDao.deleteCmtsCmNumLast(entityId);
                                cpeAnalyseDao.deletePonLastCmNum(entityId);
                                cpeAnalyseDao.deletePortCmNumLast(entityId);
                                iterator.remove();
                            } else {
                                CmNum cmNum = lastCmNum.get(entityId);
                                cmNum.setTime(ctr.getEndTime());
                                cmNum.setRealTime(new Timestamp(collectTime));
                                lastCmNum.put(entityId, cmNum);
                            }
                        }
                        // 将本次的结果放到lastCmNum里面
                        lastCmNum.putAll(deviceMap);
                        cpeAnalyseDao.insertDeviceCmNum(lastCmNum);
                        cpeAnalyseDao.insertPonCmNum(ponMap);
                        cpeAnalyseDao.insertCmtsCmNum(cmtsMap);
                        cpeAnalyseDao.insertDownCmNum(downMap);
                        cpeAnalyseDao.insertUpCmNum(upMap);
                        // 存入last表
                        cpeAnalyseDao.insertDeviceCmNumLast(lastCmNum);
                        cpeAnalyseDao.insertPonCmNumLast(ponMap);
                        cpeAnalyseDao.insertCmtsCmNumLast(cmtsMap);
                        cpeAnalyseDao.insertPortCmNumLast(downMap, upMap);
                        // 计算全网用户数
                        CmNum all = makeAllCmNum(lastCmNum);
                        cpeAnalyseDao.insertAllCmNum(all);
                        cpeAnalyseDao.staticCpeNum();
                        // TODO 还未提供Area的支持
                        // modify lzt
                        // 下面是异常CM阈值告警部分代码
                        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();
                        for (Long entityId : cmtsMap.keySet()) {
                            Map<Long, CmNum> entityMap = cmtsMap.get(entityId);
                            Long type = entityService.getEntity(entityId).getTypeId();
                            if (entityTypeService.isCmts(type)) {
                                for (Long ccIfIndex : entityMap.keySet()) {
                                    CmNum cmNum = entityMap.get(ccIfIndex);
                                    if (cmNum != null) {
                                        cmNum.setEntityId(entityId);
                                        cmNum.setCcIfIndex(ccIfIndex);
                                        PerformanceData numData = new PerformanceData(entityId,
                                                CMTS_UNUSUALCMNUMBER_FLAG, cmNum);
                                        perfDataList.add(numData);
                                    }
                                }
                            } else {
                                for (Long ccIfIndex : entityMap.keySet()) {
                                    CmNum cmNum = entityMap.get(ccIfIndex);
                                    if (cmNum != null) {
                                        cmNum.setEntityId(entityId);
                                        cmNum.setCcIfIndex(ccIfIndex);
                                        PerformanceData numData = new PerformanceData(entityId,
                                                UnusualCmNumberHandle.CC_UNUSUALCMNUMBER_FLAG, cmNum);
                                        perfDataList.add(numData);
                                    }
                                }
                            }

                        }
                        if (perfDataList.size() > 0) {
                            performanceStatistics.sendPerfomaceResult(perfDataList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private CmNum makeAllCmNum(Map<Long, CmNum> lastCmNum) {
        CmNum re = new CmNum();
        for (Long aLong : lastCmNum.keySet()) {
            CmNum cmNum = lastCmNum.get(aLong);
            re.setAllNum(re.getAllNum() + cmNum.getAllNum());
            re.setOnlineNum(re.getOnlineNum() + cmNum.getOnlineNum());
            re.setOfflineNum(re.getOfflineNum() + cmNum.getOfflineNum());
            re.setOtherNum(re.getOtherNum() + cmNum.getOtherNum());
            re.setIntegratedNum(re.getIntegratedNum() + cmNum.getIntegratedNum());
            re.setInteractiveNum(re.getInteractiveNum() + cmNum.getInteractiveNum());
            re.setBroadbandNum(re.getBroadbandNum() + cmNum.getBroadbandNum());
            re.setMtaNum(re.getMtaNum() + cmNum.getMtaNum());
            re.setCpeInteractiveNum(re.getCpeInteractiveNum() + cmNum.getCpeInteractiveNum());
            re.setCpeBroadbandNum(re.getCpeBroadbandNum() + cmNum.getCpeBroadbandNum());
            re.setCpeMtaNum(re.getCpeMtaNum() + cmNum.getCpeMtaNum());
            re.setCpeNum(re.getCpeNum() + cmNum.getCpeNum());
        }
        return re;
    }

    @SuppressWarnings("unused")
    private void addAllCmNum(CmNum cmNum, InitialDataCmAction initialDataCmAction,
            Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap, boolean typeStatistic,
            boolean cpeNumStatistic) {
        makeCmNum(cmNum, initialDataCmAction, initialDataCpeActionMap, typeStatistic, cpeNumStatistic);
    }

    @SuppressWarnings("unused")
    private void addAreaCmNum(Map<Long, CmNum> map, InitialDataCmAction initialDataCmAction,
            Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap, boolean typeStatistic,
            boolean cpeNumStatistic, CollectTimeRange ctr) {
        CmNum upCmNum;
        if (map.containsKey(initialDataCmAction.getUpChannelIfIndex())) {
            upCmNum = map.get(initialDataCmAction.getUpChannelIfIndex());
        } else {
            upCmNum = new CmNum();
            upCmNum.setRealTimeLong(initialDataCmAction.getRealtimeLong());
            upCmNum.setTimeLong(ctr.getEndTimeLong());
            map.put(initialDataCmAction.getUpChannelIfIndex(), upCmNum);
        }
        makeCmNum(upCmNum, initialDataCmAction, initialDataCpeActionMap, typeStatistic, cpeNumStatistic);
    }

    private void makeCmNum(CmNum cmNum, InitialDataCmAction initialDataCmAction,
            Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap, boolean typeStatistic,
            boolean cpeNumStatistic) {
        if (CmAttribute.isCmOnline(initialDataCmAction.getState())) {
            cmNum.addOnline();
        } else if (CmAttribute.isCmOffline(initialDataCmAction.getState())) {
            cmNum.addOffline();
        } else {
            cmNum.addOther();
        }
        // 判断是否在线
        if (CmAttribute.isCmOnline(initialDataCmAction.getState())) {
            if (typeStatistic || cpeNumStatistic) {
                if (initialDataCpeActionMap != null
                        && initialDataCpeActionMap.containsKey(initialDataCmAction.getCmmac())) {
                    Map<Long, InitialDataCpeAction> list = initialDataCpeActionMap.get(initialDataCmAction.getCmmac());
                    boolean mta = false;
                    boolean broadband = false;
                    boolean interactive = false;
                    List<Long> cpeMac = new ArrayList<Long>();
                    for (InitialDataCpeAction initialDataCpeAction : list.values()) {
                        if (initialDataCmAction.getEntityId().intValue() == initialDataCpeAction.getEntityId()
                                .intValue()) {
                            if (typeStatistic) {
                                if (InitialDataCpeAction.BROADBAND.intValue() == initialDataCpeAction.getCpetype()
                                        .intValue()) {
                                    broadband = true;
                                    cmNum.addCpeBroadbandNum();
                                } else if (InitialDataCpeAction.MTA.intValue() == initialDataCpeAction.getCpetype()
                                        .intValue()) {
                                    mta = true;
                                    cmNum.addCpeMtaNum();
                                } else if (InitialDataCpeAction.INTERACTIVE.intValue() == initialDataCpeAction
                                        .getCpetype().intValue()) {
                                    interactive = true;
                                    cmNum.addCpeInteractiveNum();
                                }
                            }
                            if (cpeNumStatistic) {
                                if (!cpeMac.contains(initialDataCpeAction.getCpemac())) {
                                    cpeMac.add(initialDataCpeAction.getCpemac());
                                    cmNum.addCpeNum();
                                }
                            }
                        }
                    }
                    if (mta && !broadband && !interactive) {
                        cmNum.addMtaNum();
                    } else if (broadband && !interactive && !mta) {
                        cmNum.addBroadbandNum();
                    } else if (interactive && !broadband && !mta) {
                        cmNum.addInteractiveNum();
                    } else {
                        cmNum.addIntegratedNum();
                    }
                } else {
                    cmNum.addBroadbandNum();
                }
            }
        }
    }

    private void addDeviceCmNum(Map<Long, CmNum> map, InitialDataCmAction initialDataCmAction,
            Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap, boolean typeStatistic,
            boolean cpeNumStatistic, CollectTimeRange ctr) {
        CmNum upCmNum;
        if (map.containsKey(initialDataCmAction.getEntityId())) {
            upCmNum = map.get(initialDataCmAction.getEntityId());
        } else {
            upCmNum = new CmNum();
            upCmNum.setRealTimeLong(initialDataCmAction.getRealtimeLong());
            upCmNum.setTimeLong(ctr.getEndTimeLong());
            map.put(initialDataCmAction.getEntityId(), upCmNum);
        }
        makeCmNum(upCmNum, initialDataCmAction, initialDataCpeActionMap, typeStatistic, cpeNumStatistic);
    }

    private void addCmtsCmNum(Map<Long, Map<Long, CmNum>> map, InitialDataCmAction initialDataCmAction,
            Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap, boolean typeStatistic,
            boolean cpeNumStatistic, CollectTimeRange ctr) {
        Map<Long, CmNum> ccmtsMap;
        if (map.containsKey(initialDataCmAction.getEntityId())) {
            ccmtsMap = map.get(initialDataCmAction.getEntityId());
        } else {
            ccmtsMap = new HashMap<Long, CmNum>();
            map.put(initialDataCmAction.getEntityId(), ccmtsMap);
        }

        CmNum upCmNum;
        if (ccmtsMap.containsKey(initialDataCmAction.getCcmtsIndex())) {
            upCmNum = ccmtsMap.get(initialDataCmAction.getCcmtsIndex());
        } else {
            upCmNum = new CmNum();
            upCmNum.setRealTimeLong(initialDataCmAction.getRealtimeLong());
            upCmNum.setTimeLong(ctr.getEndTimeLong());
            ccmtsMap.put(initialDataCmAction.getCcmtsIndex(), upCmNum);
        }
        makeCmNum(upCmNum, initialDataCmAction, initialDataCpeActionMap, typeStatistic, cpeNumStatistic);
    }

    private void addPonCmNum(Map<Long, Map<Long, CmNum>> map, InitialDataCmAction initialDataCmAction,
            Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap, boolean typeStatistic,
            boolean cpeNumStatistic, CollectTimeRange ctr) {
        Map<Long, CmNum> ponMap;
        if (map.containsKey(initialDataCmAction.getEntityId())) {
            ponMap = map.get(initialDataCmAction.getEntityId());
        } else {
            ponMap = new HashMap<Long, CmNum>();
            map.put(initialDataCmAction.getEntityId(), ponMap);
        }

        CmNum upCmNum;
        Long ponIndex = EponIndex.getPonIndex(CmcIndexUtils.getSlotNo(initialDataCmAction.getCcmtsIndex()).intValue(),
                CmcIndexUtils.getPonNo(initialDataCmAction.getCcmtsIndex()).intValue());
        if (ponMap.containsKey(ponIndex)) {
            upCmNum = ponMap.get(ponIndex);
        } else {
            upCmNum = new CmNum();
            upCmNum.setRealTimeLong(initialDataCmAction.getRealtimeLong());
            upCmNum.setTimeLong(ctr.getEndTimeLong());
            ponMap.put(ponIndex, upCmNum);
        }
        makeCmNum(upCmNum, initialDataCmAction, initialDataCpeActionMap, typeStatistic, cpeNumStatistic);
    }

    private void addDownCmNum(Map<Long, Map<Long, Map<Long, CmNum>>> downMap, InitialDataCmAction initialDataCmAction,
            Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap, boolean typeStatistic,
            boolean cpeNumStatistic, CollectTimeRange ctr) {
        Map<Long, Map<Long, CmNum>> deviceMap;
        if (downMap.containsKey(initialDataCmAction.getEntityId())) {
            deviceMap = downMap.get(initialDataCmAction.getEntityId());
        } else {
            deviceMap = new HashMap<Long, Map<Long, CmNum>>();
            downMap.put(initialDataCmAction.getEntityId(), deviceMap);
        }
        Map<Long, CmNum> ccmtsMap;
        if (deviceMap.containsKey(initialDataCmAction.getCcmtsIndex())) {
            ccmtsMap = deviceMap.get(initialDataCmAction.getCcmtsIndex());
        } else {
            ccmtsMap = new HashMap<Long, CmNum>();
            deviceMap.put(initialDataCmAction.getCcmtsIndex(), ccmtsMap);
        }

        CmNum num;
        if (ccmtsMap.containsKey(initialDataCmAction.getDownChannelIfIndex())) {
            num = ccmtsMap.get(initialDataCmAction.getDownChannelIfIndex());
        } else {
            num = new CmNum();
            num.setRealTimeLong(initialDataCmAction.getRealtimeLong());
            num.setTimeLong(ctr.getEndTimeLong());
            ccmtsMap.put(initialDataCmAction.getDownChannelIfIndex(), num);
        }
        makeCmNum(num, initialDataCmAction, initialDataCpeActionMap, typeStatistic, cpeNumStatistic);
    }

    private void addUpCmNum(Map<Long, Map<Long, Map<Long, CmNum>>> upMap, InitialDataCmAction initialDataCmAction,
            Map<Long, Map<Long, InitialDataCpeAction>> initialDataCpeActionMap, boolean typeStatistic,
            boolean cpeNumStatistic, CollectTimeRange ctr) {
        Map<Long, Map<Long, CmNum>> deviceMap;
        if (upMap.containsKey(initialDataCmAction.getEntityId())) {
            deviceMap = upMap.get(initialDataCmAction.getEntityId());
        } else {
            deviceMap = new HashMap<Long, Map<Long, CmNum>>();
            upMap.put(initialDataCmAction.getEntityId(), deviceMap);
        }
        Map<Long, CmNum> ccmtsMap;
        if (deviceMap.containsKey(initialDataCmAction.getCcmtsIndex())) {
            ccmtsMap = deviceMap.get(initialDataCmAction.getCcmtsIndex());
        } else {
            ccmtsMap = new HashMap<Long, CmNum>();
            deviceMap.put(initialDataCmAction.getCcmtsIndex(), ccmtsMap);
        }

        CmNum upCmNum;
        if (ccmtsMap.containsKey(initialDataCmAction.getUpChannelIfIndex())) {
            upCmNum = ccmtsMap.get(initialDataCmAction.getUpChannelIfIndex());
        } else {
            upCmNum = new CmNum();
            upCmNum.setRealTimeLong(initialDataCmAction.getRealtimeLong());
            upCmNum.setTimeLong(ctr.getEndTimeLong());
            ccmtsMap.put(initialDataCmAction.getUpChannelIfIndex(), upCmNum);
        }
        makeCmNum(upCmNum, initialDataCmAction, initialDataCpeActionMap, typeStatistic, cpeNumStatistic);
    }
}
