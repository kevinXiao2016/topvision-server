/***********************************************************************
 * $Id: CmcTempQualityPerfScheduler.java,v1.0 2013-9-5 上午09:50:09 $
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

import com.topvision.ems.cmc.performance.domain.CmcTempQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcTempQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-9-5-上午09:50:09
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcTempQualityPerfScheduler")
public class CmcTempQualityPerfScheduler extends AbstractExecScheduler<CmcTempQualityPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcTempQualityPerfScheduler  entityId[" + operClass.getCmcId() + "] exec start.");
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
            long cmcId = operClass.getCmcId();
            long cmcIndex = operClass.getCmcIndex();
            List<CmcTempQualityFor8800B> cmcTempQualityFor8800Bs = new ArrayList<CmcTempQualityFor8800B>();
            try {
                Timestamp collectTime = new Timestamp(System.currentTimeMillis());
                CmcTempQualityFor8800B cmcTemp = new CmcTempQualityFor8800B();
                cmcTemp.setCmcIndex(cmcIndex);
                cmcTemp.setCmcId(cmcId);
                cmcTemp.setCollectTime(collectTime);
                cmcTemp = snmpExecutorService.getTableLine(snmpParam, cmcTemp);
                if (cmcTemp != null) {
                    cmcTempQualityFor8800Bs.add(cmcTemp);
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            logger.trace("CmcTempQualityPerfScheduler write result to file.");
            CmcTempQualityPerfResult cmcTempQualityPerfResult = new CmcTempQualityPerfResult(operClass);
            cmcTempQualityPerfResult.setEntityId(cmcId);
            cmcTempQualityPerfResult.setCmcTempQualityFor8800Bs(cmcTempQualityFor8800Bs);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmcTempQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CmcTempQualityPerfScheduler exec end.");
    }
}
