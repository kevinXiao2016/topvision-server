/***********************************************************************
 * $Id: GponSrvProfileService.java,v1.0 2016年12月17日 上午8:58:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanAggregation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTranslation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTrunk;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvPotsInfo;

/**
 * @author haojie
 * @created @2016年12月17日-上午8:58:38
 *
 */
public interface GponSrvProfileService {

    /**
     * 获取OLT业务模板列表信息
     * 
     * @param entityId
     * @return
     */
    List<GponSrvProfileInfo> loadGponSrvProfileInfoList(Long entityId);

    /**
     * 获取单条业务模板信息
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    GponSrvProfileInfo loadGponSrvProfileInfo(Long entityId, Integer profileId);

    /**
     * 添加业务模板
     * 
     * @param gponSrvProfileInfo
     */
    void addGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo);

    /**
     * 修改业务模板
     * 
     * @param gponSrvProfileInfo
     */
    void modifyGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo);

    /**
     * 删除业务模板
     * 
     * @param entityId
     * @param profileId
     */
    void deleteGponSrvProfileInfo(Long entityId, Integer profileId);

    /**
     * 刷新业务模板列表
     * 
     * @param entityId
     */
    void refreshGponSrvProfileInfoList(Long entityId);

    /**
     * 获取某一业务模板以太口配置列表
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    List<GponSrvProfileEthPortConfig> loadGponSrvProfileEthPortConfigList(Long entityId, Integer profileIndex);

    /**
     * 获取单条以太口配置信息
     * 
     * @param entityId
     * @param profileIndex
     * @param ethPortIdIndex
     * @return
     */
    GponSrvProfileEthPortConfig loadGponSrvProfileEthPortConfig(Long entityId, Integer profileIndex,
            Integer ethPortIdIndex);

    /**
     * 修改以太口配置
     * 
     * @param gponSrvProfileEthPortConfig
     */
    void modifyGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig gponSrvProfileEthPortConfig);

    /**
     * 刷新某个业务模板下以太口列表数据
     * 
     * @param entityId
     * @param profileIndex
     */
    void refreshGponSrvProfileEthPortConfig(Long entityId, Integer profileIndex);

    /**
     * 获取某一业务模板端口VLAN配置列表
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    List<GponSrvProfilePortVlanCfg> loadGponSrvProfilePortVlanCfgList(Long entityId, Integer profileIndex);

    /**
     * 获取单个端口VLAN配置信息
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @return
     */
    GponSrvProfilePortVlanCfg loadGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex);

    /**
     * 刷新某一业务模板下端口VLAN配置列表
     * 
     * @param entityId
     * @param profileIndex
     */
    void refreshGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex);

    /**
     * 修改端口VLAN基本配置
     * 
     * @param gponSrvProfilePortVlanCfg
     */
    void modifyGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg);

    /**
     * 修改端口VLAN基本配置(修改模式)
     * 
     * @param gponSrvProfilePortVlanCfg
     */
    void modifyGponSrvProfilePortVlanMode(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg);

    /**
     * 获取VLAN转换列表
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @return
     */
    List<GponSrvProfilePortVlanTranslation> loadGponSrvProfilePortVlanTranslation(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex);

    /**
     * 获取VLAN聚合列表
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @return
     */
    List<GponSrvProfilePortVlanAggregation> loadGponSrvProfilePortVlanAggregation(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex);

    /**
     * 获取VLAN TRUNK列表
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @return
     */
    List<GponSrvProfilePortVlanTrunk> loadGponSrvProfilePortVlanTrunk(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex);

    /**
     * 添加VLAN转换规则
     * 
     * @param gponSrvProfilePortVlanTranslation
     */
    void addGponSrvProfilePortVlanTranslation(GponSrvProfilePortVlanTranslation gponSrvProfilePortVlanTranslation);

    /**
     * 添加VLAN聚合规则
     * 
     * @param gponSrvProfilePortVlanAggregation
     */
    void addGponSrvProfilePortVlanAggregation(GponSrvProfilePortVlanAggregation gponSrvProfilePortVlanAggregation);

    /**
     * 添加VLAN TRUNK规则
     * 
     * @param gponSrvProfilePortVlanTrunk
     */
    void addGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk gponSrvProfilePortVlanTrunk);

    /**
     * 修改VLAN转换规则
     * 
     * @param gponSrvProfilePortVlanTranslation
     */
    void modifyGponSrvProfilePortVlanTranslation(GponSrvProfilePortVlanTranslation gponSrvProfilePortVlanTranslation);

    /**
     * 修改VLAN聚合规则
     * 
     * @param gponSrvProfilePortVlanAggregation
     */
    void modifyGponSrvProfilePortVlanAggregation(GponSrvProfilePortVlanAggregation gponSrvProfilePortVlanAggregation);

    /**
     * 修改VLAN TRUNK规则
     * 
     * @param gponSrvProfilePortVlanTrunk
     */
    void modifyGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk gponSrvProfilePortVlanTrunk);

    /**
     * 删除VLAN转换规则
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @param vlanIndex
     */
    void deleteGponSrvProfilePortVlanTranslation(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex, Integer vlanIndex);

    /**
     * 删除VLAN聚合规则
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @param vlanIndex
     */
    void deleteGponSrvProfilePortVlanAggregation(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex, Integer vlanIndex);

    /**
     * 删除VLAN TRUNK规则
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     */
    void deleteGponSrvProfilePortVlanTrunk(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex);

    /**
     * 刷新单个端口VLAN配置和规则
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     */
    void refreshGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex);

    /**
     * 刷新一个业务模板下的POTS口配置列表
     * @param entityId
     * @param profileIndex
     */
    void refreshTopGponSrvPotsInfo(Long entityId, Integer profileIndex);

    /**
     * 获取某一业务模板POTS口配置列表
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    List<TopGponSrvPotsInfo> loadTopGponSrvPotsInfoList(Long entityId, Integer profileIndex);

    /**
     * 获取单条POTS口配置
     * 
     * @param entityId
     * @param profileIndex
     * @param potsIndex
     * @return
     */
    TopGponSrvPotsInfo loadTopGponSrvPotsInfo(Long entityId, Integer profileIndex, Integer potsIndex);

    /**
     * 修改POTS口配置
     * 
     * @param topGponSrvPotsInfo
     */
    void modifyTopGponSrvPotsInfo(TopGponSrvPotsInfo topGponSrvPotsInfo);
}
