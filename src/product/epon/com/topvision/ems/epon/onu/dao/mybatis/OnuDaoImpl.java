/***********************************************************************
 * $Id: OnuDaoImpl.java,v1.0 2013-10-25 上午11:07:48 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.OltOnuTypeInfo;
import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.domain.OnuPonPort;
import com.topvision.ems.epon.domain.OnuUniPort;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.CC8800ABaseInfo;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuCatv;
import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuOpticalInfo;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltOnuVoip;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OnuBaseInfo;
import com.topvision.ems.epon.onu.domain.OnuDeregisterTable;
import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.ems.exception.TopvisionDataException;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.LinkDao;
import com.topvision.ems.network.dao.PortDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.Port;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author flack
 * @created @2013-10-25-上午11:07:48
 * 
 */
@Repository("onuDao")
public class OnuDaoImpl extends MyBatisDaoSupport<Object> implements OnuDao {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private PortDao portDao;
    private static String DEFAULT_NAME = "NO_DESCRIPTION";
    private static String DEFAULT_NAME_GPONONU = "ONU_NO_DESCRIPTION";

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.Onu";
    }

    @Override
    public List<EntityType> getOnuTypes(Long type) throws SQLException {
        return getSqlSession().selectList(getNameSpace() + "getOnuTypes", type);
    }

    @Override
    public List<OltOnuAttribute> getOnuList(Long entityId, Long slotId, Long ponId, Integer llid, String mac,
            String onuName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        map.put("entityId", entityId);
        if (slotId != null && slotId != -1) {
            map.put("slotId", slotId);
        } else {
            map.put("slotId", -1L);
        }
        if (ponId != null && ponId != -1) {
            map.put("ponId", ponId);
        } else {
            map.put("ponId", -1L);
        }
        if (llid != null) {
            map.put("llid", (long) llid);
        } else {
            map.put("llid", -1L);
        }
        if (mac != null) {
            map.put("mac", mac);
        } else {
            map.put("mac", null);
        }
        if (onuName != null && !"".equals(onuName)) {
            map.put("onuName", onuName);
        } else {
            map.put("onuName", null);
        }
        return getSqlSession().selectList(getNameSpace() + "getOnuList", map);
    }

    @Override
    public List<OltOnuAttribute> getOnuListByPonId(Long ponId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuListByPonId", ponId);
    }

    @Override
    public OltOnuAttribute getOnuEntityById(Long onuId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Long userId = null;
        try {
            userId = CurrentRequest.getCurrentUser().getUserId();
        } catch (Exception e) {
            logger.debug("CurrentRequest.getCurrentUser().getUserId() fail");
        }
        map.put("onuId", onuId);
        map.put("userId", userId);
        return getSqlSession().selectOne(getNameSpace() + "getOnuEntityById", map);
    }

    @Override
    public OltOnuPonAttribute getOnuPonAttribute(Long onuPonId) {
        return getSqlSession().selectOne(getNameSpace() + "getOnuPonAttribute", onuPonId);
    }

    @Override
    public void updateOnuAdminStatus(Long onuId, Integer adminStatus) {
        OltOnuAttribute onuAttribute = new OltOnuAttribute();
        onuAttribute.setOnuId(onuId);
        onuAttribute.setOnuAdminStatus(adminStatus);
        getSqlSession().update(getNameSpace() + "updateOnuAdminStatus", onuAttribute);
    }

    @Override
    public void updateOnuName(Long onuId, String onuName) {
        OltOnuAttribute onuAttribute = new OltOnuAttribute();
        onuAttribute.setOnuId(onuId);
        onuAttribute.setOnuName(onuName);
        getSqlSession().update(getNameSpace() + "updateOnuName", onuAttribute);
    }

    @Override
    public void updateOnuAttribute(OltOnuAttribute oltOnuAttribute) {
        OltOnuAttribute onuAttribute = new OltOnuAttribute();
        onuAttribute.setOnuName(oltOnuAttribute.getOnuName());
        onuAttribute.setOnuType(oltOnuAttribute.getOnuType());
        onuAttribute.setOnuMacAddress(oltOnuAttribute.getOnuMacAddress());
        onuAttribute.setOnuMac(oltOnuAttribute.getOnuMac());
        onuAttribute.setOnuOperationStatus(oltOnuAttribute.getOnuOperationStatus());
        onuAttribute.setOnuAdminStatus(oltOnuAttribute.getOnuAdminStatus());
        onuAttribute.setOnuChipVendor(oltOnuAttribute.getOnuChipVendor());
        onuAttribute.setOnuChipType(oltOnuAttribute.getOnuChipType());
        onuAttribute.setOnuChipVersion(oltOnuAttribute.getOnuChipVersion());
        onuAttribute.setOnuSoftwareVersion(oltOnuAttribute.getOnuSoftwareVersion());
        onuAttribute.setOnuFirmwareVersion(oltOnuAttribute.getOnuFirmwareVersion());
        onuAttribute.setOnuTestDistance(oltOnuAttribute.getOnuTestDistance());
        onuAttribute.setOnuLlidId(oltOnuAttribute.getOnuLlidId());
        onuAttribute.setOnuTimeSinceLastRegister(oltOnuAttribute.getOnuTimeSinceLastRegister());
        getSqlSession().update(getNameSpace() + "updateOnuAttribute", onuAttribute);
    }

    @Override
    public void insertOrUpdateOltOnuPonAttribute(OltOnuPonAttribute oltOnuPonAttribute) {
        try {
            if (((Integer) getSqlSession().selectOne(getNameSpace() + "getOnuPonCount", oltOnuPonAttribute)) != 0) {
                getSqlSession().update(getNameSpace() + "updateOnuPonAttribute", oltOnuPonAttribute);
            } else {
                OltOnuAttribute oAttribute = new OltOnuAttribute();
                oAttribute.setEntityId(oltOnuPonAttribute.getEntityId());
                oAttribute.setOnuIndex(EponIndex.getOnuIndex(oltOnuPonAttribute.getOnuPonIndex()));
                oltOnuPonAttribute.setOnuId((Long) getSqlSession().selectOne(getNameSpace() + "getOnuId", oAttribute));
                getSqlSession().insert(getNameSpace() + "insertOnuPonRelation", oltOnuPonAttribute);
                oltOnuPonAttribute.setOnuPonId(oltOnuPonAttribute.getOnuPonId());
                getSqlSession().insert(getNameSpace() + "insertOnuPonAttribute", oltOnuPonAttribute);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public void syncSubordinateEntityAttribute(List<OltOnuAttribute> existOnuAttributes, List<OltOnuAttribute> list,
            HashMap<Long, Long> oltMap, Entity entity) {
        /*
         * List<OltOnuAttribute> existOnuAttributes = getSqlSession().selectList(getNameSpace() +
         * "getOnuListByEntity", entity.getEntityId());
         */
        List<OltOnuAttribute> deleteOnuList = existOnuAttributes;
        deleteOnuList.removeAll(list);
        for (OltOnuAttribute delAttribute : deleteOnuList) {
            entityDao.deleteByPrimaryKey(delAttribute.getOnuId());
            if (oltMap.containsKey(delAttribute.getOnuIndex())) {
                oltMap.remove(delAttribute.getOnuIndex());
            }
        }
        List<Long> insertOnuEntityIds = new ArrayList<>();
        EntityType unknownOnuType = getSqlSession().selectOne(getNameSpace() + "getUnknownOnuType",
                OltOnuAttribute.ONU_TYPE_UNKNOWN);
        try {
            for (OltOnuAttribute oltOnuAttribute : list) {
                if (oltMap.containsKey(oltOnuAttribute.getOnuIndex())) {
                    oltOnuAttribute.setOnuId(oltMap.get(oltOnuAttribute.getOnuIndex()));
                    getSqlSession().update(getNameSpace() + "updateOltOnuAttribute", oltOnuAttribute);
                    updateEntityByOnuAttribute(oltOnuAttribute);
                } else {
                    oltOnuAttribute.setPonId(oltMap.get(EponIndex.getPonIndex(oltOnuAttribute.getOnuIndex())));
                    Entity onuEntity = new Entity();
                    onuEntity.setParentId(oltOnuAttribute.getEntityId());
                    onuEntity.setSysUpTime(oltOnuAttribute.getOnuTimeSinceLastRegister().toString());
                    onuEntity.setAuthorityUserId(entity.getAuthorityUserId());
                    String historyAlias = null;
                    try {
                        historyAlias = entityDao.selectHistoryAlias(oltOnuAttribute.getEntityId(),
                                oltOnuAttribute.getOnuMac(), oltOnuAttribute.getOnuIndex().toString(),
                                oltOnuAttribute.getOnuUniqueIdentification());
                    } catch (Exception e) {
                        historyAlias = null;
                    }
                    if (historyAlias != null) {
                        onuEntity.setName(historyAlias);
                    } else if (oltOnuAttribute.getOnuName() == null || DEFAULT_NAME.equals(oltOnuAttribute.getOnuName())
                            || DEFAULT_NAME_GPONONU.equals(oltOnuAttribute.getOnuName())
                            || oltOnuAttribute.getOnuName().length() == 0) {
                        onuEntity.setName(
                                "ONU_" + EponIndex.getOnuStringByIndex(oltOnuAttribute.getOnuIndex()).toString());
                    } else {
                        onuEntity.setName(oltOnuAttribute.getOnuName());
                    }
                    onuEntity.setMac(oltOnuAttribute.getOnuMac());
                    onuEntity.setSysName(oltOnuAttribute.getOnuName());
                    oltOnuAttribute.setOnuName(onuEntity.getName());

                    EntityType entityType = oltOnuAttribute.getEntityType();
                    if (entityType != null) {
                        onuEntity.setTypeName(entityType.getName());
                        onuEntity.setTypeId(entityType.getTypeId());
                        onuEntity.setIcon48(entityType.getIcon48());
                    } else {
                        onuEntity.setTypeName(unknownOnuType.getName());
                        onuEntity.setTypeId(unknownOnuType.getTypeId());
                        onuEntity.setIcon48(unknownOnuType.getIcon48());
                    }
                    // Handle Entity OltOnuRelation OltOnuAttribute
                    Long entityId = null;
                    try {
                        entityId = entityDao.insertEntityWithId(onuEntity, oltOnuAttribute.getOnuOperationStatus());
                        oltOnuAttribute.setOnuId(entityId);
                        getSqlSession().insert(getNameSpace() + "insertOltOnuRelation", oltOnuAttribute);
                        if (EponConstants.EPON_ONU.equals(oltOnuAttribute.getOnuEorG())) {
                            getSqlSession().insert(getNameSpace() + "insertOltOnuAttribute", oltOnuAttribute);
                        } else if (GponConstant.GPON_ONU.equals(oltOnuAttribute.getOnuEorG())) {
                            GponOnuAttribute gponOnuAttribute = (GponOnuAttribute) oltOnuAttribute;
                            getSqlSession().insert(getNameSpace() + "insertGponOltOnuAttribute", gponOnuAttribute);
                            getSqlSession().insert(getNameSpace() + "insertGponOnuCapability", gponOnuAttribute);
                        }
                        insertOnuEntityIds.add(entityId);
                        oltMap.put(oltOnuAttribute.getOnuIndex(), oltOnuAttribute.getOnuId());
                    } catch (Exception e) {
                        entityDao.deleteByPrimaryKey(entityId);
                        throw new TopvisionDataException("insertOltOnuRelation error", e);
                    }
                }
            }
            try {
                entityDao.syncOnuAuthority(insertOnuEntityIds);
                createLinkAndPort(list, entity);
            } catch (Exception e) {
                logger.error("Sync OnuAuthority Or Add Link And Port error:", e);
            }
        } catch (TopvisionDataException e) {
            throw e;
        } catch (Exception e) {
            logger.error("syncSubordinateEntityAttribute", e);
        }
    }

    @Override
    public void createEntityRelationAndLink(Entity onuEntity, Entity subEntity) {
        Entity relation = new Entity();
        relation.setEntityId(subEntity.getEntityId());
        relation.setParentId(onuEntity.getEntityId());
        getSqlSession().update(getNameSpace() + "createEntityRelation", relation);
        Link link = new Link();
        link.setName("ONU[" + onuEntity.getName() + "] - CMTS[" + subEntity.getName() + "]");
        link.setSrcEntityId(onuEntity.getEntityId());
        link.setSrcIfIndex(0L);
        link.setDestEntityId(subEntity.getEntityId());
        link.setDestIfIndex(0L);
        Long linkId = linkDao.isLinkExists(link);
        if (linkId == null) {
            linkDao.insertEntity(link);
        }
    }

    @Override
    public void deleteEntityRelationAndLink(Long entityId) {
        Long subEntityId = getSqlSession().selectOne(getNameSpace() + "selectOnuSubEntityId", entityId);
        if (subEntityId != null) {
            // Entity
            getSqlSession().update(getNameSpace() + "deleteSubEntityRelation", subEntityId);
            // Link
            Link link = new Link();
            link.setSrcEntityId(entityId);
            link.setSrcIfIndex(0L);
            link.setDestEntityId(subEntityId);
            link.setDestIfIndex(0L);
            Long linkId = linkDao.isLinkExists(link);
            if (linkId != null) {
                linkDao.deleteByPrimaryKey(linkId);
            }
            // Relation
            getSqlSession().update(getNameSpace() + "deleteSubCmtsRelation", subEntityId);
        }
    }

    @SuppressWarnings("unused")
    private void syncOnuTypeInfo(OltOnuAttribute onuAttribute) {
        if (onuAttribute.getOnuType() != null) {
            OltOnuTypeInfo typeInfo = getOnuTypeInfo(onuAttribute.getOnuType());
            if (typeInfo != null) {
                onuAttribute.setOnuIcon(getOnuTypeInfo(onuAttribute.getOnuType()).getOnuIcon());
            } else {
                onuAttribute.setOnuIcon("image/blankOnu.png");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.dao.OnuDao#synOnuIndexForCmcAtVersionUpdate(long)
     */
    @Override
    public void synOnuIndexForCmcAtVersionUpdate(long entityId) {
        // 更新oltOnuRelation表
        getSqlSession().update(getNameSpace() + "synOltOnuRelationForCmcAtVersionUpdate", entityId);
        // 更新oltOnuPonRelation表
        getSqlSession().update(getNameSpace() + "synOltOnuPonRelationForCmcAtVersionUpdate", entityId);
    }

    private void createLinkAndPort(List<OltOnuAttribute> oltOnuAttributes, Entity entity) {
        List<Port> ports = new ArrayList<Port>();
        List<Link> links = new ArrayList<Link>();
        for (OltOnuAttribute attribute : oltOnuAttributes) {
            Link link = new Link();
            Port srcPort = new Port();
            Port destPort = new Port();
            link.setName("OLT[" + entity.getIp() + "] - ONU["
                    + EponIndex.getOnuStringByIndex(attribute.getOnuIndex()).toString() + "]");
            link.setSrcEntityId(entity.getEntityId());
            link.setSrcIfIndex(EponIndex.getPonIndex(attribute.getOnuIndex()));
            link.setDestEntityId(attribute.getOnuId());
            link.setDestIfIndex(attribute.getOnuIndex());
            links.add(link);
            srcPort.setEntityId(link.getSrcEntityId());
            srcPort.setIfIndex(link.getSrcIfIndex().longValue());
            srcPort.setIfName("PORT_" + EponIndex.getPortStringByIndex(attribute.getOnuIndex()).toString());
            destPort.setEntityId(link.getDestEntityId());
            destPort.setIfIndex(link.getDestIfIndex().longValue());
            destPort.setIfName("ONU_" + EponIndex.getOnuStringByIndex(attribute.getOnuIndex()).toString());
            ports.add(srcPort);
            ports.add(destPort);
        }
        for (Port port : ports) {
            Long portId = portDao.isPortExists(port);
            if (portId != null) {
                // port.setPortId(portId);
                // portDao.updateEntity(port);
            } else {
                portDao.insertEntity(port);
            }
        }
        for (Link link : links) {
            Long linkId = linkDao.isLinkExists(link);
            if (linkId != null) {
                // link.setLinkId(linkId);
                // linkDao.updateEntity(link);
            } else {
                linkDao.insertEntity(link);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.onu.dao.OnuDao#syncOltOnuAttribute(com.topvision.ems.epon.onu.domain
     * .OltOnuAttribute)
     */
    @Override
    public void syncOltOnuAttribute(OltOnuAttribute oltOnuAttribute) {
        getSqlSession().update(getNameSpace() + "syncOltOnuAttribute", oltOnuAttribute);
    }

    @Override
    public void syncOnuPonAttribute(List<OltOnuPonAttribute> list, HashMap<Long, Long> onuMap) {
        SqlSession sqlSession = getBatchSession();
        for (OltOnuPonAttribute oltOnuPonAttribute : list) {
            if (onuMap.containsKey(oltOnuPonAttribute.getOnuPonIndex())) {
                sqlSession.update(getNameSpace() + "updateOnuPonAttribute", oltOnuPonAttribute);
            } else {
                oltOnuPonAttribute.setOnuId(onuMap.get(EponIndex.getOnuIndex(oltOnuPonAttribute.getOnuPonIndex())));
                getSqlSession().insert(getNameSpace() + "insertOnuPonRelation", oltOnuPonAttribute);
                oltOnuPonAttribute.setOnuPonId(oltOnuPonAttribute.getOnuPonId());
                getSqlSession().insert(getNameSpace() + "insertOnuPonAttribute", oltOnuPonAttribute);
                onuMap.put(oltOnuPonAttribute.getOnuPonIndex(), oltOnuPonAttribute.getOnuPonId());
            }
        }
        try {
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public Onu getOnuStructure(Long onuId) {
        Onu onu = (Onu) getSqlSession().selectOne(getNameSpace() + "getOnuByOnuId", onuId);
        OnuPonPort onuPonPort = (OnuPonPort) getSqlSession().selectOne(getNameSpace() + "getOltOnuPonAttribute", onuId);
        Long slotNo = EponIndex.getSlotNo(EponIndex.getOnuIndex(onu.getOnuIndex()));
        Long ponNo = EponIndex.getPonNo(EponIndex.getOnuIndex(onu.getOnuIndex()));
        Long portId = (Long) getSqlSession().selectOne(getNameSpace() + "getPonIdByOnuId", onuId);
        OltPonOptical oltPonOptical = (OltPonOptical) getSqlSession()
                .selectOne("com.topvision.ems.epon.optical.domain.Optical.getOltPonOptical", portId);
        // 从性能采集数据中获取Olt pon口光信息
        if (oltPonOptical != null) {
            OltOnuOpticalInfo oltOptical = this.selectOltOunOpticalInfo(oltPonOptical.getEntityId(),
                    oltPonOptical.getPortIndex());
            if (oltOptical != null) {
                oltPonOptical.setTransPower(oltOptical.getPonOpticalTrans());
                oltPonOptical.setRevPower(oltOptical.getPonOpticalRev());
            }
        }
        onu.setOltPonOptical(oltPonOptical);
        // -------如果pon口没有采到，则不记录PON口------------//
        if (onuPonPort != null) {
            onuPonPort.setSlotNo(slotNo);
            onuPonPort.setPonNo(ponNo);
            // 如果是CC8800A,从CMC的光链路信息中取得光发送与光接收功率
            if (onu.getOnuPreType() != null
                    && EponConstants.CC_ONUFLAG_TYPE.contains(Integer.valueOf(onu.getOnuPreType()))) {
                OltOnuOpticalInfo onuOptical = this.selectOltCcmtsOpticalInfo(onuId, onu.getOnuIndex());
                if (onuOptical != null) {
                    onuPonPort.setOnuTransPower(onuOptical.getPonOpticalTrans());
                    onuPonPort.setOnuRevPower(onuOptical.getPonOpticalRev());
                }
            }
        } else {
            onuPonPort = new OnuPonPort();
            onuPonPort.setSlotNo(slotNo);
            onuPonPort.setPonNo(ponNo);
            onuPonPort.setOnuTramsmittedOpticalPower(0);
            onuPonPort.setOnuReceivedOpticalPower(0);
        }
        onu.setOnuPonPort(onuPonPort);
        List<Long> onuUniIdList = getSqlSession().selectList("com.topvision.ems.epon.onu.domain.Uni.getOltUniIdList",
                onuId);
        // -------如果UNI口没有采到，则不记录UNI口------------//
        if (onuUniIdList != null) {
            List<OnuUniPort> onuUniPortList = new ArrayList<OnuUniPort>();
            for (Long anOnuUniIdList : onuUniIdList) {
                OnuUniPort onuUniPort = (OnuUniPort) getSqlSession()
                        .selectOne("com.topvision.ems.epon.onu.domain.Uni.getUniAttribute", anOnuUniIdList);
                OltUniExtAttribute uniExt = (OltUniExtAttribute) getSqlSession().selectOne(
                        "com.topvision.ems.epon.onu.domain.Uni.getOltUniExtAttribute", onuUniPort.getUniId());
                if (uniExt == null) {
                    continue;
                }
                if (uniExt.getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger() != null) {
                    onuUniPort.setAutoNegAdvertisedTechAbility(
                            uniExt.getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger().toString());
                }
                onuUniPortList.add(onuUniPort);
            }
            onu.setOnuUniPortList(onuUniPortList);
        }
        List<Long> elecOnuComIdList = getSqlSession()
                .selectList("com.topvision.ems.epon.onu.domain.ElectricityOnu.getOltComIdList", onuId);
        // ----- 如果COM口没有采集到，则不记录COM口
        if (elecOnuComIdList != null) {
            List<OltOnuComAttribute> comList = new ArrayList<OltOnuComAttribute>();
            for (Long onuComId : elecOnuComIdList) {
                OltOnuComAttribute onuComPort = (OltOnuComAttribute) getSqlSession()
                        .selectOne("com.topvision.ems.epon.onu.domain.ElectricityOnu.getComAttribute", onuComId);
                comList.add(onuComPort);
            }
            onu.setOnuElecComList(comList);
        }
        return onu;
    }

    @Override
    public Long getOnuIndex(Long onuId) {
        return getSqlSession().selectOne(getNameSpace() + "getOnuIndex", onuId);
    }

    @Override
    public Long getOnuIdByIndex(Long entityId, Long onuIndex) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace() + "getOnuIdByIndex", paramMap);
    }

    @Override
    public List<Long> getOnuIdByPonId(Long entityId, Long ponId) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("ponId", ponId);
        return getSqlSession().selectList(getNameSpace() + "getOnuIdByPonId", paramMap);
    }

    @Override
    public List<Long> getOnuIdsByPonIds(Long entityId, List<Long> ponIds) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("ponIds", ponIds);
        return getSqlSession().selectList(getNameSpace() + "getOnuIdsByPonIds", paramMap);
    }

    @Override
    public OltOnuPonAttribute getOnuPonAttributeByOnuId(Long onuId) {
        return getSqlSession().selectOne(getNameSpace() + "getOnuPonAttributeByOnuId", onuId);
    }

    @Override
    public void updateOnuTemperatureDetectEnable(Long onuId, Integer onuTemperatureDetectEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("temperatureDetectEnable", onuTemperatureDetectEnable);
        getSqlSession().update(getNameSpace() + "updateOnuTemperatureDetectEnable", paramMap);
    }

    @Override
    public void updateOnuFecEnable(Long onuId, Integer onuFecEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("onuFecEnable", onuFecEnable);
        getSqlSession().update(getNameSpace() + "updateOnuFecEnable", paramMap);
    }

    @Override
    public void updateOnuIsolationEnable(Long onuId, Integer onuIsolationEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("onuIsolationEnable", onuIsolationEnable);
        getSqlSession().update(getNameSpace() + "updateOnuIsolationEnable", paramMap);
    }

    @Override
    public void updateOnu15minEnable(Long onuId, Integer onu15minEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("onu15minEnable", onu15minEnable);
        getSqlSession().update(getNameSpace() + "updateOnu15minEnable", paramMap);
    }

    @Override
    public void updateOnu24hEnable(Long onuId, Integer onu24hEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("onu24hEnable", onu24hEnable);
        getSqlSession().update(getNameSpace() + "updateOnu24hEnable", paramMap);
    }

    @Override
    public OltOnuCapability getOltOnuCapability(Long onuId) {
        return getSqlSession().selectOne(getNameSpace() + "getOltOnuCapability", onuId);
    }

    @Override
    public OltTopOnuCapability getOltTopOnuCapability(Long onuId) {
        return getSqlSession().selectOne(getNameSpace() + "getOltTopOnuCapability", onuId);
    }

    @Override
    public void updateOnuMacMaxNum(Long onuId, Integer macMaxNum) {
        OltTopOnuCapability topOnu = new OltTopOnuCapability();
        topOnu.setOnuId(onuId);
        topOnu.setCapAddrMaxQuantity(macMaxNum);
        getSqlSession().update(getNameSpace() + "updateTopOnuMacMaxNum", topOnu);
    }

    @Override
    public OltOnuRstp getOltOnuRstpByOnuId(Long onuId) {
        return getSqlSession().selectOne(getNameSpace() + "getOltOnuRstp", onuId);
    }

    @Override
    public void updateOnuRstpBridgeMode(Long onuId, Integer onuRstpBridgeMode) {
        OltOnuRstp onuRstp = new OltOnuRstp();
        onuRstp.setOnuId(onuId);
        onuRstp.setRstpBridgeMode(onuRstpBridgeMode);
        getSqlSession().update(getNameSpace() + "updateOltOnuRstp", onuRstp);
    }

    @Override
    public void syncOnuCapatility(List<OltOnuCapability> onuList, List<OltTopOnuCapability> topList,
            HashMap<Long, Long> onuMap) {
        SqlSession sqlSession = getBatchSession();
        try {
            if (onuList != null && onuList.size() > 0) {
                for (OltOnuCapability capability : onuList) {
                    Long onuId = onuMap.get(capability.getOnuIndex());
                    if (onuId != null) {
                        capability.setOnuId(onuId);
                        sqlSession.update(getNameSpace() + "updateOnuCapatility", capability);
                    }
                }
            }
            if (topList != null && topList.size() > 0) {
                for (OltTopOnuCapability topOnuCapability : topList) {
                    Long onuId = onuMap.get(topOnuCapability.getOnuIndex());
                    if (onuId != null) {
                        topOnuCapability.setOnuId(onuId);
                        sqlSession.update(getNameSpace() + "updateTopOnuCapatility", topOnuCapability);
                    }

                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public OltOnuTypeInfo getOnuTypeInfo(Integer onuTypeId) {
        return (OltOnuTypeInfo) getSqlSession().selectOne(getNameSpace() + "selectOnuTypeInfo", onuTypeId);
    }

    @Override()
    public OltOnuAttribute getOnuAttributeByIndex(Long entityId, Long onuIndex) {
        Map<String, Long> param = new HashMap<String, Long>();
        param.put("entityId", entityId);
        param.put("onuIndex", onuIndex);
        return (OltOnuAttribute) getSqlSession().selectOne(getNameSpace() + "getOnuAttributeByIndex", param);
    }

    @Override()
    public OltOnuAttribute getOnuAttributeByUniqueId(Long entityId, String uniqueId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("uniqueId", uniqueId);
        return getSqlSession().selectOne(getNameSpace() + "getOnuAttributeByUniqueId", param);
    }

    @Override
    public void insertOnuToEntity(Entity entity) {
        getSqlSession().insert("com.topvision.ems.network.domain.Entity.entity2Folder", entity);
    }

    @Override
    public void insertOnuEntityProductRelation(Long productId, Long entityId, Long productType) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("productId", productId);
        params.put("entityId", entityId);
        params.put("productType", productType);
        getSqlSession().insert("com.topvision.ems.network.domain.VirtualNet.insertEntityProductRelation", params);
    }

    @Override
    public List<OltOnuAttribute> getOnuDeviceListItem(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getOnuDeviceListItem", map);
    }

    @Override
    public void updateOnuTemperature(Long onuId, Integer temperature) {
        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("onuId", onuId);
        map.put("temperature", temperature.longValue());
        getSqlSession().update(getNameSpace() + "updateOnuTemperature", map);
    }

    @Override
    public Integer getOnuTotleNum(Long olt) {
        return (Integer) getSqlSession().selectOne(getNameSpace() + "getOnuTotleNum", olt);
    }

    @Override
    public void batchInsertOltOnuPreType(final List<OltTopOnuProductTable> productTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltTopOnuProductTable table : productTables) {
                OltOnuAttribute attribute = new OltOnuAttribute();
                attribute = getOnuAttributeByIndex(table.getEntityId(), table.getOnuIndex());
                table.setOnuId(attribute.getOnuId());
                sqlSession.update(getNameSpace() + "updateOltOnuPreType", table);
                // table.setOnuId((Long)
                // executor.selectOne(getNameSpace() + "getOnuId",
                // attribute));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<OltOnuAttribute> getOnuList(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getOnuListByParams", paramMap);
    }

    @Override
    public int getOnuListCount(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "getOnuListCountByParams", paramMap);
    }

    @Override
    public List<String> getOnuHwList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuHwList", entityId);
    }

    @Override
    public List<Long> getOnuIdByFolder(Long type, Long folderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("folderId", folderId);
        return getSqlSession().selectList(getNameSpace() + "getOnuIdByFolder", map);
    }

    @Override
    public void cleanOnuDeviceUpTime(final Long entityId, final Map<String, String> rMap, final Long collectTime) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (Map.Entry<String, String> entry : rMap.entrySet()) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("entityId", entityId);
                Long onuMibIndex = Long.parseLong(entry.getKey().substring("1.3.6.1.4.1.17409.2.3.4.1.1.18.".length()));
                paramMap.put("onuId", EponIndex.getOnuIndexByMibIndex(onuMibIndex));
                paramMap.put("collectTime", collectTime);
                sqlSession.delete("com.topvision.ems.epon.olt.domain.Olt.cleanOltDeviceUpTime", paramMap);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void addOnuDeviceUpTime(final Long entityId, final Map<String, String> rMap, final Long collectTime) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (Map.Entry<String, String> entry : rMap.entrySet()) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("entityId", entityId);
                Long onuMibIndex = Long.parseLong(entry.getKey().substring("1.3.6.1.4.1.17409.2.3.4.1.1.18.".length()));
                paramMap.put("onuId", EponIndex.getOnuIndexByMibIndex(onuMibIndex));
                // ONU不在线的时候回出现与当前时间同步的情况，此处认为大于一年的注册时间都是非法的
                if (Long.parseLong(entry.getValue()) > 3600 * 24 * 365) {
                    continue;
                }
                paramMap.put("deviceUpTime", collectTime - Long.parseLong(entry.getValue()) * 1000);
                paramMap.put("collectTime", collectTime);
                sqlSession.insert("com.topvision.ems.epon.olt.domain.Olt.insertOltDeviceUpTime", paramMap);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public Map<Long, String> getAllOnuMacAndIndex(Long entityId) {
        Map<Long, String> rMap = new HashMap<Long, String>();
        Map<Long, Long> sMap = selectMapByKeyAndValue(getNameSpace() + "getAllOnuMacAndIndex", entityId, "onuIndex",
                "onuMacAddress");
        for (Map.Entry<Long, Long> entry : sMap.entrySet()) {
            if (entry.getValue() != null) {
                rMap.put(entry.getKey(), new MacUtils(entry.getValue()).toString(MacUtils.MAOHAO));
            }
        }
        return rMap;
    }

    @Override
    public List<Long> getOnuIdList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuIdList", entityId);
    }

    private void updateEntityByOnuAttribute(OltOnuAttribute oltOnuAttribute) {
        Entity onuEntity = entityDao.selectByPrimaryKey(oltOnuAttribute.getOnuId());
        onuEntity.setName(oltOnuAttribute.getOnuName());
        onuEntity.setParentId(oltOnuAttribute.getEntityId());
        onuEntity.setSysUpTime(oltOnuAttribute.getOnuTimeSinceLastRegister().toString());
        onuEntity.setSysName(oltOnuAttribute.getOnuName());
        onuEntity.setMac(oltOnuAttribute.getOnuMac());
        EntityType entityType = oltOnuAttribute.getEntityType();
        if (entityType != null) {
            // onuEntity.setCorpId();
            onuEntity.setTypeName(entityType.getName());
            onuEntity.setTypeId(entityType.getTypeId());
            onuEntity.setIcon48(entityType.getIcon48());
        }
        entityDao.updateEntity(onuEntity);
        EntitySnap onuSnap = new EntitySnap();
        onuSnap.setEntityId(oltOnuAttribute.getOnuId());
        onuSnap.setState(oltOnuAttribute.getOnuOperationStatus() == 1 ? true : false);
        onuSnap.setSnapTime(new Timestamp(System.currentTimeMillis()));
        String sysUpTime = oltOnuAttribute.getOnuTimeSinceLastRegister().toString();
        if (StringUtils.isNumeric(sysUpTime)) {
            onuSnap.setSysUpTime(sysUpTime);
        } else {
            onuSnap.setSysUpTime(null);
        }
        entityDao.updateOnuEntitySnap(onuSnap);
    }

    @Override
    public void updateOnuEntityName(Long onuId, String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", onuId);
        map.put("name", name);
        getSqlSession().update(getNameSpace() + "updateOnuEntityName", map);
    }

    @Override
    public List<OltOnuAttribute> getOnuPreTypeByPonId(Long ponId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuPreTypeByPonId", ponId);
    }

    @Override
    public void updateOltOnuAttribute(OltOnuAttribute oltOnuAttribute) {
        Long onuId = (Long) getSqlSession().selectOne(getNameSpace() + "getOnuId", oltOnuAttribute);
        if (onuId != null) {
            oltOnuAttribute.setOnuId(onuId);
            getSqlSession().update(getNameSpace() + "updateOltOnuAttributeMac", oltOnuAttribute);
        }
    }

    @Override
    public List<OltOnuAttribute> getOnuListByEntity(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getOnuListByEntity"), entityId);
    }

    @Override
    public void updateCC8800ARestartTime(Map<String, Long> attrs) {
        getSqlSession().update(getNameSpace("updateCC8800ARestartTime"), attrs);
    }

    @Override
    public void batchUpdateOnu15MinStatus(List<OltTopOnuCapability> onus) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltTopOnuCapability onuAttr : onus) {
                Long onuId = (Long) sqlSession.selectOne(getNameSpace() + "getOnuId", onuAttr);
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("onuId", onuId);
                paramMap.put("onu15minEnable", onuAttr.getPonPerfStats15minuteEnable());
                sqlSession.update(getNameSpace() + "updateOnu15minEnable", paramMap);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void modifyOnuMacAgeTime(Long onuId, Integer macAge) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("onuId", onuId);
        map.put("macAge", macAge.longValue());
        getSqlSession().update(getNameSpace("modifyOnuMacAgeTime"), map);
    }

    @Override
    public OltOnuOpticalInfo selectOltOunOpticalInfo(Long entityId, Long ponIndex) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("entityId", entityId);
        params.put("ponIndex", ponIndex);
        return this.getSqlSession().selectOne(getNameSpace("selectOltOunOpticalInfo"), params);
    }

    @Override
    public OltOnuOpticalInfo selectOltCcmtsOpticalInfo(Long cmcId, Long ponIndex) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("cmcId", cmcId);
        params.put("ponIndex", ponIndex);
        return this.getSqlSession().selectOne(getNameSpace("selectOltCcmtsOpticalInfo"), params);
    }

    @Override
    public Integer selectOltCcmtsStatus(Long entityId) {
        return this.getSqlSession().selectOne(getNameSpace("selectOltCcmtsStatus"), entityId);
    }

    @Override
    public void updateOnuBaseInfo(OnuBaseInfo onuBaseInfo) {
        this.getSqlSession().update(getNameSpace("updateOnuBaseInfo"), onuBaseInfo);
    }

    @Override
    public void updateCC8800AInfo(CC8800ABaseInfo cc8800aInfo) {
        this.getSqlSession().update(getNameSpace("updateCC8800AInfo"), cc8800aInfo);
    }

    @Override
    public Long getOnuPonId(Long onuId) {
        return this.getSqlSession().selectOne(getNameSpace("getOnuPonIdByOnuId"), onuId);
    }

    @Override
    public Long getOnuPonIndex(Long onuId) {
        return this.getSqlSession().selectOne(getNameSpace("getOnuPonIndexByOnuId"), onuId);
    }

    @Override
    public void updateOnuOpticalInfo(OnuPonOptical onuOptical) {
        this.getSqlSession().update(getNameSpace("updateOnuOpticalInfo"), onuOptical);
    }

    @Override
    public void updateCmcOpticalInfo(OnuPonOptical onuOptical) {
        this.getSqlSession().update(getNameSpace("updateCmcOpticalInfo"), onuOptical);
    }

    @Override
    public OnuInfo queryOltOnuRelation(Long onuId) {
        return this.getSqlSession().selectOne(getNameSpace("getPerfOnuInfo"), onuId);
    }

    @Override
    public List<Long> queryOnuIdByEntityId(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("queryOnuIdByEntityId"), entityId);
    }

    @Override
    public void updateOnuOperationStatus(Long onuId, Integer status) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("onuId", onuId);
        paramsMap.put("operationStatus", status);
        this.getSqlSession().update(getNameSpace("updateOnuOperationStatus"), paramsMap);
    }

    @Override
    public Map<Long, Long> getOnuMap(Long entityId) {
        Map<Long, Long> onuMap = selectMapByKeyAndValue(getNameSpace() + "getOnuMap", entityId, "INDEX", "ID");
        return onuMap;
    }

    @Override
    public void batchUpdateOltOnuCapatilityExt(List<OltOnuCatv> oltOnuCatvs, List<OltOnuRstp> oltOnuRstps,
            List<OltOnuVoip> oltOnuVoips) {
        SqlSession sqlSession = getBatchSession();
        try {
            if (oltOnuCatvs != null && oltOnuCatvs.size() > 0) {
                for (OltOnuCatv catv : oltOnuCatvs) {
                    OltOnuAttribute oAttribute = new OltOnuAttribute();
                    oAttribute.setEntityId(catv.getEntityId());
                    oAttribute.setOnuIndex(catv.getOnuIndex());
                    catv.setOnuId((Long) sqlSession.selectOne(getNameSpace() + "getOnuId", oAttribute));
                    sqlSession.update(getNameSpace() + "updateOnuCatv", catv);
                }
            }
            if (oltOnuRstps != null && oltOnuRstps.size() > 0) {
                for (OltOnuRstp rstp : oltOnuRstps) {
                    OltOnuAttribute oAttribute = new OltOnuAttribute();
                    oAttribute.setEntityId(rstp.getEntityId());
                    oAttribute.setOnuIndex(rstp.getOnuIndex());
                    rstp.setOnuId((Long) sqlSession.selectOne(getNameSpace() + "getOnuId", oAttribute));
                    sqlSession.update(getNameSpace() + "updateOnuRstp", rstp);
                }
            }
            if (oltOnuVoips != null && oltOnuVoips.size() > 0) {
                for (OltOnuVoip voip : oltOnuVoips) {
                    OltOnuAttribute oAttribute = new OltOnuAttribute();
                    oAttribute.setEntityId(voip.getEntityId());
                    oAttribute.setOnuIndex(voip.getOnuIndex());
                    voip.setOnuId((Long) sqlSession.selectOne(getNameSpace() + "getOnuId", oAttribute));
                    sqlSession.update(getNameSpace() + "updateOnuVoip", voip);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public Onu getStandardOnuInfo(Long onuId) {
        Onu onu = (Onu) getSqlSession().selectOne(getNameSpace() + "getOnuByOnuId", onuId);
        OnuPonPort onuPonPort = (OnuPonPort) getSqlSession().selectOne(getNameSpace() + "getOltOnuPonAttribute", onuId);
        Long slotNo = EponIndex.getSlotNo(EponIndex.getOnuIndex(onu.getOnuIndex()));
        Long ponNo = EponIndex.getPonNo(EponIndex.getOnuIndex(onu.getOnuIndex()));
        // Long portId = (Long) getSqlSession().selectOne(getNameSpace() + "getPonIdByOnuId",
        // onuId);
        /*
         * OltPonOptical oltPonOptical = (OltPonOptical) getSqlSession().selectOne(
         * "com.topvision.ems.epon.optical.domain.Optical.getOltPonOptical", portId); //
         * 从性能采集数据中获取Olt pon口光信息 if (oltPonOptical != null) { OltOnuOpticalInfo oltOptical =
         * this.selectOltOunOpticalInfo(oltPonOptical.getEntityId(), oltPonOptical.getPortIndex());
         * if (oltOptical != null) { oltPonOptical.setTransPower(oltOptical.getPonOpticalTrans());
         * oltPonOptical.setRevPower(oltOptical.getPonOpticalRev()); } }
         * onu.setOltPonOptical(oltPonOptical);
         */
        // -------如果pon口没有采到，则不记录PON口------------//
        if (onuPonPort != null) {
            onuPonPort.setSlotNo(slotNo);
            onuPonPort.setPonNo(ponNo);
            /*
             * // 如果是CC8800A,从CMC的光链路信息中取得光发送与光接收功率 if (onu.getOnuPreType() != null &&
             * EponConstants.CC_ONUFLAG_TYPE.contains(Integer.valueOf(onu.getOnuPreType()))) {
             * OltOnuOpticalInfo onuOptical = this.selectOltCcmtsOpticalInfo(onuId,
             * onu.getOnuIndex()); if (onuOptical != null) {
             * onuPonPort.setOnuTransPower(onuOptical.getPonOpticalTrans());
             * onuPonPort.setOnuRevPower(onuOptical.getPonOpticalRev()); } }
             */
        } else {
            onuPonPort = new OnuPonPort();
            onuPonPort.setSlotNo(slotNo);
            onuPonPort.setPonNo(ponNo);
            /*
             * onuPonPort.setOnuTramsmittedOpticalPower(0);
             * onuPonPort.setOnuReceivedOpticalPower(0);
             */
        }
        onu.setOnuPonPort(onuPonPort);
        List<Long> onuUniIdList = getSqlSession().selectList("com.topvision.ems.epon.onu.domain.Uni.getOltUniIdList",
                onuId);
        // -------如果UNI口没有采到，则不记录UNI口------------//
        if (onuUniIdList != null) {
            List<OnuUniPort> onuUniPortList = new ArrayList<OnuUniPort>();
            for (Long anOnuUniIdList : onuUniIdList) {
                OnuUniPort onuUniPort = (OnuUniPort) getSqlSession()
                        .selectOne("com.topvision.ems.epon.onu.domain.Uni.getUniAttribute", anOnuUniIdList);
                OltUniExtAttribute uniExt = (OltUniExtAttribute) getSqlSession().selectOne(
                        "com.topvision.ems.epon.onu.domain.Uni.getOltUniExtAttribute", onuUniPort.getUniId());
                if (uniExt == null) {
                    continue;
                }
                if (uniExt.getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger() != null) {
                    onuUniPort.setAutoNegAdvertisedTechAbility(
                            uniExt.getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger().toString());
                }
                onuUniPortList.add(onuUniPort);
            }
            onu.setOnuUniPortList(onuUniPortList);
        }
        // TODO 查询ONU下连的CMC设备
        List<Entity> cmcList = entityDao.getSubEntityByEntityId(onuId);
        if (cmcList != null && !cmcList.isEmpty()) {
            onu.setCmcEntity(cmcList.get(0));
        }

        return onu;
    }

    @Override
    public Onu getOnuByMacAndParentId(String mac, Long parentId) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("mac", mac);
        paramsMap.put("parentId", parentId);
        return getSqlSession().selectOne(getNameSpace("getOnuByMacAndParentId"), paramsMap);
    }

    @Override
    public Long getPonIdByOnuIndex(Long entityId, Long onuIndex) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityId", entityId);
        paramsMap.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace("getPonIdByOnuIndex"), paramsMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.dao.OnuDao#getOnuAttributeByLoid(long, java.lang.String)
     */
    @Override
    public OltOnuAttribute getOnuAttributeByLoid(long entityId, String loid) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityId", entityId);
        paramsMap.put("loid", loid);
        paramsMap.put("loidWithMaohao", MacUtils.convertSnToMaohaoFormat(loid));
        return getSqlSession().selectOne(getNameSpace("getOnuAttributeByLoid"), paramsMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.dao.OnuDao#getOnuAttributeByMac(long, java.lang.String)
     */
    @Override
    public OltOnuAttribute getOnuAttributeByMac(long entityId, String macaddress) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityId", entityId);
        paramsMap.put("macaddress", macaddress);
        return getSqlSession().selectOne(getNameSpace("getOnuAttributeByMac"), paramsMap);
    }

    @Override
    public void batchUpdateOnuSoftVersion(Long entityId, List<OltOnuAttribute> onuAttrList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltOnuAttribute onuAttr : onuAttrList) {
                Long onuId = getOnuIdByIndex(entityId, onuAttr.getOnuIndex());
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("onuId", onuId);
                paramMap.put("onuSoftwareVersion", onuAttr.getOnuSoftwareVersion());
                sqlSession.update(getNameSpace() + "updateOnuSoftVersion", paramMap);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchUpdateOnuHardVersion(Long entityId, List<OltTopOnuCapability> onuCapabilityList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltTopOnuCapability onuAttr : onuCapabilityList) {
                Long onuId = getOnuIdByIndex(entityId, onuAttr.getOnuIndex());
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("onuId", onuId);
                paramMap.put("topOnuHardwareVersion", onuAttr.getTopOnuHardwareVersion());
                sqlSession.update(getNameSpace() + "updateOnuHardVersion", paramMap);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.dao.OnuDao#getAllOnuForAutoClean()
     */
    @Override
    public List<OltOnuAttribute> getAllOnuForAutoClean() {
        return getSqlSession().selectList("com.topvision.ems.epon.onu.dao.OnuClear.getAllOnuForAutoClean");
    }

    @Override
    public List<OltOnuAttribute> getAllCmcForAutoClean() {
        return getSqlSession().selectList("com.topvision.ems.epon.onu.dao.OnuClear.getAllCmcForAutoClean");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.dao.OnuDao#getAllOnuIndex()
     */
    @Override
    public List<OltOnuAttribute> getAllOnuIndex() {
        return getSqlSession().selectList("com.topvision.ems.epon.onu.dao.OnuClear.getAllOnuIndex");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.dao.OnuDao#updateOnuDeregisterInfo(java.util.List)
     */
    @Override
    public void updateOnuDeregisterInfo(List<OnuDeregisterTable> deregisterTables) {
        for (OnuDeregisterTable deregisterTable : deregisterTables) {
            if (deregisterTable.getTopOnuTimeSinceLastDeregister() != null
                    && deregisterTable.getTopOnuTimeSinceLastDeregister() != 0) {
                deregisterTable.updateLastDeregisterTime();
                try {
                    getSqlSession().update("com.topvision.ems.epon.onu.dao.OnuClear.updateOnuDeregisterInfo",
                            deregisterTable);
                } catch (Exception e) {
                    logger.error("updateOnuDeregisterInfo error:", e);
                }
                /*
                 * try { getSqlSession().update(
                 * "com.topvision.ems.epon.onu.dao.OnuClear.updateCmcDeregisterInfo",
                 * deregisterTable); } catch (Exception e) { logger.error(
                 * "updateOnuDeregisterInfo error:", e); }
                 */
            }
        }
    }

    @Override
    public Integer getOnuCountByOnuId(Long onuId) {
        return getSqlSession().selectOne(getNameSpace() + "getOnuCountByOnuId", onuId);
    }

    @Override
    public void updateOnuLastDeregisterTime(List<OltOnuAttribute> oltOnuAttributes) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltOnuAttribute onuAttribute : oltOnuAttributes) {
                sqlSession.update(getNameSpace("updateOnuLastDeregisterTime"), onuAttribute);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.dao.OnuDao#getOnuTl1InfoByIndex(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public com.topvision.nbi.tl1.api.domain.OnuInfo getOnuTl1InfoByIndex(Long entityId, Long onuIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace() + "getOnuTl1InfoByIndex", paramMap);
    }

    @Override
    public Integer selectEponOnuCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectEponOnuCount"), queryMap);
    }

    @Override
    public Integer selectGponOnuCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectGponOnuCount"), queryMap);
    }

    @Override
    public void updateOnuDeactive(Long onuId, Integer onuDeactive) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("onuId", onuId);
        map.put("onuDeactive", onuDeactive);
        getSqlSession().update(getNameSpace("updateOnuDeactive"), map);
    }

}
