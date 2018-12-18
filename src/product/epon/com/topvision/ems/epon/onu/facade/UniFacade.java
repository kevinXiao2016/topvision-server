/***********************************************************************
 * $Id: OnuFacade.java,v1.0 2013-10-25 上午11:22:35 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.facade;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午11:22:35
 *
 */
@EngineFacade(serviceName = "UniFacade", beanName = "uniFacade")
public interface UniFacade extends Facade {

    /**
     * UNI使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI口索引
     * @param status
     *            UNI口使能状态
     * @return Integer
     */
    OltUniAttribute setUniAdminStatus(SnmpParam snmpParam, OltUniAttribute oltUniAttribute);

    /**
     * UNI口自协商使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI口索引
     * @param status
     *            UNI口自协商使能状态
     * @return Integer
     */
    Integer setUniAutoNegotiationStatus(SnmpParam snmpParam, Long uniIndex, Integer status);

    /**
     * UNI端口隔离使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI口索引
     * @param uniIsolationEnable
     *            UNI端口隔离使能状态
     * @return Integer
     */
    Integer setUniIsolationEnable(SnmpParam snmpParam, Long uniIndex, Integer uniIsolationEnable);

    /**
     * UNI流控使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI口索引
     * @param uniFlowCtrlEnable
     *            UNI流控使能状态
     * @return Integer
     */
    Integer setUniFlowCtrlEnable(SnmpParam snmpParam, Long uniIndex, Integer uniFlowCtrlEnable);

    /**
     * UNI的15min性能统计使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI口索引
     * @param uni15minEnable
     *            UNI的15min性能统计使能状态
     * @return Integer
     */
    Integer setUni15minEnable(SnmpParam snmpParam, Long uniIndex, Integer uni15minEnable);

    /**
     * UNI的24h性能统计使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI口索引
     * @param uni24hEnable
     *            UNI的24h性能统计使能状态
     * @return Integer
     */
    Integer setUni24hEnable(SnmpParam snmpParam, Long uniIndex, Integer uni24hEnable);

    /**
     * 修改UNI广播风暴抑制信息
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltUniStormSuppressionEntry
     *            UNI端口广播风暴抑制的属性
     * @return OltUniStormSuppressionEntry
     */
    OltUniStormSuppressionEntry modifyUniStormInfo(SnmpParam snmpParam,
            OltUniStormSuppressionEntry oltUniStormSuppressionEntry);

    /**
     * 修改UNI端口限速信息
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltUniPortRateLimit
     *            UNI端口限速的属性
     * @return OltUniPortRateLimit
     */
    OltUniPortRateLimit modifyUniRateLimitInfo(SnmpParam snmpParam, OltUniPortRateLimit oltUniPortRateLimit);

    /**
     * 刷新UNI端口限速信息
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltUniPortRateLimit
     *            UNI端口限速的属性
     * @return OltUniPortRateLimit
     */
    OltUniPortRateLimit refreshUniRateLimitInfo(SnmpParam snmpParam, OltUniPortRateLimit oltUniPortRateLimit);

    /**
     * 重新进行UNI端口自协商
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI索引
     */
    void restartUniAutoNego(SnmpParam snmpParam, Long uniIndex);

    /**
     * UNI端口自协商使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI索引
     * @param uniAutoNegoEnable
     *            UNI端口自协商使能状态
     * @return Integer
     */
    Integer setUniAutoNegoEnable(SnmpParam snmpParam, Long uniIndex, Integer uniAutoNegoEnable);

    /**
     * 获取UNI端口自协商状态
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param uniIndex
     *            UNI索引
     * @return Integer
     */
    Integer getUniAutoNegoStatus(SnmpParam snmpParam, Long uniIndex);

    /**
     * UNI端口自协商工作模式修改
     * 
     * @param snmpParam
     * @param OltUniExtAttribute
     * @return OltUniExtAttribute
     */
    OltUniExtAttribute updateOltUniAutoNegoMode(SnmpParam snmpParam, OltUniExtAttribute oltUniExtAttribute);

