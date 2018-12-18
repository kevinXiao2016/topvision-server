/***********************************************************************
 * $Id: OltAclFacadeImpl.java,v1.0 2013年10月25日 下午5:43:50 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.acl.domain.AclListTable;
import com.topvision.ems.epon.acl.domain.AclPortACLListTable;
import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.ems.epon.acl.facade.OltAclFacade;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:43:50
 *
 */
public class OltAclFacadeImpl extends EmsFacade implements OltAclFacade {
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /**
     * 添加一个AclList
     * 
     * @param snmpParam
     * @param aclListTable
     *            AclList的参数
     */
    public void addAclList(SnmpParam snmpParam, AclListTable aclListTable) {
        aclListTable.setTopRuleRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, aclListTable);
    }

    /**
     * 关联一个ACLLIST到一个端口
     * 
     * @param snmpParam
     * @param aclPortACLListTable
     *            关联参数
     */
    public void addAclPortACLList(SnmpParam snmpParam, AclPortACLListTable aclPortACLListTable) {
        aclPortACLListTable.setTopAclRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, aclPortACLListTable);
    }

    /**
     * 添加一个AclRule
     * 
     * @param snmpParam
     * @param aclRuleTable
     *            AclRule的参数
     */
    public void addAclRuleList(SnmpParam snmpParam, AclRuleTable aclRuleTable) {
        aclRuleTable.setTopAclRuleRowStatus(RowStatus.CREATE_AND_GO);
        //格式化MAC地址格式
        if (aclRuleTable.getTopMatchedDstMac() != null) {
            String dstMac = MacUtils.convertToMaohaoFormat(aclRuleTable.getTopMatchedDstMac());
            aclRuleTable.setTopMatchedDstMac(dstMac);
        }
        if (aclRuleTable.getTopMatchedSrcMac() != null) {
            String srcMac = MacUtils.convertToMaohaoFormat(aclRuleTable.getTopMatchedSrcMac());
            aclRuleTable.setTopMatchedSrcMac(srcMac);
        }
        snmpExecutorService.setData(snmpParam, aclRuleTable);
    }

    /**
     * 删除一个AclList
     * 
     * @param snmpParam
     * @param topAclListIndex
     *            AclList的index
     */
    public void deleteAclList(SnmpParam snmpParam, Integer topAclListIndex) {
        AclListTable aclListTable = new AclListTable();
        aclListTable.setTopAclListIndex(topAclListIndex);
        aclListTable.setTopRuleRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, aclListTable);
    }

    /**
     * 删除一个acllist的关联
     * 
     * @param snmpParam
     * @param aclPortACLListTable
     *            删除管理的参数
     */
    public void deleteAclPortACLList(SnmpParam snmpParam, AclPortACLListTable aclPortACLListTable) {
        aclPortACLListTable.setTopAclRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, aclPortACLListTable);
    }

    /**
     * 删除一个AclRule
     * 
     * @param snmpParam
     * @param topAclRuleListIndex
     *            AclList的index
     * @param topAclRuleIndex
     *            aclrule的Index
     */
    public void deleteAclRuleList(SnmpParam snmpParam, Integer topAclRuleListIndex, Integer topAclRuleIndex) {
        AclRuleTable aclRuleTable = new AclRuleTable();
        aclRuleTable.setTopAclRuleListIndex(topAclRuleListIndex);
        aclRuleTable.setTopAclRuleIndex(topAclRuleIndex);
        aclRuleTable.setTopAclRuleRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, aclRuleTable);
    }

    /**
     * 获取设备所有AclList的方法
     * 
     * @param snmpParam
     * @return List<AclListTable>
     */
    public List<AclListTable> getAclList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, AclListTable.class);
    }

    /**
     * 获取端口下关联AclList的方法
     * 
     * @param snmpParam
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAclPortACLList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, AclPortACLListTable.class);
    }

    /**
     * 获取一个Acllist下的所有Aclrule
     * 
     * @param snmpParam
     * @return List<AclRuleTable>
     */
    public List<AclRuleTable> getAclRuleList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, AclRuleTable.class);
    }

    /**
     * 修改一个AclList
     * 
     * @param snmpParam
     * @param aclListTable
     *            修改的参数
     */
    public void modifyAclList(SnmpParam snmpParam, AclListTable aclListTable) {
        snmpExecutorService.setData(snmpParam, aclListTable);
    }

    /**
     * 修改一个AclRule
     * 
     * @param snmpParam
     * @param aclRuleTable
     */
    public void modifyAclRuleList(SnmpParam snmpParam, AclRuleTable aclRuleTable) {
        snmpExecutorService.setData(snmpParam, aclRuleTable);
    }
}
