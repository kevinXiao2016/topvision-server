/***********************************************************************
 * $Id: MessageServiceImpl.java,v 1.1 Sep 6, 2009 10:44:08 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.event.EmsEventObject;
import com.topvision.platform.message.event.EmsListener;
import com.topvision.platform.message.event.EventListenerAggregate;
import com.topvision.platform.message.event.MessageCallback;
import com.topvision.platform.message.service.MessageService;

/**
 * @Create Date Sep 6, 2009 10:44:08 AM
 * 
 * @author kelers
 * 
 */
@Service("messageService")
public class MessageServiceImpl extends BaseService implements MessageService {
    private Map<Class<?>, EventListenerAggregate> regs = new HashMap<Class<?>, EventListenerAggregate>();
    private ArrayBlockingQueue<EmsEventObject<?>> eventQueue = new ArrayBlockingQueue<EmsEventObject<?>>(1024);
    private ExecutorService executorService;
    private final int maxThreads = 5;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.service.MessageService#addListener(java.lang .Class,
     * com.topvision.ems.message.event.EmsListener)
     */
    @Override
    public <T> void addListener(Class<T> clazz, EmsListener listener) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("addListener:" + listener);
        }
        EventListenerAggregate listeners = regs.get(clazz);
        if (listeners == null) {
            listeners = new EventListenerAggregate(clazz);
            regs.put(clazz, listeners);
        }
        listeners.add(listener);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.service.MessageService#onMessage(com.topvision.ems.message.event.EmsEventObject)
     */
    @Override
    public <T extends EmsEventObject<M>, M> void addMessage(T event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            getLogger().debug(e.getMessage(), e);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.zetaframework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        eventQueue = null;
        executorService.shutdownNow();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.service.MessageService#fireMessage(com.topvision.ems.message.event.EmsEventObject)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends EmsEventObject<M>, M> void fireMessage(T event) {
        List<M> list = (List<M>) getListeners(event.getListener());
        for (M listener : list) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("begin to fireMessage:{}.{}", listener, event.getActionName());
                }
                Method m = event.getListener().getMethod(event.getActionName(), event.getClass());
                // Added by Victor@201701017增加回调监视器的执行
                MessageCallback callback = event.getCallback();
                if (callback != null) {
                    callback.onMessageInvoked((Class<? extends EmsListener>) listener.getClass());
                }
                m.invoke(listener, event);
                if (callback != null) {
                    callback.onMessageFinished((Class<? extends EmsListener>) listener.getClass());
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("end of fireMessage:{}.{}", listener, event.getActionName());
                }
            } catch (IllegalArgumentException e) {
                getLogger().debug(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                getLogger().debug(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                getLogger().debug(e.getMessage(), e);
            } catch (SecurityException e) {
                getLogger().debug(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                getLogger().debug(e.getMessage(), e);
            } catch (Exception e) {
                getLogger().debug(e.getMessage(), e);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.service.MessageService#getListeners(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getListeners(Class<T> clazz) {
        List<T> returnList = new ArrayList<T>();

        EventListenerAggregate listeners = regs.get(clazz);
        if (listeners != null) {
            returnList.addAll((List<T>) Arrays.asList(listeners.getListenersInternal()));
        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("getListeners:" + returnList);
        }

        return returnList;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.zetaframework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        executorService = Executors.newFixedThreadPool(maxThreads);
        for (int i = 0; i < maxThreads; i++) {
            final String name = String.format("MessageService_%d", i);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName(name);
                    while (eventQueue != null) {
                        try {
                            EmsEventObject<?> event = eventQueue.take();
                            if (event == null) {
                                continue;
                            }
                            fireMessage(event);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.service.MessageService#removeListener(java.lang.Class,
     *      java.util.EventListener)
     */
    @Override
    public <T> boolean removeListener(Class<T> clazz, EventListener listener) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("removeListener:" + listener);
        }
        EventListenerAggregate listeners = regs.get(clazz);
        if (listeners != null) {
            return listeners.remove(listener);
        } else {
            return false;
        }
    }

    @Override
    public Map<Class<?>, EventListenerAggregate> getRegListeners() {
        return regs;
    }

    @Override
    public ArrayBlockingQueue<EmsEventObject<?>> getEventQueue() {
        return eventQueue;
    }
}
