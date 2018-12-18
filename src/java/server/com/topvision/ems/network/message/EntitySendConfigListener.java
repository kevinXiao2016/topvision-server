/***********************************************************************
 * $Id: EntitySendConfigListener.java,v1.0 2014年8月23日 上午10:05:47 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.message;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.network.service.CommandSendService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author loyal
 * @created @2014年8月23日-上午10:05:47
 *
 */
public class EntitySendConfigListener implements EntityListener{
    private EntityService entityService;
    private MessageService messageService;
    private CommandSendService commandSendService;
    
    public void init() {
        messageService.addListener(EntityListener.class, this);
    }
    
    @Override
    public void entityAdded(EntityEvent event) {
    }
    @Override
    public void entityDiscovered(EntityEvent event) {
    }
    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }
    @Override
    public void entityChanged(EntityEvent event) {
    }
    @Override
    public void entityRemoved(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        if (entityId != null) {
            List<Long> entityList = new ArrayList<Long>();
            entityList.add(entityId);
            commandSendService.deleteSendConfigEntity(entityList);
            }
    }
    @Override
    public void managerChanged(EntityEvent event) {
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public CommandSendService getCommandSendService() {
        return commandSendService;
    }

    public void setCommandSendService(CommandSendService commandSendService) {
        this.commandSendService = commandSendService;
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

}
