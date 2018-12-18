/***********************************************************************
 * $Id: CmcDiscoveryServiceImpl.java,v1.0 2011-11-13 下午01:46:45 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CmRefreshDao;
import com.topvision.ems.cmc.config.service.CmcConfigService;
import com.topvision.ems.cmc.docsis.dao.CmcDocsisConfigDao;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.cmc.topology.service.CmcChannelDiscoveryService;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.impl.DiscoveryServiceImpl;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.platform.message.event.CmcEntityInfo;

/**
 * @author Victor
 * @created @2011-11-13-下午01:46:45
 * 
 */
@Service("cmcDiscoveryService")
public class CmcDiscoveryServiceImpl extends DiscoveryServiceImpl<CmcDiscoveryData> {
    @Autowired
    protected EntityService entityService;
    @Resource(name = "cmcService")
    protected CmcService cmcService;
    @Resource(name = "cmcConfigService")
    protected CmcConfigService cmcConfigService;
    @Resource(name = "cmcDiscoveryDao")
    protected CmcDiscoveryDao cmcDiscoveryDao;
    @Resource(name = "cmcPerfService")
    protected CmcPerfService cmcPerfService;
    @Resource(name = "cmcDocsisConfigDao")
    protected CmcDocsisConfigDao cmcDocsisConfigDao;
    @Resource(name = "perfThresholdService")
    protected PerfThresholdService perfThresholdService;
    @Resource(name = "perfTargetService")
    protected PerfTargetService perfTargetService;
    @Resource(name = "cmcDao")
    protected CmcDao cmcDao;
    @Resource(name = "cmRefreshDao")
    private CmRefreshDao cmRefreshDao;
    @Resource(name = "cmcChannelDiscoveryService")
    private CmcChannelDiscoveryService cmcChannelDiscoveryService;

    @PostConstruct
    public void initialize() {
    }

    @PreDestroy
    public void destroy() {
    }

