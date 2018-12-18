/***********************************************************************
 * $Id: CmtsSystemPerfScheduler.java,v1.0 2012-7-11 下午02:13:50 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.scheduler;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmts.performance.domain.BSRAttribute;
import com.topvision.ems.cmts.performance.domain.CASAAttribute;
import com.topvision.ems.cmts.performance.domain.CmtsServiceQualityResult;
import com.topvision.ems.cmts.performance.domain.CmtsSystemPerf;
import com.topvision.ems.cmts.performance.domain.CmtsSystemResult;
import com.topvision.ems.cmts.performance.domain.SystemAttribute;
import com.topvision.ems.cmts.performance.domain.UBRAttribute;
import com.topvision.ems.engine.executor.CpuAndMemSnmpWorker;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.facade.callback.CpuAndMemCallBack;
import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.DeviceTypeUtils;
import com.topvision.framework.utils.EponConstants;

/**
 * @author jay
 * @created @2012-7-11-下午02:13:50
 *
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmtsSystemPerfScheduler")
public class CmtsSystemPerfScheduler extends AbstractExecScheduler<CmtsSystemPerf> {
    private static Map<Long, CpuAndMemData> cmtsMap = new ConcurrentHashMap<Long, CpuAndMemData>();

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("SystemPerfScheduler entityId[" + operClass.getEntityId() + "] exec start.");
        }
        //增加在Engine端回调记录性能任务执行时间
        try {
            if (operClass.getMonitorId() != null) {
                getCallback()
                        .recordTaskCollectTime(operClass.getMonitorId(), new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.error("CmtsSystemPerfScheduler update collecttime failed:{}", e);
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            long typeId = operClass.getTypeId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            SystemAttribute systemAttribute = snmpExecutorService.getData(snmpParam, SystemAttribute.class);
            systemAttribute.setEntityId(entityId);
            systemAttribute.setCmcId(cmcId);
            systemAttribute.setDt(new Timestamp(dt));
            //进行CPU和MEM的采集
            CmtsServiceQualityResult serviceResult = getCmtsCpuAndMem(entityId, typeId, snmpParam);
            //传递采集结果
            logger.debug("SystemPerfScheduler write to file waiting for Server.");
            CmtsSystemResult result = new CmtsSystemResult(operClass);
            result.setSystemAttribute(systemAttribute);
            result.setServiceResult(serviceResult);
            //this.setCpuAndMem(result, typeId, snmpParam);// 采集
            // 将结果写入文件中，等待Server处理
            addLocalFileData(result);
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    @SuppressWarnings("unused")
    private void setCpuAndMem(CmtsSystemResult result, Long typeId, SnmpParam snmpParam) {
        if (DeviceTypeUtils.isBSR2000(typeId)) {// BSR
            BSRAttribute bsrAttribute = snmpExecutorService.getData(snmpParam, BSRAttribute.class);
            bsrAttribute.setMemUsagePercent((double) bsrAttribute.getMemAllocated()
                    / (double) bsrAttribute.getAllocatableMem());
            result.setCpuAndMemAttribute(bsrAttribute);
        } else if (DeviceTypeUtils.isCASA(typeId)) { // CASA
            CASAAttribute casaAttribute = snmpExecutorService.getData(snmpParam, CASAAttribute.class);
            casaAttribute.setMemUsagePercent((double) casaAttribute.getMemAllocated()
                    / (double) casaAttribute.getAllocatableMem());
            result.setCpuAndMemAttribute(casaAttribute);
        } else if (DeviceTypeUtils.isUBR(typeId)) { // CISCO UBR 
            UBRAttribute ubrAttribute = snmpExecutorService.getData(snmpParam, UBRAttribute.class);
            ubrAttribute.setMemUsagePercent((double) ubrAttribute.getMemUsed()
                    / (ubrAttribute.getMemFree() + ubrAttribute.getMemUsed()));
            result.setCpuAndMemAttribute(ubrAttribute);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("cpuAndMem:" + result.getCpuAndMemAttribute());
        }
    }

    /**
     * 根据配置文件的配置采集对应设备类型的CPU和内存
     * @param entityId
     * @param typeId
     * @param snmpParam
     * @return
     */
    private CmtsServiceQualityResult getCmtsCpuAndMem(Long entityId, Long typeId, SnmpParam snmpParam) {
        CmtsServiceQualityResult serviceResult = new CmtsServiceQualityResult();
        Timestamp collectTime = new Timestamp(System.currentTimeMillis());
        serviceResult.setCollectTime(collectTime);
        serviceResult.setEntityId(entityId);
        try {
            //当本地缓存中没有包含指定的类型并且配置文件中包含指定类型时,进行配置文件的解析
            if (cmtsMap.get(typeId) == null) {
                Boolean isContained = getCallback(CpuAndMemCallBack.class).isTypeContained(EponConstants.MODULE_CMTS,
                        typeId);
                if (isContained) {
                    cmtsMap = getCallback(CpuAndMemCallBack.class).getCpuAndMemConfig(EponConstants.MODULE_CMTS);
                }
            }
            CpuAndMemData deviceData = cmtsMap.get(typeId);
            if (deviceData != null) {
                CpuAndMemData collectData = snmpExecutorService.execute(new CpuAndMemSnmpWorker<CpuAndMemData>(
                        snmpParam), deviceData);
                Map<String, Double> finalResult = collectData.getComputedMap();
                Double cpuUsed = finalResult.get("cpuUtilization");
                Double memUsed = finalResult.get("memUtilization");
                if (cpuUsed != null) {
                    serviceResult.setCpuUtilization(cpuUsed);
                } else {
                    serviceResult.setCpuUtilization(-1D);
                }
                if (memUsed != null) {
                    serviceResult.setMemUtilization(memUsed);
                } else {
                    serviceResult.setMemUtilization(-1D);
                }
                return serviceResult;
            }
        } catch (Throwable e) {
            logger.error("Collect Cmts[{}] Cpu and Mem failed: {}", entityId, e);
        }
        //当采集出现异常情况时,设置默认值返回
        serviceResult.setMemUtilization(-1D);
        serviceResult.setCpuUtilization(-1D);
        return serviceResult;
    }

}