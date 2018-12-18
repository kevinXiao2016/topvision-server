/***********************************************************************
 * $Id: AutoClearRecord.java,v1.0 2016年11月14日 下午3:18:46 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.autoclear.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2016年11月14日-下午3:18:46
 *
 */
public class AutoClearRecord implements AliasesSuperType {

    private static final long serialVersionUID = -1547775581550700617L;

    private String oltName;
    private String oltIp;
    private String onuName;
    private String onuType;
    private String onuMac; // onuUniqueIdentification
    private String onuIndex;
    private Timestamp offlineTime;
    private String offlineTimeString;
    private Timestamp clearTime; // 清除时间
    private String clearTimeString;

    public static class Builder {
        private final String oltName;
        private final String oltIp;
        private final String onuName;
        private String onuType;
        private String onuMac;
        private final String onuIndex;
        private Timestamp offlineTime;
        private Timestamp clearTime; // 清除时间

        public Builder(String oltName, String oltIp, String onuName, String onuIndex) {
            this.oltName = oltName;
            this.oltIp = oltIp;
            this.onuName = onuName;
            this.onuIndex = onuIndex;
        }

        public Builder onuType(String onuType) {
            this.onuType = onuType;
            return this;
        }

        public Builder onuMac(String onuMac) {
            this.onuMac = onuMac;
            return this;
        }

        public Builder offlineTime(Timestamp offlineTime) {
            this.offlineTime = offlineTime;
            return this;
        }

        public Builder clearTime(Timestamp clearTime) {
            this.clearTime = clearTime;
            return this;
        }

        public AutoClearRecord build() {
            return new AutoClearRecord(this);
        }
    }
    
    public AutoClearRecord(){}

    private AutoClearRecord(Builder builder) {
        super();
        this.oltName = builder.oltName;
        this.oltIp = builder.oltIp;
        this.onuName = builder.onuName;
        this.onuMac = builder.onuMac;
        this.onuType = builder.onuType;
        this.onuIndex = builder.onuIndex;
        this.offlineTime = builder.offlineTime;
        this.clearTime = builder.clearTime;
    }

    /**
     * @return the oltName
     */
    public String getOltName() {
        return oltName;
    }

    /**
     * @param oltName
     *            the oltName to set
     */
    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    /**
     * @return the oltIp
     */
    public String getOltIp() {
        return oltIp;
    }

    /**
     * @param oltIp
     *            the oltIp to set
     */
    public void setOltIp(String oltIp) {
        this.oltIp = oltIp;
    }

    /**
     * @return the onuName
     */
    public String getOnuName() {
        return onuName;
    }

    /**
     * @param onuName
     *            the onuName to set
     */
    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    /**
     * @return the onuType
     */
    public String getOnuType() {
        return onuType;
    }

    /**
     * @param onuType
     *            the onuType to set
     */
    public void setOnuType(String onuType) {
        this.onuType = onuType;
    }

    /**
     * @return the onuMac
     */
    public String getOnuMac() {
        return onuMac;
    }

    /**
     * @param onuMac
     *            the onuMac to set
     */
    public void setOnuMac(String onuMac) {
        this.onuMac = onuMac;
    }

    /**
     * @return the onuIndex
     */
    public String getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(String onuIndex) {
        this.onuIndex = onuIndex;
    }

    /**
     * @return the offlineTime
     */
    public Timestamp getOfflineTime() {
        return offlineTime;
    }

    /**
     * @param offlineTime
     *            the offlineTime to set
     */
    public void setOfflineTime(Timestamp offlineTime) {
        this.offlineTime = offlineTime;
    }

    /**
     * @return the clearTime
     */
    public Timestamp getClearTime() {
        return clearTime;
    }

    /**
     * @param clearTime
     *            the clearTime to set
     */
    public void setClearTime(Timestamp clearTime) {
        this.clearTime = clearTime;
    }

    /**
     * @return the offlineTimeString
     */
    public String getOfflineTimeString() {
        if (offlineTimeString == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            offlineTimeString = sdf.format(offlineTime);
        }
        return offlineTimeString;
    }

    /**
     * @param offlineTimeString
     *            the offlineTimeString to set
     */
    public void setOfflineTimeString(String offlineTimeString) {
        this.offlineTimeString = offlineTimeString;
    }

    /**
     * @return the clearTimeString
     */
    public String getClearTimeString() {
        if (clearTimeString == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            clearTimeString = sdf.format(clearTime);
        }
        return clearTimeString;
    }

    /**
     * @param clearTimeString
     *            the clearTimeString to set
     */
    public void setClearTimeString(String clearTimeString) {
        this.clearTimeString = clearTimeString;
    }

}
