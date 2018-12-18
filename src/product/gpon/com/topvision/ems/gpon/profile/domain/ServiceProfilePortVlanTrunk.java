/***********************************************************************
 * $Id: ServiceProfilePortVlanTrunk.java,v1.0 2016年12月29日 上午10:41:05 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.domain;

/**
 * @author vanzand
 * @created @2016年12月29日-上午10:41:05
 *
 */
public class ServiceProfilePortVlanTrunk {
    private Integer serviceProfileId;
    private Integer portType;
    private Integer portId;
    private Integer vlanId;

    public Integer getServiceProfileId() {
        return serviceProfileId;
    }

    public void setServiceProfileId(Integer serviceProfileId) {
        this.serviceProfileId = serviceProfileId;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

}
