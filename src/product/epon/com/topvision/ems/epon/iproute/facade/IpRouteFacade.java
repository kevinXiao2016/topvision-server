/***********************************************************************
 * $Id: IpRouteFacade.java,v1.0 2013-11-16 下午03:41:18 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.facade;

import java.util.List;

import com.topvision.ems.epon.iproute.domain.IpRouteRecord;
import com.topvision.ems.epon.iproute.domain.StaticIpRouteConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2013-11-16-下午03:41:18
 * 
 */
@EngineFacade(serviceName = "IpRouteFacade", beanName = "ipRouteFacade")
public interface IpRouteFacade extends Facade {
    
    /**
     * 刷新IpRouteRecord
     * 
     * @param snmpParam
     * @return
     */
    List<IpRouteRecord> getIpRouteRecords(SnmpParam snmpParam);

    /**
     * 刷新静态IpRoute
     * @param snmpParam
     * @return
     */
    List<StaticIpRouteConfig> getStaticIpRoute(SnmpParam snmpParam);

    /**
     * 添加静态路由记录
     * @param snmpParam
     * @param ipRouteConfig
     * @return
     */
    StaticIpRouteConfig addIpRouteConfig(SnmpParam snmpParam, StaticIpRouteConfig ipRouteConfig);

    /**
     * 删除静态路由记录
     * @param snmpParam
     * @param ipRouteConfig
     */
    void deleteIpRouteConfig(SnmpParam snmpParam, StaticIpRouteConfig ipRouteConfig);

    /**
     * 修改静态路由记录
     * @param snmpParam
     * @param ipRouteConfig
     */
    StaticIpRouteConfig updateIpRouteConfig(SnmpParam snmpParam, StaticIpRouteConfig ipRouteConfig);
}
