/***********************************************************************
 * $Id: OnuRegisterFailEventParser.java,v1.0 2012-6-15 上午09:17:47 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * @author huqiao
 * @created @2012-6-15-上午09:17:47
 * 
 */
@Service("onuRegisterFailEventParser")
public class OnuRegisterFailEventParser extends EventParser {
    private ExecutorService onuRegisterExecutorService;
    private LinkedBlockingQueue<Event> onuRegisterQueue;
    private final int threadNum = 100;
    @Autowired
    private MessageService messageService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        // 初始化线程队列
        onuRegisterQueue = new LinkedBlockingQueue<Event>();
        onuRegisterExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        onuRegisterExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("OnuRegisterFailEventParser");
                while (true) {
                    try {
                        Event event = onuRegisterQueue.take();
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        onuRegisterQueue = null;
        getEventService().unRegistEventParser(this);
        onuRegisterExecutorService.shutdown();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#parse(com.topvision.ems.fault.domain.Event)
     */
    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.ONU_MAC_AUTH_ERROR || event.getTypeId() == EponCode.ONU_ILLEGALREGISTER) {
            // 特殊处理ONU认证失败的告警Source
            /*String oldSource = event.getSource();
            String newSource = oldSource + ":" + event.getOriginMessage();
            event.setSource(newSource);*/
            onuRegisterQueue.add(event);
            return true;
        }
        return false;
    }

}
