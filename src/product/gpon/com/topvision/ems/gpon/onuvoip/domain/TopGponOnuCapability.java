/***********************************************************************
 * $Id: TopGponOnuCapability.java,v1.0 2017年5月9日 下午6:59:35 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author haojie
 * @created @2017年5月9日-下午6:59:35
 *
 */
public class TopGponOnuCapability implements AliasesSuperType {
    private static final long serialVersionUID = 4953686843709052740L;
    private Long entityId;
    private Long onuId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.2.1.1", index = true)
    private Integer topGponOnuCapCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.2.1.2", index = true)
    private Integer topGponOnuCapPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.2.1.3", index = true)
    private Integer topGponOnuCapOnuIndex;
    private Long onuIndex;// 网管用onuIndex
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.2.1.4", type = "Integer32")
    private Integer topGponOnuCapOnuPotsNum;// pots口数量0-2

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Integer getTopGponOnuCapCardIndex() {
        return topGponOnuCapCardIndex;
    }

    public void setTopGponOnuCapCardIndex(Integer topGponOnuCapCardIndex) {
        this.topGponOnuCapCardIndex = topGponOnuCapCardIndex;
    }

    public Integer getTopGponOnuCapPonIndex() {
        return topGponOnuCapPonIndex;
    }

    public void setTopGponOnuCapPonIndex(Integer topGponOnuCapPonIndex) {
        this.topGponOnuCapPonIndex = topGponOnuCapPonIndex;
    }

    public Integer getTopGponOnuCapOnuIndex() {
        return topGponOnuCapOnuIndex;
    }

    public void setTopGponOnuCapOnuIndex(Integer topGponOnuCapOnuIndex) {
        this.topGponOnuCapOnuIndex = topGponOnuCapOnuIndex;
    }

    public Long getOnuIndex() {
        if (onuIndex != null) {
            return onuIndex;
        }
        return EponIndex.getOnuIndex(topGponOnuCapCardIndex, topGponOnuCapPonIndex, topGponOnuCapOnuIndex);
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            topGponOnuCapCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
            topGponOnuCapPonIndex = EponIndex.getPonNo(onuIndex).intValue();
            topGponOnuCapOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
        }
    }

    public Integer getTopGponOnuCapOnuPotsNum() {
        return topGponOnuCapOnuPotsNum;
    }

    public void setTopGponOnuCapOnuPotsNum(Integer topGponOnuCapOnuPotsNum) {
        this.topGponOnuCapOnuPotsNum = topGponOnuCapOnuPotsNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopGponOnuCapability [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", topGponOnuCapCardIndex=");
        builder.append(topGponOnuCapCardIndex);
        builder.append(", topGponOnuCapPonIndex=");
        builder.append(topGponOnuCapPonIndex);
        builder.append(", topGponOnuCapOnuIndex=");
        builder.append(topGponOnuCapOnuIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topGponOnuCapOnuPotsNum=");
        builder.append(topGponOnuCapOnuPotsNum);
        builder.append("]");
        return builder.toString();
    }

}
