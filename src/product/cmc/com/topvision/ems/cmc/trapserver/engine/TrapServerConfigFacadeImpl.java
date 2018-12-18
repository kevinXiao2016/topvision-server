/***********************************************************************
 * $Id: TrapServerConfigFacadeImpl.java,v1.0 2013-4-25 下午1:38:36 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.trapserver.engine;

import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.trapserver.facade.TrapServerConfigFacade;
import com.topvision.ems.cmc.trapserver.facade.domain.CmcTrapServer;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-4-25-下午1:38:36
 * 
 */
@Facade("trapServerConfigFacade")
public class TrapServerConfigFacadeImpl extends EmsFacade implements
		TrapServerConfigFacade {
	@Resource(name = "snmpExecutorService")
	private SnmpExecutorService snmpExecutorService;

	@Override
	public CmcTrapServer addTrapServerToFacility(SnmpParam snmpParam,
			CmcTrapServer trapServer) {
		return snmpExecutorService.setData(snmpParam, trapServer);
	}

	@Override
	public void deleteTrapServerFromFacility(SnmpParam snmpParam,
			CmcTrapServer trapServer) {
		snmpExecutorService.setData(snmpParam, trapServer);
	}

	@Override
	public List<CmcTrapServer> getTrapServerFromFacility(SnmpParam snmpParam) {
		return snmpExecutorService.getTable(snmpParam, CmcTrapServer.class);
	}

	public SnmpExecutorService getSnmpExecutorService() {
		return snmpExecutorService;
	}

	public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
		this.snmpExecutorService = snmpExecutorService;
	}

}
