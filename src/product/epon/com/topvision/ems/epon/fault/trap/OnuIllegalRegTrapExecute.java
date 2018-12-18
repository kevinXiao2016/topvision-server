/***********************************************************************
 * $Id: OnuIllegalRegTrapExecute.java,v1.0 2017年1月9日 上午10:56:08 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.framework.service.BaseService;

/**
 * @author lizongtian
 * @created @2017年1月9日-上午10:56:08
 *
 */
@Service("OnuIllegalRegTrapExecute")
@EventExecute(code = "event.code.{EponCode.ONU_ILLEGALREGISTER},event.code.{EponCode.ONU_MAC_AUTH_ERROR}")
public class OnuIllegalRegTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private OnuAuthService onuAuthService;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        //刷新阻塞表
        onuAuthService.refreshOnuAuthenBlockList(entityId);
        /*onuAuthService.refreshOnuAuthInfo(entityId);
        onuAuthService.refreshOnuAuthMode(entityId);
        onuAuthService.refreshOnuAuthEnable(entityId);
        onuAuthService.refreshOnuAuthPreType(entityId);*/
    }

}
