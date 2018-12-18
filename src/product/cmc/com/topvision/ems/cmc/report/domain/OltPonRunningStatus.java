/***********************************************************************
 * $Id: OltPonRunningStatus.java,v1.0 2013-9-12 下午2:18:48 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.sql.Timestamp;
import java.text.NumberFormat;

import com.topvision.ems.report.util.ReportUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2013-9-12-下午2:18:48
 * 
 */
public class OltPonRunningStatus implements AliasesSuperType {
    private static final long serialVersionUID = -6112811657478974698L;

    private Long entityId;
    // PON口
    private Long ponId;
    private Long ponIndex;
    private String ponName;
    // PON口利用率峰值
    private String ponUsage;
    // CC数量
    private Long ccNum;
    // CM数量
    private Long cmNum;

    private Timestamp collectTime;

    private String oltName;
    private String sniUsageString;

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public String getPonName() {
        ponName = "";
        if (this.ponIndex != null) {
            ponName = ReportUtils.getPonIndexStr(ponIndex);
        }
        return ponName;
    }

    public void setPonName(String ponName) {
        this.ponName = ponName;
    }

    public String getPonUsage() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(Double.parseDouble(this.ponUsage));
    }

    public void setPonUsage(String ponUsage) {
        this.ponUsage = ponUsage;
    }

    public Long getCcNum() {
        return ccNum;
    }

    public void setCcNum(Long ccNum) {
        this.ccNum = ccNum;
    }

    public Long getCmNum() {
        return cmNum;
    }

    public void setCmNum(Long cmNum) {
        this.cmNum = cmNum;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public String getSniUsageString() {
        return sniUsageString;
    }

    public void setSniUsageString(String sniUsageString) {
        this.sniUsageString = sniUsageString;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

}
