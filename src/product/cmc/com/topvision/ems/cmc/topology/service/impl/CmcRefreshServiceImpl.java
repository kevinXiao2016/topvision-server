package com.topvision.ems.cmc.topology.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.cm.dao.CmRefreshDao;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.cmc.topology.facade.CmcDiscoveryFacade;
import com.topvision.ems.cmc.topology.service.CmcChannelDiscoveryService;
import com.topvision.ems.cmc.topology.service.CmcRefreshService;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.exception.TopvisionDataException;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityTypeStandard;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

@Service("cmcRefreshService")
public class CmcRefreshServiceImpl extends BaseService implements CmcRefreshService {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private CmcDiscoveryDao cmcDiscoveryDao;
    @Autowired
    private CmcDao cmcDao;
    @Autowired
    private CmRefreshDao cmRefreshDao;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Resource(name = "cmc8800aDiscoveryService")
    private DiscoveryService<CmcDiscoveryData> cmc8800aDiscoveryService;
    @Resource(name = "cmcChannelDiscoveryService")
    private CmcChannelDiscoveryService cmcChannelDiscoveryService;

    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    private void updateCmcEntity(CmcDiscoveryData cmcData) {
        Long entityId = cmcData.getEntityId();
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        CmcAttribute cmcAttribute = cmcData.getCmcAttributes().get(0);
        Long cmcId, cmcIndex;
        if (cmcAttribute != null) {
            cmcId = cmcAttribute.getCmcId();
            cmcIndex = cmcAttribute.getCmcIndex();
            // 处理OLT重启后下级设备处于OFFLINE阶段下信息无法获取的问题
            Long cmcType = null;
            if (cmcAttribute.getTopCcmtsSysObjectId() != null) {
                cmcType = entityTypeService.getEntityTypeBySysObjId(cmcAttribute.getTopCcmtsSysObjectId()).getTypeId();
                // TODO Support STRONG_DISTRIBUTE_STANDARD AND WEEK_DISTRIBUTE_STANDARD
                if (entityTypeService.isCCMTS8800EType(cmcType)) {
                    cmcType = entityTypeService.getEntityTypeBySysObjId(cmcAttribute.getTopCcmtsSysObjectId(),
                            EntityTypeStandard.STRONG_DISTRIBUTE_STANDARD).getTypeId();
                }
            }
            cmcAttribute.setCmcDeviceStyle(cmcType);
            List<CmcEntityRelation> cmcEntityRelations = new ArrayList<>();
            CmcEntityRelation cmcEntityRelation = new CmcEntityRelation();
            cmcEntityRelation.setCmcEntityId(entityId);
            cmcEntityRelation.setOnuId(cmcId);
            cmcEntityRelation.setCmcIndex(cmcIndex);
            cmcEntityRelation.setCmcType(cmcType);
            cmcEntityRelation.setCmcId(cmcId);
            cmcEntityRelations.add(cmcEntityRelation);
            cmcDiscoveryDao.syncCmcEntityInfo(entityId, cmcEntityRelations, cmcData.getCmcAttributes());
        } else {
            throw new TopvisionDataException("cmc refresh error: cmcAttribute is null");
        }

        logger.info("storage cmcAttribute finished!");

        try {
            List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = cmcData.getCmcUpChannelBaseInfos();
            List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = cmcData.getCmcDownChannelBaseInfos();
            List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos = cmcData
                    .getCmcUpChannelSignalQualityInfos();
            for (CmcUpChannelSignalQualityInfo upChannelSignalQualityInfo : cmcUpChannelSignalQualityInfos) {
                upChannelSignalQualityInfo.setCmcId(cmcId);
            }
            List<CmcPort> cmcPorts = cmcData.getCmcPorts();
            for (CmcPort port : cmcPorts) {
                port.setCmcId(cmcId);
            }
            // modify by fanzidong,将同步信道信息封装为service接口
            cmcChannelDiscoveryService.syncCmcChannelBaseInfo(cmcUpChannelBaseInfos, cmcUpChannelSignalQualityInfos,
                    cmcDownChannelBaseInfos, cmcPorts, entityId);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("storage cmcChannelInfo finished!");

        // CM属性插入或更新数据库
        if (cmcData.getCmAttributes() != null) {
            try {
                cmRefreshDao.batchRefreshCmAttribute(entityId, cmcId, cmcData.getCmAttributes());
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        logger.info("storage CmAttribute finished!");

        // CM CPE信息插入或刷新
        if (cmcData.getCmCpeList() != null) {
            try {
                cmcDiscoveryDao.batchInsertOrUpdateCmCpe(cmcData.getCmCpeList(), entityId);
            } catch (Exception e) {
                logger.error("getCmCpeList error", e);
            }
        }
        logger.info("storage CmCpe finished!");

        // CM 大客户IP信息插入或刷新
        if (cmcData.getCmStaticIpList() != null) {
            try {
                cmcDiscoveryDao.batchInsertOrUpdateCmStaticIp(cmcData.getCmStaticIpList(), entityId);
            } catch (Exception e) {
                logger.error("getCmStaticIpList error", e);
            }
        }
        logger.info("storage CmStaticIpList finished!");

        if (cmcData.getDocsIf3CmtsCmUsStatusList() != null) {
            try {
                cmcDiscoveryDao.batchInsertOrUpdateDocsIf3CmtsCmUsStatusFor8800A(cmcData.getDocsIf3CmtsCmUsStatusList(),
                        cmcId);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        logger.info("storage DocsIf3CmtsCmUsStatusList finished!");

        // Sync Entity Info
        cmc8800aDiscoveryService.syncEntityInfo(entity, cmcData);

        if (cmcData.getTopoType().equals(DiscoveryData.BASE_TOPO)) {
            return;
        }
        // Send ConfigData Topo Event
        cmc8800aDiscoveryService.sendSynchronizedEvent(entityId, cmcData);

    }

    @Override
    public void refreshCC8800A(Long entityId, Long cmcIndex, Long cmcId) {
        CmcDiscoveryData cmcDiscoveryData = new CmcDiscoveryData(entityId);
        CmcAttribute cmcattr = new CmcAttribute();
        cmcattr.setCmcId(cmcId);
        cmcattr.setCmcIndex(cmcIndex);
        cmcattr.setEntityId(entityId);
        cmcDiscoveryData.addCmcAttribute(cmcattr);
        cmcDiscoveryData.setCmcUpChannelBaseInfos(cmcDao.getCmcUpChannelBaseInfosForDiscovery(cmcId));
        cmcDiscoveryData.setCmcDownChannelBaseInfos(cmcDao.getCmcDownChannelBaseInfosForDiscovery(cmcId));
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        cmcDiscoveryData = facadeFactory.getFacade(snmpParam.getIpAddress(), CmcDiscoveryFacade.class)
                .refreshCC8800A(snmpParam, cmcDiscoveryData);
        this.updateCmcEntity(cmcDiscoveryData);
    }

    @Override
    public void autoRefreshCC8800A(Long entityId, Long cmcIndex, Long cmcId) {
        CmcDiscoveryData cmcDiscoveryData = new CmcDiscoveryData(DiscoveryData.BASE_TOPO);
        cmcDiscoveryData.setEntityId(entityId);
        CmcAttribute cmcattr = new CmcAttribute();
        cmcattr.setCmcId(cmcId);
        cmcattr.setCmcIndex(cmcIndex);
        cmcattr.setEntityId(entityId);
        cmcDiscoveryData.addCmcAttribute(cmcattr);
        cmcDiscoveryData.setCmcUpChannelBaseInfos(cmcDao.getCmcUpChannelBaseInfosForDiscovery(cmcId));
        cmcDiscoveryData.setCmcDownChannelBaseInfos(cmcDao.getCmcDownChannelBaseInfosForDiscovery(cmcId));
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        cmcDiscoveryData = facadeFactory.getFacade(snmpParam.getIpAddress(), CmcDiscoveryFacade.class)
                .autoDiscoveryCC8800A(snmpParam, cmcDiscoveryData);
        this.updateCmcEntity(cmcDiscoveryData);
    }

}
