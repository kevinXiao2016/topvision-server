/**
 * 
 */
package com.topvision.ems.template.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.service.Service;

/**
 * 设备类型模板管理.
 * 
 * @author niejun
 */
public interface EntityTypeService extends Service {

    /**
     * 所有的数据库支持的EntityType，带上license属性，用于设备类型模糊匹配
     * 
     * @return
     */
    List<EntityType> getAllEntityTypeWithLicenseSupport();

    /**
     * 根据sysObjectID得到相对应的厂商ID.
     * 
     * @param sysObjectID
     * @return
     */
    long getCorpBySysObjId(String sysObjectID);

    /**
     * 根据displayName获得设备类型
     * 
     * @param displayName
     * @return
     */
    EntityType getEntityTypeByDisplayName(String displayName);

    /**
     * 得到给定类型ID的设备类型.
     * 
     * @param typeId
     * @return @
     */
    EntityType getEntityType(long typeId);

    /**
     * 根据sysObjectID得到模板.
     * 
     * @param sysObjectID
     * @return @
     */
    EntityType getEntityTypeBySysObjId(String sysObjectID);

    /**
     * 根据sysObjectID 和 standard(制式) 得到设备类型
     * 
     * @param sysObjectID
     * @param standard
     * @return
     */
    EntityType getEntityTypeBySysObjId(String sysObjectID, Integer standard);

    /**
     * 获取类型集合
     * 
     * @param type
     * @return @
     */
    List<EntityType> loadSubType(Long type);

    /**
     * 获取类型所有集合,包括license不支持的设备类型
     * 
     * @param type
     * @return @
     */
    List<EntityType> loadAllSubType(Long type);

    /**
     * 判断是否为基本设备类型
     * 
     * @param typeId
     * @return @
     */
    boolean isUnkownType(Long typeId);

    /**
     * 判断是否为基本设备类型
     *
     * @param typeId
     * @return @
     */
    boolean isBaseType(Long typeId);

    /**
     * 判断是否为分类设备类型
     * 
     * @param typeId
     * @return @
     */
    boolean isCategoryType(Long typeId);

    /**
     * 判断是否为olt类型
     * 
     * @param typeId
     * @return @
     */
    boolean isOlt(Long typeId);

    /**
     * 判断是否为cc类型
     * 
     * @param typeId
     * @return @
     */
    boolean isCcmts(Long typeId);

    /**
     * 判断是否为onu类型
     * 
     * @param typeId
     * @return @
     */
    boolean isOnu(Long typeId);

    /**
     * 判断是否为cmts类型
     * 
     * @param typeId
     * @return @
     */
    boolean isCmts(Long typeId);

    /**
     * 判断是否为带IP设备类型
     * 
     * @param typeId
     * @return @
     */
    boolean isEntityWithIp(Long typeId);

    /**
     * 判断是否为cc或cmts
     * 
     * @param typeId
     * @return @
     */
    boolean isCcmtsAndCmts(Long typeId);

    /**
     * 判断是否为拆分型CC
     * 
     * @param typeId
     * @return @
     */
    boolean isCcmtsWithAgent(Long typeId);

    /**
     * 判断是否为集成型CC
     * 
     * @param typeId
     * @return @
     */
    boolean isCcmtsWithoutAgent(Long typeId);

    /**
     * 判断是否为bsr cmts
     * 
     * @param typeId
     * @return @
     */
    boolean isBsrCmts(Long typeId);

    /**
     * 判断是否为ubr cmts
     * 
     * @param typeId
     * @return @
     */
    boolean isUbrCmts(Long typeId);

    /**
     * 判断是否为casa cmts
     *
     * @param typeId
     * @return @
     */
    boolean isCasaCmts(Long typeId);

    /**
     * 判断是否为casa cmts
     *
     * @param typeId
     * @return @
     */
    boolean isArrisCmts(Long typeId);

    /**
     * 判断是否为casa cmts
     *
     * @param typeId
     * @return @
     */
    boolean isOpticalReceiver(Long typeId);

    /**
     * 获取基本类型type值
     * @return
     */
    Long getBaseType();

    /**
     * 获取分类类型type值
     * @return
     */
    Long getCategoryType();

