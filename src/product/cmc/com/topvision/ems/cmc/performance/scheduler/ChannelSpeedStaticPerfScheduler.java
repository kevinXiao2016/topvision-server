/***********************************************************************
 * $Id: ChannelSpeedStaticPerfScheduler.java,v1.0 2012-7-15 上午11:39:39 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.facade.domain.CmcDownPortMonitorDomain;
import com.topvision.ems.cmc.facade.domain.CmcUpPortMonitorDomain;
import com.topvision.ems.cmc.performance.domain.ChannelSpeedStaticPerf;
import com.topvision.ems.cmc.performance.domain.ChannelSpeedStaticPerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author loyal
 * @created @2012-7-15-上午11:39:39
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("channelSpeedStaticPerfScheduler")
public class ChannelSpeedStaticPerfScheduler extends AbstractExecScheduler<ChannelSpeedStaticPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("ChannelSpeedStaticPerfScheduler  entityId[" + operClass.getEntityId() + "] cmcId["
                    + operClass.getCmcId() + "] exec start.");
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            //List<Long> channelIndexs = operClass.getChannelIndex();
            List<Long> upChannelIndexs = operClass.getUpChannelIndex();
            List<Long> downChannelIndexs = operClass.getDownChannelIndex();
            ChannelSpeedStaticPerfResult channelSpeedStaticPerfResult = new ChannelSpeedStaticPerfResult(operClass);
            //modify by loyal 上下行信道分开采集，解决casa cmts上行信道ifOutOctets及下行信道ifInOctets采集不到问题
            /*for (Long channelIndex : channelIndexs) {
                CmcPortMonitorDomain cmcPortPerf = new CmcPortMonitorDomain();
                cmcPortPerf.setIfIndex(channelIndex);
                try {
                    cmcPortPerf = exec.getTableLine(snmpParam, cmcPortPerf);
                    channelSpeedStaticPerfResult.addCmcPortPerf(cmcPortPerf);
                } catch (Exception e) {
                    logger.trace("", e);
                }
            }*/
            for (Long channelIndex : upChannelIndexs) {
                CmcUpPortMonitorDomain cmcUpPortPerf = new CmcUpPortMonitorDomain();
                cmcUpPortPerf.setIfIndex(channelIndex);
                try {
                    cmcUpPortPerf = snmpExecutorService.getTableLine(snmpParam, cmcUpPortPerf);
                    channelSpeedStaticPerfResult.addCmcUpPortPerf(cmcUpPortPerf);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
            for (Long channelIndex : downChannelIndexs) {
                CmcDownPortMonitorDomain cmcDownPortMonitorDomain = new CmcDownPortMonitorDomain();
                cmcDownPortMonitorDomain.setIfIndex(channelIndex);
                try {
                    cmcDownPortMonitorDomain = snmpExecutorService.getTableLine(snmpParam, cmcDownPortMonitorDomain);
                    channelSpeedStaticPerfResult.addCmcDownPortPerf(cmcDownPortMonitorDomain);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }

            channelSpeedStaticPerfResult.setEntityId(entityId);
            channelSpeedStaticPerfResult.setCmcId(cmcId);
            channelSpeedStaticPerfResult.setDt(dt);
            logger.trace("ChannelSpeedStaticPerfScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(channelSpeedStaticPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("ChannelSpeedStaticPerfScheduler exec end.");
    }
}
