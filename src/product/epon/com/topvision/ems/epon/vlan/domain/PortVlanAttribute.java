/***********************************************************************
 * $ PortVlanAttribute.java,v1.0 2011-10-25 9:07:53 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author jay
 * @created @2011-10-25-9:07:53
 */
public class PortVlanAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2298135248797046294L;

    public static final Integer TRANSPARENT = 0;
    public static final Integer TAG = 1;
    public static final Integer TRANSLATION = 2;
    public static final Integer AGGREGATION = 3;
    public static final Integer TRUNK = 4;
    public static final Integer STACKING = 5;
    public static final Map<String, Integer> vlanModeMap = new HashMap<String, Integer>();
    static {
        vlanModeMap.put("TRANSPARENT", TRANSPARENT);
        vlanModeMap.put("TAG", TAG);
        vlanModeMap.put("TRANSLATION", TRANSLATION);
        vlanModeMap.put("AGGREGATION", AGGREGATION);
        vlanModeMap.put("TRUNK", TRUNK);
        // vlanModeMap.put("STACKING", STACKING);
    }

    // uni vlan 模板 模式
    public static final Integer PROFILE_TRANSPARENT = 1;
    public static final Integer PROFILE_TAG = 2;
    public static final Integer PROFILE_TRANSLATION = 3;
    public static final Integer PROFILE_AGGREGATION = 4;
    public static final Integer PROFILE_TRUNK = 5;
    public static final Map<String, Integer> vlanModeProfileMap = new HashMap<String, Integer>();
    static {
        vlanModeProfileMap.put("TRANSPARENT", PROFILE_TRANSPARENT);
        vlanModeProfileMap.put("TAG", PROFILE_TAG);
        vlanModeProfileMap.put("TRANSLATION", PROFILE_TRANSLATION);
        vlanModeProfileMap.put("AGGREGATION", PROFILE_AGGREGATION);
        vlanModeProfileMap.put("TRUNK", PROFILE_TRUNK);
    }
    
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.1.1.1", index = true)
    private Long deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.1.1.2", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.1.1.3", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    private String portString;
    private String portName;
    private Integer portStatus;
    /**
     * 标记的协议标识
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.1.1.4", writable = true, type = "OctetString")
    private String vlanTagTpid;
    private String vlanTagTpidString;
    /**
     * 规则格式指示符
     */
    // 该节点确认没有意义
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.1.1.5", writable = true, type = "Integer32")
    private Integer vlanTagCfi = 0;
    /**
     * vlan优先级
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.1.1.6", writable = true, type = "Integer32")
    private Integer vlanTagPriority;
    /**
     * port Vlan ID
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.1.1.7", writable = true, type = "Integer32")
    private Integer vlanPVid;
    /**
     * vlan模式
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.1.1.8", writable = true, type = "Integer32")
    private Integer vlanMode;

    /**
     * @return the deviceIndex
     */
    public Long getDeviceIndex() {
        if (deviceIndex == null) {
            deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
        }
        return deviceIndex;
    }

    /**
     * @param deviceIndex
     *            the deviceIndex to set
     */
    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Long getPortIndex() {
        if (portIndex == null) {
            if (EponIndex.getOnuNoByMibDeviceIndex(deviceIndex) == 0) {
                // 此时代表为OLT
                portIndex = EponIndex.getPonIndexByMibDeviceIndex(deviceIndex);
            } else {
                portIndex = EponIndex.getUniIndexByMibIndex(deviceIndex, slotNo, portNo);
            }
        }
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        Long onuNo = EponIndex.getOnuNo(portIndex);
        if (onuNo == 0) {
            deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
            slotNo = EponIndex.getSlotNo(portIndex);
            portNo = 0L;
        } else {
            deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
            slotNo = EponIndex.getOnuCardNo(portIndex);
            portNo = EponIndex.getUniNo(portIndex);
        }
    }

    public Long getPortNo() {
        return portNo;
    }

    public void setPortNo(Long portNo) {
        this.portNo = portNo;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    public Integer getVlanPVid() {
        return vlanPVid;
    }

    public void setVlanPVid(Integer vlanPVid) {
        this.vlanPVid = vlanPVid;
    }

    public Integer getVlanTagCfi() {
        return vlanTagCfi;
    }

    public void setVlanTagCfi(Integer vlanTagCfi) {
        this.vlanTagCfi = vlanTagCfi;
    }

    public Integer getVlanTagPriority() {
        return vlanTagPriority;
    }

    public void setVlanTagPriority(Integer vlanTagPriority) {
        this.vlanTagPriority = vlanTagPriority;
    }

    public String getVlanTagTpid() {
        return vlanTagTpid;
    }

    public void setVlanTagTpid(String vlanTagTpid) {
        this.vlanTagTpid = EponUtil.getMacStringFromNoISOControl(vlanTagTpid);
        if (this.vlanTagTpid != null) {
            vlanTagTpidString = "0x" + this.vlanTagTpid.substring(0, 2) + this.vlanTagTpid.substring(3, 5);
        }
    }

    /**
     * @return the portId
     */
    public Long getPortId() {
        return portId;
    }

    /**
     * @param portId
     *            the portId to set
     */
    public void setPortId(Long portId) {
        this.portId = portId;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the portString
     */
    public String getPortString() {
        return portString;
    }

    /**
     * @param portString
     *            the portString to set
     */
    public void setPortString(String portString) {
        this.portString = portString;
    }

    /**
     * @return the vlanTagTpidString
     */
    public String getVlanTagTpidString() {
        return vlanTagTpidString;
    }

    /**
     * @param vlanTagTpidString
     *            the vlanTagTpidString to set
     */
    public void setVlanTagTpidString(String vlanTagTpidString) {
        this.vlanTagTpidString = vlanTagTpidString;
        if (vlanTagTpidString != null) {
            vlanTagTpid = vlanTagTpidString.substring(2, 4) + ":" + vlanTagTpidString.substring(4, 6);
        }
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Integer getPortStatus() {
        return portStatus;
    }

    public void setPortStatus(Integer portStatus) {
        this.portStatus = portStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortVlanAttribute [deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portId=");
        builder.append(portId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", portString=");
        builder.append(portString);
        builder.append(", vlanTagTpid=");
        builder.append(vlanTagTpid);
        builder.append(", vlanTagTpidString=");
        builder.append(vlanTagTpidString);
        builder.append(", vlanTagCfi=");
        builder.append(vlanTagCfi);
        builder.append(", vlanTagPriority=");
        builder.append(vlanTagPriority);
        builder.append(", vlanPVid=");
        builder.append(vlanPVid);
        builder.append(", vlanMode=");
        builder.append(vlanMode);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PortVlanAttribute))
            return false;

        PortVlanAttribute that = (PortVlanAttribute) o;

        if (vlanMode != null ? !vlanMode.equals(that.vlanMode) : that.vlanMode != null)
            return false;
        if (vlanPVid != null ? !vlanPVid.equals(that.vlanPVid) : that.vlanPVid != null)
            return false;
        if (vlanTagCfi != null ? !vlanTagCfi.equals(that.vlanTagCfi) : that.vlanTagCfi != null)
            return false;
        if (vlanTagPriority != null ? !vlanTagPriority.equals(that.vlanTagPriority) : that.vlanTagPriority != null)
            return false;
        if (vlanTagTpid != null ? !vlanTagTpid.equals(that.vlanTagTpid) : that.vlanTagTpid != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (vlanTagTpid != null ? vlanTagTpid.hashCode() : 0);
        result = 31 * result + (vlanTagCfi != null ? vlanTagCfi.hashCode() : 0);
        result = 31 * result + (vlanTagPriority != null ? vlanTagPriority.hashCode() : 0);
        result = 31 * result + (vlanPVid != null ? vlanPVid.hashCode() : 0);
        result = 31 * result + (vlanMode != null ? vlanMode.hashCode() : 0);
        return result;
    }
}
