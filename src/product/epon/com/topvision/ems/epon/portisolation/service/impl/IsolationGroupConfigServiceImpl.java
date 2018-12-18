/***********************************************************************
 * $Id: IsolationGroupConfigService.java,v1.0 2014-12-18 上午11:39:01 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.portisolation.dao.IsolationGroupConfigDao;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGroup;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGrpMember;
import com.topvision.ems.epon.portisolation.facade.IsolationGroupConfigFacade;
import com.topvision.ems.epon.portisolation.service.IsolationGroupConfigService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2014-12-18-上午11:39:01
 *
 */
@Service("isolationGroupConfigService")
public class IsolationGroupConfigServiceImpl extends BaseService implements IsolationGroupConfigService,
        SynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    IsolationGroupConfigDao isolationGroupConfigDao;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long entityId = event.getEntityId();
        if (event.getEventType().equals("OltManagement")) {
            try {
                refreshGroup(entityId);
                logger.debug("refreshGroup finish");
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("refreshGroup wrong", e);
                }
            }
            try {
                refreshGroupMember(entityId);
                logger.debug("refreshGroupMember finish");
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("refreshGroupMember wrong", e);
                }
            }
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {

    }

    @Override
    public void addGroup(PortIsolationGroup group) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(group.getEntityId());
        PortIsolationGroup newGroup = getGroupConfigFacade(snmpParam.getIpAddress())
                .addIsolationGroup(snmpParam, group);
        isolationGroupConfigDao.insertGroup(newGroup);
    }

    @Override
    public void modifyGroup(PortIsolationGroup group) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(group.getEntityId());
        PortIsolationGroup newGroup = getGroupConfigFacade(snmpParam.getIpAddress()).modifyIsolationGroup(snmpParam,
                group);
        isolationGroupConfigDao.updateGroup(newGroup);
    }

    @Override
    public void deleteGroup(PortIsolationGroup group) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(group.getEntityId());
        //删除隔离组时如果存在成员则先先成员删除
        List<PortIsolationGrpMember> memberList = getGroupMember(group.getEntityId(), group.getGroupIndex());
        if (memberList != null && !memberList.isEmpty()) {
            for (PortIsolationGrpMember member : memberList) {
                deleteGroupMember(member);
            }
        }
        getGroupConfigFacade(snmpParam.getIpAddress()).deleteIsolationGroup(snmpParam, group);
        isolationGroupConfigDao.deleteGroup(group);
    }

    @Override
    public PortIsolationGroup getGroup(Long entityId, Integer groupIndex) {
        return isolationGroupConfigDao.queryGroup(entityId, groupIndex);
    }

    @Override
    public List<PortIsolationGroup> getGroupList(Long entityId) {
        return isolationGroupConfigDao.queryGroupList(entityId);
    }

    @Override
    public void addGroupMemeber(PortIsolationGrpMember groupMember) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(groupMember.getEntityId());
        getGroupConfigFacade(snmpParam.getIpAddress()).addGroupMember(snmpParam, groupMember);
        isolationGroupConfigDao.insertGroupMemeber(groupMember);
    }

    @Override
    public void batchAddGroupMember(List<PortIsolationGrpMember> memberList, Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        for (PortIsolationGrpMember member : memberList) {
            getGroupConfigFacade(snmpParam.getIpAddress()).addGroupMember(snmpParam, member);
        }
        isolationGroupConfigDao.batchInsertGroupMember(memberList);
    }

    @Override
    public void deleteGroupMember(PortIsolationGrpMember groupMember) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(groupMember.getEntityId());
        getGroupConfigFacade(snmpParam.getIpAddress()).deleteGroupMember(snmpParam, groupMember);
        isolationGroupConfigDao.deleteGroupMember(groupMember);
    }

    @Override
    public void deleteMemberByGroup(PortIsolationGrpMember groupMember) {
        List<PortIsolationGrpMember> memberList = getGroupMember(groupMember.getEntityId(), groupMember.getGroupIndex());
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(groupMember.getEntityId());
        for (PortIsolationGrpMember member : memberList) {
            getGroupConfigFacade(snmpParam.getIpAddress()).deleteGroupMember(snmpParam, member);
        }
        isolationGroupConfigDao.deleteMemberByGroup(groupMember);
    }

    @Override
    public void deleteMemberByPorts(Long entityId, Integer groupIndex, String portsStr) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String[] portsList = portsStr.split(",");
        PortIsolationGrpMember member = null;
        for (String portIndex : portsList) {
            member = new PortIsolationGrpMember();
            member.setEntityId(entityId);
            member.setGroupIndex(groupIndex);
            member.setPortIndex(Long.parseLong(portIndex));
            getGroupConfigFacade(snmpParam.getIpAddress()).deleteGroupMember(snmpParam, member);
        }
        isolationGroupConfigDao.deleteMemberByPorts(entityId, groupIndex, portsStr);
    }

    @Override
    public List<PortIsolationGrpMember> getGroupMember(Long entityId, Integer groupIndex) {
        return isolationGroupConfigDao.queryGroupMember(entityId, groupIndex);
    }

    @Override
    public void refreshGroup(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<PortIsolationGroup> groupList = getGroupConfigFacade(snmpParam.getIpAddress())
                .getIsolationGroup(snmpParam);
        isolationGroupConfigDao.batchInsertGroup(groupList, entityId);
    }

    @Override
    public void refreshGroupMember(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<PortIsolationGrpMember> memberList = getGroupConfigFacade(snmpParam.getIpAddress()).getGroupMember(
                snmpParam);
        isolationGroupConfigDao.batchInsertMemberWithDelete(memberList, entityId);
    }

    @Override
    public void refreshGroupData(Long entityId) {
        refreshGroup(entityId);
        refreshGroupMember(entityId);
    }

    private IsolationGroupConfigFacade getGroupConfigFacade(String ip) {
        return facadeFactory.getFacade(ip, IsolationGroupConfigFacade.class);
    }

}
