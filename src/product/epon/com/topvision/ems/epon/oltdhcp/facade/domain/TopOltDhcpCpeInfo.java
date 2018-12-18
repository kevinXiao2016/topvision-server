/***********************************************************************
 * $Id: TopOltDhcpCpeInfo.java,v1.0 2017年11月16日 下午4:10:25 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.facade.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年11月16日-下午4:10:25
 *
 */
public class TopOltDhcpCpeInfo implements AliasesSuperType {
    private static final long serialVersionUID = 399937281034524863L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.8.1.2", index = true)
    private String topOltDhcpCpeIpIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.8.1.3", type = "OctetString")
    private String topOltDhcpCpeMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.8.1.4", type = "Integer32")
    private Integer topOltDhcpCpeVlan;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.8.1.5", type = "Integer32")
    private Integer topOltDhcpCpePortType;// fe(2) ge(3) xe(4) ethagg(9) onu(10)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.8.1.6", type = "Integer32")
    private Integer topOltDhcpCpeSlot;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.8.1.7", type = "Integer32")
    private Integer topOltDhcpCpePort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.8.1.8", type = "Integer32")
    private Integer topOltDhcpCpeOnu;
    private String onuName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.8.1.9", type = "Integer32")
    private Integer topOltDhcpCpeRemainingTime;// 租期剩余秒数
    private String remainingTimeStr;
    private Long typeId;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTopOltDhcpCpeIpIndex() {
        return topOltDhcpCpeIpIndex;
    }

    public void setTopOltDhcpCpeIpIndex(String topOltDhcpCpeIpIndex) {
        this.topOltDhcpCpeIpIndex = topOltDhcpCpeIpIndex;
    }

    public String getTopOltDhcpCpeMac() {
        return topOltDhcpCpeMac;
    }

    public void setTopOltDhcpCpeMac(String topOltDhcpCpeMac) {
        this.topOltDhcpCpeMac = topOltDhcpCpeMac;
    }

    public Integer getTopOltDhcpCpeVlan() {
        return topOltDhcpCpeVlan;
    }

    public void setTopOltDhcpCpeVlan(Integer topOltDhcpCpeVlan) {
        this.topOltDhcpCpeVlan = topOltDhcpCpeVlan;
    }

    public Integer getTopOltDhcpCpePortType() {
        return topOltDhcpCpePortType;
    }

    public void setTopOltDhcpCpePortType(Integer topOltDhcpCpePortType) {
        this.topOltDhcpCpePortType = topOltDhcpCpePortType;
    }

    public Integer getTopOltDhcpCpeSlot() {
        return topOltDhcpCpeSlot;
    }

    public void setTopOltDhcpCpeSlot(Integer topOltDhcpCpeSlot) {
        this.topOltDhcpCpeSlot = topOltDhcpCpeSlot;
    }

    public Integer getTopOltDhcpCpePort() {
        return topOltDhcpCpePort;
    }

    public void setTopOltDhcpCpePort(Integer topOltDhcpCpePort) {
        this.topOltDhcpCpePort = topOltDhcpCpePort;
    }

    public Integer getTopOltDhcpCpeOnu() {
        return topOltDhcpCpeOnu;
    }

    public void setTopOltDhcpCpeOnu(Integer topOltDhcpCpeOnu) {
        this.topOltDhcpCpeOnu = topOltDhcpCpeOnu;
    }

    public Integer getTopOltDhcpCpeRemainingTime() {
        return topOltDhcpCpeRemainingTime;
    }

    public void setTopOltDhcpCpeRemainingTime(Integer topOltDhcpCpeRemainingTime) {
        this.topOltDhcpCpeRemainingTime = topOltDhcpCpeRemainingTime;
        this.remainingTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()+topOltDhcpCpeRemainingTime*1000));
    }

    public String getOnuName() {
        if (this.topOltDhcpCpeSlot != null && this.topOltDhcpCpePort != null && this.topOltDhcpCpeOnu != null) {
            onuName = this.topOltDhcpCpeSlot + "/" + this.topOltDhcpCpePort + ":" + this.topOltDhcpCpeOnu;
        }
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public String getRemainingTimeStr() {
        return remainingTimeStr;
    }

    public void setRemainingTimeStr(String remainingTimeStr) {
        this.remainingTimeStr = remainingTimeStr;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltDhcpCpeInfo [entityId=");
        builder.append(entityId);
        builder.append(", topOltDhcpCpeIpIndex=");
        builder.append(topOltDhcpCpeIpIndex);
        builder.append(", topOltDhcpCpeMac=");
        builder.append(topOltDhcpCpeMac);
        builder.append(", topOltDhcpCpeVlan=");
        builder.append(topOltDhcpCpeVlan);
        builder.append(", topOltDhcpCpePortType=");
        builder.append(topOltDhcpCpePortType);
        builder.append(", topOltDhcpCpeSlot=");
        builder.append(topOltDhcpCpeSlot);
        builder.append(", topOltDhcpCpePort=");
        builder.append(topOltDhcpCpePort);
        builder.append(", topOltDhcpCpeOnu=");
        builder.append(topOltDhcpCpeOnu);
        builder.append(", onuName=");
        builder.append(onuName);
        builder.append(", topOltDhcpCpeRemainingTime=");
        builder.append(topOltDhcpCpeRemainingTime);
        builder.append(", remainingTimeStr=");
        builder.append(remainingTimeStr);
        builder.append("]");
        return builder.toString();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

}
