package com.topvision.ems.epon.fault.parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.DelayItem;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * port linked, pon los, pon disable, optical remove 抽象Parser父类
 * 
 * @author w1992wishes
 * @created @2018年1月30日-下午1:38:57
 *
 */
public abstract class PonAbstractEventParser extends EventParser {

    protected ExecutorService ponDelayeExecutorService;
    protected DelayQueue<DelayItem<Event>> ponDelayQueue;
    protected final int threadNum = 1;
    protected final long delaySec = 5000L;
    protected String threadName;

    @Autowired
    private MessageService messageService;

    @Override
    @PostConstruct
    public void initialize() {
        // 初始化线程队列
        ponDelayQueue = new DelayQueue<DelayItem<Event>>();
        ponDelayeExecutorService = Executors.newFixedThreadPool(threadNum);

        ponDelayeExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName(threadName);
                while (true) {
                    try {
                        Event event = ponDelayQueue.take().getItem();
                        if (event.getUserObject() instanceof Trap) {
                            TrapEvent evt = new TrapEvent(event.getUserObject());
                            evt.setTrap((Trap) event.getUserObject());
                            evt.setCode(event.getTypeId());
                            evt.setSource(event.getSourceRaw());
                            evt.setEntityId(event.getEntityId());
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
    @PreDestroy
    public void destroy() {
        ponDelayQueue = null;
        ponDelayeExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    @Override
    public boolean parse(Event event) {
        DelayItem<Event> ponDelayItemEvent = new DelayItem<Event>(event, delaySec);
        if (event.getTypeId() == getPonAlarmCode()) {

            // 对于告警事件，直接加入队列即可
            ponDelayQueue.add(ponDelayItemEvent);
            return true;

        } else if (event.getTypeId() == getPonClearCode()) {
            // 对于清除事件，需要特殊处理，防止在短时间内震荡产生的多次相同告警和清除被多次触发

            // 先将队列中和当前清除事件属于同一个目标源产生的事件都找到放入一个集合中
            Queue<DelayItem<Event>> sameSourceItem = getSameSources(event);

            // 如果新获取的集合第一个元素是告警事件，短时间内出现的清除事件将其从队列移出，直接返回
            DelayItem<Event> first = sameSourceItem.peek();
            if (first == null ? false : first.getItem().getTypeId() == getPonAlarmCode()) {
                ponDelayQueue.remove(first);
            } else {
                // 或者第一个元素不是告警事件，表明告警事件已从队列取出消费了，这时如果短时间内出现的震荡告警-清除事件，只需一个清除事件即可
                ponDelayQueue.removeAll(sameSourceItem);
                event.setClear(true);
                ponDelayQueue.add(ponDelayItemEvent);
            }
            return true;

        }
        return false;
    }

    protected abstract int getPonAlarmCode();

    protected abstract int getPonClearCode();

    private Queue<DelayItem<Event>> getSameSources(Event event) {
        Queue<DelayItem<Event>> sameSourceItem = new LinkedList<DelayItem<Event>>();
        for (Iterator<DelayItem<Event>> it = ponDelayQueue.iterator(); it.hasNext();) {
            DelayItem<Event> delayItem = it.next();
            if (matchSameSourceEvent(event, delayItem.getItem())) {
                sameSourceItem.add(delayItem);
            }
        }
        return sameSourceItem;
    }

    private boolean matchSameSourceEvent(Event event1, Event event2) {
        if (event1.getEntityId().equals(event2.getEntityId()) && event1.getSource().equals(event2.getSource())) {
            return true;
        }
        return false;
    }

}
