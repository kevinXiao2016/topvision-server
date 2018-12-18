/***********************************************************************
 * $Id: CcmtsDeviceVersionServiceImpl.java,v1.0 2017年10月11日 下午4:13:48 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.versioncontrol.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.devicesupport.version.service.impl.DeviceVersionServiceImpl;

/**
 * @author vanzand
 * @created @2017年10月11日-下午4:13:48
 *
 */
@Service("ccmtsDeviceVersionService")
public class CcmtsDeviceVersionServiceImpl extends DeviceVersionServiceImpl {
    
    @Autowired
    private CmcService cmcService;
    
    @Override
    public String getEntityVersion(Long entityId) {
        if (entityId == null) {
            return null;
        }
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(entityId);
        if(cmcAttribute ==null) {
            return null;
        }
        String oltVersion = cmcAttribute.getOltVersion();
        if (oltVersion == null || "".equals(oltVersion)) {
            return cmcAttribute.getDolVersion();
        } else {
            return oltVersion;
        }
    }
}
