package com.topvision.ems.fault.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.framework.dao.BaseEntityDao;

public interface EventDao extends BaseEntityDao<Event> {

    Boolean isAlertClearByEvent(Event event);

    List<Event> queryEventList(Map<String, String> map);

    List<AlertType> getEventType();

    int queryEventListSize(Map<String, String> map);

    void deleteEvent(int eventId);

    /**
     * 批量删除事件
     * @param eventIds
     */
    void batchDeleteEvent(String eventIds);
}
