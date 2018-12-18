/***********************************************************************
 * $ AbstractConfigWork.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/

package com.topvision.ems.template.works;

import com.topvision.platform.message.event.WorkEvent;
import com.topvision.platform.message.event.WorkListener;
import com.topvision.platform.message.service.MessageService;


/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
public abstract class AbstractConfigWork implements ConfigWork {
    protected void workStart(MessageService messageService) {
        WorkEvent wEvent = new WorkEvent("");
        wEvent.setActionName("workStart");
        wEvent.setListener(WorkListener.class);
        messageService.addMessage(wEvent);
    }

    protected void workEnd(MessageService messageService) {
        WorkEvent wEvent = new WorkEvent("");
        wEvent.setActionName("workEnd");
        wEvent.setListener(WorkListener.class);
        messageService.addMessage(wEvent);
    }

    protected void failure(MessageService messageService) {
        WorkEvent wEvent = new WorkEvent("");
        wEvent.setActionName("failure");
        wEvent.setListener(WorkListener.class);
        messageService.addMessage(wEvent);
    }

    protected void success(MessageService messageService) {
        WorkEvent wEvent = new WorkEvent("");
        wEvent.setActionName("success");
        wEvent.setListener(WorkListener.class);
        messageService.addMessage(wEvent);
    }
}
