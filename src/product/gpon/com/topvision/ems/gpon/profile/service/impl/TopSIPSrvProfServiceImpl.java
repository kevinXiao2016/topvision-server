/***********************************************************************
 * $Id: TopSIPSrvProfServiceImpl.java,v1.0 2017年6月21日 上午10:14:03 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.TopSIPSrvProfDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.gpon.profile.service.TopSIPSrvProfService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年6月21日-上午10:14:03
 *
 */
@Service("topSIPSrvProfService")
public class TopSIPSrvProfServiceImpl implements TopSIPSrvProfService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;
    @Autowired
    private TopSIPSrvProfDao topSIPSrvProfDao;

    @Override
    public List<TopSIPSrvProfInfo> loadTopSIPSrvProfInfoList(Long entityId) {
        return topSIPSrvProfDao.selectTopSIPSrvProfInfoList(entityId);
    }

    @Override
    public TopSIPSrvProfInfo loadTopSIPSrvProfInfo(Long entityId, Integer profileId) {
        return topSIPSrvProfDao.selectTopSIPSrvProfInfo(entityId, profileId);
    }

    @Override
    public void addTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topSIPSrvProfInfo.getEntityId());
        topSIPSrvProfInfo.setTopSIPSrvProfRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topSIPSrvProfInfo);
        topSIPSrvProfDao.insertTopSIPSrvProfInfo(topSIPSrvProfInfo);
    }

    @Override
    public void modifyTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topSIPSrvProfInfo.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topSIPSrvProfInfo);
        topSIPSrvProfDao.updateTopSIPSrvProfInfo(topSIPSrvProfInfo);
    }

    @Override
    public void deleteTopSIPSrvProfInfo(Long entityId, Integer profileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopSIPSrvProfInfo topSIPSrvProfInfo = new TopSIPSrvProfInfo();
        topSIPSrvProfInfo.setEntityId(entityId);
        topSIPSrvProfInfo.setTopSIPSrvProfIdx(profileId);
        topSIPSrvProfInfo.setTopSIPSrvProfRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topSIPSrvProfInfo);
        topSIPSrvProfDao.deleteTopSIPSrvProfInfo(entityId, profileId);
    }

    @Override
    public void refreshTopSIPSrvProfInfo(Long entityId) {
        gponProfileRefreshService.refreshTopSIPSrvProfInfo(entityId);
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }
}
