/***********************************************************************
 * $Id: CmcOnlineEventParser.java,v1.0 2016-3-25 下午3:29:35 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.fault.trap.CmcTrapConstants;
import com.topvision.ems.epon.fault.parser.OnuOnlineEventParser;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventDelayItem;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.framework.snmp.Trap;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.message.service.MessageService;

/**
 * CCMTS(CMC-II型)上下线事件 处理CCMTS上下线、断电、断纤功能
 * 
 * @author flack
 * @created @2016-3-25-下午3:29:35
 */
@Service("cmcOnlineEventParser")
public class CmcOnlineEventParser extends EventParser {
    private final Logger logger = LoggerFactory.getLogger(OnuOnlineEventParser.class);
    @Autowired
    private MessageService messageService;
    /**
     * 事件处理等待线程
     */
    private LinkedBlockingQueue<Event> queueDoEvent;
    private ExecutorService cmcExecutorService;
    // 用于插入event表的事件
    private LinkedBlockingQueue<Event> saveEventQueue;
    private ExecutorService saveEventExecutorService;
    
    private final int cos = 1500;

    // CCMTS设备事件延时队列,用于处理OFFLINE事件的延迟
    private DelayQueue<EventDelayItem> cmcEventQueue;
    // CCMTS设备告警set,用于存储已经发生的CCMTS告警
    private Set<Event> cmcAlertCache;
    // 用于记录两次上线的过程
    private Set<Event> cmcOnlineCache;
    // 用于处理延迟事件的线程池
    private ExecutorService delayExectuorService;
    @Value("${trap.offlineDelay:5}")
    private int offlineDelay;
    @Value("${trap.onlineDelay:140}")
    private int onlineDelay;
    @Value("${trap.firbreakSwitch:0}")
    private int firbreakSwitch;
    @Autowired
    private LicenseIf licenseIf;

    /**
     * 销毁一个EventParser
     */
    @Override
    @PreDestroy
    public void destroy() {
        queueDoEvent = null;
        saveEventQueue = null;
        getEventService().unRegistEventParser(this);
        cmcExecutorService.shutdown();
        saveEventExecutorService.shutdown();
        cmcEventQueue = null;
        cmcAlertCache = null;
        cmcOnlineCache = null;
        delayExectuorService.shutdownNow();
    }

