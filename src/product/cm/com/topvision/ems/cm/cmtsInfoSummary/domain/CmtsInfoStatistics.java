package com.topvision.ems.cm.cmtsInfoSummary.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmtsInfoStatistics implements AliasesSuperType {
    private static final long serialVersionUID = 9021351466219447634L;
    
    private Long cmcId;
    private Integer onlineCmNum;
    private Integer totalCmNum;
    private Double upSnrAvg;
    private Double upSnrOutRange;
    private Double downSnrAvg;
    private Double downSnrOutRange;
    private Double upTxAvg;
    private Double upTxOutRange;
    private Double downReAvg;
    private Double downReOutRange;
    private Timestamp collectTime;
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Double getUpSnrOutRange() {
        return upSnrOutRange;
    }
    public void setUpSnrOutRange(Double upSnrOutRange) {
        this.upSnrOutRange = upSnrOutRange;
    }
    public Double getDownSnrOutRange() {
        return downSnrOutRange;
    }
    public void setDownSnrOutRange(Double downSnrOutRange) {
        this.downSnrOutRange = downSnrOutRange;
    }
    public Double getUpTxOutRange() {
        return upTxOutRange;
    }
    public void setUpTxOutRange(Double upTxOutRange) {
        this.upTxOutRange = upTxOutRange;
    }
    public Double getDownReOutRange() {
        return downReOutRange;
    }
    public void setDownReOutRange(Double downReOutRange) {
        this.downReOutRange = downReOutRange;
    }
    public Timestamp getCollectTime() {
        return collectTime;
    }
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }
    public Double getUpSnrAvg() {
        return upSnrAvg;
    }
    public void setUpSnrAvg(Double upSnrAvg) {
        this.upSnrAvg = upSnrAvg;
    }
    public Double getDownSnrAvg() {
        return downSnrAvg;
    }
    public void setDownSnrAvg(Double downSnrAvg) {
        this.downSnrAvg = downSnrAvg;
    }
    public Double getUpTxAvg() {
        return upTxAvg;
    }
    public void setUpTxAvg(Double upTxAvg) {
        this.upTxAvg = upTxAvg;
    }
    public Double getDownReAvg() {
        return downReAvg;
    }
    public void setDownReAvg(Double downReAvg) {
        this.downReAvg = downReAvg;
    }
    public Integer getOnlineCmNum() {
        return onlineCmNum;
    }
    public void setOnlineCmNum(Integer onlineCmNum) {
        this.onlineCmNum = onlineCmNum;
    }
    public Integer getTotalCmNum() {
        return totalCmNum;
    }
    public void setTotalCmNum(Integer totalCmNum) {
        this.totalCmNum = totalCmNum;
    }

}
