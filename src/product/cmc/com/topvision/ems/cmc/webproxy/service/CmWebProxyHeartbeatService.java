/***********************************************************************
 * CmWebProxyHeartbeatService.java,v1.0 17-4-25 上午11:29 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.service;

import com.topvision.ems.cmc.webproxy.domain.CmWebStatus;

/**
 * @author jay
 * @created 17-4-25 上午11:29
 */
public interface CmWebProxyHeartbeatService {

    /**
     * 添加一个单机web心跳
     *
     * @return heartbeatId
     */
    CmWebStatus addHeartbeat(Long cmId);

    /**
     * 定期由页面传递过来的心跳
     * @param heartbeatId
     */
    CmWebStatus pickCCProxyByCmcId(Long heartbeatId,Long cmId);

    /**
     * 定期由页面传递过来的心跳
     * @param heartbeatId
     */
    CmWebStatus pickPortByCmId(Long heartbeatId,Long cmId,String manageIp,Integer proxyPort);

    /**
     * 释放一个单机web心跳
     * @param heartbeatId
     */
    void deleteHeartbeat(Long heartbeatId);

    /**
     * 定期由页面传递过来的心跳
     * @param heartbeatId
     */
    CmWebStatus heartbeat(Long heartbeatId);

    CmWebStatus selectPortByCmId(Long heartbeatId, Long cmId);

    CmWebStatus selectCCProxyByCmcId(Long heartbeatId, Long cmcId);

    String listInfo();
}
