/***********************************************************************
 * $Id: EponLinkQualityPerfScheduler.java,v1.0 2013-8-7 下午01:38:11 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.scheduler;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.epon.performance.domain.EponLinkQualityPerf;
import com.topvision.ems.epon.performance.domain.EponLinkQualityPerfResult;
import com.topvision.ems.epon.performance.facade.OltPonOptInfoPerf;
import com.topvision.ems.epon.performance.facade.OltSniOptInfoPerf;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-7-下午01:38:11
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("eponLinkQualityPerfScheduler")
public class EponLinkQualityPerfScheduler extends AbstractExecScheduler<EponLinkQualityPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("EponLinkQualityPerfScheduler  entityId[" + operClass.getEntityId() + "] exec start.");
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
            long entityId = operClass.getEntityId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            List<OltSniOptInfoPerf> sniOptInfoPerfs = null;
            List<OltPonOptInfoPerf> ponOptInfoPerfs = null;
            try {
                sniOptInfoPerfs = snmpExecutorService.getTable(snmpParam, OltSniOptInfoPerf.class);
                ponOptInfoPerfs = snmpExecutorService.getTable(snmpParam, OltPonOptInfoPerf.class);
            } catch (Exception e) {
                logger.debug("", e);
            }
            if (sniOptInfoPerfs != null) {
                for (OltSniOptInfoPerf tmp : sniOptInfoPerfs) {
                    tmp.setEntityId(entityId);
                    tmp.setCollectTime(new Timestamp(System.currentTimeMillis()));
                }
            }
            if (ponOptInfoPerfs != null) {
                for (OltPonOptInfoPerf tmp : ponOptInfoPerfs) {
                    tmp.setEntityId(entityId);
                    tmp.setCollectTime(new Timestamp(System.currentTimeMillis()));
                }
            }
            logger.trace("EponLinkQualityPerfScheduler write result to file.");
            EponLinkQualityPerfResult eponLinkQualityPerfResult = new EponLinkQualityPerfResult(operClass);
            eponLinkQualityPerfResult.setEntityId(entityId);
            eponLinkQualityPerfResult.setPonOptInfoPerfs(ponOptInfoPerfs);
            eponLinkQualityPerfResult.setSniOptInfoPerfs(sniOptInfoPerfs);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(eponLinkQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("EponLinkQualityPerfScheduler exec end.");
    }
}
