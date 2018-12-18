/***********************************************************************
 * $Id: TopDigitMapProfServiceImpl.java,v1.0 2017年6月21日 下午1:29:55 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.TopDigitMapProfDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.gpon.profile.service.TopDigitMapProfService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年6月21日-下午1:29:55
 *
 */
@Service("topDigitMapProfService")
public class TopDigitMapProfServiceImpl implements TopDigitMapProfService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;
    @Autowired
    private TopDigitMapProfDao topDigitMapProfDao;

    @Override
    public List<TopDigitMapProfInfo> loadTopDigitMapProfInfoList(Long entityId) {
        return topDigitMapProfDao.selectTopDigitMapProfInfoList(entityId);
    }

    @Override
    public TopDigitMapProfInfo loadTopDigitMapProfInfo(Long entityId, Integer profileId) {
        return topDigitMapProfDao.selectTopDigitMapProfInfo(entityId, profileId);
    }

    @Override
    public void addTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topDigitMapProfInfo.getEntityId());
        topDigitMapProfInfo.setTopDigitMapRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topDigitMapProfInfo);
        topDigitMapProfDao.insertTopDigitMapProfInfo(topDigitMapProfInfo);
    }

    @Override
    public void modifyTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topDigitMapProfInfo.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topDigitMapProfInfo);
        topDigitMapProfDao.updateTopDigitMapProfInfo(topDigitMapProfInfo);
    }

    @Override
    public void deleteTopDigitMapProfInfo(Long entityId, Integer profileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopDigitMapProfInfo topDigitMapProfInfo = new TopDigitMapProfInfo();
        topDigitMapProfInfo.setEntityId(entityId);
        topDigitMapProfInfo.setTopDigitMapProfIdx(profileId);
        topDigitMapProfInfo.setTopDigitMapRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topDigitMapProfInfo);
        topDigitMapProfDao.deleteTopDigitMapProfInfo(entityId, profileId);
    }

    @Override
    public void refreshTopDigitMapProfInfo(Long entityId) {
        gponProfileRefreshService.refreshTopDigitMapProfInfo(entityId);
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }
}
