/***********************************************************************
 * $Id: CmtsAlert.java,v1.0 2013-10-22 下午5:07:25 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2013-10-22-下午5:07:25
 * 
 */
public interface CmtsAlertService extends Service {
    /**
     * 获取cmts设备告警类型
     * 
     * @return
     */
    List<AlertType> getCmtsAlertType();

    /**
     * 获取cc告警列表
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<Alert> getCmtsAlertList(Map<String, Object> map, Integer start, Integer limit);

    /**
     * 获取cc告警条数
     * 
     * @param map
     * @return
     */
    Integer getCmtsAlertListNum(Map<String, Object> map);

    /**
     * 获取cc历史告警列表
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<HistoryAlert> getCmtsHistoryAlertList(Map<String, Object> map, Integer start, Integer limit);

    /**
     * 获取cc历史告警列表
     * 
     * @param map
     * @return
     */
    Integer getCmtsHistoryAlertListNum(Map<String, Object> map);

}
