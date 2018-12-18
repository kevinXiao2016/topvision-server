/***********************************************************************
 * $Id: Cmc_bRouteFacadeImpl.java,v1.0 2013-8-7 上午10:46:18 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.route.engine;

import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.route.facade.CmcRouteFacade;
import com.topvision.ems.cmc.route.facade.domain.CmcRoute;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author dosion
 * @created @2013-8-7-上午10:46:18
 * 
 */
@Facade("cmcRouteFacade")
public class CmcRouteFacadeImpl implements CmcRouteFacade {
	@Resource(name = "snmpExecutorService")
	private SnmpExecutorService snmpExecutorService;

	@Override
	public List<CmcRoute> getRouteList(SnmpParam snmpParam) {
		return snmpExecutorService.getTable(snmpParam, CmcRoute.class);
	}

	@Override
	public CmcRoute modifyRouteConfig(SnmpParam snmpParam, CmcRoute route) {
		return snmpExecutorService.setData(snmpParam, route);
	}

	public SnmpExecutorService getSnmpExecutorService() {
		return snmpExecutorService;
	}

	public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
		this.snmpExecutorService = snmpExecutorService;
	}

}
