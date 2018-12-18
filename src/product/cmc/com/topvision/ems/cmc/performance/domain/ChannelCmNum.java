/***********************************************************************
 * $Id: DsUserNum.java,v1.0 2012-7-11 下午01:36:04 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2012-7-11-下午01:36:04
 * 
 */
@Alias("channelCmNum")
public class ChannelCmNum implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 5593175368669182768L;
    private Long entityId;
    private Long parentId;
    private Integer channelId;
    private Long cmcId;
    private Timestamp dt;
    private String recentUpdateTime;
    private Long channelIndex;
    private int channelType;
    private String channelTypeString;
    private Integer cmNumTotal = 0;
    private Integer cmNumOnline = 0;
    private Integer cmNumActive = 0;
    private Integer cmNumUnregistered = 0;
    private Integer cmNumOffline = 0;
    private Integer CmNumRregistered = 0;// 注册cm数
    private Integer channelStatus;
    private Long cmcPortId;
    private String cmcMac;
    private String topCcmtsSysMacAddr;
    private String cmcName;
    private Integer cmcType;
    private String cmcPortName;
    private String displayPortName;
    private Long ifType;
    private String ifDescr;
    private String displayName;
    private String ip;
    private String ifName;

    public String getCmcPortName() {
        Long index = getChannelIndex();
        if (index != null) {
            String Slot_Pon_cmcId = CmcIndexUtils.getSlotNo(index) + Symbol.SLASH + CmcIndexUtils.getPonNo(index)
                    + Symbol.SLASH + CmcIndexUtils.getCmcId(index);
            cmcPortName = channelTypeString + " " + Slot_Pon_cmcId + Symbol.SLASH + getChannelId();
        }
        return cmcPortName;
    }

    public void setCmcPortName(String cmcPortName) {
        this.cmcPortName = cmcPortName;
    }

    public String getDisplayPortName() {
        return displayPortName;
    }

    public void setDisplayPortName(String displayPortName) {
        this.displayPortName = displayPortName;
    }

    public String getChannelTypeString() {
        if (this.channelType == 0) {
            channelTypeString = "US";
        } else {
            channelTypeString = "DS";
        }
        return channelTypeString;
    }

    public void setChannelTypeString(String channelTypeString) {
        this.channelTypeString = channelTypeString;
    }

    public Integer getCmcType() {
        return cmcType;
    }

    public void setCmcType(Integer cmcType) {
        this.cmcType = cmcType;
    }

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
        this.topCcmtsSysMacAddr = cmcMac;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the channelIndex
     */
    public Long getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
        channelId = CmcIndexUtils.getChannelId(channelIndex).intValue();
    }

    /**
     * @return the cmNumTotal
     */
    public Integer getCmNumTotal() {
        return cmNumTotal;
    }

    /**
     * @param cmNumTotal
     *            the cmNumTotal to set
     */
    public void setCmNumTotal(Integer cmNumTotal) {
        this.cmNumTotal = cmNumTotal;
    }

    /**
     * @return the cmNumOnline
     */
    public Integer getCmNumOnline() {
        return cmNumOnline;
    }

    /**
     * @param cmNumOnline
     *            the cmNumOnline to set
     */
    public void setCmNumOnline(Integer cmNumOnline) {
        this.cmNumOnline = cmNumOnline;
    }

    /**
     * @return the cmNumActive
     */
    public Integer getCmNumActive() {
        return cmNumActive;
    }

    /**
     * @param cmNumActive
     *            the cmNumActive to set
     */
    public void setCmNumActive(Integer cmNumActive) {
        this.cmNumActive = cmNumActive;
    }

    /**
     * @return the cmNumUnregistered
     */
    public Integer getCmNumUnregistered() {
        return cmNumUnregistered;
    }

    /**
     * @param cmNumUnregistered
     *            the cmNumUnregistered to set
     */
    public void setCmNumUnregistered(Integer cmNumUnregistered) {
        this.cmNumUnregistered = cmNumUnregistered;
    }

    /**
     * @return the cmNumOffline
     */
    public Integer getCmNumOffline() {
        return cmNumOffline;
    }

    /**
     * @param cmNumOffline
     *            the cmNumOffline to set
     */
    public void setCmNumOffline(Integer cmNumOffline) {
        this.cmNumOffline = cmNumOffline;
    }

    /**
     * @return the cmNumRregistered
     */
    public Integer getCmNumRregistered() {
        return CmNumRregistered;
    }

    /**
     * @param cmNumRregistered
     *            the cmNumRregistered to set
     */
    public void setCmNumRregistered(Integer cmNumRregistered) {
        CmNumRregistered = cmNumRregistered;
    }

    /**
     * @return the channelType
     */
    public int getChannelType() {
        return channelType;
    }

    /**
     * @param channelType
     *            the channelType to set
     */
    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    /**
     * @return the dt
     */
    public Timestamp getDt() {
        return dt;
    }

    /**
     * @param dt
     *            the dt to set
     */
    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    /**
     * @return the channelId
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelId
     *            the channelId to set
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the channelStatus
     */
    public Integer getChannelStatus() {
        return channelStatus;
    }

    /**
     * @param channelStatus
     *            the channelStatus to set
     */
    public void setChannelStatus(Integer channelStatus) {
        this.channelStatus = channelStatus;
    }

    /**
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    public Long getIfType() {
        return ifType;
    }

    public void setIfType(Long ifType) {
        this.ifType = ifType;
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDtStr() {
        // dt为null时要单独处理,如果处理为空字符串在导出Excel时会有问题
        if (this.dt == null) {
            return "--";
        } else {
            return DateUtils.format(this.dt);
        }
    }

    public String getTopCcmtsSysMacAddr() {
        return topCcmtsSysMacAddr;
    }

    public void setTopCcmtsSysMacAddr(String topCcmtsSysMacAddr) {
        this.topCcmtsSysMacAddr = topCcmtsSysMacAddr;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public String getRecentUpdateTime() {
        return recentUpdateTime;
    }

    public void setRecentUpdateTime(String recentUpdateTime) {
        this.recentUpdateTime = recentUpdateTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelCmNum [entityId=");
        builder.append(entityId);
        builder.append(", parentId=");
        builder.append(parentId);
        builder.append(", channelId=");
        builder.append(channelId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", recentUpdateTime=");
        builder.append(recentUpdateTime);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", channelType=");
        builder.append(channelType);
        builder.append(", channelTypeString=");
        builder.append(channelTypeString);
        builder.append(", cmNumTotal=");
        builder.append(cmNumTotal);
        builder.append(", cmNumOnline=");
        builder.append(cmNumOnline);
        builder.append(", cmNumActive=");
        builder.append(cmNumActive);
        builder.append(", cmNumUnregistered=");
        builder.append(cmNumUnregistered);
        builder.append(", cmNumOffline=");
        builder.append(cmNumOffline);
        builder.append(", CmNumRregistered=");
        builder.append(CmNumRregistered);
        builder.append(", channelStatus=");
        builder.append(channelStatus);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", cmcMac=");
        builder.append(cmcMac);
        builder.append(", topCcmtsSysMacAddr=");
        builder.append(topCcmtsSysMacAddr);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", cmcType=");
        builder.append(cmcType);
        builder.append(", cmcPortName=");
        builder.append(cmcPortName);
        builder.append(", displayPortName=");
        builder.append(displayPortName);
        builder.append(", ifType=");
        builder.append(ifType);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", ifName=");
        builder.append(ifName);
        builder.append("]");
        return builder.toString();
    }

}
