/***********************************************************************
 * $Id: Cmc_bRouteService.java,v1.0 2013-8-7 上午10:21:58 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.route.service;

import java.util.List;

import com.topvision.ems.cmc.route.facade.domain.CmcRoute;
import com.topvision.framework.service.Service;

/**
 * @author dosion
 * @created @2013-8-7-上午10:21:58
 *
 */
public interface CmcRouteService extends Service {
    
    public List<CmcRoute> getRouteConfigData(Long entityId);
    
    public void modifyRouteConfig(Long entityId, CmcRoute route);

}
