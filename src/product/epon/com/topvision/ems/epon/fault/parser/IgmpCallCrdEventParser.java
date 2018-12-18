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

@Service("igmpCallCrdEventParser")
public class IgmpCallCrdEventParser extends EventParser {
    private ExecutorService igmpCallCrdExecutorService;
    @Autowired
    private MessageService messageService;
    private LinkedBlockingQueue<Event> igmpCallCrdQueue;
    private final int threadNum = 1;

    @Override
    @PostConstruct
    public void initialize() {
        igmpCallCrdExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        // 初始化线程队列
        igmpCallCrdQueue = new LinkedBlockingQueue<Event>();
        igmpCallCrdExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("IgmpCallCrdEventParser");
                while (igmpCallCrdQueue != null) {
                    try {
                        Event event = igmpCallCrdQueue.take();
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
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        // 将自身添加到事件处理器队列
        getEventService().registEventParser(this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        igmpCallCrdQueue = null;
        igmpCallCrdExecutorService.shutdownNow();
        getEventService().unRegistEventParser(this);
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.IGMP_CALL_CRD) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            igmpCallCrdQueue.add(event);
            return true;
        } else if (event.getTypeId() == EponCode.IGMP_CALL_CRD) {
            event.setClear(true);
            igmpCallCrdQueue.add(event);
            return true;
        }
        return false;
    }

}
