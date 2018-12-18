/***********************************************************************
 * $ com.topvision.ems.performance.service.PerformanceCallback,v1.0 2012-5-6 9:05:15 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade.callback;


import com.topvision.framework.annotation.Callback;

/**
 * @author jay
 * @created @2012-5-6-9:05:15
 */
@Callback(beanName = "cmcPerformanceService", serviceName = "CmcPerformanceService")
public interface CmcPerformanceCallback {
    public void test();
    public Boolean isSupportCmPreStatus(Long cmcId);
    public Boolean isSupportCmDocsisMode(Long cmcId);
}
