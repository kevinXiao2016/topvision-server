/***********************************************************************
 * $Id: IsolationGroupConfigService.java,v1.0 2014-12-18 上午11:38:01 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.service;

import java.util.List;

import com.topvision.ems.epon.portisolation.domain.PortIsolationGroup;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGrpMember;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2014-12-18-上午11:38:01
 *
 */
public interface IsolationGroupConfigService extends Service {

    /**
     * 添加隔离组
     * @param group
     */
    void addGroup(PortIsolationGroup group);

    /**
     * 更新隔离组
     * @param group
     */
    void modifyGroup(PortIsolationGroup group);

    /**
     * 删除隔离组
     * @param group
     */
    void deleteGroup(PortIsolationGroup group);

    /**
     * 查询隔离组
     * @param entityId
     * @param groupIndex
     * @return
     */
    PortIsolationGroup getGroup(Long entityId, Integer groupIndex);

    /**
     * 查询设备所有隔离组
     * @param entityId
     * @return
     */
    List<PortIsolationGroup> getGroupList(Long entityId);

    /**
     * 添加隔离组成员
     * @param groupMember
     */
    void addGroupMemeber(PortIsolationGrpMember groupMember);

    /**
     * 批量添加隔离组成员
     * @param memberList
     * @param entityId
     */
    void batchAddGroupMember(List<PortIsolationGrpMember> memberList, Long entityId);

    /**
     * 删除隔离组成员
     * @param groupMember
     */
    void deleteGroupMember(PortIsolationGrpMember groupMember);

    /**
     * 删除隔离组下所有成员
     * @param groupMember
     */
    void deleteMemberByGroup(PortIsolationGrpMember groupMember);

    /**
     * 批量删除隔离组成员
     * @param entityId
     * @param groupIndex
     * @param portsList
     */
    void deleteMemberByPorts(Long entityId, Integer groupIndex, String portsStr);

    /**
     * 获取隔离组成员
     * @param paramsMap
     * @return
     */
    List<PortIsolationGrpMember> getGroupMember(Long entityId, Integer groupIndex);

    /**
     * 刷新隔离组信息
     * @param entityId
     */
    void refreshGroup(Long entityId);

    /**
     * 刷新隔离组成员
     * @param entityId
     */
    void refreshGroupMember(Long entityId);

    /**
     * 刷新隔离组与成员信息
     * @param entityId
     */
    void refreshGroupData(Long entityId);

}
