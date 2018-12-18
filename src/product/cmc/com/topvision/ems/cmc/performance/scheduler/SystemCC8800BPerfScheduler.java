/***********************************************************************
 * $Id: SystemPerfScheduler.java,v1.0 2012-7-11 下午02:13:50 $
 *
 * @author: bryan
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.performance.domain.SystemPerf;
import com.topvision.ems.cmc.performance.domain.SystemPerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author bryan
 * @created @2012-9-7-上午10:17:26
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("systemCC8800BPerfScheduler")
public class SystemCC8800BPerfScheduler extends AbstractExecScheduler<SystemPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            long cmcIndex = -1;
            if (operClass.getIfIndexs() != null && !operClass.getIfIndexs().isEmpty()) {
                cmcIndex = operClass.getIfIndexs().get(0);
            }
            logger.debug("SystemCC8800BPerfScheduler entityId[" + operClass.getEntityId() + "] cmcId["
                    + operClass.getCmcId() + "] cmcIndex[" + cmcIndex + "] exec start.");
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            long cmcIndex = operClass.getIfIndexs().get(0);
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            /*
             * 8800B无OLT的节点，只能从CmcAttribute的Domain中采集CPU、内存利用率 
            String[] oids = operClass.makeOids();
            for (int i = 0; i < oids.length; i++) {
                oids[i] = oids[i] + cmcIndex;
            }
            String[] results = exec.get(snmpParam, oids);
            int cpu = Integer.parseInt(results[0]);
            double ramRatio = Integer.parseInt(results[1]);*/
            CmcAttribute cmc = new CmcAttribute();

            //采集CmcAttribute
            cmc.setCmcIndex(cmcIndex);
            cmc = snmpExecutorService.getTableLine(snmpParam, cmc);
            cmc.setCmcEntityId(entityId);
            cmc.setCmcId(cmcId);
            cmc.setDt(new Timestamp(dt));
            //cmc.setTopCcmtsSysCPURatio(cpu);
            //cmc.setTopCcmtsSysRAMRatio((int) ramRatio);
            //处理采集结果
            SystemPerfResult sysResult = new SystemPerfResult(operClass);
            sysResult.setSystemPerf(cmc);
            sysResult.setCmcId(cmcId);
            sysResult.setEntityId(entityId);
            sysResult.setDt(dt);
            logger.debug("SystemPerfScheduler write to file waiting for Server.");
            // 将结果写入文件中，等待Server处理
            addLocalFileData(sysResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
    }
}
