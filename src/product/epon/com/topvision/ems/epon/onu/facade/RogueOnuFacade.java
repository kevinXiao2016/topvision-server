/***********************************************************************
 * $Id: RogueOnuFacade.java,v1.0 2017年6月17日 下午4:01:37 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.facade;

import java.util.List;

import com.topvision.ems.epon.onu.domain.TopOnuLaserEntry;
import com.topvision.ems.epon.onu.domain.TopPonPortRogueEntry;
import com.topvision.ems.epon.onu.domain.TopSystemRogueCheck;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2017年6月17日-下午4:01:37
 *
 */
@EngineFacade(serviceName = "RogueOnuFacade", beanName = "rogueOnuFacade")
public interface RogueOnuFacade extends Facade {

    /**
     * 获得OLT全局长发光ONU检测开关状态
     * 
     */
    Integer getOltRogueSwitch(SnmpParam snmpParam);

    /**
     * 设置OLT全局长发光ONU检测开关状态
     * 
     */
    void setOltRogueSwitch(SnmpParam snmpParam, TopSystemRogueCheck systemRogueCheck);

    /**
     * 获得PON端口长发光ONU信息
     * 
     */
    List<TopPonPortRogueEntry> getPonPortRogueOnuList(SnmpParam snmpParam);

    /**
     * 获得单个PON端口长发光ONU信息
     * 
     */
    TopPonPortRogueEntry getPonPortRogueOnuInfo(SnmpParam snmpParam, TopPonPortRogueEntry topPonPortRogueEntry);

    /**
     * 设置PON端口长发光检测开关
     * 
     */
    void setPonPortRogueSwitch(SnmpParam snmpParam, TopPonPortRogueEntry topPonPortRogueEntry);

    /**
     * 手动对PON端口长发光ONU进行check操作
     * 
     */
    void setPonPortRogueOnuCheck(SnmpParam snmpParam, TopPonPortRogueEntry topPonPortRogueEntry);

    /**
     * 获得OLT下ONU激光器状态
     * 
     */
    List<TopOnuLaserEntry> getOnuLaserEntry(SnmpParam snmpParam);

    /**
     * 设置ONU激光器开启和关闭
     * 
     */
    void setOnuLaserSwitch(SnmpParam snmpParam, TopOnuLaserEntry topOnuLaserEntry);

    /**
     * 获取单个ONU激光器状态信息
     * 
     */
    TopOnuLaserEntry getOnuLaserSwitch(SnmpParam snmpParam, TopOnuLaserEntry topOnuLaserEntry);

}
