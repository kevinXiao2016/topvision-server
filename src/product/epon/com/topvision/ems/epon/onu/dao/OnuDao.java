/***********************************************************************
 * $Id: OnuDao.java,v1.0 2013-10-25 上午11:04:40 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.OltOnuTypeInfo;
import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.onu.domain.CC8800ABaseInfo;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuCatv;
import com.topvision.ems.epon.onu.domain.OltOnuOpticalInfo;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltOnuVoip;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onu.domain.OnuBaseInfo;
import com.topvision.ems.epon.onu.domain.OnuDeregisterTable;
import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-上午11:04:40
 *
 */
public interface OnuDao extends BaseEntityDao<Object> {

    /**
     * Add by Rod
     * 
     * 获得全网ONU/CMC信息，用于更新最后一次下线时间,
     * 
     * 查询字段只包括onuIndex以及entityId
     * 
     * 查询表为OltOnuRelation
     * 
     * @return
     */
    List<OltOnuAttribute> getAllOnuIndex();

    /**
     * 获取ONU的type列表，
     * 
     * @param type
     * @return
     * @throws SQLException
     */
    List<EntityType> getOnuTypes(Long type) throws SQLException;

    /**
     * 根据查询条件获得ONU的集合
     * 
     * @param slotId
     *            槽位ID
     * @param portId
     *            端口ID
     * @param llid
     *            ONU对应LLID
     * @param mac
     *            ONU MAC
     * @return List<OltOnuAttribute>
     */
    List<OltOnuAttribute> getOnuList(Long entityId, Long slotId, Long portId, Integer llid, String mac, String onuName);

    /**
     * 获取onuList通过ponId return List<OltOnuAttribute>
     */
    List<OltOnuAttribute> getOnuListByPonId(Long ponId);

    /**
     * 根据ID获得特定的ONU
     * 
     * @param onuId
     *            ONU ID
     * @return OltOnuAttribute
     */
    OltOnuAttribute getOnuEntityById(Long onuId);

    /**
     * 获得ONU上的PON板属性(光模块)
     * 
     * @param onuPonId
     *            ONU PON ID
     * @return OltOnuPonAttribute
     */
    OltOnuPonAttribute getOnuPonAttribute(Long onuPonId);

    /**
     * 更新ONU别名
     * 
     * @param onuId
     *            ONU ID
     * @param onuName
     *            ONU别名
     */
    void updateOnuName(Long onuId, String onuName);

    /**
     * 更新ONU的基本信息
     * 
     * @param oltOnuAttribute
     *            ONU属性
     */
    void updateOnuAttribute(OltOnuAttribute oltOnuAttribute);

    /**
     * 插入更新ONU上PON口基本信息（光模块）
     * 
     * @param oltOnuPonAttribute
     *            ONU上PON口属性（光模块）
     */
    void insertOrUpdateOltOnuPonAttribute(OltOnuPonAttribute oltOnuPonAttribute);

    /**
     * 同步下级设备数据
     * 
     * @param list
     *            ONU属性列表
     * @param oltMap
     *            oltMap
     */
    void syncSubordinateEntityAttribute(List<OltOnuAttribute> existOnuAttributes, List<OltOnuAttribute> list,
            HashMap<Long, Long> oltMap, Entity entity);

    /**
     * Create Entity Relation And Topo Link
     * 
     * @param onuEntity
     * @param subEntity
     */
    void createEntityRelationAndLink(Entity onuEntity, Entity subEntity);

    /**
     * Delete Sub Entity Relation
     * 
     */
    void deleteEntityRelationAndLink(Long entityId);

    /**
     * 更新ONU的Index相关信息
     * 
     * @param entityId
     */
    void synOnuIndexForCmcAtVersionUpdate(long entityId);

    /**
     * syncOltOnuAttribute For Single Onu Topology
     * 
     * 
     * @param oltOnuAttribute
     */
    void syncOltOnuAttribute(OltOnuAttribute oltOnuAttribute);

    /**
     * syncOnuPonAttribute
     * 
     * @param list
     * @param onuMap
     */
    void syncOnuPonAttribute(List<OltOnuPonAttribute> list, HashMap<Long, Long> onuMap);

    /**
     * syncOnuCapatility
     * 
     * @param onuList
     * @param topList
     * @param onuMap
     */
    void syncOnuCapatility(List<OltOnuCapability> onuList, List<OltTopOnuCapability> topList,
            HashMap<Long, Long> onuMap);

