/***********************************************************************
 * $Id: ActionService.java,v 1.1 Sep 24, 2008 12:42:28 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import com.topvision.framework.exception.service.ServiceException;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.Action;

/**
 * @author kelers
 * @Create Date Sep 24, 2008 12:42:28 PM
 */
public interface ActionService extends Service {

    /**
     * 用于判断需要服务器设置的动作判断服务器是否设置
     * 
     * @return Boolean
     */
    Boolean isServerSetting();

    void reset();

    /**
     * 触发行为
     * 
     * @param action
     *            行为对象
     * @param alert
     *            告警对象
     * @param msg
     *            发送的消息
     * @throws ServiceException
     *             该行为会抛出的异常
     */
    void sendAction(Action action, Object object, String msg) throws ServiceException;
    String sendActionBak(Action action, Object object, String msg) throws ServiceException;
    
    /**
     * 短信服务器连通状态
     * smsServerIp 服务器ip
     * smsServicePort 服务端口号
     */
    String checkConnection(String smsServerIp,int smsServicePort);
}
