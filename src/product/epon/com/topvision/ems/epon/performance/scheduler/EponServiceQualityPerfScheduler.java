/***********************************************************************
 * $Id: EponServiceQualityPerfScheduler.java,v1.0 2013-8-2 上午10:44:32 $
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
import com.topvision.ems.epon.performance.domain.EponServiceQualityPerf;
import com.topvision.ems.epon.performance.domain.EponServiceQualityPertResult;
import com.topvision.ems.epon.performance.facade.OltFanPerf;
import com.topvision.ems.epon.performance.facade.OltServiceQualityPerf;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-2-上午10:44:32
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("eponServiceQualityPerfScheduler")
public class EponServiceQualityPerfScheduler extends AbstractExecScheduler<EponServiceQualityPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("EponServiceQualityPerfScheduler  entityId[" + operClass.getEntityId() + "] exec start.");
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
            List<OltServiceQualityPerf> oltServiceQualityPerfs = null;
            List<OltFanPerf> fanSpeedPerfs = null;
            try {
                oltServiceQualityPerfs = snmpExecutorService.getTable(snmpParam, OltServiceQualityPerf.class);
                fanSpeedPerfs = snmpExecutorService.getTable(snmpParam, OltFanPerf.class);
            } catch (Exception e) {
                logger.debug("", e);
            }
            if (oltServiceQualityPerfs != null) {
                for (OltServiceQualityPerf tmp : oltServiceQualityPerfs) {
                    tmp.setEntityId(entityId);
                    String oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.4.1." + tmp.getTopSysBdSlotIndex();
                    tmp.setbAttribute(Integer.parseInt(snmpExecutorService.get(snmpParam, oid)));
                    tmp.setCollectTime(new Timestamp(System.currentTimeMillis()));
                }
            }
            if (fanSpeedPerfs != null) {
                for (OltFanPerf tmp : fanSpeedPerfs) {
                    tmp.setEntityId(entityId);
                    tmp.setCollectTime(new Timestamp(System.currentTimeMillis()));
                }
            }
            logger.trace("EponServiceQualityPertResult write result to file.");
            EponServiceQualityPertResult eponServiceQualityPertResult = new EponServiceQualityPertResult(operClass);
            eponServiceQualityPertResult.setEntityId(entityId);
            eponServiceQualityPertResult.setPerfs(oltServiceQualityPerfs);
            eponServiceQualityPertResult.setFanSpeedPerfs(fanSpeedPerfs);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(eponServiceQualityPertResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("EponServiceQualityPertResult exec end.");
    }
}
