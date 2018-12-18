/***********************************************************************
 * $Id: CMMacToIndex.java,v1.0 2014-11-21 上午10:42:50 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.domain.PhysAddress;

import java.io.Serializable;

/**
 * @author jay
 * @created @2014-11-21-上午10:42:50
 *
 */
public class CMMacToIndex implements Serializable {
    private static final long serialVersionUID = 2646893565977606741L;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.7.1.1", index = true)
    private PhysAddress mac;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.7.1.2")
    private Long cmIndex; // MAC地址

    public PhysAddress getMac() {
        return mac;
    }

    public void setMac(PhysAddress mac) {
        this.mac = mac;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

}