    /**
     * 初始化一个EventParser
     */
    @Override
    @PostConstruct
    public void initialize() {
        setCos(cos);
        queueDoEvent = new LinkedBlockingQueue<Event>();
        cmcExecutorService = Executors.newFixedThreadPool(1);
        cmcExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("cmcOnlineEventParser");
                while (queueDoEvent != null) {
                    try {
                        Event event = queueDoEvent.take();
                        // modify lzt 2017-6-13
                        // 胡江昳和珠江数码张治沟通后,要求将CC下线全部上报为断纤,对下线code进行转换
                        //CCMTS下线告警转换为断纤告警,默认不转换，需要转换时在config.properities增加trap.firbreakSwitch=1的配置项
                        //下线转换为断纤是珠江数码特殊需求
                        if (firbreakSwitch == 1 || licenseIf.isProject("gz")) {
                            if (event.getTypeId() == CmcTrapConstants.CMC_OFFLINE) {
                                event.setTypeId(CmcTrapConstants.CMC_FIBER_BREAK);
                            }
                        }
                        if (event.getUserObject() instanceof Trap) {
                            TrapEvent evt = new TrapEvent(event.getUserObject());
                            evt.setTrap((Trap) event.getUserObject());
                            evt.setEntityId(event.getEntityId());
                            evt.setCode(event.getTypeId());
                            evt.setSource(event.getSourceRaw());
                            evt.setDeviceIndex(event.getDeviceIndex());
                            evt.setActionName("onTrapMessage");
                            evt.setListener(TrapListener.class);
                            messageService.fireMessage(evt);
                        }
                        doEvent(event);

                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
        });
        
        saveEventQueue = new LinkedBlockingQueue<Event>();
        saveEventExecutorService = Executors.newFixedThreadPool(1);
        saveEventExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("saveEventExecutorServiceThread");
                while (saveEventQueue != null) {
                    try {
                        Event event = saveEventQueue.take();
                        // 入库
                        if (getEventDao() != null) {
                            getEventDao().insertEntity(event);
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
        });
        
        // 处理OFFLINE延迟的问题
        cmcEventQueue = new DelayQueue<EventDelayItem>();
        cmcAlertCache = Collections.synchronizedSet(new HashSet<Event>());
        cmcOnlineCache = Collections.synchronizedSet(new HashSet<Event>());
        delayExectuorService = Executors.newSingleThreadExecutor();
        delayExectuorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("CmcOnlineEventParser.delayedEventThread");
                while (cmcEventQueue != null) {
                    try {
                        // take从延时队列取出元素,不需要进行处理
                        EventDelayItem item = cmcEventQueue.take();
                        Event cacheEvent = item.getEvent();
                        int eventTypeId = cacheEvent.getTypeId();
                        if (eventTypeId == CmcTrapConstants.CMC_ONLINE) {
                            // 排除二次上线过程中第一次上线事件对其他告警的影响
                            if (cmcOnlineCache.contains(cacheEvent)) {
                                // 对于一次上线的情况,默认认为当前上线事件会清除产生的告警
                                // 特例:对于老版本的CC解绑定在第一次上线和第二次上线之间获取设备数据时状态为offline,只有第二次上线会走到这里,如果丢弃了会导致设备上线后的状态不正确
                                cacheEvent.setClear(true);
                                queueDoEvent.add(cacheEvent);
                                // 延迟结束,告警依然存在,则清除告警
                                if (cmcAlertCache.contains(cacheEvent)) {
                                    // 将缓存中保存的告警删除
                                    cmcAlertCache.remove(cacheEvent);
                                }
                                // add by fanzidong，要清除掉延时队列里的下线事件
                                try {
                                    // 设置这三个属性，唯一确定单台设备的指定类型告警entityId source typeId
                                    Event copyEvent = new Event();
                                    copyEvent.setEntityId(cacheEvent.getEntityId());
                                    copyEvent.setSource(cacheEvent.getSource());
                                    copyEvent.setTypeId(CmcTrapConstants.CMC_OFFLINE);
                                    EventDelayItem delayItem = new EventDelayItem(offlineDelay * 1000L, copyEvent);
                                    if (cmcEventQueue.contains(delayItem)) {
                                        logger.info("remove offline delayitem");
                                        cmcEventQueue.remove(delayItem);
                                    }
                                } catch (Exception e) {
                                    logger.error("failed to remove offline delayItem");
                                }
                            }
                            // 删除缓存中的上线行为
                            cmcOnlineCache.remove(cacheEvent);
                        } else if (eventTypeId == CmcTrapConstants.CMC_OFFLINE) {
                            // 如果在延迟到期后仍然没有其它告警,则上报下线告警
                            if (!cmcAlertCache.contains(cacheEvent)) {
                                // 产生告警
                                queueDoEvent.add(cacheEvent);
                                // 同时放入缓存,做为上线事件延迟到期后清除的依据
                                cmcAlertCache.add(cacheEvent);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
        });
        // 从EventService注册解析器
        getEventService().registEventParser(this);
    }

    /**
     * 处理一个Event
     * 
     * @param event
     * @return 是否能够处理该Event 能处理返回true 不能处理返回false
     */
    @Override
    public boolean parse(Event event) {
        int typeId = event.getTypeId();
        if (typeId == CmcTrapConstants.CMC_OFFLINE || typeId == CmcTrapConstants.CMC_FIBER_BREAK
                || typeId == CmcTrapConstants.CMC_POWER_OFF || typeId == CmcTrapConstants.CMC_ONLINE) {
            // modify by fanzidong, 改为加入队列，独立线程插入，不影响主业务
            saveEventQueue.add(event);
        }
        // 处理CMTS下线事件
        if (typeId == CmcTrapConstants.CMC_OFFLINE) {
            // 当收到下线事件时如果已经存在告警,则不用再处理此下线事件(解决具体事件后又上报下线事件的情况)
            // 否则对下线事件进行延迟处理(解决只上报下线事件或者先上报下线事件的情况)
            if (!cmcAlertCache.contains(event)) {
                // 对于CMC_OFFLINE事件,先进行延迟处理,不立即发出
                EventDelayItem delayItem = new EventDelayItem(offlineDelay * 1000L, event);
                cmcEventQueue.put(delayItem);
            }
            return true;
        }
        // 处理CMTS断纤和断电事件
        if (typeId == CmcTrapConstants.CMC_FIBER_BREAK || typeId == CmcTrapConstants.CMC_POWER_OFF) {
            // 如果该设备已经存在告警,则不处理(解决存在两次上报的情况)
            // 如果不存在告警,则发出告警并缓存
            // 前提: 优先处理断纤和断电事件,最后才考虑下线事件,所以当前缓存的告警通常是优化级高的
            if (!cmcAlertCache.contains(event)) {
                // 产生告警
                queueDoEvent.add(event);
                // 同时放入缓存(Q:修改event的equals方法后,一个设备只会有一种告警被保留)
                cmcAlertCache.add(event);
                // add by fanzidong，要清除掉延时队列里的下线事件
                try {
                    // 设置这三个属性，唯一确定单台设备的指定类型告警entityId source typeId
                    Event copyEvent = new Event();
                    copyEvent.setEntityId(event.getEntityId());
                    copyEvent.setSource(event.getSource());
                    copyEvent.setTypeId(CmcTrapConstants.CMC_OFFLINE);
                    EventDelayItem delayItem = new EventDelayItem(offlineDelay * 1000L, copyEvent);
                    if (cmcEventQueue.contains(delayItem)) {
                        logger.info("remove offline delayitem");
                        cmcEventQueue.remove(delayItem);
                    }
                } catch (Exception e) {
                    logger.error("failed to remove offline delayItem");
                }
            }
            return true;
        }
        // 解绑定事件
        if (typeId == CmcTrapConstants.CMC_DELETE) {
            queueDoEvent.add(event);
            return true;
        }
        // 处理CMTS上线事件
        if (typeId == CmcTrapConstants.CMC_ONLINE) {
            // 存在两次上线的情况,因此第一次接收上线时先缓存,如果接收到第二次则直接清除存在的告警
            if (cmcOnlineCache.contains(event)) {
                event.setClear(true);
                queueDoEvent.add(event);
                // 将缓存中保存的告警删除(Q:修改event的equals方法后,不关注告警类型,全部使用上线事件进行清除)
                cmcAlertCache.remove(event);
                // 将缓存的第一次的上线事件删除(通过这种方式确定延迟到期后在cmcOnlineCache中存在的是只上报一次的上线事件)
                cmcOnlineCache.remove(event);
                // add by fanzidong，要清除掉延时队列里的下线事件
                try {
                    // 设置这三个属性，唯一确定单台设备的指定类型告警entityId source typeId
                    Event copyEvent = new Event();
                    copyEvent.setEntityId(event.getEntityId());
                    copyEvent.setSource(event.getSource());
                    copyEvent.setTypeId(CmcTrapConstants.CMC_OFFLINE);
                    EventDelayItem delayItem = new EventDelayItem(offlineDelay * 1000L, copyEvent);
                    if (cmcEventQueue.contains(delayItem)) {
                        logger.info("remove offline delayitem");
                        cmcEventQueue.remove(delayItem);
                    }
                } catch (Exception e) {
                    logger.error("failed to remove offline delayItem");
                }
            } else {
                // 记录第一次上线行为
                cmcOnlineCache.add(event);
                // 延迟处理
                EventDelayItem delayItem = new EventDelayItem(onlineDelay * 1000L, event);
                cmcEventQueue.put(delayItem);
            }
            return true;
        }
        return false;
    }

    public int getOfflineDelay() {
        return offlineDelay;
    }

    public void setOfflineDelay(int offlineDelay) {
        this.offlineDelay = offlineDelay;
    }

    public int getOnlineDelay() {
        return onlineDelay;
    }

    public void setOnlineDelay(int onlineDelay) {
        this.onlineDelay = onlineDelay;
    }

} 
