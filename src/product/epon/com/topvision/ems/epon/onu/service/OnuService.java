/***********************************************************************
 * $Id: OnuService.java,v1.0 2013-10-25 上午11:14:13 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.OltOnuTypeInfo;
import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.onu.domain.CC8800ABaseInfo;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OnuBaseInfo;
import com.topvision.ems.epon.onu.domain.OnuEnviEnum;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.service.Service;
import com.topvision.nbi.tl1.api.domain.OnuInfo;

/**
 * @author flack
 * @created @2013-10-25-上午11:14:13
 *
 */
public interface OnuService extends Service {

    /**
     * 获取ONU的type列表
     * 
     * @return
     * @throws SQLException
     */
    List<EntityType> loadOnuTypes() throws SQLException;

    /**
     * 获取ONU结构数据
     * 
     * @param onuId
     *            ONUID
     * @return ONU
     */
    Onu getOnuStructure(Long onuId);

    /**
     * 获取ONU列表
     * 
     * @param slotId
     *            槽位ID
     * @param ponId
     *            ponID
     * @param llid
     *            LLID
     * @param macAddress
     *            ONU的MAC地址
     * @param onuName
     *            ONU名称
     * @return List<OltOnuAttribute>
     */
    List<OltOnuAttribute> getOnuList(Long entityId, Long slotId, Long ponId, Integer llid, String macAddress,
            String onuName);

    /**
     * 获取ONU属性
     * 
     * @param onuId
     *            onuID
     * @return OltOnuAttribute
     */
    OltOnuAttribute getOnuAttribute(Long onuId);

    /**
     * 更新ONU属性
     * 
     * @param onuAttribute
     *            ONU属性
     */
    void updateOnuAttribute(OltOnuAttribute onuAttribute);

    /**
     * 获取ONU光传输属性
     * 
     * @param onuPonId
     *            onu上的pon口ID
     * @return OltOnuPonAttribute
     */
    OltOnuPonAttribute getOnuPonAttribute(Long onuPonId);

    /**
     * 获取UNI口属性
     * 
     * @param uniId
     *            uni口ID
     * @return OltUniAttribute
     */
    OltUniAttribute getOnuUniAttribute(Long uniId);

    /**
     * 更新UNI口属性
     * 
     * @param uniAttribute
     *            uni口对象对象
     */
    void updateOnuUniAttribute(OltUniAttribute uniAttribute);

    /**
     * ONU复位
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            onu的ID
     */
    void resetOnu(Long entityId, Long onuId);

    /**
     * 
     * ONU解注册
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            onu的ID
     */
    void deregisterOnu(Long entityId, Long onuId);

    /**
     * 获取ONU的列表及使能状态 return onuNo、onuId、15minStat、24hourStat、temperatureStat
     * 
     * @param portId
     * @return
     */
    List<Map<String, Long>> getOnuStatList(Long portId);

    /**
     * 设置ONU使能
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuAdminStatus
     *            ONU使能状态
     */
    void setOnuAdminStatus(Long entityId, Long onuId, Integer onuAdminStatus);

    /**
     * 设置ONU别名
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuName
     *            ONU别名
     */
    String modifyOnuName(Long entityId, Long onuId, String onuName);

    /**
     * 设置ONU的VOIP服务功能
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuVoipEnable
     *            ONU的VOIP服务功能状态
     */
    void setOnuVoipEnable(Long entityId, Long onuId, Integer onuVoipEnable);

    /**
     * 设置ONU温度检测使能
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuTemperatureDetectEnable
     *            ONU温度检测使能状态
     */
    void setOnuTemperatureDetectEnable(Long entityId, Long onuId, Integer onuTemperatureDetectEnable);

    /**
     * 设置ONU的FEC使能
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuFecEnable
     *            ONU的FEC使能状态
     */
    void setOnuFecEnable(Long entityId, Long onuId, Integer onuFecEnable);

    /**
     * 设置ONU的端口隔离使能
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuIsolationEnable
     *            ONU的端口隔离使能状态
     */
    void configOnuIsolationEnable(Long entityId, Long onuId, Integer onuIsolationEnable);

    /**
     * 设置ONU的15min性能统计使能
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onu15minEnable
     *            ONU的15min性能统计使能状态
     */
    void setOnu15minEnable(Long entityId, Long onuId, Integer onu15minEnable);

