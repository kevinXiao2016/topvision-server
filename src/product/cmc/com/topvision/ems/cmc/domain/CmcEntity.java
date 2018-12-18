/***********************************************************************
 * $Id: CmcEntity.java,v1.0 2011-10-25 上午11:21:05 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-10-25-上午11:21:05
 * 
 */
@Alias("cmcEntity")
public class CmcEntity implements java.io.Serializable, AliasesSuperType {
    private static final long serialVersionUID = 5685497818253425901L;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long entityId;
    private Long virtualNetId;
    private Long onuId;
    private Long onuIndex;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmcType;
    private Long cmcEntityId;
    private String ipAddress;
    private Long macAddress;
    private String cmcIndexString;//解析索引，生成slot/pon/cc形式
    private Timestamp createTime;
    private Boolean state;
    private String macAddr;

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
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId
     *            the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
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
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
        this.cmcIndexString = CmcIndexUtils.getSlotNo(cmcIndex).toString() + "/" + CmcIndexUtils.getPonNo(cmcIndex)
                + "/" + CmcIndexUtils.getCmcId(cmcIndex);
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress
     *            the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the macAddress
     */
    public Long getMacAddress() {
        return macAddress;
    }

    /**
     * @param macAddress
     *            the macAddress to set
     */
    public void setMacAddress(Long macAddress) {
        this.macAddress = macAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    public Long getVirtualNetId() {
        return virtualNetId;
    }

    public void setVirtualNetId(Long virtualNetId) {
        this.virtualNetId = virtualNetId;
    }

    public Long getCmcType() {
        return cmcType;
    }

    public void setCmcType(Long cmcType) {
        this.cmcType = cmcType;
    }

    public Long getCmcEntityId() {
        return cmcEntityId;
    }

    public void setCmcEntityId(Long cmcEntityId) {
        this.cmcEntityId = cmcEntityId;
    }

    public String getCmcIndexString() {
        return cmcIndexString;
    }

    public void setCmcIndexString(String cmcIndexString) {
        this.cmcIndexString = cmcIndexString;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeString() {
        if (createTime != null) {
            return sdf.format(createTime);
        } else {
            return "";
        }
    }

    public Boolean isState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcEntity [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", ipAddress=");
        builder.append(ipAddress);
        builder.append(", macAddress=");
        builder.append(macAddress);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
