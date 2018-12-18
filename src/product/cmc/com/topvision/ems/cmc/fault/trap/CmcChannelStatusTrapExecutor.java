/***********************************************************************
 * $Id: CmcChannelOfflineTrapExecutor.java,v1.0 2017年1月13日 上午11:01:20 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.service.EntityService;

/**
 * @author Bravin
 * @created @2017年1月13日-上午11:01:20
 *
 */
@Service("CmcChannelStatusTrapExecutor")
@EventExecute(code = "event.code.{CmcTrapConstants.CMC_CHANNEL_OFFLINE},event.code.{CmcTrapConstants.CMC_CHANNEL_ONLINE}")
public class CmcChannelStatusTrapExecutor implements TrapExceutor {
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;
    @Autowired
    private EntityService entityService;

    @Override
    public void execute(TrapEvent evt) {
        Long cmcId = evt.getEntityId();
        Entity entity = entityService.getEntity(cmcId);
        Integer typeId = (int) entity.getTypeId();
        cmcUpChannelService.refreshUpChannelBaseInfo(cmcId, typeId, null);
        cmcDownChannelService.refreshDownChannelBaseInfo(cmcId, typeId);
    }

}
