package com.topvision.ems.fault.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.dao.AlertTypeDao;
import com.topvision.ems.fault.dao.EventDao;
import com.topvision.ems.fault.dao.EventTypeDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.EventService;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.util.CurrentRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Service("eventService")
public class EventServiceImpl extends BaseService implements EventService {
    @Autowired
    private EventDao eventDao;
    @Autowired
    private EntityAddressDao entityAddressDao;
    @Autowired
    private EventTypeDao eventTypeDao;
    @Autowired
    private AlertTypeDao alertTypeDao;
    @Autowired
    private AlertService alertService;
    private List<EventType> eventTypes;
    private JSONArray jsonEventType = null;
    /**
     * 事件解析器注册列表
     */
    private final List<EventParser> eventParsers = new ArrayList<EventParser>();

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        eventTypes = new ArrayList<EventType>();
        reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#getAllEventType()
     */
    @Override
    public List<EventType> getAllEventType() {
        return eventTypes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#getEventList(java.lang.Long,
     * com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Event> getEventList(Long typeId, Page p) {
        if (eventDao == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("typeId", String.valueOf(typeId));
        map.put("startTypeId", String.valueOf(typeId * 10000));
        map.put("endTypeId", String.valueOf((typeId + 1) * 10000));
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return eventDao.selectByMap(map, p);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#getEventList(com.topvision
     * .framework.domain. Page)
     */
    @Override
    public PageData<Event> getEventList(Page p) {
        if (eventDao == null) {
            return null;
        }
        Map<String, String> filter = new HashMap<String, String>();
        return eventDao.selectByMap(filter, p);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#getEventsOfAlert(java.lang .Long)
     */
    @Override
    public List<Long> getEventsOfAlert(Long alertId) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#getEventsOfAlertType(java .lang.Integer)
     */
    @Override
    public List<Long> getEventsOfAlertType(Integer typeId) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#insertEvent(com.topvision
     * .ems.fault.domain.Event )
     */
    @Override
    public void insertEvent(Event event) {
        for (EventParser eventParser : eventParsers) {
            if (eventParser.parse(event)) {
                break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#reset()
     */
    @Override
    public void reset() {
        try {
            eventTypes.clear();
            eventTypes = eventTypeDao.getAllEventType();
        } catch (DataAccessException e) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("reset.", e);
            }
        }
    }

    /**
     * 
     * @param event
     * @return
     * @throws Exception
     */
    public Alert transform(Event event) {
        Alert alert = new Alert();
        alert.setFirstTime(event.getCreateTime());
        alert.setLastTime(event.getCreateTime());
        alert.setMessage(event.getMessage());
        alert.setMonitorId(event.getMonitorId());
        alert.setEntityId(event.getEntityId());
        alert.setHost(event.getHost());
        alert.setSource(event.getSource());
        alert.setUserObject(event.getUserObject());
        return alert;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#clearEvent(java.util.List)
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void clearEvent(Map map) {
        eventDao.deleteByMap(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#loadJSONEventType()
     */
    @Override
    public JSONArray loadJSONEventType() throws JSONException {
        if (jsonEventType == null) {
            jsonEventType = new JSONArray();
            List<EventType> list = getAllEventType();
            HashMap<String, JSONObject> map = new HashMap<String, JSONObject>(list == null ? 0 : list.size());
            JSONObject json;
            EventType type;
            for (int i = 0; list != null && i < list.size(); i++) {
                type = list.get(i);
                if (Integer.parseInt(type.getParentId()) != 0) {
                    continue;
                }
                json = new JSONObject();
                json.put("text", type.getDisplayName());
                json.put("expanded", true);
                json.put("id", String.valueOf(type.getTypeId()));
                json.put("children", new JSONArray());
                json.put("superiorId", type.getParentId());
                json.put("iconCls", "alertFolderIcon");
                map.put(String.valueOf(type.getTypeId()), json);
                jsonEventType.add(json);
            }
            JSONObject parent;
            for (int i = 0; list != null && i < list.size(); i++) {
                type = list.get(i);
                if (Integer.parseInt(type.getParentId()) == 0) {
                    continue;
                }
                json = new JSONObject();
                json.put("text", type.getDisplayName());
                json.put("expanded", true);
                json.put("id", String.valueOf(type.getTypeId()));
                json.put("children", new JSONArray());
                json.put("superiorId", type.getParentId());
                json.put("typeId", type.getTypeId());
                map.put(String.valueOf(type.getTypeId()), json);
                parent = map.get(String.valueOf(type.getParentId()));
                if (parent == null) {
                    jsonEventType.add(json);
                } else {
                    parent.getJSONArray("children").add(json);
                }
            }
        }
        return jsonEventType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#loadEventTypeToJson()
     */
    @Override
    public List<EventType> loadEventTypeToJson() throws JSONException {
        // UserContext uc = (UserContext)
        // ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        // ResourceManager resourceManager =
        // ResourceManager.getResourceManager("com.topvision.ems.epon.resources",
        // uc.getUser().getLanguage());
        // JSONArray jsonEventTypeList = new JSONArray();
        Map<String, String> filter = new HashMap<String, String>();
        List<EventType> list = eventTypeDao.getEventType(filter);
        //
        // for (EventType event : list) {
        // event.setDisplayName(resourceManager.getString(event.getDisplayName()));
        // }
        //
        // HashMap<String, JSONObject> map = new HashMap<String, JSONObject>(list == null ? 0 :
        // list.size());
        // JSONObject json;
        // EventType type;
        // for (int i = 0; list != null && i < list.size(); i++) {
        // type = list.get(i);
        // if (Integer.parseInt(type.getParentId()) != 0) {
        // continue;
        // }
        // json = new JSONObject();
        // json.put("text", type.getDisplayName());
        // json.put("expanded", false);
        // json.put("id", String.valueOf(type.getTypeId()));
        // json.put("children", new JSONArray());
        // json.put("superiorId", type.getParentId());
        // json.put("iconCls", "alertFolderIcon");
        // map.put(String.valueOf(type.getTypeId()), json);
        // jsonEventTypeList.put(json);
        // }
        // JSONObject parent;
        // for (int i = 0; list != null && i < list.size(); i++) {
        // type = list.get(i);
        // if (Integer.parseInt(type.getParentId()) == 0) {
        // continue;
        // }
        // json = new JSONObject();
        // json.put("text", type.getDisplayName());
        // json.put("expanded", true);
        // json.put("id", String.valueOf(type.getTypeId()));
        // // CC事件分为三级树，设置第二级树图标
        // if (type.getTypeId() < -7 && type.getTypeId() > -18) {
        // json.put("iconCls", "alertFolderIcon");
        // json.put("expanded", false);
        // }
        // json.put("children", new JSONArray());
        // json.put("superiorId", type.getParentId());
        // json.put("typeId", type.getTypeId());
        // map.put(String.valueOf(type.getTypeId()), json);
        // parent = map.get(String.valueOf(type.getParentId()));
        // if (parent == null) {
        // jsonEventTypeList.put(json);
        // } else {
        // parent.getJSONArray("children").put(json);
        // parent.put("iconCls", "alertFolderIcon");
        // parent.put("expanded", false);
        // }
        // }
        return list;
    }

    /**
     * 注册新的事件处理模型
     * 
     * @param eventParser
     *            处理模型
     */
    @Override
    public void registEventParser(EventParser eventParser) {
        eventParsers.add(eventParser);
        // 根据优先级对解析器进行排序
        Collections.sort(eventParsers);
    }

    /**
     * 注销一个事件处理模型
     * 
     * @param eventParser
     *            处理模型
     */
    @Override
    public void unRegistEventParser(EventParser eventParser) {
        eventParsers.remove(eventParser);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#queryEventList(java.util.Map)
     */
    @Override
    public List<Event> queryEventList(Map<String, String> map) {
        return eventDao.queryEventList(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#getEventType()
     */
    @Override
    public List<AlertType> getEventType() {
        return eventDao.getEventType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#queryEventListSize(java.util.Map)
     */
    @Override
    public int queryEventListSize(Map<String, String> map) {
        return eventDao.queryEventListSize(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.EventService#deleteEvent(int)
     */
    @Override
    public void deleteEvent(int eventId) {
        eventDao.deleteEvent(eventId);
    }

    @Override
    public void batchDeleteEvent(String eventIds) {
        eventDao.batchDeleteEvent(eventIds);
    }

    /**
     * @return the eventDao
     */
    public EventDao getEventDao() {
        return eventDao;
    }

    /**
     * @param eventDao
     *            the eventDao to set
     */
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    /**
     * @return the entityAddressDao
     */
    public EntityAddressDao getEntityAddressDao() {
        return entityAddressDao;
    }

    /**
     * @param entityAddressDao
     *            the entityAddressDao to set
     */
    public void setEntityAddressDao(EntityAddressDao entityAddressDao) {
        this.entityAddressDao = entityAddressDao;
    }

    /**
     * @return the eventTypeDao
     */
    public EventTypeDao getEventTypeDao() {
        return eventTypeDao;
    }

    /**
     * @param eventTypeDao
     *            the eventTypeDao to set
     */
    public void setEventTypeDao(EventTypeDao eventTypeDao) {
        this.eventTypeDao = eventTypeDao;
    }

    /**
     * @return the alertTypeDao
     */
    public AlertTypeDao getAlertTypeDao() {
        return alertTypeDao;
    }

    /**
     * @param alertTypeDao
     *            the alertTypeDao to set
     */
    public void setAlertTypeDao(AlertTypeDao alertTypeDao) {
        this.alertTypeDao = alertTypeDao;
    }

    /**
     * @return the alertService
     */
    public AlertService getAlertService() {
        return alertService;
    }

    /**
     * @param alertService
     *            the alertService to set
     */
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * @return the eventTypes
     */
    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    /**
     * @param eventTypes
     *            the eventTypes to set
     */
    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

}
