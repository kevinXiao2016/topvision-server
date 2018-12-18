/***********************************************************************
 * $Id: EngineStatisticsDao.java,v1.0 2017年6月15日 下午7:01:50 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.dao;

import com.topvision.ems.engine.performance.ExecutorThreadSnap;

/**
 * 用于在engine端统计运维数据
 * 
 * @author vanzand
 * @created @2017年6月15日-下午7:01:50
 *
 */
public interface EngineStatisticsDao {
    void insertExecutorSnap(ExecutorThreadSnap snap);
}
