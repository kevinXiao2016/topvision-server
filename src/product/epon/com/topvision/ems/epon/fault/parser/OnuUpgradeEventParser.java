/***********************************************************************
 * $Id: OnuUpgradeEventParser.java,v1.0 2017年1月14日 下午5:04:59 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lizongtian
 * @created @2017年1月14日-下午5:04:59
 *
 */
@Service("onuUpgradeEventParser")
public class OnuUpgradeEventParser extends EventParser {
    private ExecutorService onuUpgradeExecutorService;
    private LinkedBlockingQueue<Event> onuUpgradeQueue;
    private final int threadNum = 100;
    @Autowired
    private MessageService messageService;
    @Resource(name = "messagePusher")
    private MessagePusher messagePusher;

    @Override
    public void initialize() {
        // 初始化线程队列
        onuUpgradeQueue = new LinkedBlockingQueue<Event>();
        onuUpgradeExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        onuUpgradeExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("OnuUpgradeEventParser");
                while (true) {
                    try {
                        Event event = onuUpgradeQueue.take();
                        if (event.getUserObject() instanceof Trap) {
                            TrapEvent evt = new TrapEvent(event.getUserObject());
                            evt.setTrap((Trap) event.getUserObject());
                            evt.setEntityId(event.getEntityId());
                            evt.setCode(event.getTypeId());
                            evt.setSource(event.getSourceRaw());
                            evt.setDeviceIndex(event.getDeviceIndex());
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
    public void doEvent(Event event) {
        // 单纯的推送给event到前端
        Message msg = new Message(Message.ALERT_EVENT);
        msg.setData(event);
        if(event != null && event.getTypeId() != null) {
            msg.setId(event.getTypeId().toString());
        }
        messagePusher.sendMessage(msg);
    }

    @Override
    public void destroy() {
        onuUpgradeQueue = null;
        getEventService().unRegistEventParser(this);
        onuUpgradeExecutorService.shutdown();
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.ONU_UPGRADE_OK || event.getTypeId() == EponCode.ONU_AUTO_UPGRADE) {
            onuUpgradeQueue.add(event);
            return true;
        }
        return false;
    }

}
