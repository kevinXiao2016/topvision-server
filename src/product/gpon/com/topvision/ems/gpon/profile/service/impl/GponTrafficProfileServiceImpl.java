/***********************************************************************
 * $Id: GponTrafficProfileServiceImpl.java,v1.0 2016年12月17日 上午9:01:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.GponTrafficProfileDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
import com.topvision.ems.gpon.profile.facade.domain.GponTrafficProfileInfo;
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.gpon.profile.service.GponTrafficProfileService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2016年12月17日-上午9:01:38
 *
 */
@Service("gponTrafficProfileService")
public class GponTrafficProfileServiceImpl implements GponTrafficProfileService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponTrafficProfileDao gponTrafficProfileDao;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;

    @Override
    public List<GponTrafficProfileInfo> loadGponTrafficProfileInfoList(Long entityId) {
        return gponTrafficProfileDao.selectGponTrafficProfileInfoList(entityId);
    }

    @Override
    public GponTrafficProfileInfo loadGponTrafficProfileInfo(Long entityId, Integer profileId) {
        return gponTrafficProfileDao.selectGponTrafficProfileInfo(entityId, profileId);
    }

    @Override
    public void modifyGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponTrafficProfileInfo.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponTrafficProfileInfo);
        gponTrafficProfileDao.updateGponTrafficProfileInfo(gponTrafficProfileInfo);
    }

    @Override
    public void deleteGponTrafficProfileInfo(Long entityId, Integer gponTrafficProfileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponTrafficProfileInfo gponTrafficProfileInfo = new GponTrafficProfileInfo();
        gponTrafficProfileInfo.setEntityId(entityId);
        gponTrafficProfileInfo.setGponTrafficProfileId(gponTrafficProfileId);
        gponTrafficProfileInfo.setGponTrafficProfileRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponTrafficProfileInfo);
        gponTrafficProfileDao.deleteGponTrafficProfileInfo(entityId, gponTrafficProfileId);
    }

    @Override
    public void addGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponTrafficProfileInfo.getEntityId());
        gponTrafficProfileInfo.setGponTrafficProfileRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponTrafficProfileInfo);
        gponTrafficProfileDao.insertGponTrafficProfileInfo(gponTrafficProfileInfo);
    }

    @Override
    public void refreshGponTrafficProfileList(Long entityId) {
        gponProfileRefreshService.refreshGponTrafficProfileInfo(entityId);
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }

}
