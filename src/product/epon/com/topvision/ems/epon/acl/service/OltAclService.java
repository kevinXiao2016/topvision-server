/***********************************************************************
 * $Id: OltAclService.java,v1.0 2013-10-25 下午5:28:38 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.service;

import java.util.List;

import com.topvision.ems.epon.acl.domain.AclListTable;
import com.topvision.ems.epon.acl.domain.AclPortACLListTable;
import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.ems.epon.exception.AddAclListException;
import com.topvision.ems.epon.exception.AddAclPortACLListException;
import com.topvision.ems.epon.exception.AddAclRuleListException;
import com.topvision.ems.epon.exception.DeleteAclListException;
import com.topvision.ems.epon.exception.DeleteAclPortACLListException;
import com.topvision.ems.epon.exception.DeleteAclRuleListException;
import com.topvision.ems.epon.exception.ModifyAclListException;
import com.topvision.ems.epon.exception.ModifyAclRuleListException;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013-10-25-下午5:28:38
 *
 */
public interface OltAclService extends Service {

    /**
     * 获取端口下关联AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @param topAclPortDirection
     *            规则方向
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAclPortACLList(Long entityId, Long portIndex, Integer topAclPortDirection);

    /**
     * 获取端口所在的板卡的所有端口绑定的ACL的ID列表
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return List<Integer>
     */
    public List<Integer> getAclListBySlot(Long entityId, Long portIndex);

    /**
     * 关联一个ACLLIST到一个端口
     * 
     * @param aclPortACLListTable
     *            关联参数
     * @throws AddAclPortACLListException
     *             当添加关联失败的时候抛出
     */
    public void addAclPortACLList(AclPortACLListTable aclPortACLListTable);

    /**
     * 删除一个acllist的关联
     * 
     * @param aclPortACLListTable
     *            删除管理的参数
     * @throws DeleteAclPortACLListException
     *             当删除关联失败的时候抛出
     */
    public void deleteAclPortACLList(AclPortACLListTable aclPortACLListTable);

    /**
     * 获取设备所有AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @return List<AclListTable>
     */
    public List<AclListTable> getAclList(Long entityId);

    /**
     * 获取一个AclList下的所有port的方法
     * 
     * @param entityId
     *            设备Id
     * @param topAclRuleListIndex
     *            AclList的Index
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAclPortByAclList(Long entityId, Integer topAclRuleListIndex);

    /**
     * 添加一个AclList
     * 
     * @param aclListTable
     *            AclList的参数
     * @throws AddAclListException
     *             添加AclList失败时抛出
     */
    public void addAclList(AclListTable aclListTable);

    /**
     * 修改一个AclList
     * 
     * @param aclListTable
     *            修改的参数
     * @throws ModifyAclListException
     *             修改AclList失败时抛出
     */
    public void modifyAclList(AclListTable aclListTable);

    /**
     * 删除一个AclList
     * 
     * @param entityId
     *            设备Id
     * @param topAclListIndex
     *            AclList的index
     * @throws DeleteAclListException
     *             删除AclList失败时抛出
     */
    public void deleteAclList(Long entityId, Integer topAclListIndex);

    /**
     * 获取一个Acllist下的所有Aclrule
     * 
     * @param entityId
     *            设备Id
     * @param topAclRuleListIndex
     *            AclList的Index
     * @return List<AclRuleTable>
     */
    public List<AclRuleTable> getAclRuleList(Long entityId, Integer topAclRuleListIndex);

    /**
     * 获取所有的Aclrule
     * 
     * @param entityId
     *            设备Id
     * @return List<AclRuleTable>
     */
    public List<AclRuleTable> getAllAclRuleList(Long entityId);

    /**
     * 添加一个AclRule
     * 
     * @param aclRuleTable
     *            AclRule的参数
     * @throws AddAclRuleListException
     *             当添加AclRule失败的时候抛出
     */
    public void addAclRuleList(AclRuleTable aclRuleTable);

    /**
     * 修改一个AclRule
     * 
     * @param aclRuleTable
     * @throws ModifyAclRuleListException
     *             当修改AclRule失败的时候抛出
     */
    public void modifyAclRuleList(AclRuleTable aclRuleTable);

    /**
     * 删除一个AclRule
     * 
     * @param entityId
     *            设备Id
     * @param topAclRuleListIndex
     *            AclList的index
     * @param topAclRuleIndex
     *            aclrule的Index
     * @throws DeleteAclRuleListException
     *             当删除AclRule失败的时候抛出
     */
    public void deleteAclRuleList(Long entityId, Integer topAclRuleListIndex, Integer topAclRuleIndex);

    void refreshAclRuleList(Long entityId);

    void refreshAclPortACLList(Long entityId);

    void refreshAclListTable(Long entityId);

    /**
     * 获取一个AclRule
     * 
     * @param entityId
     *            设备Id
     * @param aclIndex
     *            AclList的index
     * @param aclRuleIndex
     *            aclrule的Index
     */
    public AclRuleTable getAclRule(Long entityId, Integer aclIndex, Integer aclRuleIndex);

}
