/***********************************************************************
 * $Id: TopOltDhcpStatisticsObjects.java,v1.0 2017年11月16日 下午4:10:00 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年11月16日-下午4:10:00
 *
 */
public class TopOltDhcpStatisticsObjects implements AliasesSuperType {
    private static final long serialVersionUID = 759300216509641395L;
    private Long entityId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.1.0")
    private Long topOltDhcpStatRDiscover;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.2.0")
    private Long topOltDhcpStatRRequest;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.3.0")
    private Long topOltDhcpStatROffer;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.4.0")
    private Long topOltDhcpStatRAck;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.5.0")
    private Long topOltDhcpStatROther;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.6.0")
    private Long topOltDhcpStatDDiscover;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.7.0")
    private Long topOltDhcpStatDRequest;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.8.0")
    private Long topOltDhcpStatDOffer;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.9.0")
    private Long topOltDhcpStatDAck;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.10.0")
    private Long topOltDhcpStatDOther;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.11.0")
    private Long topOltDhcpStatDFlood;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.12.0")
    private Long topOltDhcpStatDUnknown;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.13.0")
    private Long topOltDhcpStatDCongestion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.14.0")
    private Long topOltDhcpStatTDiscover;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.15.0")
    private Long topOltDhcpStatTRequest;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.16.0")
    private Long topOltDhcpStatTOffer;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.17.0")
    private Long topOltDhcpStatTAck;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.18.0")
    private Long topOltDhcpStatTOther;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.7.19.0", writable = true, type = "Integer32")
    private Integer topOltDhcpStatStatusAndAction;// clean(2)

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTopOltDhcpStatRDiscover() {
        return topOltDhcpStatRDiscover;
    }

    public void setTopOltDhcpStatRDiscover(Long topOltDhcpStatRDiscover) {
        this.topOltDhcpStatRDiscover = topOltDhcpStatRDiscover;
    }

    public Long getTopOltDhcpStatRRequest() {
        return topOltDhcpStatRRequest;
    }

    public void setTopOltDhcpStatRRequest(Long topOltDhcpStatRRequest) {
        this.topOltDhcpStatRRequest = topOltDhcpStatRRequest;
    }

    public Long getTopOltDhcpStatROffer() {
        return topOltDhcpStatROffer;
    }

    public void setTopOltDhcpStatROffer(Long topOltDhcpStatROffer) {
        this.topOltDhcpStatROffer = topOltDhcpStatROffer;
    }

    public Long getTopOltDhcpStatRAck() {
        return topOltDhcpStatRAck;
    }

    public void setTopOltDhcpStatRAck(Long topOltDhcpStatRAck) {
        this.topOltDhcpStatRAck = topOltDhcpStatRAck;
    }

    public Long getTopOltDhcpStatROther() {
        return topOltDhcpStatROther;
    }

    public void setTopOltDhcpStatROther(Long topOltDhcpStatROther) {
        this.topOltDhcpStatROther = topOltDhcpStatROther;
    }

    public Long getTopOltDhcpStatDDiscover() {
        return topOltDhcpStatDDiscover;
    }

    public void setTopOltDhcpStatDDiscover(Long topOltDhcpStatDDiscover) {
        this.topOltDhcpStatDDiscover = topOltDhcpStatDDiscover;
    }

    public Long getTopOltDhcpStatDRequest() {
        return topOltDhcpStatDRequest;
    }

    public void setTopOltDhcpStatDRequest(Long topOltDhcpStatDRequest) {
        this.topOltDhcpStatDRequest = topOltDhcpStatDRequest;
    }

    public Long getTopOltDhcpStatDOffer() {
        return topOltDhcpStatDOffer;
    }

    public void setTopOltDhcpStatDOffer(Long topOltDhcpStatDOffer) {
        this.topOltDhcpStatDOffer = topOltDhcpStatDOffer;
    }

    public Long getTopOltDhcpStatDAck() {
        return topOltDhcpStatDAck;
    }

    public void setTopOltDhcpStatDAck(Long topOltDhcpStatDAck) {
        this.topOltDhcpStatDAck = topOltDhcpStatDAck;
    }

    public Long getTopOltDhcpStatDOther() {
        return topOltDhcpStatDOther;
    }

    public void setTopOltDhcpStatDOther(Long topOltDhcpStatDOther) {
        this.topOltDhcpStatDOther = topOltDhcpStatDOther;
    }

    public Long getTopOltDhcpStatDFlood() {
        return topOltDhcpStatDFlood;
    }

    public void setTopOltDhcpStatDFlood(Long topOltDhcpStatDFlood) {
        this.topOltDhcpStatDFlood = topOltDhcpStatDFlood;
    }

