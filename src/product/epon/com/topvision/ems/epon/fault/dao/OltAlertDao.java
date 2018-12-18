/***********************************************************************
 * $Id: OltAlertDao.java,v1.0 2013-10-26 上午09:53:24 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.dao;

import java.util.List;

import com.topvision.ems.epon.domain.OltAlertInstance;
import com.topvision.ems.epon.domain.OltAlertTop;
import com.topvision.ems.epon.fault.domain.OltTopAlarmCodeMask;
import com.topvision.ems.epon.fault.domain.OltTopAlarmInstanceMask;
import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:53:24
 *
 */
public interface OltAlertDao extends BaseEntityDao<Object> {
    
    /**
     * 获取设备侧告警类型屏蔽列表
     * 
     * @param entityId
     *            设备ID
     * @param type
     *            发布版本类型（1:内部使用/2:广电标准/3:电信标准）
     * @return List<OltTopAlarmCodeMask>
     */
    public List<OltTopAlarmCodeMask> getOltAlertCodeMask(Long entityId, Integer type);

    /**
     * 获取可以添加告警类型屏蔽列表
     * 
     * @param entityId
     * @param type
     *            发布版本类型（1:内部使用/2:广电标准/3:电信标准）
     * 
     * @return List<AlertType>
     */
    public List<AlertType> getOltAlertAvailableType(Long entityId, Integer type);

    /**
     * 添加设备侧告警类型屏蔽
     * 
     * @param oltTopAlarmCodeMask
     *            OLT告警类型屏蔽
     */
    public void insertOltAlertCodeMask(OltTopAlarmCodeMask oltTopAlarmCodeMask);

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
    public void insertOltAlertInstanceMask(OltTopAlarmInstanceMask oltTopAlarmInstanceMask);

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
    public void insertOltTrapConfig(OltTrapConfig oltTrapConfig);

    /**
     * 添加或更新Trap配置
     * 
     * @param oltTrapConfig
     *            Trap配置
     */
    public void insertOrUpdateOltTrapConfig(OltTrapConfig oltTrapConfig);

    /**
     * 删除Trap配置
     * 
     * @param oltTrapConfig
     *            Trap配置
     */
    public void deleteOltTrapConfig(OltTrapConfig oltTrapConfig);

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
     * @return
     */
    List<AlertType> getAlertType();

    /**
     * 将采集到的OLT告警类型屏蔽数据批量插入数据库
     * 
     * @param entityId
     *            设备ID
     * @param oltTopAlarmCodeMaskList
     *            OLT告警类型屏蔽
     */
    public void batchInsertOltAlertCodeMask(Long entityId, final List<OltTopAlarmCodeMask> oltTopAlarmCodeMaskList);

    /**
     * 将采集到的OLT告警实体屏蔽数据批量插入数据库
     * 
     * @param entityId
     *            设备ID
     * @param oltTopAlarmInstanceMaskList
     *            OLT告警实体屏蔽
     */
    public void batchInsertOltAlertInstanceMask(Long entityId,
            final List<OltTopAlarmInstanceMask> oltTopAlarmInstanceMaskList);

    /**
     * 将采集到的Trap配置数据批量插入数据库
     * 
     * @param entityId
     *            设备ID
     * @param oltTrapConfigList
     *            Trap配置
     */
    public void batchInsertOltTrapConfig(Long entityId, final List<OltTrapConfig> oltTrapConfigList);

    /**
     * 获取告警top 10
     * 
     * @return List<OltAlertTop>
     */
    List<OltAlertTop> getOltDeviceAlertTop();

}
