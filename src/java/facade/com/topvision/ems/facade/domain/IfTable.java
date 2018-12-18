/***********************************************************************
 * $Id: IfTable.java,v 1.1 Mar 20, 2009 10:01:20 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @Create Date Mar 20, 2009 10:01:20 PM
 * 
 * @author kelers
 * 
 */
public class IfTable extends AbstractProperty {
    private static final long serialVersionUID = 5304304804065197488L;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1")
    private Integer ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.2")
    private String ifDescr;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.3")
    private Integer ifType;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.4")
    private Integer ifMtu;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.5")
    private Long ifSpeed;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.6")
    private String ifPhysAddress;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.7")
    private Byte ifAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.8")
    private Byte ifOperStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.9")
    private String ifLastChange;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.10")
    private Long ifInOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.11")
    private Long ifInUcastPkts;
    @SnmpProperty(oid = ".1.3.6.1.2.1.2.2.1.12")
    private Long ifInNUcastPkts;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.13")
    private Long ifInDiscards;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.14")
    private Long ifInErrors;
    @SnmpProperty(oid = ".1.3.6.1.2.1.2.2.1.15")
    private Long ifInUnknownProtos;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.16")
    private Long ifOutOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.17")
    private Long ifOutUcastPkts;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.18")
    private Long ifOutNUcastPkts;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.19")
    private Long ifOutDiscards;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.20")
    private Long ifOutErrors;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.21")
    private Long ifOutQLen;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.22")
    private String ifSpecific;

    /**
     * @return the ifAdminStatus
     */
    public Byte getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @return the ifDescr
     */
    public String getIfDescr() {
        return ifDescr;
    }

    /**
     * @return the ifIndex
     */
    public Integer getIfIndex() {
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
     * @return the ifMtu
     */
    public Integer getIfMtu() {
        return ifMtu;
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
     * @return the ifPhysAddress
     */
    public String getIfPhysAddress() {
        return ifPhysAddress;
    }

    /**
     * @return the ifSpecific
     */
    public String getIfSpecific() {
        return ifSpecific;
    }

    /**
     * @return the ifSpeed
     */
    public Long getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @return the ifType
     */
    public Integer getIfType() {
        return ifType;
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(Byte ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    /**
     * @param ifDescr
     *            the ifDescr to set
     */
    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Integer ifIndex) {
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
     * @param ifMtu
     *            the ifMtu to set
     */
    public void setIfMtu(Integer ifMtu) {
        this.ifMtu = ifMtu;
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

    /**
     * @param ifPhysAddress
     *            the ifPhysAddress to set
     */
    public void setIfPhysAddress(String ifPhysAddress) {
        this.ifPhysAddress = ifPhysAddress;
    }

    /**
     * @param ifSpecific
     *            the ifSpecific to set
     */
    public void setIfSpecific(String ifSpecific) {
        this.ifSpecific = ifSpecific;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @param ifType
     *            the ifType to set
     */
    public void setIfType(Integer ifType) {
        this.ifType = ifType;
    }
}
