/***********************************************************************
 * $Id: GponProfileServiceImpl.java,v1.0 2016年10月25日 下午12:07:02 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.GponProfileRefreshDao;
import com.topvision.ems.gpon.profile.dao.GponSrvProfileDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
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
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2016年10月25日-下午12:07:02
 *
 */
@Service("gponProfileRefreshService")
public class GponProfileRefreshServiceImpl extends BaseService
        implements GponProfileRefreshService, SynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponProfileRefreshDao gponProfileRefreshDao;
    @Autowired
    private GponSrvProfileDao gponSrvProfileDao;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        refreshGponProfile(event.getEntityId());
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public void refreshGponProfile(Long entityId) {
        try {
            refreshGponDbaProfileInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshGponDbaProfileInfo error:{}", e);
        }
        logger.info("refreshGponDbaProfileInfo finish!");

        try {
            refreshGponTrafficProfileInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshGponTrafficProfileInfo error:{}", e);
        }
        logger.info("refreshGponTrafficProfileInfo finish!");

        try {
            refreshGponLineProfileInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshGponLineProfileInfo error:{}", e);
        }
        logger.info("refreshGponLineProfileInfo finish!");

        try {
            refreshGponLineProfileTcont(entityId);
        } catch (Exception e) {
            logger.error("refreshGponLineProfileTcont error:{}", e);
        }
        logger.info("refreshGponLineProfileTcont finish!");

        try {
            refreshGponLineProfileGem(entityId);
        } catch (Exception e) {
            logger.error("refreshGponLineProfileGem error:{}", e);
        }
        logger.info("refreshGponLineProfileGem finish!");

        try {
            refreshGponLineProfileGemMap(entityId);
        } catch (Exception e) {
            logger.error("refreshGponLineProfileGemMap error:{}", e);
        }
        logger.info("refreshGponLineProfileGemMap finish!");

        try {
            refreshGponSrvProfileInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshGponSrvProfileInfo error:{}", e);
        }
        logger.info("refreshGponSrvProfileInfo finish!");

        try {
            refreshGponSrvProfileCfg(entityId);
        } catch (Exception e) {
            logger.error("refreshGponSrvProfileCfg error:{}", e);
        }
        logger.info("refreshGponSrvProfileCfg finish!");

        try {
            refreshGponSrvProfilePortNumProfile(entityId);
        } catch (Exception e) {
            logger.error("refreshGponSrvProfilePortNumProfile error:{}", e);
        }
        logger.info("refreshGponSrvProfilePortNumProfile finish!");

        try {
            refreshGponSrvProfileEthPortConfig(entityId);
        } catch (Exception e) {
            logger.error("refreshGponSrvProfileEthPortConfig error:{}", e);
        }
        logger.info("refreshGponSrvProfileEthPortConfig finish!");

        try {
            refreshGponSrvProfilePortVlanCfg(entityId);
        } catch (Exception e) {
            logger.error("refreshGponSrvProfilePortVlanCfg error:{}", e);
        }
        logger.info("refreshGponSrvProfilePortVlanCfg finish!");

        try {
            refreshGponSrvProfilePortVlanTranslation(entityId);
        } catch (Exception e) {
            logger.error("refreshGponSrvProfilePortVlanTranslation error:{}", e);
        }
        logger.info("refreshGponSrvProfilePortVlanTranslation finish!");

        try {
            refreshGponSrvProfilePortVlanAggregation(entityId);
        } catch (Exception e) {
            logger.error("refreshGponSrvProfilePortVlanAggregation error:{}", e);
        }
        logger.info("refreshGponSrvProfilePortVlanAggregation finish!");

        try {
            refreshGponSrvProfilePortVlanTrunk(entityId);
        } catch (Exception e) {
            logger.error("refreshGponSrvProfilePortVlanTrunk error:{}", e);
        }
        logger.info("refreshGponSrvProfilePortVlanTrunk finish!");

        try {
            refreshTopGponSrvProfile(entityId);
        } catch (Exception e) {
            logger.error("refreshTopGponSrvProfile error:{}", e);
        }
        logger.info("refreshTopGponSrvProfile finish!");
        // 代理模板
        try {
            refreshTopSIPAgentProfInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshTopSIPAgentProfInfo error:{}", e);
        }
        logger.info("refreshTopSIPAgentProfInfo finish!");
        // 数图模板
        try {
            refreshTopDigitMapProfInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshTopDigitMapProfInfo error:{}", e);
        }
        logger.info("refreshTopDigitMapProfInfo finish!");
        // SIP业务数据模板
        try {
            refreshTopSIPSrvProfInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshTopSIPSrvProfInfo error:{}", e);
        }
        logger.info("refreshTopSIPSrvProfInfo finish!");
        // VOIP媒体模板
        try {
            refreshTopVoipMediaProfInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshTopVoipMediaProfInfo error:{}", e);
        }
        logger.info("refreshTopVoipMediaProfInfo finish!");
        // POTS口模板
        try {
            refreshTopGponSrvPotsInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshTopGponSrvPotsInfo error:{}", e);
        }
        logger.info("refreshTopGponSrvPotsInfo finish!");
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }

    @Override
    public void refreshTopGponSrvPotsInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopGponSrvPotsInfo> potsTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                TopGponSrvPotsInfo.class);
        if (potsTable != null) {
            gponProfileRefreshDao.insertTopGponSrvPotsInfos(potsTable, entityId);
        }
    }

    @Override
    public void refreshTopVoipMediaProfInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopVoipMediaProfInfo> mediaTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                TopVoipMediaProfInfo.class);
        if (mediaTable != null) {
            gponProfileRefreshDao.insertTopVoipMediaProfInfos(mediaTable, entityId);
        }
    }

    @Override
    public void refreshTopSIPSrvProfInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopSIPSrvProfInfo> sipSrvTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                TopSIPSrvProfInfo.class);
        if (sipSrvTable != null) {
            gponProfileRefreshDao.insertTopSIPSrvProfInfos(sipSrvTable, entityId);
        }
    }

    @Override
    public void refreshTopDigitMapProfInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopDigitMapProfInfo> digitMapTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                TopDigitMapProfInfo.class);
        if (digitMapTable != null) {
            gponProfileRefreshDao.insertTopDigitMapProfInfos(digitMapTable, entityId);
        }
    }

    @Override
    public void refreshGponDbaProfileInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponDbaProfileInfo> dbaTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponDbaProfileInfo.class);
        if (dbaTable != null) {
            gponProfileRefreshDao.insertDBAProfiles(dbaTable, entityId);
        }
    }

    @Override
    public void refreshGponLineProfileGem(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponLineProfileGem> gemTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponLineProfileGem.class);
        if (gemTable != null) {
            gponProfileRefreshDao.insertGemProfiles(gemTable, entityId);
        }
    }

    @Override
    public void refreshGponLineProfileGemMap(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponLineProfileGemMap> gemMapTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponLineProfileGemMap.class);
        if (gemMapTable != null) {
            gponProfileRefreshDao.insertGemMapProfiles(gemMapTable, entityId);
        }
    }

    @Override
    public void refreshGponLineProfileInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponLineProfileInfo> lineTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponLineProfileInfo.class);
        if (lineTable != null) {
            gponProfileRefreshDao.insertLineProfiles(lineTable, entityId);
        }
    }

    @Override
    public void refreshGponLineProfileTcont(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponLineProfileTcont> tcontProfileTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponLineProfileTcont.class);
        if (tcontProfileTable != null) {
            gponProfileRefreshDao.insertTcontProfiles(tcontProfileTable, entityId);
        }
    }

    @Override
    public void refreshGponSrvProfileCfg(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfileCfg> srvProfileCfgTable = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponSrvProfileCfg.class);
        if (srvProfileCfgTable != null) {
            gponProfileRefreshDao.insertSrvProfileCfgs(srvProfileCfgTable, entityId);
        }
    }

    @Override
    public void refreshTopGponSrvProfile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopGponSrvProfile> topGponSrvProfile = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                TopGponSrvProfile.class);
        if (topGponSrvProfile != null) {
            gponProfileRefreshDao.insertTopGponSrvProfiles(topGponSrvProfile, entityId);
        }
    }

    @Override
    public void refreshGponSrvProfileEthPortConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfileEthPortConfig> srvEthPortTable = getGponProfilleFacade(snmpParam)
                .getGponProfileTable(snmpParam, GponSrvProfileEthPortConfig.class);
        if (srvEthPortTable != null) {
            gponProfileRefreshDao.insertSrvEthProfiles(srvEthPortTable, entityId);
        }
    }

    @Override
    public void refreshGponSrvProfileInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfileInfo> srvProfileInfos = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponSrvProfileInfo.class);
        if (srvProfileInfos != null) {
            gponProfileRefreshDao.insertSrvProfileInfos(srvProfileInfos, entityId);
        }
    }

    @Override
    public void refreshGponSrvProfilePortNumProfile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfilePortNumProfile> srvPortNumProfiles = getGponProfilleFacade(snmpParam)
                .getGponProfileTable(snmpParam, GponSrvProfilePortNumProfile.class);
        if (srvPortNumProfiles != null) {
            gponProfileRefreshDao.insertSrvPortNumProfiles(srvPortNumProfiles, entityId);
        }
    }

    @Override
    public void refreshGponSrvProfilePortVlanAggregation(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfilePortVlanAggregation> vlanAggrProfileTable = getGponProfilleFacade(snmpParam)
                .getGponProfileTable(snmpParam, GponSrvProfilePortVlanAggregation.class);
        if (vlanAggrProfileTable != null) {
            gponProfileRefreshDao.insertVlanAggrProfiles(vlanAggrProfileTable, entityId);
        }
    }

    @Override
    public void refreshGponSrvProfilePortVlanCfg(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfilePortVlanCfg> portVlanProfiles = getGponProfilleFacade(snmpParam)
                .getGponProfileTable(snmpParam, GponSrvProfilePortVlanCfg.class);
        if (portVlanProfiles != null) {
            gponProfileRefreshDao.insertPortVlanProfiles(portVlanProfiles, entityId);
        }
    }

    @Override
    public void refreshGponSrvProfilePortVlanTranslation(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfilePortVlanTranslation> vlanTranslationProfiles = getGponProfilleFacade(snmpParam)
                .getGponProfileTable(snmpParam, GponSrvProfilePortVlanTranslation.class);
        if (vlanTranslationProfiles != null) {
            gponProfileRefreshDao.insertVlanTranslationProfiles(vlanTranslationProfiles, entityId);
        }
    }

    @Override
    public void refreshGponSrvProfilePortVlanTrunk(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfilePortVlanTrunk> vlanTrunkProfiles = getGponProfilleFacade(snmpParam)
                .getGponProfileTable(snmpParam, GponSrvProfilePortVlanTrunk.class);
        if (vlanTrunkProfiles != null) {
            gponProfileRefreshDao.insertVlanTrunkProfiles(vlanTrunkProfiles, entityId);
        }
    }

    @Override
    public void refreshGponTrafficProfileInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponTrafficProfileInfo> trafficProfiles = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponTrafficProfileInfo.class);
        if (trafficProfiles != null) {
            gponProfileRefreshDao.insertTrafficProfiles(trafficProfiles, entityId);
        }
    }

    @Override
    public void refreshGponLineProfileTcont(Long entityId, Integer profileIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponLineProfileTcont tcont = new GponLineProfileTcont();
        tcont.setEntityId(entityId);
        tcont.setGponLineProfileTcontProfileIndex(profileIndex);
        List<GponLineProfileTcont> tconts = getGponProfilleFacade(snmpParam).getTcontInProfile(snmpParam, tcont);
        if (tconts != null) {
            gponProfileRefreshDao.insertTcontProfiles(tconts, entityId, profileIndex);
        }
    }

    @Override
    public void refreshGponLineProfileGem(Long entityId, Integer profileIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponLineProfileGem gem = new GponLineProfileGem();
        gem.setEntityId(entityId);
        gem.setGponLineProfileGemProfileIndex(profileIndex);
        List<GponLineProfileGem> gems = getGponProfilleFacade(snmpParam).getGemInProfile(snmpParam, gem);
        if (gems != null) {
            gponProfileRefreshDao.insertGemProfiles(gems, entityId, profileIndex);
            // 同步刷新GEM映射
            for (GponLineProfileGem gponLineProfileGem : gems) {
                refreshGponLineProfileGemMap(entityId, profileIndex, gponLineProfileGem.getGponLineProfileGemIndex());
            }
        }
    }

    @Override
    public void refreshGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponLineProfileGemMap gemMap = new GponLineProfileGemMap();
        gemMap.setEntityId(entityId);
        gemMap.setGponLineProfileGemMapProfileIndex(profileIndex);
        gemMap.setGponLineProfileGemMapGemIndex(gemIndex);
        List<GponLineProfileGemMap> gemMaps = getGponProfilleFacade(snmpParam).getGemMapInProfile(snmpParam, gemMap);
        if (gemMaps != null) {
            gponProfileRefreshDao.insertGemMapProfiles(gemMaps, entityId, profileIndex, gemIndex);
        }
    }

    @Override
    public void refreshGponSrvProfileEthPortConfig(Long entityId, Integer profileIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponSrvProfileEthPortConfig ethPortConfig = new GponSrvProfileEthPortConfig();
        ethPortConfig.setEntityId(entityId);
        ethPortConfig.setGponSrvProfileEthPortProfileIndex(profileIndex);
        List<GponSrvProfileEthPortConfig> ethPortConfigs = getGponProfilleFacade(snmpParam)
                .getEthPortConfigInProfile(snmpParam, ethPortConfig);
        if (ethPortConfigs != null) {
            gponProfileRefreshDao.insertEthPortConfigs(ethPortConfigs, entityId, profileIndex);
        }
    }

    @Override
    public void refreshGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponSrvProfilePortVlanCfg> portVlanCfgs = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                GponSrvProfilePortVlanCfg.class);
        if (portVlanCfgs != null) {
            gponProfileRefreshDao.insertPortVlanCfgs(portVlanCfgs, entityId, profileIndex);
            // TODO 同步刷新规则，由于mib表不支持getnext，此处刷新OLT下全部vlan规则
            refreshGponSrvProfilePortVlanTranslation(entityId);
            refreshGponSrvProfilePortVlanAggregation(entityId);
            refreshGponSrvProfilePortVlanTrunk(entityId);
        }
    }

    @Override
    public void refreshGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponSrvProfilePortVlanCfg portVlanCfg = new GponSrvProfilePortVlanCfg();
        portVlanCfg.setEntityId(entityId);
        portVlanCfg.setGponSrvProfilePortVlanProfileIndex(profileIndex);
        portVlanCfg.setGponSrvProfilePortVlanPortTypeIndex(0);
        portVlanCfg.setGponSrvProfilePortVlanPortIdIndex(portIndex);
        portVlanCfg = getGponProfilleFacade(snmpParam).getPortVlanCfg(snmpParam, portVlanCfg);
        if (portVlanCfg != null) {
            gponSrvProfileDao.updateGponSrvProfilePortVlanCfg(portVlanCfg);
            // TODO 刷新规则，由于mib表不支持getnext，此处刷新OLT下全部vlan规则
            refreshGponSrvProfilePortVlanTranslation(entityId);
            refreshGponSrvProfilePortVlanAggregation(entityId);
            refreshGponSrvProfilePortVlanTrunk(entityId);
        }
    }

    @Override
    public void refreshGponSrvProfileCfg(Long entityId, Integer gponSrvProfileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponSrvProfileCfg cfg = new GponSrvProfileCfg();
        cfg.setEntityId(entityId);
        cfg.setGponSrvProfileIndex(gponSrvProfileId);
        cfg = getGponProfilleFacade(snmpParam).getGponSrvProfileCfg(snmpParam, cfg);
        if (cfg != null) {
            gponProfileRefreshDao.insertGponSrvProfileCfg(cfg);
        }
    }

    @Override
    public void refreshGponSrvProfilePortNumProfile(Long entityId, Integer gponSrvProfileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponSrvProfilePortNumProfile portNum = new GponSrvProfilePortNumProfile();
        portNum.setEntityId(entityId);
        portNum.setGponSrvProfilePortNumProfileIndex(gponSrvProfileId);
        portNum = getGponProfilleFacade(snmpParam).getPortNumProfile(snmpParam, portNum);
        if (portNum != null) {
            gponProfileRefreshDao.insertGponSrvProfilePortNumProfile(portNum);
        }
    }

    @Override
    public void refreshTopSIPAgentProfInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopSIPAgentProfInfo> topSIPAgentProfInfo = getGponProfilleFacade(snmpParam).getGponProfileTable(snmpParam,
                TopSIPAgentProfInfo.class);
        if (topSIPAgentProfInfo != null) {
            gponProfileRefreshDao.insertTopSIPAgentProfInfos(topSIPAgentProfInfo, entityId);
        }
    }

    @Override
    public void refreshTopGponSrvProfile(Long entityId, Integer gponSrvProfileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopGponSrvProfile cfg = new TopGponSrvProfile();
        cfg.setEntityId(entityId);
        cfg.setTopGponSrvProfileIndex(gponSrvProfileId);
        cfg = getGponProfilleFacade(snmpParam).getTopGponSrvProfile(snmpParam, cfg);
        if (cfg != null) {
            gponProfileRefreshDao.insertTopGponSrvProfile(cfg);
        }
    }

    @Override
    public void refreshTopGponSrvPotsInfo(Long entityId, Integer profileIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopGponSrvPotsInfo pots = new TopGponSrvPotsInfo();
        pots.setEntityId(entityId);
        pots.setTopGponSrvPotsInfoProfIdx(profileIndex);
        List<TopGponSrvPotsInfo> potsList = getGponProfilleFacade(snmpParam).getTopGponSrvPotsInfo(snmpParam, pots);
        if (potsList != null) {
            gponProfileRefreshDao.insertTopGponSrvPotsInfos(potsList, entityId, profileIndex);
        }
    }

}
