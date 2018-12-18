/***********************************************************************
 * $Id: OltPerfService.java,v1.0 2013-10-25 下午3:43:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.service;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午3:43:54
 *
 */
public interface EponStatsService extends Service {
    /**
     * 判断是否存在Epon的采集模块
     * 
     * @param entityId
     * @param category
     * @return
     */
    public boolean hasEponMonitor(Long entityId, String category);

    /**
     * 开启EPON的在线状态采集
     * 
     * @param entityId
     */
    public void startEponOnlineQuality(Long entityId, Long typeId);
    
    /**
     * 关闭EPON的在线状态采集
     * 
     * @param entityId
     */
    public void stopEponOnlineQuality(Long entityId);
    
    /**
     * 开启EPON的服务质量采集
     * 
     * @param entityId
     */
    public void startEponServiceQuality(Long entityId, Long typeId);

    /**
     * 关闭EPON的服务质量
     * 
     * @param entityId
     */
    public void stopEponServiceQuality(Long entityId);

    /**
     * 修改EPON的指标质量采集
     * 
     * @param entityId
     */
    public void modifyEponTargetQuality(Long entityId, String targetName, Integer lastInterval, Integer newInterval);

    /**
     * 开启EPON的链路质量采集
     * 
     * @param entityId
     */
    public void startEponLinkQuality(Long entityId, Long typeId);

    /**
     * 关闭EPON的链路质量采集
     * 
     * @param entityId
     */
    public void stopEponLinkQuality(Long entityId);

    /**
     * 开启EPON的端口流量采集
     * 
     * @param entityId
     */
    public void startEponFlowQuality(Long entityId, Long typeId);

    /**
     * 关闭EPON的端口流量采集
     * 
     * @param entityId
     */
    public void stopEponFlowQuality(Long entityId);

    /**
     * 获得OLT的指标索引
     * 
     * @param entityId
     * @param targetName
     */
    public List<Long> getModifyOltTargetIndexs(Long entityId, String targetName);

    /**
     * 开启OLT设备的性能采集配置
     * 
     * Use for Topology
     * 
     * startOltPerfCollect
     * 
     * @param entity
     */
    public void startOltPerfCollect(Entity entity);
    
    /**
     * Add by Rod 
     * 
     * Use for Topology
     * 
     * stopOltPerfCollect 
     * 
     * @param entity
     */
    public void stopOltPerfCollect(Entity entity);
    
    
    

}
