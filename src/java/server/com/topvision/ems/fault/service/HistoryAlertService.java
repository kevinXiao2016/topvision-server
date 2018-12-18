package com.topvision.ems.fault.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.AlertTypeEx;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.Service;

/**
 * 历史告警处理.
 * 
 */
public interface HistoryAlertService extends Service {

    /**
     * 删除给定ID的历史告警.
     * 
     * @param alertIds
     */
    void deleteHistoryAlert(List<Long> alertIds);

    /**
     * 清空历史告警.
     * 
     * @param map
     */
    void emptyHistoryAlert();

    /**
     * 获取设备的历史告警. 查询条件: 设备ID:entityId, 设备地址:ip, 设备名称:name, 清除者:clearUser, 告警类型:typeId,
     * 起始时间:startTime, 结束时间:endTime, 描述(模糊匹配):message
     * 
     * @param map
     * @return PageData
     */
    PageData<HistoryAlert> getEntityHistoryAlert(Long entityId, Page p, Map<String, String> map);

    /**
     * 得到给定ID的历史告警.
     * 
     * @param alertId
     * @return HistoryAlert
     */
    HistoryAlert getHistoryAlert(Long alertId);

    /**
     * 处理满足给定条件的历史告警, 例如导出等, 减少调用查询方法返回List再循环.
     * 
     * @param handler
     * @param map
     */
    void handleHistoryAlert(MyResultHandler handler, Map<String, String> map);

    /**
     * 获取历史告警.
     * 
     * @param p
     * @param map
     * @return PageData
     */
    PageData<HistoryAlert> queryHistoryAlert(Page p, Map<String, String> map);

    /**
     * 根据告警的大分类统计告警数量.
     * 
     * @return List<AlertTypeEx>
     */
    List<AlertTypeEx> statHistoryAlertByCategory();

}
