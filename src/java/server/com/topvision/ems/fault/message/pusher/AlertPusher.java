/***********************************************************************
 * $Id: AlertPusher.java,v1.0 2012-12-22 上午10:34:06 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.message.pusher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.message.AlertEvent;
import com.topvision.ems.fault.message.AlertListener;
import com.topvision.ems.fault.service.LevelManager;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author Victor
 * @created @2012-12-22-上午10:34:06
 * 
 */
@Service("alertPusher")
public class AlertPusher extends BaseService implements AlertListener {
    private static Logger logger = LoggerFactory.getLogger(AlertPusher.class);
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private MessageService messageService;

    @Override
    @PostConstruct
    public void initialize() {
        messageService.addListener(AlertListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        messageService.removeListener(AlertListener.class, this);
    }

    /**
     * 告警事件处理方法。每当接受到一个Trap时被调用
     * 
     * @see com.topvision.ems.message.event.AlertListener#alertAdded(com.topvision.ems.message.event.AlertEvent)
     */
    @Override
    public void alertAdded(AlertEvent event) {

        // @Modiby By Rod At Authority
        event.getAlert().setLevelName(LevelManager.getInstance().getLevelName(event.getAlert().getLevelId()));
        event.getAlert().setFirstTimeStr(DateUtils.format(event.getAlert().getFirstTime()));
        // 处理温度转换
        try {
            event.getAlert().setMessage(UnitConfigConstant.parseUnitConfigAlertMsg(event.getAlert().getTypeId(),
                    event.getAlert().getMessage()));
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        // -----将当前告警添加到告警发送队列中（不是准备队列）-----//
        Message msg = new Message("alert");
        event.getAlert().setUserObject(null);
        msg.setData(event.getAlert());
        if (event.getAlert() != null && event.getAlert().getTypeId() != null) {
            msg.setId(event.getAlert().getTypeId().toString());
        }
        messagePusher.sendMessage(msg);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.AlertListener#alertCleared(com.topvision.ems.message.event.AlertEvent)
     */
    @Override
    public void alertCleared(AlertEvent event) {
        try {
            Message msg = new Message("alert");
            msg.setData(event.getAlert());
            if (event.getAlert() != null && event.getAlert().getTypeId() != null) {
                msg.setId(event.getAlert().getTypeId().toString());
            }
            messagePusher.sendMessage(msg);
        } catch (Exception ex) {
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.AlertListener#alertConfirmed(com.topvision.ems.message.event.AlertEvent)
     */
    @Override
    public void alertConfirmed(AlertEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.AlertListener#alertDeleted(com.topvision.ems.message.event.AlertEvent)
     */
    @Override
    public void alertDeleted(AlertEvent event) {
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * @param messagePusher
     *            the messagePusher to set
     */
    public void setMessagePusher(MessagePusher messagePusher) {
        this.messagePusher = messagePusher;
    }

}
