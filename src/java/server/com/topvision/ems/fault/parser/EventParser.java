/***********************************************************************
 * $ com.topvision.ems.fault.parser.EventParser,v1.0 2012-1-13 8:53:14 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.parser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.dao.EventDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.EventService;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;

/**
 * 事件解析器接口抽象类，系统内对EVENT的默认处理都封装在这里
 * 
 * @author Administrator
 * @created @2012-1-13-8:53:14
 */
public abstract class EventParser extends BaseService implements Comparable<EventParser> {
    @Autowired
    protected EntityAddressDao entityAddressDao;
    @Autowired
    private EventService eventService;
    @Autowired
    protected EventDao eventDao;
    @Autowired
    protected AlertService alertService;
    @Autowired
    protected EntityService entityService;
    /**
     * 解析器优先级，保证解析器按照一定的顺序执行ru解析
     */
    private int cos = 0;

    /**
     * 初始化一个EventParser
     */
    @Override
    public abstract void initialize();

    /**
     * 销毁一个EventParser
     */
    @Override
    public abstract void destroy();

    /**
     * 处理一个Event
     * 
     * @param event
     * @return 是否能够处理该Event 能处理返回true 不能处理返回false
     */
    public abstract boolean parse(Event event);

    /**
     * 一个正常Event处理需要经过的步骤
     * 
     * @param event
     */
    public void doEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin doEvent: {}", event);
        }
        List<EventType> eventTypes = getEventTypeByTypeId(event.getTypeId());
        if (logger.isDebugEnabled()) {
            logger.debug("after getEventTypeByTypeId using typeId: {}", event.getTypeId());
        }
        if (eventTypes.size() == 0) {
            return;
        }
        AlertType alertType = null;
        for (EventType eventType : eventTypes) {
            event.setName(eventType.getDisplayName());
            if (eventType.getAlertTypeId() == null) {
                // 进入此处，表示该事件不存在关联告警，此处只可能对应一条eventType，用break或者continue均可
                continue;
            } else {
                alertType = getAlertTypeByTypeId(eventType.getAlertTypeId());
                if (logger.isDebugEnabled()) {
                    logger.debug("after getAlertTypeByTypeId using alertTypeId: {} ", eventType.getAlertTypeId());
                }
            }
            if (alertType == null) {
                continue;
            }
            if (!alertType.getActive()) {
                continue;
            }
            if (entityAddressDao != null && event.getEntityId() == null) {
                EntityAddress ea = entityAddressDao.selectByAddress(event.getHost());
                if (ea != null) {
                    event.setEntityId(ea.getEntityId());
                    event.setParentId(ea.getEntityId());
                }
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("eventTypeId", event.getTypeId().toString());
            map.put("typeId", String.valueOf(alertType.getTypeId()));
            map.put("source", event.getSource());
            if (event.getEntityId() != null) {
                map.put("entityId", String.valueOf(event.getEntityId()));
            } else {
                map.put("host", event.getHost());
            }
            if (event.getMonitorId() != null) {
                map.put("monitorId", String.valueOf(event.getMonitorId()));
            }
            // add by fanzidong,ONU上线可能需要清除CMTS解绑定告警（CMTS解绑定后从网管删除，不能在trapparser中正确判断类型，转换告警）
            if (event.getTypeId().equals(16434) && alertType.getTypeId().equals(1016439)) {
                try {
                    // 转换source
                    String source = event.getSource();
                    source = source.replaceFirst("ONU", "CMTS");
                    // 转换message，获取mac
                    String message = event.getOriginMessage();
                    message = message.replaceAll("MAC:", "").trim();

                    map.put("source", source);
                    map.put("messageLike", message);
                } catch (Exception e) {
                    logger.error(
                            "[doEvent] [convert some clear alert message error] happens. [{}]. [Probably need to check it!] [typeId: {}, source: {}].",
                            e.getMessage(), event.getTypeId(), event.getSource());
                }
            }
            // List<Alert> list = alertService.selectByMap(map);
            List<Alert> list = selectCurrentAlert(map);
            Alert oldAlert = null;
            if (list != null && !list.isEmpty()) {
                oldAlert = list.get(0);
            }
            Alert alert = null;
            if (oldAlert != null && event.isClear() != null && event.isClear()) {
                // add by fanzidong @20170621，增加时间戳判断，只能清除之前的告警
                if (event.getCreateTime() == null
                        || event.getCreateTime().getTime() <= oldAlert.getLastTime().getTime()) {
                    // 记录日志
                    if (event.getCreateTime() != null) {
                        logger.error(
                                "[doEvent] [cannot clear alert which happentime after it] happens. [event.message: {}, event.createTime: {}, oldAlert.message: {}, oldAlert.lastTime: {}].",
                                event.getMessage(), event.getCreateTime().toString(), oldAlert.getMessage(),
                                oldAlert.getLastTime().toString());
                    }
                } else {
                    if (event.getClearLevelId() != null) {
                        // Perf Event Clear
                        int clearLevelId = event.getClearLevelId();
                        int oldLevelId = oldAlert.getLevelId();
                        if (clearLevelId <= oldLevelId) {
                            alert = oldAlert;
                            alert.setIp(event.getIp());
                            alert.setClearTime(event.getCreateTime());
                            logger.debug("before txClearAlert: " + alert);
                            alertService.txClearAlert(alert, event.getMessage());
                            logger.debug("after txClearAlert: " + alert);
                        }
                    } else {
                        // clear
                        alert = oldAlert;
                        alert.setIp(event.getIp());
                        alert.setClearTime(event.getCreateTime());
                        logger.debug("before txClearAlert: " + alert);
                        alertService.txClearAlert(alert, event.getMessage());
                        logger.debug("after txClearAlert: " + alert);
                    }
                }

            } else if (oldAlert != null) {
                // Modify by Rod 区别设备告警和性能阈值告警
                if (event.getLevelId() == null) {
                    alert = oldAlert;
                    alert.happenedAgain();
                    // 告警等级提升,改变告警等级
                    if (alertType.getSmartUpdate()) {
                        if (alert.getHappenTimes() >= Integer.parseInt(alertType.getAlertTimes())) {
                            alert.setLevelId(Byte.parseByte((alertType.getUpdateLevel().toString())));
                        }
                    } else {
                        // 网管可能改变了告警等级,每次都与网管等级保持一致
                        alert.setLevelId(alertType.getLevelId());
                    }
                    alert.setLastTime(event.getCreateTime());
                    logger.debug("before updateAlert: " + alert);
                    alertService.updateAlert(alert);
                    logger.debug("after updateAlert: " + alert);
                } else {
                    // 性能阈值告警
                    int level = event.getLevelId();
                    int oldLevel = oldAlert.getLevelId();
                    if (level == oldLevel) {
                        // 告警等级没有变化
                        alert = oldAlert;
                        alert.happenedAgain();
                        alert.setLastTime(event.getCreateTime());
                        logger.debug("before updateAlert: " + alert);
                        alertService.updateAlert(alert);
                        logger.debug("after updateAlert: " + alert);
                    } else if (level > oldLevel) {
                        alert = oldAlert;
                        alert.setLevelId(event.getLevelId());
                        alert.setLastTime(event.getCreateTime());
                        logger.debug("before updateAlert: " + alert);
                        alertService.updateAlert(alert);
                        logger.debug("after updateAlert: " + alert);
                    }

                }
            } else if (event.isClear() == null || !event.isClear()) {
                alert = transform(event);
                alert.setTypeId(alertType.getTypeId());
                alert.setTypeName(alertType.getDisplayName());
                if (event.getLevelId() == null) {
                    alert.setLevelId(alertType.getLevelId());
                } else {
                    alert.setLevelId(event.getLevelId());
                }
                logger.debug("before insertAlert: " + alert);
                alertService.insertAlert(alert);
                logger.debug("after insertAlert: " + alert);
            } else if (oldAlert == null && event.isClear()) {
                // 当手动清除告警后的处理
                alert = transform(event);
                alert.setTypeId(alertType.getTypeId());
                alert.setClearTime(new Timestamp(System.currentTimeMillis()));
                alert.setTypeName(alertType.getDisplayName());
                // 清除告警事件，将推到前台的告警level设置为0屏蔽前台推送提示框
                alert.setLevelId((byte) 0);
                // 只需要通知页面即可
                alertService.fireAlert(alert, false);
            }
        }
        // event.setAlertId(alert.getAlertId());
        if (eventDao != null) {
            // 对于插入事件时网管数据已经不存在的情况,不进行任何处理(无法插入数据库)
            // Entity entity = entityService.getEntityFromDB(event.getEntityId());
            // if (entity != null) {
            // eventDao.insertEntity(event);
            // }
        }
    }

    protected List<Alert> selectCurrentAlert(Map<String, String> map) {
        return alertService.selectCurrentAlert(map);
    }

    protected List<EventType> getEventTypeByTypeId(Integer typeId) {
        List<EventType> types = new ArrayList<EventType>();
        List<EventType> eventTypes = eventService.getAllEventType();
        for (EventType eventType : eventTypes) {
            if (eventType.getTypeId().equals(typeId)) {
                types.add(eventType);
            }
        }
        return types;
    }

    protected AlertType getAlertTypeByTypeId(Integer typeId) {
        List<AlertType> alertTypes = alertService.getAllAlertTypes();
        for (AlertType alertType : alertTypes) {
            if (alertType.getTypeId().equals(typeId)) {
                return alertType;
            }
        }
        return null;
    }

    /**
     * @param event
     * @return
     * @throws Exception
     */
    public Alert transform(Event event) {
        Alert alert = new Alert();
        alert.setFirstTime(event.getCreateTime());
        alert.setLastTime(event.getCreateTime());
        alert.setIp(event.getIp());
        alert.setMessage(event.getMessage());
        alert.setMonitorId(event.getMonitorId());
        alert.setEntityId(event.getEntityId());
        // add by bravin@20150716:告警的entityId由局端的设备ID改为终端的ID，所以得取parentId
        Entity entity = entityService.getEntity(event.getEntityId());
        alert.setParentId(entity.getParentId());
        alert.setHost(event.getHost());
        alert.setSource(event.getSource());
        alert.setUserObject(event.getUserObject());
        alert.setSourceObject(event.getSourceObject());
        return alert;
    }

    public AlertService getAlertService() {
        return alertService;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public EntityAddressDao getEntityAddressDao() {
        return entityAddressDao;
    }

    public void setEntityAddressDao(EntityAddressDao entityAddressDao) {
        this.entityAddressDao = entityAddressDao;
    }

    public EventDao getEventDao() {
        return eventDao;
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public int getCos() {
        return cos;
    }

    public void setCos(int cos) {
        this.cos = cos;
    }

    @Override
    public int compareTo(EventParser eventParser) {
        if (this.cos > eventParser.getCos()) {
            return 1;
        } else if (this.cos == eventParser.getCos()) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 判断一个事件在目前环境下是否可以作为告警的清除事件
     * 
     * @param event
     * @return
     */
    public Boolean isAlertClearByEvent(Event event) {
        return eventDao.isAlertClearByEvent(event);
    }

}
