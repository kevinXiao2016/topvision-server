/***********************************************************************
 * $Id: DispersionDao.java,v1.0 2015-3-12 下午2:17:02 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.dispersion.domain.Dispersion;

/**
 * @author fanzidong
 * @created @2015-3-12-下午2:17:02
 * 
 */
public interface DispersionDao {
    /**
     * 查询离散度列表
     * 
     * @param queryMap
     * @return
     */
    List<Dispersion> selectDispersionList(Map<String, Object> queryMap);

    /**
     * 获取符合指定查询条件的离散度不分页个数
     * 
     * @param queryMap
     * @return
     */
    Integer getDispersionListNum(Map<String, Object> queryMap);

    /**
     * 获取指定光节点在指定范围内的离散度数据
     * 
     * @param opticalNodeId
     * @param startTime
     *            (format: yyyy-m-d h:i:s)
     * @param endTime
     *            (format: yyyy-m-d h:i:s)
     * @return
     */
    List<Dispersion> selectDispersionsByIdAndRange(Long opticalNodeId, String startTime, String endTime);

    /**
     * 获取指定光节点在某时刻的离散度数据
     * 
     * @param opticalNodeId
     * @param exactTime
     *            (format: yyyy-m-d h:i:s)
     * @return
     */
    Dispersion selectDispersionByIdAndTime(Long opticalNodeId, String exactTime);

    /**
     * 批量插入光节点离散度数据
     * 
     * @param dispersions
     */
    void batchInsertDispersions(List<Dispersion> dispersions);

    /**
     * 根据ID查询对应的光节点
     * 
     * @param opticalNodeId
     * @return
     */
    Dispersion getDispersionById(Long opticalNodeId);
}
