/***********************************************************************
 * $Id: SyslogServerParam.java,v 1.1 2009-9-30 下午02:57:38 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.syslog;

import java.io.Serializable;
import java.util.List;

/**
 * @Create Date 2009-9-30 下午02:57:38
 * 
 * @author kelers
 * 
 */
public class SyslogServerParam implements Serializable {
    private static final long serialVersionUID = -1267182111333960210L;
    private List<Integer> listenPorts;

    /**
     * @return the listenPorts
     */
    public List<Integer> getListenPorts() {
        return listenPorts;
    }

    /**
     * @param listenPorts
     *            the listenPorts to set
     */
    public void setListenPorts(List<Integer> listenPorts) {
        this.listenPorts = listenPorts;
    }
}
