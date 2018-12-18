/***********************************************************************
 * $Id: CmtsSnapServiceImpl.java,v1.0 2014年9月23日 下午1:54:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmts.dao.CmtsBaseDao;
import com.topvision.ems.cmts.domain.CmtsBaseInfo;
import com.topvision.ems.cmts.topology.facade.CmtsDiscoveryFacade;
import com.topvision.ems.network.service.BfsxEntitySnapService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Bravin
 * @created @2014年9月23日-下午1:54:47
 *
 */
@Service("cmtsSnapService")
public class CmtsSnapServiceImpl extends BaseService implements BfsxEntitySnapService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    CmtsBaseDao cmtsBaseDao;

    @Override
    public void refreshSnapInfo(Long entityId, Long typeId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        CmtsDiscoveryFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), CmtsDiscoveryFacade.class);
        CmtsBaseInfo cmtsInfo = facade.getCmtsBaseInfo(snmpParam);
        cmtsInfo.setEntityId(entityId);
        entityService.updateEntityDeviceName(entityId, cmtsInfo.getSysName());
        entityService.updateSnapSysUptime(entityId, cmtsInfo.getSysUpTime());
        cmtsBaseDao.updateCmtsBaseInfo(cmtsInfo);
    }

}
