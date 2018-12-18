/***********************************************************************
 * $Id: CmDailyNumStaticReport.java,v1.0 2013-9-13 下午3:03:15 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.sql.Date;

/**
 * @author fanzidong
 * @created @2013-9-13-下午3:03:15
 * 
 */
public class CmDailyNumStaticReport {
    public static final String TOPOFOLDER = "topofolder";
    public static final String ENTITY = "entity";
    public static final String RANGE_FOLDERID = "folderId";
    public static final String RANGE_OLTID = "oltId";
    public static final String RANGE_CMCID = "cmcId";
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final String OTHERSTATUS = "otherStatus";
    public static final String ALLSTATUS = "allStatus";

    private Long cmcId;
    private Integer cmNumOnlineMax;
    private Integer offlineNum;
    private Integer otherNum;
    private Integer totalCmNum;
    private Date realtime;
    private Long oltId;
    private Long folderId;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getCmNumOnlineMax() {
        return cmNumOnlineMax;
    }

    public void setCmNumOnlineMax(Integer cmNumOnlineMax) {
        this.cmNumOnlineMax = cmNumOnlineMax;
    }

    public Integer getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(Integer offlineNum) {
        this.offlineNum = offlineNum;
    }

    public Integer getOtherNum() {
        return otherNum;
    }

    public void setOtherNum(Integer otherNum) {
        this.otherNum = otherNum;
    }

    public Integer getTotalCmNum() {
        return totalCmNum;
    }

    public void setTotalCmNum(Integer totalCmNum) {
        this.totalCmNum = totalCmNum;
    }

    public Date getRealtime() {
        return realtime;
    }

    public void setRealtime(Date realtime) {
        this.realtime = realtime;
    }

    public Long getOltId() {
        return oltId;
    }

    public void setOltId(Long oltId) {
        this.oltId = oltId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString() {
        return "CmDailyNumStaticReport [cmcId=" + cmcId + ", cmNumOnlineMax=" + cmNumOnlineMax + ", offlineNum="
                + offlineNum + ", otherNum=" + otherNum + ", totalCmNum=" + totalCmNum + ", realtime=" + realtime
                + ", oltId=" + oltId + ", folderId=" + folderId + "]";
    }

}
