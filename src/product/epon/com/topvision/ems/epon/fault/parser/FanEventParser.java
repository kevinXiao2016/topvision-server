/***********************************************************************
 * $Id: FanRemoveEventParser.java,v1.0 2012-3-20 上午10:09:09 $
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
 * @created @2012-3-20-上午10:09:09
 * 
 */
@Service("fanEventParser")
public class FanEventParser extends EventParser {
    private ExecutorService fanExecutorService;
    @Autowired
    private MessageService messageService;
    private LinkedBlockingQueue<Event> fanQueue;
    private final int threadNum = 1;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        // 初始化线程队列
        fanQueue = new LinkedBlockingQueue<Event>();
        fanExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        fanExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("FanEventParser");
                while (true) {
                    try {
                        Event event = fanQueue.take();
                        if (event.getUserObject() instanceof Trap) {
                            TrapEvent evt = new TrapEvent(event.getUserObject());
                            evt.setTrap((Trap) event.getUserObject());
                            evt.setEntityId(event.getEntityId());
                            evt.setCode(event.getTypeId());
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        fanQueue = null;
        fanExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#parse(com.topvision.ems.fault.domain.Event)
     */
    @Override
    public boolean parse(Event event) {
        // 增加对文件上传成功的event处理
        if (event.getTypeId() == EponCode.BD_FAN_REMOVE || event.getTypeId() == EponCode.BD_FAN_FAIL) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            fanQueue.add(event);
            return true;
        } else if (event.getTypeId() == EponCode.BD_FAN_INSERT || event.getTypeId() == EponCode.BD_FAN_NORMAL) {
            event.setClear(true);
            fanQueue.add(event);
            return true;
        }
        return false;
    }

}
