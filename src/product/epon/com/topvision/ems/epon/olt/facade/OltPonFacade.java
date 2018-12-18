/***********************************************************************
 * $Id: OltControlFacade.java,v1.0 2013-10-25 上午10:42:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.facade;

import java.util.List;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPonStormSuppressionEntry;
import com.topvision.ems.epon.olt.domain.TopPonPortRateLimit;
import com.topvision.ems.epon.olt.domain.TopPonPortSpeedEntry;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午10:42:05
 *
 */
@EngineFacade(serviceName = "OltPonFacade", beanName = "oltPonFacade")
public interface OltPonFacade extends Facade {

    /**
     * PON口使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param ponIndex
     *            PON口索引
     * @param status
     *            PON口使能状态
     * @return Integer
     */
    Integer setPonAdminStatus(SnmpParam snmpParam, Long ponIndex, Integer status);

    /**
     * PON口隔离使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param ponIndex
     *            PON口索引
     * @param status
     *            PON口隔离使能状态
     * @return Integer
     */
    Integer setPonIsolationStatus(SnmpParam snmpParam, Long ponIndex, Integer status);

    /**
     * PON口加密模式设置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param ponIndex
     *            PON口索引
     * @param encryptMode
     *            PON口加密模式
     * @param exchangeTime
     *            密钥交换时间
     * @return OltPonAttribute
     */
    OltPonAttribute setPonPortEncryptMode(SnmpParam snmpParam, Long ponIndex, Integer encryptMode, Integer exchangeTime);

    /**
     * PON口MAC最大学习数设置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param ponIndex
     *            PON口索引
     * @param maxLearnMacNum
     *            MAC最大学习数
     * @return Long
     */
    Long setPonMaxLearnMacNum(SnmpParam snmpParam, Long ponIndex, Long maxLearnMacNum);

    /**
     * PON口15分钟性能统计使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param ponIndex
     *            PON口索引
     * @param perfStatus
     *            PON口15分钟性能统计使能
     * @return Integer
     */
    Integer setPon15MinPerfStatus(SnmpParam snmpParam, Long ponIndex, Integer perfStatus);

    /**
     * PON口24小时性能统计使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param ponIndex
     *            PON口索引
     * @param perfStatus
     *            PON口24小时性能统计使能
     * @return Integer
     */
    Integer setPon24HourPerfStatus(SnmpParam snmpParam, Long ponIndex, Integer perfStatus);

    /**
     * 获取PON口广播风暴抑制信息
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltPonStormSuppressionEntry>
     */
    List<OltPonStormSuppressionEntry> getOltPonStormSuppressionEntries(SnmpParam snmpParam);

    /**
     * 设置PON口广播风暴
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param ponIndex
     *            PON索引
     * @return OltPonStormSuppressionEntry
     */
    OltPonStormSuppressionEntry setPonStormInfo(SnmpParam snmpParam,
            OltPonStormSuppressionEntry oltPonStormSuppressionEntry);

    /**
     * 设置PON口的限速管理
     * 
     * @param snmpParam
     * @param ponPortRateLimit
     */
    void setPonRateLimit(SnmpParam snmpParam, TopPonPortRateLimit ponPortRateLimit);

    /**
     * 设置PON口的工作模式
     * 
     * @param snmpParam
     * @param topPonPortSpeedEntry
     */
    void setPonPortSpeedMode(SnmpParam snmpParam, TopPonPortSpeedEntry topPonPortSpeedEntry);

    /**
     * 获得设备Pon口的属性
     * 
     * @param snmpParam
     *            snmpParam
     * @return OltPonAttribute
     */
    List<OltPonAttribute> getPonListAttribute(SnmpParam snmpParam);

    /**
     * 获得Pon口的属性
     * 
     * @param snmpParam
     *            snmpParam
     * @return OltPonAttribute
     */
    OltPonAttribute getPonAttribute(SnmpParam snmpParam, Long ponIndex);

    /**
     * 获取PON口速率
     * @param snmpParam
     * @return
     */
    List<TopPonPortSpeedEntry> getPonPortSpeed(SnmpParam snmpParam);

}
