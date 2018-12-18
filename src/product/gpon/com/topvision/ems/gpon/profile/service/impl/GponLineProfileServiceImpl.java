/***********************************************************************
 * $Id: GponLineProfileServiceImpl.java,v1.0 2016年12月17日 上午9:08:48 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.GponLineProfileDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;
import com.topvision.ems.gpon.profile.service.GponLineProfileService;
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2016年12月17日-上午9:08:48
 *
 */
@Service("gponLineProfileService")
public class GponLineProfileServiceImpl implements GponLineProfileService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponLineProfileDao gponLineProfileDao;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;

    @Override
    public List<GponLineProfileInfo> loadGponLineProfileInfoList(Long entityId) {
        return gponLineProfileDao.selectGponLineProfileInfoList(entityId);
    }

    @Override
    public List<GponLineProfileTcont> loadGponLineProfileTcontList(Long entityId, Integer profileIndex) {
        return gponLineProfileDao.selectGponLineProfileTcontList(entityId, profileIndex);
    }

    @Override
    public List<GponLineProfileGem> loadGponLineProfileGemList(Long entityId, Integer profileIndex) {
        return gponLineProfileDao.selectGponLineProfileGemList(entityId, profileIndex);
    }

    @Override
    public List<GponLineProfileGemMap> loadGponLineProfileGemMapList(Long entityId, Integer profileIndex,
            Integer gemIndex) {
        return gponLineProfileDao.selectGponLineProfileGemMapList(entityId, profileIndex, gemIndex);
    }

    @Override
    public GponLineProfileInfo loadGponLineProfileInfo(Long entityId, Integer profileIndex) {
        return gponLineProfileDao.selectGponLineProfileInfo(entityId, profileIndex);
    }

    @Override
    public GponLineProfileTcont loadGponLineProfileTcont(Long entityId, Integer profileIndex, Integer tcontIndex) {
        return gponLineProfileDao.selectGponLineProfileTcont(entityId, profileIndex, tcontIndex);
    }

    @Override
    public GponLineProfileGem loadGponLineProfileGem(Long entityId, Integer profileIndex, Integer gemIndex) {
        return gponLineProfileDao.selectGponLineProfileGem(entityId, profileIndex, gemIndex);
    }

    @Override
    public GponLineProfileGemMap loadGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex,
            Integer gemMapIndex) {
        return gponLineProfileDao.selectGponLineProfileGemMap(entityId, profileIndex, gemIndex, gemMapIndex);
    }

    @Override
    public void modifyGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponLineProfileInfo.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileInfo);
        gponLineProfileDao.updateGponLineProfileInfo(gponLineProfileInfo);
    }

    @Override
    public void modifyGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponLineProfileTcont.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileTcont);
        gponLineProfileDao.updateGponLineProfileTcont(gponLineProfileTcont);
    }

    @Override
    public void modifyGponLineProfileGem(GponLineProfileGem gponLineProfileGem) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponLineProfileGem.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileGem);
        gponLineProfileDao.updateGponLineProfileGem(gponLineProfileGem);
    }

    @Override
    public void modifyGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponLineProfileGemMap.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileGemMap);
        gponLineProfileDao.updateGponLineProfileGemMap(gponLineProfileGemMap);
    }

    @Override
    public void deleteGponLineProfileInfo(Long entityId, Integer gponLineProfileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponLineProfileInfo gponLineProfileInfo = new GponLineProfileInfo();
        gponLineProfileInfo.setEntityId(entityId);
        gponLineProfileInfo.setGponLineProfileId(gponLineProfileId);
        gponLineProfileInfo.setGponLineProfileRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileInfo);
        gponLineProfileDao.deleteGponLineProfileInfo(entityId, gponLineProfileId);
    }

    @Override
    public void deleteGponLineProfileTcont(Long entityId, Integer profileIndex, Integer tcontIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponLineProfileTcont gponLineProfileTcont = new GponLineProfileTcont();
        gponLineProfileTcont.setEntityId(entityId);
        gponLineProfileTcont.setGponLineProfileTcontProfileIndex(profileIndex);
        gponLineProfileTcont.setGponLineProfileTcontIndex(tcontIndex);
        gponLineProfileTcont.setGponLineProfileTcontRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileTcont);
        gponLineProfileDao.deleteGponLineProfileTcont(entityId, profileIndex, tcontIndex);
    }

    @Override
    public void deleteGponLineProfileGem(Long entityId, Integer profileIndex, Integer gemIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponLineProfileGem gponLineProfileGem = new GponLineProfileGem();
        gponLineProfileGem.setEntityId(entityId);
        gponLineProfileGem.setGponLineProfileGemProfileIndex(profileIndex);
        gponLineProfileGem.setGponLineProfileGemIndex(gemIndex);
        gponLineProfileGem.setGponLineProfileGemRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileGem);
        gponLineProfileDao.deleteGponLineProfileGem(entityId, profileIndex, gemIndex);
    }

    @Override
    public void deleteGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex, Integer gemMapIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponLineProfileGemMap gponLineProfileGemMap = new GponLineProfileGemMap();
        gponLineProfileGemMap.setEntityId(entityId);
        gponLineProfileGemMap.setGponLineProfileGemMapProfileIndex(profileIndex);
        gponLineProfileGemMap.setGponLineProfileGemMapGemIndex(gemIndex);
        gponLineProfileGemMap.setGponLineProfileGemMapIndex(gemMapIndex);
        gponLineProfileGemMap.setGponLineProfileGemMapRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileGemMap);
        gponLineProfileDao.deleteGponLineProfileGemMap(entityId, profileIndex, gemIndex, gemMapIndex);
    }

    @Override
    public void addGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponLineProfileInfo.getEntityId());
        gponLineProfileInfo.setGponLineProfileRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileInfo);
        gponLineProfileDao.insertGponLineProfileInfo(gponLineProfileInfo);
    }

    @Override
    public void addGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponLineProfileTcont.getEntityId());
        gponLineProfileTcont.setGponLineProfileTcontRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileTcont);
        gponLineProfileDao.insertGponLineProfileTcont(gponLineProfileTcont);
    }

    @Override
    public void addGponLineProfileGem(GponLineProfileGem gponLineProfileGem) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponLineProfileGem.getEntityId());
        gponLineProfileGem.setGponLineProfileGemRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileGem);
        gponLineProfileDao.insertGponLineProfileGem(gponLineProfileGem);
    }

    @Override
    public void addGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponLineProfileGemMap.getEntityId());
        gponLineProfileGemMap.setGponLineProfileGemMapRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponLineProfileGemMap);
        gponLineProfileDao.insertGponLineProfileGemMap(gponLineProfileGemMap);
    }

    @Override
    public void refreshGponLineProfileList(Long entityId) {
        gponProfileRefreshService.refreshGponLineProfileInfo(entityId);
        gponProfileRefreshService.refreshGponLineProfileTcont(entityId);
        gponProfileRefreshService.refreshGponLineProfileGem(entityId);
        gponProfileRefreshService.refreshGponLineProfileGemMap(entityId);
    }

    @Override
    public void refreshGponLineProfileTcontList(Long entityId, Integer profileIndex) {
        gponProfileRefreshService.refreshGponLineProfileTcont(entityId, profileIndex);
    }

    @Override
    public void refreshGponLineProfileGemList(Long entityId, Integer profileIndex) {
        gponProfileRefreshService.refreshGponLineProfileGem(entityId, profileIndex);
    }

    @Override
    public void refreshGponLineProfileGemMapList(Long entityId, Integer profileIndex, Integer gemIndex) {
        gponProfileRefreshService.refreshGponLineProfileGemMap(entityId, profileIndex, gemIndex);
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }

    @Override
    public Integer getMappingModeByProfileId(Long entityId, Integer profileId) {
        return gponLineProfileDao.getMappingModeByProfileId(entityId, profileId);
    }

}