    /**
     * 获得ONU端口关系
     * 
     * @param entityId
     *            设备ID
     * @return HashMap<Long, Long>
     */
    Map<Long, Long> getOnuMap(Long entityId);

    /**
     * 获取ONU结构关系
     * 
     * @param onuId
     *            ONU ID
     * @return Onu
     */
    Onu getOnuStructure(Long onuId);

    /**
     * 获取ONU索引
     * 
     * @param onuId
     *            ONU ID
     * @return Long
     */
    Long getOnuIndex(Long onuId);

    /**
     * 获取ONU ID
     * 
     * @param entityId
     *            设备索引号
     * @param onuIndex
     *            ONU 索引号
     * 
     * @return Long
     */
    Long getOnuIdByIndex(Long entityId, Long onuIndex);

    /**
     * 获取ONU ID
     * 
     * @param ponId
     * 
     * @param entityId
     *            设备索引号
     * @return Long
     */
    List<Long> getOnuIdByPonId(Long entityId, Long ponId);

    /**
     * 获取ONU ID
     * 
     * @param ponId
     * 
     * @param entityId
     *            设备索引号
     * @return Long
     */
    List<Long> getOnuIdsByPonIds(Long entityId, List<Long> ponIds);

    /**
     * 获得ONU PON端口属性
     * 
     * @param onuId
     *            ONU ID
     * @return
     */
    OltOnuPonAttribute getOnuPonAttributeByOnuId(Long onuId);

    /**
     * 设置onu温度检测使能
     * 
     * @param onuId
     *            onu Id
     * @param onuTemperatureDetectEnable
     *            使能状态
     */
    void updateOnuTemperatureDetectEnable(Long onuId, Integer onuTemperatureDetectEnable);

    /**
     * 设置onu FEC使能
     * 
     * @param onuId
     *            onu Id
     * @param onuFecEnable
     *            使能状态
     */
    void updateOnuFecEnable(Long onuId, Integer onuFecEnable);

    /**
     * 设置onu 端口隔离使能
     * 
     * @param onuId
     *            onu Id
     * @param onuIsolationEnable
     *            使能状态
     */
    void updateOnuIsolationEnable(Long onuId, Integer onuIsolationEnable);

    /**
     * 设置onu 15分钟使能
     * 
     * @param onuId
     *            onu Id
     * @param onu15minEnable
     *            使能状态
     */
    void updateOnu15minEnable(Long onuId, Integer onu15minEnable);

    /**
     * 设置onu 24小时使能
     * 
     * @param onuId
     *            onu Id
     * @param onu24hEnable
     *            使能状态
     */
    void updateOnu24hEnable(Long onuId, Integer onu24hEnable);

    /**
     * 设置onu MAC地址学习数
     * 
     * @param onuId
     *            onu Id
     * @param macMaxNum
     *            mac学习数
     */
    void updateOnuMacMaxNum(Long onuId, Integer macMaxNum);

    /**
     * 设置onu RSTP
     * 
     * @param onuId
     *            onu Id
     * @param onuRstpBridgeMode
     *            rstp模式
     */
    void updateOnuRstpBridgeMode(Long onuId, Integer onuRstpBridgeMode);

    /**
     * 获取ONU能力参数
     * 
     * @param onuId
     *            ONU口ID
     * 
     * @return OltOnuCapability
     */
    OltOnuCapability getOltOnuCapability(Long onuId);

    /**
     * 获取ONU私有MIB能力参数
     * 
     * @param onuId
     *            ONU口ID
     * 
     * @return OltTopOnuCapability
     */
    OltTopOnuCapability getOltTopOnuCapability(Long onuId);

    /**
     * 获取RSTP参数
     * 
     * @param onuId
     *            ONU口ID
     * 
     * @return OltOnuRstp
     */
    OltOnuRstp getOltOnuRstpByOnuId(Long onuId);

    /**
     * 修改ONU温度
     * 
     * @param onuId
     * @param onuTemperature
     */
    void updateOnuTemperature(Long onuId, Integer temperature);

    /**
     * 获得ONU类型基本信息
     * 
     * @param onuTypeId
     * @return
     */
    OltOnuTypeInfo getOnuTypeInfo(Integer onuTypeId);

    /**
     * 获得ONU的基本信息 （onuIndex）
     * 
     * @param entityId
     * @param onuIndex
     * @return
     */
    OltOnuAttribute getOnuAttributeByIndex(Long entityId, Long onuIndex);

