/***********************************************************************
 * $Id: LoopBackRecords.java,v1.0 2013-11-16 下午02:03:31 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Rod John
 * @created @2013-11-16-下午02:03:31
 * 
 */
public class IpRouteRecord implements Serializable {
    private static final long serialVersionUID = 7181560458138190471L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.2.1.1", index = true)
    private Integer topIPRouteBlockIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.2.1.2", index = true)
    private Integer topIPRouteBlockProtTypeIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.2.1.3")
    private Integer topIPRouteBlockRealCount;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.12.2.2.1.4")
    private byte[] topIPRouteBlockInfoList;

    /**
     * @return the topIPRouteBlockIndex
     */
    public Integer getTopIPRouteBlockIndex() {
        return topIPRouteBlockIndex;
    }

    /**
     * @param topIPRouteBlockIndex
     *            the topIPRouteBlockIndex to set
     */
    public void setTopIPRouteBlockIndex(Integer topIPRouteBlockIndex) {
        this.topIPRouteBlockIndex = topIPRouteBlockIndex;
    }

    /**
     * @return the topIPRouteBlockProtTypeIndex
     */
    public Integer getTopIPRouteBlockProtTypeIndex() {
        return topIPRouteBlockProtTypeIndex;
    }

    /**
     * @param topIPRouteBlockProtTypeIndex
     *            the topIPRouteBlockProtTypeIndex to set
     */
    public void setTopIPRouteBlockProtTypeIndex(Integer topIPRouteBlockProtTypeIndex) {
        this.topIPRouteBlockProtTypeIndex = topIPRouteBlockProtTypeIndex;
    }

    /**
     * @return the topIPRouteBlockRealCount
     */
    public Integer getTopIPRouteBlockRealCount() {
        return topIPRouteBlockRealCount;
    }

    /**
     * @param topIPRouteBlockRealCount
     *            the topIPRouteBlockRealCount to set
     */
    public void setTopIPRouteBlockRealCount(Integer topIPRouteBlockRealCount) {
        this.topIPRouteBlockRealCount = topIPRouteBlockRealCount;
    }

    /**
     * @return the topIPRouteBlockInfoList
     */
    public byte[] getTopIPRouteBlockInfoList() {
        return topIPRouteBlockInfoList;
    }

    /**
     * @param topIPRouteBlockInfoList
     *            the topIPRouteBlockInfoList to set
     */
    public void setTopIPRouteBlockInfoList(byte[] topIPRouteBlockInfoList) {
        this.topIPRouteBlockInfoList = topIPRouteBlockInfoList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoopBackRecords [topIPRouteBlockIndex=");
        builder.append(topIPRouteBlockIndex);
        builder.append(", topIPRouteBlockProtTypeIndex=");
        builder.append(topIPRouteBlockProtTypeIndex);
        builder.append(", topIPRouteBlockRealCount=");
        builder.append(topIPRouteBlockRealCount);
        builder.append(", topIPRouteBlockInfoList=");
        builder.append(topIPRouteBlockInfoList);
        builder.append("]");
        return builder.toString();
    }

}
