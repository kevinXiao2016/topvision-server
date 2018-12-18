/***********************************************************************
 * $Id: AutoDiscoveryStatus.java,v1.0 2014-5-12 下午9:43:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;

/**
 * @author Rod John
 * @created @2014-5-12-下午9:43:06
 * 
 */
@Service("autoDiscoveryStatus")
public class AutoDiscoveryStatus {
    private List<BatchAutoDiscoveryIps> batchAutoDiscoveryIps = new ArrayList<>();

    /**
     * @return the batchAutoDiscoveryIps
     */
    public List<BatchAutoDiscoveryIps> getBatchAutoDiscoveryIps() {
        return batchAutoDiscoveryIps;
    }

    /**
     * @param batchAutoDiscoveryIps
     *            the batchAutoDiscoveryIps to set
     */
    public void setBatchAutoDiscoveryIps(List<BatchAutoDiscoveryIps> batchAutoDiscoveryIps) {
        this.batchAutoDiscoveryIps = batchAutoDiscoveryIps;
    }

}
