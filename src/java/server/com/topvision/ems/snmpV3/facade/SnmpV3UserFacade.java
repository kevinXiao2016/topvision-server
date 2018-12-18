/***********************************************************************
 * $Id: SnmpV3UserFacade.java,v1.0 2013-1-9 下午2:23:10 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.facade;

import java.util.List;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Access;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013-1-9-下午2:23:10
 * 
 */
@EngineFacade(serviceName = "snmpV3UserFacade", beanName = "snmpV3UserFacade")
public interface SnmpV3UserFacade extends Facade {
    void addSnmpV3User(SnmpParam snmpParam, UsmSnmpV3User snmpV3User);

    void addSnmpV3Group(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Group);

    void addSnmpV3Access(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Access);

    void addSnmpV3View(SnmpParam snmpParam, VacmSnmpV3View snmpV3View);

    void deleteSnmpV3User(SnmpParam snmpParam, UsmSnmpV3User snmpV3User);

    void deleteSnmpV3Group(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Group);

    void deleteSnmpV3Access(SnmpParam snmpParam, VacmSnmpV3Access snmpV3Access);

    void deleteSnmpV3View(SnmpParam snmpParam, VacmSnmpV3View snmpV3View);

    void setSnmpV3User(SnmpParam snmpParam, UsmSnmpV3User snmpV3User);

    String testUserValid(SnmpParam snmpParam, UsmSnmpV3User snmpV3User);

    String ModifySnmpVersion(SnmpParam snmpParam, Integer snmpVersion);

    List<UsmSnmpV3User> refreshSnmpV3UserList(SnmpParam snmpParam);

    List<VacmSnmpV3Group> refreshSnmpV3GroupList(SnmpParam snmpParam);

    List<VacmSnmpV3Access> refreshSnmpV3AccessList(SnmpParam snmpParam);

    List<VacmSnmpV3View> refreshSnmpV3ViewList(SnmpParam snmpParam);

    VacmSnmpV3View createView(SnmpParam snmpParam, VacmSnmpV3View view);

    String getSysObjectId(SnmpParam snmpParam);

    void setSnmpVersion(SnmpParam snmpParam, String version);

    VacmSnmpV3Access createAccess(SnmpParam snmpParam, VacmSnmpV3Access access);

    VacmSnmpV3View destoryView(SnmpParam snmpParam, VacmSnmpV3View view);

    VacmSnmpV3Access destoryAccess(SnmpParam snmpParam, VacmSnmpV3Access access);

    void destoryUser(SnmpParam snmpParam, UsmSnmpV3User user);

    UsmSnmpV3User cloneUser(SnmpParam snmpParam, UsmSnmpV3User user);

    UsmSnmpV3User activateUser(SnmpParam snmpParam, UsmSnmpV3User user);

    VacmSnmpV3Group createSnmpV3Group(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Group);

    String getAgentEngineId(SnmpParam snmpParam);

    void destoryGroup(SnmpParam snmpParam, VacmSnmpV3Group group);

    void setAccess(SnmpParam snmpParam, VacmSnmpV3Access access);

    void setView(SnmpParam snmpParam, VacmSnmpV3View view);

    void setSnmpV3Group(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Group);

    String getSnmpVersion(SnmpParam snmpParam);

    String getSnmpV3UserPulic(SnmpParam snmpParam, String oid);
}
