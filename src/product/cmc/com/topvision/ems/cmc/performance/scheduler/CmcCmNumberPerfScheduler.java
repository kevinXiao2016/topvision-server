/***********************************************************************
 * $Id: CmcCmNumberPerfScheduler.java,v1.0 2013-8-12 下午01:55:04 $
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

import com.topvision.ems.cmc.performance.domain.CmcCmNumberPerf;
import com.topvision.ems.cmc.performance.domain.CmcCmNumberPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcCmNumber;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-12-下午01:55:04
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcCmNumberPerfScheduler")
public class CmcCmNumberPerfScheduler extends AbstractExecScheduler<CmcCmNumberPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcCmNumberPerfScheduler entityId[" + operClass.getCmcId() + "] exec start.");
        }
        try {
            // long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            List<CmcCmNumber> cmcCmNumbers = new ArrayList<CmcCmNumber>();
            try {
                for (Long channelIndex : operClass.getChannelIndex()) {
                    CmcCmNumber cmNumber = new CmcCmNumber();
                    cmNumber.setCmcId(cmcId);
                    cmNumber.setChannelIndex(channelIndex);
                    cmNumber.setCollectTime(new Timestamp(System.currentTimeMillis()));
                    cmcCmNumbers.add(snmpExecutorService.getTableLine(snmpParam, cmNumber));
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            logger.trace("CmcLinkQualityPerfSchedual write result to file.");
            CmcCmNumberPerfResult numberPerfResult = new CmcCmNumberPerfResult(operClass);
            numberPerfResult.setEntityId(cmcId);
            numberPerfResult.setCmcCmNumbers(cmcCmNumbers);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(numberPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CmcCmNumberPerfScheduler exec end.");
    }
}
