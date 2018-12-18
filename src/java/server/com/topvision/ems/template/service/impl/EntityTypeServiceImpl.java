/**
 * 
 */
package com.topvision.ems.template.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.EntityTypeStandard;
import com.topvision.ems.network.domain.EntityTypeRelation;
import com.topvision.ems.template.dao.EntityTypeDao;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.message.event.LicenseChangeEvent;
import com.topvision.platform.message.event.LicenseChangeListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.zetaframework.var.ZetaValueGetter;

/**
 * @author niejun
 * 
 */
@ZetaValueGetter("EntityType")
@Service("entityTypeService")
public class EntityTypeServiceImpl extends BaseService implements EntityTypeService, LicenseChangeListener {
    @Autowired
    private EntityTypeDao entityTypeDao;
    private List<EntityType> entityTypes = new ArrayList<EntityType>();
    // private Map<String, EntityType> sysObjIdMapping = new HashMap<String, EntityType>();
    private Map<String, Map<Integer, EntityType>> sysObjIdMapping = new HashMap<String, Map<Integer, EntityType>>();
    private final Map<Long, EntityType> idMapping = new HashMap<Long, EntityType>();
    // 所有设备类型，包括license不支持的 add by loyal
    private final Map<Long, EntityType> allIdMapping = new HashMap<Long, EntityType>();

    private List<EntityTypeRelation> entityTypeRelationList = new ArrayList<EntityTypeRelation>();;
    private Map<Long, List<Long>> entityTypeMap = new HashMap<Long, List<Long>>();
    private Map<Long, List<Long>> allEntityTypeMap = new HashMap<Long, List<Long>>();
    private Map<Long, Long> entityLicenseGroupTypeMap = new HashMap<Long, Long>();
    private Map<Long, Long> entityNetworkGroupTypeMap = new HashMap<Long, Long>();

    public static final Long Unkown_Type = -1l;// 基本类型
    public static final Long Base_Type = 1l;// 基本类型
    public static final Long Category_Type = 2l;// 分类类型
    public static final Long License_Type = 3l;// LicenseGroup
    public static final Long Network_Type = 4l;// LicenseGroup
    public static final Long OLT = 10000l;
    public static final Long ONU = 13000l;
    public static final Long CCMTS = 30000l;
    public static final Long CMTS = 40000l;
    public static final Long ENTITYWITHIP = 50000l;
    public static final Long CCMTSANDCMTS = 60000l;
    public static final Long CCMTSWITHAGENT = 70000l;
    public static final Long CCMTSWITHOUTAGENT = 80000l;
    public static final Long BSRCMTS = 90000l;
    public static final Long UBRCMTS = 100000l;
    public static final Long CASACMTS = 110000l;
    public static final Long ARRISCMTS = 140000l;
    public static final Long OPTICALRECEIVER = 120000l;

    public static final Long CCMTS8800A = 30001l;
    public static final Long CCMTS8800B = 30002l;
    public static final Long CCMTS8800C_A = 30005L;
    public static final Long CCMTS8800S = 30007l;
    public static final Long PN8601 = 10001l;
    public static final Long PN8602 = 10002l;
    public static final Long PN8603 = 10003l;
    public static final Long PN8602_E = 10005L;
    public static final Long PN8602_EF = 10006L;
    public static final Long PN8602_G = 10007L;

    public static final long CCMTS8800E = 30010l;
    public static final long CCMTS8800C_E = 30011l;
    public static final long CCMTS8800D_E = 30012l;
    public static final long CCMTS8800C_10G = 30020l;
    public static final long CCMTS8800F = 30022l;

    // TODO 标准OLT大类型，目前开发使用，类型需要统一规划
    private static final long STANDARD_OLT = 11000L;

