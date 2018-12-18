/***********************************************************************
 * $Id: CmcAlertService.java,v1.0 2011-12-8 下午12:45:53 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.alert.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.service.Service;

/**
 * 告警功能
 * 
 * @author loyal
 * @created @2011-12-8-下午12:45:53
 * 
 */
public interface CmcAlertService extends Service {
    /**
     * 获取cc告警列表
     * 
     * @param map Map
     * @param start Integer
     * @param limit Integer
     * @return List<Alert>
     */
    @SuppressWarnings("rawtypes")
    List<Alert> getCmcAlertList(Map map, Integer start, Integer limit);

    /**
     * 获取cc告警条数
     * 
     * @param map Map
     * @return Integer
     */
    @SuppressWarnings("rawtypes")
    Integer getCmcAlertListNum(Map map);

    /**
     * 获取cc历史告警列表
     * 
     * @param map Map
     * @param start Integer
     * @param limit Integer
     * @return   List<HistoryAlert>
     */
    @SuppressWarnings("rawtypes")
    List<HistoryAlert> getCmcHistoryAlertList(Map map, Integer start, Integer limit);

    /**
     * 获取cc历史告警条数
     * 
     * @param map Map
     * @return Integer
     */
    @SuppressWarnings("rawtypes")
    Integer getCmcHistoryAlertListNum(Map map);

    /**
     * 获取事件等级控制列表
     * 
     * @param entityId  Long
     * @return List<DocsDevEvControl>
     */
    List<DocsDevEvControl> getdocsDevEvControlList(Long entityId);

    /**
     * 获取cm 阈值告警类型
     * @return
     */
    List<AlertType> getCmPollAlertType();

    /**
     * 获取cm阈值告警
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<Alert> getCurrentCmPollAlertList(Map<String, String> map, Integer start, Integer limit);

    /**
     * 获取cm历史阈值告警
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<HistoryAlert> getHisCmPollAlertList(Map<String, String> map, Integer start, Integer limit);

    /**
     * 获取cm阈值告警条数
     * 
     * @param map
     * @return
     */
    Integer getCurrentCmPollAlertNum(Map<String, String> map);

    /**
     * 获取cm阈值历史告警条数
     * 
     * @param map
     * @return
     */
    Integer getHisCmPollAlertListNum(Map<String, String> map);

    /**
     * 获得CC相关告警类型
     * 
     */
    List<AlertType> getCmcAlertTypes();
}
