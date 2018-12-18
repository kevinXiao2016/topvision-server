/***********************************************************************
 * $Id: CmcAlertClearCallback.java,v1.0 2015-11-20 上午10:41:26 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.callback;

import com.topvision.framework.annotation.Callback;

/**
 * @author Administrator
 * @created @2015-11-20-上午10:41:26
 *
 */
@Callback(beanName = "cmcAlertClearService", serviceName = "cmcAlertClearService")
public interface CmcAlertClearCallback {

    /**
     * 清除cc告警
     * @param cmcId
     */
    void clearCmcAlert(Long cmcId);

}
