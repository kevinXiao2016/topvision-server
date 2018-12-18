/***********************************************************************
 * $Id: OltRunningStatus.java,v1.0 2013-9-12 下午1:51:12 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.text.NumberFormat;
import java.util.List;

import com.topvision.ems.report.util.ReportUtils;

/**
 * @author haojie
 * @created @2013-9-12-下午1:51:12
 * 
 */
public class OltRunningStatus {
    // OLT
    private Long entityId;
    private String oltName;
    // SNI
    private String sniPortName;
    private Float sniUsage;
    private Long sniIndex;
    private String sniUsageString;
    // PON
    private List<OltPonRunningStatus> OltPonRunningStatusList;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public String getSniPortName() {
        return sniPortName;
    }

    public void setSniPortName(String sniPortName) {
        this.sniPortName = sniPortName;
    }

    public Float getSniUsage() {
        return sniUsage;
    }

    public void setSniUsage(Float sniUsage) {
        this.sniUsage = sniUsage;
    }

    public List<OltPonRunningStatus> getOltPonRunningStatusList() {
        return OltPonRunningStatusList;
    }

    public void setOltPonRunningStatusList(List<OltPonRunningStatus> oltPonRunningStatusList) {
        OltPonRunningStatusList = oltPonRunningStatusList;
    }

    public String getSniUsageString() {
        if (this.sniIndex != null) {
            this.sniPortName = ReportUtils.getPonIndexStr(this.sniIndex);
        }
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        sniUsageString = "";
        if (this.sniUsage != null && this.sniPortName != null) {
            sniUsageString = nf.format(this.sniUsage) + "[" + this.sniPortName + "]";
        }
        return sniUsageString;
    }

    public void setSniUsageString(String sniUsageString) {
        this.sniUsageString = sniUsageString;
    }

    public Long getSniIndex() {
        return sniIndex;
    }

    public void setSniIndex(Long sniIndex) {
        this.sniIndex = sniIndex;
    }

}
