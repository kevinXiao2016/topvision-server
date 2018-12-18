/***********************************************************************
 * $Id: PnmpMonitorCmService.java,v1.0 2017年8月8日 下午4:36:14 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:36:14
 *
 */
public interface PnmpMonitorCmService {

    /**
     * 获取所有中频监控CM MAC
     * 
     * @return
     */
    List<String> selectAllMiddleMonitorCmMac();

    /**
     * 获取所有中频监控CM列表(包含信息)
     * 
     * @return
     */
    List<PnmpCmData> selectAllMiddleMonitorCmList();

    /**
     * 获取所有中频监控CM列表
     * 
     * @return
     */
    List<PnmpCmData> getAllMiddleMonitorCmList();

    /**
     * 获取所有高频监控CM列表
     * 
     * @return
     */
    List<PnmpCmData> getAllHighMonitorCmList();

    /**
     * 获取符合条件的高频监控CM列表
     * 
     * @return
     */
    List<PnmpCmData> getHighMonitorCmList(Map<String, Object> paraMap);

    /**
     * 获取所有高频监控CM数量
     * 
     * @return
     */
    Integer getAllHighMonitorCmListNum();

    /**
     * 获取所有中频监控CM数量
     * 
     * @return
     */
    Integer getAllMiddleMonitorCmListNum();

    /**
     * 根据条件获取高频监控CM列表总数
     * 
     * @param queryMap
     * @return
     */
    Integer getHighMonitorCmListNum(Map<String, Object> queryMap);

    /**
     * 插入中频监控CM
     */
    void addMiddleMonitorCm(String cmMac);

    /**
     * 删除中频监控CM
     */
    void removeMiddleMonitorCm(String cmMac);

    /**
     * 插入高频监控CM
     * 
     * return 1：加入成功； 2：已在高频采集队列； 3：不在网管中
     */
    Integer addHighMonitorCm(Map<String, Object> queryMap);

    /**
     * 删除高频监控CM
     */
    void removeHighMonitorCm(String cmMac);

    /**
     * 批量删除高频监控队列中的CM
     * 
     * @param cmMacs
     */
    void batchRemoveHighMonitorCm(List<String> cmMacs);

    /**
     * 根据cm mac获取 cmc ipAddress, alias
     * 
     * @param queryMap
     * @return
     */
    Map<String, Object> getCmcAttributeByCmMac(Map<String, Object> queryMap);

}