    /**
     * 获得ONU的基本信息 （uniqueId）
     * 
     * @param entityId
     * @param uniqueId
     * @return
     */
    OltOnuAttribute getOnuAttributeByUniqueId(Long entityId, String uniqueId);

    /**
     * 添加onu到拓扑图
     * 
     * @param entity
     */
    void insertOnuToEntity(Entity entity);

    /**
     * 建立product与entity关系
     * 
     * @param productId
     */
    void insertOnuEntityProductRelation(Long productId, Long entityId, Long productType);

    /**
     * 获取指定条件的ONU设备列表
     * 
     * @param map
     * @return
     */
    List<OltOnuAttribute> getOnuDeviceListItem(Map<String, Object> map);

    Integer getOnuTotleNum(Long olt);

    /**
     * 批量更新ONU的预配置类型
     * 
     * @param productTables
     */
    void batchInsertOltOnuPreType(final List<OltTopOnuProductTable> productTables);

    /**
     * 获得ONU设备列表
     * 
     * @param paramMap
     * @return
     */
    List<OltOnuAttribute> getOnuList(Map<String, Object> paramMap);

    /**
     * 获得ONU设备列表数目
     * 
     * @param paramMap
     * @return
     */
    int getOnuListCount(Map<String, Object> paramMap);

    /**
     * 获得ONU版本列表
     * 
     * @param entityId
     * @return
     */
    List<String> getOnuHwList(Long entityId);

    /**
     * 查找拓扑图下的onu列表
     * 
     * @param type
     *            Long
     * @param folderId
     *            Long
     * @return
     */
    List<Long> getOnuIdByFolder(Long type, Long folderId);

    /**
     * Add by Rod
     * 
     * 更新ONU设备的在线时长
     * 
     * @param oltAttribute
     * @param collectTime
     */
    void addOnuDeviceUpTime(Long entityId, Map<String, String> rMap, Long collectTime);

    /**
     * Add by Rod
     * 
     * 删除Onu设备的在线时长历史数据
     * 
     * @param entityId
     * @param collectTime
     */
    void cleanOnuDeviceUpTime(Long entityId, Map<String, String> rMap, Long collectTime);

    /**
     * Add by Rod
     * 
     * 获得当前OLT设备的所有ONU的Index 与 MacAddress
     * 
     * @param entityId
     */
    Map<Long, String> getAllOnuMacAndIndex(Long entityId);

    /**
     * Add by Rod
     * 
     * 获得当前OLT设备的所有ONU的ONUID
     * 
     * @param entityId
     * @return
     */
    List<Long> getOnuIdList(Long entityId);

    /**
     * Added by huangdongsheng
     * 
     * 更新entity中的ONU的Name，供google地图使用
     * 
     * @param onuId
     * @param name
     */
    void updateOnuEntityName(Long onuId, String name);

    /**
     * @param onuId
     * @param adminStatus
     */
    void updateOnuAdminStatus(Long onuId, Integer adminStatus);

    /**
     * 获取ONU预配置类型
     * 
     * @param ponId
     * @return
     */
    List<OltOnuAttribute> getOnuPreTypeByPonId(Long ponId);

    void updateOltOnuAttribute(OltOnuAttribute oltOnuAttribute);

    List<OltOnuAttribute> getOnuListByEntity(Long entityId);

    void updateCC8800ARestartTime(Map<String, Long> attrs);

    /**
     * 批量更新ONU PON端口15min性能统计使能状态
     * 
     * @param oltTopOnuCapability
     */
    void batchUpdateOnu15MinStatus(List<OltTopOnuCapability> onus);

    /**
     * 修改ONU的UNI全局MAC地址老化时间
     * 
     * @param onuId
     * @param macAge
     */
    void modifyOnuMacAgeTime(Long onuId, Integer macAge);

    /**
     * 从性能采集数据中获取OLt或者ONU PON口光信息
     * 
     * @param entityId
     * @param ponIndex
     * @return
     */
    OltOnuOpticalInfo selectOltOunOpticalInfo(Long entityId, Long ponIndex);

    /**
     * 从性能采集数据中获取ccmts光信息
     * 
     * @param cmcId
     * @param ponIndex
     * @return
     */
    OltOnuOpticalInfo selectOltCcmtsOpticalInfo(Long cmcId, Long ponIndex);

    /**
     * 获得A型设备在线状态
     * 
     * @param entityId
     * @return
     */
    Integer selectOltCcmtsStatus(Long entityId);

