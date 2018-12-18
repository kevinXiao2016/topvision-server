/***********************************************************************
 * $Id: CmcUserNumHisPerf.java,v1.0 2013-6-22 上午11:46:16 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.sql.Timestamp;

/**
 * @author loyal
 * @created @2013-6-22-上午11:46:16
 *
 */
public class CmcUserNumHisPerf {
    private Long cmcId;
    private Long onlineNum;
    private Long otherNum;
    private Long offlineNum;
    private Long interactiveNum;
    private Long broadbandNum;
    private Long cpeInteractiveNum;
    private Long cpeBroadbandNum;
    private Long cpeMtaNum;
    private Long cpeNum;
    private Timestamp collectTime;
    private Long mtaNum;
    private Long integratedNum;
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Long getOnlineNum() {
        return onlineNum;
    }
    public void setOnlineNum(Long onlineNum) {
        this.onlineNum = onlineNum;
    }
    public Long getOtherNum() {
        return otherNum;
    }
    public void setOtherNum(Long otherNum) {
        this.otherNum = otherNum;
    }
    public Long getOfflineNum() {
        return offlineNum;
    }
    public void setOfflineNum(Long offlineNum) {
        this.offlineNum = offlineNum;
    }
    public Long getInteractiveNum() {
        return interactiveNum;
    }
    public void setInteractiveNum(Long interactiveNum) {
        this.interactiveNum = interactiveNum;
    }
    public Long getBroadbandNum() {
        return broadbandNum;
    }
    public void setBroadbandNum(Long broadbandNum) {
        this.broadbandNum = broadbandNum;
    }
    public Long getCpeNum() {
        return cpeNum;
    }
    public void setCpeNum(Long cpeNum) {
        this.cpeNum = cpeNum;
    }
    public Timestamp getCollectTime() {
        return collectTime;
    }
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }
    public Long getMtaNum() {
        return mtaNum;
    }
    public void setMtaNum(Long mtaNum) {
        this.mtaNum = mtaNum;
    }
    public Long getIntegratedNum() {
        return integratedNum;
    }
    public void setIntegratedNum(Long integratedNum) {
        this.integratedNum = integratedNum;
    }
    public Long getCpeInteractiveNum() { return cpeInteractiveNum; }
    public void setCpeInteractiveNum(Long cpeInteractiveNum) { this.cpeInteractiveNum = cpeInteractiveNum; }
    public Long getCpeBroadbandNum() { return cpeBroadbandNum; }
    public void setCpeBroadbandNum(Long cpeBroadbandNum) { this.cpeBroadbandNum = cpeBroadbandNum; }
    public Long getCpeMtaNum() { return cpeMtaNum; }
    public void setCpeMtaNum(Long cpeMtaNum) { this.cpeMtaNum = cpeMtaNum; }
}
