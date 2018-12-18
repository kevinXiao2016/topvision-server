/***********************************************************************
 * $Id: OnuRegisterFailEventParser.java,v1.0 2017-6-30 上午09:17:47 $
 * 
 * @author: lzt
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
 * @author lzt
 * @created @2017-6-30-上午09:17:47
 * 
 */
@Service("rogueOnuEventParser")
public class RogueOnuEventParser extends EventParser {
    private ExecutorService rogueOnuExecutorService;
    private LinkedBlockingQueue<Event> rogueOnuQueue;
    private final int threadNum = 1;
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
        rogueOnuQueue = new LinkedBlockingQueue<Event>();
        rogueOnuExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        rogueOnuExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("RogueOnuEventParser");
                while (true) {
                    try {
                        Event event = rogueOnuQueue.take();
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
        rogueOnuQueue = null;
        getEventService().unRegistEventParser(this);
        rogueOnuExecutorService.shutdown();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#parse(com.topvision.ems.fault.domain.Event)
     */
    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.ONU_ROGUE) {
            // 特殊处理ONU认证失败的告警Source
            /*String oldSource = event.getSource();
            String newSource = oldSource + ":" + event.getOriginMessage();
            event.setSource(newSource);*/
            rogueOnuQueue.add(event);
            return true;
        }
        return false;
    }

}
