/***********************************************************************
 * $Id: OnuOnlineScheduler.java,v1.0 2015-4-22 下午4:23:02 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.performance.domain.OnuOnlineCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuOnlinePerf;
import com.topvision.ems.epon.performance.domain.OnuOnlineResult;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.utils.EponConstants;

/**
 * @author flack
 * @created @2015-4-22-下午4:23:02
 *
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("onuOnlineScheduler")
public class OnuOnlineScheduler extends AbstractExecScheduler<OnuOnlinePerf> {

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("OnuOnlineScheduler  OnuId[" + operClass.getOnuId() + "] exec start.");
        }
        try {
            long entityId = operClass.getEntityId();
            Timestamp collectTime = new Timestamp(System.currentTimeMillis());
            //  增加在Engine端回调记录性能任务执行时间
            try {
                if (operClass.getMonitorId() != null) {
                    getCallback().recordTaskCollectTime(operClass.getMonitorId(), collectTime);
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            snmpParam = getCallback().getSnmpParamByEntity(entityId);
            OnuOnlineCollectInfo onuOnlineInfo = new OnuOnlineCollectInfo();
            onuOnlineInfo.setOnuIndex(operClass.getOnuIndex());
            List<String> excludeOids = new ArrayList<>();
            if (operClass.getOnuEorG().equals(EponConstants.EPON_ONU)) {
                excludeOids.add(OltOnuAttribute.GPON_OID_PRE);
            } else if (operClass.getOnuEorG().equals(GponConstant.GPON_ONU)) {
                excludeOids.add(OltOnuAttribute.EPON_OID_PRE);
            }
            //从设备采集onu在线状态
            onuOnlineInfo = snmpExecutorService.getTableLine(snmpParam, onuOnlineInfo, excludeOids);
            if (onuOnlineInfo.getOnuOperationStatus() != null) {
                //传递结果，只处理采集到值的情况
                OnuOnlineResult onlineResult = new OnuOnlineResult(operClass);
                onlineResult.setEntityId(entityId);
                onlineResult.setOnuId(operClass.getOnuId());
                onlineResult.setOnuIndex(operClass.getOnuIndex());
                onlineResult.setOnuOnlneStatus(onuOnlineInfo.getOnuOperationStatus());
                onlineResult.setCollectTime(collectTime);
                // 将结果记入磁盘文件 等待server处理
                addLocalFileData(onlineResult);
            }
        } catch (Exception e) {
            logger.debug("OnuOnlineScheduler exec failed", e);
        }
        logger.debug("OnuOnlineScheduler exec end.");
    }
}
