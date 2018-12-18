/***********************************************************************
 * $Id: GponUniFilterMacFacade.java,v1.0 2016年12月24日 下午12:12:27 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.unifiltermac.facade;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.gpon.profile.facade.domain.*;
import com.topvision.ems.gpon.unifiltermac.facade.domain.GponUniFilterMac;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

import java.util.List;

/**
 * @author jay
 * @created @2016年12月24日-下午12:12:27
 *
 */
@EngineFacade(serviceName = "GponUniFilterMacFacade", beanName = "gponUniFilterMacFacade")
public interface GponUniFilterMacFacade extends Facade {
    /**
     * 添加一条mac过滤信息
     * @param snmpParam
     * @param gponUniFilterMac
     */
    void addGponUniFilterMac(SnmpParam snmpParam, GponUniFilterMac gponUniFilterMac);

    /**
     * 删除一条mac过滤信息
     * @param snmpParam
     * @param gponUniFilterMac
     */
    void deleteGponUniFilterMac(SnmpParam snmpParam, GponUniFilterMac gponUniFilterMac);

    /**
     * 刷新一个uni口下的mac过滤表
     * @param snmpParam
     * @param gponUniFilterMac
     * @return
     */
    List<GponUniFilterMac> refreshGponUniFilterMacForUni(SnmpParam snmpParam, GponUniFilterMac gponUniFilterMac);

    /**
     * 刷新一个olt下的所有mac过来表
     * @param snmpParam
     * @return
     */
    List<GponUniFilterMac> refreshGponUniFilterMacForEntity(SnmpParam snmpParam);
}
