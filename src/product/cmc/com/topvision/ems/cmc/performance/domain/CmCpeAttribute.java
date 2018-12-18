/***********************************************************************
 * $ CmCpeAttribute.java,v1.0 2013-7-13 11:41:27 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import org.apache.ibatis.type.Alias;

import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2013-7-13-11:41:27
 */
@Alias("cmCpeAttribute")
public class CmCpeAttribute implements AliasesSuperType{
    private static final long serialVersionUID = 4201959565030472965L;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmId;
    private Long statusIndex;
    private String name;
    private String ip;
    private String cmcName;
    private Long type;
    private Long typeId;
    private String typeName;
    private String statusMacAddress;
    private String cmAlias;
    private String statusInetAddress;
    private Long statusDownChannelIfIndex;
    private Long statusUpChannelIfIndex;
    private Long topCmCpeCcmtsIfIndex;
    private Long cpeIp;
    private Long cpeMac;
    private String topCmCpeIpAddress;
    private String topCmCpeMacAddress;
    private Integer topCmCpeType;
    private Timestamp updateTime;
    private Long upchannelId;
    private Long downChannelId;
    private String cmtsUpDescr;
    private String cmtsDownDescr;
    private String statusIpAddress;
    private String ifname;

    public CmCpeAttribute() {
    }

    public Long getCpeIp() {
        return cpeIp;
    }

    public void setCpeIp(Long cpeIp) {
        topCmCpeIpAddress = new IpUtils(cpeIp).toString();
        this.cpeIp = cpeIp;
    }

    public Long getCpeMac() {
        return cpeMac;
    }

    public void setCpeMac(Long cpeMac) {
        topCmCpeMacAddress = new MacUtils(cpeMac).toString();
        this.cpeMac = cpeMac;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getStatusDownChannelIfIndex() {
        return statusDownChannelIfIndex;
    }

    public void setStatusDownChannelIfIndex(Long statusDownChannelIfIndex) {
        this.statusDownChannelIfIndex = statusDownChannelIfIndex;
        this.downChannelId = CmcIndexUtils.getChannelId(statusDownChannelIfIndex);
    }

    public Long getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    public String getStatusInetAddress() {
        return statusInetAddress;
    }

    public void setStatusInetAddress(String statusInetAddress) {
        this.statusInetAddress = statusInetAddress;
    }

    public String getStatusMacAddress() {
        return statusMacAddress;
    }

    public void setStatusMacAddress(String statusMacAddress) {
        this.statusMacAddress = statusMacAddress;
    }

    public Long getStatusUpChannelIfIndex() {
        return statusUpChannelIfIndex;
    }

    public void setStatusUpChannelIfIndex(Long statusUpChannelIfIndex) {
        this.statusUpChannelIfIndex = statusUpChannelIfIndex;
        this.upchannelId = CmcIndexUtils.getChannelId(statusUpChannelIfIndex);
    }

    public Long getTopCmCpeCcmtsIfIndex() {
        return topCmCpeCcmtsIfIndex;
    }

    public void setTopCmCpeCcmtsIfIndex(Long topCmCpeCcmtsIfIndex) {
        this.topCmCpeCcmtsIfIndex = topCmCpeCcmtsIfIndex;
    }

    public String getTopCmCpeIpAddress() {
        return topCmCpeIpAddress;
    }

    public void setTopCmCpeIpAddress(String topCmCpeIpAddress) {
        this.topCmCpeIpAddress = topCmCpeIpAddress;
    }

    public String getTopCmCpeMacAddress() {
        return topCmCpeMacAddress;
    }

    public void setTopCmCpeMacAddress(String topCmCpeMacAddress) {
        this.topCmCpeMacAddress = topCmCpeMacAddress;
    }

    public Integer getTopCmCpeType() {
        return topCmCpeType;
    }

    public void setTopCmCpeType(Integer topCmCpeType) {
        this.topCmCpeType = topCmCpeType;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getCpeTypeName() {
        if (topCmCpeType == null) {
            return "";
        }
        if (InitialDataCpeAction.BROADBAND.intValue() == topCmCpeType
                .intValue()) {
            return "HOST";
        } else if (InitialDataCpeAction.MTA.intValue() == topCmCpeType
                .intValue()) {
            return "MTA";
        } else if (InitialDataCpeAction.INTERACTIVE.intValue() == topCmCpeType
                .intValue()) {
            return "STB";
        }
        return "HOST";
    }

    public String getUpdateTimeString() {
        if (updateTime == null) {
            return "";
        }
        return sdf.format(updateTime);
    }

    public String getStatusDownChannelIfIndexString() {
        return "D" +
                CmcIndexUtils.getSlotNo(statusDownChannelIfIndex) + "/" +
                CmcIndexUtils.getPonNo(statusDownChannelIfIndex) + "/" +
                CmcIndexUtils.getCmcId(statusDownChannelIfIndex) + "/" +
                CmcIndexUtils.getChannelId(statusDownChannelIfIndex);
    }

    public String getStatusUpChannelIfIndexString() {
        return "U" +
                CmcIndexUtils.getSlotNo(statusUpChannelIfIndex) + "/" +
                CmcIndexUtils.getPonNo(statusUpChannelIfIndex) + "/" +
                CmcIndexUtils.getCmcId(statusUpChannelIfIndex) + "/" +
                CmcIndexUtils.getChannelId(statusUpChannelIfIndex);
    }

    public Long getUpchannelId() {
        return upchannelId;
    }

    public void setUpchannelId(Long upchannelId) {
        this.upchannelId = upchannelId;
    }

    public Long getDownChannelId() {
        return downChannelId;
    }

    public void setDownChannelId(Long downChannelId) {
        this.downChannelId = downChannelId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public String getCmAlias() {
        return cmAlias;
    }

    public void setCmAlias(String cmAlias) {
        this.cmAlias = cmAlias;
    }

    public String getCmtsUpDescr() {
        return cmtsUpDescr;
    }

    public void setCmtsUpDescr(String cmtsUpDescr) {
        this.cmtsUpDescr = cmtsUpDescr;
    }

    public String getCmtsDownDescr() {
        return cmtsDownDescr;
    }

    public void setCmtsDownDescr(String cmtsDownDescr) {
        this.cmtsDownDescr = cmtsDownDescr;
    }

    public String getStatusIpAddress() {
        return statusIpAddress;
    }

    public void setStatusIpAddress(String statusIpAddress) {
        this.statusIpAddress = statusIpAddress;
    }

    public String getIfname() {
        return ifname;
    }

    public void setIfname(String ifname) {
        this.ifname = ifname;
    }
    
}
