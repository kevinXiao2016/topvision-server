/***********************************************************************
 * $Id: OltControlFacade.java,v1.0 2013-10-25 上午10:42:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.facade;

import java.util.List;

import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotMapTable;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午10:42:05
 *
 */
@EngineFacade(serviceName = "OltSlotFacade", beanName = "oltSlotFacade")
public interface OltSlotFacade extends Facade {

    /**
     * 获取MPU板卡状态
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param slotIndex
     *            槽位索引
     * @return OltSlotAttribute
     */
    OltSlotAttribute getMpuSlotInfo(SnmpParam snmpParam, Long slotIndex, Long slotNo);

    /**
     * 获取设备上板卡预配置类型
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param slotIndex
     *            板卡索引
     * @return Integer
     */
    Integer getSlotPreConfigType(SnmpParam snmpParam, Long slotIndex);

    /**
     * 槽位预配置板卡类型设置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param slotIndex
     *            槽位索引
     * @param preConfigType
     *            预配置类型
     * @return Integer
     */
    Integer setSlotPreConfigType(SnmpParam snmpParam, Long slotIndex, Integer preConfigType);

    /**
     * 板卡复位
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param slotNo
     *            板卡索引
     */
    void resetOltBoard(SnmpParam snmpParam, Long slotNo);

    /**
     * 风扇风速调节
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param fanCardIndex
     *            风扇索引
     * @param fanSpeedLevel
     *            风速等级
     * @return Integer
     */
    Integer setOltFanSpeedControl(SnmpParam snmpParam, Long fanCardIndex, Integer fanSpeedLevel);

    /**
     * 修改板卡温度探测使能
     * 
     * @param snmpParam
     * @param slotIndex
     * @param topSysBdTempDetectEnable
     * @return
     */
    Integer setSlotBdTempDetectEnable(SnmpParam snmpParam, Long slotIndex, Integer topSysBdTempDetectEnable);

    /**
    * 获取板卡温度
    * 
    * @param snmpParam
    * @param slotNo
    * @return
    */
    Integer getBdTemperature(SnmpParam snmpParam, Long slotNo);

    /**
     * 获取风扇属性
     * 
     * @param snmpParam
     * @param fanIndex
     * @return
     */
    OltFanStatus getOltFanStatus(SnmpParam snmpParam, Long fanIndex);

    /**
     * 修改板卡adminStatus状态
     * 
     * @param snmpParam
     * @param slotIndex
     * @param boardAdminStatus
     * @return
     */
    Integer setSlotBdAdminStatus(SnmpParam snmpParam, Long slotIndex, Integer boardAdminStatus);

    /**
     * 获取OLT物理槽位与逻辑槽位的对应关系
     * @param snmpParam
     * @return
     */
    List<OltSlotMapTable> getOltSlotMapTable(SnmpParam snmpParam);

    Integer getBdLampStatus(SnmpParam snmpParam, Long slotIndex);

    OltSlotStatus getOltSlotStatus(SnmpParam snmpParam, Long slotIndex);

}
