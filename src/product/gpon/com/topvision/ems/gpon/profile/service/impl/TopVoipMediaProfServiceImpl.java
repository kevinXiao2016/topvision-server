/***********************************************************************
 * $Id: TopVoipMediaProfServiceImpl.java,v1.0 2017年6月21日 上午9:10:44 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.TopVoipMediaProfDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopVoipMediaProfInfo;
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.gpon.profile.service.TopVoipMediaProfService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年6月21日-上午9:10:44
 *
 */
@Service("topVoipMediaProfService")
public class TopVoipMediaProfServiceImpl implements TopVoipMediaProfService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;
    @Autowired
    private TopVoipMediaProfDao topVoipMediaProfDao;

    @Override
    public List<TopVoipMediaProfInfo> loadTopVoipMediaProfInfoList(Long entityId) {
        List<TopVoipMediaProfInfo> topVoipMediaProfInfos = topVoipMediaProfDao.selectTopVoipMediaProfInfoList(entityId);
        TopVoipMediaProfInfo topVoipMediaProfInfo = new TopVoipMediaProfInfo();
        topVoipMediaProfInfo.setTopVoipMediaProfName("no bind");
        topVoipMediaProfInfo.setTopVoipMediaProfIdx(0);
        topVoipMediaProfInfos.add(0, topVoipMediaProfInfo);
        return topVoipMediaProfInfos;
    }

    @Override
    public TopVoipMediaProfInfo loadTopVoipMediaProfInfo(Long entityId, Integer profileId) {
        return topVoipMediaProfDao.selectTopVoipMediaProfInfo(entityId, profileId);
    }

    @Override
    public void addTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topVoipMediaProfInfo.getEntityId());
        topVoipMediaProfInfo.setTopVoipMediaRowStatus(RowStatus.CREATE_AND_GO);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topVoipMediaProfInfo);
        topVoipMediaProfDao.insertTopVoipMediaProfInfo(topVoipMediaProfInfo);
    }

    @Override
    public void modifyTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topVoipMediaProfInfo.getEntityId());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topVoipMediaProfInfo);
        topVoipMediaProfDao.updateTopVoipMediaProfInfo(topVoipMediaProfInfo);
    }

    @Override
    public void deleteTopVoipMediaProfInfo(Long entityId, Integer profileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopVoipMediaProfInfo topVoipMediaProfInfo = new TopVoipMediaProfInfo();
        topVoipMediaProfInfo.setEntityId(entityId);
        topVoipMediaProfInfo.setTopVoipMediaProfIdx(profileId);
        topVoipMediaProfInfo.setTopVoipMediaRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topVoipMediaProfInfo);
        topVoipMediaProfDao.deleteTopVoipMediaProfInfo(entityId, profileId);
    }

    @Override
    public void refreshTopVoipMediaProfInfo(Long entityId) {
        gponProfileRefreshService.refreshTopVoipMediaProfInfo(entityId);
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }
}