    @Autowired
    private LicenseIf licenseIf;
    @Autowired
    private MessageService messageService;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(LicenseChangeListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(LicenseChangeListener.class, this);
        // modify by bravin@20160520:类似这种非常核心而且被缓存的数据需要在服务器一开始启动的时候加入内存，而不是等待服务器启动完成后再启动

        // Modify by Victor@20160713张东明解决问题时把start移到initialize，导致licenseChanged调用start失效，
        // 现在把原来start内容移到licenseChanged中去，initialize方法调用licenseChanged进行初始化
        licenseChanged(null);
    }

    @Override
    public void stop() {
        entityTypes.clear();
        sysObjIdMapping.clear();
        idMapping.clear();
        allIdMapping.clear();
        entityTypeRelationList.clear();
        entityTypeMap.clear();
        allEntityTypeMap.clear();
    }

    @Override
    public List<EntityType> getAllEntityTypeWithLicenseSupport() {
        List<EntityType> result = new ArrayList<>();
        for (Entry<Long, EntityType> entry : allIdMapping.entrySet()) {
            EntityType entityType = entry.getValue();
            if (sysObjIdMapping.containsKey(entityType.getSysObjectID())) {
                entityType.setLicenseSupport(true);
            }
            result.add(entityType);
        }
        return result;
    }

    @Override
    public long getCorpBySysObjId(String sysObjectID) {
        try {
            String corp = sysObjectID.substring(sysObjectID.indexOf(EntityIdentifyImpl.SOID_HEAD)
                    + EntityIdentifyImpl.SOID_HEAD.length());
            long l = Long.parseLong(corp.substring(0, corp.indexOf('.')));
            return l == 0 ? Entity.UNKNOWN : l;
        } catch (Exception ex) {
            return Entity.UNKNOWN;
        }
    }

    @Override
    public EntityType getEntityType(long typeId) {
        return idMapping.get(typeId);
    }

    @Override
    public EntityType getEntityTypeBySysObjId(String sysObjId) {
        if (sysObjIdMapping.get(sysObjId) != null) {
            return sysObjIdMapping.get(sysObjId).get(EntityTypeStandard.NORMAL_STANDARD);
        }
        return getEntityTypeBySysObjIdPre(sysObjId);
    }

