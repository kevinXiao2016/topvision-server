/***********************************************************************
 * $Id: PerfTaskUpdateInfo.java,v1.0 2014-2-10 下午4:07:07 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2014-2-10-下午4:07:07
 * 
 */
public class PerfTaskUpdateInfo implements AliasesSuperType {
    private static final long serialVersionUID = 6943271427667082952L;
    private String category;
    private String targetName;
    private Long identifyKey;
    private Integer lastInterval;
    private Integer lastEnable;
    private Integer newInterval;
    private Integer newEnable;
    private Object data;
    private SnmpParam snmpParam;
    private Long typeId;

    /**
     * 
     * @param category
     * @param targetName
     * @param identifyKey
     * @param lastInterval
     * @param newInterval
     * @param data
     * @param snmpParam
     */
    public PerfTaskUpdateInfo(String category, String targetName, Long identifyKey, Integer lastInterval,
            Integer newInterval, Object data, SnmpParam snmpParam, Long typeId) {
        super();
        this.category = category;
        this.targetName = targetName;
        this.identifyKey = identifyKey;
        this.lastInterval = lastInterval;
        this.newInterval = newInterval;
        this.data = data;
        this.snmpParam = snmpParam;
        this.typeId = typeId;
    }

    /**
     * 
     * @param category
     * @param targetName
     * @param identifyKey
     * @param lastInterval
     * @param lastEnable
     * @param newInterval
     * @param newEnable
     * @param data
     * @param snmpParam
     */
    public PerfTaskUpdateInfo(String category, String targetName, Long identifyKey, Integer lastInterval,
            Integer lastEnable, Integer newInterval, Integer newEnable, Object data, SnmpParam snmpParam, Long typeId) {
        super();
        this.category = category;
        this.targetName = targetName;
        this.identifyKey = identifyKey;
        this.lastInterval = lastInterval;
        this.lastEnable = lastEnable;
        if (lastEnable == 0) {
            this.lastInterval = 0;
        }
        this.newInterval = newInterval;
        this.newEnable = newEnable;
        if (newEnable == 0) {
            this.newInterval = 0;
        }
        this.data = data;
        this.snmpParam = snmpParam;
        this.typeId = typeId;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName
     *            the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the identifyKey
     */
    public Long getIdentifyKey() {
        return identifyKey;
    }

    /**
     * @param identifyKey
     *            the identifyKey to set
     */
    public void setIdentifyKey(Long identifyKey) {
        this.identifyKey = identifyKey;
    }

    /**
     * @return the lastInterval
     */
    public Integer getLastInterval() {
        return lastInterval;
    }

    /**
     * @param lastInterval
     *            the lastInterval to set
     */
    public void setLastInterval(Integer lastInterval) {
        this.lastInterval = lastInterval;
    }

    /**
     * @return the newInterval
     */
    public Integer getNewInterval() {
        return newInterval;
    }

    /**
     * @param newInterval
     *            the newInterval to set
     */
    public void setNewInterval(Integer newInterval) {
        this.newInterval = newInterval;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return the lastEnable
     */
    public Integer getLastEnable() {
        return lastEnable;
    }

    /**
     * @param lastEnable
     *            the lastEnable to set
     */
    public void setLastEnable(Integer lastEnable) {
        this.lastEnable = lastEnable;
    }

    /**
     * @return the newEnable
     */
    public Integer getNewEnable() {
        return newEnable;
    }

    /**
     * @param newEnable
     *            the newEnable to set
     */
    public void setNewEnable(Integer newEnable) {
        this.newEnable = newEnable;
    }

    /**
     * @return the snmpParam
     */
    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    /**
     * @param snmpParam
     *            the snmpParam to set
     */
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfTaskUpdateInfo [category=");
        builder.append(category);
        builder.append(", targetName=");
        builder.append(targetName);
        builder.append(", identifyKey=");
        builder.append(identifyKey);
        builder.append(", lastInterval=");
        builder.append(lastInterval);
        builder.append(", lastEnable=");
        builder.append(lastEnable);
        builder.append(", newInterval=");
        builder.append(newInterval);
        builder.append(", newEnable=");
        builder.append(newEnable);
        builder.append(", data=");
        builder.append(data);
        builder.append(", snmpParam=");
        builder.append(snmpParam);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append("]");
        return builder.toString();
    }
}
