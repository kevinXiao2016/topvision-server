/***********************************************************************
 * $Id: UsBitErrorRatePerfScheduler.java,v1.0 2012-7-13 上午09:42:02 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.domain.UsBitErrorRatePerf;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRatePerfDomain;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRatePerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author loyal
 * @created @2012-7-13-上午09:42:02
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("usBitErrorRatePerfScheduler")
public class UsBitErrorRatePerfScheduler extends AbstractExecScheduler<UsBitErrorRatePerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("UsBitErrorRatePerfScheduler entityId[" + operClass.getEntityId() + "] cmcId["
                    + operClass.getCmcId() + "] exec start.");
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            List<Long> channelIndexs = operClass.getChannelIndex();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            UsBitErrorRatePerfResult usBitErrorRatePerfResult = new UsBitErrorRatePerfResult(operClass);
            for (Long channelIndex : channelIndexs) {
                UsBitErrorRatePerfDomain usBitErrorRatePerfDomain = new UsBitErrorRatePerfDomain();
                usBitErrorRatePerfDomain.setChannelIndex(channelIndex);
                try {
                    usBitErrorRatePerfDomain = snmpExecutorService.getTableLine(snmpParam, usBitErrorRatePerfDomain);
                    usBitErrorRatePerfResult.addUsBitErrorRatePerfDomain(usBitErrorRatePerfDomain);
                } catch (Exception e) {
                    logger.trace("", e);
                }
            }

            usBitErrorRatePerfResult.setEntityId(entityId);
            usBitErrorRatePerfResult.setCmcId(cmcId);
            usBitErrorRatePerfResult.setDt(dt);
            logger.debug("UsBitErrorRatePerfScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(usBitErrorRatePerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("UsBitErrorRatePerfScheduler exec end.");
    }
}
