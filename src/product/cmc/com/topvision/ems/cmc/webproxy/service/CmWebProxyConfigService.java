/***********************************************************************
 * CmWebProxyConfigService.java,v1.0 17-4-24 下午3:04 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.service;

/**
 * @author jay
 * @created 17-4-24 下午3:04
 */
public interface CmWebProxyConfigService {
    Integer loadCmWebJumpModule();

    String loadNatServerIp();

    void configCmWebProxy(Integer cmWebJumpModule, String natIp);
}
