/***********************************************************************
 * $ DefaultEventParser.java,v1.0 2012-1-12 17:13:42 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.epon.fault.dao.OnuOfflineEventDao;
import com.topvision.ems.epon.fault.domain.OnuOfflineAlarm;
import com.topvision.ems.epon.fault.trap.OltTrapConstants;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventDelayItem;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * ONU上下线事件解析器 处理ONU上下线、解注册、解绑定、断电、断纤事件
 * 
 * @author jay
 * @created @2012-1-12-17:13:42
 */
@Service("onuOnlineEventParser")
public class OnuOnlineEventParser extends EventParser {
    private final Logger logger = LoggerFactory.getLogger(OnuOnlineEventParser.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuOfflineEventDao onuOfflineEventDao;
    /**
     * 事件处理等待线程
     */
    private LinkedBlockingQueue<Event> queueDoEvent;
    private ExecutorService onuExecutorService;
    private final int cos = 1000;

    // 用于处理延迟事件的线程池
    private ExecutorService delayExectuorService;
    // ONU设备事件延时队列,用于处理OFFLINE事件的延迟
    private DelayQueue<EventDelayItem> onuEventQueue;
    // ONU设备告警set,用于存储已经发生的ONU告警
    private Set<Event> onuAlertCache;
    @Value("${trap.offlineDelay:5}")
    private int delayed_time;
    @Resource(name = "messagePusher")
    private MessagePusher messagePusher;

    /**
     * 销毁一个EventParser
     */
    @Override
    @PreDestroy
    public void destroy() {
        queueDoEvent = null;
        getEventService().unRegistEventParser(this);
        onuExecutorService.shutdown();
        onuEventQueue = null;
        onuAlertCache = null;
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
        onuExecutorService = Executors.newFixedThreadPool(1);
        onuExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("OnuOnlineEventParser");
                while (queueDoEvent != null) {
                    try {
                        Event event = queueDoEvent.take();
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

        // 处理OFFLINE延迟的问题
        onuEventQueue = new DelayQueue<EventDelayItem>();
        onuAlertCache = Collections.synchronizedSet(new HashSet<Event>());
        delayExectuorService = Executors.newSingleThreadExecutor();
        delayExectuorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("OnuOnlineEventParser.delayedEventThread");
                while (onuEventQueue != null) {
                    try {
                        // take从延时队列取出元素,不需要进行处理
                        EventDelayItem item = onuEventQueue.take();
                        Event delayEvent = item.getEvent();
                        int eventTypeId = delayEvent.getTypeId();
                        if (eventTypeId == OltTrapConstants.ONU_OFFLINE) {
                            // 如果在延迟到期后仍然没有其它告警,则上报下线告警
                            if (!onuAlertCache.contains(delayEvent)) {
                                // 产生告警
                                queueDoEvent.add(delayEvent);
                            }
                        } else if (eventTypeId == OltTrapConstants.ONU_ONLINE) {
                            // 上线事件延迟到期后进行告警的清除
                            delayEvent.setClear(true);
                            queueDoEvent.add(delayEvent);
                            // 将缓存中保存的告警删除(Q:修改event的equals方法后,不关注告警类型,全部使用上线事件进行清除)
                            onuAlertCache.remove(delayEvent);
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
        // 处理ONU下线
        if (typeId == EponCode.ONU_OFFLINE) {
            // 当收到下线事件时如果已经存在告警,则不用再处理此下线事件(解决具体事件后又上报下线事件的情况)
            // 否则对下线事件进行延迟处理(解决只上报下线事件或者先上报下线事件的情况)
            if (!onuAlertCache.contains(event)) {
                // 对于ONU_OFFLINE事件,先进行延迟处理,不立即发出
                EventDelayItem delayItem = new EventDelayItem(delayed_time * 1000L, event);
                onuEventQueue.put(delayItem);
            }
            return true;
        }
        // 处理ONU断电,断纤,解注册,解绑定(删除)
        if (typeId == EponCode.ONU_PWR_DOWN || typeId == EponCode.ONU_FIBER_BREAK || typeId == EponCode.ONU_DEREGISTER) {
            // 补充sourceObject信息
            // 产生告警
            queueDoEvent.add(event);
            // 同时放入缓存(Q:修改event的equals方法后,一个设备只会有一种告警被保留)
            onuAlertCache.add(event);
            return true;
        }
        // 处理ONU上线事件
        if (typeId == EponCode.ONU_ONLINE) {
            // 防止ONLINE事件在OFFLINE延迟之前就上报,因此对ONLINE事件也进行延迟
            EventDelayItem delayItem = new EventDelayItem(delayed_time * 1000L, event);
            onuEventQueue.put(delayItem);
            return true;
        }
        return false;
    }

    @Override
    public void doEvent(Event event) {
        try {
            OltOnuAttribute onuAttribute = onuService.getOnuAttribute(event.getEntityId());
            if (event.getTypeId() == EponCode.ONU_PWR_DOWN || event.getTypeId() == EponCode.ONU_FIBER_BREAK
                    || event.getTypeId() == EponCode.ONU_DEREGISTER || event.getTypeId() == EponCode.ONU_OFFLINE) {
                Entity entity = entityService.getEntity(event.getEntityId());
                OnuOfflineAlarm alarm = new OnuOfflineAlarm();
                alarm.setEntityId(onuAttribute.getEntityId());
                alarm.setMac(onuAttribute.getOnuMac());
                alarm.setFireTime(event.getCreateTime());
                alarm.setMessage(event.getMessage());
                alarm.setOnuAlias(entity.getName());
                alarm.setOnuType(entity.getTypeId());
                alarm.setOnuIndex(onuAttribute.getOnuIndex());
                alarm.setAlertType(event.getTypeId());
                alarm.setParentId(onuAttribute.getEntityId());
                onuOfflineEventDao.insertOnuOfflineEvent(alarm);
            } else if (event.getTypeId() == EponCode.ONU_ONLINE) {
                // TODO 推送单独的event
                Message msg = new Message(Message.ALERT_EVENT);
                event.setParentId(onuAttribute.getEntityId());
                msg.setData(event);
                if (event != null && event.getTypeId() != null) {
                    msg.setId(event.getTypeId().toString());
                }
                messagePusher.sendMessage(msg);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        super.doEvent(event);

    }

    public int getDelayed_time() {
        return delayed_time;
    }

    public void setDelayed_time(int delayed_time) {
        this.delayed_time = delayed_time;
    }

}