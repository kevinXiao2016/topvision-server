/***********************************************************************
 * $Id: OltAttribute.java,v1.0 2011-9-26 上午09:02:13 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * OLT设备属性
 * 
 * @author zhanglongyang
 * 
 */
public class OltAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.2.1.1.2.1", writable = true, type = "OctetString")
    private String oltName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.2.1.1.3.1")
    private String oltType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.2.1.1.5.1")
    private Long oltDeviceUpTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.2.1.1.6.1")
    private Integer oltDeviceNumOfTotalServiceSlot;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.2.1.1.7.1")
    private Integer oltDeviceNumOfTotalPowerSlot;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.2.1.1.8.1")
    private Integer oltDeviceNumOfTotalFanSlot;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.2.1.1.9.1")
    private Integer oltDeviceStyle;
    private Long inbandIpAddress;
    private Long inbandIpSubnetMask;
    private Long inbandIpGateway = 0L;
    private Long inbandMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.5.0", writable = true, type = "Integer32")
    private Integer inbandVlanId;
    private String inbandPortIndex;
    private Long outbandIpSubnetMask;
    private Long outbandIpGateway = 0L;
    private Long outbandIpAddress;
    private Long outbandMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.11.0")
    private String systemOui;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.12.0")
    private String vendorName;
    private Long topSysSnmpHostIp;
    private Long topSysSnmpHostIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.1.3.0", writable = true, type = "Integer32")
    private Integer topSysSnmpVersion;
    private Integer sysSnmpVersionInSnmp4J;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.1.4.0", writable = true, type = "OctetString")
    private String topSysReadCommunity;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.1.5.0", writable = true, type = "OctetString")
    private String topSysWriteCommunity;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.1.6.0", writable = true, type = "Integer32")
    private Integer topSysSnmpPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.1.7.0", writable = true, type = "Integer32")
    private Integer topSysInBandMaxBw;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.2.1.0", writable = true, type = "Integer32")
    private Integer topSysOltRackNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.2.2.0", writable = true, type = "Integer32")
    private Integer topSysOltFrameNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.5.1.0", writable = true, type = "Integer32")
    private Integer onuAuthenticationPolicy;

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.2.0", writable = true, type = "IpAddress")
    private String inbandIp;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.3.0", writable = true, type = "IpAddress")
    private String inbandMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.4.0", writable = true, type = "IpAddress")
    private String inbandGateway;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.6.0")
    private String inbandMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.7.0", writable = true, type = "IpAddress")
    private String outbandIp;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.8.0", writable = true, type = "IpAddress")
    private String outbandMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.9.0", writable = true, type = "IpAddress")
    private String outbandGateway;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.1.10.0")
    private String outbandMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.1.1.0", writable = true, type = "IpAddress")
    private String snmpHostIp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.1.2.0", writable = true, type = "IpAddress")
    private String hostIpMask;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.4.0", writable = true)
    private String sysContact;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.6.0", writable = true)
    private String sysLocation;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.3.1.0", writable = true, type = "Integer32")
    private Integer sniMacAddrTableAgingTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.3.2.0", writable = true, type = "Integer32")
    private Integer topSysArpAgingTime;
    private String ip;
    private String name;
    private String contact;
    private String location;
    private String note;
    private Integer status;// 管理状态
    private Integer state;// 在线状态
    private String bSoftwareVersion;// OLT设备软件版本
    private Double cpuUsed;
    private Double memUsed;
    private String cpu;
    private String mem;
    private Timestamp snapTime;
    private Long sysUpTime;
    private Boolean attention;
    private Integer systemRogueCheck;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltAttribute [entityId=");
        builder.append(entityId);
        builder.append(", oltName=");
        builder.append(oltName);
        builder.append(", oltType=");
        builder.append(oltType);
        builder.append(", oltDeviceUpTime=");
        builder.append(oltDeviceUpTime);
        builder.append(", oltDeviceNumOfTotalServiceSlot=");
        builder.append(oltDeviceNumOfTotalServiceSlot);
        builder.append(", oltDeviceNumOfTotalPowerSlot=");
        builder.append(oltDeviceNumOfTotalPowerSlot);
        builder.append(", oltDeviceNumOfTotalFanSlot=");
        builder.append(oltDeviceNumOfTotalFanSlot);
        builder.append(", oltDeviceStyle=");
        builder.append(oltDeviceStyle);
        builder.append(", inbandIpAddress=");
        builder.append(inbandIpAddress);
        builder.append(", inbandIpSubnetMask=");
        builder.append(inbandIpSubnetMask);
        builder.append(", inbandIpGateway=");
        builder.append(inbandIpGateway);
        builder.append(", inbandMacAddress=");
        builder.append(inbandMacAddress);
        builder.append(", inbandVlanId=");
        builder.append(inbandVlanId);
        builder.append(", outbandIpSubnetMask=");
        builder.append(outbandIpSubnetMask);
        builder.append(", outbandIpGateway=");
        builder.append(outbandIpGateway);
        builder.append(", outbandIpAddress=");
        builder.append(outbandIpAddress);
        builder.append(", outbandMacAddress=");
        builder.append(outbandMacAddress);
        builder.append(", systemOui=");
        builder.append(systemOui);
        builder.append(", vendorName=");
        builder.append(vendorName);
        builder.append(", topSysSnmpHostIp=");
        builder.append(topSysSnmpHostIp);
        builder.append(", topSysSnmpHostIpMask=");
        builder.append(topSysSnmpHostIpMask);
        builder.append(", topSysSnmpVersion=");
        builder.append(topSysSnmpVersion);
        builder.append(", topSysReadCommunity=");
        builder.append(topSysReadCommunity);
        builder.append(", topSysWriteCommunity=");
        builder.append(topSysWriteCommunity);
        builder.append(", topSysSnmpPort=");
        builder.append(topSysSnmpPort);
        builder.append(", topSysInBandMaxBw=");
        builder.append(topSysInBandMaxBw);
        builder.append(", topSysOltRackNum=");
        builder.append(topSysOltRackNum);
        builder.append(", topSysOltFrameNum=");
        builder.append(topSysOltFrameNum);
        builder.append(", onuAuthenticationPolicy=");
        builder.append(onuAuthenticationPolicy);
        builder.append(", inbandIp=");
        builder.append(inbandIp);
        builder.append(", inbandMask=");
        builder.append(inbandMask);
        builder.append(", inbandGateway=");
        builder.append(inbandGateway);
        builder.append(", inbandMac=");
        builder.append(inbandMac);
        builder.append(", outbandIp=");
        builder.append(outbandIp);
        builder.append(", outbandMask=");
        builder.append(outbandMask);
        builder.append(", outbandGateway=");
        builder.append(outbandGateway);
        builder.append(", outbandMac=");
        builder.append(outbandMac);
        builder.append(", snmpHostIp=");
        builder.append(snmpHostIp);
        builder.append(", hostIpMask=");
        builder.append(hostIpMask);
        builder.append(", sysContact=");
        builder.append(sysContact);
        builder.append(", sysLocation=");
        builder.append(sysLocation);
        builder.append(", sniMacAddrTableAgingTime=");
        builder.append(sniMacAddrTableAgingTime);
        builder.append("]");
        return builder.toString();
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
     * @return the oltName
     */
    public String getOltName() {
        return oltName;
    }

    /**
     * @param oltName
     *            the oltName to set
     */
    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    /**
     * @return the oltType
     */
    public String getOltType() {
        return oltType;
    }

    /**
     * @param oltType
     *            the oltType to set
     */
    public void setOltType(String oltType) {
        this.oltType = oltType;
    }

    /**
     * @return the oltDeviceUpTime
     */
    public Long getOltDeviceUpTime() {
        return oltDeviceUpTime;
    }

    /**
     * @param oltDeviceUpTime
     *            the oltDeviceUpTime to set
     */
    public void setOltDeviceUpTime(Long oltDeviceUpTime) {
        this.oltDeviceUpTime = oltDeviceUpTime;
    }

    /**
     * @return the oltDeviceNumOfTotalServiceSlot
     */
    public Integer getOltDeviceNumOfTotalServiceSlot() {
        return oltDeviceNumOfTotalServiceSlot;
    }

    /**
     * @param oltDeviceNumOfTotalServiceSlot
     *            the oltDeviceNumOfTotalServiceSlot to set
     */
    public void setOltDeviceNumOfTotalServiceSlot(Integer oltDeviceNumOfTotalServiceSlot) {
        this.oltDeviceNumOfTotalServiceSlot = oltDeviceNumOfTotalServiceSlot;
    }

    /**
     * @return the oltDeviceNumOfTotalPowerSlot
     */
    public Integer getOltDeviceNumOfTotalPowerSlot() {
        return oltDeviceNumOfTotalPowerSlot;
    }

    /**
     * @param oltDeviceNumOfTotalPowerSlot
     *            the oltDeviceNumOfTotalPowerSlot to set
     */
    public void setOltDeviceNumOfTotalPowerSlot(Integer oltDeviceNumOfTotalPowerSlot) {
        this.oltDeviceNumOfTotalPowerSlot = oltDeviceNumOfTotalPowerSlot;
    }

    /**
     * @return the oltDeviceNumOfTotalFanSlot
     */
    public Integer getOltDeviceNumOfTotalFanSlot() {
        return oltDeviceNumOfTotalFanSlot;
    }

    /**
     * @param oltDeviceNumOfTotalFanSlot
     *            the oltDeviceNumOfTotalFanSlot to set
     */
    public void setOltDeviceNumOfTotalFanSlot(Integer oltDeviceNumOfTotalFanSlot) {
        this.oltDeviceNumOfTotalFanSlot = oltDeviceNumOfTotalFanSlot;
    }

    /**
     * @return the oltDeviceStyle
     */
    public Integer getOltDeviceStyle() {
        return oltDeviceStyle;
    }

    /**
     * @param oltDeviceStyle
     *            the oltDeviceStyle to set
     */
    public void setOltDeviceStyle(Integer oltDeviceStyle) {
        this.oltDeviceStyle = oltDeviceStyle;
    }

    /**
     * @return the inbandIpAddress
     */
    public Long getInbandIpAddress() {
        return inbandIpAddress;
    }

    /**
     * @param inbandIpAddress
     *            the inbandIpAddress to set
     */
    public void setInbandIpAddress(Long inbandIpAddress) {
        this.inbandIpAddress = inbandIpAddress;
        if (inbandIpAddress != null) {
            this.inbandIp = IpUtils.long2ip(inbandIpAddress);
        }
    }

    /**
     * @return the inbandIpSubnetMask
     */
    public Long getInbandIpSubnetMask() {
        return inbandIpSubnetMask;
    }

    /**
     * @param inbandIpSubnetMask
     *            the inbandIpSubnetMask to set
     */
    public void setInbandIpSubnetMask(Long inbandIpSubnetMask) {
        this.inbandIpSubnetMask = inbandIpSubnetMask;
        if (inbandIpSubnetMask != null) {
            this.inbandMask = IpUtils.long2ip(inbandIpSubnetMask);
        }
    }

    /**
     * @return the inbandIpGateway
     */
    public Long getInbandIpGateway() {
        return inbandIpGateway;
    }

    /**
     * @param inbandIpGateway
     *            the inbandIpGateway to set
     */
    public void setInbandIpGateway(Long inbandIpGateway) {
        this.inbandIpGateway = inbandIpGateway;
        if (inbandIpGateway != null) {
            this.inbandGateway = IpUtils.long2ip(inbandIpGateway);
        }
    }

    /**
     * @return the inbandMacAddress
     */
    public Long getInbandMacAddress() {
        return inbandMacAddress;
    }

    /**
     * @param inbandMacAddress
     *            the inbandMacAddress to set
     */
    public void setInbandMacAddress(Long inbandMacAddress) {
        this.inbandMacAddress = inbandMacAddress;
        if (inbandMacAddress != null) {
            this.inbandMac = new MacUtils(inbandMacAddress).toString(MacUtils.MAOHAO).toUpperCase();
        }
    }

    /**
     * @return the inbandVlanId
     */
    public Integer getInbandVlanId() {
        return inbandVlanId;
    }

    /**
     * @param inbandVlanId
     *            the inbandVlanId to set
     */
    public void setInbandVlanId(Integer inbandVlanId) {
        this.inbandVlanId = inbandVlanId;
    }

    /**
     * @return the outbandIpSubnetMask
     */
    public Long getOutbandIpSubnetMask() {
        return outbandIpSubnetMask;
    }

    /**
     * @param outbandIpSubnetMask
     *            the outbandIpSubnetMask to set
     */
    public void setOutbandIpSubnetMask(Long outbandIpSubnetMask) {
        this.outbandIpSubnetMask = outbandIpSubnetMask;
        if (outbandIpSubnetMask != null) {
            this.outbandMask = IpUtils.long2ip(outbandIpSubnetMask);
        }
    }

    /**
     * @return the outbandIpGateway
     */
    public Long getOutbandIpGateway() {
        return outbandIpGateway;
    }

    /**
     * @param outbandIpGateway
     *            the outbandIpGateway to set
     */
    public void setOutbandIpGateway(Long outbandIpGateway) {
        this.outbandIpGateway = outbandIpGateway;
        this.outbandGateway = IpUtils.long2ip(outbandIpGateway);
    }

    /**
     * @return the outbandIpAddress
     */
    public Long getOutbandIpAddress() {
        return outbandIpAddress;
    }

    /**
     * @param outbandIpAddress
     *            the outbandIpAddress to set
     */
    public void setOutbandIpAddress(Long outbandIpAddress) {
        this.outbandIpAddress = outbandIpAddress;
        if (outbandIpAddress != null) {
            this.outbandIp = IpUtils.long2ip(outbandIpAddress);
        }
    }

    /**
     * @return the outbandMacAddress
     */
    public Long getOutbandMacAddress() {
        return outbandMacAddress;
    }

    /**
     * @param outbandMacAddress
     *            the outbandMacAddress to set
     */
    public void setOutbandMacAddress(Long outbandMacAddress) {
        this.outbandMacAddress = outbandMacAddress;
        if (outbandMacAddress != null) {
            this.outbandMac = new MacUtils(outbandMacAddress).toString(MacUtils.MAOHAO).toUpperCase();
        }
    }

    /**
     * @return the systemOui
     */
    public String getSystemOui() {
        return systemOui;
    }

    /**
     * @param systemOui
     *            the systemOui to set
     */
    public void setSystemOui(String systemOui) {
        this.systemOui = systemOui;
    }

    /**
     * @return the vendorName
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * @param vendorName
     *            the vendorName to set
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * @return the topSysSnmpHostIp
     */
    public Long getTopSysSnmpHostIp() {
        return topSysSnmpHostIp;
    }

    /**
     * @param topSysSnmpHostIp
     *            the topSysSnmpHostIp to set
     */
    public void setTopSysSnmpHostIp(Long topSysSnmpHostIp) {
        this.topSysSnmpHostIp = topSysSnmpHostIp;
        if (topSysSnmpHostIp != null) {
            this.snmpHostIp = IpUtils.long2ip(topSysSnmpHostIp);
        }
    }

    /**
     * @return the topSysSnmpHostIpMask
     */
    public Long getTopSysSnmpHostIpMask() {
        return topSysSnmpHostIpMask;
    }

    /**
     * @param topSysSnmpHostIpMask
     *            the topSysSnmpHostIpMask to set
     */
    public void setTopSysSnmpHostIpMask(Long topSysSnmpHostIpMask) {
        this.topSysSnmpHostIpMask = topSysSnmpHostIpMask;
        if (topSysSnmpHostIpMask != null) {
            this.hostIpMask = IpUtils.long2ip(topSysSnmpHostIpMask);
        }

    }

    /**
     * @return the topSysSnmpVersion
     */
    public Integer getTopSysSnmpVersion() {
        return topSysSnmpVersion;
    }

    /**
     * @param topSysSnmpVersion
     *            the topSysSnmpVersion to set
     */
    public void setTopSysSnmpVersion(Integer topSysSnmpVersion) {
        this.topSysSnmpVersion = topSysSnmpVersion;
        if (topSysSnmpVersion == null) {
            topSysSnmpVersion = 1;
        }
        switch (topSysSnmpVersion) {
        case 1:
            this.sysSnmpVersionInSnmp4J = 0;
            break;
        case 2:
            this.sysSnmpVersionInSnmp4J = 1;
            break;
        case 3:
            this.sysSnmpVersionInSnmp4J = 3;
            break;
        case 4:
            this.sysSnmpVersionInSnmp4J = 4;
            break;
        default:
            this.sysSnmpVersionInSnmp4J = 1;
            break;
        }
    }

    /**
     * @return the topSysReadCommunity
     */
    public String getTopSysReadCommunity() {
        return topSysReadCommunity;
    }

    /**
     * @param topSysReadCommunity
     *            the topSysReadCommunity to set
     */
    public void setTopSysReadCommunity(String topSysReadCommunity) {
        this.topSysReadCommunity = topSysReadCommunity;
    }

    /**
     * @return the topSysWriteCommunity
     */
    public String getTopSysWriteCommunity() {
        return topSysWriteCommunity;
    }

    /**
     * @param topSysWriteCommunity
     *            the topSysWriteCommunity to set
     */
    public void setTopSysWriteCommunity(String topSysWriteCommunity) {
        this.topSysWriteCommunity = topSysWriteCommunity;
    }

    /**
     * @return the topSysSnmpPort
     */
    public Integer getTopSysSnmpPort() {
        return topSysSnmpPort;
    }

    /**
     * @param topSysSnmpPort
     *            the topSysSnmpPort to set
     */
    public void setTopSysSnmpPort(Integer topSysSnmpPort) {
        this.topSysSnmpPort = topSysSnmpPort;
    }

    /**
     * @return the topSysInBandMaxBw
     */
    public Integer getTopSysInBandMaxBw() {
        return topSysInBandMaxBw;
    }

    /**
     * @param topSysInBandMaxBw
     *            the topSysInBandMaxBw to set
     */
    public void setTopSysInBandMaxBw(Integer topSysInBandMaxBw) {
        this.topSysInBandMaxBw = topSysInBandMaxBw;
    }

    /**
     * @return the topSysOltRackNum
     */
    public Integer getTopSysOltRackNum() {
        return topSysOltRackNum;
    }

    /**
     * @param topSysOltRackNum
     *            the topSysOltRackNum to set
     */
    public void setTopSysOltRackNum(Integer topSysOltRackNum) {
        this.topSysOltRackNum = topSysOltRackNum;
    }

    /**
     * @return the topSysOltFrameNum
     */
    public Integer getTopSysOltFrameNum() {
        return topSysOltFrameNum;
    }

    /**
     * @param topSysOltFrameNum
     *            the topSysOltFrameNum to set
     */
    public void setTopSysOltFrameNum(Integer topSysOltFrameNum) {
        this.topSysOltFrameNum = topSysOltFrameNum;
    }

    /**
     * @return the onuAuthenticationPolicy
     */
    public Integer getOnuAuthenticationPolicy() {
        return onuAuthenticationPolicy;
    }

    /**
     * @param onuAuthenticationPolicy
     *            the onuAuthenticationPolicy to set
     */
    public void setOnuAuthenticationPolicy(Integer onuAuthenticationPolicy) {
        this.onuAuthenticationPolicy = onuAuthenticationPolicy;
    }

    /**
     * @return the inbandIp
     */
    public String getInbandIp() {
        return inbandIp;
    }

    /**
     * @param inbandIp
     *            the inbandIp to set
     */
    public void setInbandIp(String inbandIp) {
        this.inbandIp = inbandIp;
        if (inbandIp != null) {
            this.inbandIpAddress = IpUtils.ip2long(inbandIp);
        }
    }

    /**
     * @return the outbandIp
     */
    public String getOutbandIp() {
        return outbandIp;
    }

    /**
     * @param outbandIp
     *            the outbandIp to set
     */
    public void setOutbandIp(String outbandIp) {
        this.outbandIp = outbandIp;
        if (outbandIp != null) {
            this.outbandIpAddress = IpUtils.ip2long(outbandIp);
        }
    }

    /**
     * @return the snmpHostIp
     */
    public String getSnmpHostIp() {
        return snmpHostIp;
    }

    /**
     * @param snmpHostIp
     *            the snmpHostIp to set
     */
    public void setSnmpHostIp(String snmpHostIp) {
        this.snmpHostIp = snmpHostIp;
        if (snmpHostIp != null) {
            this.topSysSnmpHostIp = IpUtils.ip2long(snmpHostIp);
        }
    }

    /**
     * @return the inbandMask
     */
    public String getInbandMask() {
        return inbandMask;
    }

    /**
     * @param inbandMask
     *            the inbandMask to set
     */
    public void setInbandMask(String inbandMask) {
        this.inbandMask = inbandMask;
        if (inbandMask != null) {
            this.inbandIpSubnetMask = IpUtils.ip2long(inbandMask);
        }
    }

    /**
     * @return the inbandGateway
     */
    public String getInbandGateway() {
        return inbandGateway;
    }

    /**
     * @param inbandGateway
     *            the inbandGateway to set
     */
    public void setInbandGateway(String inbandGateway) {
        this.inbandGateway = inbandGateway;
        if (inbandGateway != null) {
            this.inbandIpGateway = IpUtils.ip2long(inbandGateway);
        }
    }

    /**
     * @return the outbandMask
     */
    public String getOutbandMask() {
        return outbandMask;
    }

    /**
     * @param outbandMask
     *            the outbandMask to set
     */
    public void setOutbandMask(String outbandMask) {
        this.outbandMask = outbandMask;
        this.outbandIpSubnetMask = IpUtils.ip2long(outbandMask);
    }

    /**
     * @return the outbandGateway
     */
    public String getOutbandGateway() {
        return outbandGateway;
    }

    /**
     * @param outbandGateway
     *            the outbandGateway to set
     */
    public void setOutbandGateway(String outbandGateway) {
        this.outbandGateway = outbandGateway;
        if (outbandGateway != null) {
            this.outbandIpGateway = IpUtils.ip2long(outbandGateway);
        }
    }

    /**
     * @return the hostIpMask
     */
    public String getHostIpMask() {
        return hostIpMask;
    }

    /**
     * @param hostIpMask
     *            the hostIpMask to set
     */
    public void setHostIpMask(String hostIpMask) {
        this.hostIpMask = hostIpMask;
        if (hostIpMask != null) {
            this.topSysSnmpHostIpMask = IpUtils.ip2long(hostIpMask);
        }
    }

    /**
     * @return the inbandMac
     */
    public String getInbandMac() {
        return inbandMac;
    }

    /**
     * @param inbandMac
     *            the inbandMac to set
     */
    public void setInbandMac(String inbandMac) {
        this.inbandMac = inbandMac;
        this.inbandMacAddress = new MacUtils(this.inbandMac).longValue();
    }

    /**
     * @return the outbandMac
     */
    public String getOutbandMac() {
        return outbandMac;
    }

    /**
     * @param outbandMac
     *            the outbandMac to set
     */
    public void setOutbandMac(String outbandMac) {
        this.outbandMac = outbandMac;
        this.outbandMacAddress = new MacUtils(this.outbandMac).longValue();
    }

    /**
     * @return the sysContact
     */
    public String getSysContact() {
        return sysContact;
    }

    /**
     * @param sysContact
     *            the sysContact to set
     */
    public void setSysContact(String sysContact) {
        this.sysContact = sysContact;
    }

    /**
     * @return the sysLocation
     */
    public String getSysLocation() {
        return sysLocation;
    }

    /**
     * @param sysLocation
     *            the sysLocation to set
     */
    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    /**
     * @return the sniMacAddrTableAgingTime
     */
    public Integer getSniMacAddrTableAgingTime() {
        return sniMacAddrTableAgingTime;
    }

    /**
     * @param sniMacAddrTableAgingTime
     *            the sniMacAddrTableAgingTime to set
     */
    public void setSniMacAddrTableAgingTime(Integer sniMacAddrTableAgingTime) {
        this.sniMacAddrTableAgingTime = sniMacAddrTableAgingTime;
    }

    /**
     * @return the inbandPortIndex
     */
    public String getInbandPortIndex() {
        return inbandPortIndex;
    }

    /**
     * @param inbandPortIndex
     *            the inbandPortIndex to set
     */
    public void setInbandPortIndex(String inbandPortIndex) {
        this.inbandPortIndex = inbandPortIndex;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSysSnmpVersionInSnmp4J() {
        return sysSnmpVersionInSnmp4J;
    }

    public void setSysSnmpVersionInSnmp4J(Integer sysSnmpVersionInSnmp4J) {
        this.sysSnmpVersionInSnmp4J = sysSnmpVersionInSnmp4J;
        switch (sysSnmpVersionInSnmp4J) {
        case 0:
            this.topSysSnmpVersion = 1;
            break;
        case 1:
            this.topSysSnmpVersion = 2;
            break;
        case 3:
            this.topSysSnmpVersion = 3;
            break;
        case 4:
            this.topSysSnmpVersion = 4;
            break;
        default:
            this.topSysSnmpVersion = 2;
            break;
        }
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getbSoftwareVersion() {
        return bSoftwareVersion;
    }

    public void setbSoftwareVersion(String bSoftwareVersion) {
        this.bSoftwareVersion = bSoftwareVersion;
    }

    public Integer getTopSysArpAgingTime() {
        return topSysArpAgingTime;
    }

    public void setTopSysArpAgingTime(Integer topSysArpAgingTime) {
        this.topSysArpAgingTime = topSysArpAgingTime;
    }

    public Double getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(Double cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public Double getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Double memUsed) {
        this.memUsed = memUsed;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public Timestamp getSnapTime() {
        return snapTime;
    }

    public void setSnapTime(Timestamp snapTime) {
        this.snapTime = snapTime;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public Boolean getAttention() {
        return attention;
    }

    public void setAttention(Long attention) {
        if (attention == null) {
            this.attention = false;
        } else {
            this.attention = true;
        }
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSystemRogueCheck() {
        return systemRogueCheck;
    }

    public void setSystemRogueCheck(Integer systemRogueCheck) {
        this.systemRogueCheck = systemRogueCheck;
    }

}
