/***********************************************************************
 * $Id: GponProfileRefreshDao.java,v1.0 2016年12月17日 下午2:15:37 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponDbaProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanAggregation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTranslation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTrunk;
import com.topvision.ems.gpon.profile.facade.domain.GponTrafficProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvPotsInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvProfile;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopVoipMediaProfInfo;

/**
 * @author haojie
 * @created @2016年12月17日-下午2:15:37
 *
 */
public interface GponProfileRefreshDao {

    /**
     * 批量插入DBA模板
     * 
     * @param dbaTable
     * @param entityId
     */
    void insertDBAProfiles(List<GponDbaProfileInfo> dbaTable, Long entityId);

    /**
     * 批量插入流量模板
     * 
     * @param trafficProfiles
     * @param entityId
     */
    void insertTrafficProfiles(List<GponTrafficProfileInfo> trafficProfiles, Long entityId);

    /**
     * 批量插入线路模板基本信息
     * 
     * @param lineTable
     * @param entityId
     */
    void insertLineProfiles(List<GponLineProfileInfo> lineTable, Long entityId);

    /**
     * 批量插入T-CONT配置
     * 
     * @param tcontProfileTable
     * @param entityId
     */
    void insertTcontProfiles(List<GponLineProfileTcont> tcontProfileTable, Long entityId);

    /**
     * 批量插入GEM配置
     * 
     * @param gemTable
     * @param entityId
     */
    void insertGemProfiles(List<GponLineProfileGem> gemTable, Long entityId);

    /**
     * 批量插入GEM映射配置
     * 
     * @param gemMapTable
     * @param entityId
     */
    void insertGemMapProfiles(List<GponLineProfileGemMap> gemMapTable, Long entityId);

    /**
     * 批量插入业务模板基本信息
     * 
     * @param srvProfileInfos
     * @param entityId
     */
    void insertSrvProfileInfos(List<GponSrvProfileInfo> srvProfileInfos, Long entityId);

    /**
     * 批量插入业务模板基本配置
     * 
     * @param srvProfileTable
     * @param entityId
     */
    void insertSrvProfileCfgs(List<GponSrvProfileCfg> srvProfileTable, Long entityId);

    /**
     * 批量插入业务模板端口数配置
     * 
     * @param srvPortNumProfiles
     * @param entityId
     */
    void insertSrvPortNumProfiles(List<GponSrvProfilePortNumProfile> srvPortNumProfiles, Long entityId);

    /**
     * 批量插入业务模板以太口配置
     * 
     * @param srvEthPortTable
     * @param entityId
     */
    void insertSrvEthProfiles(List<GponSrvProfileEthPortConfig> srvEthPortTable, Long entityId);

    /**
     * 批量插入端口VLAN配置
     * 
     * @param portVlanProfiles
     * @param entityId
     */
    void insertPortVlanProfiles(List<GponSrvProfilePortVlanCfg> portVlanProfiles, Long entityId);

    /**
     * 批量插入VLAN转换规则
     * 
     * @param vlanTranslationProfiles
     * @param entityId
     */
    void insertVlanTranslationProfiles(List<GponSrvProfilePortVlanTranslation> vlanTranslationProfiles, Long entityId);

    /**
     * 批量插入VLAN聚合规则
     * 
     * @param vlanAggrProfileTable
     * @param entityId
     */
    void insertVlanAggrProfiles(List<GponSrvProfilePortVlanAggregation> vlanAggrProfileTable, Long entityId);

    /**
     * 批量 插入VLAN TRUNK规则
     * 
     * @param vlanTrunkProfiles
     * @param entityId
     */
    void insertVlanTrunkProfiles(List<GponSrvProfilePortVlanTrunk> vlanTrunkProfiles, Long entityId);

    /**
     * 批量插入一个业务模板下T-CONT列表
     * 
     * @param tconts
     * @param entityId
     * @param profileIndex
     */
    void insertTcontProfiles(List<GponLineProfileTcont> tconts, Long entityId, Integer profileIndex);

    /**
     * 批量插入一个业务模板下GEM列表
     * 
     * @param gems
     * @param entityId
     * @param profileIndex
     */
    void insertGemProfiles(List<GponLineProfileGem> gems, Long entityId, Integer profileIndex);

    /**
     * 批量插入一个GEM下的GEM映射
     * @param gemMaps
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     */
    void insertGemMapProfiles(List<GponLineProfileGemMap> gemMaps, Long entityId, Integer profileIndex,
            Integer gemIndex);

    /**
     * 批量插入一个业务模板下的以太口配置列表
     * @param ethPortConfigs
     * @param entityId
     * @param profileIndex
     */
    void insertEthPortConfigs(List<GponSrvProfileEthPortConfig> ethPortConfigs, Long entityId, Integer profileIndex);

    /**
     * 批量插入一个业务模板下端口VLAN配置列表
     * @param portVlanCfgs
     * @param entityId
     * @param profileIndex
     */
    void insertPortVlanCfgs(List<GponSrvProfilePortVlanCfg> portVlanCfgs, Long entityId, Integer profileIndex);

    /**
     * 批量插入业务模板VOIP相关配置信息
     * @param topGponSrvProfile
     * @param entityId
     */
    void insertTopGponSrvProfiles(List<TopGponSrvProfile> topGponSrvProfile, Long entityId);

    /**
     * 批量插入SIP代理模板信息
     * @param topSIPAgentProfInfo
     * @param entityId
     */
    void insertTopSIPAgentProfInfos(List<TopSIPAgentProfInfo> topSIPAgentProfInfo, Long entityId);

    /**
     * 批量插入POTS口模板
     * @param potsTable
     * @param entityId
     */
    void insertTopGponSrvPotsInfos(List<TopGponSrvPotsInfo> potsTable, Long entityId);

    /**
     * 批量插入媒体模板
     * @param mediaTable
     * @param entityId
     */
    void insertTopVoipMediaProfInfos(List<TopVoipMediaProfInfo> mediaTable, Long entityId);

    /**
     * 批量插入SIP业务数据模板
     * @param sipSrvTable
     * @param entityId
     */
    void insertTopSIPSrvProfInfos(List<TopSIPSrvProfInfo> sipSrvTable, Long entityId);

    /**
     * 批量插入数图模板
     * @param digitMapTable
     * @param entityId
     */
    void insertTopDigitMapProfInfos(List<TopDigitMapProfInfo> digitMapTable, Long entityId);

    /**
     * 
     * @param potsList
     * @param entityId
     * @param profileIndex
     */
    void insertTopGponSrvPotsInfos(List<TopGponSrvPotsInfo> potsList, Long entityId, Integer profileIndex);

    /**
     * 添加业务模板配置信息
     * 
     * @param gponSrvProfileCfg
     */
    void insertGponSrvProfileCfg(GponSrvProfileCfg gponSrvProfileCfg);

    /**
     * 添加业务模板端口配置信息
     * 
     * @param gponSrvProfilePortNumProfile
     */
    void insertGponSrvProfilePortNumProfile(GponSrvProfilePortNumProfile gponSrvProfilePortNumProfile);
    
    /**
     * 添加业务模板VOIP信息
     * 
     * @param cfg
     */
    void insertTopGponSrvProfile(TopGponSrvProfile cfg);

}
