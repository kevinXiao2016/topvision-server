/***********************************************************************
 * CmtsCm.java,v1.0 17-8-9 下午7:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author jay
 * @created 17-8-9 下午7:05
 */
@Alias("cmtsCm")
public class CmtsCm implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8571259721660333279L;

    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long statusIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.2")
    private String statusMacAddress; // MAC地址
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.3")
    private String statusIpAddress; // IP地址
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.9")
    private Integer statusValue; // 当前状态
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.13")
    private Integer statusSignalNoise;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.21")
    private String statusInetAddress; // 因特网地址
    private String statusInetAddressIpString; // 因特网地址

    private SnmpParam snmpParam;

    private String docsIfCmtsCmStatusInetAddressTypeString;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    public String getStatusMacAddress() {
        return statusMacAddress;
    }

    public void setStatusMacAddress(String statusMacAddress) {
        this.statusMacAddress = statusMacAddress;
    }

    public String getStatusIpAddress() {
        return statusIpAddress;
    }

    public void setStatusIpAddress(String statusIpAddress) {
        this.statusIpAddress = statusIpAddress;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusInetAddress() {
        if (statusInetAddress == null) {
            return statusInetAddress;
        }
        String[] sug = statusInetAddress.trim().split(".");
        if (sug.length == 4) {
            // 老版本的ip 10.10.10.1
            return statusInetAddress;
        } else {
            sug = statusInetAddress.split(":");
            if (sug.length == 4) {
                int[] ipInt = { 0, 0, 0, 0 };
                for (int i = 0; i < sug.length; i++) {
                    ipInt[i] = Integer.parseInt(sug[i], 16);
                }
                // 新版本的ip无法被转换成字符的 6e:12:34:01
                return IpUtils.intArrayToIp(ipInt);
            } else {
                int[] ipInt = { 0, 0, 0, 0 };
                char[] ipChar = statusInetAddress.toCharArray();
                if (ipChar.length == 4) {
                    for (int i = 0; i < ipChar.length; i++) {
                        ipInt[i] = (int) ipChar[i];
                    }
                    // 新版本的ip已经转换成字符的 d a
                    return IpUtils.intArrayToIp(ipInt);
                } else {
                    // ipv6
                    return statusInetAddress;
                }
            }
        }
    }

    public void setStatusInetAddress(String statusInetAddress) {
        this.statusInetAddress = statusInetAddress;
    }

    public String getStatusInetAddressIpString() {
        return statusInetAddressIpString;
    }

    public void setStatusInetAddressIpString(String statusInetAddressIpString) {
        this.statusInetAddressIpString = statusInetAddressIpString;
    }

    public String getDocsIfCmtsCmStatusInetAddressTypeString() {
        return docsIfCmtsCmStatusInetAddressTypeString;
    }

    public void setDocsIfCmtsCmStatusInetAddressTypeString(String docsIfCmtsCmStatusInetAddressTypeString) {
        this.docsIfCmtsCmStatusInetAddressTypeString = docsIfCmtsCmStatusInetAddressTypeString;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Integer getStatusSignalNoise() {
        return statusSignalNoise;
    }

    public void setStatusSignalNoise(Integer statusSignalNoise) {
        this.statusSignalNoise = statusSignalNoise;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    @Override
    public String toString() {
        return "CmtsCm{" + "entityId=" + entityId + ", statusIndex=" + statusIndex + ", statusMacAddress='"
                + statusMacAddress + '\'' + ", statusIpAddress='" + statusIpAddress + '\'' + ", statusValue="
                + statusValue + ", statusInetAddress='" + statusInetAddress + '\'' + ", statusInetAddressIpString='"
                + statusInetAddressIpString + '\'' + ", docsIfCmtsCmStatusInetAddressTypeString='"
                + docsIfCmtsCmStatusInetAddressTypeString + '\'' + '}';
    }
}