    public Long getTopOltDhcpStatDUnknown() {
        return topOltDhcpStatDUnknown;
    }

    public void setTopOltDhcpStatDUnknown(Long topOltDhcpStatDUnknown) {
        this.topOltDhcpStatDUnknown = topOltDhcpStatDUnknown;
    }

    public Long getTopOltDhcpStatDCongestion() {
        return topOltDhcpStatDCongestion;
    }

    public void setTopOltDhcpStatDCongestion(Long topOltDhcpStatDCongestion) {
        this.topOltDhcpStatDCongestion = topOltDhcpStatDCongestion;
    }

    public Long getTopOltDhcpStatTDiscover() {
        return topOltDhcpStatTDiscover;
    }

    public void setTopOltDhcpStatTDiscover(Long topOltDhcpStatTDiscover) {
        this.topOltDhcpStatTDiscover = topOltDhcpStatTDiscover;
    }

    public Long getTopOltDhcpStatTRequest() {
        return topOltDhcpStatTRequest;
    }

    public void setTopOltDhcpStatTRequest(Long topOltDhcpStatTRequest) {
        this.topOltDhcpStatTRequest = topOltDhcpStatTRequest;
    }

    public Long getTopOltDhcpStatTOffer() {
        return topOltDhcpStatTOffer;
    }

    public void setTopOltDhcpStatTOffer(Long topOltDhcpStatTOffer) {
        this.topOltDhcpStatTOffer = topOltDhcpStatTOffer;
    }

    public Long getTopOltDhcpStatTAck() {
        return topOltDhcpStatTAck;
    }

    public void setTopOltDhcpStatTAck(Long topOltDhcpStatTAck) {
        this.topOltDhcpStatTAck = topOltDhcpStatTAck;
    }

    public Long getTopOltDhcpStatTOther() {
        return topOltDhcpStatTOther;
    }

    public void setTopOltDhcpStatTOther(Long topOltDhcpStatTOther) {
        this.topOltDhcpStatTOther = topOltDhcpStatTOther;
    }

    public Integer getTopOltDhcpStatStatusAndAction() {
        return topOltDhcpStatStatusAndAction;
    }

    public void setTopOltDhcpStatStatusAndAction(Integer topOltDhcpStatStatusAndAction) {
        this.topOltDhcpStatStatusAndAction = topOltDhcpStatStatusAndAction;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltDhcpStatisticsObjects [entityId=");
        builder.append(entityId);
        builder.append(", topOltDhcpStatRDiscover=");
        builder.append(topOltDhcpStatRDiscover);
        builder.append(", topOltDhcpStatRRequest=");
        builder.append(topOltDhcpStatRRequest);
        builder.append(", topOltDhcpStatROffer=");
        builder.append(topOltDhcpStatROffer);
        builder.append(", topOltDhcpStatRAck=");
        builder.append(topOltDhcpStatRAck);
        builder.append(", topOltDhcpStatROther=");
        builder.append(topOltDhcpStatROther);
        builder.append(", topOltDhcpStatDDiscover=");
        builder.append(topOltDhcpStatDDiscover);
        builder.append(", topOltDhcpStatDRequest=");
        builder.append(topOltDhcpStatDRequest);
        builder.append(", topOltDhcpStatDOffer=");
        builder.append(topOltDhcpStatDOffer);
        builder.append(", topOltDhcpStatDAck=");
        builder.append(topOltDhcpStatDAck);
        builder.append(", topOltDhcpStatDOther=");
        builder.append(topOltDhcpStatDOther);
        builder.append(", topOltDhcpStatDFlood=");
        builder.append(topOltDhcpStatDFlood);
        builder.append(", topOltDhcpStatDUnknown=");
        builder.append(topOltDhcpStatDUnknown);
        builder.append(", topOltDhcpStatDCongestion=");
        builder.append(topOltDhcpStatDCongestion);
        builder.append(", topOltDhcpStatTDiscover=");
        builder.append(topOltDhcpStatTDiscover);
        builder.append(", topOltDhcpStatTRequest=");
        builder.append(topOltDhcpStatTRequest);
        builder.append(", topOltDhcpStatTOffer=");
        builder.append(topOltDhcpStatTOffer);
        builder.append(", topOltDhcpStatTAck=");
        builder.append(topOltDhcpStatTAck);
        builder.append(", topOltDhcpStatTOther=");
        builder.append(topOltDhcpStatTOther);
        builder.append(", topOltDhcpStatStatusAndAction=");
        builder.append(topOltDhcpStatStatusAndAction);
        builder.append("]");
        return builder.toString();
    }

}
