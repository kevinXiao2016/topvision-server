/***********************************************************************
 * $Id: OltAclFacade.java,v1.0 2013年10月25日 下午5:42:24 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.facade;

import java.util.List;

import com.topvision.ems.epon.acl.domain.AclListTable;
import com.topvision.ems.epon.acl.domain.AclPortACLListTable;
import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:42:24
 *
 */
@EngineFacade(serviceName = "OltAclFacade", beanName = "oltAclFacade")
public interface OltAclFacade extends Facade {
    /**
     * 获取端口下关联AclList的方法
     * 
     * @param snmpParam
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAclPortACLList(SnmpParam snmpParam);

    /**
     * 关联一个ACLLIST到一个端口
     * 
     * @param snmpParam
     * @param aclPortACLListTable
     *            关联参数
     */
    public void addAclPortACLList(SnmpParam snmpParam, AclPortACLListTable aclPortACLListTable);

    /**
     * 删除一个acllist的关联
     * 
     * @param snmpParam
     * @param aclPortACLListTable
     *            删除管理的参数
     */
    public void deleteAclPortACLList(SnmpParam snmpParam, AclPortACLListTable aclPortACLListTable);

    /**
     * 获取设备所有AclList的方法
     * 
     * @param snmpParam
     * @return List<AclListTable>
     */
    public List<AclListTable> getAclList(SnmpParam snmpParam);

    /**
     * 添加一个AclList
     * 
     * @param snmpParam
     * @param aclListTable
     *            AclList的参数
     */
    public void addAclList(SnmpParam snmpParam, AclListTable aclListTable);

    /**
     * 修改一个AclList
     * 
     * @param snmpParam
     * @param aclListTable
     *            修改的参数
     */
    public void modifyAclList(SnmpParam snmpParam, AclListTable aclListTable);

    /**
     * 删除一个AclList
     * 
     * @param snmpParam
     * @param topAclListIndex
     *            AclList的index
     */
    public void deleteAclList(SnmpParam snmpParam, Integer topAclListIndex);

    /**
     * 获取一个Acllist下的所有Aclrule
     * 
     * @param snmpParam
     * @return List<AclRuleTable>
     */
    public List<AclRuleTable> getAclRuleList(SnmpParam snmpParam);

    /**
     * 添加一个AclRule
     * 
     * @param snmpParam
     * @param aclRuleTable
     *            AclRule的参数
     */
    public void addAclRuleList(SnmpParam snmpParam, AclRuleTable aclRuleTable);

    /**
     * 修改一个AclRule
     * 
     * @param snmpParam
     * @param aclRuleTable
     */
    public void modifyAclRuleList(SnmpParam snmpParam, AclRuleTable aclRuleTable);

    /**
     * 删除一个AclRule
     * 
     * @param snmpParam
     * @param topAclRuleListIndex
     *            AclList的index
     * @param topAclRuleIndex
     *            aclrule的Index
     */
    public void deleteAclRuleList(SnmpParam snmpParam, Integer topAclRuleListIndex, Integer topAclRuleIndex);

}
