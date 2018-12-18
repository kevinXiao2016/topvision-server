/***********************************************************************
 * $Id: CcmtsSpectrumGp.java,v1.0 2013-8-2 上午9:19:13 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2013-8-2-上午9:19:13
 * 
 */
public class CcmtsSpectrumGp implements Serializable {
    private static final long serialVersionUID = -7217977196045228784L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.1", index = true)
    private Integer groupId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.2", writable = true, type = "Integer32")
    private Integer hopPeriod;// HOP间隔
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.3", writable = true, type = "Integer32")
    private Integer snrThres1;// SNR第一阈值
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.4", writable = true, type = "Integer32")
    private Integer snrThres2;// SNR第二阈值
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.5", writable = true, type = "Integer32")
    private Integer fecThresCorrect1;// 可纠错码第一阈值
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.6", writable = true, type = "Integer32")
    private Integer fecThresCorrect2;// 可纠错码第二阈值
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.7", writable = true, type = "Integer32")
    private Integer fecThresUnCorrect1;// 不可纠错码第一阈值
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.8", writable = true, type = "Integer32")
    private Integer fecThresUnCorrect2;// 不可纠错码第二阈值
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.10", writable = true, type = "Integer32")
    private Integer adminStatus;// 跳频开关，1开2关
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.19", writable = true, type = "Integer32")
    private Integer maxHopLimit;// 最大HOP次数
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.18", writable = true, type = "Integer32")
    private Integer groupPolicy;// 跳频策略
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.15", writable = true, type = "Integer32")
    private Integer groupPriority1st;// 第一优先级
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.16", writable = true, type = "Integer32")
    private Integer groupPriority2st;// 第二优先级
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.17", writable = true, type = "Integer32")
    private Integer groupPriority3st;// 第三优先级
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.2.1.20", writable = true, type = "Integer32")
    private Integer groupRowStatus;

    private List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public Integer getGroupRowStatus() {
        return groupRowStatus;
    }

    public void setGroupRowStatus(Integer groupRowStatus) {
        this.groupRowStatus = groupRowStatus;
    }

    public List<CcmtsSpectrumGpFreq> getCcmtsSpectrumGpFreqList() {
        return ccmtsSpectrumGpFreqList;
    }

    public void setCcmtsSpectrumGpFreqList(List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList) {
        this.ccmtsSpectrumGpFreqList = ccmtsSpectrumGpFreqList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsSpectrumGp [entityId=");
        builder.append(entityId);
        builder.append(", groupId=");
        builder.append(groupId);
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
        builder.append(", groupRowStatus=");
        builder.append(groupRowStatus);
        builder.append(", ccmtsSpectrumGpFreqList=");
        builder.append(ccmtsSpectrumGpFreqList);
        builder.append("]");
        return builder.toString();
    }

}
