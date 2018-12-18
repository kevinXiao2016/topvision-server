package com.topvision.ems.fault.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.service.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

public interface EventService extends Service {

    /**
     * 获得所有事件类型
     * 
     * @return List<EventType>
     */
    List<EventType> getAllEventType();

    /**
     * 获得符合条件的事件分页列表
     * 
     * @param typeId
     * @param extPage
     * @return PageData
     */
    PageData<Event> getEventList(Long typeId, Page extPage);

    /**
     * 获得所有事件分页列表
     * 
     * @param p
     * @return PageData
     */
    PageData<Event> getEventList(Page p);

    /**
     * 获得给定告警的所有事件ID
     * 
     * @param alertId
     * @return List<Long>
     */
    List<Long> getEventsOfAlert(Long alertId);

    /**
     * 获得给定类型的所有事件ID
     * 
     * @param typeId
     * @return List<Long>
     */
    List<Long> getEventsOfAlertType(Integer typeId);

    /**
     * 插入事件
     * 
     * @param event
     */
    void insertEvent(Event event);

    /**
     * 删除事件
     * 
     * @param eventIdIds
     */
    @SuppressWarnings("rawtypes")
    void clearEvent(Map map);

    /**
     * 获取所有事件Json数据类型
     * 
     * @return
     */
    JSONArray loadJSONEventType() throws JSONException;

    /**
     * 获取所有事件类型Json数据类型
     * 
     * @return
     */
    List<EventType> loadEventTypeToJson() throws JSONException;

    void reset();

    /**
     * 注册新的事件处理模型
     * 
     * @param eventParser
     *            处理模型
     */
    void registEventParser(EventParser eventParser);

    /**
     * 注销一个事件处理模型
     * 
     * @param eventParser
     *            处理模型
     */
    void unRegistEventParser(EventParser eventParser);

    /**
     * 获得事件列表
     * 
     * @param map
     * @return
     */
    List<Event> queryEventList(Map<String, String> map);

    /**
     * 获得事件类型列表
     * 
     * @return
     */
    List<AlertType> getEventType();

    /**
     * 获得事件数量
     * 
     * @return
     */
    int queryEventListSize(Map<String, String> map);

    /**
     * 删除事件
     * 
     * @param eventId
     */
    void deleteEvent(int eventId);

    /**
     * 批量删除事件
     * 
     * @param eventIds
     */
    void batchDeleteEvent(String eventIds);
}
