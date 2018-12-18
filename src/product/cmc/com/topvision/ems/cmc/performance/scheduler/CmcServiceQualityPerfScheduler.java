/***********************************************************************
 * $Id: CmcServiceQualityPerfScheduler.java,v1.0 2013-8-8 下午03:12:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.domain.CmcServiceQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-8-下午03:12:06
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcServiceQualityPerfScheduler")
public class CmcServiceQualityPerfScheduler extends AbstractExecScheduler<CmcServiceQualityPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcServiceQualityPerfScheduler  entityId[" + operClass.getCmcId() + "] exec start.");
        }
        // Add by Rod 增加在Engine端回调记录性能任务执行时间
        try {
            if (operClass.getMonitorId() != null) {
                getCallback()
                        .recordTaskCollectTime(operClass.getMonitorId(), new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        try {
            // long entityId = operClass.getEntityId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            CmcServiceQuality cmcServiceQuality = new CmcServiceQuality();
            cmcServiceQuality.setCmcId(operClass.getCmcId());
            cmcServiceQuality.setCmcIndex(operClass.getCmcIndex());
            cmcServiceQuality.setCollectTime(new Timestamp(System.currentTimeMillis()));
            try {
                cmcServiceQuality = snmpExecutorService.getTableLine(snmpParam, cmcServiceQuality);
            } catch (Exception e) {
                logger.debug(
                        "[CmcServiceQualityPerfScheduler] [GET CmcServiceQuality error] happens. [Probably Because {}]. [Probably need to check device using this oid]   [cmcId:{}, cmcIndex:{}].",
                        e.getMessage(), operClass.getCmcId(), operClass.getCmcIndex());
            }
            logger.trace("CmcServiceQualityPertResult write result to file.");
            CmcServiceQualityPerfResult cmcServiceQualityPerfResult = new CmcServiceQualityPerfResult(operClass);
            cmcServiceQualityPerfResult.setEntityId(operClass.getCmcId());
            cmcServiceQualityPerfResult.setPerf(cmcServiceQuality);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmcServiceQualityPerfResult);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.debug("CmcServiceQualityPertResult exec end.");
    }
}
