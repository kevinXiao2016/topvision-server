/***********************************************************************
 * $Id: OnuLinkQualityScheduler.java,v1.0 2015-4-22 下午4:23:02 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityPerf;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityResult;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.utils.EponConstants;

/**
 * @author flack
 * @created @2015-4-22-下午4:23:02
 *
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("onuLinkQualityScheduler")
public class OnuLinkQualityScheduler extends AbstractExecScheduler<OnuLinkQualityPerf> {

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("OnuLinkQualityScheduler entityId[" + operClass.getOnuId() + "] exec start.");
        }
        Timestamp collectTime = new Timestamp(System.currentTimeMillis());
        // 增加在Engine端回调记录性能任务执行时间
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
            // 从设备采集ONU链路质量数据
            OnuLinkCollectInfo linkCollectInfo = new OnuLinkCollectInfo();
            linkCollectInfo.setOnuIndex(operClass.getOnuIndex());
            linkCollectInfo.setCardIndex(0);
            linkCollectInfo.setPortIndex(0);
            /*
             * List<String> excludeOids = new ArrayList<>(); if
             * (operClass.getOnuEorG().equals(EponConstants.EPON_ONU)) {
             * excludeOids.add(OltOnuAttribute.GPON_OID_PRE); } else if
             * (operClass.getOnuEorG().equals(EponConstants.GPON_ONU)) {
             * excludeOids.add(OltOnuAttribute.EPON_OID_PRE); }
             */
            linkCollectInfo = snmpExecutorService.getTableLine(snmpParam, linkCollectInfo);
            if (linkCollectInfo.getOnuPonRevPower() != null && linkCollectInfo.getOnuPonTransPower() != null
                    && linkCollectInfo.getOltPonRevPower() != null) {
                // 传递结果，只处理采集到值的情况
                OnuLinkQualityResult linkQualityResult = new OnuLinkQualityResult(operClass);
                linkQualityResult.setEntityId(entityId);
                linkQualityResult.setOnuId(operClass.getOnuId());
                linkQualityResult.setOnuIndex(operClass.getOnuIndex());
                if (linkCollectInfo.getOnuPonRevPower() != null) {
                    if (EponConstants.RE_POWER == linkCollectInfo.getOnuPonRevPower()) {
                        linkQualityResult.setOnuPonRevPower(EponConstants.INVALID_VALUE);
                    } else {
                        linkQualityResult.setOnuPonRevPower(linkCollectInfo.getOnuPonRevPower() / 100F);
                    }
                } else {
                    linkQualityResult.setOltPonRevPower(EponConstants.INVALID_VALUE);
                }

                if (linkCollectInfo.getOnuPonTransPower() != null) {
                    if (EponConstants.TX_POWER == linkCollectInfo.getOnuPonTransPower()) {
                        linkQualityResult.setOnuPonTransPower(EponConstants.INVALID_VALUE);
                    } else {
                        linkQualityResult.setOnuPonTransPower(linkCollectInfo.getOnuPonTransPower() / 100F);
                    }
                } else {
                    linkQualityResult.setOnuPonTransPower(EponConstants.INVALID_VALUE);
                }

                if (linkCollectInfo.getOltPonRevPower() != null) {
                    if (EponConstants.RE_POWER == linkCollectInfo.getOltPonRevPower()) {
                        linkQualityResult.setOltPonRevPower(EponConstants.INVALID_VALUE);
                    } else {
                        linkQualityResult.setOltPonRevPower(linkCollectInfo.getOltPonRevPower() / 100F);
                    }
                } else {
                    linkQualityResult.setOltPonRevPower(EponConstants.INVALID_VALUE);
                }

                linkQualityResult.setCollectTime(collectTime);
                // 将结果记入磁盘文件 等待server处理
                addLocalFileData(linkQualityResult);
            }
        } catch (Exception e) {
            logger.debug("OnuLinkQualityScheduler exec failed", e);
        }
        logger.debug("OnuLinkQualityScheduler exec end.");
    }
}
