/***********************************************************************
 * $Id: TrapServerParam.java,v 1.1 2009-9-30 下午02:57:10 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.io.Serializable;
import java.util.List;

/**
 * @Create Date 2009-9-30 下午02:57:10
 * 
 * @author kelers
 * 
 */
public class TrapServerParam implements Serializable {
    private static final long serialVersionUID = 7256773535753726864L;
    private String listenAddress;
    private List<Integer> listenPorts;

    /**
     * @return the listenAddress
     */
    public String getListenAddress() {
        return listenAddress;
    }

    /**
     * @return the listenPorts
     */
    public List<Integer> getListenPorts() {
        return listenPorts;
    }

    /**
     * @param listenAddress
     *            the listenAddress to set
     */
    public void setListenAddress(String listenAddress) {
        this.listenAddress = listenAddress;
    }

    /**
     * @param listenPorts
     *            the listenPorts to set
     */
    public void setListenPorts(List<Integer> listenPorts) {
        this.listenPorts = listenPorts;
    }
}
