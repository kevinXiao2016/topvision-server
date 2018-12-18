/***********************************************************************
 * $Id: MessageService.java,v 1.1 Sep 6, 2009 10:44:36 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.service;

import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import com.topvision.framework.service.Service;
import com.topvision.platform.message.event.EmsEventObject;
import com.topvision.platform.message.event.EmsListener;
import com.topvision.platform.message.event.EventListenerAggregate;

/**
 * @Create Date Sep 6, 2009 10:44:36 AM
 * 
 * @author kelers
 * 
 */
public interface MessageService extends Service {
    /**
     * 添加监听器
     * 
     * @param <T>
     * @param clazz
     * @param listener
     */
    <T> void addListener(Class<T> clazz, EmsListener listener);

    /**
     * 延时执行
     * 
     * @param <T>
     * @param <M>
     * @param event
     */
    <T extends EmsEventObject<M>, M> void addMessage(T event);

    /**
     * 立即运行
     * 
     * @param <T>
     * @param <M>
     * @param event
     */
    <T extends EmsEventObject<M>, M> void fireMessage(T event);

    /**
     * 根据监视器类型获取监听对象列表
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    <T> List<T> getListeners(Class<T> clazz);

    /**
     * 删除监听器
     * 
     * @param <T>
     * @param clazz
     * @param listener
     * @return
     */
    <T> boolean removeListener(Class<T> clazz, EventListener listener);

    /**
     * 返回所有注册的消息处理程序，用于admin进行查看
     * @return 
     */
    public Map<Class<?>, EventListenerAggregate> getRegListeners();

    /**
     * 返回所有注册的消息处理程序，用于admin进行查看
     * @return 
     */
    public ArrayBlockingQueue<EmsEventObject<?>> getEventQueue();
}
