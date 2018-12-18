/***********************************************************************
 * $Id: BoardLoadconfigTrapExecute.java,v1.0 2017年1月9日 上午10:29:50 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.framework.service.BaseService;

/**
 * @author lizongtian
 * @created @2017年1月9日-上午10:29:50
 *
 */
@Service("BoardLoadconfigTrapExecute")
@EventExecute(code = "event.code.{EponCode.BD_PROV_FAIL}")
public class BoardLoadconfigTrapExecute extends BaseService implements TrapExceutor {

    @SuppressWarnings("rawtypes")
    @Autowired
    private DiscoveryService discoveryService;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        discoveryService.refresh(entityId);
    }
}