    /**
     * 更新ONU基本信息
     * 
     * @param onuBaseInfo
     */
    void updateOnuBaseInfo(OnuBaseInfo onuBaseInfo);

    /**
     * 更新CC8800A基本信息
     * 
     * @param cc8800AInfo
     */
    void updateCC8800AInfo(CC8800ABaseInfo cc8800AInfo);

    /**
     * 根据onuId获取onuPonId
     * 
     * @param onuId
     */
    Long getOnuPonId(Long onuId);

    /**
     * 根据onuId获取onuPonIndex
     * 
     * @param onuId
     * @return
     */
    Long getOnuPonIndex(Long onuId);

    /**
     * 更新ONU光功率信息
     * 
     * @param onuOptical
     */
    void updateOnuOpticalInfo(OnuPonOptical onuOptical);

    /**
     * 更新类A型设备的光功率信息
     * 
     * @param onuOptical
     */
    void updateCmcOpticalInfo(OnuPonOptical onuOptical);

    /**
     * 根据OnuId查询对应的onuIndex和entityId
     * 
     * @param onuId
     * @return
     */
    OnuInfo queryOltOnuRelation(Long onuId);

    /**
     * 根据entityId查询下连的onuId 只包含onuId,不包括类A型设备ID
     * 
     * @param entityId
     * @return
     */
    List<Long> queryOnuIdByEntityId(Long entityId);

    /**
     * 更新ONU的状态
     * 
     * @param onuId
     */
    void updateOnuOperationStatus(Long onuId, Integer status);

    /**
     * 批量更新onu能力
     * 
     * @param oltOnuCatvs
     * @param oltOnuRstps
     * @param oltOnuVoips
     */
    void batchUpdateOltOnuCapatilityExt(List<OltOnuCatv> oltOnuCatvs, List<OltOnuRstp> oltOnuRstps,
            List<OltOnuVoip> oltOnuVoips);

    /**
     * 获取ONU结构关系
     * 
     * @param onuId
     *            ONU ID
     * @return Onu
     */
    Onu getStandardOnuInfo(Long onuId);

    /**
     * 根据MAC和parentId获取onuId和onuIndex信息
     * 
     * @param mac
     * @param parentId
     * @return
     */
    Onu getOnuByMacAndParentId(String mac, Long parentId);

    /**
     * 根据ONU
     * 
     * @param entityId
     * @param onuIndex
     * @return
     */
    Long getPonIdByOnuIndex(Long entityId, Long onuIndex);

    /**
     * @param entityId
     * @param loid
     * @return
     */
    OltOnuAttribute getOnuAttributeByLoid(long entityId, String loid);

    /**
     * @param entityId
     * @param macaddress
     * @return
     */
    OltOnuAttribute getOnuAttributeByMac(long entityId, String macaddress);

    void batchUpdateOnuSoftVersion(Long entityId, List<OltOnuAttribute> onuAttrList);

    void batchUpdateOnuHardVersion(Long entityId, List<OltTopOnuCapability> onuCapabilityList);

    /**
     * 获得所有ONU用来自动清除
     * 
     * @return
     */
    List<OltOnuAttribute> getAllOnuForAutoClean();

    /**
     * 更新ONU最后一次下线时间
     * 
     * @param deregisterTables
     */
    void updateOnuDeregisterInfo(List<OnuDeregisterTable> deregisterTables);

    Integer getOnuCountByOnuId(Long onuId);

    /**
     * 获得所有CMC用来自动清除
     * 
     * @return
     */
    List<OltOnuAttribute> getAllCmcForAutoClean();

    /**
     * 更新lastDeregisterTime
     * 
     * @param cmcAttributes
     */
    void updateOnuLastDeregisterTime(List<OltOnuAttribute> oltOnuAttributes);

    /**
     * @param rntityId
     * @param onuIndex
     * @return
     */
    com.topvision.nbi.tl1.api.domain.OnuInfo getOnuTl1InfoByIndex(Long entityId, Long onuIndex);

    /**
     * 查询epon ONU数量
     * 
     * @param queryMap
     * @return
     */
    Integer selectEponOnuCount(Map<String, Object> queryMap);

    /**
     * 查询gpon ONU数量
     * 
     * @param queryMap
     * @return
     */
    Integer selectGponOnuCount(Map<String, Object> queryMap);

    /**
     * 修改onu去激活
     * 
     * @param onuId
     * @param onuDeactive
     */
    void updateOnuDeactive(Long onuId, Integer onuDeactive);

}
