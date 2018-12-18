/***********************************************************************
 * $Id: IgmpGroupInfo.java,v1.0 2016-6-7 下午3:27:27 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.IpsAddress;

/**
 * @author flack
 * @created @2016-6-7-下午3:27:27
 * IGMP组播组配置
 */
public class IgmpVlanGroup implements AliasesSuperType {
    private static final long serialVersionUID = -3716954848619059442L;

    private Long entityId;
    //组播vlanID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.2.1.1", index = true)
    private Integer vlanId;
    //组播组ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.2.1.2", index = true)
    private Integer groupId;
    //组播组地址ip
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.2.1.3", index = true)
    private IpsAddress groupIpIndex;
    private String groupIp;
    //组播组源ip
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.2.1.4", index = true)
    private IpsAddress srcIpIndex;
    private String groupSrcIp;
    //组播组描述
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.2.1.5", writable = true, type = "OctetString")
    private String groupDesc;
    //组播组最大带宽
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.2.1.6", writable = true, type = "Integer32")
    private Integer groupMaxBW;
    //组播组预加入
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.2.1.7", writable = true, type = "Integer32")
    private Integer joinMode;
    //行创建
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.2.1.8", writable = true, type = "Integer32")
    private Integer rowStatus;

    //组播组别名,由网管维护
    private String groupName;

    //组播组状态
    private Integer groupState;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupIp() {
        if (groupIp == null && groupIpIndex != null) {
            this.groupIp = groupIpIndex.toString();
        }
        return groupIp;
    }

    public void setGroupIp(String groupIp) {
        this.groupIp = groupIp;
    }

    public String getGroupSrcIp() {
        if (groupSrcIp == null && srcIpIndex != null) {
            this.groupSrcIp = srcIpIndex.toString();
        }
        return groupSrcIp;
    }

    public void setGroupSrcIp(String groupSrcIp) {
        this.groupSrcIp = groupSrcIp;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public Integer getGroupMaxBW() {
        return groupMaxBW;
    }

    public void setGroupMaxBW(Integer groupMaxBW) {
        this.groupMaxBW = groupMaxBW;
    }

    public Integer getJoinMode() {
        return joinMode;
    }

    public void setJoinMode(Integer joinMode) {
        this.joinMode = joinMode;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public IpsAddress getGroupIpIndex() {
        if (groupIpIndex == null && groupIp != null) {
            this.groupIpIndex = new IpsAddress(groupIp);
        }
        return groupIpIndex;
    }

    public void setGroupIpIndex(IpsAddress groupIpIndex) {
        this.groupIpIndex = groupIpIndex;
    }

    public IpsAddress getSrcIpIndex() {
        if (srcIpIndex == null && groupSrcIp != null) {
            this.srcIpIndex = new IpsAddress(groupSrcIp);
        }
        return srcIpIndex;
    }

    public void setSrcIpIndex(IpsAddress srcIpIndex) {
        this.srcIpIndex = srcIpIndex;
    }

    public Integer getGroupState() {
        return groupState;
    }

    public void setGroupState(Integer groupState) {
        this.groupState = groupState;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpVlanGroup [entityId=");
        builder.append(entityId);
        builder.append(", vlanId=");
        builder.append(vlanId);
        builder.append(", groupId=");
        builder.append(groupId);
        builder.append(", groupIpIndex=");
        builder.append(groupIpIndex);
        builder.append(", groupIp=");
        builder.append(groupIp);
        builder.append(", srcIpIndex=");
        builder.append(srcIpIndex);
        builder.append(", groupSrcIp=");
        builder.append(groupSrcIp);
        builder.append(", groupDesc=");
        builder.append(groupDesc);
        builder.append(", groupMaxBW=");
        builder.append(groupMaxBW);
        builder.append(", joinMode=");
        builder.append(joinMode);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append(", groupName=");
        builder.append(groupName);
        builder.append(", groupState=");
        builder.append(groupState);
        builder.append("]");
        return builder.toString();
    }

}
