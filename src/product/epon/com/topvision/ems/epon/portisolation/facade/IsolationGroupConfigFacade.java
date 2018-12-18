/***********************************************************************
 * $Id: IsolationGroupConfigFacade.java,v1.0 2014-12-18 上午11:41:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.facade;

import java.util.List;

import com.topvision.ems.epon.portisolation.domain.PortIsolationGroup;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGrpMember;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2014-12-18-上午11:41:05
 *
 */
@EngineFacade(serviceName = "IsolationGroupConfigFacade", beanName = "isolationGroupConfigFacade")
public interface IsolationGroupConfigFacade extends Facade {

    /**
     * 从设备获取隔离组
     * @param snmpParam
     * @return
     */
    List<PortIsolationGroup> getIsolationGroup(SnmpParam snmpParam);

    /**
     * 添加隔离组
     * @param snmpParam
     * @param group
     */
    PortIsolationGroup addIsolationGroup(SnmpParam snmpParam, PortIsolationGroup group);

    /**
     * 修改隔离组
     * @param snmpParam
     * @param group
     */
    PortIsolationGroup modifyIsolationGroup(SnmpParam snmpParam, PortIsolationGroup group);

    /**
     * 删除隔离组
     * @param snmpParam
     * @param group
     */
    void deleteIsolationGroup(SnmpParam snmpParam, PortIsolationGroup group);

    /**
     * 获取隔离组成员
     * @param snmpParam
     * @return
     */
    List<PortIsolationGrpMember> getGroupMember(SnmpParam snmpParam);

    /**
     * 添加隔离组成员
     * @param snmpParam
     * @param member
     */
    PortIsolationGrpMember addGroupMember(SnmpParam snmpParam, PortIsolationGrpMember member);

    /**
     * 删除隔离组成员
     * @param snmpParam
     * @param member
     */
    void deleteGroupMember(SnmpParam snmpParam, PortIsolationGrpMember member);

}
