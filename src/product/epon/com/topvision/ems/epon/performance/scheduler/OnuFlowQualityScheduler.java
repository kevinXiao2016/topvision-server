/***********************************************************************
 * $Id: OnuFlowQualityScheduler.java,v1.0 2015-5-7 上午11:23:46 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.epon.performance.domain.OnuFlowCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuFlowQualityPerf;
import com.topvision.ems.epon.performance.domain.OnuFlowQualityResult;
import com.topvision.ems.epon.performance.engine.OnuPerfDao;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2015-5-7-上午11:23:46
 *
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("onuFlowQualityScheduler")
public class OnuFlowQualityScheduler extends AbstractExecScheduler<OnuFlowQualityPerf> {
    public static final long DEVICETYPE_ONUPON = 2L;
    public static final long DEVICETYPE_ONUUNI = 3L;
    public static final long DEVICETYPE_ONUGPON = 7L;
    public static final String GPON_ONU_TYPE = "G";

    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("OnuFlowQualityScheduler onuId[" + operClass.getOnuId() + "] exec start.");
        }
        long entityId = operClass.getEntityId();
        long onuId = operClass.getOnuId();
        try {
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            //增加在Engine端回调记录性能任务执行时间
            try {
                if (operClass.getMonitorId() != null) {
                    getCallback().recordTaskCollectTime(operClass.getMonitorId(),
                            new Timestamp(System.currentTimeMillis()));
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            //UNIINDEX和PONINDEX改为在采集时获取
            OnuPerfDao onuPerfDao = engineDaoFactory.getEngineDao(OnuPerfDao.class);
            List<Long> uniIndexList = onuPerfDao.queryUniIndexByOnuId(onuId);
            String onuType = onuPerfDao.queryOnuTypeByOnuId(onuId);
            List<Long> ponIndexList = onuPerfDao.queryPonIndexByOnuId(onuId);
            operClass.setPonIndexList(ponIndexList);
            //进行数据采集
            List<OnuFlowCollectInfo> onuFlowPerfs = new ArrayList<OnuFlowCollectInfo>();
            Timestamp collectTime = new Timestamp(System.currentTimeMillis());
            if (operClass.getUniIndexList() != null && !operClass.getUniIndexList().isEmpty()) {
                // 处理UNI口采集
                OnuFlowCollectInfo uniPerf = null;
                for (Long uniIndex : operClass.getUniIndexList()) {
                    uniPerf = new OnuFlowCollectInfo();
                    uniPerf.setPortType(PerfTargetConstants.PORTTYPE_UNI);
                    uniPerf.setPortIndex(uniIndex);
                    uniPerf.setDeviceIndex(EponIndex.getOnuPortDeviceIndex(uniIndex, DEVICETYPE_ONUUNI));
                    uniPerf.setCardIndex(0);
                    uniPerf.setDevicePortIndex(EponIndex.getUniNo(uniIndex).intValue());
                    uniPerf.setCollectTime(collectTime);
                    try {
                        uniPerf = snmpExecutorService.getTableLine(snmpParam, uniPerf);
                        onuFlowPerfs.add(uniPerf);
                    } catch (Exception e) {
                        logger.debug("Collect onu[{}] uni[{}] flow failed: {}", onuId, uniIndex, e);
                    }
                }
            }
            if (operClass.getPonIndexList() != null && !operClass.getPonIndexList().isEmpty()) {
                // 处理PON口采集
                OnuFlowCollectInfo ponPerf = null;
                for (Long ponIndex : operClass.getPonIndexList()) {
                    ponPerf = new OnuFlowCollectInfo();
                    ponPerf.setPortType(PerfTargetConstants.PORTTYPE_PON);
                    ponPerf.setPortIndex(ponIndex);
                    if (GPON_ONU_TYPE.equals(onuType)) {
                        ponPerf.setDeviceIndex(EponIndex.getOnuPortDeviceIndex(ponIndex, DEVICETYPE_ONUGPON));
                    } else {
                        ponPerf.setDeviceIndex(EponIndex.getOnuPortDeviceIndex(ponIndex, DEVICETYPE_ONUPON));
                    }
                    ponPerf.setCardIndex(0);
                    ponPerf.setDevicePortIndex(0);
                    ponPerf.setCollectTime(collectTime);
                    try {
                        ponPerf = snmpExecutorService.getTableLine(snmpParam, ponPerf);
                        onuFlowPerfs.add(ponPerf);
                    } catch (Exception e) {
                        logger.debug("Collect onu[{}] uni[{}] flow failed: {}", onuId, ponIndex, e);
                    }
                }
            }
            logger.trace("EponFlowQualityPerfScheduler write result to file.");
            OnuFlowQualityResult onuFlowQualityResult = new OnuFlowQualityResult(operClass);
            onuFlowQualityResult.setEntityId(entityId);
            onuFlowQualityResult.setOnuId(onuId);
            onuFlowQualityResult.setOnuIndex(operClass.getOnuIndex());
            onuFlowQualityResult.setOnuFlowPerfs(onuFlowPerfs);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(onuFlowQualityResult);
        } catch (Exception e) {
            logger.debug("Onu[{}] flow collect failed: {}", onuId, e);
        }
        logger.debug("EponFlowQualityPerfScheduler exec end.");
    }

}
