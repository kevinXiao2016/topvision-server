/***********************************************************************
 * $Id: PnmpCmtsReport.java,v1.0 2017年8月8日 下午2:08:26 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午2:08:26
 *
 */
public class PnmpCmtsReport implements AliasesSuperType {

    private static final long serialVersionUID = 4108056655295801768L;
    private Long cmcId;
    private Long cmcIndex;
    private String cmcName;
    private String cmcMac;
    private String entityIp;
    private String entityName;
    private Integer totalCmNum;
    private Integer onlineCmNum;
    private Integer offlineCmNum;
    private Integer healthCmNum;
    private Integer marginalCmNum;
    private Integer badCmNum;
    private Timestamp updateTime;
    private String updateTimeString;

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

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getTotalCmNum() {
        return totalCmNum;
    }

    public void setTotalCmNum(Integer totalCmNum) {
        this.totalCmNum = totalCmNum;
    }

    public Integer getOnlineCmNum() {
        return onlineCmNum;
    }

    public void setOnlineCmNum(Integer onlineCmNum) {
        this.onlineCmNum = onlineCmNum;
    }

    public Integer getOfflineCmNum() {
        return offlineCmNum;
    }

    public void setOfflineCmNum(Integer offlineCmNum) {
        this.offlineCmNum = offlineCmNum;
    }

    public Integer getHealthCmNum() {
        return healthCmNum;
    }

    public void setHealthCmNum(Integer healthCmNum) {
        this.healthCmNum = healthCmNum;
    }

    public Integer getMarginalCmNum() {
        return marginalCmNum;
    }

    public void setMarginalCmNum(Integer marginalCmNum) {
        this.marginalCmNum = marginalCmNum;
    }

    public Integer getBadCmNum() {
        return badCmNum;
    }

    public void setBadCmNum(Integer badCmNum) {
        this.badCmNum = badCmNum;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    public String getUpdateTimeString() {
        if (updateTimeString != null && updateTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            updateTimeString = sdf.format(updateTime);
        }
        return updateTimeString;
    }

    public void setUpdateTimeString(String updateTimeString) {
        this.updateTimeString = updateTimeString;
    }
 
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PnmpCmtsReport [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", cmcMac=");
        builder.append(cmcMac);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", totalCmNum=");
        builder.append(totalCmNum);
        builder.append(", onlineCmNum=");
        builder.append(onlineCmNum);
        builder.append(", offlineCmNum=");
        builder.append(offlineCmNum);
        builder.append(", healthCmNum=");
        builder.append(healthCmNum);
        builder.append(", marginalCmNum=");
        builder.append(marginalCmNum);
        builder.append(", badCmNum=");
        builder.append(badCmNum);
        builder.append(", updateTime=");
        builder.append(updateTime);
        builder.append("]");
        return builder.toString();
    }
}
