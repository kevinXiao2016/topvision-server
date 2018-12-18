/***********************************************************************
 * $Id: CmcFlowQualityPerfScheduler.java,v1.0 2013-8-13 下午02:57:56 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.performance.domain.CmcFlowQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcFlowQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcChannelCommonTable;
import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;

/**
 * @author Rod John
 * @created @2013-8-13-下午02:57:56
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcFlowQualityPerfScheduler")
public class CmcFlowQualityPerfScheduler extends AbstractExecScheduler<CmcFlowQualityPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcFlowQualityPerfScheduler entityId[" + operClass.getCmcId() + "] exec start.");
        }
        // Add by Rod 增加在Engine端回调记录性能任务执行时间
        try {
            if (operClass.getMonitorId() != null) {
                getCallback()
                        .recordTaskCollectTime(operClass.getMonitorId(), new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            // long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            List<CmcFlowQuality> cmcFlowQualities = new ArrayList<CmcFlowQuality>();
            try {
                Timestamp collectTime = new Timestamp(System.currentTimeMillis());
                ArrayList<Long> indexList = new ArrayList<Long>();
                for (Long ifIndex : operClass.getIfIndex()) {
                    //对广州发现的domain中index重复导致采集重复数据在dbsaver中出现问题，此处对ifindex进行重复检查
                    if (indexList.contains(ifIndex)) {
                        continue;
                    } else {
                        indexList.add(ifIndex);
                    }
                    CmcFlowQuality quality = new CmcFlowQuality();
                    quality.setIfIndex(ifIndex);
                    quality.setCollectTime(collectTime);
                    quality.setCmcId(cmcId);
                    try {
                        // modify by loyal eqam surport
                        CmcFlowQuality qualityCollect = snmpExecutorService.getTableLine(snmpParam, quality);
                        if (CmcPort.IF_TYPE_EQAM.equals(qualityCollect.getIfType())) {
                            // 采集利用率
                            CmcChannelCommonTable cmcChannelCommonTable = new CmcChannelCommonTable();
                            cmcChannelCommonTable.setIfIndex(ifIndex);
                            cmcChannelCommonTable = snmpExecutorService.getTableLine(snmpParam, cmcChannelCommonTable);
                            qualityCollect.setQamChannelCommonUtilization(cmcChannelCommonTable
                                    .getQamChannelCommonUtilization());
                        }
                        cmcFlowQualities.add(qualityCollect);
                    } catch (SnmpNoSuchInstanceException e) {
                        logger.info("", e);
                    }
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            logger.trace("CmcFlowQualityPerfScheduler write result to file.");
            CmcFlowQualityPerfResult flowQualityPerfResult = new CmcFlowQualityPerfResult(operClass);
            flowQualityPerfResult.setEntityId(cmcId);
            flowQualityPerfResult.setFlowQualities(cmcFlowQualities);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(flowQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CmcFlowQualityPerfScheduler exec end.");
    }
}
