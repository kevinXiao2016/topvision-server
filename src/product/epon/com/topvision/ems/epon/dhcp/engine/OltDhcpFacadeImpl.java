/***********************************************************************
 * $Id: OltDhcpFacadeImpl.java,v1.0 2013-10-28 下午04:13:20 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.dhcp.domain.OltDhcpBaseConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacDynamic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacStatic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpServerConfig;
import com.topvision.ems.epon.dhcp.facade.OltDhcpFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2013-10-28-下午04:13:20
 *
 */
public class OltDhcpFacadeImpl extends EmsFacade implements OltDhcpFacade {
    private SnmpExecutorService snmpExecutorService;

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#getDhcpBaseConfig(com.topvision.framework.snmp
     * .SnmpParam)
     */
    @Override
    public OltDhcpBaseConfig getDhcpBaseConfig(SnmpParam snmpParam) {
        // MIB默认Index为1 domain中对每个属性的OID加上Index
        return snmpExecutorService.getData(snmpParam, OltDhcpBaseConfig.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#getDhcpServerConfigs(com.topvision.framework.
     * snmp.SnmpParam)
     */
    @Override
    public List<OltDhcpServerConfig> getDhcpServerConfigs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltDhcpServerConfig.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#getDhcpGiaddrConfigs(com.topvision.framework.
     * snmp.SnmpParam)
     */
    @Override
    public List<OltDhcpGiaddrConfig> getDhcpGiaddrConfigs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltDhcpGiaddrConfig.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#getDhcpIpMacStatics(com.topvision.framework.snmp
     * .SnmpParam)
     */
    @Override
    public List<OltDhcpIpMacStatic> getDhcpIpMacStatics(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltDhcpIpMacStatic.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#modifyDhcpBaseConfig(com.topvision.framework.
     * snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpBaseConfig)
     */
    @Override
    public OltDhcpBaseConfig modifyDhcpBaseConfig(SnmpParam snmpParam, OltDhcpBaseConfig baseConfig) {
        return snmpExecutorService.setData(snmpParam, baseConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#addDhcpServerConfig(com.topvision.framework.snmp
     * .SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpServerConfig)
     */
    @Override
    public OltDhcpServerConfig addDhcpServerConfig(SnmpParam snmpParam, OltDhcpServerConfig serverConfig) {
        serverConfig.setTopOltDHCPServRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, serverConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#addDhcpGiaddrConfig(com.topvision.framework.snmp
     * .SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpGiaddrConfig)
     */
    @Override
    public OltDhcpGiaddrConfig addDhcpGiaddrConfig(SnmpParam snmpParam, OltDhcpGiaddrConfig giaddrConfig) {
        giaddrConfig.setTopOltDHCPGiaddrRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, giaddrConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#addDhcpIpMacStatic(com.topvision.framework.snmp
     * .SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpIpMacStatic)
     */
    @Override
    public OltDhcpIpMacStatic addDhcpIpMacStatic(SnmpParam snmpParam, OltDhcpIpMacStatic ipMacStaticConfig) {
        ipMacStaticConfig.setTopOltDHCPIpMacRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, ipMacStaticConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#deleteDhcpServerConfig(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpServerConfig)
     */
    @Override
    public void deleteDhcpServerConfig(SnmpParam snmpParam, OltDhcpServerConfig serverConfig) {
        serverConfig.setTopOltDHCPServRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, serverConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#deleteDhcpGiaddrConfig(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpGiaddrConfig)
     */
    @Override
    public void deleteDhcpGiaddrConfig(SnmpParam snmpParam, OltDhcpGiaddrConfig giaddrConfig) {
        giaddrConfig.setTopOltDHCPGiaddrRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, giaddrConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#deleteDhcpIpMacStatic(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpIpMacStatic)
     */
    @Override
    public void deleteDhcpIpMacStatic(SnmpParam snmpParam, OltDhcpIpMacStatic ipMacStaticConfig) {
        ipMacStaticConfig.setTopOltDHCPIpMacRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, ipMacStaticConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltDhcpFacade#getDhcpIpMacDynamics(com.topvision.framework.
     * snmp.SnmpParam)
     */
    @Override
    public List<OltDhcpIpMacDynamic> getDhcpIpMacDynamics(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltDhcpIpMacDynamic.class);
    }

}
