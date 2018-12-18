/***********************************************************************
 * $Id: CmcFlapClearService.java,v1.0 2017年2月8日 上午9:17:21 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import org.quartz.SchedulerException;

/**
 * @author lizongtian
 * @created @2017年2月8日-上午9:17:21
 *
 */
public interface CmcFlapClearService {

    void resetClearTrigger(String flapClearPeriod) throws SchedulerException;

}
