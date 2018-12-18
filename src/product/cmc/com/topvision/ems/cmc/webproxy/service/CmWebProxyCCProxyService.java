/***********************************************************************
 * CmWebProxyCCProxyService.java,v1.0 17-5-9 上午10:42 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.service;

import com.topvision.ems.cmc.webproxy.domain.CmWebCCProxy;

/**
 * @author jay
 * @created 17-5-9 上午10:42
 */
public interface CmWebProxyCCProxyService {
    CmWebCCProxy selectCCProxyByCmcId(Long cmcId);

    CmWebCCProxy pickCCProxyByCmcId(Long heartbeatId, Long cmcId);

    void releaseCCProxyByCmcId(Long heartbeatId, Long cmcId);

    public String listInfo();
}
