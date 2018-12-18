/***********************************************************************
 * CmWebProxyCmPortMapService.java,v1.0 17-4-25 上午11:25 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.service;

import com.topvision.ems.cmc.webproxy.domain.CmWebPortMap;

/**
 * @author jay
 * @created 17-4-25 上午11:25
 */
public interface CmWebProxyCmPortMapService {
    /**
     * 通过cmId查询映射端口
     *
     *
     * @param cmId
     * @return
     */
    CmWebPortMap selectPortByCmId(Long cmId);

    /**
     * 通过cmId建立一个映射端口
     *
     *
     *
     * @param cmId
     * @param natIp
     *@param mangerIp
     * @param proxyPort   @return
     */
    CmWebPortMap pickPortByCmId(Long heartbeatId, Long cmId, String natIp, String mangerIp, Integer proxyPort);

    /**
     * 释放一个客户端的PortMap
     * @param heartbeatId
     * @param cmId
     */
    void releasePortByCmId(Long heartbeatId, Long cmId);

    String listInfo();
}