    /**
     * UNI端口老化时间修改
     * 
     * @param snmpParam
     * @param oltUniExtAttribute
     * @return OltUniExtAttribute
     */
    OltUniExtAttribute updateOltUniMacAge(SnmpParam snmpParam, OltUniExtAttribute oltUniExtAttribute);

    /**
     * 从设备获取UNI端口MAC老化时间
     * 
     * @param snmpParam
     * @param oltUniExtAttribute
     * @return OltUniExtAttribute
     */
    OltUniExtAttribute fetchOltUniMacAge(SnmpParam snmpParam, OltUniExtAttribute oltUniExtAttribute);

    /**
     * 修改uni mac地址最大学习数
     * 
     * @param snmpParam
     * @param OltUniExtAttribute
     * @return
     */
    OltUniExtAttribute modifyUniMacAddrLearnMaxNum(SnmpParam snmpParam, OltUniExtAttribute oltTopUniAttribute);

    /**
     * UNI端口mac地址清理
     * 
     * @param snmpParam
     * @param uniExtAttribute
     * @return
     */
    void setUniMacClear(SnmpParam snmpParam, OltUniExtAttribute uniExtAttribute);

    /**
     * 设置下行报文环回使能
     * 
     * @param snmpParam
     */
    void setUniDSLoopBackEnable(SnmpParam snmpParam, OltUniExtAttribute v);

    /**
     * 设置上行utag报文指定优先级
     * 
     * @param snmpParam
     */
    void setUniUSUtgPri(SnmpParam snmpParam, OltUniExtAttribute v);

    /**
     * 刷新UNI口的广播风暴抑制参数
     * 
     * @param snmpParam
     */
    public List<OltUniStormSuppressionEntry> refreshOnuStormOutPacketRate(SnmpParam snmpParam);

    /**
     * 刷新UNI口的上行untag报文指定优先级
     * 
     * @param snmpParam
     * @param o
     * @return
     */
    public OltUniExtAttribute refreshUniUSUtgPri(SnmpParam snmpParam, OltUniExtAttribute o);

    /**
     * 配置ONU UNI端口环回使能
     * 
     * @param snmpParam
     * @param v
     */
    void setUniLoopDetectEnable(SnmpParam snmpParam, OltUniExtAttribute v);

    /**
     * 通过设备Uni口的属性
     * 
     * @param snmpParam
     *            snmpParam
     * @return OltUniExtAttribute
     */
    List<OltUniExtAttribute> getUniListAttribute(SnmpParam snmpParam);

    /**
     * 刷新UNI口EXT信息
     * 
     * @param snmpParam
     * @param oltUniExtAttribute
     * @return
     */
    OltUniExtAttribute refreshOnuUniExtInfo(SnmpParam snmpParam, OltUniExtAttribute oltUniExtAttribute);

    /**
     * 刷新UNI口信息
     * 
     * @param snmpParam
     * @param oltUniAttribute
     */
    OltUniAttribute refreshOnuUniAttribute(SnmpParam snmpParam, OltUniAttribute oltUniAttribute);

    /**
     * UNI 限速信息
     * 
     * @param snmpParam
     * @return
     */
    List<OltUniPortRateLimit> getOltUniPortRateLimits(SnmpParam snmpParam);

    /**
     * 获取一个onu下的uni风暴抑制
     * 
     * @param snmpParam
     * @param oltUniPortRateLimits
     * @return
     */
    List<OltUniPortRateLimit> getUniPortRateLimit(SnmpParam snmpParam, List<OltUniPortRateLimit> oltUniPortRateLimits);

    /**
     * 刷新UNI的VLAN信息
     * 
     * @param snmpParam
     * @param vlanTable
     * @return
     */
    UniVlanBindTable refreshUniVlanBindTable(SnmpParam snmpParam, UniVlanBindTable vlanTable);

}
