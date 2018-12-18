/***********************************************************************
 * $Id: DeviceEventParser.java,v1.0 2017年1月12日 下午7:33:09 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lizongtian
 * @created @2017年1月12日-下午7:33:09
 *
 */
@Service("deviceEventParser")
public class DeviceEventParser extends EventParser {
    private ExecutorService deviceEventExecutorService;
    @Autowired
    private MessageService messageService;
    private LinkedBlockingQueue<Event> deviceQueue;
    private final int threadNum = 1;

    @Override
    public void initialize() {
        // 初始化线程队列
        deviceQueue = new LinkedBlockingQueue<Event>();
        deviceEventExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        deviceEventExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("DeviceEventParser");
                while (true) {
                    try {
                        Event event = deviceQueue.take();
                        if (event.getUserObject() instanceof Trap) {
                            TrapEvent evt = new TrapEvent(event.getUserObject());
                            evt.setTrap((Trap) event.getUserObject());
                            evt.setCode(event.getTypeId());
                            evt.setEntityId(event.getEntityId());
                            evt.setSource(event.getSourceRaw());
                            evt.setActionName("onTrapMessage");
                            evt.setListener(TrapListener.class);
                            messageService.addMessage(evt);
                        }
                        doEvent(event);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
        });
        // 将自身添加到事件处理器队列
        getEventService().registEventParser(this);
    }

    @Override
    public void destroy() {
        deviceQueue = null;
        deviceEventExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    @Override
    public void doEvent(Event event) {
        if (event.getTypeId() == EponCode.DEVICE_START) {
            // 推送事件
            List<EventType> eventTypes = getEventTypeByTypeId(event.getTypeId());
            if (eventTypes.size() == 0) {
                return;
            }
            for (EventType eventType : eventTypes) {
                event.setName(eventType.getDisplayName());
            }
            Alert alert = null;
            alert = transform(event);
            alert.setTypeId(event.getTypeId());
            // 清除告警事件，将推到前台的告警level设置为0屏蔽前台推送提示框
            alert.setLevelId((byte) 0);
            // 只需要通知页面即可
            getAlertService().fireAlert(alert, false);
            if (getEventDao() != null) {
                getEventDao().insertEntity(event);
            }
        } else {
            super.doEvent(event);
        }
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.DEVICE_RESET) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            deviceQueue.add(event);
            return true;
        } else if (event.getTypeId() == EponCode.DEVICE_START) {
            event.setClear(true);
            deviceQueue.add(event);
            return true;
        }
        return false;
    }

}
