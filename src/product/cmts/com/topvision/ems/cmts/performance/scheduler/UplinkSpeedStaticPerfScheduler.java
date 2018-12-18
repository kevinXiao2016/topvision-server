/***********************************************************************
 * $Id: UplinkSpeedStaticPerfScheduler.java,v1.0 2012-7-15 上午11:39:39 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.scheduler;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmts.facade.domain.UplinkSpeedPerf;
import com.topvision.ems.cmts.performance.domain.UplinkSpeedStaticPerf;
import com.topvision.ems.cmts.performance.domain.UplinkSpeedStaticPerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author jay
 * @created @2012-7-15-上午11:39:39
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("uplinkSpeedStaticPerfScheduler")
public class UplinkSpeedStaticPerfScheduler extends AbstractExecScheduler<UplinkSpeedStaticPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("UplinkSpeedStaticPerfScheduler  entityId[" + operClass.getEntityId() + "] exec start.");
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            List<Long> ifIndexs = operClass.getIfIndex();
            UplinkSpeedStaticPerfResult perfResult = new UplinkSpeedStaticPerfResult(operClass);
            for (Long ifIndex : ifIndexs) {
                UplinkSpeedPerf perf = new UplinkSpeedPerf();
                perf.setIfIndex(ifIndex);
                try {
                    perf = snmpExecutorService.getTableLine(snmpParam, perf);
                    perfResult.addUplinkSpeedPerf(perf);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }

            perfResult.setEntityId(entityId);
            perfResult.setDt(dt);
            logger.debug("UplinkSpeedStaticPerfScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(perfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("UplinkSpeedStaticPerfScheduler exec end.");
    }
}