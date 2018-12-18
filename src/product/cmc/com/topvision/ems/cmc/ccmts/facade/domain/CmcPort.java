/***********************************************************************
 * $Id: CmcPort.java,v1.0 2011-10-28 下午02:36:38 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-10-28-下午02:36:38
 * 
 * @modify by Rod
 */
@Alias("cmcPortAttribute")
public class CmcPort implements Serializable, AliasesSuperType {
    /**
     * ifType：下行信道 modify by Rod ifType : 下行接口
     */
    public static final Integer IF_TYPE_CABLE_DOWNSTREAM = 128;
    /**
     * ifType:上行信道
     * 
     * modify by Rod ifType : 上行接口
     */
    public static final Integer IF_TYPE_CABLE_UPSTREAM = 129;
    /**
     * 
     * modify by Rod ifType : 下行信道 128在标准文档中没有找到，当前设备均使用128
     */
    public static final Integer IF_TYPE_CABLE_DOWNSTREAM_CHANNEL = 128;
    /**
     * 
     * modify by Rod ifType : 上行信道 205是标准文档定义
     */
    public static final Integer IF_TYPE_CABLE_UPSTREAM_CHANNEL = 205;

    /**
     * ifType:Mac layer
     */
    public static final Integer IF_TYPE_MACLAYER = 127;
    
    public static final Integer IF_TYPE_EQAM = 214;
    /**
     * ifType:其他未知
     */
    public static final Integer IF_TYPE_OTHER = 1;
    private static final long serialVersionUID = -1014232022945986624L;
    private Long cmcPortId;
    private Long cmcIndex;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.2")
    private String ifDescr;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.3")
    private Integer ifType;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.4")
    private Integer ifMtu;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.5")
    private Integer ifSpeed;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.6")
    private String ifPhysAddress;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.7", writable = true, type = "Integer32")
    private Integer ifAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.8")
    private Integer ifOperStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.9")
    private Long ifLastChange;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.1")
    private String ifName;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.18")
    private String ifAlias;

    public CmcPort() {
    }

    public CmcPort(Long upChannelIndex, Long cmcIndex) {
        this.ifIndex = upChannelIndex;
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the ifDescr
     */
    public String getIfDescr() {
        return ifDescr;
    }

    /**
     * @param ifDescr
     *            the ifDescr to set
     */
    public void setIfDescr(String ifDescr) {
        if (ifDescr != null && ifDescr.length() > 255) {
            this.ifDescr = ifDescr.substring(0, 255);
        } else {
            this.ifDescr = ifDescr;
        }
    }

    /**
     * @return the ifType
     */
    public Integer getIfType() {
        return ifType;
    }

    /**
     * @param ifType
     *            the ifType to set
     */
    public void setIfType(Integer ifType) {
        this.ifType = ifType;
    }

    /**
     * @return the ifMtu
     */
    public Integer getIfMtu() {
        return ifMtu;
    }

    /**
     * @param ifMtu
     *            the ifMtu to set
     */
    public void setIfMtu(Integer ifMtu) {
        this.ifMtu = ifMtu;
    }

    /**
     * @return the ifSpeed
     */
    public Integer getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Integer ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @return the ifPhysAddress
     */
    public String getIfPhysAddress() {
        return ifPhysAddress;
    }

    /**
     * @param ifPhysAddress
     *            the ifPhysAddress to set
     */
    public void setIfPhysAddress(String ifPhysAddress) {
        this.ifPhysAddress = ifPhysAddress;
    }

    /**
     * @return the ifLastChange
     */
    public Long getIfLastChange() {
        return ifLastChange;
    }

    /**
     * @param ifLastChange
     *            the ifLastChange to set
     */
    public void setIfLastChange(Long ifLastChange) {
        this.ifLastChange = ifLastChange;
    }

    /**
     * @return the ifAdminStatus
     */
    public Integer getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(Integer ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    /**
     * @return the ifOperStatus
     */
    public Integer getIfOperStatus() {
        return ifOperStatus;
    }

    /**
     * @param ifOperStatus
     *            the ifOperStatus to set
     */
    public void setIfOperStatus(Integer ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    public String getIfAlias() {
        return ifAlias;
    }

    public void setIfAlias(String ifAlias) {
        this.ifAlias = ifAlias;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcPort [cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(", ifType=");
        builder.append(ifType);
        builder.append(", ifMtu=");
        builder.append(ifMtu);
        builder.append(", ifSpeed=");
        builder.append(ifSpeed);
        builder.append(", ifPhysAddress=");
        builder.append(ifPhysAddress);
        builder.append(", ifAdminStatus=");
        builder.append(ifAdminStatus);
        builder.append(", ifOperStatus=");
        builder.append(ifOperStatus);
        builder.append(", ifLastChange=");
        builder.append(ifLastChange);
        builder.append(", ifAlias=");
        builder.append(ifAlias);
        builder.append(", ifName=");
        builder.append(ifName);
        builder.append("]");
        return builder.toString();
    }

}
