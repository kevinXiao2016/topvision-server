/***********************************************************************
 * $Id: IsolationGroupConfigFacadeImpl.java,v1.0 2014-12-18 上午11:42:18 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGroup;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGrpMember;
import com.topvision.ems.epon.portisolation.facade.IsolationGroupConfigFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2014-12-18-上午11:42:18
 *
 */
public class IsolationGroupConfigFacadeImpl extends EmsFacade implements IsolationGroupConfigFacade {
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public List<PortIsolationGroup> getIsolationGroup(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, PortIsolationGroup.class);
    }

    @Override
    public PortIsolationGroup addIsolationGroup(SnmpParam snmpParam, PortIsolationGroup group) {
        group.setGroupRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, group);
    }

    @Override
    public PortIsolationGroup modifyIsolationGroup(SnmpParam snmpParam, PortIsolationGroup group) {
        return snmpExecutorService.setData(snmpParam, group);
    }

    @Override
    public void deleteIsolationGroup(SnmpParam snmpParam, PortIsolationGroup group) {
        group.setGroupRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, group);
    }

    @Override
    public List<PortIsolationGrpMember> getGroupMember(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, PortIsolationGrpMember.class);
    }

    @Override
    public PortIsolationGrpMember addGroupMember(SnmpParam snmpParam, PortIsolationGrpMember member) {
        member.setMemberRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, member);
    }

    @Override
    public void deleteGroupMember(SnmpParam snmpParam, PortIsolationGrpMember member) {
        member.setMemberRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, member);
    }

}
