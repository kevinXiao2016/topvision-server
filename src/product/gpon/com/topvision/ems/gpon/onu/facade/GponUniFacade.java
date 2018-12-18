/***********************************************************************
 * $Id: GponUniFacade.java,v1.0 2016年12月25日 上午10:40:31 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.facade;

import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2016年12月25日-上午10:40:31
 *
 */
@EngineFacade(serviceName = "GponUniFacade", beanName = "gponUniFacade")
public interface GponUniFacade extends Facade {

    void setUni15minEnable(SnmpParam snmpParam, Long uniIndex, Integer uni15minEnable);

    void setUniAdminStatus(SnmpParam snmpParam, Long uniIndex, Integer uniAdminStatus);

    void setUniVlanConfig(SnmpParam snmpParam, Long onuIndex, Integer gponOnuUniPri, Integer gponOnuUniPvid);

}
