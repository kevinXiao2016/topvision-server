/***********************************************************************
 * $Id: IgmpOnuConfig.java,v1.0 2016-6-7 下午5:14:17 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016-6-7-下午5:14:17
 * ONU IGMP配置
 */
public class IgmpOnuConfig implements AliasesSuperType {
    private static final long serialVersionUID = 3311616481013205948L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private String onuName;
    //端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.1", index = true)
    private Integer portType;
    //槽位索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.2", index = true)
    private Integer slotNo;
    //pon口索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.3", index = true)
    private Integer ponNo;
    //onu口索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.4", index = true)
    private Integer onuNo;
    //onu组播模式
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.5", writable = true, type = "Integer32")
    private Integer onuMode;
    //onu快速离开使能
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.6", writable = true, type = "Integer32")
    private Integer onuFastLeave;

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

    public Long getOnuIndex() {
        if (onuIndex == null) {
            onuIndex = EponIndex.getOnuIndex(slotNo, ponNo, onuNo);
        }
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public String getOnuName() {
        if (onuName == null && onuIndex != null) {
            onuName = EponIndex.getOnuStringByIndex(onuIndex).toString();
        }
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getSlotNo() {
        if (slotNo == null && onuIndex != null) {
            slotNo = EponIndex.getSlotNo(onuIndex).intValue();
        }
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPonNo() {
        if (ponNo == null && onuIndex != null) {
            ponNo = EponIndex.getPonNo(onuIndex).intValue();
        }
        return ponNo;
    }

    public void setPonNo(Integer ponNo) {
        this.ponNo = ponNo;
    }

    public Integer getOnuNo() {
        if (onuNo == null && onuIndex != null) {
            onuNo = EponIndex.getOnuNo(onuIndex).intValue();
        }
        return onuNo;
    }

    public void setOnuNo(Integer onuNo) {
        this.onuNo = onuNo;
    }

    public Integer getOnuMode() {
        return onuMode;
    }

    public void setOnuMode(Integer onuMode) {
        this.onuMode = onuMode;
    }

    public Integer getOnuFastLeave() {
        return onuFastLeave;
    }

    public void setOnuFastLeave(Integer onuFastLeave) {
        this.onuFastLeave = onuFastLeave;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpOnuConfig [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", ponNo=");
        builder.append(ponNo);
        builder.append(", onuNo=");
        builder.append(onuNo);
        builder.append(", onuMode=");
        builder.append(onuMode);
        builder.append(", onuFastLeave=");
        builder.append(onuFastLeave);
        builder.append("]");
        return builder.toString();
    }

}
