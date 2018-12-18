/***********************************************************************
 * $Id: BoardOfflineTrapExecute.java,v1.0 2017年1月9日 上午10:04:50 $
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
 * @created @2017年1月9日-上午10:04:50
 * 处理板卡拔出、板卡离线、板卡重启三种事件
 */
@Service("BoardOfflineTrapExecute")
@EventExecute(code = "event.code.{EponCode.BD_OFFLINE},event.code.{EponCode.BD_REMOVE},event.code.{EponCode.BD_RESET}")
public class BoardOfflineTrapExecute extends BaseService implements TrapExceutor {

    @SuppressWarnings("rawtypes")
    @Autowired
    private DiscoveryService discoveryService;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        discoveryService.refresh(entityId);
    }

}
