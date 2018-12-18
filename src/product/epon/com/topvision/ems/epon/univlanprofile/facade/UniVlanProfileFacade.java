/***********************************************************************
 * $Id: UniVlanProfileFacade.java,v1.0 2013-11-28 上午10:14:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.facade;

import java.util.List;

import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanRuleTable;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-11-28-上午10:14:42
 *
 */
@EngineFacade(serviceName = "UniVlanProfileFacade", beanName = "uniVlanProfileFacade")
public interface UniVlanProfileFacade extends Facade {

    /**
     * 刷新UNI vlan模板
     * 
     * @param snmpParam
     * @return
     */
    List<UniVlanProfileTable> getUniVlanProfiles(SnmpParam snmpParam);

    /**
     * 新增UNI vlan模板
     * 
     * @param snmpParam
     * @param uniVlanProfile
     * @return
     */
    UniVlanProfileTable addUniVlanProfile(SnmpParam snmpParam, UniVlanProfileTable uniVlanProfile);

    /**
     * 删除UNI vlan模板
     * 
     * @param snmpParam
     * @param uniVlanProfile
     * @return
     */
    UniVlanProfileTable deleteUniVlanProfile(SnmpParam snmpParam, UniVlanProfileTable uniVlanProfile);

    /**
     * 修改UNI vlan模板
     * 
     * @param snmpParam
     * @param uniVlanProfile
     * @return
     */
    UniVlanProfileTable setUniVlanProfile(SnmpParam snmpParam, UniVlanProfileTable uniVlanProfile);

    /**
     * 刷新UNI vlan模板规则列表
     * 
     * @param snmpParam
     * @return
     */
    List<UniVlanRuleTable> getUniVlanRules(SnmpParam snmpParam);

    /**
     * 新增UNI vlan模板规则
     * 
     * @param snmpParam
     * @param uniVlanRule
     * @return
     */
    UniVlanRuleTable addUniVlanRule(SnmpParam snmpParam, UniVlanRuleTable uniVlanRule);

    /**
     * 删除UNI vlan模板规则
     * 
     * @param snmpParam
     * @param uniVlanRule
     * @return
     */
    UniVlanRuleTable deleteUniVlanRule(SnmpParam snmpParam, UniVlanRuleTable uniVlanRule);

    /**
     * 修改UNI vlan模板规则
     * 
     * @param snmpParam
     * @param uniVlanRule
     * @return
     */
    UniVlanRuleTable setUniVlanRule(SnmpParam snmpParam, UniVlanRuleTable uniVlanRule);

    /**
     * 获得UNI 端口绑定模板
     * 
     * @param snmpParam
     * @return
     */
    List<UniVlanBindTable> getUniVlanBindTables(SnmpParam snmpParam);

    /**
     * 修改UNI 端口绑定模板
     * 
     * @param snmpParam
     * @return
     */
    UniVlanBindTable setUniVlanBindTables(SnmpParam snmpParam, UniVlanBindTable uniVlanBindTable);

    /**
     * 获取指定UNI口的Uni vlan绑定信息
     * @param snmpParam
     * @param uniVlanBindTable
     * @return
     */
    UniVlanBindTable getUniVlanBindInfo(SnmpParam snmpParam, UniVlanBindTable uniVlanBindTable);

    /**
     *  替换uni口的模板绑定
     * @param snmpParam
     * @param uniIndex
     * @param profileIndex
     */
    void replaceBindProfile(SnmpParam snmpParam, UniVlanBindTable uniBindInfo);

    /**
     * 获取一个onu下uni口vlan bind
     * @param snmpParam
     * @param uniVlanBindTables
     * @return
     */
    List<UniVlanBindTable> getUniVlanBindTables(SnmpParam snmpParam, List<UniVlanBindTable> uniVlanBindTables);

    /**
     * 解除UNI口模板的绑定
     * @param snmpParam
     * @param uniVlanBindTable
     */
    void unBindUniProfile(SnmpParam snmpParam, UniVlanBindTable uniVlanBindTable);

}
