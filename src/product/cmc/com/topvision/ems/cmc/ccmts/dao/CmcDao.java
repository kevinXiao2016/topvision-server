package com.topvision.ems.cmc.ccmts.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcBfsxSnapInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmciAttribute;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.framework.dao.BaseEntityDao;

public interface CmcDao extends BaseEntityDao<CmcAttribute> {

    /**
     * 获取cmc所在olt entityId
     * 
     * @param cmcId
     * @return cmc所在olt entityId
     */
    Long getEntityIdByCmcId(Long cmcId);

    /**
     * 获取cmc属性
     * 
     * @param cmcId
     *            cmc设备id
     * @return CmcAttribute cmc属性
     */
    CmcAttribute getCmcAttributeByCmcId(Long cmcId);

    /**
     * 通过entityId获得cmcId
     * 
     * @param entityId
     * @return
     */
    Long getCmcIdByEntityId(Long entityId);

    /**
     * 获取cmctype
     * 
     * @param cmcId
     * @return
     */
    Integer getCmcTypeByCmcId(Long cmcId);

    /**
     * 获取cmcIndex
     * 
     * @param cmcId
     * @return
     */
    Long getCmcIndexByCmcId(Long cmcId);

    /**
     * 获取cmc设备列表
     * 
     * @param map
     * @return
     */
    List<CmcAttribute> getDeviceListItem(Map<String, Object> map);

    /**
     * 根据onuId获取cmcId
     * 
     * @param onuId
     * @return
     */
    Long getCmcIdByOnuId(Long onuId);

    /**
     * 获取未关联的CC
     * 
     * @param
     * @return
     */
    Map<Long, String> getCmcNotRelated();

    /**
     * 根据ONUID查询该ONU下的所有CMC设备
     * 
     * @param onuId
     * @return
     */
    List<Cmc> getCmcList(Long onuId);

    /**
     * 将CMC关联到某个ONU
     * 
     * @param onuId
     * @param cmcId
     */
    void relateCmcToOnu(Long onuId, Long cmcId);

    /**
     * 获取onu id
     * 
     * @param cmcId
     * @return
     */
    Long getOnuIdByCmcId(Long cmcId);

    /**
     * 判断cmc是否存在于topo图
     * 
     * @param cmcId
     * @param folderId
     * @return
     */
    boolean isCmcExistsInTopo(Long cmcId, Long folderId);

    /**
     * 获取地域下的entityId
     * 
     * @param folderId
     * @param type
     * @return
     */
    List<Long> getCmcIdByFolder(Long folderId, Long type);

    /**
     * 采集时插入拆分型CC的基本系统信息
     * 
     * @param cmcAttribute
     */
    void batchInsertCcSplitSystemInfo(CmcAttribute cmcAttribute);

    /**
     * 获取cc与entity的关系表
     * 
     * @param cmcId
     * @return
     */
    CmcEntityRelation getCmcEntityRelationByCmcId(Long cmcId);

    /**
     * 获取当前olt设备上所有cmcId
     * 
     * @param entityId
     * @return
     */
    List<Long> getCmcIdsByEntityId(Long entityId);

    /**
     * 更改8800a设备名后对应修改onu表中onuName，保证一致性
     * 
     * @param onuId
     * @param onuName
     */
    void modifyOnuName(Long onuId, String onuName);

    /**
     * 通过olt entityId获取cmcId列表
     * 
     * @param entityId
     * @return
     */
    List<Long> getCmcIdListByEntityId(Long entityId);

    /**
     * 通过mac地址获取cmc别名
     * 
     * @param cmcMacString
     */
    String getCmcNameByMac(String cmcMacString);

    /**
     * 根据entityId及信道ifIndx获取信道所属cc Mac
     * 
     * @param entityId
     * @return
     */
    Long getCmcMacByEntityIdAndChannelIndex(Long entityId, Long ChannelIndex);

    /**
     * 判断端口是否属于cc
     * 
     * @param ifIndex
     * @return
     */
    boolean isCmcChannel(Long ifIndex);

    /**
     * 设置cc基本信息
     * 
     * @param cmcId
     * @param cmcSystemBasicInfo
     */
    void updateCcmtsBasicInfo(CmcSystemBasicInfo cmcSystemBasicInfo);

    /**
     * 获取B型设备FPGA对功能的支持信息
     * 
     * @param cmcId
     * @return
     */
    CmcFpgaSpecification getFpgaSpecificationById(Long cmcId);

    /**
     * 更新CMC设备的状态改变时间
     * 
     * @date 2013-12-19 Added by huangdongsheng
     * @param cmcId
     * @param time
     */
    void updateStatusChangeTime(Long cmcId, Timestamp time);

    /**
     * get OnuIndex
     * 
     * @param cmcId
     */
    Long getOnuIndexByCmcId(Long cmcId);

    /**
     * Add by Rod
     * 
     * @param ipAddress
     * @param address
     * @return
     */
    Entity getCmcByTrapInfo(String ipAddress, String address);

    /**
     * 获取CMC设备统计信息
     * 
     * @return
     */
    SubDeviceCount querySubCountInfo(Long entityId);

    /**
     * 更新CMC刷新的基本信息
     * 
     * @param cmcSnapInfo
     */
    void updateCmcBaseInfo(CmcBfsxSnapInfo cmcSnapInfo);

    /**
     * 
     * @param cmcId
     * @return
     */
    List<CmcUpChannelBaseInfo> getCmcUpChannelBaseInfosForDiscovery(Long cmcId);

    /**
     * 
     * @param cmcId
     * @return
     */
    List<CmcDownChannelBaseInfo> getCmcDownChannelBaseInfosForDiscovery(Long cmcId);

    /**
     * 批量插入cc的网络基本信息
     * 
     * @param cmcSystemIpInfo
     * @param entityId
     */
    void batchInsertCmcSystemIpInfo(CmcSystemIpInfo cmcSystemIpInfo, Long entityId);

    /**
     * CC添加到拓扑图时更新cc的mac信息
     * 
     * @param cmcId
     * @param topCcmtsSysMacAddr
     */
    void updateCmcMac(Long cmcId, String topCcmtsSysMacAddr);

    /**
     * 通过entityId和cmcIndex获取cmcId
     * 
     * @param entityId
     * @param cmcIndex
     * @return
     */
    Long getCmcIdByEntityIdAndCmcIndex(Long entityId, Long cmcIndex);

    /**
     * 根据entityId获取所有的cmcentityrelation
     * 
     * @param entityId
     * @return
     */
    List<CmcEntityRelation> getCmcEntityRelationByEntityId(Long entityId);

    void updateCmcStatus(Long cmcId, Integer status);

    /**
     * 获得所有ONU用来自动清除
     * 
     * @return
     */
    List<CmcAttribute> getAllCmcForAutoClean();

    /**
     * 获取离线cmci
     * 
     * @return
     */
    List<CmciAttribute> getCmciForAutoClean();

    /**
     * 获取上次时间
     * 
     * @param
     * @return int
     */
    Integer getCmcClearTime(Long cmcId);

    /**
     * 更新时间
     * 
     * @param
     * @return void
     */
    void updateCmcClearTime(Integer time, Long cmcId);

    List<CmcAttribute> getContainOptDorCmcList();

}
