/***********************************************************************
 * $Id: EmsCcmtsSpectrumGp.java,v1.0 2013-8-2 上午11:14:12 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author haojie
 * @created @2013-8-2-上午11:14:12
 * 
 */
public class EmsCcmtsSpectrumGp implements Serializable {
    private static final long serialVersionUID = -7329330502348779963L;

    private Long emsGroupId;
    private String emsGroupName;
    private Integer hopPeriod;
    private Integer snrThres1;
    private Integer snrThres2;
    private Integer fecThresCorrect1;
    private Integer fecThresCorrect2;
    private Integer fecThresUnCorrect1;
    private Integer fecThresUnCorrect2;
    private Integer adminStatus;
    private Integer maxHopLimit;
    private Integer groupPolicy;
    private Integer groupPriority1st;
    private Integer groupPriority2st;
    private Integer groupPriority3st;
    private Integer deviceGroupId;

    private List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList;

    public Long getEmsGroupId() {
        return emsGroupId;
    }

    public void setEmsGroupId(Long emsGroupId) {
        this.emsGroupId = emsGroupId;
    }

    public String getEmsGroupName() {
        return emsGroupName;
    }

    public void setEmsGroupName(String emsGroupName) {
        this.emsGroupName = emsGroupName;
    }

    public Integer getHopPeriod() {
        return hopPeriod;
    }

    public void setHopPeriod(Integer hopPeriod) {
        this.hopPeriod = hopPeriod;
    }

    public Integer getSnrThres1() {
        return snrThres1;
    }

    public void setSnrThres1(Integer snrThres1) {
        this.snrThres1 = snrThres1;
    }

    public Integer getSnrThres2() {
        return snrThres2;
    }

    public void setSnrThres2(Integer snrThres2) {
        this.snrThres2 = snrThres2;
    }

    public Integer getFecThresCorrect1() {
        return fecThresCorrect1;
    }

    public void setFecThresCorrect1(Integer fecThresCorrect1) {
        this.fecThresCorrect1 = fecThresCorrect1;
    }

    public Integer getFecThresCorrect2() {
        return fecThresCorrect2;
    }

    public void setFecThresCorrect2(Integer fecThresCorrect2) {
        this.fecThresCorrect2 = fecThresCorrect2;
    }

    public Integer getFecThresUnCorrect1() {
        return fecThresUnCorrect1;
    }

    public void setFecThresUnCorrect1(Integer fecThresUnCorrect1) {
        this.fecThresUnCorrect1 = fecThresUnCorrect1;
    }

    public Integer getFecThresUnCorrect2() {
        return fecThresUnCorrect2;
    }

    public void setFecThresUnCorrect2(Integer fecThresUnCorrect2) {
        this.fecThresUnCorrect2 = fecThresUnCorrect2;
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    public Integer getMaxHopLimit() {
        return maxHopLimit;
    }

    public void setMaxHopLimit(Integer maxHopLimit) {
        this.maxHopLimit = maxHopLimit;
    }

    public Integer getGroupPolicy() {
        return groupPolicy;
    }

    public void setGroupPolicy(Integer groupPolicy) {
        this.groupPolicy = groupPolicy;
    }

    public Integer getGroupPriority1st() {
        return groupPriority1st;
    }

    public void setGroupPriority1st(Integer groupPriority1st) {
        this.groupPriority1st = groupPriority1st;
    }

    public Integer getGroupPriority2st() {
        return groupPriority2st;
    }

    public void setGroupPriority2st(Integer groupPriority2st) {
        this.groupPriority2st = groupPriority2st;
    }

    public Integer getGroupPriority3st() {
        return groupPriority3st;
    }

    public void setGroupPriority3st(Integer groupPriority3st) {
        this.groupPriority3st = groupPriority3st;
    }

    public List<EmsCcmtsSpectrumGpFreq> getEmsCcmtsSpectrumGpFreqList() {
        return emsCcmtsSpectrumGpFreqList;
    }

    public void setEmsCcmtsSpectrumGpFreqList(List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList) {
        this.emsCcmtsSpectrumGpFreqList = emsCcmtsSpectrumGpFreqList;
    }

    public Integer getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(Integer deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EmsCcmtsSpectrumGp [emsGroupId=");
        builder.append(emsGroupId);
        builder.append(", deviceGroupId=");
        builder.append(deviceGroupId);
        builder.append(", emsGroupName=");
        builder.append(emsGroupName);
        builder.append(", hopPeriod=");
        builder.append(hopPeriod);
        builder.append(", snrThres1=");
        builder.append(snrThres1);
        builder.append(", snrThres2=");
        builder.append(snrThres2);
        builder.append(", fecThresCorrect1=");
        builder.append(fecThresCorrect1);
        builder.append(", fecThresCorrect2=");
        builder.append(fecThresCorrect2);
        builder.append(", fecThresUnCorrect1=");
        builder.append(fecThresUnCorrect1);
        builder.append(", fecThresUnCorrect2=");
        builder.append(fecThresUnCorrect2);
        builder.append(", adminStatus=");
        builder.append(adminStatus);
        builder.append(", maxHopLimit=");
        builder.append(maxHopLimit);
        builder.append(", groupPolicy=");
        builder.append(groupPolicy);
        builder.append(", groupPriority1st=");
        builder.append(groupPriority1st);
        builder.append(", groupPriority2st=");
        builder.append(groupPriority2st);
        builder.append(", groupPriority3st=");
        builder.append(groupPriority3st);
        builder.append(", emsCcmtsSpectrumGpFreqList=");
        builder.append(emsCcmtsSpectrumGpFreqList);
        builder.append("]");
        return builder.toString();
    }

}
