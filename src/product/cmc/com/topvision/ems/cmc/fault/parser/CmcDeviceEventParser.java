/***********************************************************************
 * $Id: CmcDeviceEventParser.java,v1.0 2017年1月15日 上午10:35:29 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.fault.trap.CmcTrapConstants;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lizongtian
 * @created @2017年1月15日-上午10:35:29
 *
 */
@Service("cmcDeviceEventParser")
public class CmcDeviceEventParser extends EventParser {
    private ExecutorService cmcDeviceExecutorService;
    private LinkedBlockingQueue<Event> cmcDeviceQueue;
    private final int threadNum = 10;
    @Autowired
    private MessageService messageService;

    @Override
    public void initialize() {
        // 初始化线程队列
        cmcDeviceQueue = new LinkedBlockingQueue<Event>();
        // 初始化处理线程
        cmcDeviceExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        cmcDeviceExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("CmcDeviceEventParser");
                while (true) {
                    try {
                        Event event = cmcDeviceQueue.take();
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

    @Override
    public void destroy() {
        cmcDeviceQueue = null;
        cmcDeviceExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == CmcTrapConstants.CMC_RESET || event.getTypeId() == CmcTrapConstants.CMC_RESTART) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            cmcDeviceQueue.add(event);
            return true;
        } else if(event.getTypeId() == CmcTrapConstants.CMC_REONLINE) {
            event.setClear(true);
            cmcDeviceQueue.add(event);
            return true;
        }
        return false;
    }

}
