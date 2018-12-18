/***********************************************************************
 * $Id: CcmtsSpectrumGpHopHis.java,v1.0 2013-8-2 上午9:59:31 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2013-8-2-上午9:59:31
 * 
 */
public class CcmtsSpectrumGpHopHis implements Serializable {
    private static final long serialVersionUID = 879123997533227296L;

    private Long entityId;
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.1", index = true)
    private String cmcMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.2", index = true)
    private Integer chnlId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.3", index = true)
    private Integer hisIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.4", writable = false, type = "Integer32")
    private Integer hisSelect1st;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.5", writable = false, type = "Integer32")
    private Integer hisSelect2st;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.6", writable = false, type = "Integer32")
    private Integer hisSelect3st;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.7", writable = false, type = "Integer32")
    private Integer hisPolicy;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.8", writable = false, type = "Integer32")
    private Integer hisGroupId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.9", writable = false, type = "Integer32")
    private Integer hisMaxHop;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.14", writable = false, type = "Integer32")
    private Integer hisFrequency;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.15", writable = false, type = "Integer32")
    private Integer hisWidth;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.17", writable = false, type = "Integer32")
    private Integer hisPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.18", writable = false, type = "Integer32")
    private Integer hisSnr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.20", writable = false, type = "Integer32")
    private Integer hisCorrect;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.21", writable = false, type = "Integer32")
    private Integer hisUnCorrect;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.6.1.30", writable = false, type = "Integer32")
    private Long lastHopTimeYMD;

    private String lastHopTime;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    public Integer getChnlId() {
        return chnlId;
    }

    public void setChnlId(Integer chnlId) {
        this.chnlId = chnlId;
    }

    public Integer getHisIndex() {
        return hisIndex;
    }

    public void setHisIndex(Integer hisIndex) {
        this.hisIndex = hisIndex;
    }

    public Integer getHisSelect1st() {
        return hisSelect1st;
    }

    public void setHisSelect1st(Integer hisSelect1st) {
        this.hisSelect1st = hisSelect1st;
    }

    public Integer getHisSelect2st() {
        return hisSelect2st;
    }

    public void setHisSelect2st(Integer hisSelect2st) {
        this.hisSelect2st = hisSelect2st;
    }

    public Integer getHisSelect3st() {
        return hisSelect3st;
    }

    public void setHisSelect3st(Integer hisSelect3st) {
        this.hisSelect3st = hisSelect3st;
    }

    public Integer getHisPolicy() {
        return hisPolicy;
    }

    public void setHisPolicy(Integer hisPolicy) {
        this.hisPolicy = hisPolicy;
    }

    public Integer getHisGroupId() {
        return hisGroupId;
    }

    public void setHisGroupId(Integer hisGroupId) {
        this.hisGroupId = hisGroupId;
    }

    public Integer getHisMaxHop() {
        return hisMaxHop;
    }

    public void setHisMaxHop(Integer hisMaxHop) {
        this.hisMaxHop = hisMaxHop;
    }

    public Integer getHisFrequency() {
        return hisFrequency;
    }

    public void setHisFrequency(Integer hisFrequency) {
        this.hisFrequency = hisFrequency;
    }

    public Integer getHisWidth() {
        return hisWidth;
    }

    public void setHisWidth(Integer hisWidth) {
        this.hisWidth = hisWidth;
    }

    public Integer getHisPower() {
        return hisPower;
    }

    public void setHisPower(Integer hisPower) {
        this.hisPower = hisPower;
    }

    public Integer getHisSnr() {
        return hisSnr;
    }

    public void setHisSnr(Integer hisSnr) {
        this.hisSnr = hisSnr;
    }

    public Integer getHisCorrect() {
        return hisCorrect;
    }

    public void setHisCorrect(Integer hisCorrect) {
        this.hisCorrect = hisCorrect;
    }

    public Integer getHisUnCorrect() {
        return hisUnCorrect;
    }

    public void setHisUnCorrect(Integer hisUnCorrect) {
        this.hisUnCorrect = hisUnCorrect;
    }

    public Long getLastHopTimeYMD() {
        return lastHopTimeYMD;
    }

    public void setLastHopTimeYMD(Long lastHopTimeYMD) {
        this.lastHopTimeYMD = lastHopTimeYMD;
    }

    public String getLastHopTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lastTime = new Date(lastHopTimeYMD);
        return dateFormat.format(lastTime);
    }

    public void setLastHopTime(String lastHopTime) {
        this.lastHopTime = lastHopTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsSpectrumGpHopHis [entityId=");
        builder.append(entityId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", cmcMac=");
        builder.append(cmcMac);
        builder.append(", chnlId=");
        builder.append(chnlId);
        builder.append(", hisIndex=");
        builder.append(hisIndex);
        builder.append(", hisSelect1st=");
        builder.append(hisSelect1st);
        builder.append(", hisSelect2st=");
        builder.append(hisSelect2st);
        builder.append(", hisSelect3st=");
        builder.append(hisSelect3st);
        builder.append(", hisPolicy=");
        builder.append(hisPolicy);
        builder.append(", hisGroupId=");
        builder.append(hisGroupId);
        builder.append(", hisMaxHop=");
        builder.append(hisMaxHop);
        builder.append(", hisFrequency=");
        builder.append(hisFrequency);
        builder.append(", hisWidth=");
        builder.append(hisWidth);
        builder.append(", hisPower=");
        builder.append(hisPower);
        builder.append(", hisSnr=");
        builder.append(hisSnr);
        builder.append(", hisCorrect=");
        builder.append(hisCorrect);
        builder.append(", hisUnCorrect=");
        builder.append(hisUnCorrect);
        builder.append(", lastHopTimeYMD=");
        builder.append(lastHopTimeYMD);
        builder.append(", lastHopTime=");
        builder.append(lastHopTime);
        builder.append("]");
        return builder.toString();
    }
}
