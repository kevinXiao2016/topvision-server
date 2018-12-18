/***********************************************************************
 * $Id: Cmc8800eTopologyHandle.java,v1.0 Jun 21, 2017 8:52:04 AM $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

/**
 * @author Victorli
 * @created @Jun 21, 2017-8:52:04 AM
 *
 */
@Service("cmc8800eTopologyHandle")
public class Cmc8800eTopologyHandle extends CmcWithAgentTopologyHandle {
    private final static String CC8800E_SYSOID = "1.3.6.1.4.1.32285.11.1.1.1.6";

    @Override
    @PostConstruct
    public void initialize() {
        batchDiscoveryService.registerTopoHandler(CC8800E_SYSOID, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        batchDiscoveryService.unregisterTopoHandler(CC8800E_SYSOID);
    }
}
