/***********************************************************************
 * $Id: OltAlertService.java,v1.0 2013-10-26 上午09:51:21 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.service;

import java.util.List;

import com.topvision.ems.epon.domain.OltAlertInstance;
import com.topvision.ems.epon.domain.OltAlertTop;
import com.topvision.ems.epon.fault.domain.OltTopAlarmCodeMask;
import com.topvision.ems.epon.fault.domain.OltTopAlarmInstanceMask;
import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:51:21
 *
 */
public interface OltAlertService extends Service {
    /**
     * 获取设备侧告警类型屏蔽列表
     * 
     * @param entityId
     *            设备ID
     * @return List<OltTopAlarmCodeMask>
     */
    public List<OltTopAlarmCodeMask> getOltAlertCodeMask(Long entityId);

    /**
     * 获取可以添加告警类型屏蔽列表
     * 
     * @param entityId
     *            设备ID
     * 
     * @return List<AlertType>
     */
    public List<AlertType> getOltAlertAvailableType(Long entityId);

    /**
     * 添加设备侧告警类型屏蔽
     * 
     * @param oltTopAlarmCodeMask
     *            OLT告警类型屏蔽
     */
    public void addOltAlertCodeMask(OltTopAlarmCodeMask oltTopAlarmCodeMask);

    /**
     * 删除设备侧告警类型屏蔽
     * 
     * @param entityId
     *            设备ID
     * @param codeMaskIndex
     *            OLT告警屏蔽索引
     */
    public void deleteOltAlertCodeMask(Long entityId, Long codeMaskIndex);

    /**
     * 获取设备侧告警实体屏蔽列表
     * 
     * @param entityId
     *            设备ID
     * @return List<OltTopAlarmInstanceMask>
     */
    public List<OltTopAlarmInstanceMask> getOltAlertInstanceMask(Long entityId);

    /**
     * 获取OLT上可以进行告警屏蔽的实体
     * 
     * @param entityId
     *            设备ID
     * @return List<OltAlertInstance>
     */
    public List<OltAlertInstance> getOltAlertAvailableInstance(Long entityId);

    /**
     * 添加设备侧告警实体屏蔽
     * 
     * @param oltTopAlarmInstanceMask
     *            OLT告警实体屏蔽
     */
    public void addOltAlertInstanceMask(OltTopAlarmInstanceMask oltTopAlarmInstanceMask);

    /**
     * 删除设备侧告警实体屏蔽
     * 
     * @param entityId
     *            设备ID
     * @param instanceMaskIndex
     *            OLT告警屏蔽索引
     */
    public void deleteOltAlertInstanceMask(Long entityId, Long instanceMaskIndex);

    /**
     * 从设备刷新告警屏蔽信息
     * 
     * @param entityId
     *            设备ID
     */
    public void refreshMaskDataFromFacility(Long entityId);

    /**
     * 获取Trap配置
     * 
     * @param entityId
     *            设备ID
     * @return List<OltTrapConfig>
     */
    public List<OltTrapConfig> getOltTrapConfig(Long entityId);

    /**
     * 添加Trap配置
     * 
     * @param oltTrapConfig
     *            Trap配置
     */
    public void addOltTrapConfig(OltTrapConfig oltTrapConfig);

    /**
     * 删除Trap配置
     * 
     * @param entityId
     *            设备ID
     * @param trapServerIp
     *            Trap服务器IP
     * @param trapPort
     *            端口号
     */
    public void deleteOltTrapConfig(Long entityId, String trapNameString);

    /**
     * 从设备刷新Trap配置信息
     * 
     * @param entityId
     *            设备ID
     */
    public void refreshTrapConfigFromFacility(Long entityId);

    /**
     * 获取可用设备IP
     * 
     * @return List<String>
     */
    List<String> getAvailableEntityIp();

    /**
     * 获取告警等级
     * 
     * @return List<Level>
     */
    List<Level> getAlertSeverity();

    /**
     * 获取告警类型
     * 
     * @return List<AlertType>
     */
    List<AlertType> getAlertType();

    /**
     * 获取告警top 10
     * 
     * @return List<OltAlertTop>
     */
    List<OltAlertTop> getOltDeviceAlertTop();

    /**
     * 获得系统目前使用的告警类型,然后赋值告警CODE
     * 
     * @param snmpParam
     * @return
     */
    Integer getTopEponAlarmMode(SnmpParam snmpParam);

    /**
     * 设置Trap参数
     * 
     * @param entityId
     *            设备ID
     */
    void setOltTrapConfig(Long entityId);

}
