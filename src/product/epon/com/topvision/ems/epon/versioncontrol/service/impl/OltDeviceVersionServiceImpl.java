/***********************************************************************
 * $Id: OltDeviceVersionServiceImpl.java,v1.0 2017年10月11日 下午3:53:33 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.versioncontrol.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.impl.DeviceVersionServiceImpl;
import com.topvision.ems.epon.versioncontrol.service.impl.dao.OltDeviceVersionDao;

/**
 * @author vanzand
 * @created @2017年10月11日-下午3:53:33
 *
 */
@Service("oltDeviceVersionService")
public class OltDeviceVersionServiceImpl extends DeviceVersionServiceImpl {
    
    @Autowired
    private OltDeviceVersionDao oltDeviceVersionDao;
    
    @Override
    public String getEntityVersion(Long entityId) {
        if(entityId == null) {
            return null;
        }
        return oltDeviceVersionDao.getDeviceVersion(entityId);
    }

}
