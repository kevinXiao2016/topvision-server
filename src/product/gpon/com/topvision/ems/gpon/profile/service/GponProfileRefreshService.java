/***********************************************************************
 * $Id: GponProfileService.java,v1.0 2016年10月25日 下午12:06:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

/**
 * @author Bravin
 * @created @2016年10月25日-下午12:06:47
 *
 */
public interface GponProfileRefreshService {

    /**
     * 拓扑OLT所有GPON模板
     * 
     * @param entityId
     */
    void refreshGponProfile(Long entityId);

    /**
     * 拓扑OLT所有DBA模板
     * 
     * @param entityId
     */
    void refreshGponDbaProfileInfo(Long entityId);

    /**
     * 拓扑OLT所有流量模板
     * 
     * @param entityId
     */
    void refreshGponTrafficProfileInfo(Long entityId);

    /**
     * 拓扑OLT所有线路模板基本信息
     * 
     * @param entityId
     */
    void refreshGponLineProfileInfo(Long entityId);

    /**
     * 拓扑OLT所有线路模板T-CONT配置
     * 
     * @param entityId
     */
    void refreshGponLineProfileTcont(Long entityId);

    /**
     * 拓扑OLT所有GEM配置
     * 
     * @param entityId
     */
    void refreshGponLineProfileGem(Long entityId);

    /**
     * 拓扑OLT所有GEM映射配置
     * 
     * @param entityId
     */
    void refreshGponLineProfileGemMap(Long entityId);

    /**
     * 拓扑OLT所有业务模板基本信息
     * 
     * @param entityId
     */
    void refreshGponSrvProfileInfo(Long entityId);

    /**
     * 拓扑OLT所有业务模板基本配置
     * 
     * @param entityId
     */
    void refreshGponSrvProfileCfg(Long entityId);

    /**
     * 拓扑OLT所有业务模板端口配置信息
     * 
     * @param entityId
     */
    void refreshGponSrvProfilePortNumProfile(Long entityId);

    /**
     * 拓扑OLT所有以太口配置
     * 
     * @param entityId
     */
    void refreshGponSrvProfileEthPortConfig(Long entityId);

    /**
     * 拓扑OLT所有业务模板端口VLAN配置
     * 
     * @param entityId
     */
    void refreshGponSrvProfilePortVlanCfg(Long entityId);

    /**
     * 拓扑OLT所有业务模板VLAN转换规则
     * 
     * @param entityId
     */
    void refreshGponSrvProfilePortVlanTranslation(Long entityId);

    /**
     * 拓扑OLT所有业务模板VLAN聚合规则
     * 
     * @param entityId
     */
    void refreshGponSrvProfilePortVlanAggregation(Long entityId);

    /**
     * 拓扑OLT所有业务模板VLAN TRUNK规则
     * 
     * @param entityId
     */
    void refreshGponSrvProfilePortVlanTrunk(Long entityId);

    /**
     * 刷新某一线路模板下T-CONT列表信息
     * 
     * @param entityId
     * @param profileIndex
     */
    void refreshGponLineProfileTcont(Long entityId, Integer profileIndex);

    /**
     * 刷新某一线路模板下GEM列表信息
     * 
     * @param entityId
     * @param profileIndex
     */
    void refreshGponLineProfileGem(Long entityId, Integer profileIndex);

    /**
     * 刷新某一GEM下GEM映射信息
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     */
    void refreshGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex);

    
    /**
     * 刷新某一业务模板下以太口配置列表
     * 
     * @param entityId
     * @param profileIndex
     */
    void refreshGponSrvProfileEthPortConfig(Long entityId, Integer profileIndex);

    /**
     * 刷新 某一业务模板下端口VLAN列表
     * 
     * @param entityId
     * @param profileIndex
     */
    void refreshGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex);

    /**
     * 刷新一个端口VLAN的配置信息（包括VLAN规则）
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     */
    void refreshGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex);

    /**
     * 刷新一个业务模板的配置信息
     * 
     * @param entityId
     * @param gponSrvProfileId
     */
    void refreshGponSrvProfileCfg(Long entityId, Integer gponSrvProfileId);

    /**
     * 刷新一个业务模板的端口配置信息
     * 
     * @param entityId
     * @param gponSrvProfileId
     */
    void refreshGponSrvProfilePortNumProfile(Long entityId, Integer gponSrvProfileId);

    /**
     * 刷新GPON业务模板VOIP相关配置
     * 
     * @param entityId
     */
    void refreshTopGponSrvProfile(Long entityId);

    /**
     * 刷新GPON SIP代理模板信息
     * 
     * @param entityId
     */
    void refreshTopSIPAgentProfInfo(Long entityId);

    /**
     * 刷新GPON业务模板VOIP相关配置
     * 
     * @param entityId
     */
    void refreshTopGponSrvProfile(Long entityId, Integer gponSrvProfileId);

    /**
     * 刷新数图模板
     * @param entityId
     */
    void refreshTopDigitMapProfInfo(Long entityId);

    /**
     * 刷新SIP业务数据模板
     * @param entityId
     */
    void refreshTopSIPSrvProfInfo(Long entityId);

    /**
     * 刷新VOIP媒体模板
     * @param entityId
     */
    void refreshTopVoipMediaProfInfo(Long entityId);

    /**
     * 刷新POTS口配置模板
     * @param entityId
     */
    void refreshTopGponSrvPotsInfo(Long entityId);

    /**
     * 刷新一个业务模板下POTS口配置列表
     * @param entityId
     * @param profileIndex
     */
    void refreshTopGponSrvPotsInfo(Long entityId, Integer profileIndex);
}
