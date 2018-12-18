/***********************************************************************
 * $Id: MultiPingResult.java,v 1.1 Jul 31, 2008 10:42:56 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ping;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Create Date Jul 31, 2008 10:42:56 PM
 * 
 * @author kelers
 * 
 */
public class MultiPingResult implements Serializable {
    private static final long serialVersionUID = 3475684374815022529L;
    private Map<String, String> reachableAddrs = null;
    private List<String> unreachableAddrs = null;

    public MultiPingResult() {
    }

    public MultiPingResult(Map<String, String> reachableAddrs, List<String> unreachableAddrs) {
        this.reachableAddrs = reachableAddrs;
        this.unreachableAddrs = unreachableAddrs;
    }

    /**
     * @return the reachableAddrs
     */
    public Map<String, String> getReachableAddrs() {
        return reachableAddrs;
    }

    /**
     * @return the unreachableAddrs
     */
    public List<String> getUnreachableAddrs() {
        return unreachableAddrs;
    }

    /**
     * @param reachableAddrs
     *            the reachableAddrs to set
     */
    public void setReachableAddrs(Map<String, String> reachableAddrs) {
        this.reachableAddrs = reachableAddrs;
    }

    /**
     * @param unreachableAddrs
     *            the unreachableAddrs to set
     */
    public void setUnreachableAddrs(List<String> unreachableAddrs) {
        this.unreachableAddrs = unreachableAddrs;
    }

}
