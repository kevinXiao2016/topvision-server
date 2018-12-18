/***********************************************************************
 * $Id: OltRstpFacade.java,v1.0 2013-10-25 下午5:35:58 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.facade;

import java.util.List;

import com.topvision.ems.epon.rstp.domain.OltStpGlobalConfig;
import com.topvision.ems.epon.rstp.domain.OltStpPortConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午5:35:58
 *
 */
@EngineFacade(serviceName = "OltRstpFacade", beanName = "oltRstpFacade")
public interface OltRstpFacade extends Facade {
    /**
     * 返回设备STP的全局配置
     * 
     * @param snmpParam
     * @return OltStpGlobalConfig
     */
    OltStpGlobalConfig getOltStpGlobalConfig(SnmpParam snmpParam);

    /**
     * 返回端口STP的配置信息
     * 
     * @param snmpParam
     * @return list
     */
    List<OltStpPortConfig> getOltStpPortConfigs(SnmpParam snmpParam);

    /**
     * 更新STP全局使能
     * 
     * @param snmpParam
     * @param globalConfig
     */
    void updateOltStpGlobalEnable(SnmpParam snmpParam, OltStpGlobalConfig globalConfig);

    /**
     * 更新STP全局配置
     * 
     * @param snmpParam
     * @param globalConfig
     */
    void updateOltStpGlobalConfig(SnmpParam snmpParam, OltStpGlobalConfig globalConfig);

    /**
     * 更新STP端口配置
     * 
     * @param snmpParam
     * @param portConfig
     */
    void updateOltStpPortConfig(SnmpParam snmpParam, OltStpPortConfig portConfig);

}
