/***********************************************************************
 * $Id: BfsxServiceImpl.java,v1.0 2014年9月23日 下午2:43:50 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.service.BfsxEntitySnapService;
import com.topvision.ems.network.service.BfsxService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.OnlineService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.exception.engine.PingException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2014年9月23日-下午2:43:50
 *
 */
@Service
public class BfsxServiceImpl extends BaseService implements BfsxService {
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnlineService onlineService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void refreshEntity(long entityId) {
        Entity entity = entityService.getEntity(entityId);
        try {
            long delay = onlineService.ping(entity.getIp());
            if (delay < 0) {
                updateEntitySnapState(entity, false);
                throw new PingException("discoveryPing");
            }
            updateEntitySnapState(entity, true);
        } catch (Exception e) {
            updateEntitySnapState(entity, false);
            throw new PingException("discoveryPing", e);
        }
        EntityType entityType = entityTypeService.getEntityType(entity.getTypeId());
        BfsxEntitySnapService service = (BfsxEntitySnapService) beanFactory
                .getBean(String.format("%sSnapService", entityType.getModule()));
        service.refreshSnapInfo(entityId, entity.getTypeId());
    }

    public void updateEntitySnapState(Entity entity, boolean state) {
        EntityValueEvent event = new EntityValueEvent(entity.getEntityId());
        event.setEntityId(entity.getEntityId());
        event.setState(state);
        event.setActionName("performanceChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);
    }
}
