/***********************************************************************
 * $Id: CpuAndMemoryService.java,v1.0 2014年6月24日 下午2:56:31 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.admin.domain.SystemMonitor;

/**
 * @author Bravin
 * @created @2014年6月24日-下午2:56:31
 *
 */
public interface CpuAndMemoryService {

    /**
     * 加载CPU和内存的信息
     * @param map
     * @return
     */
    List<SystemMonitor> loadCpuAndMemory(Map<String, Object> map);

}
