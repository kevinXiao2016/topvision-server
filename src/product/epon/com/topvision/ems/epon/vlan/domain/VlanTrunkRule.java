/***********************************************************************
 * $ VlanTrunkRule.java,v1.0 2011-10-25 11:19:58 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-10-25-11:19:58
 */
public class VlanTrunkRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1227141072901537121L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.4.1.1", index = true)
    private Long deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.4.1.2", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.4.1.3", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    /**
     * Trunked Vlan List for the port
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.4.1.4", writable = true, type = "OctetString")
    private String trunkVidList;
    private List<Integer> trunkVidListAfterSwitch;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.4.1.5", writable = true, type = "Integer32")
    private Integer portVlanTrunkRowStatus;

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
            portNo = EponIndex.getPonNo(portIndex);
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

    public Integer getPortVlanTrunkRowStatus() {
        return portVlanTrunkRowStatus;
    }

    public void setPortVlanTrunkRowStatus(Integer portVlanTrunkRowStatus) {
        this.portVlanTrunkRowStatus = portVlanTrunkRowStatus;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public String getTrunkVidList() {
        return trunkVidList;
    }

    public void setTrunkVidList(String trunkVidList) {
        this.trunkVidList = trunkVidList;
        if (trunkVidList != null) {
            trunkVidListAfterSwitch = EponUtil.getVlanListFromMib(trunkVidList);
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
     * @return the trunkVidListAfterSwitch
     */
    public List<Integer> getTrunkVidListAfterSwitch() {
        return trunkVidListAfterSwitch;
    }

    /**
     * @param trunkVidListAfterSwitch
     *            the trunkVidListAfterSwitch to set
     */
    public void setTrunkVidListAfterSwitch(List<Integer> trunkVidListAfterSwitch) {
        this.trunkVidListAfterSwitch = trunkVidListAfterSwitch;
        if (trunkVidListAfterSwitch != null) {
            trunkVidList = EponUtil.getVlanBitMapFormList(trunkVidListAfterSwitch);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VlanTrunkRule");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", trunkVidList='").append(trunkVidList).append('\'');
        sb.append(", portVlanTrunkRowStatus=").append(portVlanTrunkRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
