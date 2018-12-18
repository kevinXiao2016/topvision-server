/***********************************************************************
 * $Id: LoopBackConfig.java,v1.0 2013-11-16 上午11:49:29 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.domain.IpsAddress;

/**
 * @author flack
 * @created @2013-11-16-上午11:49:29
 * 
 */
public class StaticIpRouteConfig implements Serializable {
    private static final long serialVersionUID = -8745393237045942506L;

    public static final Integer DEFAULT = 0;
    public static final Integer BFD = 1;
    public static final Integer ICMP = 2;
    public static final Integer DELECT = 3;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.1.1.1", index = true)
    private IpsAddress staticRouteDstIpAddrIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.1.1.2", index = true)
    private IpsAddress staticRouteDstIpMaskIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.1.1.3", index = true)
    private IpsAddress staticRouteNextHopIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.1.1.4", writable = true, type = "Integer32")
    private Integer staticRouteDistance;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.1.1.5", writable = true, type = "Integer32")
    private Integer staticRouteTrack;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.1.1.6", writable = true, type = "Integer32")
    private Integer staticRouteRowStatus;

    /**
     * @return the staticRouteDstIpAddrIndex
     */
    public IpsAddress getStaticRouteDstIpAddrIndex() {
        return staticRouteDstIpAddrIndex;
    }

    /**
     * @param staticRouteDstIpAddrIndex
     *            the staticRouteDstIpAddrIndex to set
     */
    public void setStaticRouteDstIpAddrIndex(IpsAddress staticRouteDstIpAddrIndex) {
        this.staticRouteDstIpAddrIndex = staticRouteDstIpAddrIndex;
    }

    /**
     * @return the staticRouteDstIpMaskIndex
     */
    public IpsAddress getStaticRouteDstIpMaskIndex() {
        return staticRouteDstIpMaskIndex;
    }

    /**
     * @param staticRouteDstIpMaskIndex
     *            the staticRouteDstIpMaskIndex to set
     */
    public void setStaticRouteDstIpMaskIndex(IpsAddress staticRouteDstIpMaskIndex) {
        this.staticRouteDstIpMaskIndex = staticRouteDstIpMaskIndex;
    }

    /**
     * @return the staticRouteNextHopIndex
     */
    public IpsAddress getStaticRouteNextHopIndex() {
        return staticRouteNextHopIndex;
    }

    /**
     * @param staticRouteNextHopIndex
     *            the staticRouteNextHopIndex to set
     */
    public void setStaticRouteNextHopIndex(IpsAddress staticRouteNextHopIndex) {
        this.staticRouteNextHopIndex = staticRouteNextHopIndex;
    }

    /**
     * @return the staticRouteDistance
     */
    public Integer getStaticRouteDistance() {
        return staticRouteDistance;
    }

    /**
     * @param staticRouteDistance
     *            the staticRouteDistance to set
     */
    public void setStaticRouteDistance(Integer staticRouteDistance) {
        this.staticRouteDistance = staticRouteDistance;
    }

    /**
     * @return the staticRouteTrack
     */
    public Integer getStaticRouteTrack() {
        return staticRouteTrack;
    }

    /**
     * @param staticRouteTrack
     *            the staticRouteTrack to set
     */
    public void setStaticRouteTrack(Integer staticRouteTrack) {
        this.staticRouteTrack = staticRouteTrack;
    }

    /**
     * @return the staticRouteRowStatus
     */
    public Integer getStaticRouteRowStatus() {
        return staticRouteRowStatus;
    }

    /**
     * @param staticRouteRowStatus
     *            the staticRouteRowStatus to set
     */
    public void setStaticRouteRowStatus(Integer staticRouteRowStatus) {
        this.staticRouteRowStatus = staticRouteRowStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoopBackConfig [staticRouteDstIpAddrIndex=");
        builder.append(staticRouteDstIpAddrIndex);
        builder.append(", staticRouteDstIpMaskIndex=");
        builder.append(staticRouteDstIpMaskIndex);
        builder.append(", staticRouteNextHopIndex=");
        builder.append(staticRouteNextHopIndex);
        builder.append(", staticRouteDistance=");
        builder.append(staticRouteDistance);
        builder.append(", staticRouteTrack=");
        builder.append(staticRouteTrack);
        builder.append(", staticRouteRowStatus=");
        builder.append(staticRouteRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
