/***********************************************************************
 * $Id: CpuAndMemSnmpWorker.java,v1.0 2015-6-24 下午4:00:52 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ql.util.express.ExpressRunner;
import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author flack
 * @created @2015-6-24-下午4:00:52
 *
 */
public class CpuAndMemSnmpWorker<T extends CpuAndMemData> extends SnmpWorker<T> {
    private static final long serialVersionUID = -612597148289797919L;

    public CpuAndMemSnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    public CpuAndMemSnmpWorker(SnmpParam snmpParam, SnmpUtil snmpUtil) {
        super(snmpParam);
        this.snmpUtil = snmpUtil;
    }

    @Override
    protected void exec() throws Exception {
        logger.info("Begin to collect cpu and mem data");
        Long entityId = snmpParam.getEntityId();
        snmpUtil.reset(snmpParam);
        try {
            Map<String, String> nodeMap = result.getNodeMap();
            Map<String, String> resultMap = result.getResultMap();
            Map<String, String> collectMap = new HashMap<String, String>();
            //进行实际OID的采集处理
            for (Entry<String, String> nodeEntry : nodeMap.entrySet()) {
                String nodeKey = nodeEntry.getKey();
                String oid = nodeEntry.getValue();
                String collectValue = snmpUtil.get(oid);
                collectMap.put(nodeKey, collectValue);
                logger.info("Cmts[{}] Node[{}] collectValue[{}]", entityId, nodeKey, collectValue);
            }
            Map<String, Double> finalResult = getComputeResult(collectMap, resultMap);
            result.setComputedMap(finalResult);
        } catch (Throwable e) {
            logger.error("Collect Device[{}] Cpu and Mem failed: {}", entityId, e);
        }
        
    }

    private Map<String, Double> getComputeResult(Map<String, String> nodeMap, Map<String, String> resultMap) {
        Map<String, Double> computeMap = new HashMap<String, Double>();
        if (nodeMap != null && resultMap != null) {
            for (Entry<String, String> resutEntry : resultMap.entrySet()) {
                String resultKey = resutEntry.getKey();
                String resultAlg = resutEntry.getValue();
                try {
                    for (Entry<String, String> entry : nodeMap.entrySet()) {
                        String key = entry.getKey();
                        Double value = Double.parseDouble((String) entry.getValue());
                        if (resultAlg.contains(key)) {
                            resultAlg = resultAlg.replace("#{" + key + "}", String.valueOf(value));
                        }
                    }
                    logger.debug(resultKey + "_" + resultAlg);
                    ExpressRunner runner = new ExpressRunner();
                    Object result = runner.execute(resultAlg, null, null, true, false);
                    computeMap.put(resultKey, (Double) result);
                } catch (Throwable e) {
                    logger.error("Parse Result failed: {}", e);
                }
            }
        }
        return computeMap;
    }

}
