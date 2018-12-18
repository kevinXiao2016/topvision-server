/***********************************************************************
 * $Id: DhcpFacadeImpl.java,v1.0 2017年11月17日 上午11:21:53 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
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
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2017年11月17日-上午11:21:53
 *
 */
@Facade("dhcpFacade")
public class OltDhcpFacadeImpl extends EmsFacade implements OltDhcpFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<TopOltDhcpCpeInfo> getTopOltDhcpCpeInfos(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltDhcpCpeInfo.class);
    }

    @Override
    public TopOltDhcpGlobalObjects getTopOltDhcpGlobalObjects(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, TopOltDhcpGlobalObjects.class);
    }

    @Override
    public List<TopOltDhcpPortAttribute> getTopOltDhcpPortAttributes(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltDhcpPortAttribute.class);
    }

    @Override
    public List<TopOltDhcpServerGroup> getTopOltDhcpServerGroups(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltDhcpServerGroup.class);
    }

    @Override
    public List<TopOltDhcpStaticIp> getTopOltDhcpStaticIps(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltDhcpStaticIp.class);
    }

    @Override
    public List<TopOltDhcpVifCfg> getTopOltDhcpVifCfgs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltDhcpVifCfg.class);
    }

    @Override
    public List<TopOltDhcpVLANCfg> getTopOltDhcpVLANCfgs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltDhcpVLANCfg.class);
    }

    @Override
    public TopOltPppoeStatisticsObjects getTopOltPppoeStatisticsObjects(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, TopOltPppoeStatisticsObjects.class);
    }

    @Override
    public TopOltDhcpStatisticsObjects getTopOltDhcpStatisticsObjects(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, TopOltDhcpStatisticsObjects.class);
    }

    @Override
    public void modifyOltDhcpGlobalObjects(SnmpParam snmpParam, TopOltDhcpGlobalObjects globalObjects) {
        snmpExecutorService.setData(snmpParam, globalObjects);
    }

    @Override
    public void setOltDhcpServerGroup(SnmpParam snmpParam, TopOltDhcpServerGroup group) {
        snmpExecutorService.setData(snmpParam, group);
    }

    @Override
    public void modifyPortAttribute(SnmpParam snmpParam, TopOltDhcpPortAttribute port) {
        snmpExecutorService.setData(snmpParam, port);
    }

    @Override
    public void setOltDhcpStaticIp(SnmpParam snmpParam, TopOltDhcpStaticIp staticIp) {
        snmpExecutorService.setData(snmpParam, staticIp);
    }

    @Override
    public void clearOltDhcpStatistics(SnmpParam snmpParam) {
        TopOltDhcpStatisticsObjects objects = new TopOltDhcpStatisticsObjects();
        objects.setTopOltDhcpStatStatusAndAction(2);
        snmpExecutorService.setData(snmpParam, objects);
    }

    @Override
    public void clearOltPppoeStatistics(SnmpParam snmpParam) {
        TopOltPppoeStatisticsObjects objects = new TopOltPppoeStatisticsObjects();
        objects.setTopOltPppoeStatStatusAndAction(2);
        snmpExecutorService.setData(snmpParam, objects);
    }

    @Override
    public void setOltDhcpVifCfg(SnmpParam snmpParam, TopOltDhcpVifCfg vif) {
        snmpExecutorService.setData(snmpParam, vif);
    }

    @Override
    public void setOltDhcpVLANCfg(SnmpParam snmpParam, TopOltDhcpVLANCfg vlanCfg) {
        snmpExecutorService.setData(snmpParam, vlanCfg);
    }

}
