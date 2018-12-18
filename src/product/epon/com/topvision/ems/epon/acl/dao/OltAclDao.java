/***********************************************************************
 * $Id: OltAclDao.java,v1.0 2013年10月25日 下午5:41:14 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.dao;

import java.util.List;

import com.topvision.ems.epon.acl.domain.AclListTable;
import com.topvision.ems.epon.acl.domain.AclPortACLListTable;
import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:41:14
 *
 */
public interface OltAclDao extends BaseEntityDao<Entity> {

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
     * 获取端口下关联AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAllAclPortACLList(Long entityId);

    /**
     * 获取ACL关联的端口
     * 
     * @param entityId
     *            设备ID
     * @param aclIndex
     *            ACL INDEX
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAclPortByAclList(Long entityId, Integer aclIndex);

    /**
     * 关联一个ACLLIST到一个端口
     * 
     * @param aclPortACLListTable
     *            关联参数
     */
    public void addAclPortACLList(AclPortACLListTable aclPortACLListTable);

    /**
     * 删除一个acllist的关联
     * 
     * @param aclPortACLListTable
     *            删除管理的参数
     */
    public void deleteAclPortACLList(AclPortACLListTable aclPortACLListTable);

    /***************************************************** Acl列表配置 **************************************************/
    /**
     * 获取设备所有AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @return List<AclListTable>
     */
    public List<AclListTable> getAclList(Long entityId);

    /**
     * 添加一个AclList
     * 
     * @param aclListTable
     *            AclList的参数
     */
    public void addAclList(AclListTable aclListTable);

    /**
     * 修改一个AclList
     * 
     * @param aclListTable
     *            修改的参数
     */
    public void modifyAclList(AclListTable aclListTable);

    /**
     * 删除一个AclList
     * 
     * @param entityId
     *            设备Id
     * @param topAclListIndex
     *            AclList的index
     */
    public void deleteAclList(Long entityId, Integer topAclListIndex);

    /***************************************************** AclRule配置 **************************************************/
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
     */
    public void addAclRuleList(AclRuleTable aclRuleTable);

    /**
     * 修改一个AclRule
     * 
     * @param aclRuleTable
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
     */
    public void deleteAclRuleList(Long entityId, Integer topAclRuleListIndex, Integer topAclRuleIndex);

    /**
     * 保存AclRule到数据库
     * 
     * @param aclRuleTables
     *            aclRule对象列表
     */
    void saveAclRuleList(Long entityId, List<AclRuleTable> aclRuleTables);

    /**
     * 保存Acl管理端口对象到数据库
     * 
     * @param aclPortACLListTables
     *            关联列表
     */
    void saveAclPortACLList(Long entityId, List<AclPortACLListTable> aclPortACLListTables);

    /**
     * 保存AclList到数据库
     * 
     * @param aclListTables
     *            Acllist对象列表
     */
    void saveAclList(Long entityId, List<AclListTable> aclListTables);

    /**
     * 获取一个aclRule
     * 
     * @param entityId
     * @param aclIndex
     * @param aclRuleIndex
     */
    public AclRuleTable getAclRule(Long entityId, Integer aclIndex, Integer aclRuleIndex);

    /**
     * 修改acl的RuleNum
     * 
     * @param entityId
     * @param aclIndex
     * @param flag
     */
    public void modifyAclListAclRuleNum(Long entityId, Integer aclIndex, Integer flag);

}
