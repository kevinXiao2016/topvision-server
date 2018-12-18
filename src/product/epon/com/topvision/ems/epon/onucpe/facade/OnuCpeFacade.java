/***********************************************************************
 * $Id: OnuCpeFacade.java,v1.0 2016年7月6日 下午3:04:26 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.facade;

import com.topvision.ems.epon.cpelocation.domain.OnuCpeIpLocation;
import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.ems.epon.performance.domain.GponOnuUniCpeList;
import com.topvision.ems.epon.performance.domain.OnuUniCpeList;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2016年7月6日-下午3:04:26
 *
 */
@EngineFacade(serviceName = "OnuCpeFacade", beanName = "onuCpeFacade")
public interface OnuCpeFacade {

    /**
     * @param snmpParam
     * @param uniIndex
     * @return
     */
    OnuUniCpeList refreshEponOnuUniCpe(SnmpParam snmpParam, Long uniIndex);

    /**
     * @param snmpParam
     * @param uniIndex
     * @return
     */
    GponOnuUniCpeList refreshGponOnuUniCpe(SnmpParam snmpParam, Long uniIndex);

    /**
     * 获取topDhcpIpInfoTotal数据,供解析后分析查询
     * @param snmpParam
     * @param cpeLocation
     * @return
     */
    String fetchOnuCpeLocationList(SnmpParam snmpParam, OnuCpeLocation cpeLocation);

    /**
     * 根据IP快查ONU CPE
     * @param snmpParam
     * @param ip
     * @return
     */
    OnuCpeIpLocation fetchOnuCpeLocationByIP(SnmpParam snmpParam, String ip);

    /**
     * 获取topDhcpIpInfoTotal数据,供解析后分析查询
     * @param snmpParam
     * @param string
     * @return
     */
    String fetchOnuCpeLocationList(SnmpParam snmpParam, String string);
}
