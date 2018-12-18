/***********************************************************************
 * $Id: PingCtlEntry.java,v1.0 2016-7-26 下午1:46:17 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Rod John
 * @created @2016-7-26-下午1:46:17
 *
 */
public class PingCtlEntry extends BaseEntity {

    private static final long serialVersionUID = 676141749148064968L;
    public static final Integer IPV4 = 1;
    public static final Integer IPV6 = 2;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.1", index = true)
    private String pingCtlOwnerIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.2", index = true)
    private String pingCtlTestName;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.3", writable = true, type = "Integer32")
    private Integer pingCtlTargetAddressType;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.4", writable = true, type = "OctetString")
    private String pingCtlTargetAddress;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.6", writable = true, type = "Gauge32")
    private Integer pingCtlTimeOut;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.7", writable = true, type = "Gauge32")
    private Integer pingCtlProbeCount;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.8", writable = true, type = "Integer32")
    private Integer pingCtlAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.23", writable = true, type = "Integer32")
    private Integer pingCtlRowStatus;

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
     * @return the pingCtlOwnerIndex
     */
    public String getPingCtlOwnerIndex() {
        return pingCtlOwnerIndex;
    }

    /**
     * @param pingCtlOwnerIndex the pingCtlOwnerIndex to set
     */
    public void setPingCtlOwnerIndex(String pingCtlOwnerIndex) {
        this.pingCtlOwnerIndex = pingCtlOwnerIndex;
    }

    /**
     * @return the pingCtlTestName
     */
    public String getPingCtlTestName() {
        return pingCtlTestName;
    }

    /**
     * @param pingCtlTestName the pingCtlTestName to set
     */
    public void setPingCtlTestName(String pingCtlTestName) {
        this.pingCtlTestName = pingCtlTestName;
    }

    /**
     * @return the pingCtlTargetAddressType
     */
    public Integer getPingCtlTargetAddressType() {
        return pingCtlTargetAddressType;
    }

    /**
     * @param pingCtlTargetAddressType the pingCtlTargetAddressType to set
     */
    public void setPingCtlTargetAddressType(Integer pingCtlTargetAddressType) {
        this.pingCtlTargetAddressType = pingCtlTargetAddressType;
    }

    /**
     * @return the pingCtlTargetAddress
     */
    public String getPingCtlTargetAddress() {
        return pingCtlTargetAddress;
    }

    /**
     * @param pingCtlTargetAddress the pingCtlTargetAddress to set
     */
    public void setPingCtlTargetAddress(String pingCtlTargetAddress) {
        this.pingCtlTargetAddress = pingCtlTargetAddress;
    }

    /**
     * @return the pingCtlProbeCount
     */
    public Integer getPingCtlProbeCount() {
        return pingCtlProbeCount;
    }

    /**
     * @param pingCtlProbeCount the pingCtlProbeCount to set
     */
    public void setPingCtlProbeCount(Integer pingCtlProbeCount) {
        this.pingCtlProbeCount = pingCtlProbeCount;
    }

    /**
     * @return the pingCtlTimeOut
     */
    public Integer getPingCtlTimeOut() {
        return pingCtlTimeOut;
    }

    /**
     * @param pingCtlTimeOut the pingCtlTimeOut to set
     */
    public void setPingCtlTimeOut(Integer pingCtlTimeOut) {
        this.pingCtlTimeOut = pingCtlTimeOut;
    }

    /**
     * @return the pingCtlAdminStatus
     */
    public Integer getPingCtlAdminStatus() {
        return pingCtlAdminStatus;
    }

    /**
     * @param pingCtlAdminStatus the pingCtlAdminStatus to set
     */
    public void setPingCtlAdminStatus(Integer pingCtlAdminStatus) {
        this.pingCtlAdminStatus = pingCtlAdminStatus;
    }

    /**
     * @return the pingCtlRowStatus
     */
    public Integer getPingCtlRowStatus() {
        return pingCtlRowStatus;
    }

    /**
     * @param pingCtlRowStatus the pingCtlRowStatus to set
     */
    public void setPingCtlRowStatus(Integer pingCtlRowStatus) {
        this.pingCtlRowStatus = pingCtlRowStatus;
    }

}
