/***********************************************************************
 * $Id: Server.java,v 1.1 Oct 24, 2008 3:39:22 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.snmp.SnmpParam;

/**
 * 
 * @author kelers
 * 
 */
public class Server extends SnmpParam {
    private static final long serialVersionUID = 2531784024277746242L;
    private String os = null;
    private String monitorMode = null;// SNMP,TELNET,SSH,WMI

    /**
     * 
     * @return
     */
    public String getMonitorMode() {
        return monitorMode;
    }

    /**
     * 
     * @return
     */
    public String getOs() {
        return os;
    }

    /**
     * 
     * @param monitorMode
     */
    public void setMonitorMode(String monitorMode) {
        this.monitorMode = monitorMode;
    }

    /**
     * 
     * @param os
     */
    public void setOs(String os) {
        this.os = os;
    }
}
