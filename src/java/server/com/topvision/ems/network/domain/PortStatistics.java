package com.topvision.ems.network.domain;

import java.sql.Timestamp;

public class PortStatistics implements java.io.Serializable {
    private static final long serialVersionUID = -2287267060622931421L;
    private Long ifIndex;
    private String ifName;
    private Byte ifAdminStatus;
    private Byte ifOperStatus;
    private String ifLastChange;
    private Long ifInOctets;
    private Long ifInUcastPkts;
    private Long ifInNUcastPkts;
    private Long ifInDiscards;
    private Long ifInErrors;
    private Long ifInUnknownProtos;
    private Long ifOutOctets;
    private Long ifOutUcastPkts;
    private Long ifOutNUcastPkts;
    private Long ifOutDiscards;
    private Long ifOutErrors;
    private Long ifOutQLen;
    private Timestamp collectTime;

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @return the ifAdminStatus
     */
    public Byte getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @return the ifInDiscards
     */
    public Long getIfInDiscards() {
        return ifInDiscards;
    }

    /**
     * @return the ifInErrors
     */
    public Long getIfInErrors() {
        return ifInErrors;
    }

    /**
     * @return the ifInNUcastPkts
     */
    public Long getIfInNUcastPkts() {
        return ifInNUcastPkts;
    }

    /**
     * @return the ifInOctets
     */
    public Long getIfInOctets() {
        return ifInOctets;
    }

    /**
     * @return the ifInUcastPkts
     */
    public Long getIfInUcastPkts() {
        return ifInUcastPkts;
    }

    /**
     * @return the ifInUnknownProtos
     */
    public Long getIfInUnknownProtos() {
        return ifInUnknownProtos;
    }

    /**
     * @return the ifLastChange
     */
    public String getIfLastChange() {
        return ifLastChange;
    }

    /**
     * @return the ifName
     */
    public String getIfName() {
        return ifName;
    }

    /**
     * @return the ifOperStatus
     */
    public Byte getIfOperStatus() {
        return ifOperStatus;
    }

    /**
     * @return the ifOutDiscards
     */
    public Long getIfOutDiscards() {
        return ifOutDiscards;
    }

    /**
     * @return the ifOutErrors
     */
    public Long getIfOutErrors() {
        return ifOutErrors;
    }

    /**
     * @return the ifOutNUcastPkts
     */
    public Long getIfOutNUcastPkts() {
        return ifOutNUcastPkts;
    }

    /**
     * @return the ifOutOctets
     */
    public Long getIfOutOctets() {
        return ifOutOctets;
    }

    /**
     * @return the ifOutQLen
     */
    public Long getIfOutQLen() {
        return ifOutQLen;
    }

    /**
     * @return the ifOutUcastPkts
     */
    public Long getIfOutUcastPkts() {
        return ifOutUcastPkts;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(Byte ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @param ifInDiscards
     *            the ifInDiscards to set
     */
    public void setIfInDiscards(Long ifInDiscards) {
        this.ifInDiscards = ifInDiscards;
    }

    /**
     * @param ifInErrors
     *            the ifInErrors to set
     */
    public void setIfInErrors(Long ifInErrors) {
        this.ifInErrors = ifInErrors;
    }

    /**
     * @param ifInNUcastPkts
     *            the ifInNUcastPkts to set
     */
    public void setIfInNUcastPkts(Long ifInNUcastPkts) {
        this.ifInNUcastPkts = ifInNUcastPkts;
    }

    /**
     * @param ifInOctets
     *            the ifInOctets to set
     */
    public void setIfInOctets(Long ifInOctets) {
        this.ifInOctets = ifInOctets;
    }

    /**
     * @param ifInUcastPkts
     *            the ifInUcastPkts to set
     */
    public void setIfInUcastPkts(Long ifInUcastPkts) {
        this.ifInUcastPkts = ifInUcastPkts;
    }

    /**
     * @param ifInUnknownProtos
     *            the ifInUnknownProtos to set
     */
    public void setIfInUnknownProtos(Long ifInUnknownProtos) {
        this.ifInUnknownProtos = ifInUnknownProtos;
    }

    /**
     * @param ifLastChange
     *            the ifLastChange to set
     */
    public void setIfLastChange(String ifLastChange) {
        this.ifLastChange = ifLastChange;
    }

    /**
     * @param ifName
     *            the ifName to set
     */
    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    /**
     * @param ifOperStatus
     *            the ifOperStatus to set
     */
    public void setIfOperStatus(Byte ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    /**
     * @param ifOutDiscards
     *            the ifOutDiscards to set
     */
    public void setIfOutDiscards(Long ifOutDiscards) {
        this.ifOutDiscards = ifOutDiscards;
    }

    /**
     * @param ifOutErrors
     *            the ifOutErrors to set
     */
    public void setIfOutErrors(Long ifOutErrors) {
        this.ifOutErrors = ifOutErrors;
    }

    /**
     * @param ifOutNUcastPkts
     *            the ifOutNUcastPkts to set
     */
    public void setIfOutNUcastPkts(Long ifOutNUcastPkts) {
        this.ifOutNUcastPkts = ifOutNUcastPkts;
    }

    /**
     * @param ifOutOctets
     *            the ifOutOctets to set
     */
    public void setIfOutOctets(Long ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }

    /**
     * @param ifOutQLen
     *            the ifOutQLen to set
     */
    public void setIfOutQLen(Long ifOutQLen) {
        this.ifOutQLen = ifOutQLen;
    }

    /**
     * @param ifOutUcastPkts
     *            the ifOutUcastPkts to set
     */
    public void setIfOutUcastPkts(Long ifOutUcastPkts) {
        this.ifOutUcastPkts = ifOutUcastPkts;
    }
}
