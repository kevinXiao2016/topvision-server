package com.topvision.ems.cm.cmpoll.facade.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created 15-3-3.
 */
public class CmPollResult implements Serializable {
    private static final long serialVersionUID = 7745408685847560316L;
    public static final String NULL = "null";
    //REMOTEQUERY
    public static final Integer REMOTEQUERYSTARTED = 1;

    //checkStatus
    public static final Integer CHECKSUCCESS = 0;
    public static final Integer CHECKPINGFALSE = 1;
    public static final Integer CHECKSNMPFALSE = 2;
    public static final Integer CHECKREMOTEQUERYFALSE = 3;

    private Long resultId;
    private Long entityId;
    private Long cmcId;
    private Long cmId;
    private Long cmcIndex;
    private Long cmIndex;
    private String cmMac;
    private String cmIp;
    private String upChIds; //1,2,3
    private String upChRxPowers; //10,null,30
    private String upChTxPowers; //10,null,30
    private String upChSnrs;
    private String downChIds;
    private String downChRxPowers;
    private String downChSnrs;
    private Integer statusValue;
    private Integer checkStatus; // 0 CHECKSUCCESS,1 CHECKPINGFALSE,2 CHECKSNMPFALSE,3
                                 // CHECKREMOTEQUERYFALSE
    private String upChFreqs;
    private String downChFreqs;
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

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public String getUpChIds() {
        return upChIds;
    }

    public void setUpChIds(String upChIds) {
        this.upChIds = upChIds;
    }

    public String getDownChIds() {
        return downChIds;
    }

    public void setDownChIds(String downChIds) {
        this.downChIds = downChIds;
    }

    public String getUpChRxPowers() {
        return upChRxPowers;
    }

    public void setUpChRxPowers(String upChRxPowers) {
        this.upChRxPowers = upChRxPowers;
    }

    public String getUpChTxPowers() {
        return upChTxPowers;
    }

    public void setUpChTxPowers(String upChTxPowers) {
        this.upChTxPowers = upChTxPowers;
    }

    public String getUpChSnrs() {
        return upChSnrs;
    }

    public void setUpChSnrs(String upChSnrs) {
        this.upChSnrs = upChSnrs;
    }

    public String getDownChRxPowers() {
        return downChRxPowers;
    }

    public void setDownChRxPowers(String downChRxPowers) {
        this.downChRxPowers = downChRxPowers;
    }

    public String getDownChSnrs() {
        return downChSnrs;
    }

    public void setDownChSnrs(String downChSnrs) {
        this.downChSnrs = downChSnrs;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    @Override
    public String toString() {
        return "CmPollResult{" + "resultId=" + resultId + ", entityId=" + entityId + ", cmcId=" + cmcId + ", cmId="
                + cmId + ", cmcIndex=" + cmcIndex + ", cmIndex=" + cmIndex + ", cmMac='" + cmMac + '\'' + ", cmIp='"
                + cmIp + '\'' + ", upChIds='" + upChIds + '\'' + ", upChRxPowers='" + upChRxPowers + '\''
                + ", upChTxPowers='" + upChTxPowers + '\'' + ", upChSnrs='" + upChSnrs + '\'' + ", downChIds='"
                + downChIds + '\'' + ", downChRxPowers='" + downChRxPowers + '\'' + ", downChSnrs='" + downChSnrs
                + '\'' + ", statusValue=" + statusValue + ", checkStatus=" + checkStatus + ", upChFreqs='" + upChFreqs
                + '\'' + ", downChFreqs='" + downChFreqs + '\'' + ", collectTime=" + collectTime + '}';
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

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setCmRemoteQueryStatus(Integer cmRemoteQueryStatus) {
        if (!cmRemoteQueryStatus.equals(REMOTEQUERYSTARTED)) {
            checkStatus = CHECKREMOTEQUERYFALSE;
        }
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public void setUpChFreqs(String upChFreqs) {
        this.upChFreqs = upChFreqs;
    }

    public String getUpChFreqs() {
        return upChFreqs;
    }

    public void setDownChFreqs(String downChFreqs) {
        this.downChFreqs = downChFreqs;
    }

    public String getDownChFreqs() {
        return downChFreqs;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }
}
