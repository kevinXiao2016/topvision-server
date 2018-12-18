/***********************************************************************
 * $Id: CmStatusScheduler.java,v1.0 2012-7-17 上午10:32:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.performance.DocsIfCmtsCmStatusExt;
import com.topvision.ems.cmc.performance.domain.TopCmControl;
import com.topvision.ems.cmc.performance.facade.callback.CmcPerformanceCallback;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmPartialSvcState;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.performance.domain.CmStatusPerf;
import com.topvision.ems.cmc.performance.domain.CmStatusPerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.common.CollectTimeRange;
import com.topvision.framework.common.CollectTimeUtil;

/**
 * @author loyal
 * @created @2012-7-17-上午10:32:45
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmStatusScheduler")
public class CmStatusScheduler extends AbstractExecScheduler<CmStatusPerf> {
    private CmcPerformanceCallback cmcPerformanceCallback;
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmStatusScheduler  entityId[" + operClass.getEntityId() + "] cmcId[" + operClass.getCmcId()
                    + "] exec start.");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long now = System.currentTimeMillis();
            CollectTimeUtil ctu = CollectTimeUtil.getCollectTimeUtil(CollectTimeUtil.CmStatus);
            long dt = System.currentTimeMillis();
            CollectTimeRange ctr = null;
            try {
                ctr = ctu.getCollectTimeRange(now);
                dt = ctu.getCollectTime(now);
                if (logger.isDebugEnabled()) {
                    logger.debug("CmStatusScheduler startTime[" + sdf.format(new Date(ctr.getStartTimeLong()))
                            + "] endTime[" + sdf.format(new Date(ctr.getEndTimeLong())) + "] collectTime["
                            + sdf.format(new Date(dt)) + "]");
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            long entityId = operClass.getEntityId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            CmStatusPerfResult cmStatusPerfResult = new CmStatusPerfResult(operClass);
            try {
                List<CmAttribute> list = snmpExecutorService.getTable(snmpParam, CmAttribute.class);
                try {
                    boolean isSupportCmPreStatus = getCmcPerformanceCallback().isSupportCmPreStatus(entityId);
                    Map<Long,Integer> preStatusMap = new HashMap<>();
                    if (isSupportCmPreStatus) {
                        List<TopCmControl> preStatus =snmpExecutorService.getTable(snmpParam, TopCmControl.class);
                        if (preStatus!=null) {
                            for (TopCmControl preStatu : preStatus) {
                                preStatusMap.put(preStatu.getStatusIndex(),preStatu.getTopCmPreStatus());
                            }
                        }
                    }

                    for (CmAttribute cmAttribute : list) {
                        if (!isSupportCmPreStatus) {
                            cmAttribute.setPreStatus(TopCmControl.VERSIONNOTSUPPORTED);
                        } else {
                            if (!preStatusMap.containsKey(cmAttribute.getStatusIndex())) {
                                cmAttribute.setPreStatus(TopCmControl.NOTCOLLECTED);
                            } else {
                                cmAttribute.setPreStatus(preStatusMap.get(cmAttribute.getStatusIndex()));
                            }
                        }
                    }
                }catch (Exception e) {
                    logger.debug("CmStatusScheduler preStatus error:", e);
                }
                try {
                    boolean isSupportCmDocsisMode = getCmcPerformanceCallback().isSupportCmDocsisMode(entityId);
                    Map<Long,Integer> docsisModeMap = new HashMap<>();
                    if (isSupportCmDocsisMode) {
                        List<DocsIfCmtsCmStatusExt> docsisModes =snmpExecutorService.getTable(snmpParam, DocsIfCmtsCmStatusExt.class);
                        if (docsisModes!=null) {
                            for (DocsIfCmtsCmStatusExt docsisMode : docsisModes) {
                                docsisModeMap.put(docsisMode.getStatusIndex(), docsisMode.getDocsisMode());
                            }
                        }
                    }
                    for (CmAttribute cmAttribute : list) {
                        if (!isSupportCmDocsisMode) {
                            cmAttribute.setDocsisMode(DocsIfCmtsCmStatusExt.VERSIONNOTSUPPORTED);
                        } else {
                            if (!docsisModeMap.containsKey(cmAttribute.getStatusIndex())) {
                                cmAttribute.setDocsisMode(DocsIfCmtsCmStatusExt.NOTCOLLECTED);
                            } else {
                                cmAttribute.setDocsisMode(docsisModeMap.get(cmAttribute.getStatusIndex()));
                            }
                        }
                    }
                }catch (Exception e) {
                    logger.debug("CmStatusScheduler docsisMode error:", e);
                }
                cmStatusPerfResult.setCmAttributes(list);
                if (list.isEmpty()) {
                    cmStatusPerfResult.setCmArrayEmpty(true);
                }

                cmStatusPerfResult.setDocsIf3CmtsCmUsStatus(snmpExecutorService.getTable(snmpParam,
                        DocsIf3CmtsCmUsStatus.class));
                
                // 刷新CM partial service信息
                try{
                    List<CmPartialSvcState> states = snmpExecutorService.getTable(snmpParam, CmPartialSvcState.class);
                    cmStatusPerfResult.setPartialStates(states);
                } catch(Exception e) {
                    logger.debug("CmStatusScheduler CmPartialSvcState error:", e);
                }
            } catch (Exception e) {
                logger.debug("CmStatusScheduler error:", e);
            }

            cmStatusPerfResult.setEntityId(entityId);
            cmStatusPerfResult.setDt(dt);
            logger.debug("CmStatusScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmStatusPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CmStatusScheduler exec end.");
    }
    protected synchronized CmcPerformanceCallback getCmcPerformanceCallback() {
        if (cmcPerformanceCallback != null) {
            try {
                cmcPerformanceCallback.test();
                return cmcPerformanceCallback;
            } catch (Exception e) {
            }
        }
        try {
            cmcPerformanceCallback = getCallback(CmcPerformanceCallback.class);
            cmcPerformanceCallback.test();
            return cmcPerformanceCallback;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equalsIgnoreCase("Property 'serviceUrl' is required")) {
                throw e;
            }
            return null;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }
}
