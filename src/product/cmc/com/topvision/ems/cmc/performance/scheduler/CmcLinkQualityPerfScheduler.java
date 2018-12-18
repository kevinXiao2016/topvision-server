/***********************************************************************
 * $Id: CmcLinkQualityPerfScheduler.java,v1.0 2013-8-12 上午09:29:49 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.domain.CmcLinkQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcLinkQuality;
import com.topvision.ems.cmc.performance.facade.CmcLinkQualityFor8800A;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-12-上午09:29:49
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcLinkQualityPerfScheduler")
public class CmcLinkQualityPerfScheduler extends AbstractExecScheduler<CmcLinkQualityPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcLinkQualityPerfScheduler entityId[" + operClass.getCmcId() + "] exec start.");
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
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            List<CmcLinkQuality> cmcLinkQualities = null;
            CmcLinkQualityFor8800A cmcLinkQualityFor8800A = null;
            try {
                if (operClass.getCcmtSwithoutAgent()) {
                    cmcLinkQualityFor8800A = new CmcLinkQualityFor8800A();
                    cmcLinkQualityFor8800A.setOnuIndex(operClass.getOnuIndex());
                    cmcLinkQualityFor8800A = snmpExecutorService.getTableLine(snmpParam, cmcLinkQualityFor8800A);
                } else {
                    cmcLinkQualities = snmpExecutorService.getTable(snmpParam, CmcLinkQuality.class);
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            logger.trace("CmcLinkQualityPerfScheduler write result to file.");
            long cmcId = operClass.getCmcId();

            if (cmcLinkQualities != null) {
                for (CmcLinkQuality quality : cmcLinkQualities) {
                    quality.setCmcId(cmcId);
                    quality.setCmcIndex(operClass.getCmcIndex());
                    quality.setCollectTime(new Timestamp(System.currentTimeMillis()));
                }
            }
            if (cmcLinkQualityFor8800A != null) {
                cmcLinkQualityFor8800A.setCmcId(cmcId);
                cmcLinkQualityFor8800A.setCollectTime(new Timestamp(System.currentTimeMillis()));
            }
            CmcLinkQualityPerfResult cmcLinkQualityPerfResult = new CmcLinkQualityPerfResult(operClass);
            cmcLinkQualityPerfResult.setEntityId(cmcId);
            cmcLinkQualityPerfResult.setPerfs(cmcLinkQualities);
            cmcLinkQualityPerfResult.setCmcLinkQualityFor8800A(cmcLinkQualityFor8800A);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmcLinkQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CmcLinkQualityPerfScheduler exec end.");
    }
}
