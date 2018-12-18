/***********************************************************************
 * $Id: PerfTargetData.java,v1.0 2014-2-13 下午2:28:30 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

/**
 * @author Rod John
 * @created @2014-2-13-下午2:28:30
 *
 */
public interface PerfTargetData {
  
    /**
     * 获得修改性能采集指标需要的数据
     * 
     * @param entityId
     * @param targetName
     * @return
     */
    Object perfTargetChangeData(Long entityId, String targetName);
    
    
}
