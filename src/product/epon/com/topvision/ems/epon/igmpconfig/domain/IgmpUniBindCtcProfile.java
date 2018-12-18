/***********************************************************************
 * $Id: IgmpUniBindCtcProfile.java,v1.0 2016-6-7 下午4:47:04 $
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
 * @created @2016-6-7-下午4:47:04
 * UNI口绑定可控组播模板配置
 */
public class IgmpUniBindCtcProfile implements AliasesSuperType {
    private static final long serialVersionUID = -5388940654509982522L;

    private Long entityId;
    private Long onuId;
    private Long uniIndex;
    private String uniName;
    //CTC模板ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.4.1.1", index = true)
    private Integer profileId;
    //端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.4.1.2", index = true)
    private Integer portType;
    //Slot槽位
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.4.1.3", index = true)
    private Integer slotNo;
    //端口ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.4.1.4", index = true)
    private Integer portNo;
    //Onu ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.4.1.5", index = true)
    private Integer onuNo;
    //Uni ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.4.1.6", index = true)
    private Integer uniNo;
    //行创建
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.4.1.7", writable = true, type = "Integer32")
    private Integer rowStatus;

    private String profileDesc;

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

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
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

    public Integer getPortNo() {
        if (portNo == null && uniIndex != null) {
            portNo = EponIndex.getPonNo(uniIndex).intValue();
        }
        return portNo;
    }

    public void setPortNo(Integer portNo) {
        this.portNo = portNo;
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

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndex(slotNo, portNo, onuNo, uniNo);
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

    public String getProfileDesc() {
        return profileDesc;
    }

    public void setProfileDesc(String profileDesc) {
        this.profileDesc = profileDesc;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpUniBindCtcProfile [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", uniIndex=");
        builder.append(uniIndex);
        builder.append(", profileId=");
        builder.append(profileId);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", onuNo=");
        builder.append(onuNo);
        builder.append(", uniNo=");
        builder.append(uniNo);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append("]");
        return builder.toString();
    }

}
