/***********************************************************************
 * $Id: OltDhcpBaseServiceImpl.java,v1.0 2017年11月21日 下午1:17:03 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.logicinterface.service.LogicInterfaceService;
import com.topvision.ems.epon.oltdhcp.dao.OltDhcpBaseDao;
import com.topvision.ems.epon.oltdhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpBaseService;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpRefreshService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年11月21日-下午1:17:03
 *
 */
@Service("oltDhcpBaseService")
public class OltDhcpBaseServiceImpl extends BaseService implements OltDhcpBaseService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpBaseDao oltDhcpBaseDao;
    @Autowired
    private OltDhcpRefreshService oltDhcpRefreshService;
    @Autowired
    private LogicInterfaceService logicInterfaceService;
    
    public static final Integer DHCP_ENABLE = 1;
    public static final Integer PPPOE_ENABLE = 1;
    
    public static final Integer DHCP_PROT = 1;
    public static final Integer PPPOE_PROT = 2;

    @Override
    public TopOltDhcpGlobalObjects getOltDhcpBaseCfg(Long entityId) {
        return oltDhcpBaseDao.getOltDhcpBaseCfg(entityId);
    }

    @Override
    public void moidfyDhcpEnable(Long entityId, Integer status) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects globalObjects = new TopOltDhcpGlobalObjects();
        globalObjects.setTopOltDhcpEnable(status);
        getOltDhcpFacade(snmpParam).modifyOltDhcpGlobalObjects(snmpParam, globalObjects);
        // 开启DHCP开关的时候，需要重新刷新DHCP相关数据
        if (status.equals(DHCP_ENABLE)) {
            try {
                refreshDhcpData(entityId);
            } catch (Exception e) {
                logger.debug("refreshDhcpData error", e);
            }
        }
        oltDhcpBaseDao.updateOltDhcpEnable(entityId, status);
    }

    @Override
    public void refreshDhcpData(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);

        // 刷新全局数据，包括全局开关，防静态IP开关，Option82配置
        TopOltDhcpGlobalObjects globalObjects = getOltDhcpFacade(snmpParam).getTopOltDhcpGlobalObjects(snmpParam);
        if (globalObjects != null) {
            globalObjects.setEntityId(entityId);
            oltDhcpBaseDao.updateOltDhcpGlobalObjects(globalObjects);
        }

        // 刷新级联口/信任口配置信息
        List<TopOltDhcpPortAttribute> portAttributes = getOltDhcpFacade(snmpParam)
                .getTopOltDhcpPortAttributes(snmpParam);
        if (portAttributes != null) {
            oltDhcpBaseDao.updatePortAttributes(entityId, DHCP_PROT, portAttributes);
        }

        logicInterfaceService.refreshLogicInterface(entityId);
        logicInterfaceService.refreshLogicInterfaceIpConfig(entityId);
        // 刷新模式配置
        oltDhcpRefreshService.refreshTopOltDhcpVLANCfg(entityId);

        // 刷新RELAY规则配置
        oltDhcpRefreshService.refreshTopOltDhcpVifCfg(entityId);

        // 刷新服务器组
        oltDhcpRefreshService.refreshTopOltDhcpServerGroup(entityId);

        // 刷新CPE信息
        oltDhcpRefreshService.refreshTopOltDhcpCpeInfo(entityId);

        // 刷新报文统计信息
        oltDhcpRefreshService.refreshTopOltDhcpStatisticsObjects(entityId);

        // 刷新静态IP信息
        oltDhcpRefreshService.refreshTopOltDhcpStaticIp(entityId);

    }

    @Override
    public void modifyOption82Cfg(Long entityId, Integer status, Integer policy, String format) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects globalObjects = new TopOltDhcpGlobalObjects();
        globalObjects.setEntityId(entityId);
        globalObjects.setTopOltDhcpOpt82Enable(status);
        globalObjects.setTopOltDhcpOpt82Policy(policy);
        globalObjects.setTopOltDhcpOpt82Format(format);
        getOltDhcpFacade(snmpParam).modifyOltDhcpGlobalObjects(snmpParam, globalObjects);
        oltDhcpBaseDao.updateOltDhcpOption82Cfg(globalObjects);
    }

    @Override
    public void refrshOption82Cfg(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects globalObjects = getOltDhcpFacade(snmpParam).getTopOltDhcpGlobalObjects(snmpParam);
        if (globalObjects != null) {
            globalObjects.setEntityId(entityId);
            oltDhcpBaseDao.updateOltDhcpOption82Cfg(globalObjects);
        }
    }

    @Override
    public void modifyPppoeEnable(Long entityId, Integer status) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects globalObjects = new TopOltDhcpGlobalObjects();
        globalObjects.setTopOltPPPoEPlusEnable(status);
        getOltDhcpFacade(snmpParam).modifyOltDhcpGlobalObjects(snmpParam, globalObjects);
        // 开启PPPoE开关的时候，需要重新刷新PPPoE相关数据
        if (status.equals(PPPOE_ENABLE)) {
            try {
                refreshPppoeData(entityId);
            } catch (Exception e) {
                logger.debug("refreshPppoeData error", e);
            }
        }
        oltDhcpBaseDao.updateOltPppoeEnable(entityId, status);
    }

    @Override
    public void refreshPppoeData(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);

        // 刷新PPPoE全局配置
        TopOltDhcpGlobalObjects globalObjects = getOltDhcpFacade(snmpParam).getTopOltDhcpGlobalObjects(snmpParam);
        if (globalObjects != null) {
            globalObjects.setEntityId(entityId);
            oltDhcpBaseDao.updateOltPppoeGlobalObjects(globalObjects);
        }

        // 刷新级联口/信任口配置信息
        List<TopOltDhcpPortAttribute> portAttributes = getOltDhcpFacade(snmpParam)
                .getTopOltDhcpPortAttributes(snmpParam);
        if (portAttributes != null) {
            oltDhcpBaseDao.updatePortAttributes(entityId, PPPOE_PROT, portAttributes);
        }

        // 刷新报文统计信息
        oltDhcpRefreshService.refreshTopOltPppoeStatisticsObjects(entityId);

    }

    @Override
    public void modifyPppoeCfg(Long entityId, Integer policy, String format) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects globalObjects = new TopOltDhcpGlobalObjects();
        globalObjects.setEntityId(entityId);
        globalObjects.setTopOltPPPoEPlusPolicy(policy);
        globalObjects.setTopOltPPPoEPlusFormat(format);
        getOltDhcpFacade(snmpParam).modifyOltDhcpGlobalObjects(snmpParam, globalObjects);
        oltDhcpBaseDao.updateOltPppoeCfg(globalObjects);
    }

    @Override
    public void refreshPppoeCfg(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects globalObjects = getOltDhcpFacade(snmpParam).getTopOltDhcpGlobalObjects(snmpParam);
        if (globalObjects != null) {
            globalObjects.setEntityId(entityId);
            oltDhcpBaseDao.updateOltPppoeCfg(globalObjects);
        }
    }

    @Override
    public void modifyOltDhcpSourceVerifyEnable(Long entityId, Integer status) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects globalObjects = new TopOltDhcpGlobalObjects();
        globalObjects.setTopOltDhcpSourceVerifyEnable(status);
        getOltDhcpFacade(snmpParam).modifyOltDhcpGlobalObjects(snmpParam, globalObjects);
        oltDhcpBaseDao.updateOltDhcpSourceVerifyEnable(entityId, status);
    }

    private OltDhcpFacade getOltDhcpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OltDhcpFacade.class);
    }

}
