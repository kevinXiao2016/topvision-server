/***********************************************************************
 * $Id: CmcIpInfo.java,v1.0 2013-4-26 下午04:22:35 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

/**
 * @author Rod John
 * @created @2013-4-26-下午04:22:35
 * 
 */
public class CmcIpInfo {
    private Long entityId;
    // IP类型   
    private String ipType;
    // IP地址
    private String ipAddress;
    // 网关地址
    private String ipGateAddress;
    // 掩码地址
    private String ipMaskAddress;
    // Index
    private Integer seqIndex;
    /**
     * @return the ipType
     */
    public String getIpType() {
        return ipType;
    }
    /**
     * @param ipType the ipType to set
     */
    public void setIpType(String ipType) {
        this.ipType = ipType;
    }
    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }
    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    /**
     * @return the ipGateAddress
     */
    public String getIpGateAddress() {
        return ipGateAddress;
    }
    /**
     * @param ipGateAddress the ipGateAddress to set
     */
    public void setIpGateAddress(String ipGateAddress) {
        this.ipGateAddress = ipGateAddress;
    }
    /**
     * @return the ipMaskAddress
     */
    public String getIpMaskAddress() {
        return ipMaskAddress;
    }
    /**
     * @param ipMaskAddress the ipMaskAddress to set
     */
    public void setIpMaskAddress(String ipMaskAddress) {
        this.ipMaskAddress = ipMaskAddress;
    }
    
    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }
    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    /**
     * @return the seqIndex
     */
    public Integer getSeqIndex() {
        return seqIndex;
    }
    /**
     * @param seqIndex the seqIndex to set
     */
    public void setSeqIndex(Integer seqIndex) {
        this.seqIndex = seqIndex;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcIpInfo [ipType=");
        builder.append(ipType);
        builder.append(", ipAddress=");
        builder.append(ipAddress);
        builder.append(", ipGateAddress=");
        builder.append(ipGateAddress);
        builder.append(", ipMaskAddress=");
        builder.append(ipMaskAddress);
        builder.append("]");
        return builder.toString();
    }
    
    
}
