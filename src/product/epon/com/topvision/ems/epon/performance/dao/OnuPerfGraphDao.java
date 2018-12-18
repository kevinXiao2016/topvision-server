/***********************************************************************
 * $Id: OnuPerfGraphDao.java,v1.0 2015-4-27 下午03:08:48 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author lizongtian
 * @created @2015-4-27-下午03:08:48
 *   
 */
public interface OnuPerfGraphDao extends BaseEntityDao<Entity> {
    /**
     * 加载光链路质量性能数据
     * 
     * @param onuId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> selectOnuOptPerfPoints(Long onuId, String perfTarget, String startTime, String endTime);

    /**
     * 查询ONU速率性能数据
     * @param paramMap
     * @return
     */
    List<Point> queryOnuFlowPerfPoints(Map<String, Object> paramMap);
}
