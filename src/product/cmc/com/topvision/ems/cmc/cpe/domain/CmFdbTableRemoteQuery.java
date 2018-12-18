/***********************************************************************
 * $Id: CmLocateInfo.java,v1.0 2015年2月10日 上午11:08:45 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.PhysAddress;

import java.io.Serializable;

/**
 * @author jay
 * @created @2015年2月10日-上午11:08:45
 *
 */
public class CmFdbTableRemoteQuery implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4493242165379207556L;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long cmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.11.1.1", index = true )
    private PhysAddress fdbAddress;
    private String fdbAddressString;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.11.1.2")
    private Integer fdbStatus;

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public PhysAddress getFdbAddress() {
        return fdbAddress;
    }

    public void setFdbAddress(PhysAddress fdbAddress) {
        this.fdbAddress = fdbAddress;
    }

    public String getFdbAddressString() {
        return fdbAddressString;
    }

    public void setFdbAddressString(String fdbAddressString) {
        this.fdbAddressString = fdbAddressString;
    }

    public Integer getFdbStatus() {
        return fdbStatus;
    }

    public void setFdbStatus(Integer fdbStatus) {
        this.fdbStatus = fdbStatus;
    }
}
