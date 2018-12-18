/***********************************************************************
 * $Id: OltUniPortRateLimit.java,v1.0 2011-10-18 下午03:01:30 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-10-18-下午03:01:30
 * 
 */
@Alias(value = "oltUniRateLimit")
public class OltUniPortRateLimit implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8597302703106462007L;
    private Long entityId;
    private Long uniId;
    private Long uniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.1", index = true)
    private Long uniPortRateLimitDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.2", index = true)
    private Long uniPortRateLimitCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.3", index = true)
    private Long uniPortRateLimitPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.4", writable = true, type = "Integer32")
    private Integer uniPortInCIR;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.5", writable = true, type = "Integer32")
    private Integer uniPortInCBS;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.6", writable = true, type = "Integer32")
    private Integer uniPortInEBS;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.7", writable = true, type = "Integer32")
    private Integer uniPortOutCIR;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.8", writable = true, type = "Integer32")
    private Integer uniPortOutPIR;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.9", writable = true, type = "Integer32")
    private Integer uniPortInRateLimitEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.4.1.10", writable = true, type = "Integer32")
    private Integer uniPortOutRateLimitEnable;

    /**
     * @return the uniPortRateLimitDeviceIndex
     */
    public Long getUniPortRateLimitDeviceIndex() {
        return uniPortRateLimitDeviceIndex;
    }

    /**
     * @param uniPortRateLimitDeviceIndex
     *            the uniPortRateLimitDeviceIndex to set
     */
    public void setUniPortRateLimitDeviceIndex(Long uniPortRateLimitDeviceIndex) {
        this.uniPortRateLimitDeviceIndex = uniPortRateLimitDeviceIndex;
    }

    /**
     * @return the uniPortRateLimitCardIndex
     */
    public Long getUniPortRateLimitCardIndex() {
        return uniPortRateLimitCardIndex;
    }

    /**
     * @param uniPortRateLimitCardIndex
     *            the uniPortRateLimitCardIndex to set
     */
    public void setUniPortRateLimitCardIndex(Long uniPortRateLimitCardIndex) {
        this.uniPortRateLimitCardIndex = uniPortRateLimitCardIndex;
    }

    /**
     * @return the uniPortRateLimitPortIndex
     */
    public Long getUniPortRateLimitPortIndex() {
        return uniPortRateLimitPortIndex;
    }

    /**
     * @param uniPortRateLimitPortIndex
     *            the uniPortRateLimitPortIndex to set
     */
    public void setUniPortRateLimitPortIndex(Long uniPortRateLimitPortIndex) {
        this.uniPortRateLimitPortIndex = uniPortRateLimitPortIndex;
    }

    /**
     * @return the uniPortInCIR
     */
    public Integer getUniPortInCIR() {
        return uniPortInCIR;
    }

    /**
     * @param uniPortInCIR
     *            the uniPortInCIR to set
     */
    public void setUniPortInCIR(Integer uniPortInCIR) {
        this.uniPortInCIR = uniPortInCIR;
    }

    /**
     * @return the uniPortInCBS
     */
    public Integer getUniPortInCBS() {
        return uniPortInCBS;
    }

    /**
     * @param uniPortInCBS
     *            the uniPortInCBS to set
     */
    public void setUniPortInCBS(Integer uniPortInCBS) {
        this.uniPortInCBS = uniPortInCBS;
    }

    /**
     * @return the uniPortInEBS
     */
    public Integer getUniPortInEBS() {
        return uniPortInEBS;
    }

    /**
     * @param uniPortInEBS
     *            the uniPortInEBS to set
     */
    public void setUniPortInEBS(Integer uniPortInEBS) {
        this.uniPortInEBS = uniPortInEBS;
    }

    /**
     * @return the uniPortOutCIR
     */
    public Integer getUniPortOutCIR() {
        return uniPortOutCIR;
    }

    /**
     * @param uniPortOutCIR
     *            the uniPortOutCIR to set
     */
    public void setUniPortOutCIR(Integer uniPortOutCIR) {
        this.uniPortOutCIR = uniPortOutCIR;
    }

    /**
     * @return the uniPortOutPIR
     */
    public Integer getUniPortOutPIR() {
        return uniPortOutPIR;
    }

    /**
     * @param uniPortOutPIR
     *            the uniPortOutPIR to set
     */
    public void setUniPortOutPIR(Integer uniPortOutPIR) {
        this.uniPortOutPIR = uniPortOutPIR;
    }

    /**
     * @return the uniPortInRateLimitEnable
     */
    public Integer getUniPortInRateLimitEnable() {
        return uniPortInRateLimitEnable;
    }

    /**
     * @param uniPortInRateLimitEnable
     *            the uniPortInRateLimitEnable to set
     */
    public void setUniPortInRateLimitEnable(Integer uniPortInRateLimitEnable) {
        this.uniPortInRateLimitEnable = uniPortInRateLimitEnable;
    }

    /**
     * @return the uniPortOutRateLimitEnable
     */
    public Integer getUniPortOutRateLimitEnable() {
        return uniPortOutRateLimitEnable;
    }

    /**
     * @param uniPortOutRateLimitEnable
     *            the uniPortOutRateLimitEnable to set
     */
    public void setUniPortOutRateLimitEnable(Integer uniPortOutRateLimitEnable) {
        this.uniPortOutRateLimitEnable = uniPortOutRateLimitEnable;
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
     * @return the uniId
     */
    public Long getUniId() {
        return uniId;
    }

    /**
     * @param uniId
     *            the uniId to set
     */
    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    /**
     * @return the uniIndex
     */
    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndexByMibIndex(uniPortRateLimitDeviceIndex, uniPortRateLimitCardIndex,
                    uniPortRateLimitPortIndex);
        }
        return uniIndex;
    }

    /**
     * @param uniIndex
     *            the uniIndex to set
     */
    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        uniPortRateLimitDeviceIndex = EponIndex.getOnuMibIndexByIndex(uniIndex);
        uniPortRateLimitCardIndex = EponIndex.getOnuCardNo(uniIndex);
        uniPortRateLimitPortIndex = EponIndex.getUniNo(uniIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltUniPortRateLimit [entityId=");
        builder.append(entityId);
        builder.append(", uniId=");
        builder.append(uniId);
        builder.append(", uniIndex=");
        builder.append(uniIndex);
        builder.append(", uniPortRateLimitDeviceIndex=");
        builder.append(uniPortRateLimitDeviceIndex);
        builder.append(", uniPortRateLimitCardIndex=");
        builder.append(uniPortRateLimitCardIndex);
        builder.append(", uniPortRateLimitPortIndex=");
        builder.append(uniPortRateLimitPortIndex);
        builder.append(", uniPortInCIR=");
        builder.append(uniPortInCIR);
        builder.append(", uniPortInCBS=");
        builder.append(uniPortInCBS);
        builder.append(", uniPortInEBS=");
        builder.append(uniPortInEBS);
        builder.append(", uniPortOutCIR=");
        builder.append(uniPortOutCIR);
        builder.append(", uniPortOutPIR=");
        builder.append(uniPortOutPIR);
        builder.append(", uniPortInRateLimitEnable=");
        builder.append(uniPortInRateLimitEnable);
        builder.append(", uniPortOutRateLimitEnable=");
        builder.append(uniPortOutRateLimitEnable);
        builder.append("]");
        return builder.toString();
    }

}
