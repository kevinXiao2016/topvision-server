/***********************************************************************
 * $Id: TrapCallback.java,v 1.1 2009-9-30 下午12:27:44 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import com.topvision.framework.annotation.Callback;

/**
 * @Create Date 2009-9-30 下午12:27:44
 * 
 * @author kelers
 * 
 */
@Callback(beanName = "trapService", serviceName = "trapService")
public interface TrapCallback {
    /**
     * 用户收取Trap后处理
     * 
     * @param trap
     */
    void onTrap(Trap trap);
}
