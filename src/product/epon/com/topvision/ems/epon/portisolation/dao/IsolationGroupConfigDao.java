/***********************************************************************
 * $Id: IsolationGroupConfigDao.java,v1.0 2014-12-18 上午11:32:31 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.dao;

import java.util.List;

import com.topvision.ems.epon.portisolation.domain.PortIsolationGroup;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGrpMember;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2014-12-18-上午11:32:31
 *
 */
public interface IsolationGroupConfigDao extends BaseEntityDao<Object> {

    /**
     * 添加隔离组
     * @param group
     */
    void insertGroup(PortIsolationGroup group);

    /**
     * 批量插入隔离组
     * 在获取设备采集数据时使用
     * @param groupList
     * @param entityId
     */
    void batchInsertGroup(List<PortIsolationGroup> groupList, Long entityId);

    /**
     * 更新隔离组
     * @param group
     */
    void updateGroup(PortIsolationGroup group);

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
    PortIsolationGroup queryGroup(Long entityId, Integer groupIndex);

    /**
     * 查询设备所有隔离组
     * @param entityId
     * @return
     */
    List<PortIsolationGroup> queryGroupList(Long entityId);

    /**
     * 添加隔离组成员
     * @param groupMember
     */
    void insertGroupMemeber(PortIsolationGrpMember groupMember);

    /**
     * 批量插入隔离组成员
     * 在设备数据采集时使用
     * @param memberList
     * @param entityId
     */
    void batchInsertMemberWithDelete(List<PortIsolationGrpMember> memberList, Long entityId);

    /**
     * 批量添加隔离组成员
     * @param memberList
     */
    void batchInsertGroupMember(List<PortIsolationGrpMember> memberList);

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
    void deleteMemberByPorts(Long entityId, Integer groupIndex, String portsList);

    /**
     * 获取隔离组成员
     * @param paramsMap
     * @return
     */
    List<PortIsolationGrpMember> queryGroupMember(Long entityId, Integer groupIndex);

    /**
     * 根据选择的端口获取隔离组成员
     * @param entityId
     * @param groupIndex
     * @param portsList
     * @return
     */
    List<PortIsolationGrpMember> queryMemberByPorts(Long entityId, Integer groupIndex, String portsList);

}
