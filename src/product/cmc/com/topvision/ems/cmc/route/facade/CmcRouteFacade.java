/***********************************************************************
 * $Id: Cmc_bRouteFacade.java,v1.0 2013-8-7 上午10:44:02 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.route.facade;

import java.util.List;

import com.topvision.ems.cmc.route.facade.domain.CmcRoute;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author dosion
 * @created @2013-8-7-上午10:44:02
 *
 */
@EngineFacade(serviceName = "CmcRouteFacade", beanName = "cmcRouteFacade")
public interface CmcRouteFacade extends Facade{

    /**
     * 获取静态路由配置
     * @param snmpParam
     * @return
     */
    public List<CmcRoute> getRouteList(SnmpParam snmpParam);
    
    /**
     * 修改静态路由配置
     * @param snmpParam
     * @param route
     */
    public CmcRoute modifyRouteConfig(SnmpParam snmpParam, CmcRoute route);
}
