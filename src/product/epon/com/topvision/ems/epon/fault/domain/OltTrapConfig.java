/***********************************************************************
 * $ OltTrapConfig.java,v1.0 2011-10-11 8:26:18 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @modified by victor
 * 
 *           eponManagementAddrName不需要设置，仅仅在mib中使用
 * 
 *           addrTAddress 为trap的ip
 * 
 *           addrTPort 为trap的端口，默认为162.
 * 
 * @modified by Rod 对于字符串作为索引的，统一以后不需要在Domain进行处理，直接传入字符串即可
 * 
 * @created @2011-10-11-8:26:18
 */
public class OltTrapConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8540092700223339295L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.3.1.1.1", index = true)
    private String eponManagementAddrName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.3.1.1.2", writable = true, type = "OctetString")
    private String eponManagementAddrTAddress;
    /**
     * trap ip
     */
    private String addrTAddress;
    /**
     * trap port
     */
    private Integer addrTPort = 162;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.3.1.1.3", writable = true, type = "OctetString")
    private String eponManagementAddrCommunity;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.3.1.1.4", writable = true, type = "Integer32")
    private Integer eponManagementAddrRowStatus;

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
     * @return the eponManagementAddrName
     */
    public String getEponManagementAddrName() {
        return eponManagementAddrName;
    }

    /**
     * @param eponManagementAddrName
     *            the eponManagementAddrName to set
     */
    public void setEponManagementAddrName(String eponManagementAddrName) {
        this.eponManagementAddrName = eponManagementAddrName;
    }

    /**
     * @return the eponManagementAddrTAddress
     */
    public String getEponManagementAddrTAddress() {
        if (eponManagementAddrTAddress == null) {
            generateAddress();
        }
        return eponManagementAddrTAddress;
    }

    /**
     * @param eponManagementAddrTAddress
     *            the eponManagementAddrTAddress to set
     */
    public void setEponManagementAddrTAddress(String eponManagementAddrTAddress) {
        // like this AC:0A:0A:32:00:00:00:A2
        this.eponManagementAddrTAddress = eponManagementAddrTAddress;
        if (eponManagementAddrTAddress != null) {
            // ip
            String[] ss = eponManagementAddrTAddress.split(":");
            StringBuilder ip = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                ip.append(".").append(Integer.parseInt(ss[i], 16));
            }
            addrTAddress = ip.substring(1);
            // port
            String port = eponManagementAddrTAddress.substring(12);
            port = port.replaceAll(":", "");
            addrTPort = Integer.parseInt(port, 16);
        }
    }

    /**
     * @return the addrTAddress
     */
    public String getAddrTAddress() {
        return addrTAddress;
    }

    /**
     * @param addrTAddress
     *            the addrTAddress to set
     */
    public void setAddrTAddress(String addrTAddress) {
        this.addrTAddress = addrTAddress;
        /*
         *  @modify by Rod
         * 
         * if (eponManagementAddrName == null) { // ip变成oid作为index,agent要求这个oid对应的为可见字符
         * StringBuilder oid = new StringBuilder(); for (byte c : addrTAddress.getBytes()) {
         * oid.append(".").append(c); } eponManagementAddrName = oid.substring(1); }
         */
        if (addrTPort != null) {
            generateAddress();
        }
    }

    /**
     * @return the eponManagementAddrCommunity
     */
    public String getEponManagementAddrCommunity() {
        return eponManagementAddrCommunity;
    }

    /**
     * @param eponManagementAddrCommunity
     *            the eponManagementAddrCommunity to set
     */
    public void setEponManagementAddrCommunity(String eponManagementAddrCommunity) {
        this.eponManagementAddrCommunity = eponManagementAddrCommunity;
    }

    /**
     * @return the eponManagementAddrRowStatus
     */
    public Integer getEponManagementAddrRowStatus() {
        return eponManagementAddrRowStatus;
    }

    /**
     * @param eponManagementAddrRowStatus
     *            the eponManagementAddrRowStatus to set
     */
    public void setEponManagementAddrRowStatus(Integer eponManagementAddrRowStatus) {
        this.eponManagementAddrRowStatus = eponManagementAddrRowStatus;
    }

    /**
     * @return the addrTPort
     */
    public Integer getAddrTPort() {
        return addrTPort;
    }

    /**
     * @param addrTPort
     *            the addrTPort to set
     */
    public void setAddrTPort(Integer addrTPort) {
        this.addrTPort = addrTPort;
        if (addrTAddress != null) {
            generateAddress();
        }
    }

    private void generateAddress() {
        if (addrTAddress != null) {
            String[] tmp = addrTAddress.split("\\.");
            StringBuilder addr = new StringBuilder();
            for (String s : tmp) {
                s = "00" + Integer.toHexString(Integer.parseInt(s));
                addr.append(":").append(s.substring(s.length() - 2));
            }
            String port = "0000" + Integer.toHexString(addrTPort);
            port = port.substring(port.length() - 4);
            addr.append(":00:00:").append(port);
            addr.insert(21, ':');
            eponManagementAddrTAddress = addr.substring(1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltTrapConfig [entityId=");
        builder.append(entityId);
        builder.append(", eponManagementAddrName=");
        builder.append(eponManagementAddrName);
        builder.append(", eponManagementAddrTAddress=");
        builder.append(eponManagementAddrTAddress);
        builder.append(", addrTAddress=");
        builder.append(addrTAddress);
        builder.append(", addrTPort=");
        builder.append(addrTPort);
        builder.append(", eponManagementAddrCommunity=");
        builder.append(eponManagementAddrCommunity);
        builder.append(", eponManagementAddrRowStatus=");
        builder.append(eponManagementAddrRowStatus);
        builder.append("]");
        return builder.toString();
    }
}
