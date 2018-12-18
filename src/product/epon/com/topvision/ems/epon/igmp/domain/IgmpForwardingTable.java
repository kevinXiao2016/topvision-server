/***********************************************************************
 * $ IgmpForwardingTable.java,v1.0 2011-11-23 9:07:18 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-9:07:18
 */
@TableProperty(tables = { "default" })
public class IgmpForwardingTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 设备索引号 For OLT, set to corresponding device/slot/port For ONU, set to 0
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.3.1.1", index = true)
    private Long deviceIndex;
    /**
     * Vlan索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.3.1.2", index = true)
    private Long groupVlanIndex;

    /**
     * 组Mac地址索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.3.1.3", index = true)
    private String macMibIndex;
    private Long macLongDB;

    /**
     * Forwarding port list 1. OLT
     * ------------------------------------------------------------------- It is presented by member
     * port list. Each member port is identified by 4-byte format, same as TC of EponDeviceIndex.
     * The actual length of this string depends of the number of member ports in the port list. The
     * relationship is: Length of the string = 4 * (NUM of member ports) For example, if device 1,
     * port 1/2 and 2/3 is the member ports, then this object will be presented as: 01 01 02 00 01
     * 02 03 00
     * 
     * 2. ONU ------------------------------------------------------------------- It is presented by
     * member port list. Each member port is identified by 2-byte format, One for slot ID, the last
     * for port ID. The actual length of this string depends of the number of member ports. If the
     * ONU is fixed (not modulized), set slot ID to 0, the NMS could ignore the slot info. Note
     * that, slot here shall follow the last byte definition of EponCardIndex to indicate the
     * main-slot and sub-slot index. The relationship is: Length of the string = 2 * (NUM of member
     * ports) For example, if port 1/2 and 3/4 is the member ports of the group, then the object is
     * presented as: 01 02 03 04
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.3.1.4", writable = true, type = "OctetString")
    private String groupPortList;
    private List<Long> groupPortIndexList;

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getGroupPortList() {
        return groupPortList;
    }

    public void setGroupPortList(String groupPortList) {
        this.groupPortList = groupPortList;
        groupPortIndexList = EponUtil.getSniPortIndexFromMib(groupPortList);
    }

    /**
     * @return the groupPortIndexList
     */
    public List<Long> getGroupPortIndexList() {
        return groupPortIndexList;
    }

    /**
     * @param groupPortIndexList
     *            the groupPortIndexList to set
     */
    public void setGroupPortIndexList(List<Long> groupPortIndexList) {
        this.groupPortIndexList = groupPortIndexList;
        groupPortList = EponUtil.getMibStringFromSniPortList(groupPortIndexList);
    }

    public Long getGroupVlanIndex() {
        return groupVlanIndex;
    }

    public void setGroupVlanIndex(Long groupVlanIndex) {
        this.groupVlanIndex = groupVlanIndex;
    }

    public Long getMacLongDB() {
        return macLongDB;
    }

    public void setMacLongDB(Long macLongDB) {
        this.macLongDB = macLongDB;
        macMibIndex = EponUtil.getMibMacFromHexMac(new MacUtils(macLongDB).toString(MacUtils.MAOHAO).toUpperCase());
    }

    public String getMacMibIndex() {
        return macMibIndex;
    }

    public void setMacMibIndex(String macMibIndex) {
        this.macMibIndex = macMibIndex;
        macLongDB = new MacUtils(EponUtil.getMacFromMibMac(macMibIndex)).longValue();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpForwardingTable");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", entityId=").append(entityId);
        sb.append(", groupVlanIndex=").append(groupVlanIndex);
        sb.append(", macMibIndex='").append(macMibIndex).append('\'');
        sb.append(", macLongDB=").append(macLongDB);
        sb.append(", groupPortList='").append(groupPortList).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
