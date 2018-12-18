/***********************************************************************
 * $Id: OltTopOnuProductTable.java,v1.0 2012-6-13 下午03:31:39 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.util.Map;

import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-6-13-下午03:31:39
 * 
 */

public class OltTopOnuProductTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1305800037052495720L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private Integer onuType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.1", index = true)
    private Integer topOnuProductCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.2", index = true)
    private Integer topOnuProductPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.3", index = true)
    private Integer topOnuProductOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.4", writable = true, type = "Integer32")
    private Integer topOnuProductTypeNum;
    private String topOnuProductType;

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
        if (topOnuProductCardIndex != null && topOnuProductPonIndex != null && topOnuProductOnuIndex != null) {
            onuIndex = EponIndex.getOnuIndex(topOnuProductCardIndex, topOnuProductPonIndex, topOnuProductOnuIndex);
        }
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        this.topOnuProductCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
        this.topOnuProductPonIndex = EponIndex.getPonNo(onuIndex).intValue();
        this.topOnuProductOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
    }

    /**
     * @return the topOnuProductCardIndex
     */
    public Integer getTopOnuProductCardIndex() {
        return topOnuProductCardIndex;
    }

    /**
     * @param topOnuProductCardIndex
     *            the topOnuProductCardIndex to set
     */
    public void setTopOnuProductCardIndex(Integer topOnuProductCardIndex) {
        this.topOnuProductCardIndex = topOnuProductCardIndex;
    }

    /**
     * @return the topOnuProductPonIndex
     */
    public Integer getTopOnuProductPonIndex() {
        return topOnuProductPonIndex;
    }

    /**
     * @param topOnuProductPonIndex
     *            the topOnuProductPonIndex to set
     */
    public void setTopOnuProductPonIndex(Integer topOnuProductPonIndex) {
        this.topOnuProductPonIndex = topOnuProductPonIndex;
    }

    /**
     * @return the topOnuProductOnuIndex
     */
    public Integer getTopOnuProductOnuIndex() {
        return topOnuProductOnuIndex;
    }

    /**
     * @param topOnuProductOnuIndex
     *            the topOnuProductOnuIndex to set
     */
    public void setTopOnuProductOnuIndex(Integer topOnuProductOnuIndex) {
        this.topOnuProductOnuIndex = topOnuProductOnuIndex;
    }

    /**
     * @return the topOnuProductType
     */
    public String getTopOnuProductType() {
        return topOnuProductType;
    }

    /**
     * @param topOnuProductType
     *            the topOnuProductType to set
     */
    public void setTopOnuProductType(String topOnuProductType) {
        this.topOnuProductType = topOnuProductType;
        if (topOnuProductType != null) {
            this.topOnuProductTypeNum = 0;
            if (EponConstants.EPON_ONU_PRETYPE.get(topOnuProductType) != null) {
                this.topOnuProductTypeNum = EponConstants.EPON_ONU_PRETYPE.get(topOnuProductType);
            }
        }
    }

    public Integer getOnuType() {
        return onuType;
    }

    public void setOnuType(Integer onuType) {
        this.onuType = onuType;
    }

    public Integer getTopOnuProductTypeNum() {
        return topOnuProductTypeNum;
    }

    public void setTopOnuProductTypeNum(Integer topOnuProductTypeNum) {
        this.topOnuProductTypeNum = topOnuProductTypeNum;
        // TODO
        this.onuType = topOnuProductTypeNum;
        if (topOnuProductTypeNum != null) {
            this.topOnuProductType = "255";
            for (Map.Entry<String, Integer> s : EponConstants.EPON_ONU_PRETYPE.entrySet()) {
                if (s.getValue().equals(topOnuProductTypeNum)) {
                    this.topOnuProductType = s.getKey();
                    break;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltTopOnuProductTable [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topOnuProductCardIndex=");
        builder.append(topOnuProductCardIndex);
        builder.append(", topOnuProductPonIndex=");
        builder.append(topOnuProductPonIndex);
        builder.append(", topOnuProductOnuIndex=");
        builder.append(topOnuProductOnuIndex);
        builder.append(", topOnuProductType=");
        builder.append(topOnuProductType);
        builder.append("]");
        return builder.toString();
    }

}
