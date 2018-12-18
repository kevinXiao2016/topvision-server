/***********************************************************************
 * $Id: GponSrvProfileDao.java,v1.0 2016年12月20日 下午2:15:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanAggregation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTranslation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTrunk;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvPotsInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvProfile;
import com.topvision.framework.dao.Dao;

/**
 * @author haojie
 * @created @2016年12月20日-下午2:15:43
 *
 */
public interface GponSrvProfileDao extends Dao {

    /**
     * 获取业务模板列表
     * 
     * @param entityId
     * @return
     */
    List<GponSrvProfileInfo> selectGponSrvProfileInfoList(Long entityId);

    /**
     * 获取以太口配置列表信息
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    List<GponSrvProfileEthPortConfig> selectGponSrvProfileEthPortConfigList(Long entityId, Integer profileIndex);

    /**
     * 获取端口VLAN配置列表信息
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    List<GponSrvProfilePortVlanCfg> selectGponSrvProfilePortVlanCfgList(Long entityId, Integer profileIndex);

    /**
     * 获取单个业务模板信息
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    GponSrvProfileInfo selectGponSrvProfileInfo(Long entityId, Integer profileId);

    /**
     * 获取单个以太口配置信息
     * 
     * @param entityId
     * @param profileIndex
     * @param ethPortIdIndex
     * @return
     */
    GponSrvProfileEthPortConfig selectGponSrvProfileEthPortConfig(Long entityId, Integer profileIndex,
            Integer ethPortIdIndex);

    /**
     * 获取单个端口VLAN配置信息（不包含规则）
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @return
     */
    GponSrvProfilePortVlanCfg selectGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex);

    /**
     * 获取VLAN转换列表
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @return
     */
    List<GponSrvProfilePortVlanTranslation> selectGponSrvProfilePortVlanTranslation(Long entityId, Integer profileIndex,
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
    List<GponSrvProfilePortVlanAggregation> selectGponSrvProfilePortVlanAggregation(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex);

    /**
     * 获取VLAN TRUNK规则
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     * @return
     */
    List<GponSrvProfilePortVlanTrunk> selectGponSrvProfilePortVlanTrunk(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex);

    /**
     * 新增业务模板
     * 
     * @param gponSrvProfileInfo
     */
    void insertGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo);

    /**
     * 添以太口配置信息
     * 
     * @param gponSrvProfileEthPortConfig
     */
    void insertGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig gponSrvProfileEthPortConfig);

    /**
     * 添加端口VLAN配置信息
     * 
     * @param gponSrvProfilePortVlanCfg
     */
    void insertGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg);

    /**
     * 添加VLAN转换规则
     * 
     * @param gponSrvProfilePortVlanTranslation
     */
    void insertGponSrvProfilePortVlanTranslation(GponSrvProfilePortVlanTranslation gponSrvProfilePortVlanTranslation);

    /**
     * 添加VLAN聚合规则
     * 
     * @param gponSrvProfilePortVlanAggregation
     */
    void insertGponSrvProfilePortVlanAggregation(GponSrvProfilePortVlanAggregation gponSrvProfilePortVlanAggregation);

    /**
     * 添加VLAN TRUNK规则
     * 
     * @param gponSrvProfilePortVlanTrunk
     */
    void insertGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk gponSrvProfilePortVlanTrunk);

    /**
     * 修改业务模板信息
     * 
     * @param gponSrvProfileInfo
     */
    void updateGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo);

    /**
     * 修改业务模板配置信息
     * 
     * @param gponSrvProfileCfg
     */
    void updateGponSrvProfileCfg(GponSrvProfileCfg gponSrvProfileCfg);

    /**
     * 修改业务模板端端口数量配置信息
     * 
     * @param gponSrvProfilePortNumProfile
     */
    void updateGponSrvProfilePortNumProfile(GponSrvProfilePortNumProfile gponSrvProfilePortNumProfile);

    /**
     * 修改以太口配置信息
     * 
     * @param gponSrvProfileEthPortConfig
     */
    void updateGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig gponSrvProfileEthPortConfig);

    /**
     * 修改端口VLAN配置信息
     * 
     * @param gponSrvProfilePortVlanCfg
     */
    void updateGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg);

    /**
     * 删除业务模版
     * 
     * @param entityId
     * @param profileId
     */
    void deleteGponSrvProfileInfo(Long entityId, Integer profileId);

    /**
     * 刪除VLAN转换规则
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
     * 刪除VLAN聚合规则
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
     * 刪除VLAN TRUNK规则
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIndex
     */
    void deleteGponSrvProfilePortVlanTrunk(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex);

    /**
     * 修改端口VLAN MODE
     * 
     * @param gponSrvProfilePortVlanCfg
     */
    void updateGponSrvProfilePortVlanMode(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg);

    /**
     * 删除端口VLAN规则
     * 
     * @param entityId
     * @param profileIndex
     * @param portTypeIndex
     * @param portIdIndex
     */
    void deletePortVlanRule(Long entityId, Integer profileIndex, Integer portTypeIndex, Integer portIdIndex);

    /**
     * 修改转换规则
     * 
     * @param translation
     */
    void updateGponSrvProfilePortVlanTranslation(GponSrvProfilePortVlanTranslation translation);

    /**
     * 修改聚合规则
     * 
     * @param aggregation
     */
    void updateGponSrvProfilePortVlanAggregation(GponSrvProfilePortVlanAggregation aggregation);

    /**
     * 修改TRUNK规则
     * 
     * @param trunk
     */
    void updateGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk trunk);

    /**
     * 修改业务模板VOIP信息
     * 
     * @param topGponSrvProfile
     */
    void updateTopGponSrvProfile(TopGponSrvProfile topGponSrvProfile);

    /**
     * 获取某一业务模板下POTS口配置列表
     * @param entityId
     * @param profileIndex
     * @return
     */
    List<TopGponSrvPotsInfo> selectTopGponSrvPotsInfoList(Long entityId, Integer profileIndex);

    /**
     * 获取单条POTS口配置
     * @param entityId
     * @param profileIndex
     * @param potsIndex
     * @return
     */
    TopGponSrvPotsInfo selectTopGponSrvPotsInfo(Long entityId, Integer profileIndex, Integer potsIndex);

    /**
     * 修改POTS口配置
     * @param topGponSrvPotsInfo
     */
    void updateTopGponSrvPotsInfo(TopGponSrvPotsInfo topGponSrvPotsInfo);
}
