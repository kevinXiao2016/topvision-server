/***********************************************************************
 * $Id: OltConfigInfoFacade.java,v1.0 2013-10-26 上午10:06:50 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.facade;

import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-26-上午10:06:50
 *
 */
@EngineFacade(serviceName = "OltConfigFileFacade", beanName = "oltConfigFileFacade")
public interface OltConfigFileFacade extends Facade {

    /**
     * OLT保存配置
     * 
     * @param snmpParam
     *            网管SNMP参数
     */
    void saveOltConfig(SnmpParam snmpParam);

    /**
     * 获取Olt配置保存状态
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return Integer
     */
    Integer getOltSaveStatus(SnmpParam snmpParam);
}
