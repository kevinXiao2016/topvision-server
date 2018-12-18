/***********************************************************************
 * $Id: SystemPerfScheduler.java,v1.0 2012-7-11 下午02:13:50 $
 * 
 * @author: dosion
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
 * @author dosion
 * @created @2012-7-11-下午02:13:50
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("systemPerfScheduler")
public class SystemPerfScheduler extends AbstractExecScheduler<SystemPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("SystemPerfScheduler entityId[" + operClass.getEntityId() + "] cmcId[" + operClass.getCmcId()
                    + "] exec start.");
        }

        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            long cmcIndex = operClass.getIfIndexs().get(0);
            String[] oids = operClass.makeOids();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            //采集OLT的CPU内存利用率
            String[] results = snmpExecutorService.get(snmpParam, oids);
            int cpu = Integer.parseInt(results[0]);
            double totalMem = Integer.parseInt(results[1]) * 1024 * 1024;
            double freeMem = Integer.parseInt(results[2]);
            double ramRatio = (freeMem / totalMem) * 100;
            CmcAttribute cmc = new CmcAttribute();
            cmc.setCmcIndex(cmcIndex);

            cmc.setCmcIndex(cmcIndex);
            // 采集CCMTS系统信息
            cmc = snmpExecutorService.getTableLine(snmpParam, cmc);
            cmc.setCmcEntityId(entityId);
            cmc.setCmcId(cmcId);
            cmc.setDt(new Timestamp(dt));
            //使用OLT的CPU 内存利用率代替CC的CPU 内存利用率
            cmc.setTopCcmtsSysCPURatio(cpu);
            cmc.setTopCcmtsSysRAMRatio((int) ramRatio);
            //采集结果处理
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
