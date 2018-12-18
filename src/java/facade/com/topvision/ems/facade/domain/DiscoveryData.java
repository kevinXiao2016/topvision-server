/***********************************************************************
 * $Id: DiscoveryData.java,v1.0 2011-6-28 下午08:00:44 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-6-28-下午08:00:44
 * 
 */
public class DiscoveryData implements Serializable {
    private static final long serialVersionUID = 8508174802678728501L;
    public static final Integer ALL_TOPO = 1;
    public static final Integer BASE_TOPO = 2;
    public static final String TOPVISION_OID = "32285";

    private Long entityId;
    private String ip = null;
    private String mac = null;
    private String hostName = null;
    private SnmpParam snmpParam = null;
    private DeviceBaseInfo system = null;
    private List<IpAddressTable> ipAddressTables = null;
    private List<PortEntity> interfaces = null;
    private Throwable stackTrace = null;
    private Long discoveryTime;
    private Object userObject = null;

    private Integer topoType = ALL_TOPO;
    private List<String> excludeOids = null;

    /**
     * 
     */
    public DiscoveryData() {
    }

    /**
     * 
     * @param entityId
     */
    public DiscoveryData(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * Base TopoType
     * 
     * @param topoType
     */
    public DiscoveryData(Integer topoType) {
        this.topoType = topoType;
    }

    /**
     * excludeOids
     * 
     * @param excludeOids
     */
    public DiscoveryData(List<String> excludeOids){
        this.excludeOids = excludeOids;
    }

    /**
     * @return the topoType
     */
    public Integer getTopoType() {
        return topoType;
    }

    /**
     * @return the excludeOids
     */
    public List<String> getExcludeOids() {
        return excludeOids;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the snmpParam
     */
    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    /**
     * @param snmpParam
     *            the snmpParam to set
     */
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    /**
     * @return the discoveryTime
     */
    public Long getDiscoveryTime() {
        return discoveryTime;
    }

    /**
     * @param discoveryTime
     *            the discoveryTime to set
     */
    public void setDiscoveryTime(Long discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    /**
     * @return the userObject
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * @param userObject
     *            the userObject to set
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    /**
     * @return the stackTrace
     */
    public Throwable getStackTrace() {
        return stackTrace;
    }

    /**
     * @param stackTrace
     *            the stackTrace to set
     */
    public void setStackTrace(Throwable stackTrace) {
        this.stackTrace = stackTrace;
    }

    /**
     * @return the system
     */
    public DeviceBaseInfo getSystem() {
        return system;
    }

    /**
     * @param system
     *            the system to set
     */
    public void setSystem(DeviceBaseInfo system) {
        this.system = system;
    }

    /**
     * @return the interfaces
     */
    public List<PortEntity> getInterfaces() {
        return interfaces;
    }

    /**
     * @param interfaces
     *            the interfaces to set
     */
    public void setInterfaces(List<PortEntity> interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * @return the ipAddressTables
     */
    public List<IpAddressTable> getIpAddressTables() {
        return ipAddressTables;
    }

    /**
     * @param ipAddressTables
     *            the ipAddressTables to set
     */
    public void setIpAddressTables(List<IpAddressTable> ipAddressTables) {
        this.ipAddressTables = ipAddressTables;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DiscoveryData [entityId=");
        builder.append(entityId);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", hostName=");
        builder.append(hostName);
        builder.append(", snmpParam=");
        builder.append(snmpParam);
        builder.append(", system=");
        builder.append(system);
        builder.append(", ipAddressTables=");
        builder.append(ipAddressTables);
        builder.append(", interfaces=");
        builder.append(interfaces);
        builder.append(", stackTrace=");
        builder.append(stackTrace);
        builder.append(", discoveryTime=");
        builder.append(discoveryTime);
        builder.append(", userObject=");
        builder.append(userObject);
        builder.append("]");
        return builder.toString();
    }
}
