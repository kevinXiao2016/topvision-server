/***********************************************************************
 * $Id: IgmpUniVlanTransConfig.java,v1.0 2016-6-7 下午6:39:34 $
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
 * @created @2016-6-7-下午6:39:34
 * UNI端口VLAN转换表配置
 */
public class IgmpUniVlanTrans implements AliasesSuperType {
    private static final long serialVersionUID = 4078361108894090404L;

    private Long entityId;
    private Long onuId;
    private Long uniIndex;
    private String uniName;
    //端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.1", index = true)
    private Integer portType;
    //板卡索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.2", index = true)
    private Integer slotNo;
    //pon口索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.3", index = true)
    private Integer ponNo;
    //onu认证索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.4", index = true)
    private Integer onuNo;
    //uni口索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.5", index = true)
    private Integer uniNo;
    //组播vlan转化表序号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.6", index = true)
    private Integer transIndex;
    //组播vlan转化表Old vlan
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.7", writable = true, type = "Integer32")
    private Integer transOldVlan;
    //组播vlan转化表New vlan
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.8", writable = true, type = "Integer32")
    private Integer transNewVlan;
    //行创建状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.3.1.9", writable = true, type = "Integer32")
    private Integer rowStatus;

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

    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndex(slotNo, ponNo, onuNo, uniNo);
        }
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public String getUniName() {
        if (uniName == null && uniIndex != null) {
            uniName = EponIndex.getUniStringByIndex(uniIndex).toString();
        }
        return uniName;
    }

    public void setUniName(String uniName) {
        this.uniName = uniName;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getSlotNo() {
        if (slotNo == null && uniIndex != null) {
            slotNo = EponIndex.getSlotNo(uniIndex).intValue();
        }
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPonNo() {
        if (ponNo == null && uniIndex != null) {
            ponNo = EponIndex.getPonNo(uniIndex).intValue();
        }
        return ponNo;
    }

    public void setPonNo(Integer ponNo) {
        this.ponNo = ponNo;
    }

    public Integer getOnuNo() {
        if (onuNo == null && uniIndex != null) {
            onuNo = EponIndex.getOnuNo(uniIndex).intValue();
        }
        return onuNo;
    }

    public void setOnuNo(Integer onuNo) {
        this.onuNo = onuNo;
    }

    public Integer getUniNo() {
        if (uniNo == null && uniIndex != null) {
            uniNo = EponIndex.getUniNo(uniIndex).intValue();
        }
        return uniNo;
    }

    public void setUniNo(Integer uniNo) {
        this.uniNo = uniNo;
    }

    public Integer getTransIndex() {
        return transIndex;
    }

    public void setTransIndex(Integer transIndex) {
        this.transIndex = transIndex;
    }

    public Integer getTransOldVlan() {
        return transOldVlan;
    }

    public void setTransOldVlan(Integer transOldVlan) {
        this.transOldVlan = transOldVlan;
    }

    public Integer getTransNewVlan() {
        return transNewVlan;
    }

    public void setTransNewVlan(Integer transNewVlan) {
        this.transNewVlan = transNewVlan;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpUniVlanTrans [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", uniIndex=");
        builder.append(uniIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", ponNo=");
        builder.append(ponNo);
        builder.append(", onuNo=");
        builder.append(onuNo);
        builder.append(", uniNo=");
        builder.append(uniNo);
        builder.append(", transIndex=");
        builder.append(transIndex);
        builder.append(", transOldVlan=");
        builder.append(transOldVlan);
        builder.append(", transNewVlan=");
        builder.append(transNewVlan);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append("]");
        return builder.toString();
    }

}
