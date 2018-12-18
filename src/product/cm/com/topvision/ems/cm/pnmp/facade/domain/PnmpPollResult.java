package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author jay
 * @created 15-3-3.
 */

@Alias("pnmpPollResult")
public class PnmpPollResult implements Serializable,AliasesSuperType {
    private static final long serialVersionUID = 7745408685847560316L;

    //checkStatus
    public static final Integer CHECKSUCCESS = 0;
    public static final Integer CHECKPINGFALSE = 1;
    public static final Integer CHECKSNMPFALSE = 2;

    private Long resultId;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmIndex;
    private String cmMac;
    private String cmIp;
    private Integer statusValue;
    private Integer checkStatus; // 0 CHECKSUCCESS,1 CHECKPINGFALSE,2 CHECKSNMPFALSE,
    private Integer upChannelId;//上行信道ID
    private Long upChannelFreq;//上行信道中心频率
    private Long upChannelWidth;//上行信道带宽
    private Integer upSnr;
    private Integer upTxPower;
    private Integer downSnr;
    private Integer downRxPower;
    private String equalizationData;
    private Long collectTime;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public String getEqualizationData() {
        return equalizationData;
    }

    public void setEqualizationData(String equalizationData) {
        this.equalizationData = equalizationData;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public void setPingCheck(boolean pingCheck) {
        if (!pingCheck) {
            checkStatus = CHECKPINGFALSE;
        }
    }

    public void setSnmpCheck(boolean snmpCheck) {
        if (!snmpCheck) {
            checkStatus = CHECKSNMPFALSE;
        }
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public Long getUpChannelWidth() {
        return upChannelWidth;
    }

    public void setUpChannelWidth(Long upChannelWidth) {
        this.upChannelWidth = upChannelWidth;
    }

    public Integer getUpSnr() {
        return upSnr;
    }

    public void setUpSnr(Integer upSnr) {
        if (upSnr != null && upSnr != Integer.MIN_VALUE) {
            this.upSnr = upSnr;
        } else {
            this.upSnr = null;
        }
    }

    public Integer getUpTxPower() {
        return upTxPower;
    }

    public void setUpTxPower(Integer upTxPower) {
        if (upTxPower != null && upTxPower != Integer.MIN_VALUE) {
            this.upTxPower = upTxPower;
        } else {
            this.upTxPower = null;
        }
    }

    public Integer getDownSnr() {
        return downSnr;
    }

    public void setDownSnr(Integer downSnr) {
        if (downSnr != null && downSnr != Integer.MIN_VALUE) {
            this.downSnr = downSnr;
        } else {
            this.downSnr = null;
        }
    }

    public Integer getDownRxPower() {
        return downRxPower;
    }

    public void setDownRxPower(Integer downRxPower) {
        if (downRxPower != null && downRxPower != Integer.MIN_VALUE) {
            this.downRxPower = downRxPower;
        } else {
            this.downRxPower = null;
        }
    }

    public Integer getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Integer upChannelId) {
        this.upChannelId = upChannelId;
    }

    public Long getUpChannelFreq() {
        return upChannelFreq;
    }

    public void setUpChannelFreq(Long upChannelFreq) {
        this.upChannelFreq = upChannelFreq;
    }

    @Override
    public String toString() {
        return "PnmpPollResult{" +
                "resultId=" + resultId +
                ", entityId=" + entityId +
                ", cmcId=" + cmcId +
                ", cmcIndex=" + cmcIndex +
                ", cmIndex=" + cmIndex +
                ", cmMac='" + cmMac + '\'' +
                ", cmIp='" + cmIp + '\'' +
                ", statusValue=" + statusValue +
                ", checkStatus=" + checkStatus +
                ", upChannelWidth=" + upChannelWidth +
                ", equalizationData='" + equalizationData + '\'' +
                ", collectTime=" + collectTime +
                '}';
    }
}
