/***********************************************************************
 * $Id: CmtsDiscoveryFacade.java,v1.0 2013-7-20 下午02:28:00 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.facade;

import com.topvision.ems.cmts.domain.CmtsBaseInfo;
import com.topvision.ems.cmts.topology.domain.CmtsDiscoveryData;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2013-7-20-下午02:28:00
 * 
 */
@EngineFacade(serviceName = "CmtsDiscoveryFacade", beanName = "cmtsDiscoveryFacade")
public interface CmtsDiscoveryFacade extends Facade {

    /**
     * cmts 拓扑发现
     * 
     * @param param
     *            snmp参数，必须包含IP和SNMP相关参数信息
     * @return 采集的设备信息
     */
    CmtsDiscoveryData discovery(SnmpParam param, CmtsDiscoveryData cmtsDiscoveryData);

    /**
     * cmts 自动刷新
     * 
     * @param param
     * @param cmtsDiscoveryData
     * @return
     */
    CmtsDiscoveryData autoDiscovery(SnmpParam param, CmtsDiscoveryData cmtsDiscoveryData);

    /**
     * cmts拓扑发现
     * 
     * @param param
     *            snmp参数，必须包含IP和SNMP相关参数信息
     * @return 采集的设备信息
     */
    CmtsDiscoveryData discoveryBsr2000(SnmpParam param, CmtsDiscoveryData cmtsDiscoveryData);

    /**
     * 获取CMTS基本信息
     * @param param
     * @return
     */
    CmtsBaseInfo getCmtsBaseInfo(SnmpParam param);
}
