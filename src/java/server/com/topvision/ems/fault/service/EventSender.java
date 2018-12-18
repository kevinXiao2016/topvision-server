package com.topvision.ems.fault.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.framework.service.BaseService;

public class EventSender extends BaseService implements Runnable {
    public static EventSender getInstance() {
        if (sender == null) {
            sender = new EventSender();
            // Modify by Rod 手动调用init
            sender.init();
        }
        return sender;
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static EventSender sender = null;
    private EventService eventService = null;
    private AlertService alertService = null;
    private LinkedBlockingQueue<Object> queue;
    private ExecutorService executorService;

    private EventSender() {

    }

    /**
     * 
     * @param source
     * @return Alert
     */
    public Alert createAlert(Integer typeId, String host, String source) {
        Alert alert = new Alert();
        alert.setFirstTime(new Timestamp(System.currentTimeMillis()));
        alert.setTypeId(typeId);
        String name = alertService.getEntityNameByIp(host);
        if (name != null) {
            alert.setHost(host + "[" + name + "]");
        } else {
            alert.setHost(host);
        }
        alert.setSource(source);
        return alert;
    }

    /**
     * 
     * @param source
     * @return Event
     */
    public Event createEvent(Integer typeId, String host, String source) {
        Event event = new Event();
        // 在初始化Event设置名称
        List<EventType> eventTypes = getEventTypeByTypeId(typeId);
        for (EventType eventType : eventTypes) {
            event.setName(eventType.getDisplayName());
        }
        event.setCreateTime(new Timestamp(System.currentTimeMillis()));
        event.setTypeId(typeId);
        event.setIp(host);
        String name = alertService.getEntityNameByIp(host);
        if (name != null) {
            event.setHost(host + "[" + name + "]");
        } else {
            event.setHost(host);
        }
        event.setSource(source);
        return event;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("EventSender destroy...");
        }
        executorService.shutdownNow();
    }

    /**
     * 
     * @return AlertService
     */
    public AlertService getAlertService() {
        return alertService;
    }

    /**
     * @return the eventService
     */
    public EventService getEventService() {
        return eventService;
    }

    /**
     * Modify by Rod 
     * 
     * 添加baseService的   @PostConstruct 标签后，会导致单例模式失效
     */
    public void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("EventSender initialize...");
        }
        queue = new LinkedBlockingQueue<Object>();
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(sender);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        Thread.currentThread().setName("EventSender");
        while (queue != null) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Event consume thread:queue.size={}", queue.size());
                }
                Object obj = queue.take();
                if (obj instanceof Event) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("EventSender consume an event:{}", obj);
                    }
                    eventService.insertEvent((Event) obj);
                } else if (obj instanceof Alert) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("EventSender consume an alert:{}", obj);
                    }
                    alertService.insertAlert((Alert) obj);
                }
            } catch (InterruptedException e) {
            } catch (Exception e) {
                logger.error("event/alert queue error", e);
            }
        }
    }

    public void sendAlert(Alert alert) {
        if (logger.isDebugEnabled()) {
            logger.debug("EventSender.sendAlert:{}", alert);
        }
        try {
            queue.put(alert);
        } catch (InterruptedException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("send alert error." + e.getMessage(), e);
            }
        }

    }

    public void send(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("send event:{}", event);
        }
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("send event error." + e.getMessage(), e);
            }
        }
    }

    public void send(List<Event> events) {
        if (logger.isDebugEnabled()) {
            logger.debug("send events:{}", events);
        }
        try {
            for (Event event : events) {
                queue.put(event);
            }
        } catch (InterruptedException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("send event error." + e.getMessage(), e);
            }
        }
    }

    /**
     * 
     * @param alertService
     */
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * @param eventService
     *            the eventService to set
     */
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    private List<EventType> getEventTypeByTypeId(Integer typeId) {
        List<EventType> types = new ArrayList<EventType>();
        List<EventType> eventTypes = eventService.getAllEventType();
        for (EventType eventType : eventTypes) {
            if (eventType.getTypeId().equals(typeId)) {
                types.add(eventType);
            }
        }
        return types;
    }

    /**
     * @return the queue
     */
    public LinkedBlockingQueue<Object> getQueue() {
        return queue;
    }
}
