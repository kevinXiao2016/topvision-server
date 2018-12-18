package com.topvision.ems.fault.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.EventType;
import com.topvision.framework.dao.BaseEntityDao;

public interface EventTypeDao extends BaseEntityDao<EventType> {

    public List<EventType> selectEventByType(Map<String, String> filter);

    public List<EventType> getEventType(Map<String, String> filter);

    List<EventType> getAllEventType();

}
