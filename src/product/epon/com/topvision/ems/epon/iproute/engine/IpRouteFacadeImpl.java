/***********************************************************************
 * $Id: IpRouteFacadeImpl.java,v1.0 2013-11-16 下午03:41:44 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.iproute.domain.IpRouteRecord;
import com.topvision.ems.epon.iproute.domain.StaticIpRouteConfig;
import com.topvision.ems.epon.iproute.facade.IpRouteFacade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;

/**
 * @author Rod John
 * @created @2013-11-16-下午03:41:44
 * 
 */
public class IpRouteFacadeImpl extends EmsFacade implements IpRouteFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<IpRouteRecord> getIpRouteRecords(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IpRouteRecord.class);
    }

    @Override
    public List<StaticIpRouteConfig> getStaticIpRoute(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, StaticIpRouteConfig.class);
    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public StaticIpRouteConfig addIpRouteConfig(SnmpParam snmpParam, StaticIpRouteConfig ipRouteConfig) {
        ipRouteConfig.setStaticRouteRowStatus(EponConstants.TB_CREATEANDGO);
        return snmpExecutorService.setData(snmpParam, ipRouteConfig);
    }

    @Override
    public void deleteIpRouteConfig(SnmpParam snmpParam, StaticIpRouteConfig ipRouteConfig) {
        ipRouteConfig.setStaticRouteRowStatus(EponConstants.TB_DROP);
        snmpExecutorService.setData(snmpParam, ipRouteConfig);
    }

    @Override
    public StaticIpRouteConfig updateIpRouteConfig(SnmpParam snmpParam, StaticIpRouteConfig ipRouteConfig) {
        return snmpExecutorService.setData(snmpParam, ipRouteConfig);
    }
}
