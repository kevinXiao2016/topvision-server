/***********************************************************************
 * $Id: TopOnuIfPotsInfo.java,v1.0 2017年6月22日 上午10:59:41 $
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
 * @created @2017年6月22日-上午10:59:41
 *
 */
public class TopOnuIfPotsInfo implements AliasesSuperType {
    private static final long serialVersionUID = -1086472424718176161L;
    private Long entityId;
    private Long onuId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.4.1.1", index = true)
    private Integer topOnuIfPotsSlotIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.4.1.2", index = true)
    private Integer topOnuIfPotsPortIndx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.4.1.3", index = true)
    private Integer topOnuIfPotsOnuIdx;
    private Long onuIndex;// 网管用onuIndex
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.4.1.4", index = true)
    private Integer topOnuIfPotsPotsIdx;// 端口号

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.4.1.5", writable = true, type = "Integer32")
    private Integer topOnuIfPotsAdminState;// 管理状态

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

    public Integer getTopOnuIfPotsSlotIdx() {
        return topOnuIfPotsSlotIdx;
    }

    public void setTopOnuIfPotsSlotIdx(Integer topOnuIfPotsSlotIdx) {
        this.topOnuIfPotsSlotIdx = topOnuIfPotsSlotIdx;
    }

    public Integer getTopOnuIfPotsPortIndx() {
        return topOnuIfPotsPortIndx;
    }

    public void setTopOnuIfPotsPortIndx(Integer topOnuIfPotsPortIndx) {
        this.topOnuIfPotsPortIndx = topOnuIfPotsPortIndx;
    }

    public Integer getTopOnuIfPotsOnuIdx() {
        return topOnuIfPotsOnuIdx;
    }

    public void setTopOnuIfPotsOnuIdx(Integer topOnuIfPotsOnuIdx) {
        this.topOnuIfPotsOnuIdx = topOnuIfPotsOnuIdx;
    }

    public Long getOnuIndex() {
        if (onuIndex != null) {
            return onuIndex;
        }
        return EponIndex.getOnuIndex(topOnuIfPotsSlotIdx, topOnuIfPotsPortIndx, topOnuIfPotsOnuIdx);
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            topOnuIfPotsSlotIdx = EponIndex.getSlotNo(onuIndex).intValue();
            topOnuIfPotsPortIndx = EponIndex.getPonNo(onuIndex).intValue();
            topOnuIfPotsOnuIdx = EponIndex.getOnuNo(onuIndex).intValue();
        }
    }

    public Integer getTopOnuIfPotsPotsIdx() {
        return topOnuIfPotsPotsIdx;
    }

    public void setTopOnuIfPotsPotsIdx(Integer topOnuIfPotsPotsIdx) {
        this.topOnuIfPotsPotsIdx = topOnuIfPotsPotsIdx;
    }

    public Integer getTopOnuIfPotsAdminState() {
        return topOnuIfPotsAdminState;
    }

    public void setTopOnuIfPotsAdminState(Integer topOnuIfPotsAdminState) {
        this.topOnuIfPotsAdminState = topOnuIfPotsAdminState;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOnuIfPotsInfo [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", topOnuIfPotsSlotIdx=");
        builder.append(topOnuIfPotsSlotIdx);
        builder.append(", topOnuIfPotsPortIndx=");
        builder.append(topOnuIfPotsPortIndx);
        builder.append(", topOnuIfPotsOnuIdx=");
        builder.append(topOnuIfPotsOnuIdx);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topOnuIfPotsPotsIdx=");
        builder.append(topOnuIfPotsPotsIdx);
        builder.append(", topOnuIfPotsAdminState=");
        builder.append(topOnuIfPotsAdminState);
        builder.append("]");
        return builder.toString();
    }

}
