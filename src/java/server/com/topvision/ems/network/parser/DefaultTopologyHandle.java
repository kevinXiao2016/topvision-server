/***********************************************************************
 * $Id: DefaultTopologyHandle.java,v1.0 2017年9月20日 下午1:26:16 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.parser;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.TopoHandlerProperty;

/**
 * @author vanzand
 * @created @2017年9月20日-下午1:26:16
 *
 */
@Service("defaultTopologyHandle")
public final class DefaultTopologyHandle extends AbstractTopologyHandle {

    public final static String DEFAULT = "default";

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#getTopoInfo()
     */
    @Override
    public TopoHandlerProperty getTopoInfo() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        batchDiscoveryService.registerTopoHandler(DEFAULT, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        batchDiscoveryService.unregisterTopoHandler(DEFAULT);
    }

}
