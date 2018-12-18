/***********************************************************************
 * $Id: TrapServerConfigFacade.java,v1.0 2013-4-25 上午11:56:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.trapserver.facade;

import java.util.List;

import com.topvision.ems.cmc.trapserver.facade.domain.CmcTrapServer;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-4-25-上午11:56:54
 *
 */
@EngineFacade(serviceName = "TrapServerConfigFacade", beanName = "trapServerConfigFacade")
public interface TrapServerConfigFacade extends Facade {

    /**
     * 向设备添加TrapServer配置
     * @param snmpParam
     * @param trapServer
     * @return TrapServer
     */
    public CmcTrapServer addTrapServerToFacility(SnmpParam snmpParam, CmcTrapServer trapServer);

    /**
     * 从设备上删除TrapServer配置
     * @param snmpParam
     * @param trapServer
     */
    public void deleteTrapServerFromFacility(SnmpParam snmpParam, CmcTrapServer trapServer);

    /**
     * 获取设备上配置的TrapServer
     * @param snmpParam
     * @return
     */
    public List<CmcTrapServer> getTrapServerFromFacility(SnmpParam snmpParam);

}
