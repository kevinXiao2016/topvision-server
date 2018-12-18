/***********************************************************************
 * $Id: Cmc_bRouteServiceImpl.java,v1.0 2013-8-7 上午10:39:23 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.route.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.route.facade.CmcRouteFacade;
import com.topvision.ems.cmc.route.facade.domain.CmcRoute;
import com.topvision.ems.cmc.route.service.CmcRouteService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;

/**
 * @author dosion
 * @created @2013-8-7-上午10:39:23
 *
 */
@Service("cmcRouteService")
public class CmcRouteServiceImpl extends CmcBaseCommonService implements CmcRouteService {

    @Override
    public List<CmcRoute> getRouteConfigData(Long entityId) {
        snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        return getFacade(snmpParam.getIpAddress(), CmcRouteFacade.class).getRouteList(snmpParam);
    }

    @Override
    public void modifyRouteConfig(Long entityId, CmcRoute route) {
        snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        getFacade(snmpParam.getIpAddress(), CmcRouteFacade.class).modifyRouteConfig(snmpParam, route);
    }
    
}
