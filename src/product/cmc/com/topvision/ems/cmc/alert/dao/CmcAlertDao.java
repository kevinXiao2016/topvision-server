/***********************************************************************
 * $Id: CmcAlertDao.java,v1.0 2011-12-8 下午03:40:15 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.alert.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2011-12-8-下午03:40:15
 * 
 */
public interface CmcAlertDao extends BaseEntityDao<Entity> {

    /**
     * @param cmcId
     * @return
     */
    @SuppressWarnings("rawtypes")
    List<Alert> getCmcAlertList(Map map, Integer start, Integer limit);

    /**
     * @param cmcId
     * @return
     */
    @SuppressWarnings("rawtypes")
    List<HistoryAlert> getCmcHistoryAlertList(Map map, Integer start, Integer limit);

    /**
     * 获取cc告警条数
     * 
     * @param cmcId
     * @return
     */
    @SuppressWarnings("rawtypes")
    Integer getCmcAlertListNum(Map map);

    /**
     * 获取cc历史告警条数
     * 
     * @param cmcId
     * @return
     */
    @SuppressWarnings("rawtypes")
    Integer getCmcHistoryAlertListNum(Map map);

    /**
     * 获取事件等级控制列表
     * 
     * @param entityId
     * @return
     */
    List<DocsDevEvControl> getdocsDevEvControlList(Long entityId);
    
    /**
     * 获取cmc mac
     * @param cmcIndex
     * @param entityId
     * @return
     */
    String getCmcMacByCmcIndexAndEntityId(Long cmcIndex, Long entityId);
    
    /**
     * 获取cm 阈值告警类型
     * @return
     */
    List<AlertType> selectCmPollAlertType();
    
    /**
     * 获取cm阈值告警
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<Alert> selectCurrentCmPollAlertList(Map<String, String> map, Integer start, Integer limit);
    
    /**
     * 获取cm历史阈值告警
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<HistoryAlert> selectHisCmPollAlertList(Map<String, String> map, Integer start, Integer limit);
    
    /**
     * 获取cm阈值告警条数
     * 
     * @param map
     * @return
     */
    Integer selectCurrentCmPollAlertNum(Map<String, String> map);

    /**
     * 获取cm阈值历史告警条数
     * 
     * @param map
     * @return
     */
    Integer selectHisCmPollAlertListNum(Map<String, String> map);
    
}
