/***********************************************************************
 * $Id: OltDeviceVersionDao.java,v1.0 2017年10月11日 下午4:04:36 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.versioncontrol.service.impl.dao;

import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author vanzand
 * @created @2017年10月11日-下午4:04:36
 *
 */
public interface OltDeviceVersionDao extends BaseEntityDao<Object> {

    /**
     * 获取指定设备的版本控制
     * 
     * @param entityId
     * @return
     */
    String getDeviceVersion(Long entityId);
}
