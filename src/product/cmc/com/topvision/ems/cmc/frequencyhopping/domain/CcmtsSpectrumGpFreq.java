/***********************************************************************
 * $Id: CcmtsSpectrumGpFreq.java,v1.0 2013-8-2 上午9:30:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2013-8-2-上午9:30:30
 * 
 */
public class CcmtsSpectrumGpFreq implements Serializable {
    private static final long serialVersionUID = -7294732083071326680L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.3.1.1", index = true)
    private Integer groupId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.3.1.2", index = true)
    private Integer freqIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.3.1.3", writable = true, type = "Integer32")
    private Integer freqFrequency;// 中心频点
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.3.1.4", writable = true, type = "Integer32")
    private Integer freqMaxWidth;// 最大可用带宽
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.3.1.5", writable = true, type = "Integer32")
    private Integer freqPower;// 接收电平
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.3.1.6", writable = true, type = "Integer32")
    private Integer freqRowStatus;

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

    public Integer getFreqIndex() {
        return freqIndex;
    }

    public void setFreqIndex(Integer freqIndex) {
        this.freqIndex = freqIndex;
    }

    public Integer getFreqFrequency() {
        return freqFrequency;
    }

    public void setFreqFrequency(Integer freqFrequency) {
        this.freqFrequency = freqFrequency;
    }

    public Integer getFreqMaxWidth() {
        return freqMaxWidth;
    }

    public void setFreqMaxWidth(Integer freqMaxWidth) {
        this.freqMaxWidth = freqMaxWidth;
    }

    public Integer getFreqPower() {
        return freqPower;
    }

    public void setFreqPower(Integer freqPower) {
        this.freqPower = freqPower;
    }

    public Integer getFreqRowStatus() {
        return freqRowStatus;
    }

    public void setFreqRowStatus(Integer freqRowStatus) {
        this.freqRowStatus = freqRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsSpectrumGpFreq [entityId=");
        builder.append(entityId);
        builder.append(", groupId=");
        builder.append(groupId);
        builder.append(", freqIndex=");
        builder.append(freqIndex);
        builder.append(", freqFrequency=");
        builder.append(freqFrequency);
        builder.append(", freqMaxWidth=");
        builder.append(freqMaxWidth);
        builder.append(", freqPower=");
        builder.append(freqPower);
        builder.append(", freqRowStatus=");
        builder.append(freqRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
