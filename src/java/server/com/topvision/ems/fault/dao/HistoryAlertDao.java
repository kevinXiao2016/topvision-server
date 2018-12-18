package com.topvision.ems.fault.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.AlertTypeEx;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.fault.domain.LevelStat;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;

public interface HistoryAlertDao extends BaseEntityDao<HistoryAlert> {

    /**
     * 处理满足给定条件的历史告警.
     * 
     * @param handler
     *            MyResultHandler
     * @param map
     *            Map<String, String>
     */
    void handleHistoryAlert(MyResultHandler handler, Map<String, String> map);

    /**
     * 查询满足给定条件的历史告警.
     * 
     * @param p
     *            Page
     * @param map
     *            Map<String, String>
     * @return PageData
     */
    PageData<HistoryAlert> queryHistoryAlert(Page p, Map<String, String> map);

    /**
     * 按照告警分类进行统计.
     * 
     * @return List<AlertTypeEx>
     */
    List<AlertTypeEx> statHistoryAlertByCategory();

    /**
     * 按照级别统计历史告警数量.
     * 
     * @param map
     *            Map<String, String>
     * @return List<LevelStat>
     */
    List<LevelStat> statHistoryAlertByLevel(Map<String, String> map);

    /**
     * 获得设备历史告警列表
     * 
     * @param entityId
     * @param alertType
     * @param dataTime
     * @return
     */
    List<HistoryAlert> getEntityAvailability(Long entityId, String alertType);

    /**
     * 获得告警时长
     * 
     * @param alertId
     * @param firstTime
     * @param lastTime
     * @return
     */
    Long getAvailability(Long alertId, Timestamp startTime, Timestamp endTime);

    /**
     * 获取历史告警信息
     * 
     * @param alertId
     * @return
     */
    HistoryAlert getHistoryAlertByAlertId(Long alertId);

}
