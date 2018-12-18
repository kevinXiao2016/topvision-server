/***********************************************************************
 * $Id: OltAuthentication.java,v1.0 2011-9-26 上午09:08:15 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;

/**
 * Onu认证规则
 * 
 * @author zhanglongyang
 * 
 */
public class OltAuthentication implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 4645750861221986478L;
    private Long entityId;
    private Long authId;
    private Long ponId;
    private Long slotId;
    private Long ponIndex;
    private Long onuIndex;
    private Integer authType;
    private Integer authAction;
    private String onuAuthenMacAddress;
    private Integer onuSnMode;
    private String topOnuAuthLogicSn;
    private String topOnuAuthPassword;
    private Integer onuPreType;
    private String onuPreTypeString;
    public OltAuthentication() {
        super();
    }

    /**
     * 添加MAC认证的构造方法
     * 
     * @param mac
     */
    public OltAuthentication(OltAuthenMacInfo mac) {
        this.entityId = mac.getEntityId();
        this.authType = EponConstants.OLT_AUTHEN_MAC;
        this.authAction = mac.getAuthAction();
        this.onuAuthenMacAddress = mac.getOnuAuthenMacAddress();
        this.onuIndex = mac.getOnuIndex();
        this.onuPreType = mac.getOnuPreType();
    }

    /**
     * 添加SN认证的构造方法
     * 
     * @param sn
     */
    public OltAuthentication(OltAuthenSnInfo sn) {
        this.entityId = sn.getEntityId();
        this.authType = EponConstants.OLT_AUTHEN_SN;
        this.onuIndex = sn.getOnuIndex();
        this.topOnuAuthLogicSn = sn.getTopOnuAuthLogicSn();
        this.topOnuAuthPassword = sn.getTopOnuAuthPassword();
        this.onuSnMode = sn.getTopOnuAuthLogicSnMode();
        this.onuPreType = sn.getOnuPreType();
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

    public Integer getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(Integer onuPreType) {
        this.onuPreType = onuPreType;
    }

    /**
     * @return the authId
     */
    public Long getAuthId() {
        return authId;
    }

    /**
     * @param authId
     *            the authId to set
     */
    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    /**
     * @return the ponId
     */
    public Long getPonId() {
        return ponId;
    }

    /**
     * @param ponId
     *            the ponId to set
     */
    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    /**
     * @return the slotId
     */
    public Long getSlotId() {
        return slotId;
    }

    /**
     * @param slotId
     *            the slotId to set
     */
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    /**
     * @return the ponIndex
     */
    public Long getPonIndex() {
        return ponIndex;
    }

    /**
     * @param ponIndex
     *            the ponIndex to set
     */
    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
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
     * @return the authType
     */
    public Integer getAuthType() {
        return authType;
    }

    /**
     * @param authType
     *            the authType to set
     */
    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    /**
     * @return the authAction
     */
    public Integer getAuthAction() {
        return authAction;
    }

    /**
     * @param authAction
     *            the authAction to set
     */
    public void setAuthAction(Integer authAction) {
        this.authAction = authAction;
    }

    /**
     * @return the onuAuthenMacAddress
     */
    public String getOnuAuthenMacAddress() {
        return onuAuthenMacAddress;
    }

    /**
     * @param onuAuthenMacAddress
     *            the onuAuthenMacAddress to set
     */
    public void setOnuAuthenMacAddress(String onuAuthenMacAddress) {
        this.onuAuthenMacAddress = onuAuthenMacAddress;
    }

    /**
     * @return the onuSnMode
     */
    public Integer getOnuSnMode() {
        return onuSnMode;
    }

    /**
     * @param onuSnMode
     *            the onuSnMode to set
     */
    public void setOnuSnMode(Integer onuSnMode) {
        this.onuSnMode = onuSnMode;
    }

    /**
     * @return the topOnuAuthLogicSn
     */
    public String getTopOnuAuthLogicSn() {
        return topOnuAuthLogicSn;
    }

    /**
     * @param topOnuAuthLogicSn
     *            the topOnuAuthLogicSn to set
     */
    public void setTopOnuAuthLogicSn(String topOnuAuthLogicSn) {
        this.topOnuAuthLogicSn = topOnuAuthLogicSn;
    }

    /**
     * @return the topOnuAuthPassword
     */
    public String getTopOnuAuthPassword() {
        return topOnuAuthPassword;
    }

    /**
     * @param topOnuAuthPassword
     *            the topOnuAuthPassword to set
     */
    public void setTopOnuAuthPassword(String topOnuAuthPassword) {
        this.topOnuAuthPassword = topOnuAuthPassword;
    }

    public String getOnuPreTypeString() {
        return onuPreTypeString;
    }

    public void setOnuPreTypeString(String onuPreTypeString) {
        this.onuPreTypeString = onuPreTypeString;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltAuthentication");
        sb.append("{authAction=").append(authAction);
        sb.append(", entityId=").append(entityId);
        sb.append(", authId=").append(authId);
        sb.append(", ponId=").append(ponId);
        sb.append(", slotId=").append(slotId);
        sb.append(", ponIndex=").append(ponIndex);
        sb.append(", onuIndex='").append(onuIndex).append('\'');
        sb.append(", authType=").append(authType);
        sb.append(", onuAuthenMacAddress=").append(onuAuthenMacAddress);
        sb.append(", onuSnMode=").append(onuSnMode);
        sb.append(", onuPreType=").append(onuPreType);
        sb.append(", topOnuAuthLogicSn='").append(topOnuAuthLogicSn).append('\'');
        sb.append(", topOnuAuthPassword='").append(topOnuAuthPassword).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
