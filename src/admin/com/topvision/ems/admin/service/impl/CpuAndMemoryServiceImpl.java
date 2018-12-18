/***********************************************************************
 * $Id: CpuAndMemoryServiceImpl.java,v1.0 2014年6月24日 下午2:56:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.admin.dao.SystemMonitorDao;
import com.topvision.ems.admin.domain.SystemMonitor;
import com.topvision.ems.admin.service.CpuAndMemoryService;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2014年6月24日-下午2:56:43
 *
 */
@Service("cpuAndMemoryService")
public class CpuAndMemoryServiceImpl extends BaseService implements CpuAndMemoryService {
    @Autowired
    private SystemMonitorDao systemMonitorDao;

    @Override
    public List<SystemMonitor> loadCpuAndMemory(Map<String, Object> map) {
        return systemMonitorDao.loadCpuAndMemory(map);
    }

}
