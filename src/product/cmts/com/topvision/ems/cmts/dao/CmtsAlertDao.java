/***********************************************************************
 * $Id: CmtsAlertDao.java,v1.0 2013-10-22 下午6:46:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2013-10-22-下午6:46:20
 * 
 */
public interface CmtsAlertDao extends BaseEntityDao<Alert> {
    /**
     * 获取cmts设备告警类型
     * 
     * @return
     */
    List<AlertType> selectCmtsAlertType();

    /**
     * 获取cc告警列表
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<Alert> selectCmtsAlertList(Map<String, Object> map, Integer start, Integer limit);

    /**
     * 获取cc告警条数
     * 
     * @param map
     * @return
     */
    Integer selectCmtsAlertListNum(Map<String, Object> map);

    /**
     * 获取cc历史告警列表
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<HistoryAlert> selectCmtsHistoryAlertList(Map<String, Object> map, Integer start, Integer limit);

    /**
     * 获取cc历史告警列表
     * 
     * @param map
     * @return
     */
    Integer selectCmtsHistoryAlertListNum(Map<String, Object> map);
}
