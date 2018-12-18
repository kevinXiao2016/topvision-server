/***********************************************************************
 * $Id: OltAuthenSnInfo.java,v1.0 2011-10-18 上午11:15:02 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-10-18-上午11:15:02
 * 
 */
public class OltAuthenSnInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -492197466379426127L;
    private Long entityId;
    private Long onuId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.1.1.1", index = true)
    private Integer topOnuAuthLogicSnCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.1.1.2", index = true)
    private Integer topOnuAuthLogicSnPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.1.1.3", index = true)
    private Integer topOnuAuthLogicSnOnuIndex;
    private Long onuIndex; // 系统中所用Index
    // 该字段改为可读，由password是否为空决定
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.1.1.4")
    private Integer topOnuAuthLogicSnMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.1.1.5", writable = true, type = "OctetString")
    private String topOnuAuthLogicSn;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.1.1.6", writable = true, type = "OctetString")
    private String topOnuAuthPassword;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.1.1.7", writable = true, type = "Integer32")
    private Integer topOnuAuthLogicSnAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.1.1.8", writable = true, type = "Integer32")
    private Integer topOnuAuthLogicSnRowStatus;
    private Integer onuPreType;

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
     * @return the topOnuAuthLogicSnCardIndex
     */
    public Integer getTopOnuAuthLogicSnCardIndex() {
        return topOnuAuthLogicSnCardIndex;
    }

    /**
     * @param topOnuAuthLogicSnCardIndex
     *            the topOnuAuthLogicSnCardIndex to set
     */
    public void setTopOnuAuthLogicSnCardIndex(Integer topOnuAuthLogicSnCardIndex) {
        this.topOnuAuthLogicSnCardIndex = topOnuAuthLogicSnCardIndex;
    }

    /**
     * @return the topOnuAuthLogicSnPonIndex
     */
    public Integer getTopOnuAuthLogicSnPonIndex() {
        return topOnuAuthLogicSnPonIndex;
    }

    /**
     * @param topOnuAuthLogicSnPonIndex
     *            the topOnuAuthLogicSnPonIndex to set
     */
    public void setTopOnuAuthLogicSnPonIndex(Integer topOnuAuthLogicSnPonIndex) {
        this.topOnuAuthLogicSnPonIndex = topOnuAuthLogicSnPonIndex;
    }

    /**
     * @return the topOnuAuthLogicSnOnuIndex
     */
    public Integer getTopOnuAuthLogicSnOnuIndex() {
        return topOnuAuthLogicSnOnuIndex;
    }

    /**
     * @param topOnuAuthLogicSnOnuIndex
     *            the topOnuAuthLogicSnOnuIndex to set
     */
    public void setTopOnuAuthLogicSnOnuIndex(Integer topOnuAuthLogicSnOnuIndex) {
        this.topOnuAuthLogicSnOnuIndex = topOnuAuthLogicSnOnuIndex;
    }

    /**
     * @return the topOnuAuthLogicSnMode
     */
    public Integer getTopOnuAuthLogicSnMode() {
        return topOnuAuthLogicSnMode;
    }

    /**
     * @param topOnuAuthLogicSnMode
     *            the topOnuAuthLogicSnMode to set
     */
    public void setTopOnuAuthLogicSnMode(Integer topOnuAuthLogicSnMode) {
        this.topOnuAuthLogicSnMode = topOnuAuthLogicSnMode;
    }

    /**
     * @return the topOnuAuthLogicSn
     */
    public String getTopOnuAuthLogicSn() {
        return topOnuAuthLogicSn == "" ? null : topOnuAuthLogicSn;
    }

    /**
     * @param topOnuAuthLogicSn
     *            the topOnuAuthLogicSn to set
     */
    public void setTopOnuAuthLogicSn(String topOnuAuthLogicSn) {
        if (topOnuAuthLogicSn != null && topOnuAuthLogicSn.equalsIgnoreCase("00:00:00:00:00:00:00:00:00:00:00:00")) {
            topOnuAuthLogicSn = null;
        }
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

    /**
     * @return the topOnuAuthLogicSnAction
     */
    public Integer getTopOnuAuthLogicSnAction() {
        return topOnuAuthLogicSnAction;
    }

    /**
     * @param topOnuAuthLogicSnAction
     *            the topOnuAuthLogicSnAction to set
     */
    public void setTopOnuAuthLogicSnAction(Integer topOnuAuthLogicSnAction) {
        this.topOnuAuthLogicSnAction = topOnuAuthLogicSnAction;
    }

    /**
     * @return the topOnuAuthLogicSnRowStatus
     */
    public Integer getTopOnuAuthLogicSnRowStatus() {
        return topOnuAuthLogicSnRowStatus;
    }

    /**
     * @param topOnuAuthLogicSnRowStatus
     *            the topOnuAuthLogicSnRowStatus to set
     */
    public void setTopOnuAuthLogicSnRowStatus(Integer topOnuAuthLogicSnRowStatus) {
        this.topOnuAuthLogicSnRowStatus = topOnuAuthLogicSnRowStatus;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        if (onuIndex == null) {
            onuIndex = new EponIndex(topOnuAuthLogicSnCardIndex, topOnuAuthLogicSnPonIndex, topOnuAuthLogicSnOnuIndex)
                    .getOnuIndex();
        }
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuAuthLogicSnCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
        topOnuAuthLogicSnPonIndex = EponIndex.getPonNo(onuIndex).intValue();
        topOnuAuthLogicSnOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
    }

    /**
     * @return the onuPreType
     */
    public Integer getOnuPreType() {
        return onuPreType;
    }

    /**
     * @param onuPreType the onuPreType to set
     */
    public void setOnuPreType(Integer onuPreType) {
        this.onuPreType = onuPreType;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltAuthenSnInfo [entityId=");
        builder.append(entityId);
        builder.append(", topOnuAuthLogicSnCardIndex=");
        builder.append(topOnuAuthLogicSnCardIndex);
        builder.append(", topOnuAuthLogicSnPonIndex=");
        builder.append(topOnuAuthLogicSnPonIndex);
        builder.append(", topOnuAuthLogicSnOnuIndex=");
        builder.append(topOnuAuthLogicSnOnuIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topOnuAuthLogicSnMode=");
        builder.append(topOnuAuthLogicSnMode);
        builder.append(", topOnuAuthLogicSn=");
        builder.append(topOnuAuthLogicSn);
        builder.append(", topOnuAuthPassword=");
        builder.append(topOnuAuthPassword);
        builder.append(", topOnuAuthLogicSnAction=");
        builder.append(topOnuAuthLogicSnAction);
        builder.append(", topOnuAuthLogicSnRowStatus=");
        builder.append(topOnuAuthLogicSnRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
