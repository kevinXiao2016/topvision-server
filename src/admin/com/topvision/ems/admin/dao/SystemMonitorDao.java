/***********************************************************************
 * $Id: SystemMonitorDao.java,v1.0 2014-6-24 上午9:02:33 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.admin.domain.SystemMonitor;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Victor
 * @created @2014-6-24-上午9:02:33
 *
 */
public interface SystemMonitorDao extends BaseEntityDao<SystemMonitor> {

    /**
     * 加载CPU和内存的信息
     * @param map
     * @return
     */
    List<SystemMonitor> loadCpuAndMemory(Map<String, Object> map);

}
