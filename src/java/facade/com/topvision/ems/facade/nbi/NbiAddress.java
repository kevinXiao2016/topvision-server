/***********************************************************************
 * $Id: NbiAddressConfig.java,v1.0 2016年3月14日 上午10:43:59 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.nbi;

import java.io.Serializable;

/**
 * @author Bravin
 * @created @2016年3月14日-上午10:43:59
 *
 */
public class NbiAddress implements Serializable {
    private static final long serialVersionUID = -493659870288975911L;
    private String nbiAddress;
    private int nbiPort;
    private boolean startNbi;

    public String getNbiAddress() {
        return nbiAddress;
    }

    public void setNbiAddress(String nbiAddress) {
        this.nbiAddress = nbiAddress;
    }

    public int getNbiPort() {
        return nbiPort;
    }

    public void setNbiPort(int nbiPort) {
        this.nbiPort = nbiPort;
    }

    public boolean isStartNbi() {
        return startNbi;
    }

    public void setStartNbi(boolean startNbi) {
        this.startNbi = startNbi;
    }

}
