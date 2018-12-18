/***********************************************************************
 * $Id: OltRealtimeDao.java,v1.0 2014-7-14 下午4:47:23 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.dao;

import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2014-7-14-下午4:47:23
 *
 */
public interface OltRealtimeDao extends BaseEntityDao<Object> {

    /**
     * 查询光功率阈值
     * @param entityId
     * @param ruleName
     * @return
     */
    PerfThresholdRule queryOpticalThreshold(Long entityId, String ruleName);

    /**
     * 查询设备别名
     * @param entityId
     * @param mac
     * @return
     */
    String queryDeviceAlias(Long entityId, String mac);

}
