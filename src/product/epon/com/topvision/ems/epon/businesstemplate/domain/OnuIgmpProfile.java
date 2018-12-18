/***********************************************************************
 * $Id: OnuIgmpProfile.java,v1.0 2015-12-8 下午3:12:03 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2015-12-8-下午3:12:03
 *
 */
public class OnuIgmpProfile implements AliasesSuperType {
    private static final long serialVersionUID = 3735242217817838717L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.3.1.1", index = true)
    private Integer igmpProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.3.1.2", index = true)
    private Integer igmpPortId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.3.1.3", writable = true, type = "Integer32")
    private Integer igmpMaxGroup;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.3.1.4", writable = true, type = "Integer32")
    private Integer igmpVlanMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.3.1.5", writable = true, type = "Integer32")
    private Integer igmpTransId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.3.1.6", writable = true, type = "OctetString")
    private String igmpVlanList;
    private List<Integer> igmpVlanArray;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.3.1.7", writable = true, type = "Integer32")
    private Integer rowStatus;
    //业务模板绑定能力集和绑定次数数据
    private Integer srvBindCap;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getIgmpProfileId() {
        return igmpProfileId;
    }

    public void setIgmpProfileId(Integer igmpProfileId) {
        this.igmpProfileId = igmpProfileId;
    }

    public Integer getIgmpPortId() {
        return igmpPortId;
    }

    public void setIgmpPortId(Integer igmpPortId) {
        this.igmpPortId = igmpPortId;
    }

    public Integer getIgmpMaxGroup() {
        return igmpMaxGroup;
    }

    public void setIgmpMaxGroup(Integer igmpMaxGroup) {
        this.igmpMaxGroup = igmpMaxGroup;
    }

    public Integer getIgmpVlanMode() {
        return igmpVlanMode;
    }

    public void setIgmpVlanMode(Integer igmpVlanMode) {
        this.igmpVlanMode = igmpVlanMode;
    }

    public Integer getIgmpTransId() {
        return igmpTransId;
    }

    public void setIgmpTransId(Integer igmpTransId) {
        this.igmpTransId = igmpTransId;
    }

    public String getIgmpVlanList() {
        return igmpVlanList;
    }

    public void setIgmpVlanList(String igmpVlanList) {
        this.igmpVlanList = igmpVlanList;
        if (igmpVlanList != null) {
            this.igmpVlanArray = EponUtil.getTwiceBitValueFromOcterString(igmpVlanList);
        }
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public List<Integer> getIgmpVlanArray() {
        return igmpVlanArray;
    }

    public void setIgmpVlanArray(List<Integer> igmpVlanArray) {
        this.igmpVlanArray = igmpVlanArray;
        if (igmpVlanArray != null) {
            this.igmpVlanList = EponUtil.getOcterStringFromTwoByteValueList(igmpVlanArray, 32);
        }
    }

    public Integer getSrvBindCap() {
        return srvBindCap;
    }

    public void setSrvBindCap(Integer srvBindCap) {
        this.srvBindCap = srvBindCap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuIgmpProfile [entityId=");
        builder.append(entityId);
        builder.append(", igmpProfileId=");
        builder.append(igmpProfileId);
        builder.append(", igmpPortId=");
        builder.append(igmpPortId);
        builder.append(", igmpMaxGroup=");
        builder.append(igmpMaxGroup);
        builder.append(", igmpVlanMode=");
        builder.append(igmpVlanMode);
        builder.append(", igmpTransId=");
        builder.append(igmpTransId);
        builder.append(", igmpVlanList=");
        builder.append(igmpVlanList);
        builder.append(", igmpVlanArray=");
        builder.append(igmpVlanArray);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append(", srvBindCap=");
        builder.append(srvBindCap);
        builder.append("]");
        return builder.toString();
    }
}
