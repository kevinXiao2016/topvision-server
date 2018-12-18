/***********************************************************************
 * $Id: OltControlFacade.java,v1.0 2013-10-25 上午10:42:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.facade;

import java.util.List;

import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.domain.OltSniMacAddress;
import com.topvision.ems.epon.olt.domain.OltSniRedirect;
import com.topvision.ems.epon.olt.domain.OltSniStormSuppressionEntry;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午10:42:05
 *
 */
@EngineFacade(serviceName = "oltSniFacade", beanName = "oltSniFacade")
public interface OltSniFacade extends Facade {

    /**
     * SNI口名称设置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param sniName
     *            SNI口名称
     * @return String
     */
    String setSniName(SnmpParam snmpParam, Long sniIndex, String sniName);

    /**
     * SNI口使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param status
     *            SNI口使能状态
     * @return Integer
     */
    Integer setSniAdminStatus(SnmpParam snmpParam, Long sniIndex, Integer status);

    /**
     * SNI口隔离使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param status
     *            SNI口隔离使能状态
     * @return Integer
     */
    Integer setSniIsolationStatus(SnmpParam snmpParam, Long sniIndex, Integer status);

    /**
     * SNI口流量控制设置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param ingressRate
     *            入方向流速
     * @param egressRate
     *            出方向流速
     * @return OltSniAttribute
     */
    OltSniAttribute setSniFlowControl(SnmpParam snmpParam, Long sniIndex, Integer ingressRate, Integer egressRate);

    /**
     * SNI口流量控制使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param status
     *            使能状态
     */
    OltSniAttribute setSniCtrlFlowEnable(SnmpParam snmpParam, Long sniIndex, Integer status);

    /**
     * SNI口广播风暴参数修改
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltSniStormSuppressionEntry
     *            SNI口广播风暴参数
     * @return OltSniStormSuppressionEntry
     */
    OltSniStormSuppressionEntry modifySniStormSuppression(SnmpParam snmpParam,
            OltSniStormSuppressionEntry oltSniStormSuppressionEntry);

    /**
     * SNI口MAC老化时间修改
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAttribute
     *            OLT属性
     * @return Integer
     */
    Integer modifySniMacAddressAgingTime(SnmpParam snmpParam, OltAttribute oltAttribute);

    /**
     * SNI口自协商状态设置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param mode
     *            自协商模式
     * @return Integer
     */
    Integer setSniAutoNegotiationMode(SnmpParam snmpParam, Long sniIndex, Integer mode);

    /**
     * 修改SNI口重定向
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param redirect
     */
    void modifyRedirectSni(SnmpParam snmpParam, OltSniRedirect redirect);

    /**
     * 新增SNI口重定向
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param redirect
     */
    void addRedirectSni(SnmpParam snmpParam, OltSniRedirect redirect);

    /**
     * 删除SNI口重定向
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param redirect
     */
    void deleteRedirectSni(SnmpParam snmpParam, OltSniRedirect redirect);

    /**
     * SNI口MAC最大学习数设置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param maxLearnMacNum
     *            MAC最大学习数
     * @return Long
     */
    Long setSniMaxLearnMacNum(SnmpParam snmpParam, Long sniIndex, Long maxLearnMacNum);

    /**
     * SNI口15分钟性能统计使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param perfStatus
     *            15分钟性能统计使能
     * @return Integer
     */
    Integer setSni15MinPerfStatus(SnmpParam snmpParam, Long sniIndex, Integer perfStatus);

    /**
     * SNI口24小时性能统计使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sniIndex
     *            SNI口索引
     * @param perfStatus
     *            24小时性能统计使能
     * @return Integer
     */
    Integer setSni24HourPerfStatus(SnmpParam snmpParam, Long sniIndex, Integer perfStatus);

    /**
     * 修改SNI口MAC地址管理 （包括新增与删除）
     * 
     * @param snmpParam
     * @param sniMacAddress
     */
    void modifySniMacAddress(SnmpParam snmpParam, OltSniMacAddress sniMacAddress);

    /**
     * 通过sniindex获取sni口的属性
     * 
     * @param snmpParam
     *            snmpParam
     * @param sniIndex
     *            sniIndex
     * @return OltSniAttribute
     */
    OltSniAttribute getOltSniAttributeBySniIndex(SnmpParam snmpParam, Long sniIndex);

    /**
     * 通过设备sni口的属性
     * 
     * @param snmpParam
     *            snmpParam
     * @return OltSniAttribute
     */
    List<OltSniAttribute> getSniListAttribute(SnmpParam snmpParam);

    /**
     * 获取sni风暴抑制配置
     * @param snmpParam
     * @return
     */
    List<OltSniStormSuppressionEntry> getoltSniStormSuppressionEntries(SnmpParam snmpParam);

}
