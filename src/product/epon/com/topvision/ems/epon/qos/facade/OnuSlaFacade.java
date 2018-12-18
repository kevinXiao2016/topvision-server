/***********************************************************************
 * $Id: OnuSlaFacade.java,v1.0 2013年10月25日 下午5:54:09 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.facade;

import java.util.List;

import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:54:09
 *
 */
@EngineFacade(serviceName = "OnuSlaFacade", beanName = "onuSlaFacade")
public interface OnuSlaFacade extends Facade {

    /**
     * 获取olt下所有onu的sla
     * @param snmpParam
     * @return
     */
    List<SlaTable> getOnuSla(SnmpParam snmpParam);

    /**
     * 获取单个onu的sla
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    SlaTable getOnuSla(SnmpParam snmpParam, Long onuIndex);

}
