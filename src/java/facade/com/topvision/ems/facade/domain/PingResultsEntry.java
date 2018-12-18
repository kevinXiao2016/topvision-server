/***********************************************************************
 * $Id: PingResultsEntry.java,v1.0 2016-7-26 下午1:46:56 $
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
 * @created @2016-7-26-下午1:46:56
 *
 */
public class PingResultsEntry extends BaseEntity {

    private static final long serialVersionUID = -190365729081131926L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.1", index = true)
    private String pingCtlOwnerIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.2.1.2", index = true)
    private String pingCtlTestName;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.3.1.1")
    private Integer pingResultsOperStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.3.1.2")
    private Integer pingResultsIpTargetAddressType;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.3.1.3")
    private String pingResultsIpTargetAddress;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.3.1.4")
    private Integer pingResultsMinRtt;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.3.1.5")
    private Integer pingResultsMaxRtt;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.3.1.6")
    private Integer pingResultsAverageRtt;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.3.1.7")
    private Integer pingResultsProbeResponses;
    @SnmpProperty(oid = "1.3.6.1.2.1.80.1.3.1.10")
    private String pingResultsLastGoodProbe;

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
     * @return the pingResultsOperStatus
     */
    public Integer getPingResultsOperStatus() {
        return pingResultsOperStatus;
    }

    /**
     * @param pingResultsOperStatus the pingResultsOperStatus to set
     */
    public void setPingResultsOperStatus(Integer pingResultsOperStatus) {
        this.pingResultsOperStatus = pingResultsOperStatus;
    }

    /**
     * @return the pingResultsIpTargetAddressType
     */
    public Integer getPingResultsIpTargetAddressType() {
        return pingResultsIpTargetAddressType;
    }

    /**
     * @param pingResultsIpTargetAddressType the pingResultsIpTargetAddressType to set
     */
    public void setPingResultsIpTargetAddressType(Integer pingResultsIpTargetAddressType) {
        this.pingResultsIpTargetAddressType = pingResultsIpTargetAddressType;
    }

    /**
     * @return the pingResultsIpTargetAddress
     */
    public String getPingResultsIpTargetAddress() {
        return pingResultsIpTargetAddress;
    }

    /**
     * @param pingResultsIpTargetAddress the pingResultsIpTargetAddress to set
     */
    public void setPingResultsIpTargetAddress(String pingResultsIpTargetAddress) {
        this.pingResultsIpTargetAddress = pingResultsIpTargetAddress;
    }

    /**
     * @return the pingResultsMinRtt
     */
    public Integer getPingResultsMinRtt() {
        return pingResultsMinRtt;
    }

    /**
     * @param pingResultsMinRtt the pingResultsMinRtt to set
     */
    public void setPingResultsMinRtt(Integer pingResultsMinRtt) {
        this.pingResultsMinRtt = pingResultsMinRtt;
    }

    /**
     * @return the pingResultsMaxRtt
     */
    public Integer getPingResultsMaxRtt() {
        return pingResultsMaxRtt;
    }

    /**
     * @param pingResultsMaxRtt the pingResultsMaxRtt to set
     */
    public void setPingResultsMaxRtt(Integer pingResultsMaxRtt) {
        this.pingResultsMaxRtt = pingResultsMaxRtt;
    }

    /**
     * @return the pingResultsAverageRtt
     */
    public Integer getPingResultsAverageRtt() {
        return pingResultsAverageRtt;
    }

    /**
     * @param pingResultsAverageRtt the pingResultsAverageRtt to set
     */
    public void setPingResultsAverageRtt(Integer pingResultsAverageRtt) {
        this.pingResultsAverageRtt = pingResultsAverageRtt;
    }

    /**
     * @return the pingResultsProbeResponses
     */
    public Integer getPingResultsProbeResponses() {
        return pingResultsProbeResponses;
    }

    /**
     * @param pingResultsProbeResponses the pingResultsProbeResponses to set
     */
    public void setPingResultsProbeResponses(Integer pingResultsProbeResponses) {
        this.pingResultsProbeResponses = pingResultsProbeResponses;
    }

    /**
     * @return the pingResultsLastGoodProbe
     */
    public String getPingResultsLastGoodProbe() {
        return pingResultsLastGoodProbe;
    }

    /**
     * @param pingResultsLastGoodProbe the pingResultsLastGoodProbe to set
     */
    public void setPingResultsLastGoodProbe(String pingResultsLastGoodProbe) {
        this.pingResultsLastGoodProbe = pingResultsLastGoodProbe;
    }

}
