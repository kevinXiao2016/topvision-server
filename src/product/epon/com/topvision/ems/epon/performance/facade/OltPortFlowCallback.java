/***********************************************************************
 * $Id: OltPortFlowFacade.java,v1.0 2014年10月28日 下午7:56:19 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.util.List;

import com.topvision.framework.annotation.Callback;

/**
 * @author Bravin
 * @created @2014年10月28日-下午7:56:19
 *
 */
@Callback(beanName = "eponStatsService", serviceName = "eponStatsService")
public interface OltPortFlowCallback {
    /**
     * 获取需要采集的SNI端口列表
     * @param entityId
     * @return
     */
    List<Long> getOltSniIndexList(Long entityId);

    /**
     * 获取需要采集的PON端口列表
     * @param entityId
     * @return
     */
    List<Long> getOltPonIndexList(Long entityId);
}