    /**
     * 获取License group type值
     * @return
     */
    Long getLicenseGroupType();

    /**
     * 获取Network group type值
     * @return
     */
    Long getNetworkGroupType();

    /**
     * 获取olt类型type值
     * @return
     */
    Long getOltType();

    /**
     * 获取onu类型type值
     * @return
     */
    Long getOnuType();

    /**
     * 获取cc类型type值
     * @return
     */
    Long getCcmtsType();

    /**
     * 获取cmts类型type值
     * @return
     */
    Long getCmtsType();

    /**
     * 获取带IP设备类型type值
     * @return
     */
    Long getEntitywithipType();

    /**
     * 获取cc，cmts类型type值
     * @return
     */
    Long getCcmtsandcmtsType();

    /**
     * 获取集成型cc类型type值
     * @return
     */
    Long getCcmtswithagentType();

    /**
     * 获取拆分型cc类型type值
     * @return
     */
    Long getCcmtswithoutagentType();

    /**
     * 获取cc类型type值
     * @return
     */
    Long getCcmts8800AType();

    /**
     * 获取bse cmts类型type值
     * @return
     */
    Long getBsrcmtsType();

    /**
     * 获取ubr cmts类型type值
     * @return
     */
    Long getUbrcmtsType();

    /**
     * 获取casa cmts类型type值
     * @return
     */
    Long getCasacmtsType();

    /**
     * 获取casa cmts类型type值
     * @return
     */
    Long getOpticalReceiverType();

    /**
     * 获取设备类型map<type, List<typeId>>   
     * 
     * @return
     */
    Map<Long, List<Long>> getEntityTypeMap();

    /**
     * 通过entityTypeId获得License分组的TypeId
     * @param typeId
     * @return
     */
    Long getEntityLicenseGroupIdByEntityTypeId(long typeId);

    /**
     * 获取License管理中需要管理的entityType列表
     * @return
     */
    List<EntityType> getLicenseGroupEntityTypes();

    /**
     * 通过entityTypeId获得Network分组的TypeId
     * @param typeId
     * @return
     */
    Long getEntityNetworkGroupIdByEntityTypeId(Long typeId);

    /**
     * 获取Network管理中需要管理的entityType列表
     * @return
     */
    List<EntityType> getNetworkGroupEntityTypes();

    /**
     * 是不是pn8603
     * @param typeId
     * @return
     */
    boolean isPN8603Type(long typeId);

    /**
     * 是不是pn8602
     * @param typeId
     * @return
     */
    boolean isPN8602Type(long typeId);

    /**

     * 是不是pn8601
     * @param typeId
     * @return
     */
    boolean isPN8601Type(long typeId);

    /**
     * 是不是pn8602-E
     * 
     * @param typeId
     * @return
     */
    boolean isPN8602_EType(long typeId);

    /**
     * 是不是pn8602-EF
     * 
     * @param typeId
     * @return
     */
    boolean isPN8602_EFType(long typeId);

    /**
     * 是不是pn8602-G
     * 
     * @param typeId
     * @return
     */
    boolean isPN8602_GType(long typeId);

    /**
     * 是不是CC8800A和CC8800B
     * @param typeId
     * @return
     */
    boolean isCCMTSAOrBType(long typeId);

    /**
     * 是不是CC8800S
     * @param typeId
     * @return
     */
    boolean isCCMTS8800SType(long typeId);

    /**
     * EntityType - E
     * 
     * @param typeId
     * @return
     */
    boolean isCCMTS8800EType(long typeId);

    /**
     * 根据设备类型获取设备大类型
     * @param typeId
     * @return
     */
    EntityType getNetWorkGroupType(Long typeId);

    /**
     * 根据设备类型获取设备所属的分类类型
     * @param typeId
     * @return
     */
    List<Long> getCategoryTypeByTypeId(Long typeId);

    /**
     * 获取指定类型下的子类型typeId集合
     * @param type
     * @return
     */
    List<Long> getSubTypeIdList(Long type);

    /**
     * 是不是标准OLT类型
     * @param typeId
     * @return
     */
    boolean isStandardOlt(long typeId);

    /**
     * 判断是否为C-E设备
     * @param typeId
     * @return
     */
    boolean isCCMTS8800CEType(long typeId);

}
