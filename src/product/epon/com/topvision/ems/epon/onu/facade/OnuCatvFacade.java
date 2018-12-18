/***********************************************************************
 * $Id: OnuCatvConfigFacade.java,v1.0 2016-4-27 上午10:15:28 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.facade;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OnuCatvConfig;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2016-4-27-上午10:15:28
 *
 */
@EngineFacade(serviceName = "OnuCatvConfigFacade", beanName = "onuCatvConfigFacade")
public interface OnuCatvFacade {

    /**
     * 修改onu catv 配置信息
     * @param snmpParam
     * @param onuCatvConfig
     */
    void modifyOnuCatvConfig(SnmpParam snmpParam, OnuCatvConfig onuCatvConfig);

    /**
     * 获取onu catv配置信息
     * @param snmpParam
     * @param onuCatvConfig
     * @return
     */
    OnuCatvConfig getOnuCatvConfig(SnmpParam snmpParam, OnuCatvConfig onuCatvConfig);

    /**
     * 获取OLT下所有catv 配置信息
     * @param snmpParam
     * @return
     */
    List<OnuCatvConfig> getOnuCatvConfigAll(SnmpParam snmpParam);

}
