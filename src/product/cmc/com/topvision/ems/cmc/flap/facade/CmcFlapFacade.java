/***********************************************************************
 * $Id: CmcFacade.java,v1.0 2011-7-1 下午02:43:48 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.flap.facade;

import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-7-1-下午02:43:48
 * 
 */
@EngineFacade(serviceName = "CmcFlapFacade", beanName = "cmcFlapFacade")
public interface CmcFlapFacade extends Facade {

	/**
	 * 获取Flap时间
	 * 
	 * @param snmpParam
	 * @return
	 */
	String getTopCmFlapInterval(SnmpParam snmpParam);

	/**
	 * 设置Flap时间
	 * 
	 * @param snmpParam
	 * @param topCmFlapInterval
	 */
	void setTopCmFlapInterval(SnmpParam snmpParam, Integer topCmFlapInterval);
	
	void resetFlapCounters(SnmpParam snmpParam,Long cmcIndex);

}