    public EntityType getEntityTypeBySysObjIdPre(String sysObjId) {
        int max = -1;
        EntityType result = null;
        /*for (Entry<String, Map<Integer, EntityType>> entry : sysObjIdMapping.entrySet()) {
            if (entry.getKey().length() > 0 && sysObjId.contains(entry.getKey())) {
                if (entry.getKey().length() > max) {
                    result = entry.getValue().get(EntityTypeStandard.NORMAL_STANDARD);
                    max = entry.getKey().length();
                }
            }
        }*/

        List<EntityType> entityTypes = getAllEntityTypeWithLicenseSupport();
        for (EntityType tmp : entityTypes) {
            String entrySysOid = tmp.getSysObjectID();
            if (entrySysOid.length() > 0 && sysObjId.contains(entrySysOid)) {
                if (entrySysOid.length() > max) {
                    result = tmp;
                    max = entrySysOid.length();
                }
            }
        }
        if (result != null && !result.isLicenseSupport()) {
            result = null;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.service.EntityTypeService#getEntityTypeBySysObjId(java.lang.
     * String, java.lang.Integer)
     */
    @Override
    public EntityType getEntityTypeBySysObjId(String sysObjectID, Integer standard) {
        if (standard == null) {
            return getEntityTypeBySysObjId(sysObjectID);
        }
        Map<Integer, EntityType> standardMap = sysObjIdMapping.get(sysObjectID);
        if (standardMap == null) {
            return null;
        }
        if (standardMap.size() == 1) {
            return getEntityTypeBySysObjId(sysObjectID);
        }
        return sysObjIdMapping.get(sysObjectID).get(standard);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.template.service.EntityTypeService#getEntityTypeByDisplayName(java.lang.String)
     */
    @Override
    public EntityType getEntityTypeByDisplayName(String displayName) {
        for (EntityType entityType : entityTypes) {
            if (entityType.getDisplayName().equals(displayName)) {
                return entityType;
            }
        }
        return null;
    }

    // 获取类型集合
    @Override
    public List<EntityType> loadSubType(Long type) {
        List<EntityType> types = new ArrayList<EntityType>();
        List<Long> typeIdList = entityTypeMap.get(type);
        if (typeIdList != null) {
            Collections.sort(typeIdList);
            for (Long typeId : typeIdList) {
                types.add(idMapping.get(typeId));
            }
        }
        return types;
    }

    // 获取类型所有集合,包括license不支持的设备类型 add by loyal
    public List<EntityType> loadAllSubType(Long type) {
        List<EntityType> types = new ArrayList<EntityType>();
        List<Long> typeIdList = allEntityTypeMap.get(type);
        if (typeIdList != null) {
            Collections.sort(typeIdList);
            for (Long typeId : typeIdList) {
                types.add(allIdMapping.get(typeId));
            }
        }
        return types;
    }

    // 判断typeId是否属于type
    private boolean is(Long typeId, Long type) {
        if (type.equals(typeId)) {
            return true;
        }
        List<Long> tmp = entityTypeMap.get(type);
        if (tmp != null && tmp.contains(typeId)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isUnkownType(Long typeId) {
        return typeId == Unkown_Type;
    }

    @Override
    public boolean isBaseType(Long typeId) {
        return is(typeId, Base_Type);
    }

    @Override
    public boolean isCategoryType(Long typeId) {
        return is(typeId, Category_Type);
    }

    @Override
    public boolean isOlt(Long typeId) {
        return is(typeId, OLT);
    }

    @Override
    public boolean isCcmts(Long typeId) {
        return is(typeId, CCMTS);
    }

    @Override
    public boolean isOnu(Long typeId) {
        return is(typeId, ONU);
    }

    @Override
    public boolean isCmts(Long typeId) {
        return is(typeId, CMTS);
    }

    @Override
    public boolean isEntityWithIp(Long typeId) {
        return is(typeId, ENTITYWITHIP);
    }

    @Override
    public boolean isCcmtsAndCmts(Long typeId) {
        return is(typeId, CCMTSANDCMTS);
    }

    @Override
    public boolean isCcmtsWithAgent(Long typeId) {
        return is(typeId, CCMTSWITHAGENT);
    }

    @Override
    public boolean isCcmtsWithoutAgent(Long typeId) {
        return is(typeId, CCMTSWITHOUTAGENT);
    }

    @Override
    public boolean isBsrCmts(Long typeId) {
        return is(typeId, BSRCMTS);
    }

    @Override
    public boolean isUbrCmts(Long typeId) {
        return is(typeId, UBRCMTS);
    }

    @Override
    public boolean isCasaCmts(Long typeId) {
        return is(typeId, CASACMTS);
    }

    @Override
    public boolean isArrisCmts(Long typeId) {
        return is(typeId, ARRISCMTS);
    }

    @Override
    public boolean isOpticalReceiver(Long typeId) {
        return is(typeId, OPTICALRECEIVER);
    }

    @Override
    public boolean isPN8603Type(long typeId) {
        return typeId == PN8603;
    }

    @Override
    public boolean isPN8602_EType(long typeId) {
        return typeId == PN8602_E;
    }

    @Override
    public boolean isPN8602_EFType(long typeId) {
        return typeId == PN8602_EF;
    }

    @Override
    public boolean isPN8602_GType(long typeId) {
        return typeId == PN8602_G;
    }

    @Override
    public boolean isPN8602Type(long typeId) {
        return typeId == PN8602;
    }

    @Override
    public boolean isPN8601Type(long typeId) {
        return typeId == PN8601;
    }

    @Override
    public boolean isCCMTSAOrBType(long typeId) {
        return typeId == CCMTS8800A || typeId == CCMTS8800B;
    }

    @Override
    public boolean isCCMTS8800SType(long typeId) {
        return typeId == CCMTS8800S;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.service.EntityTypeService#isCCMTS8800EType(long)
     */
    @Override
    public boolean isCCMTS8800EType(long typeId) {
        return typeId == CCMTS8800E || typeId == CCMTS8800C_E || typeId == CCMTS8800D_E || typeId == CCMTS8800C_10G
                || typeId == CCMTS8800F;
    }

    @Override
    public boolean isCCMTS8800CEType(long typeId) {
        return typeId == CCMTS8800C_E;
    }

    // 获取type值
    @Override
    public Long getBaseType() {
        return Base_Type;
    }

    @Override
    public Long getCategoryType() {
        return Category_Type;
    }

    @Override
    public Long getLicenseGroupType() {
        return License_Type;
    }

    @Override
    public Long getNetworkGroupType() {
        return Network_Type;
    }

    @Override
    public Long getOltType() {
        return OLT;
    }

    @Override
    public Long getOnuType() {
        return ONU;
    }

    @Override
    public Long getCcmtsType() {
        return CCMTS;
    }

    @Override
    public Long getCmtsType() {
        return CMTS;
    }

    @Override
    public Long getEntitywithipType() {
        return ENTITYWITHIP;
    }

    @Override
    public Long getCcmtsandcmtsType() {
        return CCMTSANDCMTS;
    }

    @Override
    public Long getCcmtswithagentType() {
        return CCMTSWITHAGENT;
    }

    @Override
    public Long getCcmtswithoutagentType() {
        return CCMTSWITHOUTAGENT;
    }

    @Override
    public Long getCcmts8800AType() {
        return CCMTS8800A;
    }

    @Override
    public Long getBsrcmtsType() {
        return BSRCMTS;
    }

    @Override
    public Long getUbrcmtsType() {
        return UBRCMTS;
    }

    @Override
    public Long getCasacmtsType() {
        return CASACMTS;
    }

    @Override
    public Long getOpticalReceiverType() {
        return OPTICALRECEIVER;
    }

    @Override
    public Map<Long, List<Long>> getEntityTypeMap() {
        return entityTypeMap;
    }

    @Override
    public Long getEntityLicenseGroupIdByEntityTypeId(long typeId) {
        if (entityLicenseGroupTypeMap.containsKey(typeId)) {
            return entityLicenseGroupTypeMap.get(typeId);
        } else {
            return -1L;
        }
    }

    @Override
    public List<EntityType> getLicenseGroupEntityTypes() {
        return entityTypeDao.selectEntityTypesByTypeGroup(License_Type);
    }

    @Override
    public Long getEntityNetworkGroupIdByEntityTypeId(Long typeId) {
        if (entityNetworkGroupTypeMap.containsKey(typeId)) {
            return entityNetworkGroupTypeMap.get(typeId);
        } else {
            return -1L;
        }
    }

    @Override
    public List<EntityType> getNetworkGroupEntityTypes() {
        return entityTypeDao.selectEntityTypesByTypeGroup(Network_Type);
    }

    @Override
    public EntityType getNetWorkGroupType(Long typeId) {
        if (isCategoryType(typeId)) {
            return idMapping.get(typeId);
        }
        Long type;
        if (entityNetworkGroupTypeMap.containsKey(typeId)) {
            type = entityNetworkGroupTypeMap.get(typeId);
        } else {
            type = -1L;
        }
        return idMapping.get(type);
    }

    /**
     * handle sysObjIdMapping
     * 
     * @param entityType
     */
    private void insertSysObjIdMapping(EntityType entityType) {
        if (sysObjIdMapping.get(entityType.getSysObjectID()) != null) {
            Map<Integer, EntityType> sMap = sysObjIdMapping.get(entityType.getSysObjectID());
            sMap.put(entityType.getStandard(), entityType);
        } else {
            Map<Integer, EntityType> sMap = new HashMap<>();
            sMap.put(entityType.getStandard(), entityType);
            sysObjIdMapping.put(entityType.getSysObjectID(), sMap);
        }
    }

    @Override
    public void licenseChanged(LicenseChangeEvent evt) {
        stop();
        try {
            // 保持设备类型和分类在内存中
            entityTypes = entityTypeDao.selectByMap(null);
            // 根据模块对entitytype进行过滤
            // Map<String, Database> modules = UserContextManager.getInstance().getModules();
            getLogger().info("start loadEntityType info");
            int size = entityTypes.size();
            for (int i = 0; i < size; i++) {
                EntityType t = entityTypes.get(i);
                allIdMapping.put(t.getTypeId(), t);
                if ("network".equals(t.getModule())
                        || (licenseIf.isSupportModule(t.getModule()) && licenseIf.isSupportDeviceOrParent(t.getName(),
                                t.getModule()))) {
                    // sysObjIdMapping.put(t.getSysObjectID(), t);
                    insertSysObjIdMapping(t);
                    getLogger().info("loadEntityType info {}", t);
                    idMapping.put(t.getTypeId(), t);
                    t.setDisabled(false);
                } else {
                    t.setDisabled(true);
                }
            }
            entityTypeDao.updateEntity(entityTypes);
            getLogger().info("end loadEntityType info");
            // 维护licenseGroup的relation
            List<EntityType> licenseGroupEntityTypes = getLicenseGroupEntityTypes();
            List<Long> licenseGroupEntityTypeIds = new ArrayList<Long>();
            for (EntityType entityType : licenseGroupEntityTypes) {
                if (!entityType.getName().equals(entityType.getModule())) {
                    continue;
                }
                licenseGroupEntityTypeIds.add(entityType.getTypeId());
            }
            // 维护networkGroup的relation
            List<EntityType> networkGroupEntityTypes = getNetworkGroupEntityTypes();
            List<Long> networkGroupEntityTypeIds = new ArrayList<Long>();
            for (EntityType entityType : networkGroupEntityTypes) {
                networkGroupEntityTypeIds.add(entityType.getTypeId());
            }
            entityTypeRelationList = entityTypeDao.selectEntityTypeRelation();
            for (EntityTypeRelation entityTypeRelation : entityTypeRelationList) {
                Long $type = entityTypeRelation.getType();
                Long $typeId = entityTypeRelation.getTypeId();
                if (idMapping.get($typeId) != null) {
                    if (entityTypeMap.get($type) == null) {
                        entityTypeMap.put($type, new ArrayList<Long>());
                    }
                    entityTypeMap.get($type).add($typeId);
                }
                // 维护licenseGroup的relation
                if (licenseGroupEntityTypeIds.contains($type)) {
                    entityLicenseGroupTypeMap.put($typeId, $type);
                }
                // 维护networkGroup的relation
                if (networkGroupEntityTypeIds.contains($type)) {
                    entityNetworkGroupTypeMap.put($typeId, $type);
                }
            }

            for (EntityTypeRelation entityTypeRelation : entityTypeRelationList) {
                Long $type = entityTypeRelation.getType();
                Long $typeId = entityTypeRelation.getTypeId();
                if (allIdMapping.get($typeId) != null) {
                    if (allEntityTypeMap.get($type) == null) {
                        allEntityTypeMap.put($type, new ArrayList<Long>());
                    }
                    allEntityTypeMap.get($type).add($typeId);
                }
            }
        } catch (Exception ex) {
            getLogger().error("loadEntityType in initialze:", ex);
        }
    }

    @Override
    public List<Long> getCategoryTypeByTypeId(Long typeId) {
        return entityTypeDao.queryCategoryTypeByTypeId(typeId);
    }

    @Override
    public List<Long> getSubTypeIdList(Long type) {
        return entityTypeMap.get(type);
    }

    @Override
    public boolean isStandardOlt(long typeId) {
        return is(typeId, STANDARD_OLT);
    }

}
