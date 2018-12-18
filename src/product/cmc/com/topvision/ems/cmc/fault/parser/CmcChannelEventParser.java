/***********************************************************************
 * $Id: CcmChannelEventParser.java,v1.0 2017年1月12日 上午9:17:45 $
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
 * @created @2017年1月12日-上午9:17:45
 *
 */
@Service("cmcChannelEventParser")
public class CmcChannelEventParser extends EventParser {
    private ExecutorService cmcChannelExecutorService;
    private LinkedBlockingQueue<Event> cmcChannelQueue;
    private final int threadNum = 100;
    @Autowired
    private MessageService messageService;

    @Override
    public void initialize() {
        // 初始化线程队列
        cmcChannelQueue = new LinkedBlockingQueue<Event>();
        // 初始化处理线程
        cmcChannelExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        cmcChannelExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("CmcChannelEventParser");
                while (true) {
                    try {
                        Event event = cmcChannelQueue.take();
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
        cmcChannelQueue = null;
        cmcChannelExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == CmcTrapConstants.CMC_CHANNEL_OFFLINE
                || event.getTypeId() == CmcTrapConstants.CMC_US_CLOSE
                || event.getTypeId() == CmcTrapConstants.CMC_DS_CLOSE
                || event.getTypeId() == CmcTrapConstants.CMC_US_PARAM
                || event.getTypeId() == CmcTrapConstants.CMC_DS_PARAM) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            cmcChannelQueue.add(event);
            return true;
        } else if (event.getTypeId() == CmcTrapConstants.CMC_CHANNEL_ONLINE
                || event.getTypeId() == CmcTrapConstants.CMC_US_OPEN
                || event.getTypeId() == CmcTrapConstants.CMC_DS_OPEN) {
            event.setClear(true);
            cmcChannelQueue.add(event);
            return true;
        }
        return false;
    }

}
