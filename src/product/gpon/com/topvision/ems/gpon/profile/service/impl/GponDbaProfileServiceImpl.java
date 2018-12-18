/***********************************************************************
 * $Id: GponDbaProfileServiceImpl.java,v1.0 2016年12月17日 上午9:09:27 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.GponDbaProfileDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
import com.topvision.ems.gpon.profile.facade.domain.GponDbaProfileInfo;
import com.topvision.ems.gpon.profile.service.GponDbaProfileService;
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2016年12月17日-上午9:09:27
 *
 */
@Service("gponDbaProfileService")
public class GponDbaProfileServiceImpl implements GponDbaProfileService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponDbaProfileDao gponDbaProfileDao;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;

    @Override
    public List<GponDbaProfileInfo> loadGponDbaProfileInfoList(Long entityId) {
        return gponDbaProfileDao.selectGponDbaProfileInfoList(entityId);
    }

    @Override
    public GponDbaProfileInfo loadGponDbaProfileInfo(Long entityId, Integer profileId) {
        return gponDbaProfileDao.selectGponDbaProfileInfo(entityId, profileId);
    }

    @Override
    public void addGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponDbaProfileInfo.getEntityId());
        gponDbaProfileInfo.setGponDbaProfileRowStatus(RowStatus.CREATE_AND_GO);
        if (gponDbaProfileInfo.getGponDbaProfileType() == 0) {
            //创建空模版的时候不下发type
            gponDbaProfileInfo.setGponDbaProfileType(null);
        }
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponDbaProfileInfo);
        gponDbaProfileDao.insertGponDbaProfileInfo(gponDbaProfileInfo);
    }

    @Override
    public void modifyGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponDbaProfileInfo.getEntityId());
        if (gponDbaProfileInfo.getGponDbaProfileType() == 0) {
            gponDbaProfileInfo.setGponDbaProfileType(null);
        }
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponDbaProfileInfo);
        gponDbaProfileDao.updateGponDbaProfileInfo(gponDbaProfileInfo);
    }

    @Override
    public void deleteGponDbaProfileInfo(Long entityId, Integer gponDbaProfileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponDbaProfileInfo gponDbaProfileInfo = new GponDbaProfileInfo();
        gponDbaProfileInfo.setEntityId(entityId);
        gponDbaProfileInfo.setGponDbaProfileId(gponDbaProfileId);
        gponDbaProfileInfo.setGponDbaProfileRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, gponDbaProfileInfo);
        gponDbaProfileDao.deleteGponDbaProfileInfo(entityId, gponDbaProfileId);
    }

    @Override
    public void refreshGponDbaProfileList(Long entityId) {
        gponProfileRefreshService.refreshGponDbaProfileInfo(entityId);
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }
}
