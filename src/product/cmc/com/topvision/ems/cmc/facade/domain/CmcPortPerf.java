/***********************************************************************
 * $Id: CmcPortMonitorDomain.java,v1.0 2011-10-28 下午02:39:39 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-10-28-下午02:39:39
 * 
 */
@Alias("cmcPortPerf")
public class CmcPortPerf implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7830813196963179988L;
    private Long cmcPortId;
    private Long cmcId;
    private Long monitorId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.5")
    private Long ifSpeed;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.10")
    private Long ifInOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.11")
    private Long ifInUcastPkts;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.12")
    private Long ifInNUcastPkts;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.13")
    private Long ifInDiscards;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.14")
    private Long ifInErrors;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.15")
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
     * @return the monitorId
     */
    public Long getMonitorId() {
        return monitorId;
    }

    /**
     * @param monitorId
     *            the monitorId to set
     */
    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
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
     * @return the ifSpeed
     */
    public Long getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @return the ifInOctets
     */
    public Long getIfInOctets() {
        return ifInOctets;
    }

    /**
     * @param ifInOctets
     *            the ifInOctets to set
     */
    public void setIfInOctets(Long ifInOctets) {
        this.ifInOctets = ifInOctets;
    }

    /**
     * @return the ifInUcastPkts
     */
    public Long getIfInUcastPkts() {
        return ifInUcastPkts;
    }

    /**
     * @param ifInUcastPkts
     *            the ifInUcastPkts to set
     */
    public void setIfInUcastPkts(Long ifInUcastPkts) {
        this.ifInUcastPkts = ifInUcastPkts;
    }

    /**
     * @return the ifInNUcastPkts
     */
    public Long getIfInNUcastPkts() {
        return ifInNUcastPkts;
    }

    /**
     * @param ifInNUcastPkts
     *            the ifInNUcastPkts to set
     */
    public void setIfInNUcastPkts(Long ifInNUcastPkts) {
        this.ifInNUcastPkts = ifInNUcastPkts;
    }

    /**
     * @return the ifInDiscards
     */
    public Long getIfInDiscards() {
        return ifInDiscards;
    }

    /**
     * @param ifInDiscards
     *            the ifInDiscards to set
     */
    public void setIfInDiscards(Long ifInDiscards) {
        this.ifInDiscards = ifInDiscards;
    }

    /**
     * @return the ifInErrors
     */
    public Long getIfInErrors() {
        return ifInErrors;
    }

    /**
     * @param ifInErrors
     *            the ifInErrors to set
     */
    public void setIfInErrors(Long ifInErrors) {
        this.ifInErrors = ifInErrors;
    }

    /**
     * @return the ifInUnknownProtos
     */
    public Long getIfInUnknownProtos() {
        return ifInUnknownProtos;
    }

    /**
     * @param ifInUnknownProtos
     *            the ifInUnknownProtos to set
     */
    public void setIfInUnknownProtos(Long ifInUnknownProtos) {
        this.ifInUnknownProtos = ifInUnknownProtos;
    }

    /**
     * @return the ifOutOctets
     */
    public Long getIfOutOctets() {
        return ifOutOctets;
    }

    /**
     * @param ifOutOctets
     *            the ifOutOctets to set
     */
    public void setIfOutOctets(Long ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }

    /**
     * @return the ifOutUcastPkts
     */
    public Long getIfOutUcastPkts() {
        return ifOutUcastPkts;
    }

    /**
     * @param ifOutUcastPkts
     *            the ifOutUcastPkts to set
     */
    public void setIfOutUcastPkts(Long ifOutUcastPkts) {
        this.ifOutUcastPkts = ifOutUcastPkts;
    }

    /**
     * @return the ifOutNUcastPkts
     */
    public Long getIfOutNUcastPkts() {
        return ifOutNUcastPkts;
    }

    /**
     * @param ifOutNUcastPkts
     *            the ifOutNUcastPkts to set
     */
    public void setIfOutNUcastPkts(Long ifOutNUcastPkts) {
        this.ifOutNUcastPkts = ifOutNUcastPkts;
    }

    /**
     * @return the ifOutDiscards
     */
    public Long getIfOutDiscards() {
        return ifOutDiscards;
    }

    /**
     * @param ifOutDiscards
     *            the ifOutDiscards to set
     */
    public void setIfOutDiscards(Long ifOutDiscards) {
        this.ifOutDiscards = ifOutDiscards;
    }

    /**
     * @return the ifOutErrors
     */
    public Long getIfOutErrors() {
        return ifOutErrors;
    }

    /**
     * @param ifOutErrors
     *            the ifOutErrors to set
     */
    public void setIfOutErrors(Long ifOutErrors) {
        this.ifOutErrors = ifOutErrors;
    }

    /**
     * @return the ifOutQLen
     */
    public Long getIfOutQLen() {
        return ifOutQLen;
    }

    /**
     * @param ifOutQLen
     *            the ifOutQLen to set
     */
    public void setIfOutQLen(Long ifOutQLen) {
        this.ifOutQLen = ifOutQLen;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcPortPerf [cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", monitorId=");
        builder.append(monitorId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifInOctets=");
        builder.append(ifInOctets);
        builder.append(", ifInUcastPkts=");
        builder.append(ifInUcastPkts);
        builder.append(", ifInNUcastPkts=");
        builder.append(ifInNUcastPkts);
        builder.append(", ifInDiscards=");
        builder.append(ifInDiscards);
        builder.append(", ifInErrors=");
        builder.append(ifInErrors);
        builder.append(", ifInUnknownProtos=");
        builder.append(ifInUnknownProtos);
        builder.append(", ifOutOctets=");
        builder.append(ifOutOctets);
        builder.append(", ifOutUcastPkts=");
        builder.append(ifOutUcastPkts);
        builder.append(", ifOutNUcastPkts=");
        builder.append(ifOutNUcastPkts);
        builder.append(", ifOutDiscards=");
        builder.append(ifOutDiscards);
        builder.append(", ifOutErrors=");
        builder.append(ifOutErrors);
        builder.append(", ifOutQLen=");
        builder.append(ifOutQLen);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
