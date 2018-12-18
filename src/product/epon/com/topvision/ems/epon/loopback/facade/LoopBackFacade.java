/***********************************************************************
 * $Id: LoopBackConfigFacade.java,v1.0 2013-11-16 上午11:53:00 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.loopback.facade;

import java.util.List;

import com.topvision.ems.epon.loopback.domain.LoopbackConfigTable;
import com.topvision.ems.epon.loopback.domain.LoopbackSubIpTable;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-11-16-上午11:53:00
 *
 */
@EngineFacade(serviceName = "LoopBackFacade", beanName = "loopBackFacade")
public interface LoopBackFacade extends Facade {

    /**
     * 刷新LoopbackConfigTable信息
     * 
     * @param snmpParam
     * @return
     */
    List<LoopbackConfigTable> getLoopbackConfigTables(SnmpParam snmpParam);
    
    /**
     * 刷新LoopbackSubIpTable信息
     * 
     * @param snmpParam
     * @return
     */
    List<LoopbackSubIpTable> getLoopbackSubIpTables(SnmpParam snmpParam);

    /**
     * 添加环回接口
     * @param loopBack
     */
    LoopbackConfigTable addLoopBackInterface(SnmpParam snmpParam, LoopbackConfigTable loopBack);

    /**
     * 删除环回接口
     * @param snmpParam
     * @param loopBack
     */
    void deleteLoopBackInterface(SnmpParam snmpParam, LoopbackConfigTable loopBack);

    /**
     * 修改环回接口
     * @param snmpParam
     * @param loopBack
     * @return
     */
    LoopbackConfigTable modifyLoopBackInterface(SnmpParam snmpParam, LoopbackConfigTable loopBack);

    /**
     * 添加环回接口子IP
     * @param snmpParam
     * @param subIpTable
     * @return
     */
    LoopbackSubIpTable addLoopBackSubIp(SnmpParam snmpParam, LoopbackSubIpTable subIpTable);

    /**
     * 删除环回接口子IP
     * @param snmpParam
     * @param subIpTable
     */
    void deleteLoopBackSubIp(SnmpParam snmpParam, LoopbackSubIpTable subIpTable);

    /**
     * 修改环回接口子IP
     * @param snmpParam
     * @param subIpTable
     * @return
     */
    LoopbackSubIpTable modifyLoopBackSubIp(SnmpParam snmpParam, LoopbackSubIpTable subIpTable);

}
