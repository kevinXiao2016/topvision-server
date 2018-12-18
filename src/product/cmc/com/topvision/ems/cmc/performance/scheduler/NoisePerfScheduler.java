/***********************************************************************
 * $ NoisePerfScheduler.java,v1.0 2012-5-3 15:28:53 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.snmp4j.smi.OID;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.domain.NoisePerf;
import com.topvision.ems.cmc.performance.domain.NoisePerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author jay
 * @created @2012-5-3-15:28:53
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("noisePerfScheduler")
public class NoisePerfScheduler extends AbstractExecScheduler<NoisePerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("NoisePerfScheduler entityId[" + operClass.getEntityId() + "] cmcId[" + operClass.getCmcId()
                    + "] exec start.");
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            String[] oids = operClass.makeOids();
            logger.debug("NoisePerfScheduler collect oids.");
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            String[] results = snmpExecutorService.get(snmpParam, oids);
            Map<Long, Integer> noises = new HashMap<Long, Integer>();
            for (int i = 0; i < oids.length; i++) {
                String oid = oids[i];
                String result = results[i];
                OID o = new OID(oid);
                Long ifIndex = o.getUnsigned(o.getValue().length - 1);
                int noise = Integer.parseInt(result);
                noises.put(ifIndex, noise);
            }
            NoisePerfResult noisePerfResult = new NoisePerfResult(operClass);
            noisePerfResult.setEntityId(entityId);
            noisePerfResult.setCmcId(cmcId);
            noisePerfResult.setNoises(noises);
            noisePerfResult.setDt(dt);
            logger.debug("NoisePerfScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(noisePerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("NoisePerfScheduler exec end.");
    }
}
