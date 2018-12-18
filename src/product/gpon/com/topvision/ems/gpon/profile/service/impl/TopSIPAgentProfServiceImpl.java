/***********************************************************************
 * $Id: TopSIPAgentProfServiceImpl.java,v1.0 2017年5月5日 下午1:31:20 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.profile.dao.TopSIPAgentProfDao;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.gpon.profile.service.TopSIPAgentProfService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年5月5日-下午1:31:20
 *
 */
@Service("topSIPAgentProfService")
public class TopSIPAgentProfServiceImpl implements TopSIPAgentProfService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;
    @Autowired
    private TopSIPAgentProfDao topSIPAgentProfDao;

    @Override
    public List<TopSIPAgentProfInfo> loadTopSIPAgentProfInfoList(Long entityId) {
        List<TopSIPAgentProfInfo> topSIPAgentProfInfos = topSIPAgentProfDao.selectTopSIPAgentProfInfoList(entityId);
        TopSIPAgentProfInfo topSIPAgentProfInfo = new TopSIPAgentProfInfo();
        topSIPAgentProfInfo.setTopSIPAgtProfName("no bind");
        topSIPAgentProfInfo.setTopSIPAgtProfIdx(0);
        topSIPAgentProfInfos.add(0, topSIPAgentProfInfo);
        return topSIPAgentProfInfos;
    }

    @Override
    public TopSIPAgentProfInfo loadTopSIPAgentProfInfo(Long entityId, Integer profileId) {
        return topSIPAgentProfDao.selectTopSIPAgentProfInfo(entityId, profileId);
    }

    @Override
    public void addTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topSIPAgentProfInfo.getEntityId());
        topSIPAgentProfInfo.setTopSIPAgtRowStatus(RowStatus.CREATE_AND_GO);
        // outbound服务器和注册服务器采用和代理服务器一样的配置
        topSIPAgentProfInfo.setTopSIPAgtOutboundAddr(topSIPAgentProfInfo.getTopSIPAgtProxyAddr());
        topSIPAgentProfInfo.setTopSIPAgtOutboundPort(topSIPAgentProfInfo.getTopSIPAgtProxyPort());
        topSIPAgentProfInfo.setTopSIPAgtSecOutboundAddr(topSIPAgentProfInfo.getTopSIPAgtSecProxyAddr());
        topSIPAgentProfInfo.setTopSIPAgtSecOutboundPort(topSIPAgentProfInfo.getTopSIPAgtSecProxyPort());
        topSIPAgentProfInfo.setTopSIPAgtRegAddr(topSIPAgentProfInfo.getTopSIPAgtProxyAddr());
        topSIPAgentProfInfo.setTopSIPAgtRegPort(topSIPAgentProfInfo.getTopSIPAgtProxyPort());
        topSIPAgentProfInfo.setTopSIPAgtSecRegAddr(topSIPAgentProfInfo.getTopSIPAgtSecProxyAddr());
        topSIPAgentProfInfo.setTopSIPAgtSecRegPort(topSIPAgentProfInfo.getTopSIPAgtSecProxyPort());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topSIPAgentProfInfo);
        topSIPAgentProfDao.insertTopSIPAgentProfInfo(topSIPAgentProfInfo);
    }

    @Override
    public void modifyTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topSIPAgentProfInfo.getEntityId());
        // outbound服务器和注册服务器采用和代理服务器一样的配置
        topSIPAgentProfInfo.setTopSIPAgtOutboundAddr(topSIPAgentProfInfo.getTopSIPAgtProxyAddr());
        topSIPAgentProfInfo.setTopSIPAgtOutboundPort(topSIPAgentProfInfo.getTopSIPAgtProxyPort());
        topSIPAgentProfInfo.setTopSIPAgtSecOutboundAddr(topSIPAgentProfInfo.getTopSIPAgtSecProxyAddr());
        topSIPAgentProfInfo.setTopSIPAgtSecOutboundPort(topSIPAgentProfInfo.getTopSIPAgtSecProxyPort());
        topSIPAgentProfInfo.setTopSIPAgtRegAddr(topSIPAgentProfInfo.getTopSIPAgtProxyAddr());
        topSIPAgentProfInfo.setTopSIPAgtRegPort(topSIPAgentProfInfo.getTopSIPAgtProxyPort());
        topSIPAgentProfInfo.setTopSIPAgtSecRegAddr(topSIPAgentProfInfo.getTopSIPAgtSecProxyAddr());
        topSIPAgentProfInfo.setTopSIPAgtSecRegPort(topSIPAgentProfInfo.getTopSIPAgtSecProxyPort());
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topSIPAgentProfInfo);
        topSIPAgentProfDao.updateTopSIPAgentProfInfo(topSIPAgentProfInfo);
    }

    @Override
    public void deleteTopSIPAgentProfInfo(Long entityId, Integer profileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopSIPAgentProfInfo topSIPAgentProfInfo = new TopSIPAgentProfInfo();
        topSIPAgentProfInfo.setEntityId(entityId);
        topSIPAgentProfInfo.setTopSIPAgtProfIdx(profileId);
        topSIPAgentProfInfo.setTopSIPAgtRowStatus(RowStatus.DESTORY);
        getGponProfilleFacade(snmpParam).setGponProfileTable(snmpParam, topSIPAgentProfInfo);
        topSIPAgentProfDao.deleteTopSIPAgentProfInfo(entityId, profileId);
    }

    @Override
    public void refreshTopSIPAgentProfInfo(Long entityId) {
        gponProfileRefreshService.refreshTopSIPAgentProfInfo(entityId);
    }

    private GponProfileFacade getGponProfilleFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponProfileFacade.class);
    }

}
