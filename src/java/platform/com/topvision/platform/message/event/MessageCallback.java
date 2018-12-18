/***********************************************************************
 * $Id: MessageCallback.java,v1.0 2017年1月17日 上午11:58:05 $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * 此接口为消息执行监听回调接口， 支持消息被某个listener执行开始和完成的监听。
 * 
 * @author Victorli
 * @created @2017年1月17日-上午11:58:05
 *
 */
public interface MessageCallback {
    /**
     * 告知消息发起者消息被某个listener开始执行
     * 
     * @param listener
     *            执行消息的listener实现类 。
     * 
     *            eg）在拓扑发现发送消息OnuEntityEvent时，setCallback加入回调，
     *            则在OnuDiscoveryServiceImpl开始执行消息前后都会触发callback，并在callback调用时带入执行类
     *            com.topvision.ems.epon.topology.service.impl.OnuDiscoveryServiceImpl，
     *            此参数用于回调时区分不同的消息执行者，可以忽略
     */
    void onMessageInvoked(Class<? extends EmsListener> listener);

    /**
     * 告知消息发起者消息被某个listener执行完成
     * 
     * @param listener
     *            执行消息的listener实现类 。
     */
    void onMessageFinished(Class<? extends EmsListener> listener);
}
