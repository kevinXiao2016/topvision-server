/***********************************************************************
 * $Id: SlotEventParser.java,v1.0 2012-2-8 下午12:00:06 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.topvision.ems.fault.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * @author huqiao
 * @created @2012-2-8-下午12:00:06
 * 
 */
@Service("slotEventParser")
public class SlotEventParser extends EventParser {
    private ExecutorService slotExecutorService;
    private LinkedBlockingQueue<Event> slotQueue;
    private final int threadNum = 1;
    private final Map<Long, Long> switchOverTimeMap = new HashMap<Long, Long>();
    @Autowired
    private MessageService messageService;
    private DelayQueue<SlotEventDelayItem> slotOfflineDelayQueue;
    private DelayQueue<SlotEventDelayItem> slotProvFaildDelayQueue;
    @Value("${trap.slotOfflineDelay:60}")
    private int slotOfflineDelayed_time;
    @Value("${trap.slotProvFaildDelay:3600}")
    private int slotProvFaildDelayed_time;

    // 设置优先级
    // private final int cos = 100;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        // 初始化线程队列
        slotQueue = new LinkedBlockingQueue<Event>();
        slotOfflineDelayQueue = new DelayQueue<SlotEventDelayItem>();
        slotProvFaildDelayQueue = new DelayQueue<SlotEventDelayItem>();

        //主动移除delayQueue中的超时对象
        new Thread () {
            public void run() {
                while (true) {
                    try {
                        SlotEventDelayItem delayItem = slotOfflineDelayQueue.take();
                        logger.debug("SlotEventParser slotOfflineDelayQueue free delayItem[" +delayItem+ "]");
                    } catch (InterruptedException e) {
                        logger.debug("",e);
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();

        new Thread () {
            public void run() {
                while (true) {
                    try {
                        SlotEventDelayItem delayItem = slotProvFaildDelayQueue.take();
                        logger.debug("SlotEventParser slotProvFaildDelayQueue free delayItem[" +delayItem+ "]");
                    } catch (InterruptedException e) {
                        logger.debug("",e);
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
        // 初始化处理线程
        slotExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        slotExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("SlotEventParser");
                while (true) {
                    try {
                        Event event = slotQueue.take();
                        logger.debug("SlotEventParser slotQueue send event[" +event+ "]");
                        if (event.getUserObject() instanceof Trap) {
                            TrapEvent evt = new TrapEvent(event.getUserObject());
                            evt.setTrap((Trap) event.getUserObject());
                            evt.setEntityId(event.getEntityId());
                            evt.setCode(event.getTypeId());
                            evt.setSource(event.getSource());
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
        // 将自身添加到事件处理器队列
        getEventService().registEventParser(this);
    }

    // 重写方法 不需要标准的事件处理流程
    // 修改原因 需要满足板卡上线
    @Override
    public void doEvent(Event event) {
        super.doEvent(event);
        
        // 在进行标准事件处理后，需要将板卡上线推送至前端
        if (event.getTypeId() == EponCode.BD_ONLINE || event.getTypeId() == EponCode.SYSTEM_HA_SWITCH) {
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
            alert.setLevelId((byte) 0);
            getAlertService().fireAlert(alert, false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        slotQueue = null;
        slotOfflineDelayQueue = null;
        slotProvFaildDelayQueue = null;
        slotExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#parse(com.topvision.ems.fault.domain.Event)
     */
    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.SYSTEM_HA_SWITCH) {
            // 主备倒换会产生两次SYSTEM_HA_SWITCH告警
            // 这里的主备倒换是为了标记不处理板卡下线
            switchOverTimeMap.put(event.getEntityId(), System.currentTimeMillis());
            slotQueue.add(event);
            return true;
        } else if (event.getTypeId() == EponCode.BD_OFFLINE) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            if (switchOverTimeMap.containsKey(event.getEntityId())) {
                // @Modify by Rod 主备倒换过程中的板卡下线告警不处理
                if (Math.abs(System.currentTimeMillis() - switchOverTimeMap.get(event.getEntityId())) < 5000) {
                    return true;
                }
            }
            SlotEventDelayItem delayItem = new SlotEventDelayItem(slotOfflineDelayed_time * 1000L, event);
            if (!slotOfflineDelayQueue.contains(delayItem)) {
                slotOfflineDelayQueue.put(delayItem);
                slotQueue.add(event);
            }
            return true;
        } else if (event.getTypeId() == EponCode.BD_REMOVE || event.getTypeId() == EponCode.BD_RESET
                || event.getTypeId() == EponCode.BD_PROV_FAIL || event.getTypeId() == EponCode.BD_TYPE_MISMATCH
                || event.getTypeId() == EponCode.BD_SW_MISMATCH || event.getTypeId() == EponCode.BD_TEMP_HIGH
                || event.getTypeId() == EponCode.BD_TEMP_LOW || event.getTypeId() == EponCode.BD_SLOT_MISMATCH) {
            if (event.getTypeId() == EponCode.BD_PROV_FAIL) {
                SlotEventDelayItem delayItem = new SlotEventDelayItem(slotProvFaildDelayed_time * 1000L, event);
                if (!slotProvFaildDelayQueue.contains(delayItem)) {
                    slotProvFaildDelayQueue.put(delayItem);
                    slotQueue.add(event);
                }
            } else {
                slotQueue.add(event);
            }
            return true;
        } else if (event.getTypeId() == EponCode.BD_ONLINE || event.getTypeId() == EponCode.BD_INSERT
                || event.getTypeId() == EponCode.BD_TEMP_OK) {
            event.setClear(true);
            slotQueue.add(event);
            return true;
        }
        return false;
    }
}
