/***********************************************************************
 * $Id: CmtsInfoMaintain.java,v1.0 2017年9月12日 下午4:05:34 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmtsInfoSummary.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author admin
 * @created @2017年9月12日-下午4:05:34
 *
 */
public class CmtsInfoMaintain implements AliasesSuperType{
    private static final long serialVersionUID = 5800404906693781176L;

    private Long cmcId;
    private Timestamp collectTime;
    private Double upChannelTxAvg;// 上行电平
    private Double upChannelSnrAvg; // 下行SNR
    private Double downChannelSnrAvg; // 下行SNR
    private Double downChannelTxAvg;// 下行电平
    private Double upChannelTxOutRange;// 上行电平
    private Double upChannelSnrOutRange; // 下行SNR
    private Double downChannelSnrOutRange; // 下行SNR
    private Double downChannelTxOutRange;// 下行电平
    private Double cm_total;// 下行电平
    private Double cm_online;// 下行电平
    
    public CmtsInfoMaintain(){
        super();
    }
    
    public CmtsInfoMaintain(Long cmcId,Timestamp collectTime){
        super();
        this.cmcId=cmcId;
        this.upChannelTxAvg=0d;
        this.upChannelSnrAvg=0d;
        this.downChannelSnrAvg=0d;
        this.downChannelTxAvg=0d;
        this.upChannelTxOutRange=0d;
        this.upChannelSnrOutRange=0d;
        this.downChannelSnrOutRange=0d;
        this.downChannelTxOutRange=0d;
        this.cm_total=0d;
        this.cm_online=0d;
        this.collectTime=collectTime;
    }
    
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Timestamp getCollectTime() {
        return collectTime;
    }
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }
    public Double getUpChannelTxAvg() {
        return upChannelTxAvg;
    }
    public void setUpChannelTxAvg(Double upChannelTxAvg) {
        this.upChannelTxAvg = upChannelTxAvg;
    }
    public Double getUpChannelSnrAvg() {
        return upChannelSnrAvg;
    }
    public void setUpChannelSnrAvg(Double upChannelSnrAvg) {
        this.upChannelSnrAvg = upChannelSnrAvg;
    }
    public Double getDownChannelSnrAvg() {
        return downChannelSnrAvg;
    }
    public void setDownChannelSnrAvg(Double downChannelSnrAvg) {
        this.downChannelSnrAvg = downChannelSnrAvg;
    }
    public Double getDownChannelTxAvg() {
        return downChannelTxAvg;
    }
    public void setDownChannelTxAvg(Double downChannelTxAvg) {
        this.downChannelTxAvg = downChannelTxAvg;
    }
    public Double getUpChannelTxOutRange() {
        return upChannelTxOutRange;
    }
    public void setUpChannelTxOutRange(Double upChannelTxOutRange) {
        this.upChannelTxOutRange = upChannelTxOutRange;
    }
    public Double getUpChannelSnrOutRange() {
        return upChannelSnrOutRange;
    }
    public void setUpChannelSnrOutRange(Double upChannelSnrOutRange) {
        this.upChannelSnrOutRange = upChannelSnrOutRange;
    }
    public Double getDownChannelSnrOutRange() {
        return downChannelSnrOutRange;
    }
    public void setDownChannelSnrOutRange(Double downChannelSnrOutRange) {
        this.downChannelSnrOutRange = downChannelSnrOutRange;
    }
    public Double getDownChannelTxOutRange() {
        return downChannelTxOutRange;
    }
    public void setDownChannelTxOutRange(Double downChannelTxOutRange) {
        this.downChannelTxOutRange = downChannelTxOutRange;
    }
    public Double getCm_total() {
        return cm_total;
    }
    public void setCm_total(Double cm_total) {
        this.cm_total = cm_total;
    }
    public Double getCm_online() {
        return cm_online;
    }
    public void setCm_online(Double cm_online) {
        this.cm_online = cm_online;
    }
}
