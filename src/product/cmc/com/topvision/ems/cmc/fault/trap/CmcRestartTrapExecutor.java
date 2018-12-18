/***********************************************************************
 * $Id: CmcRestartTrapExecutor.java,v1.0 2017年1月13日 上午10:53:09 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;

/**
 * @author Bravin
 * @created @2017年1月13日-上午10:53:09
 *
 */
@Service("CmcRestartTrapExecutor")
@EventExecute(code = "event.code.{CmcTrapConstants.CMC_RESTART},event.code.{CmcTrapConstants.CMC_RESET}")
public class CmcRestartTrapExecutor implements TrapExceutor {
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityDao entityDao;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        Entity entity = entityService.getEntity(entityId);
        if (entity != null) {
            EntitySnap entitySnap = new EntitySnap();
            entitySnap.setEntityId(entityId);
            entitySnap.setState(false);
            entityDao.updateOnuEntitySnap(entitySnap);
            cmcService.updateCmcStatus(entityId, CmcConstants.TOPCCMTSSYSSTATUS_OFFLINE);
        }
    }

}
