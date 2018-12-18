/***********************************************************************
 * $Id: NbiConfig.java,v1.0 2015-11-5 下午3:50:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2015-11-5-下午3:50:47
 *
 */
public class NbiConfig implements AliasesSuperType {
    private static final long serialVersionUID = -6761905344120888367L;

    public static final String NORTHBOUND = "nbi.alarm";
    public static final String NORTHBOUND_IPADDRESS = "nbi.alarm.ipaddress";
    public static final String NORTHBOUND_PORT = "nbi.alarm.port";
    public static final String NORTHBOUND_COMMUNITY = "nbi.alarm.community";
    public static final String NORTHBOUND_HEARTBEAT_SWITCH = "nbi.alarm.heartbeatswitch";
    public static final String NORTHBOUND_HEARTBEAT_INTERVAL = "nbi.alarm.heartbeatinterval";
    public static final String NORTHBOUND_HEARTBEAT_LABEL = "nbi.alarm.heartbeatlabel";

    
    
    private String nbiIpAddress;
    private String nbiPort;
    private Boolean nbiHeartBeatSwitch;
    private Integer nbiHeartBeatInterval;
    private String nbiCommunity;
    private String nbiHeartBeatLabel;

    public NbiConfig() {
    }

    /**
     * @param nbiIpAddress
     * @param nbiPort
     * @param nbiHeartBeatSwitch
     * @param nbiHeartBeatInterval
     */
    public NbiConfig(String nbiIpAddress, String nbiPort, Boolean nbiHeartBeatSwitch, Integer nbiHeartBeatInterval,
            String nbiCommunity, String nbiHeartBeatLabel) {
        this.nbiIpAddress = nbiIpAddress;
        this.nbiPort = nbiPort;
        this.nbiHeartBeatSwitch = nbiHeartBeatSwitch;
        this.nbiHeartBeatInterval = nbiHeartBeatInterval;
        this.nbiCommunity = nbiCommunity;
        this.nbiHeartBeatLabel = nbiHeartBeatLabel;
    }

    /**
     * @return the nbiIpAddress
     */
    public String getNbiIpAddress() {
        return nbiIpAddress;
    }

    /**
     * @param nbiIpAddress the nbiIpAddress to set
     */
    public void setNbiIpAddress(String nbiIpAddress) {
        this.nbiIpAddress = nbiIpAddress;
    }

    /**
     * @return the nbiPort
     */
    public String getNbiPort() {
        return nbiPort;
    }

    /**
     * @param nbiPort the nbiPort to set
     */
    public void setNbiPort(String nbiPort) {
        this.nbiPort = nbiPort;
    }

    /**
     * @return the nbiHeartBeatSwitch
     */
    public Boolean getNbiHeartBeatSwitch() {
        return nbiHeartBeatSwitch;
    }

    /**
     * @param nbiHeartBeatSwitch the nbiHeartBeatSwitch to set
     */
    public void setNbiHeartBeatSwitch(Boolean nbiHeartBeatSwitch) {
        this.nbiHeartBeatSwitch = nbiHeartBeatSwitch;
    }

    /**
     * @return the nbiHeartBeatInterval
     */
    public Integer getNbiHeartBeatInterval() {
        return nbiHeartBeatInterval;
    }

    /**
     * @param nbiHeartBeatInterval the nbiHeartBeatInterval to set
     */
    public void setNbiHeartBeatInterval(Integer nbiHeartBeatInterval) {
        this.nbiHeartBeatInterval = nbiHeartBeatInterval;
    }

    /**
     * @return the nbiCommunity
     */
    public String getNbiCommunity() {
        return nbiCommunity;
    }

    /**
     * @param nbiCommunity the nbiCommunity to set
     */
    public void setNbiCommunity(String nbiCommunity) {
        this.nbiCommunity = nbiCommunity;
    }

    public String getNbiHeartBeatLabel() {
        return nbiHeartBeatLabel;
    }

    public void setNbiHeartBeatLabel(String nbiHeartBeatLabel) {
        this.nbiHeartBeatLabel = nbiHeartBeatLabel;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NbiConfig [nbiIpAddress=");
        builder.append(nbiIpAddress);
        builder.append(", nbiPort=");
        builder.append(nbiPort);
        builder.append(", nbiHeartBeatSwitch=");
        builder.append(nbiHeartBeatSwitch);
        builder.append(", nbiHeartBeatInterval=");
        builder.append(nbiHeartBeatInterval);
        builder.append(", nbiCommunity=");
        builder.append(nbiCommunity);
        builder.append(", nbiHeartBeatLabel=");
        builder.append(nbiHeartBeatLabel);
        builder.append("]");
        return builder.toString();
    }

}
