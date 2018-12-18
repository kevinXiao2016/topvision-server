/***********************************************************************
 * $Id: DsUserNumPerfScheduler.java,v1.0 2012-7-11 下午01:53:39 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.facade.domain.ChannelCmNumStatic;
import com.topvision.ems.cmc.performance.domain.ChannelCmNumPerf;
import com.topvision.ems.cmc.performance.domain.ChannelCmNumPerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author loyal
 * @created @2012-7-11-下午01:53:39
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("channelCmNumPerfScheduler")
public class ChannelCmNumPerfScheduler extends AbstractExecScheduler<ChannelCmNumPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("DsUserNumPerfScheduler  entityId[" + operClass.getEntityId() + "] cmcId["
                    + operClass.getCmcId() + "] exec start.");
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            List<Long> channelIndexs = operClass.getChannelIndex();
            ChannelCmNumPerfResult channelCmNumPerfResult = new ChannelCmNumPerfResult(operClass);
            for (Long channelIndex : channelIndexs) {
                ChannelCmNumStatic channelCmNumStatic = new ChannelCmNumStatic();
                channelCmNumStatic.setChannelIndex(channelIndex);
                try {
                    channelCmNumStatic = snmpExecutorService.getTableLine(snmpParam, channelCmNumStatic);
                    channelCmNumPerfResult.addChannelCmNumStatic(channelCmNumStatic);
                } catch (Exception e) {
                    logger.trace("", e);
                }
            }
            channelCmNumPerfResult.setEntityId(entityId);
            channelCmNumPerfResult.setCmcId(cmcId);
            channelCmNumPerfResult.setDt(dt);
            logger.trace("DsUserNumPerfScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(channelCmNumPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("DsUserNumPerfScheduler exec end.");
    }
}
