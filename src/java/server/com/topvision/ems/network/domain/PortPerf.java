package com.topvision.ems.network.domain;

import java.sql.Timestamp;
import java.text.NumberFormat;

import com.topvision.ems.network.util.PortUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class PortPerf extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -6953249456869947341L;
    private static NumberFormat percent = NumberFormat.getPercentInstance();
    private Long id;
    private Long entityId;
    private Long ifIndex;
    private String ifName;
    private Long ifSpeed;
    private Byte ifAdminStatus;
    private Byte ifOperStatus;
    private Double ifDiscards;
    private Double ifDiscardsRate;
    private Double ifErrors;
    private Double ifErrorsRate;
    private Double ifInDiscards;
    private Double ifInDiscardsRate;
    private Double ifInErrors;
    private Double ifInErrorsRate;
    private Double ifInNUcastPkts;
    private Double ifInOctets;
    private Double ifInOctetsRate;
    private Double ifInUcastPkts;
    private Double ifNUcastPkts;
    private Double ifOctets;
    private Double ifOctetsRate;
    private Double ifOutDiscards;
    private Double ifOutDiscardsRate;
    private Double ifOutErrors;
    private Double ifOutErrorsRate;
    private Double ifOutNUcastPkts;
    private Double ifOutOctets;
    private Double ifOutOctetsRate;
    private Double ifOutUcastPkts;
    private Double ifUcastPkts;
    private Timestamp collectTime;
    private Integer intervalSeconds;

    static {
        percent.setMinimumFractionDigits(2);
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the ifAdminStatus
     */
    public Byte getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @return the ifDiscards
     */
    public Double getIfDiscards() {
        return ifDiscards;
    }

    /**
     * @return the ifDiscardsRate
     */
    public Double getIfDiscardsRate() {
        return ifDiscardsRate;
    }

    /**
     * @return the ifErrors
     */
    public Double getIfErrors() {
        return ifErrors;
    }

    /**
     * @return the ifErrorsRate
     */
    public Double getIfErrorsRate() {
        return ifErrorsRate;
    }

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @return the ifInDiscards
     */
    public Double getIfInDiscards() {
        return ifInDiscards;
    }

    /**
     * @return the ifInDiscardsRate
     */
    public Double getIfInDiscardsRate() {
        return ifInDiscardsRate;
    }

    /**
     * @return the ifInDiscardsRate
     */
    public String getIfInDiscardsRateString() {
        return percent.format(ifInDiscardsRate);
    }

    /**
     * @return the ifInErrors
     */
    public Double getIfInErrors() {
        return ifInErrors;
    }

    /**
     * @return the ifInErrorsRate
     */
    public Double getIfInErrorsRate() {
        return ifInErrorsRate;
    }

    /**
     * @return the ifInErrorsRate
     */
    public String getIfInErrorsRateString() {
        return percent.format(ifInErrorsRate);
    }

    /**
     * @return the ifInNUcastPkts
     */
    public Double getIfInNUcastPkts() {
        return ifInNUcastPkts;
    }

    /**
     * @return the ifInOctets
     */
    public Double getIfInOctets() {
        return ifInOctets;
    }

    /**
     * @return the ifInOctetsRate
     */
    public Double getIfInOctetsRate() {
        return ifInOctetsRate;
    }

    /**
     * @return the ifInOctetsRate
     */
    public String getIfInOctetsRateString() {
        return PortUtil.getIfOctetsRateString(ifInOctetsRate);
    }

    /**
     * @return the ifInOctets
     */
    public String getIfInOctetsString() {
        return PortUtil.getIfOctetsString(ifInOctets);
    }

    /**
     * @return the ifInUcastPkts
     */
    public Double getIfInUcastPkts() {
        return ifInUcastPkts;
    }

    /**
     * @return the ifName
     */
    public String getIfName() {
        return ifName;
    }

    /**
     * @return the ifNUcastPkts
     */
    public Double getIfNUcastPkts() {
        return ifNUcastPkts;
    }

    /**
     * @return the ifOctets
     */
    public Double getIfOctets() {
        return ifOctets;
    }

    /**
     * @return the ifOctetsRate
     */
    public Double getIfOctetsRate() {
        return ifOctetsRate;
    }

    /**
     * @return the ifOctetsRate
     */
    public String getIfOctetsRateString() {
        return PortUtil.getIfOctetsRateString(ifOctetsRate);
    }

    /**
     * @return the ifOctets
     */
    public String getIfOctetsString() {
        return PortUtil.getIfOctetsString(ifOctets);
    }

    /**
     * @return the ifOperStatus
     */
    public Byte getIfOperStatus() {
        return ifOperStatus;
    }

    /**
     * @return the ifOutDiscards
     */
    public Double getIfOutDiscards() {
        return ifOutDiscards;
    }

    /**
     * @return the ifOutDiscardsRate
     */
    public Double getIfOutDiscardsRate() {
        return ifOutDiscardsRate;
    }

    /**
     * @return the ifOutDiscardsRate
     */
    public String getIfOutDiscardsRateString() {
        return percent.format(ifOutDiscardsRate);
    }

    /**
     * @return the ifOutErrors
     */
    public Double getIfOutErrors() {
        return ifOutErrors;
    }

    /**
     * @return the ifOutErrorsRate
     */
    public Double getIfOutErrorsRate() {
        return ifOutErrorsRate;
    }

    /**
     * @return the ifOutErrorsRate
     */
    public String getIfOutErrorsRateString() {
        return percent.format(ifOutErrorsRate);
    }

    /**
     * @return the ifOutNUcastPkts
     */
    public Double getIfOutNUcastPkts() {
        return ifOutNUcastPkts;
    }

    /**
     * @return the ifOutOctets
     */
    public Double getIfOutOctets() {
        return ifOutOctets;
    }

    /**
     * @return the ifOutOctetsRate
     */
    public Double getIfOutOctetsRate() {
        return ifOutOctetsRate;
    }

    /**
     * @return the ifOutOctetsRate
     */
    public String getIfOutOctetsRateString() {
        return PortUtil.getIfOctetsRateString(ifOutOctetsRate);
    }

    /**
     * @return the ifOutOctets
     */
    public String getIfOutOctetsString() {
        return PortUtil.getIfOctetsString(ifOutOctets);
    }

    /**
     * @return the ifOutUcastPkts
     */
    public Double getIfOutUcastPkts() {
        return ifOutUcastPkts;
    }

    /**
     * @return the ifSpeed
     */
    public Long getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @return the ifSpeed
     */
    public String getIfSpeedString() {
        return PortUtil.getIfSpeedString(ifSpeed);
    }

    /**
     * @return the ifUcastPkts
     */
    public Double getIfUcastPkts() {
        return ifUcastPkts;
    }

    /**
     * @return the intervalSeconds
     */
    public Integer getIntervalSeconds() {
        return intervalSeconds;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(Byte ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    /**
     * @param ifDiscards
     *            the ifDiscards to set
     */
    public void setIfDiscards(Double ifDiscards) {
        this.ifDiscards = ifDiscards;
    }

    /**
     * @param ifDiscardsRate
     *            the ifDiscardsRate to set
     */
    public void setIfDiscardsRate(Double ifDiscardsRate) {
        this.ifDiscardsRate = ifDiscardsRate;
    }

    /**
     * @param ifErrors
     *            the ifErrors to set
     */
    public void setIfErrors(Double ifErrors) {
        this.ifErrors = ifErrors;
    }

    /**
     * @param ifErrorsRate
     *            the ifErrorsRate to set
     */
    public void setIfErrorsRate(Double ifErrorsRate) {
        this.ifErrorsRate = ifErrorsRate;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @param ifInDiscards
     *            the ifInDiscards to set
     */
    public void setIfInDiscards(Double ifInDiscards) {
        this.ifInDiscards = ifInDiscards;
    }

    /**
     * @param ifInDiscardsRate
     *            the ifInDiscardsRate to set
     */
    public void setIfInDiscardsRate(Double ifInDiscardsRate) {
        this.ifInDiscardsRate = ifInDiscardsRate;
    }

    /**
     * @param ifInErrors
     *            the ifInErrors to set
     */
    public void setIfInErrors(Double ifInErrors) {
        this.ifInErrors = ifInErrors;
    }

    /**
     * @param ifInErrorsRate
     *            the ifInErrorsRate to set
     */
    public void setIfInErrorsRate(Double ifInErrorsRate) {
        this.ifInErrorsRate = ifInErrorsRate;
    }

    /**
     * @param ifInNUcastPkts
     *            the ifInNUcastPkts to set
     */
    public void setIfInNUcastPkts(Double ifInNUcastPkts) {
        this.ifInNUcastPkts = ifInNUcastPkts;
    }

    /**
     * @param ifInOctets
     *            the ifInOctets to set
     */
    public void setIfInOctets(Double ifInOctets) {
        this.ifInOctets = ifInOctets;
    }

    /**
     * @param ifInOctetsRate
     *            the ifInOctetsRate to set
     */
    public void setIfInOctetsRate(Double ifInOctetsRate) {
        this.ifInOctetsRate = ifInOctetsRate;
    }

    /**
     * @param ifInUcastPkts
     *            the ifInUcastPkts to set
     */
    public void setIfInUcastPkts(Double ifInUcastPkts) {
        this.ifInUcastPkts = ifInUcastPkts;
    }

    /**
     * @param ifName
     *            the ifName to set
     */
    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    /**
     * @param ifNUcastPkts
     *            the ifNUcastPkts to set
     */
    public void setIfNUcastPkts(Double ifNUcastPkts) {
        this.ifNUcastPkts = ifNUcastPkts;
    }

    /**
     * @param ifOctets
     *            the ifOctets to set
     */
    public void setIfOctets(Double ifOctets) {
        this.ifOctets = ifOctets;
    }

    /**
     * @param ifOctetsRate
     *            the ifOctetsRate to set
     */
    public void setIfOctetsRate(Double ifOctetsRate) {
        this.ifOctetsRate = ifOctetsRate;
    }

    /**
     * @param ifOperStatus
     *            the ifOperStatus to set
     */
    public void setIfOperStatus(Byte ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    /**
     * @param ifOutDiscards
     *            the ifOutDiscards to set
     */
    public void setIfOutDiscards(Double ifOutDiscards) {
        this.ifOutDiscards = ifOutDiscards;
    }

    /**
     * @param ifOutDiscardsRate
     *            the ifOutDiscardsRate to set
     */
    public void setIfOutDiscardsRate(Double ifOutDiscardsRate) {
        this.ifOutDiscardsRate = ifOutDiscardsRate;
    }

    /**
     * @param ifOutErrors
     *            the ifOutErrors to set
     */
    public void setIfOutErrors(Double ifOutErrors) {
        this.ifOutErrors = ifOutErrors;
    }

    /**
     * @param ifOutErrorsRate
     *            the ifOutErrorsRate to set
     */
    public void setIfOutErrorsRate(Double ifOutErrorsRate) {
        this.ifOutErrorsRate = ifOutErrorsRate;
    }

    /**
     * @param ifOutNUcastPkts
     *            the ifOutNUcastPkts to set
     */
    public void setIfOutNUcastPkts(Double ifOutNUcastPkts) {
        this.ifOutNUcastPkts = ifOutNUcastPkts;
    }

    /**
     * @param ifOutOctets
     *            the ifOutOctets to set
     */
    public void setIfOutOctets(Double ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }

    /**
     * @param ifOutOctetsRate
     *            the ifOutOctetsRate to set
     */
    public void setIfOutOctetsRate(Double ifOutOctetsRate) {
        this.ifOutOctetsRate = ifOutOctetsRate;
    }

    /**
     * @param ifOutUcastPkts
     *            the ifOutUcastPkts to set
     */
    public void setIfOutUcastPkts(Double ifOutUcastPkts) {
        this.ifOutUcastPkts = ifOutUcastPkts;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @param ifUcastPkts
     *            the ifUcastPkts to set
     */
    public void setIfUcastPkts(Double ifUcastPkts) {
        this.ifUcastPkts = ifUcastPkts;
    }

    /**
     * @param intervalSeconds
     *            the intervalSeconds to set
     */
    public void setIntervalSeconds(Integer intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        data.append("id:").append(id);
        data.append(",entityId:").append(entityId);
        data.append(",ifIndex:").append(ifIndex);
        data.append(",ifSpeed:").append(getIfSpeedString());
        data.append(",ifInOctetsRate:").append(getIfInOctetsRateString());
        data.append(",ifOutOctetsRate:").append(getIfOutOctetsRateString());
        data.append(",ifInOctets:").append(getIfInOctetsString());
        data.append(",ifOutOctets:").append(getIfOutOctetsString());
        data.append(",ifInErrorsRate:").append(getIfInErrorsRateString());
        data.append(",ifOutErrorsRate:").append(getIfOutErrorsRateString());
        data.append(",ifInDiscardsRate:").append(getIfInDiscardsRateString());
        data.append(",ifOutDiscardsRate:").append(getIfOutDiscardsRateString());
        data.append(",collectTime:").append(collectTime);
        return data.toString();
    }
}
