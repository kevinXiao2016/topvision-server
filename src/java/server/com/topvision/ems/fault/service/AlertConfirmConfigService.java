/***********************************************************************
 * $Id: AlertConfirmConfigService.java,v1.0 2012-3-27 下午05:23:41 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.service;

import java.util.List;

import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event2Alert;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.framework.service.Service;

/**
 * @author huqiao
 * @created @2012-3-27-下午05:23:41
 * 
 */
public interface AlertConfirmConfigService extends Service {

    /**
     * 获得某告警的所有清除告警事件信息
     * 
     * @param alertTypeId
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
     * 获得某告警的清除事件
     * 
     * @param eventTypeId
     * @param alertTypeId
     * @return
     */
    Event2Alert getEvent2AlertById(Integer eventTypeId, Integer alertTypeId);

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
     * 插入新的告警类型
     * 
     * @param alertType
     */
    void insertAlertType(AlertType alertType);

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
