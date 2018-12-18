/***********************************************************************
 * $Id: OltAlertFacade.java,v1.0 2013-10-26 上午10:11:24 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.facade;

import java.util.List;

import com.topvision.ems.epon.fault.domain.EponEventLog;
import com.topvision.ems.epon.fault.domain.OltTopAlarmCodeMask;
import com.topvision.ems.epon.fault.domain.OltTopAlarmInstanceMask;
import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2013-10-26-上午10:11:24
 *
 */
@EngineFacade(serviceName = "OltAlertFacade", beanName = "oltAlertFacade")
public interface OltAlertFacade extends Facade {

    /**
     * 获取OLT告警类型屏蔽数据
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltTopAlarmCodeMask>
     */
    public List<OltTopAlarmCodeMask> getOltAlertCodeMask(SnmpParam snmpParam);

    /**
     * 添加设备侧告警类型屏蔽
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltTopAlarmCodeMask
     *            OLT告警类型屏蔽
     * @return OltTopAlarmCodeMask
     */
    public OltTopAlarmCodeMask addOltAlertCodeMask(SnmpParam snmpParam, OltTopAlarmCodeMask oltTopAlarmCodeMask);

    /**
     * 删除设备侧告警类型屏蔽
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltTopAlarmCodeMask
     *            OLT告警类型屏蔽
     */
    public void deleteOltAlertCodeMask(SnmpParam snmpParam, OltTopAlarmCodeMask oltTopAlarmCodeMask);

    /**
     * 获取OLT告警实体屏蔽数据
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltTopAlarmInstanceMask>
     */
    public List<OltTopAlarmInstanceMask> getOltAlertInstanceMask(SnmpParam snmpParam);

    /**
     * 添加设备侧告警实体屏蔽
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltTopAlarmInstanceMask
     *            OLT告警实体屏蔽
     * @return OltTopAlarmInstanceMask
     */
    public OltTopAlarmInstanceMask addOltAlertInstanceMask(SnmpParam snmpParam,
            OltTopAlarmInstanceMask oltTopAlarmInstanceMask);

    /**
     * 删除设备侧告警实体屏蔽
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltTopAlarmInstanceMask
     *            OLT告警实体屏蔽
     */
    public void deleteOltAlertInstanceMask(SnmpParam snmpParam, OltTopAlarmInstanceMask oltTopAlarmInstanceMask);

    /**
     * 获取Trap配置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltTrapConfig>
     */
    public List<OltTrapConfig> getOltTrapConfig(SnmpParam snmpParam);

    /**
     * 添加Trap配置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltTrapConfig
     *            Trap配置参数
     * @return OltTrapConfig
     */
    public OltTrapConfig addOltTrapConfig(SnmpParam snmpParam, OltTrapConfig oltTrapConfig);

    /**
     * 删除Trap配置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltTrapConfig
     *            Trap配置参数
     */
    public void deleteOltTrapConfig(SnmpParam snmpParam, OltTrapConfig oltTrapConfig);

    /**
     * 获得系统的告警类型
     * 
     * @param snmpParam
     */
    public Integer getTopEponAlarmMode(SnmpParam snmpParam);

    public void setTopEponAlarmMode(SnmpParam snmpParam, Integer code);

    /**
     * 获得当前设备的告警信息
     * 
     * @param snmpParam
     * @return
     */
    public List<EponEventLog> getEponEventLogs(SnmpParam snmpParam);

}
