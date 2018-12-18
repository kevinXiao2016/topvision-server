/***********************************************************************
 * $Id: UniEventParser.java,v1.0 2017年1月14日 上午9:39:51 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

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
 * @author lizongtian
 * @created @2017年1月14日-上午9:39:51
 *
 */
@Service("uniEventParser")
public class UniEventParser extends EventParser {
    private ExecutorService uniExecutorService;
    private LinkedBlockingQueue<Event> uniQueue;
    private final int threadNum = 100;
    @Autowired
    private MessageService messageService;

    @Override
    public void initialize() {
        // 初始化线程队列
        uniQueue = new LinkedBlockingQueue<Event>();
        // 初始化处理线程
        uniExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        uniExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("UniEventParser");
                while (true) {
                    try {
                        Event event = uniQueue.take();
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

    @Override
    public void destroy() {
        uniQueue = null;
        uniExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.ONU_UNI_LINKDOWN) {
            uniQueue.add(event);
            return true;
        }
        return false;
    }

}
