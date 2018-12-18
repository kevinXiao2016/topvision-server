/***********************************************************************
 * $Id: UpgradeStatusService.java,v1.0 2014年11月22日 下午1:30:21 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service;

import java.util.Map;

import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年11月22日-下午1:30:21
 *
 */
public interface UpgradeStatusService extends Service{
    
    public Map<Integer, String> getAllStatus();

    Map<Integer, String> getResultStatus();

    Map<Integer, String> getErrorStatus();

    Map<Integer, String> getNowStatus();

}
