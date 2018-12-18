/***********************************************************************
 * $Id: CmStatusScheduler.java,v1.0 2012-7-17 上午10:32:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CpeStatusPerf;
import com.topvision.ems.cmc.performance.domain.CpeStatusPerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.common.CollectTimeRange;
import com.topvision.framework.common.CollectTimeUtil;
import com.topvision.framework.annotation.Engine;

/**
 * @author jay
 * @created @2012-7-17-上午10:32:45
 *
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cpeStatusScheduler")
public class CpeStatusScheduler extends AbstractExecScheduler<CpeStatusPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CpeStatusScheduler  entityId[" + operClass.getEntityId() + "] exec start.");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long now = System.currentTimeMillis();
            CollectTimeUtil ctu = CollectTimeUtil.getCollectTimeUtil(CollectTimeUtil.CmStatus);
            CollectTimeRange ctr = ctu.getCollectTimeRange(now);
            long dt = ctu.getCollectTime(now);
            if (logger.isDebugEnabled()) {
                logger.debug("CpeStatusScheduler startTime[" + sdf.format(new Date(ctr.getStartTimeLong()))
                        + "] endTime[" + sdf.format(new Date(ctr.getEndTimeLong())) + "] collectTime["
                        + sdf.format(new Date(dt)) + "]");
            }

            long entityId = operClass.getEntityId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            CpeStatusPerfResult cpeStatusPerfResult = new CpeStatusPerfResult(operClass);
            try {
                cpeStatusPerfResult.setCpeAttributes(snmpExecutorService.getTable(snmpParam, TopCpeAttribute.class));
            } catch (Exception e) {
                logger.debug("CpeStatusScheduler error:", e);
            }

            cpeStatusPerfResult.setEntityId(entityId);
            cpeStatusPerfResult.setDt(dt);
            logger.debug("CpeStatusScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cpeStatusPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CpeStatusScheduler exec end.");
    }
}