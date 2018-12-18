/***********************************************************************
 * $Id: IgmpGlobalGroupInfo.java,v1.0 2016-6-16 下午5:07:30 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2016-6-16-下午5:07:30
 *
 */
public class IgmpGlobalGroup implements AliasesSuperType {
    private static final long serialVersionUID = 4898824316194413930L;

    private Long entityId;
    //组播组ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.6.1.1", index = true)
    private Integer groupId;
    //组播组地址ip
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.6.1.2", type = "OctetString")
    private String groupIp;
    //组播vlan
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.6.1.3", type = "Integer32")
    private Integer vlanId;
    //组播组源ip
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.6.1.4", type = "OctetString")
    private String groupSrcIp;
    //组播组状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.6.1.5", type = "OctetString")
    private String globalState;
    private Integer groupState;
    //组播组下行端口列表
    //@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.6.1.6", type = "OctetString")
    private String portList;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupIp() {
        return groupIp;
    }

    public void setGroupIp(String groupIp) {
        this.groupIp = groupIp;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public String getGroupSrcIp() {
        return groupSrcIp;
    }

    public void setGroupSrcIp(String groupSrcIp) {
        this.groupSrcIp = groupSrcIp;
    }

    public Integer getGroupState() {
        if (groupState == null && globalState != null) {
            this.groupState = Integer.parseInt(globalState, 16);
        }
        return groupState;
    }

    public void setGroupState(Integer groupState) {
        this.groupState = groupState;
    }

    public String getPortList() {
        return portList;
    }

    public void setPortList(String portList) {
        this.portList = portList;
    }

    public String getGlobalState() {
        return globalState;
    }

    public void setGlobalState(String globalState) {
        this.globalState = globalState;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpGlobalGroup [entityId=");
        builder.append(entityId);
        builder.append(", groupId=");
        builder.append(groupId);
        builder.append(", groupIp=");
        builder.append(groupIp);
        builder.append(", vlanId=");
        builder.append(vlanId);
        builder.append(", groupSrcIp=");
        builder.append(groupSrcIp);
        builder.append(", groupState=");
        builder.append(groupState);
        builder.append(", portList=");
        builder.append(portList);
        builder.append("]");
        return builder.toString();
    }

}
