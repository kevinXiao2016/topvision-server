/***********************************************************************
 * $Id: PnmpMonitorCmDao.java,v1.0 2017年8月8日 下午2:57:28 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午2:57:28
 *
 */
public interface PnmpMonitorCmDao {

    Integer selectOnlineCmNum();

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
     * 获取所有高频监控CM MAC
     * 
     * @return
     */
    List<String> selectAllHighMonitorCmMac();

    /**
     * 获取所有高频监控CM列表
     * 
     * @return
     */
    List<PnmpCmData> selectAllHighMonitorCmList();

    /**
     * 获取符合条件的高频监控CM列表
     * 
     * @param queryMap
     * @return
     */
    List<PnmpCmData> selectHighMonitorCmList(Map<String, Object> queryMap);

    /**
     * 获取所有高频监控CM列表总数
     * 
     * @return
     */
    Integer selectAllHighMonitorCmListNum();

    /**
     * 获取所有中频监控CM列表总数
     * 
     * @return
     */
    Integer selectAllMiddleMonitorCmListNum();

    /**
     * 根据条件获取高频监控CM列表总数
     * 
     * @param queryMap
     * @return
     */
    Integer selectHighMonitorCmListNum(Map<String, Object> queryMap);

    /**
     * 从中频队列移除CM
     * 
     * @param cmMac
     */
    void deleteMiddleMonitorCm(String cmMac);

    /**
     * 增加CM到中频队列
     * 
     * @param cmMac
     */
    void insertMiddleMonitorCm(String cmMac);

    /**
     * 增加CM到高频队列
     * 
     * @param cmMac
     */
    void insertHighMonitorCm(String cmMac);

    /**
     * 查询cmMac，用于判断该cmMac是否在高频采集
     * 
     * @param queryMap
     * @return
     */
    String selectHighMonitorCmByPK(Map<String, Object> queryMap);

    /**
     * 根据cmMac 查询cmc的ipAddress, alias
     * 
     * @param queryMap
     * @return
     */
    Map<String, Object> selectCmcAttributeByCmMac(Map<String, Object> queryMap);

    /**
     * 从高频队列移除CM
     * 
     * @param cmMac
     */
    void deleteHighMonitorCm(String cmMac);

    /**
     * 批量从高频监控队列中移除CM
     * 
     * @param cmMacs
     */
    void batchDeleteHighMonitorCm(List<String> cmMacs);

}
