/***********************************************************************
 * $Id: Cmc_bRoute.java,v1.0 2013-8-7 上午10:26:37 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.route.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author dosion
 * @created @2013-8-7-上午10:26:37
 * 
 */
public class CmcRoute implements Serializable {
    private static final long serialVersionUID = -8586934221784298847L;

    private Long entityid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.18.1.1", index = true)
    private Integer topCcmtsRouteIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.18.1.2", writable = true, type = "IpAddress")
    private String topCcmtsRouteDstIp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.18.1.3", writable = true, type = "IpAddress")
    private String topCcmtsRouteIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.18.1.4", writable = true, type = "IpAddress")
    private String topCcmtsRouteNexthop;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.18.1.5")
    private String topCcmtsRoutePort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.18.1.6", writable = true, type = "Integer32")
    private Integer topCcmtsRouteStatus;

    public Long getEntityid() {
        return entityid;
    }

    public void setEntityid(Long entityid) {
        this.entityid = entityid;
    }

    public Integer getTopCcmtsRouteIndex() {
        return topCcmtsRouteIndex;
    }

    public void setTopCcmtsRouteIndex(Integer topCcmtsRouteIndex) {
        this.topCcmtsRouteIndex = topCcmtsRouteIndex;
    }

    public String getTopCcmtsRouteDstIp() {
        return topCcmtsRouteDstIp;
    }

    public void setTopCcmtsRouteDstIp(String topCcmtsRouteDstIp) {
        this.topCcmtsRouteDstIp = topCcmtsRouteDstIp;
    }

    public String getTopCcmtsRouteIpMask() {
        return topCcmtsRouteIpMask;
    }

    public void setTopCcmtsRouteIpMask(String topCcmtsRouteIpMask) {
        this.topCcmtsRouteIpMask = topCcmtsRouteIpMask;
    }

    public String getTopCcmtsRouteNexthop() {
        return topCcmtsRouteNexthop;
    }

    public void setTopCcmtsRouteNexthop(String topCcmtsRouteNexthop) {
        this.topCcmtsRouteNexthop = topCcmtsRouteNexthop;
    }

    public String getTopCcmtsRoutePort() {
        return topCcmtsRoutePort;
    }

    public void setTopCcmtsRoutePort(String topCcmtsRoutePort) {
        this.topCcmtsRoutePort = topCcmtsRoutePort;
    }

    public Integer getTopCcmtsRouteStatus() {
        return topCcmtsRouteStatus;
    }

    public void setTopCcmtsRouteStatus(Integer topCcmtsRouteStatus) {
        this.topCcmtsRouteStatus = topCcmtsRouteStatus;
    }

}
