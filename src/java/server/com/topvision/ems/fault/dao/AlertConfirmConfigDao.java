/***********************************************************************
 * $Id: AlertConfirmConfigDao.java,v1.0 2012-3-27 下午06:49:51 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.dao;

import java.util.List;

import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author huqiao
 * @created @2012-3-27-下午06:49:51
 * 
 */
public interface AlertConfirmConfigDao extends BaseEntityDao<AlertType> {
    /**
     * 获得某告警的所有清除告警事件信息
     * 
     * @param eventTypeId
     * @return
     */
    List<EventType> getConfirmEventByEventTypeId(Integer alertTypeId);

    /**
     * 获得某告警的所有产生告警事件信息
     * 
     * @param alertTypeId
     * @return
     */
    List<EventType> getOriginEventByEventTypeId(Integer alertTypeId);

    /**
     * 插入某一条告警清除规则
     * 
     * @param alert
     * @param confirmAlert
     */
    void insertConfirmAlert(Integer alertId, Integer confirmAlertId);

    /**
     * 删除某一条告警清除规则
     * 
     * @param alert
     * @param confirmAlert
     */
    void deleteConfirmAlert(Integer alertId, Integer confirmAlertId);

    /**
     * 获得所有可以用来作为清除事件的事件
     * 
     * @return
     */
    List<EventType> getAllEventTypeNoAlert();

    /**
     * 插入新的告警事件对应关系
     * 
     * @param eventTypeId
     * @param alertTypeId
     */
    void insertOriginAlert(Integer eventTypeId, Integer alertTypeId);

    /**
     * 删除告警事件对应关系
     * 
     * @param eventTypeId
     * @param alertTypeId
     */
    void deleteOriginAlert(Integer eventTypeId, Integer alertTypeId);
    
    /**
     * 获得告警确认全局配置
     * 
     * @return
     */
    Boolean getAlertConfirmConfig();
    
    /**
     * 修改告警确认全局配置
     * 
     * @param alertConfirmConfig
     */
    void updateAlertConfirmConfig(Boolean alertConfirmConfig);

}
