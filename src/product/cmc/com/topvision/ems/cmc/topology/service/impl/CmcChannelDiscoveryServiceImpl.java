/***********************************************************************
 * $Id: CmcChannelDiscoveryServiceImpl.java,v1.0 2017年6月3日 上午11:16:02 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.topology.service.CmcChannelDiscoveryService;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.dao.DevicePerfTargetDao;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author vanzand
 * @created @2017年6月3日-上午11:16:02
 *
 */
@Service("cmcChannelDiscoveryService")
public class CmcChannelDiscoveryServiceImpl extends BaseService implements CmcChannelDiscoveryService {

    @Resource(name = "cmcDiscoveryDao")
    protected CmcDiscoveryDao cmcDiscoveryDao;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private DevicePerfTargetDao devicePerfTargetDao;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.topology.service.CmcChannelDiscoveryService#syncCmcChannelBaseInfo(java
     * .util.List, java.util.List, java.util.List, java.util.List, java.lang.Long)
     */
    @Override
    public void syncCmcChannelBaseInfo(List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos,
            List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos,
            List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos, List<CmcPort> cmcPorts, Long entityId) {

        Map<String, Long> channelMap = cmcDiscoveryDao.getChannelMap(entityId);
        cmcDiscoveryDao.syncCmcChannelBaseInfo(cmcUpChannelBaseInfos, cmcUpChannelSignalQualityInfos,
                cmcDownChannelBaseInfos, cmcPorts, entityId);
        // add by fanzidong @20170603, 判断是否有新发现信道，来决定是否需要更新相关perfmonitor
        syncPerfMonitor(channelMap, cmcUpChannelBaseInfos, cmcUpChannelSignalQualityInfos, cmcDownChannelBaseInfos,
                cmcPorts, entityId);
    }

    /**
     * 同步entity下的信道信息，确定是否要重建perfmonitor（如果是OLT下的，可能存在多个CC）
     * 
     * @param cmcUpChannelBaseInfos
     * @param cmcUpChannelSignalQualityInfos
     * @param cmcDownChannelBaseInfos
     * @param cmcPorts
     * @param entityId
     */
    private void syncPerfMonitor(Map<String, Long> channelMap, List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos,
            List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos,
            List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos, List<CmcPort> cmcPorts, Long entityId) {
        if (cmcPorts == null || cmcPorts.size() == 0) {
            return;
        }

        Map<Long, Boolean> hasNewChlMap = new HashMap<Long, Boolean>();

        boolean atLeastOneNewChl = false;
        // 判断是否有新发现的信道
        if (cmcUpChannelBaseInfos != null) {
            for (CmcUpChannelBaseInfo cmcUpChannelBaseInfo : cmcUpChannelBaseInfos) {
                Long cmcId = cmcUpChannelBaseInfo.getCmcId();
                if (!hasNewChlMap.containsKey(cmcId)) {
                    hasNewChlMap.put(cmcId, false);
                }
                if (hasNewChlMap.get(cmcId)) {
                    continue;
                }
                Long channelIndex = cmcUpChannelBaseInfo.getChannelIndex();
                String id_index_key = cmcId + "_" + channelIndex;
                Long cmcPortId = channelMap.get(id_index_key);
                if (cmcPortId == null) {
                    hasNewChlMap.put(cmcId, true);
                    atLeastOneNewChl = true;
                }
            }
        }

        if (cmcDownChannelBaseInfos != null) {
            for (CmcDownChannelBaseInfo cmcDownChannelBaseInfo : cmcDownChannelBaseInfos) {
                Long cmcId = cmcDownChannelBaseInfo.getCmcId();
                if (!hasNewChlMap.containsKey(cmcId)) {
                    hasNewChlMap.put(cmcId, false);
                }
                if (hasNewChlMap.get(cmcId)) {
                    continue;
                }
                Long channelIndex = cmcDownChannelBaseInfo.getChannelIndex();
                String id_index_key = cmcId + "_" + channelIndex;
                Long cmcPortId = channelMap.get(id_index_key);
                if (cmcPortId == null) {
                    // CmcPortRelation
                    hasNewChlMap.put(cmcId, true);
                    atLeastOneNewChl = true;
                }
            }
        }
        if (!atLeastOneNewChl) {
            return;
        }
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 遍历每个CC，如果有信道变化，则重启对应的perfmonitor
        // modify by fanzidong， 如果是第一次发现设备，则
        for (Long cmcId : hasNewChlMap.keySet()) {
            Boolean hasNewChl = hasNewChlMap.get(cmcId);
            if (hasNewChl) {
                // add by fanzidong， 此时需要根据devicePerfTargetCollecttime是否存在来确定对应的性能指标，没有的话应该是初次拓扑，直接忽略
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("perfTargetName", PerfTargetConstants.CMC_SNR);
                paramMap.put("entityIds", cmcId);
                List<DevicePerfTarget> currentPerfTarget = devicePerfTargetDao.queryDeviceSingleTarget(paramMap);
                if(currentPerfTarget == null || currentPerfTarget.size() == 0) {
                    continue;
                }
                
                Entity cmc = entityService.getEntity(cmcId);
                Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
                // 需要重新创建已启动的CmcFlowQuality、CmcSignalQuality
                try {
                    logger.debug("begin reCreate CmcFlowQuality");
                    cmcPerfService.stopCmcFlowQuality(cmcId, snmpParam);
                    cmcPerfService.startCmcFlowQuality(cmcId, cmcIndex, snmpParam, entityId, cmc.getTypeId());
                } catch (Exception e) {
                    logger.error("begin reCreate CmcFlowQuality failed: ", e.getMessage());
                }
                try {
                    logger.debug("begin reCreate CmcSignalQuality");
                    cmcPerfService.stopCmcSignalQuality(cmcId, snmpParam);
                    cmcPerfService.startCmcSignalQuality(cmcId, cmcIndex, snmpParam, entityId, cmc.getTypeId());
                } catch (Exception e) {
                    logger.error("begin reCreate CmcSignalQuality failed: ", e.getMessage());
                }
            }
        }

    }

}
