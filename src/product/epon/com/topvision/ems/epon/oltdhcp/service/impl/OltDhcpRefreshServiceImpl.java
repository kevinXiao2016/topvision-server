/***********************************************************************
 * $Id: OltDhcpRefreshServiceImpl.java,v1.0 2017年11月17日 上午10:44:58 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpRefreshDao;
import com.topvision.ems.epon.oltdhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpCpeInfo;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpServerGroup;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStaticIp;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVLANCfg;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVifCfg;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltPppoeStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpRefreshService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author haojie
 * @created @2017年11月17日-上午10:44:58
 *
 */
@Service
public class OltDhcpRefreshServiceImpl extends BaseService implements OltDhcpRefreshService, SynchronizedListener {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    protected MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpRefreshDao oltDhcpRefreshDao;

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long entityId = event.getEntityId();
        try {
            refreshTopOltDhcpCpeInfo(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltDhcpCpeInfo wrong", e);
        }
        logger.info("refreshTopOltDhcpCpeInfo finish");

        try {
            refreshTopOltDhcpGlobalObjects(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltDhcpGlobalObjects wrong", e);
        }
        logger.info("refreshTopOltDhcpGlobalObjects finish");
        try {
            refreshTopOltDhcpPortAttribute(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltDhcpPortAttribute wrong", e);
        }
        logger.info("refreshTopOltDhcpPortAttribute finish");
        try {
            refreshTopOltDhcpServerGroup(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltDhcpServerGroup wrong", e);
        }
        logger.info("refreshTopOltDhcpServerGroup finish");
        try {
            refreshTopOltDhcpStaticIp(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltDhcpStaticIp wrong", e);
        }
        logger.info("refreshTopOltDhcpStaticIp finish");

        try {
            refreshTopOltDhcpStatisticsObjects(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltDhcpStatisticsObjects wrong", e);
        }
        logger.info("refreshTopOltDhcpStatisticsObjects finish");

        try {
            refreshTopOltDhcpVifCfg(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltDhcpVifCfg wrong", e);
        }
        logger.info("refreshTopOltDhcpVifCfg finish");

        try {
            refreshTopOltDhcpVLANCfg(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltDhcpVLANCfg wrong", e);
        }
        logger.info("refreshTopOltDhcpVLANCfg finish");

        try {
            refreshTopOltPppoeStatisticsObjects(entityId);
        } catch (Exception e) {
            logger.error("refreshTopOltPppoeStatisticsObjects wrong", e);
        }
        logger.info("refreshTopOltPppoeStatisticsObjects finish");
    }

    @Override
    public void refreshTopOltDhcpCpeInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOltDhcpCpeInfo> topOltDhcpCpeInfos = getOltDhcpFacade(snmpParam).getTopOltDhcpCpeInfos(snmpParam);
        if (topOltDhcpCpeInfos != null) {
            oltDhcpRefreshDao.batchInsertTopOltDhcpCpeInfos(entityId, topOltDhcpCpeInfos);
        }
    }

    @Override
    public void refreshTopOltDhcpGlobalObjects(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects topOltDhcpGlobalObjects = getOltDhcpFacade(snmpParam)
                .getTopOltDhcpGlobalObjects(snmpParam);
        if (topOltDhcpGlobalObjects != null) {
            oltDhcpRefreshDao.insertTopOltDhcpGlobalObjects(entityId, topOltDhcpGlobalObjects);
        }
    }

    @Override
    public void refreshTopOltDhcpPortAttribute(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOltDhcpPortAttribute> topOltDhcpPortAttributes = getOltDhcpFacade(snmpParam)
                .getTopOltDhcpPortAttributes(snmpParam);
        if (topOltDhcpPortAttributes != null) {
            oltDhcpRefreshDao.batchInsertTopOltDhcpPortAttributes(entityId, topOltDhcpPortAttributes);
        }
    }

    @Override
    public void refreshTopOltDhcpServerGroup(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOltDhcpServerGroup> topOltDhcpServerGroups = getOltDhcpFacade(snmpParam)
                .getTopOltDhcpServerGroups(snmpParam);
        if (topOltDhcpServerGroups != null) {
            oltDhcpRefreshDao.batchInsertTopOltDhcpServerGroups(entityId, topOltDhcpServerGroups);
        }
    }

    @Override
    public void refreshTopOltDhcpStaticIp(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOltDhcpStaticIp> topOltDhcpStaticIps = getOltDhcpFacade(snmpParam).getTopOltDhcpStaticIps(snmpParam);
        if (topOltDhcpStaticIps != null) {
            oltDhcpRefreshDao.batchInsertTopOltDhcpStaticIps(entityId, topOltDhcpStaticIps);
        }
    }

    @Override
    public void refreshTopOltDhcpStatisticsObjects(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpStatisticsObjects topOltDhcpStatisticsObjects = getOltDhcpFacade(snmpParam)
                .getTopOltDhcpStatisticsObjects(snmpParam);
        if (topOltDhcpStatisticsObjects != null) {
            oltDhcpRefreshDao.insertTopOltDhcpStatisticsObjects(entityId, topOltDhcpStatisticsObjects);
        }
    }

    @Override
    public void refreshTopOltDhcpVifCfg(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOltDhcpVifCfg> topOltDhcpVifCfgs = getOltDhcpFacade(snmpParam).getTopOltDhcpVifCfgs(snmpParam);
        if (topOltDhcpVifCfgs != null) {
            oltDhcpRefreshDao.batchInsertTopOltDhcpVifCfgs(entityId, topOltDhcpVifCfgs);
        }
    }

    @Override
    public void refreshTopOltDhcpVLANCfg(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOltDhcpVLANCfg> topOltDhcpVLANCfgs = getOltDhcpFacade(snmpParam).getTopOltDhcpVLANCfgs(snmpParam);
        if (topOltDhcpVLANCfgs != null) {
            oltDhcpRefreshDao.batchInsertTopOltDhcpVLANCfgs(entityId, topOltDhcpVLANCfgs);
        }
    }

    @Override
    public void refreshTopOltPppoeStatisticsObjects(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltPppoeStatisticsObjects topOltPppoeStatisticsObjects = getOltDhcpFacade(snmpParam)
                .getTopOltPppoeStatisticsObjects(snmpParam);
        if (topOltPppoeStatisticsObjects != null) {
            oltDhcpRefreshDao.insertTopOltPppoeStatisticsObjects(entityId, topOltPppoeStatisticsObjects);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    private OltDhcpFacade getOltDhcpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OltDhcpFacade.class);
    }
}
