/***********************************************************************
 * $ VlanAttribute.java,v1.0 2011-10-25 9:56:54 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.common.ByteUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-10-25-9:56:54
 */
@TableProperty(tables = { "default" })
public class VlanAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -821831341235187373L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.2.1.1.1", index = true)
    private Integer vlanIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.2.1.1.2", index = true)
    private Long deviceNo = 1L;
    /**
     * VLAN组播控制模式
     */
    private Integer topMcFloodMode;
    /**
     * VLAN虚接口是否创建的标识
     */
    private Integer vlanVifFlag;
    /**
     * OLT VLAN name
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.2.1.1.3", writable = true, type = "OctetString")
    private String oltVlanName;
    /**
     * Vlan包括的tag的PON/SNI in OLT。
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.2.1.1.4", writable = true, type = "OctetString")
    private String taggedPort;
    private byte[] taggedPortBytes;
    //private String taggedPort;
    private List<Long> taggedPortIndexList;
    /**
     * Vlan包括的Untag的PON/SNI in OLT。
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.2.1.1.5", writable = true, type = "OctetString")
    private String untaggedPort;
    private byte[] untaggedPortBytes;
    //private String untaggedPort;
    private List<Long> untaggedPortIndexList;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.2.1.1.6", writable = true, type = "Integer32")
    private Integer oltVlanRowStatus;

    private List<String> tagPortNameList;
    private List<String> unTagPortNameList;

    public Long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getOltVlanName() {
        return oltVlanName;
    }

    public void setOltVlanName(String oltVlanName) {
        this.oltVlanName = oltVlanName;
    }

    public Integer getOltVlanRowStatus() {
        return oltVlanRowStatus;
    }

    public void setOltVlanRowStatus(Integer oltVlanRowStatus) {
        this.oltVlanRowStatus = oltVlanRowStatus;
    }

    public String getTaggedPort() {
        return taggedPort;
    }

    public void setTaggedPort(String taggedPort) {
        this.taggedPort = taggedPort;
        taggedPortIndexList = EponUtil.getSniPortIndexFromMib(taggedPort);
    }

    public String getUntaggedPort() {
        return untaggedPort;
    }

    public void setUntaggedPort(String untaggedPort) {
        this.untaggedPort = untaggedPort;
        untaggedPortIndexList = EponUtil.getSniPortIndexFromMib(untaggedPort);
    }

    public Integer getVlanIndex() {
        return vlanIndex;
    }

    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    public List<Long> getTaggedPortIndexList() {
        return taggedPortIndexList;
    }

    public void setTaggedPortIndexList(List<Long> taggedPortIndexList) {
        this.taggedPortIndexList = taggedPortIndexList;
        taggedPort = EponUtil.getMibStringFromSniPortList(taggedPortIndexList);
    }

    public List<Long> getUntaggedPortIndexList() {
        return untaggedPortIndexList;
    }

    public void setUntaggedPortIndexList(List<Long> untaggedPortIndexList) {
        this.untaggedPortIndexList = untaggedPortIndexList;
        untaggedPort = EponUtil.getMibStringFromSniPortList(untaggedPortIndexList);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopMcFloodMode() {
        return topMcFloodMode;
    }

    public void setTopMcFloodMode(Integer topMcFloodMode) {
        this.topMcFloodMode = topMcFloodMode;
    }

    public Integer getVlanVifFlag() {
        return vlanVifFlag;
    }

    public void setVlanVifFlag(Integer vlanVifFlag) {
        this.vlanVifFlag = vlanVifFlag;
    }

    /**
     * @return the taggedPortBytes
     */
    public byte[] getTaggedPortBytes() {
        if (taggedPortBytes == null) {
            taggedPortBytes = ByteUtils.toBytes(taggedPort);
        }
        return taggedPortBytes;
    }

    /**
     * @param taggedPortBytes
     *            the taggedPortBytes to set
     */
    public void setTaggedPortBytes(byte[] taggedPortBytes) {
        this.taggedPortBytes = taggedPortBytes;
        this.taggedPort = ByteUtils.toString(taggedPortBytes);
    }

    /**
     * @return the untaggedPortBytes
     */
    public byte[] getUntaggedPortBytes() {
        if (untaggedPortBytes == null) {
            untaggedPortBytes = ByteUtils.toBytes(untaggedPort);
        }
        return untaggedPortBytes;
    }

    /**
     * @param untaggedPortBytes
     *            the untaggedPortBytes to set
     */
    public void setUntaggedPortBytes(byte[] untaggedPortBytes) {
        this.untaggedPortBytes = untaggedPortBytes;
        this.untaggedPort = ByteUtils.toString(untaggedPortBytes);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VlanAttribute");
        sb.append("{deviceNo=").append(deviceNo);
        sb.append(", vlanIndex=").append(vlanIndex);
        sb.append(", oltVlanName='").append(oltVlanName).append('\'');
        sb.append(", taggedPort='").append(taggedPort).append('\'');
        sb.append(", untaggedPort='").append(untaggedPort).append('\'');
        sb.append(", oltVlanRowStatus=").append(oltVlanRowStatus);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof VlanAttribute))
            return false;

        VlanAttribute that = (VlanAttribute) o;

        if (taggedPort != null ? !taggedPort.equals(that.taggedPort) : that.taggedPort != null)
            return false;
        if (untaggedPort != null ? !untaggedPort.equals(that.untaggedPort) : that.untaggedPort != null)
            return false;
        if (vlanIndex != null ? !vlanIndex.equals(that.vlanIndex) : that.vlanIndex != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (vlanIndex != null ? vlanIndex.hashCode() : 0);
        result = 31 * result + (taggedPort != null ? taggedPort.hashCode() : 0);
        result = 31 * result + (untaggedPort != null ? untaggedPort.hashCode() : 0);
        return result;
    }

    public List<String> getTagPortNameList() {
        return tagPortNameList;
    }

    public void setTagPortNameList(List<String> tagPortNameList) {
        this.tagPortNameList = tagPortNameList;
    }

    public List<String> getUnTagPortNameList() {
        return unTagPortNameList;
    }

    public void setUnTagPortNameList(List<String> unTagPortNameList) {
        this.unTagPortNameList = unTagPortNameList;
    }
}
