/***********************************************************************
 * $Id: Cmc8800bTopologyHandle.java,v1.0 2015-7-23 下午3:12:19 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

/**
 * @author Rod John
 * @created @2015-7-23-下午3:12:19
 *
 */
@Service("cmc8800bTopologyHandle")
public class Cmc8800bTopologyHandle extends CmcWithAgentTopologyHandle {
    private final static String CC8800B_SYSOID = "1.3.6.1.4.1.32285.11.1.1.1.2";

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        batchDiscoveryService.registerTopoHandler(CC8800B_SYSOID, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        batchDiscoveryService.unregisterTopoHandler(CC8800B_SYSOID);
    }

}
