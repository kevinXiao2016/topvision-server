/***********************************************************************
 * $Id: DispersionService.java,v1.0 2015-3-12 下午2:10:13 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.dispersion.domain.Dispersion;

/**
 * @author fanzidong
 * @created @2015-3-12-下午2:10:13
 * 
 */
public interface DispersionService {

    /**
     * 查询离散度列表
     * 
     * @param queryMap
     * @return
     */
    List<Dispersion> getDispersionList(Map<String, Object> queryMap);

    /**
     * 根据ID查询对应的光节点
     * 
     * @param opticalNodeId
     * @return
     */
    Dispersion getDispersionById(Long opticalNodeId);

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
     * @param opticalId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Dispersion> selectDispersionsByIdAndRange(Long opticalId, String startTime, String endTime);

    /**
     * 获取指定光节点在某时刻的离散度数据
     * 
     * @param opticalNodeId
     * @param exactTime
     * @return
     */
    Dispersion loadDispersionByIdAndTime(Long opticalNodeId, String exactTime);

}