    /**
     * 批量设置ONU PON的15min性能统计使能
     * 
     * @param entityId
     */
    void updateEntityOnu15minEnable(List<OltOnuPonAttribute> onuPonAttributes);

    /**
     * 设置ONU的24h性能统计使能
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onu24hEnable
     *            ONU的24h性能统计使能状态
     */
    void setOnu24hEnable(Long entityId, Long onuId, Integer onu24hEnable);

    /**
     * 设置ONU的CATV服务使能
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuCatvEnable
     *            ONU的CATV使能状态
     */
    void setOnuCatvEnable(Long entityId, Long onuId, Integer onuCatvEnable);

    /**
     * 设置ONU的MAC地址最大学习数目
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuMacMaxNum
     *            ONU的MAC地址最大学习数目
     */
    void setOnuMacMaxNum(Long entityId, Long onuId, Integer onuMacMaxNum);

    /**
     * 设置ONU的RSTP桥模式
     * 
     * @param entityId
     *            设备ID
     * @param onuId
     *            ONU ID
     * @param onuRstpBridgeMode
     *            ONU的RSTP桥模式
     */
    void setOnuRstpBridgeMode(Long entityId, Long onuId, Integer onuRstpBridgeMode);

    /**
     * 获取ONU PON端口属性
     * 
     * @param onuId
     * @return OltOnuPonAttribute
     */
    OltOnuPonAttribute getOnuPonAttributeByOnuId(Long onuId);

    /**
     * 获取ONU 能力信息
     * 
     * @param onuId
     * @return OltOnuCapability
     */
    OltOnuCapability getOltOnuCapabilityByOnuId(Long onuId);

    /**
     * 获取ONU MAC 地址学习数
     * 
     * @param onuId
     * @return OltTopOnuCapability 模式
     */
    OltTopOnuCapability getOltTopOnuCapabilityByOnuId(Long onuId);

    /**
     * 获取ONU RSTP模式
     * 
     * @param onuId
     * @return OltOnuRstp RSTP模式
     */
    OltOnuRstp getOltOnuRstpByOnuId(Long onuId);

    /**
     * 获取查询ONU列表总行数
     * 
     * @param paramMap
     * @return
     */
    int getOnuListCount(Map<String, Object> paramMap);

    /**
     * 通过onuIndex 获得onuId
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU INDEX
     */
    Long getOnuIdByIndex(Long entityId, Long onuIndex) throws Exception;

    /**
     * 获得ONU硬件版本号
     * 
     * @param entityId
     * @return
     */
    List<String> getOnuHwList(Long entityId);

    /**
     * 获得ONU列表
     * 
     * @param ponId
     * @return
     */
    List<OltOnuAttribute> getOnuListByPonId(Long ponId);

    /**
     * 刷新ONU的信息
     * 
     * @param refresgId
     */
    // void refreshOnuInfo(Long entityId);

    /**
     * Add by Rod
     * 
     * 获得当前OLT设备的所有ONU的Index 与 MacAddress
     * 
     * @param entityId
     */
    Map<String, String> getAllOnuMacAndIndex(Long entityId);

    /**
     * 获得CMC的设备类型
     * 
     * @Add by Rod
     * 
     * @param onuPreType
     * @return
     */
    EntityType getCmcEntityType(Integer onuPreType);

    /**
     * 获得CMC的设备类型
     * 
     * @Add by Rod For C-A AND C-B
     * 
     * @param cmcIndex
     * @param entityId
     * @return
     */
    EntityType getCmcEntityType(Long cmcIndex, Long entityId);

    /**
     * 获得CMC的设备类型
     * 
     * @Add by Rod For Distribute E
     * 
     * @param cmcIndex
     * @param entityId
     * @param standard
     * @return
     */
    EntityType getCmcEntityType(Long cmcIndex, Long entityId, Integer standard);

    /**
     * 刷新ONU温度
     * 
     * @param entityId
     * @param onuId
     */
    Integer refreshOnuTemperature(Long entityId, Long onuId);

    /**
     * 获得ONU类型基本信息
     * 
     * @param onuTypeId
     */
    OltOnuTypeInfo getOnuTypeInfo(Integer onuTypeId);

    /**
     * 添加onu到拓扑图
     * 
     * @param entityId
     *            ， entity
     */
    void moveToTopoFromOnuView(Long onuId, Entity entity);

