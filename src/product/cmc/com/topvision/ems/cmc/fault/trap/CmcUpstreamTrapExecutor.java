/***********************************************************************
 * $Id: CmcUpstreamCloseTrapExecutor.java,v1.0 2017年1月13日 上午10:54:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.service.EntityService;

/**
 * @author Bravin
 * @created @2017年1月13日-上午10:54:51
 *
 */
@Service("CmcUpstreamTrapExecutor")
@EventExecute(code = "event.code.{CmcTrapConstants.CMC_US_CLOSE},event.code.{CmcTrapConstants.CMC_US_OPEN},event.code.{CmcTrapConstants.CMC_US_PARAM}")
public class CmcUpstreamTrapExecutor implements TrapExceutor {
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private EntityService entityService;

    @Override
    public void execute(TrapEvent evt) {
        Long cmcId = evt.getEntityId();
        Entity entity = entityService.getEntity(cmcId);
        Integer typeId = (int) entity.getTypeId();
        cmcUpChannelService.refreshUpChannelBaseInfo(cmcId, typeId, null);
    }
}