    @Override
    public void updateEntity(Entity entity, CmcDiscoveryData data) {

        super.updateEntity(entity, data);

        Long entityId = entity.getEntityId();

        List<CmcEntityRelation> cmcEntityRelations = new ArrayList<>();
        List<CmcAttribute> cmcAttributes = data.getCmcAttributes();
        List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = data.getCmcUpChannelBaseInfos();
        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = data.getCmcDownChannelBaseInfos();
        List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos = data.getCmcUpChannelSignalQualityInfos();
        //add by fanzidong,解决null
        if(cmcUpChannelSignalQualityInfos==null){
        	cmcUpChannelSignalQualityInfos = new ArrayList<CmcUpChannelSignalQualityInfo>();
        }
        List<CmcPort> cmcPorts = data.getCmcPorts();

        Map<Long, CmcEntityInfo> cmcEntityInfos = data.getCmcEntityInfos();

        if (cmcEntityInfos != null) {
            for (CmcAttribute cmcAttribute : data.getCmcAttributes()) {
            	Long cmcIndex = cmcAttribute.getCmcIndex();
                CmcEntityInfo cmcEntityInfo = cmcEntityInfos.get(cmcIndex);
                // CmcEntityRelation
                CmcEntityRelation cmcEntityRelation = new CmcEntityRelation();
                cmcEntityRelation.setCmcEntityId(cmcEntityInfo.getCmcEntityId());
                cmcEntityRelation.setOnuId(cmcEntityInfo.getOnuId());
                cmcEntityRelation.setCmcIndex(cmcIndex);
                cmcEntityRelation.setCmcType(cmcEntityInfo.getTypeId());
                cmcEntityRelation.setCmcId(cmcEntityInfo.getCmcId());
                cmcEntityRelations.add(cmcEntityRelation);
            }

            // CmcAttribute
            for (CmcAttribute attribute : cmcAttributes) {
                Long cmcIndex = attribute.getCmcIndex();
                attribute.setCmcId(cmcEntityInfos.get(cmcIndex).getCmcId());
                attribute.setCmcDeviceStyle(cmcEntityInfos.get(cmcIndex).getTypeId());
            }

            cmcDiscoveryDao.syncCmcEntityInfo(entityId, cmcEntityRelations, cmcAttributes);

            // UpChannel
            for (CmcUpChannelBaseInfo upChannelBaseInfo : cmcUpChannelBaseInfos) {
                Long cmcIndex = upChannelBaseInfo.getCmcIndex();
                CmcEntityInfo entityInfo = cmcEntityInfos.get(cmcIndex);
                if (entityInfo != null) {
                    upChannelBaseInfo.setCmcId(entityInfo.getCmcId());
                }
            }

            // UpChannelSignal
            for (CmcUpChannelSignalQualityInfo upChannelSignalQualityInfo : cmcUpChannelSignalQualityInfos) {
                Long cmcIndex = upChannelSignalQualityInfo.getCmcIndex();
                CmcEntityInfo entityInfo = cmcEntityInfos.get(cmcIndex);
                if (entityInfo != null) {
                    upChannelSignalQualityInfo.setCmcId(entityInfo.getCmcId());
                }
            }

            // DownChannel
            for (CmcDownChannelBaseInfo downChannelBaseInfo : cmcDownChannelBaseInfos) {
                Long cmcIndex = downChannelBaseInfo.getCmcIndex();
                CmcEntityInfo entityInfo = cmcEntityInfos.get(cmcIndex);
                if (entityInfo != null) {
                    downChannelBaseInfo.setCmcId(entityInfo.getCmcId());
                }
            }

            // CmcPorts
            if(cmcPorts != null) {
                for (CmcPort port : cmcPorts) {
                    Long cmcIndex = port.getCmcIndex();
                    CmcEntityInfo entityInfo = cmcEntityInfos.get(cmcIndex);
                    if (entityInfo != null) {
                        port.setCmcId(entityInfo.getCmcId());
                    }
                }
            }

            try {
                // modify by fanzidong,将同步信道信息封装为service接口
                cmcChannelDiscoveryService.syncCmcChannelBaseInfo(cmcUpChannelBaseInfos, cmcUpChannelSignalQualityInfos,
                        cmcDownChannelBaseInfos, cmcPorts, entityId);
            } catch (Exception e) {
                logger.error("syncCmcChannelBaseInfo error:", e);
            }
        }

        // CM Topo Update
        if (data.getCmAttributes() != null) {
            try {
                List<CmAttribute> cmAttribuets = data.getCmAttributes();
                Map<Long, Long> cmcIds = new HashMap<Long, Long>();
                Map<Long, List<CmAttribute>> cmcAttributeMap = new HashMap<Long, List<CmAttribute>>();
                List<Long> cmcIdList = cmcDiscoveryDao.getCmcIdByOlt(entityId);
                for (Long cmcId : cmcIdList) {
                    List<CmAttribute> cas = new ArrayList<CmAttribute>();
                    cmcAttributeMap.put(cmcId, cas);
                }
                for (CmAttribute cmAttribute : cmAttribuets) {
                    // TODO 这里的index需要检查一下
                    Long cmcIndex = CmcIndexUtils.getCmcIndexFromCmIndex(cmAttribute.getStatusIndex());
                    Long cmcId;
                    if (!cmcIds.containsKey(cmcIndex)) {
                        Map<String, Long> map = new HashMap<String, Long>();
                        map.put("entityId", entityId);
                        map.put("cmcIndex", cmcIndex);
                        cmcId = cmcDiscoveryDao.getCmcIdByCmcIndexAndEntityId(map);
                        if (cmcId == null) {
                            logger.info("entityId [" + entityId + "] cmcIndex [" + cmcIndex + "] cmcId [" + cmcId + "]");
                            continue;
                        } else {
                            cmcIds.put(cmcIndex, cmcId);
                        }
                    } else {
                        cmcId = cmcIds.get(cmcIndex);
                    }
                    List<CmAttribute> attrs;
                    if (cmcAttributeMap.containsKey(cmcId)) {
                        attrs = cmcAttributeMap.get(cmcId);
                    } else {
                        attrs = new ArrayList<CmAttribute>();
                        cmcAttributeMap.put(cmcId, attrs);
                    }
                    attrs.add(cmAttribute);
                }
                for (Long cmcId : cmcAttributeMap.keySet()) {
                    List<CmAttribute> attrs = cmcAttributeMap.get(cmcId);
                    cmRefreshDao.batchRefreshCmAttribute(entityId, cmcId, attrs);
                }
            } catch (Exception e) {
                logger.error("batchInsertCmAttribute fail!", e);
            }
        }

        // CM CPE信息插入或刷新
        if (data.getCmCpeList() != null) {
            try {
                cmcDiscoveryDao.batchInsertOrUpdateCmCpe(data.getCmCpeList(), entityId);
            } catch (Exception e) {
                logger.error("getCmCpeList error", e);
            }
        }

        // CM 大客户IP信息插入或刷新
        if (data.getCmStaticIpList() != null) {
            try {
                cmcDiscoveryDao.batchInsertOrUpdateCmStaticIp(data.getCmStaticIpList(), entityId);
            } catch (Exception e) {
                logger.error("getCmStaticIpList error", e);
            }
        }
        // CM3.0 上行信号信息插入或刷新
        if (data.getDocsIf3CmtsCmUsStatusList() != null) {
            try {
                cmcDiscoveryDao.batchInsertOrUpdateDocsIf3CmtsCmUsStatus(data.getDocsIf3CmtsCmUsStatusList(), entityId);
            } catch (Exception e) {
                logger.error("getDocsIf3CmtsCmUsStatusList error", e);
            }
        }

        if (data.getCmcPhyConfigs() != null) {
            try {
                cmcDiscoveryDao.batchInsertCmcPhyConfigs(data.getCmcPhyConfigs(), entityId);
            } catch (Exception e) {
                logger.error("getCmcPhyConfigs error", e);
            }
        }

    }
}
