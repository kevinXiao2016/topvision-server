/***********************************************************************
 * $Id: CmcSignalQualityPerfScheduler.java,v1.0 2013-8-10 下午03:16:13 $
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

import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-10-下午03:16:13
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcSignalQualityPerfScheduler")
public class CmcSignalQualityPerfScheduler extends AbstractExecScheduler<CmcSignalQualityPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcSignalQualityPerfScheduler entityId[" + operClass.getCmcId() + "] exec start.");
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
            //设置信噪采集采用实时模式 topUpChannelSignalQualityRealTimeSnmpDataStatus         1 enable  2 disable
            /*
            SumaVision(config)# show running-config verbose | include real-time

             cable upstream signal-quality real-time snmp-data
             cable upstream-spectrum data-mode real-time

             SumaVision(config)# show cable upstream signal-quality
            Channel Contention Unerrored            Corrected            Uncorrectable        Upstream Mirco(dB)
                    Intervals  Codewords            Codewords            Codewords            SNR      Reflection
            1       0          1057505              0                    8                    36.6     0
            2       0          963585               1                    1                    37.9     0
            3       0          1270717              0                    20                   40.3     0
            4       0          1061965              0                    18                   40.3     0
             */
            try {
                snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.18.2.1.0", "1");
            } catch (Exception e) {
                logger.debug("set topUpChannelSignalQualityRealTimeSnmpDataStatus error", e);
            }
            long cmcId = operClass.getCmcId();
            List<CmcSignalQuality> cmcSignalQualities = new ArrayList<CmcSignalQuality>();
            try {
                Timestamp collectTime = new Timestamp(System.currentTimeMillis());
                for (Long channelIndex : operClass.getCmcChannelIndexs()) {
                    CmcSignalQuality quality = new CmcSignalQuality();
                    quality.setCmcId(cmcId);
                    quality.setCmcChannelIndex(channelIndex);
                    quality.setCollectTime(collectTime);
                    cmcSignalQualities.add(snmpExecutorService.getTableLine(snmpParam, quality));
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            logger.trace("CmcSignalQualityPerfScheduler write result to file.");
            CmcSignalQualityPerfResult cmcSignalQualityPerfResult = new CmcSignalQualityPerfResult(operClass);
            cmcSignalQualityPerfResult.setEntityId(cmcId);
            cmcSignalQualityPerfResult.setPerfs(cmcSignalQualities);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmcSignalQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CmcSignalQualityPerfScheduler exec end.");
    }
}
