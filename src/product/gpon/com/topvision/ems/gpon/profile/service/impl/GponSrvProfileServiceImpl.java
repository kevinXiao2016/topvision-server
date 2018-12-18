/***********************************************************************
 * $Id: GponSrvProfileServiceImpl.java,v1.0 2016年12月17日 上午9:05:14 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.GponSrvProfileDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
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
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.gpon.profile.service.GponSrvProfileService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2016年12月17日-上午9:05:14
 *
 */
@Service("gponSrvProfileService")
public class GponSrvProfileServiceImpl implements GponSrvProfileService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponSrvProfileDao gponSrvProfileDao;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;
    private final Logger logger = LoggerFactory.getLogger(GponSrvProfileServiceImpl.class);

    @Override
    public List<GponSrvProfileEthPortConfig> loadGponSrvProfileEthPortConfigList(Long entityId, Integer profileIndex) {
        return gponSrvProfileDao.selectGponSrvProfileEthPortConfigList(entityId, profileIndex);
    }

    @Override
    public List<GponSrvProfileInfo> loadGponSrvProfileInfoList(Long entityId) {
        return gponSrvProfileDao.selectGponSrvProfileInfoList(entityId);
    }

    @Override
    public List<GponSrvProfilePortVlanCfg> loadGponSrvProfilePortVlanCfgList(Long entityId, Integer profileIndex) {
        return gponSrvProfileDao.selectGponSrvProfilePortVlanCfgList(entityId, profileIndex);
    }

    @Override
    public void modifyGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig gponSrvProfileEthPortConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfileEthPortConfig.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfileEthPortConfig);
        gponSrvProfileDao.updateGponSrvProfileEthPortConfig(gponSrvProfileEthPortConfig);
    }

    @Override
    public void modifyGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfileInfo.getEntityId());
        // 更新业务模板基本信息表
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfileInfo);
        gponSrvProfileDao.updateGponSrvProfileInfo(gponSrvProfileInfo);

        // 更新业务模板基本配置表
        GponSrvProfileCfg gponSrvProfileCfg = new GponSrvProfileCfg();
        gponSrvProfileCfg.setEntityId(gponSrvProfileInfo.getEntityId());
        gponSrvProfileCfg.setGponSrvProfileIndex(gponSrvProfileInfo.getGponSrvProfileId());
        gponSrvProfileCfg
                .setGponSrvProfileLoopbackDetectCheck(gponSrvProfileInfo.getGponSrvProfileLoopbackDetectCheck());
        gponSrvProfileCfg.setGponSrvProfileMacAgeSeconds(gponSrvProfileInfo.getGponSrvProfileMacAgeSeconds());
        gponSrvProfileCfg.setGponSrvProfileMacLearning(gponSrvProfileInfo.getGponSrvProfileMacLearning());
        gponSrvProfileCfg.setGponSrvProfileMcFastLeave(gponSrvProfileInfo.getGponSrvProfileMcFastLeave());
        gponSrvProfileCfg.setGponSrvProfileMcMode(gponSrvProfileInfo.getGponSrvProfileMcMode());
        gponSrvProfileCfg.setGponSrvProfileUpIgmpFwdMode(gponSrvProfileInfo.getGponSrvProfileUpIgmpFwdMode());
        gponSrvProfileCfg.setGponSrvProfileUpIgmpTCI(gponSrvProfileInfo.getGponSrvProfileUpIgmpTCI());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfileCfg);
        gponSrvProfileDao.updateGponSrvProfileCfg(gponSrvProfileCfg);

        // 更新业务模板端口配置表
        GponSrvProfilePortNumProfile portNumPro = new GponSrvProfilePortNumProfile();
        portNumPro.setEntityId(gponSrvProfileInfo.getEntityId());
        portNumPro.setGponSrvProfilePortNumProfileIndex(gponSrvProfileInfo.getGponSrvProfileId());
        portNumPro.setGponSrvProfileCatvNum(gponSrvProfileInfo.getGponSrvProfileCatvNum());
        portNumPro.setGponSrvProfileEthNum(gponSrvProfileInfo.getGponSrvProfileEthNum());
        portNumPro.setGponSrvProfileWlanNum(gponSrvProfileInfo.getGponSrvProfileWlanNum());
        portNumPro.setGponSrvProfileVeipNum(gponSrvProfileInfo.getGponSrvProfileVeipNum());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, portNumPro);
        gponSrvProfileDao.updateGponSrvProfilePortNumProfile(portNumPro);

        // 更新VOIP配置表
        TopGponSrvProfile topGponSrvProfile = new TopGponSrvProfile();
        topGponSrvProfile.setEntityId(gponSrvProfileInfo.getEntityId());
        topGponSrvProfile.setTopGponSrvProfileIndex(gponSrvProfileInfo.getGponSrvProfileId());
        topGponSrvProfile.setTopGponSrvProfilePotsNum(gponSrvProfileInfo.getTopGponSrvProfilePotsNum());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topGponSrvProfile);
        gponSrvProfileDao.updateTopGponSrvProfile(topGponSrvProfile);

        // 同步刷新以太口列表/端口VLAN列表/POTS口配置列表，不在界面上表现，捕获异常
        try {
            refreshGponSrvProfileEthPortConfig(gponSrvProfileInfo.getEntityId(),
                    gponSrvProfileInfo.getGponSrvProfileId());
            refreshGponSrvProfilePortVlanCfg(gponSrvProfileInfo.getEntityId(),
                    gponSrvProfileInfo.getGponSrvProfileId());
            refreshTopGponSrvPotsInfo(gponSrvProfileInfo.getEntityId(), gponSrvProfileInfo.getGponSrvProfileId());
        } catch (Exception e) {
            logger.debug("refresh ethport and portvlan error after modify srvprofile!");
        }
    }

    @Override
    public void modifyGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg cfg) {
        GponSrvProfilePortVlanCfg cfgOld = gponSrvProfileDao.selectGponSrvProfilePortVlanCfg(cfg.getEntityId(),
                cfg.getGponSrvProfilePortVlanProfileIndex(), cfg.getGponSrvProfilePortVlanPortTypeIndex(),
                cfg.getGponSrvProfilePortVlanPortIdIndex());
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cfg.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, cfg);
        gponSrvProfileDao.updateGponSrvProfilePortVlanCfg(cfg);

        if (!cfg.getGponSrvProfilePortVlanMode().equals(cfgOld.getGponSrvProfilePortVlanMode())) {
            // 切换VLAN模式，清除VLAN规则
            gponSrvProfileDao.deletePortVlanRule(cfg.getEntityId(), cfg.getGponSrvProfilePortVlanProfileIndex(),
                    cfg.getGponSrvProfilePortVlanPortTypeIndex(), cfg.getGponSrvProfilePortVlanPortIdIndex());
        }
    }

    @Override
    public void deleteGponSrvProfileInfo(Long entityId, Integer profileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponSrvProfileInfo gponSrvProfileInfo = new GponSrvProfileInfo();
        gponSrvProfileInfo.setEntityId(entityId);
        gponSrvProfileInfo.setGponSrvProfileId(profileId);
        gponSrvProfileInfo.setGponSrvProfileRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfileInfo);
        gponSrvProfileDao.deleteGponSrvProfileInfo(entityId, profileId);
    }

    @Override
    public void addGponSrvProfilePortVlanAggregation(
            GponSrvProfilePortVlanAggregation gponSrvProfilePortVlanAggregation) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfilePortVlanAggregation.getEntityId());
        gponSrvProfilePortVlanAggregation.setGponSrvProfilePortVlanAggrRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfilePortVlanAggregation);
        gponSrvProfileDao.insertGponSrvProfilePortVlanAggregation(gponSrvProfilePortVlanAggregation);
    }

    @Override
    public void addGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfileInfo.getEntityId());
        gponSrvProfileInfo.setGponSrvProfileRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfileInfo);
        gponSrvProfileDao.insertGponSrvProfileInfo(gponSrvProfileInfo);

        // 同步刷新基本配置值信息/获取端口配置信息/以太口列表/端口VLAN列表,不在界面上表现，捕获异常
        try {
            gponProfileRefreshService.refreshGponSrvProfileCfg(gponSrvProfileInfo.getEntityId(),
                    gponSrvProfileInfo.getGponSrvProfileId());
            gponProfileRefreshService.refreshGponSrvProfilePortNumProfile(gponSrvProfileInfo.getEntityId(),
                    gponSrvProfileInfo.getGponSrvProfileId());
            gponProfileRefreshService.refreshTopGponSrvProfile(gponSrvProfileInfo.getEntityId(),
                    gponSrvProfileInfo.getGponSrvProfileId());
            refreshGponSrvProfileEthPortConfig(gponSrvProfileInfo.getEntityId(),
                    gponSrvProfileInfo.getGponSrvProfileId());
            refreshGponSrvProfilePortVlanCfg(gponSrvProfileInfo.getEntityId(),
                    gponSrvProfileInfo.getGponSrvProfileId());
            refreshTopGponSrvPotsInfo(gponSrvProfileInfo.getEntityId(), gponSrvProfileInfo.getGponSrvProfileId());
        } catch (Exception e) {
            logger.debug("refresh ethport and portvlan error after add srvprofile!");
        }
    }

    @Override
    public void addGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk gponSrvProfilePortVlanTrunk) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfilePortVlanTrunk.getEntityId());
        gponSrvProfilePortVlanTrunk.setGponSrvProfilePortVlanTrunkRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfilePortVlanTrunk);
        gponSrvProfileDao.insertGponSrvProfilePortVlanTrunk(gponSrvProfilePortVlanTrunk);
    }

    @Override
    public void addGponSrvProfilePortVlanTranslation(
            GponSrvProfilePortVlanTranslation gponSrvProfilePortVlanTranslation) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfilePortVlanTranslation.getEntityId());
        gponSrvProfilePortVlanTranslation.setGponSrvProfilePortVlanTransRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfilePortVlanTranslation);
        gponSrvProfileDao.insertGponSrvProfilePortVlanTranslation(gponSrvProfilePortVlanTranslation);
    }

    @Override
    public void refreshGponSrvProfileInfoList(Long entityId) {
        gponProfileRefreshService.refreshGponSrvProfileInfo(entityId);
        gponProfileRefreshService.refreshGponSrvProfileCfg(entityId);
        gponProfileRefreshService.refreshTopGponSrvProfile(entityId);
        gponProfileRefreshService.refreshGponSrvProfilePortNumProfile(entityId);
        gponProfileRefreshService.refreshGponSrvProfileEthPortConfig(entityId);
        gponProfileRefreshService.refreshGponSrvProfilePortVlanCfg(entityId);
        gponProfileRefreshService.refreshGponSrvProfilePortVlanTranslation(entityId);
        gponProfileRefreshService.refreshGponSrvProfilePortVlanAggregation(entityId);
        gponProfileRefreshService.refreshGponSrvProfilePortVlanTrunk(entityId);
        gponProfileRefreshService.refreshTopGponSrvPotsInfo(entityId);
    }

    @Override
    public GponSrvProfileInfo loadGponSrvProfileInfo(Long entityId, Integer profileId) {
        return gponSrvProfileDao.selectGponSrvProfileInfo(entityId, profileId);
    }

    @Override
    public GponSrvProfileEthPortConfig loadGponSrvProfileEthPortConfig(Long entityId, Integer profileIndex,
            Integer ethPortIdIndex) {
        return gponSrvProfileDao.selectGponSrvProfileEthPortConfig(entityId, profileIndex, ethPortIdIndex);
    }

    @Override
    public void refreshGponSrvProfileEthPortConfig(Long entityId, Integer profileIndex) {
        gponProfileRefreshService.refreshGponSrvProfileEthPortConfig(entityId, profileIndex);
    }

    @Override
    public GponSrvProfilePortVlanCfg loadGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex) {
        return gponSrvProfileDao.selectGponSrvProfilePortVlanCfg(entityId, profileIndex, portTypeIndex, portIndex);
    }

    @Override
    public void refreshGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex) {
        gponProfileRefreshService.refreshGponSrvProfilePortVlanCfg(entityId, profileIndex);
    }

    @Override
    public void refreshTopGponSrvPotsInfo(Long entityId, Integer profileIndex) {
        gponProfileRefreshService.refreshTopGponSrvPotsInfo(entityId, profileIndex);
    }

    @Override
    public List<GponSrvProfilePortVlanTranslation> loadGponSrvProfilePortVlanTranslation(Long entityId,
            Integer profileIndex, Integer portTypeIndex, Integer portIndex) {
        return gponSrvProfileDao.selectGponSrvProfilePortVlanTranslation(entityId, profileIndex, portTypeIndex,
                portIndex);
    }

    @Override
    public List<GponSrvProfilePortVlanAggregation> loadGponSrvProfilePortVlanAggregation(Long entityId,
            Integer profileIndex, Integer portTypeIndex, Integer portIndex) {
        return gponSrvProfileDao.selectGponSrvProfilePortVlanAggregation(entityId, profileIndex, portTypeIndex,
                portIndex);
    }

    @Override
    public List<GponSrvProfilePortVlanTrunk> loadGponSrvProfilePortVlanTrunk(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex) {
        return gponSrvProfileDao.selectGponSrvProfilePortVlanTrunk(entityId, profileIndex, portTypeIndex, portIndex);
    }

    @Override
    public void deleteGponSrvProfilePortVlanTranslation(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex, Integer vlanIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponSrvProfilePortVlanTranslation translation = new GponSrvProfilePortVlanTranslation();
        translation.setEntityId(entityId);
        translation.setGponSrvProfilePortVlanTransProfileIndex(profileIndex);
        translation.setGponSrvProfilePortVlanTransPortTypeIndex(portTypeIndex);
        translation.setGponSrvProfilePortVlanTransPortIdIndex(portIndex);
        translation.setGponSrvProfilePortVlanTransVlanIndex(vlanIndex);
        translation.setGponSrvProfilePortVlanTransRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, translation);
        gponSrvProfileDao.deleteGponSrvProfilePortVlanTranslation(entityId, profileIndex, portTypeIndex, portIndex,
                vlanIndex);
    }

    @Override
    public void deleteGponSrvProfilePortVlanAggregation(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex, Integer vlanIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponSrvProfilePortVlanAggregation aggregation = new GponSrvProfilePortVlanAggregation();
        aggregation.setEntityId(entityId);
        aggregation.setGponSrvProfilePortVlanAggrProfileIndex(profileIndex);
        aggregation.setGponSrvProfilePortVlanAggrPortTypeIndex(portTypeIndex);
        aggregation.setGponSrvProfilePortVlanAggrPortIdIndex(portIndex);
        aggregation.setGponSrvProfilePortVlanAggrVlanIndex(vlanIndex);
        aggregation.setGponSrvProfilePortVlanAggrRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, aggregation);
        gponSrvProfileDao.deleteGponSrvProfilePortVlanAggregation(entityId, profileIndex, portTypeIndex, portIndex,
                vlanIndex);
    }

    @Override
    public void deleteGponSrvProfilePortVlanTrunk(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponSrvProfilePortVlanTrunk trunk = new GponSrvProfilePortVlanTrunk();
        trunk.setEntityId(entityId);
        trunk.setGponSrvProfilePortVlanTrunkProfileIndex(profileIndex);
        trunk.setGponSrvProfilePortVlanTrunkPortTypeIndex(portTypeIndex);
        trunk.setGponSrvProfilePortVlanTrunkPortIdIndex(portIndex);
        trunk.setGponSrvProfilePortVlanTrunkRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, trunk);
        gponSrvProfileDao.deleteGponSrvProfilePortVlanTrunk(entityId, profileIndex, portTypeIndex, portIndex);
    }

    @Override
    public void refreshGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex) {
        gponProfileRefreshService.refreshGponSrvProfilePortVlanCfg(entityId, profileIndex, portTypeIndex, portIndex);
    }

    @Override
    public void modifyGponSrvProfilePortVlanMode(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfilePortVlanCfg.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfilePortVlanCfg);
        gponSrvProfileDao.updateGponSrvProfilePortVlanMode(gponSrvProfilePortVlanCfg);
        // 切换VLAN模式，清除VLAN规则
        gponSrvProfileDao.deletePortVlanRule(gponSrvProfilePortVlanCfg.getEntityId(),
                gponSrvProfilePortVlanCfg.getGponSrvProfilePortVlanProfileIndex(),
                gponSrvProfilePortVlanCfg.getGponSrvProfilePortVlanPortTypeIndex(),
                gponSrvProfilePortVlanCfg.getGponSrvProfilePortVlanPortIdIndex());
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }

    @Override
    public void modifyGponSrvProfilePortVlanTranslation(
            GponSrvProfilePortVlanTranslation gponSrvProfilePortVlanTranslation) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfilePortVlanTranslation.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfilePortVlanTranslation);
        gponSrvProfileDao.updateGponSrvProfilePortVlanTranslation(gponSrvProfilePortVlanTranslation);
    }

    @Override
    public void modifyGponSrvProfilePortVlanAggregation(
            GponSrvProfilePortVlanAggregation gponSrvProfilePortVlanAggregation) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfilePortVlanAggregation.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfilePortVlanAggregation);
        gponSrvProfileDao.updateGponSrvProfilePortVlanAggregation(gponSrvProfilePortVlanAggregation);
    }

    @Override
    public void modifyGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk gponSrvProfilePortVlanTrunk) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponSrvProfilePortVlanTrunk.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponSrvProfilePortVlanTrunk);
        gponSrvProfileDao.updateGponSrvProfilePortVlanTrunk(gponSrvProfilePortVlanTrunk);
    }

    @Override
    public List<TopGponSrvPotsInfo> loadTopGponSrvPotsInfoList(Long entityId, Integer profileIndex) {
        return gponSrvProfileDao.selectTopGponSrvPotsInfoList(entityId, profileIndex);
    }

    @Override
    public TopGponSrvPotsInfo loadTopGponSrvPotsInfo(Long entityId, Integer profileIndex, Integer potsIndex) {
        return gponSrvProfileDao.selectTopGponSrvPotsInfo(entityId, profileIndex, potsIndex);
    }

    @Override
    public void modifyTopGponSrvPotsInfo(TopGponSrvPotsInfo topGponSrvPotsInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topGponSrvPotsInfo.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topGponSrvPotsInfo);
        gponSrvProfileDao.updateTopGponSrvPotsInfo(topGponSrvPotsInfo);
    }

}
