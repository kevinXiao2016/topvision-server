/***********************************************************************
 * $Id: GponLineProfileGemMap.java,v1.0 2016年10月24日 下午6:06:20 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年10月24日-下午6:06:20
 *
 */
public class GponLineProfileGemMap implements AliasesSuperType {
    private static final long serialVersionUID = 2022378337670043093L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.4.1.1", index = true, type = "Integer32")
    private Integer gponLineProfileGemMapProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.4.1.2", index = true, type = "Integer32")
    private Integer gponLineProfileGemMapGemIndex;// 1-64
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.4.1.3", index = true, type = "Integer32")
    private Integer gponLineProfileGemMapIndex;// 1-8
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.4.1.4", writable = true, type = "Integer32")
    private Integer gponLineProfileGemMapVlan;// 1-4094
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.4.1.5", writable = true, type = "Integer32")
    private Integer gponLineProfileGemMapPriority;// 0-7
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.4.1.6", writable = true, type = "Integer32")
    private Integer gponLineProfileGemMapPortType;// 1:eth; 2:iphost
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.4.1.7", writable = true, type = "Integer32")
    private Integer gponLineProfileGemMapPortId;// 与gponLineProfileGemMapPortType必须同时下发，eth时范围1-24，
                                                // iphost时范围1-64
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.4.1.8", writable = true, type = "Integer32")
    private Integer gponLineProfileGemMapRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponLineProfileGemMapProfileIndex() {
        return gponLineProfileGemMapProfileIndex;
    }

    public void setGponLineProfileGemMapProfileIndex(Integer gponLineProfileGemMapProfileIndex) {
        this.gponLineProfileGemMapProfileIndex = gponLineProfileGemMapProfileIndex;
    }

    public Integer getGponLineProfileGemMapGemIndex() {
        return gponLineProfileGemMapGemIndex;
    }

    public void setGponLineProfileGemMapGemIndex(Integer gponLineProfileGemMapGemIndex) {
        this.gponLineProfileGemMapGemIndex = gponLineProfileGemMapGemIndex;
    }

    public Integer getGponLineProfileGemMapIndex() {
        return gponLineProfileGemMapIndex;
    }

    public void setGponLineProfileGemMapIndex(Integer gponLineProfileGemMapIndex) {
        this.gponLineProfileGemMapIndex = gponLineProfileGemMapIndex;
    }

    public Integer getGponLineProfileGemMapVlan() {
        return gponLineProfileGemMapVlan;
    }

    public void setGponLineProfileGemMapVlan(Integer gponLineProfileGemMapVlan) {
        this.gponLineProfileGemMapVlan = gponLineProfileGemMapVlan;
    }

    public Integer getGponLineProfileGemMapPriority() {
        return gponLineProfileGemMapPriority;
    }

    public void setGponLineProfileGemMapPriority(Integer gponLineProfileGemMapPriority) {
        this.gponLineProfileGemMapPriority = gponLineProfileGemMapPriority;
    }

    public Integer getGponLineProfileGemMapPortId() {
        return gponLineProfileGemMapPortId;
    }

    public void setGponLineProfileGemMapPortId(Integer gponLineProfileGemMapPortId) {
        this.gponLineProfileGemMapPortId = gponLineProfileGemMapPortId;
    }

    public Integer getGponLineProfileGemMapRowStatus() {
        return gponLineProfileGemMapRowStatus;
    }

    public void setGponLineProfileGemMapRowStatus(Integer gponLineProfileGemMapRowStatus) {
        this.gponLineProfileGemMapRowStatus = gponLineProfileGemMapRowStatus;
    }

    /**
     * @return the gponLineProfileGemMapPortType
     */
    public Integer getGponLineProfileGemMapPortType() {
        return gponLineProfileGemMapPortType;
    }

    /**
     * @param gponLineProfileGemMapPortType
     *            the gponLineProfileGemMapPortType to set
     */
    public void setGponLineProfileGemMapPortType(Integer gponLineProfileGemMapPortType) {
        this.gponLineProfileGemMapPortType = gponLineProfileGemMapPortType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponLineProfileGemMap [entityId=");
        builder.append(entityId);
        builder.append(", gponLineProfileGemMapProfileIndex=");
        builder.append(gponLineProfileGemMapProfileIndex);
        builder.append(", gponLineProfileGemMapGemIndex=");
        builder.append(gponLineProfileGemMapGemIndex);
        builder.append(", gponLineProfileGemMapIndex=");
        builder.append(gponLineProfileGemMapIndex);
        builder.append(", gponLineProfileGemMapVlan=");
        builder.append(gponLineProfileGemMapVlan);
        builder.append(", gponLineProfileGemMapPriority=");
        builder.append(gponLineProfileGemMapPriority);
        builder.append(", gponLineProfileGemMapPortType=");
        builder.append(gponLineProfileGemMapPortType);
        builder.append(", gponLineProfileGemMapPortId=");
        builder.append(gponLineProfileGemMapPortId);
        builder.append(", gponLineProfileGemMapRowStatus=");
        builder.append(gponLineProfileGemMapRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
