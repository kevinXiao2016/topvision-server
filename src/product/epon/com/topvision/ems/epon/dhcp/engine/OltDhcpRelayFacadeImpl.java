/***********************************************************************
 * $Id: DhcpRelayFacadeImpl.java,v1.0 2013-6-22 上午11:34:04 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.dhcp.domain.DhcpBundle;
import com.topvision.ems.epon.dhcp.domain.DhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpIntIpConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpOption60;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtDevice;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtServerConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayVlanMap;
import com.topvision.ems.epon.dhcp.domain.DhcpServerConfig;
import com.topvision.ems.epon.dhcp.facade.OltDhcpRelayFacade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author dosion
 * @created @2013-6-22-上午11:34:04
 * 
 */
public class OltDhcpRelayFacadeImpl extends EmsFacade implements OltDhcpRelayFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public DhcpBundle modifyDhcpRelayBundleInfo(SnmpParam snmpParam, DhcpBundle cmcDhcpBundle) {
        DhcpBundle afterModified = null;
        afterModified = snmpExecutorService.setData(snmpParam, cmcDhcpBundle);
        return afterModified;
    }

    @Override
    public DhcpServerConfig modifyDhcpRelayServerInfo(SnmpParam snmpParam, DhcpServerConfig cmcDhcpServerConfig) {
        return snmpExecutorService.setData(snmpParam, cmcDhcpServerConfig);
    }

    @Override
    public DhcpGiaddrConfig modifyDhcpRelayGiAddrInfo(SnmpParam snmpParam, DhcpGiaddrConfig cmcDhcpGiAddr) {
        return snmpExecutorService.setData(snmpParam, cmcDhcpGiAddr);
    }

    @Override
    public DhcpOption60 modifyDhcpRelayOption60Info(SnmpParam dolSnmpParam, DhcpOption60 cmcDhcpOption60) {
        DhcpOption60 afterModified;
        afterModified = snmpExecutorService.setData(dolSnmpParam, cmcDhcpOption60);
        return afterModified;
    }

    @Override
    public List<DhcpBundle> getCmcDhcpRelayBundleInfo(SnmpParam snmpParam) {
        List<DhcpBundle> cmcDhcpBundles = snmpExecutorService.getTable(snmpParam, DhcpBundle.class);
        logger.debug("\n\nRefresh CmcDhcpBundle:{}", cmcDhcpBundles);
        return cmcDhcpBundles;
    }

    @Override
    public List<DhcpServerConfig> getDhcpRelayServerConfigInfo(SnmpParam snmpParam) {
        List<DhcpServerConfig> cmcDhcpServerConfigs = snmpExecutorService.getTable(snmpParam, DhcpServerConfig.class);
        logger.debug("\n\nRefresh CmcDhcpServerConfig:{}", cmcDhcpServerConfigs);
        return cmcDhcpServerConfigs;
    }

    @Override
    public List<DhcpGiaddrConfig> getDhcpRelayGiAddrInfo(SnmpParam snmpParam) {
        List<DhcpGiaddrConfig> cmcDhcpGiAddrs = snmpExecutorService.getTable(snmpParam, DhcpGiaddrConfig.class);
        logger.debug("\n\nRefresh CmcDhcpGiAddr:{}", cmcDhcpGiAddrs);
        return cmcDhcpGiAddrs;
    }

    @Override
    public List<DhcpOption60> getDhcpRelayOption60Info(SnmpParam snmpParam) {
        List<DhcpOption60> cmcDhcpOption60s = snmpExecutorService.getTable(snmpParam, DhcpOption60.class);
        logger.debug("\n\nRefresh CmcDhcpOption60:{}", cmcDhcpOption60s);
        return cmcDhcpOption60s;
    }

    @Override
    public List<DhcpIntIpConfig> getDhcpRelayIntIp(SnmpParam snmpParam) {
        List<DhcpIntIpConfig> intIps = snmpExecutorService.getTable(snmpParam, DhcpIntIpConfig.class);
        logger.debug("\n\nRefresh CmcDhcpIntIp:{}", intIps);
        return intIps;
    }

    @Override
    public DhcpIntIpConfig modifyDhcpRelayIntIp(SnmpParam snmpParam, DhcpIntIpConfig virtualIp) {
        return snmpExecutorService.setData(snmpParam, virtualIp);
    }

    @Override
    public Integer modifyDhcpRelaySwitch(SnmpParam snmpParam, Integer dhcpRelaySwitch) {
        return Integer.parseInt(snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.3.1.5.0", dhcpRelaySwitch.toString()));
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public DhcpRelayExtDevice modifyDhcpRelayExtDeviceOption(SnmpParam snmpParam, DhcpRelayExtDevice dhcpRelayExtDevice) {
        return snmpExecutorService.setData(snmpParam, dhcpRelayExtDevice);
    }

    @Override
    public DhcpRelayExtGiaddrConfig modifyDhcpRelayExtGiaddr(SnmpParam snmpParam,
            DhcpRelayExtGiaddrConfig dhcpRelayExtGiaddr) {
        return snmpExecutorService.setData(snmpParam, dhcpRelayExtGiaddr);
    }

    @Override
    public DhcpRelayExtServerConfig modifyDhcpRelayExtServer(SnmpParam snmpParam,
            DhcpRelayExtServerConfig dhcpRelayExtServer) {
        return snmpExecutorService.setData(snmpParam, dhcpRelayExtServer);
    }

    @Override
    public DhcpRelayVlanMap modifyDhcpRelayVlanMap(SnmpParam snmpParam, DhcpRelayVlanMap dhcpRelayVlanMap) {
        return snmpExecutorService.setData(snmpParam, dhcpRelayVlanMap);
    }

    @Override
    public List<DhcpRelayVlanMap> getDhcpRelayVlanMaps(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DhcpRelayVlanMap.class);
    }

    @Override
    public List<DhcpRelayExtDevice> getDhcpRelayExtDeviceOptions(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DhcpRelayExtDevice.class);
    }

    @Override
    public List<DhcpRelayExtGiaddrConfig> getDhcpRelayExtGiaddrs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DhcpRelayExtGiaddrConfig.class);
    }

    @Override
    public List<DhcpRelayExtServerConfig> getDhcpRelayExtServers(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DhcpRelayExtServerConfig.class);
    }

    @Override
    public Integer getDhcpRelaySwitch(SnmpParam snmpParam) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.3.1.5.0"));
    }
    
}
