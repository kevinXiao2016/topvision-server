/***********************************************************************
 * $Id: OnuCatvOrInfoScheduler.java,v1.0 2016-5-9 下午2:18:12 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.epon.performance.domain.OnuCatvOrInfoEntry;
import com.topvision.ems.epon.performance.domain.OnuCatvOrInfoPerf;
import com.topvision.ems.epon.performance.domain.OnuCatvOrInfoResult;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2016-5-9-下午2:18:12
 *
 */

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("onuCatvOrInfoScheduler")
public class OnuCatvOrInfoScheduler extends AbstractExecScheduler<OnuCatvOrInfoPerf> {

    /* (non-Javadoc)
     * @see com.topvision.ems.engine.performance.AbstractExecScheduler#exec()
     */
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("OnuCatvOrInfoScheduler entityId[" + operClass.getOnuId() + "] exec start.");
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
            OnuCatvOrInfoEntry onuCatvOrInfoEntry = new OnuCatvOrInfoEntry();
            onuCatvOrInfoEntry.setEntityId(entityId);
            onuCatvOrInfoEntry.setOnuIndex(operClass.getOnuIndex());
            onuCatvOrInfoEntry = snmpExecutorService.getTableLine(snmpParam, onuCatvOrInfoEntry);
            if (onuCatvOrInfoEntry.getOnuCatvOrInfoRxPower() != null) {
                OnuCatvOrInfoResult result = new OnuCatvOrInfoResult(operClass);
                result.setEntityId(entityId);
                result.setOnuId(operClass.getOnuId());
                result.setOnuIndex(operClass.getOnuIndex());
                result.setOnuCatvOrInfoEntry(onuCatvOrInfoEntry);
                result.setCollectTime(collectTime);
                // 将结果记入磁盘文件 等待server处理
                addLocalFileData(result);
            }
        } catch (Exception e) {
            logger.debug("OnuCatvOrInfoScheduler exec failed", e);
        }
        logger.debug("OnuCatvOrInfoScheduler exec end.");
    }

}
