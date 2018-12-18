/***********************************************************************
 * $Id: IpSegmentInfo.java,v1.0 2014-5-10 下午4:21:18 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-5-10-下午4:21:18
 *
 */
public class IpSegmentInfo implements AliasesSuperType {
    private static final long serialVersionUID = 5163586658618617039L;

    private String ipSegment;
    private String displayName;
    private Integer totalNum;
    private Integer onlineNum;
    private Integer offineNum;

    public static final int LOAD_CMC_FLAG = 1;
    public static final int LOAD_CM_FLAG = 2;

    public IpSegmentInfo() {
        super();
    }

    public IpSegmentInfo(String ipSegment, String displayName, Integer totalNum, Integer onlineNum, Integer offineNum) {
        super();
        this.ipSegment = ipSegment;
        this.displayName = displayName;
        this.totalNum = totalNum;
        this.onlineNum = onlineNum;
        this.offineNum = offineNum;
    }

    public String getIpSegment() {
        return ipSegment;
    }

    public void setIpSegment(String ipSegment) {
        this.ipSegment = ipSegment;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Integer getOffineNum() {
        return offineNum;
    }

    public void setOffineNum(Integer offineNum) {
        this.offineNum = offineNum;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IpSegmentInfo [ipSegment=");
        builder.append(ipSegment);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", totalNum=");
        builder.append(totalNum);
        builder.append(", onlineNum=");
        builder.append(onlineNum);
        builder.append(", offineNum=");
        builder.append(offineNum);
        builder.append("]");
        return builder.toString();
    }

}
