/***********************************************************************
 * $Id: BaseCmtsDiscoverySnmpWorker.java,v1.0 2014-10-16 下午4:11:20 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmts.topology.domain.CmtsDiscoveryData;
import com.topvision.ems.engine.executor.CpuAndMemSnmpWorker;
import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpDataProxy;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Rod John
 * @created @2014-10-16-下午4:11:20
 * 
 */
public class BaseCmtsDiscoverySnmpWorker<T extends CmtsDiscoveryData> extends SnmpWorker<T> {
    private static final long serialVersionUID = -571233892249015855L;

    /**
     * @param snmpParam
     */
    public BaseCmtsDiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        logger.info("Begin to discover:" + snmpParam);
        snmpParam.setMibs("RFC1213-MIB,DOCS-IF-MIB,DOCS-QOS-MIB,DOCS-SUBMGT-MIB,TDOCS-IF3-MIB,CLAB-TOPO-MIB");
        /*snmpParam.setTimeout(5000);
        snmpParam.setRetry((byte) 2);*/
        snmpUtil.reset(snmpParam);
        result.setDiscoveryTime(System.currentTimeMillis());
        result.setEntityId(snmpParam.getEntityId());
        result.setIp(snmpParam.getIpAddress());
        if (!snmpUtil.verifySnmpVersion()) {
            result.setStackTrace(new SnmpException("Community Error"));
            return;
        }
        snmpParam.setVersion(snmpUtil.getVersion());
        result.setSnmpParam(snmpParam);

        // 获取基本信息（system）
        try {
            result.setSystem(snmpUtil.get(DeviceBaseInfo.class));
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology CmcPort finished!");

        // 获取上行信道基本信息
        try {
            List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = snmpUtil.getTable(CmcUpChannelBaseInfo.class, true);
            if (cmcUpChannelBaseInfos != null) {
                for (CmcUpChannelBaseInfo cmcUpChannelBaseInfo : cmcUpChannelBaseInfos) {
                    try {
                        cmcUpChannelBaseInfo.setCmtsChannelModulationProfile(cmcUpChannelBaseInfo
                                .getChannelModulationProfile());
                        Long modType = Long.parseLong(snmpUtil.getNext("1.3.6.1.2.1.10.127.1.3.5.1.4."
                                + cmcUpChannelBaseInfo.getChannelModulationProfile() + ".4"));
                        cmcUpChannelBaseInfo.setChannelModulationProfile(modType);
                    } catch (NumberFormatException e) {
                        logger.error("", e);
                    } catch (SnmpException e) {
                        logger.error("", e);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                result.setCmcUpChannelBaseInfos(cmcUpChannelBaseInfos);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology CmcUpChannelBaseInfo finished!");

        // 获取下行信道基本信息
        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = new ArrayList<CmcDownChannelBaseInfo>();
        try {
            cmcDownChannelBaseInfos = snmpUtil.getTable(CmcDownChannelBaseInfo.class, true);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (cmcDownChannelBaseInfos != null) {
            result.setCmcDownChannelBaseInfos(cmcDownChannelBaseInfos);
        }
        logger.info("topology CmcDownChannelBaseInfo finished!");

        // 获取信道基本信息（ifTable，需要判断上下行）
        try {
            List<CmcPort> cmcPorts = snmpUtil.getTable(CmcPort.class, true);
            if (cmcPorts != null) {
                result.setCmcPorts(cmcPorts);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology CmcPort finished!");

        CmcAttribute cmcAttribute = new CmcAttribute();
        Long entityId = snmpParam.getEntityId();
        try {
            cmcAttribute = getCmtsCpuAndMem(result.getCpuAndMemData(), snmpParam);
        } catch (Exception e) {
            logger.error("topology Cmts[{}] cpu and memory failed: ", entityId, e);
        }
        result.setCmcAttribute(cmcAttribute);
        logger.debug("topology CMTS cpu and memory finished!");
    }

    /**
     * 根据配置文件的配置采集对应设备类型的CPU和内存
     * @param entityId
     * @param typeId
     * @param snmpParam
     * @return
     */
    private CmcAttribute getCmtsCpuAndMem(CpuAndMemData deviceData, SnmpParam snmpParam) {
        CmcAttribute serviceResult = new CmcAttribute();
        try {
            if (deviceData != null) {
                CpuAndMemSnmpWorker<CpuAndMemData> collectWorker = new CpuAndMemSnmpWorker<CpuAndMemData>(snmpParam,
                        snmpUtil);
                SnmpDataProxy<CpuAndMemData> proxy = new SnmpDataProxy<CpuAndMemData>();
                proxy.setData(deviceData);
                collectWorker.setDataProxy(proxy);
                collectWorker.run();
                CpuAndMemData resultData = collectWorker.getDataProxy().getData();
                Map<String, Double> finalResult = resultData.getComputedMap();
                Double cpuUsed = finalResult.get("cpuUtilization");
                Double memUsed = finalResult.get("memUtilization");
                logger.info("Cmts[{}] CPU[{}] and MEM[{}]", snmpParam.getEntityId(), cpuUsed, memUsed);
                if (cpuUsed != null) {
                    serviceResult.setCpuUsed(cpuUsed);
                } else {
                    serviceResult.setCpuUsed(-1D);
                }
                if (memUsed != null) {
                    serviceResult.setMemUsed(memUsed);
                } else {
                    serviceResult.setMemUsed(-1D);
                }
                return serviceResult;
            }
        } catch (Throwable e) {
            logger.error("Collect Cmts[{}] Cpu and Mem failed: {}", snmpParam.getEntityId(), e);
        }
        //当采集出现异常情况时,设置默认值返回
        serviceResult.setCpuUsed(-1D);
        serviceResult.setMemUsed(-1D);
        return serviceResult;
    }

}
