/***********************************************************************
 * $Id: OnuCpeStatusScheduler.java,v1.0 2015-4-22 下午4:23:02 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.scheduler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onucpe.facade.OnuCpeUtil;
import com.topvision.ems.epon.performance.domain.GponOnuUniCpeList;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.epon.performance.domain.OnuCpeStatusPerf;
import com.topvision.ems.epon.performance.domain.OnuCpeStatusPerfResult;
import com.topvision.ems.epon.performance.domain.OnuUniCpeList;
import com.topvision.framework.annotation.Engine;

/**
 * @author jay
 * @created @2015-4-22-下午4:23:02
 *
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("onuCpeStatusScheduler")
public class OnuCpeStatusScheduler extends AbstractExecScheduler<OnuCpeStatusPerf> {

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("OnuCpeStatusScheduler  entityId[" + operClass.getEntityId() + "] exec start.");
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
                logger.error("", e);
            }
            OnuCpeStatusPerfResult onuCpeStatusPerfResult = new OnuCpeStatusPerfResult(operClass);
            try {
                snmpParam = getCallback().getSnmpParamByEntity(entityId);
                //从设备采集onucpe
                List<OnuUniCpeList> onuCpes = snmpExecutorService.getTable(snmpParam, OnuUniCpeList.class);
                try {
                    List<GponOnuUniCpeList> gponOnuCpes = snmpExecutorService.getTable(snmpParam, GponOnuUniCpeList.class);
                    for (GponOnuUniCpeList gponOnuCpe : gponOnuCpes) {
                        onuCpes.add(OnuCpeUtil.makeOnuUniCpeList(gponOnuCpe));
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
                onuCpeStatusPerfResult.setOnuCpes(onuCpes);
                if (onuCpes.isEmpty()) {
                    onuCpeStatusPerfResult.setArrayEmpty(true);
                } else {
                    Map<String, String> mapper = new HashMap<String, String>();
                    for (OnuUniCpeList cpeList : onuCpes) {
                        String keyOid = cpeList.getInfoSlotIndex() + "." + cpeList.getInfoPortIndex() + "."
                                + cpeList.getInfoOnuIndex();
                        String dhcpIpInfoTotal = mapper.get(keyOid);
                        if (dhcpIpInfoTotal == null) {
                            try {
                                //这部分必须try...catch。并不是所有的版本，所有的设备都支持IP-终端类型查询，只有在V1.9.0.0版本后之后走DHCH-RELAY的终端才能查询出
                                dhcpIpInfoTotal = snmpExecutorService.get(snmpParam,
                                        "1.3.6.1.4.1.32285.11.2.3.1.12.2.1.4." + keyOid);
                                mapper.put(keyOid, dhcpIpInfoTotal);
                            } catch (Exception e) {
                                logger.trace("device may not support this version", e);
                            }
                        }
                        cpeList.setDhcpIpInfoTotal(dhcpIpInfoTotal);
                    }
                }
            } catch (Exception e) {
                logger.debug("OnuCpeStatusScheduler error:", e);
            }

            onuCpeStatusPerfResult.setEntityId(entityId);
            onuCpeStatusPerfResult.setDt(collectTime.getTime());
            logger.debug("OnuCpeStatusScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(onuCpeStatusPerfResult);
        } catch (Exception e) {
            logger.error("OnuCpeStatusScheduler exec failed", e);
        }
        logger.debug("OnuCpeStatusScheduler exec end.");
    }
}