    /**
     * 获取给定条件的ONU设备资源.
     * 
     * @param buildConditions
     * @return
     */
    List<OltOnuAttribute> getOnuDeviceListItem(Map<String, Object> map);

    /**
     * 获得ONU设备列表
     * 
     * @param paramMap
     * @return
     */
    List<OltOnuAttribute> getOnuList(Map<String, Object> paramMap);

    /**
     * Add by Rod
     * 
     * 更新ONU设备的在线时长
     * 
     * @param entityId
     * @param rMap
     */
    void addOnuDeviceUpTime(Long entityId, Map<String, String> rMap);

    /**
     * Add by Victor@20131210用于CC重启统计
     *
     * 获取OLT下所有的ONU列表(包括CC)
     * 
     * @param entityId
     * @return ONU(CCMTS)列表
     */
    List<OltOnuAttribute> getOnuListByEntity(Long entityId);

    /**
     * Add by Victor@20131210用于CC重启统计
     * 
     * 更新重启表，如果重新注册时间比重启时间近，则去除掉，标识为offline
     * 
     * @param attrs
     */
    void updateCC8800ARestartTime(Map<String, Long> attrs);

    /**
     * 更新所有ONU PON端口15分钟性能统计状态
     * 
     * @return
     */
    void updateOnuPon15MinStatus(Long entityId);

    /**
     * 修改ONU的UNI全局MAC地址老化时间
     * 
     * @param uniExtAttribute
     * @param onuId
     */
    void modifyOnuMacAgeTime(OltUniExtAttribute uniExtAttribute, Long onuId);

    /**
     * 从设备获取ONU的UNI全局MAC地址老化时间
     * 
     * @param uniExtAttribute
     * @param onuId
     */
    OltUniExtAttribute fetchOnuMacAgeTime(OltUniExtAttribute uniExtAttribute, Long onuId);

    /**
     * 
     * @param baseInfo
     */
    void refreshOnuBaseInfo(OnuBaseInfo baseInfo);

    /**
     * 刷新CC8800A信息
     * 
     * @param caInfo
     * @param onuInfo
     */
    void refreshCC8800ABaseInfo(CC8800ABaseInfo caInfo, OnuBaseInfo onuInfo);

    /**
     * 刷新OLT下所有ONU能力信息
     * 
     * @param entityId
     */
    void refreshOltOnuCapatilityExt(Long entityId);

    /**
     * 刷新OLT下单个ONU能力信息
     * 
     * @param entityId
     * @param onuIndex
     */
    void refreshOltOnuCapatilityExt(Long entityId, Long onuIndex);

    /**
     * 获取标准OLT下ONU结构数据
     * 
     * @param onuId
     *            ONUID
     * @return ONU
     */
    Onu getStandardOnuInfo(Long onuId);

    /**
     * 通过MAC查询ONU
     * 
     * @param entityId
     * @param macaddress
     * @return
     */
    OltOnuAttribute getOnuAttributeByMac(long entityId, String macaddress);

    /**
     * 根据LOID(SN)查询ONU
     * 
     * @param entityId
     * @param loid
     * @return
     */
    OltOnuAttribute getOnuAttributeByLoid(long entityId, String loid);

    /**
     * 获取onu下行速率
     * 
     * @param entityId
     * @param onuId
     * @param onuEorG
     *            'E' 'G'
     * @return
     */
    Integer getOnuDownRate(Long entityId, Long onuId, String onuEorG);

    Integer getOnuCountByOnuId(Long onuId);

    /**
     * 获取单个ONU Rstp
     * 
     * @return
     */
    OltOnuRstp getOltOnuRstp(Long entityId, Long onuId);

    /**
     * 获取ONU tl1所需信息
     * 
     * @param entityId
     * @param onuIndex 
     * @return
     */
    OnuInfo getOnuTl1InfoByIndex(Long entityId, Long onuIndex);

    /**
     * 查询Onu环境
     * 
     * @param queryMap
     * @return
     */
    OnuEnviEnum queryOnuEnvi(Map<String, Object> queryMap);

    /**
     * ONU去激活
     * 
     * @param onuId
     * @param onuDeactive
     */
    void onuDeactive(Long onuId, Integer onuDeactive);
}
