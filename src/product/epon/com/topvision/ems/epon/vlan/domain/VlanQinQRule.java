/***********************************************************************
 * $ VlanQinQRule.java,v1.0 2011-10-25 11:24:55 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-10-25-11:24:55
 */
public class VlanQinQRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6872827041077891417L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.1", index = true)
    private Long deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.2", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.3", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    /**
     * The starting VLAN ID
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.4", index = true)
    private Integer pqStartVlanId;
    /**
     * The ending VLAN ID
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.5", index = true)
    private Integer pqEndVlanId;
    /**
     * The outer VLAN id to be QinQed
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.6", writable = true, type = "Integer32")
    private Integer pqSVlanId;
    /**
     * 是否使用新的COS值
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.7", writable = true, type = "Integer32")
    private Integer pqSTagCosDetermine;
    /**
     * 新的COS值
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.8", writable = true, type = "Integer32")
    private Integer pqSTagCosNewValue;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.4.1.1.9", writable = true, type = "Integer32")
    private Integer pqRowStatus;

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

    public Integer getPqRowStatus() {
        return pqRowStatus;
    }

    public void setPqRowStatus(Integer pqRowStatus) {
        this.pqRowStatus = pqRowStatus;
    }

    /**
     * @return the pqSTagCosDetermine
     */
    public Integer getPqSTagCosDetermine() {
        return pqSTagCosDetermine;
    }

    /**
     * @param pqSTagCosDetermine
     *            the pqSTagCosDetermine to set
     */
    public void setPqSTagCosDetermine(Integer pqSTagCosDetermine) {
        this.pqSTagCosDetermine = pqSTagCosDetermine;
    }

    /**
     * @return the pqSTagCosNewValue
     */
    public Integer getPqSTagCosNewValue() {
        return pqSTagCosNewValue;
    }

    /**
     * @param pqSTagCosNewValue
     *            the pqSTagCosNewValue to set
     */
    public void setPqSTagCosNewValue(Integer pqSTagCosNewValue) {
        this.pqSTagCosNewValue = pqSTagCosNewValue;
    }

    /**
     * @return the pqStartVlanId
     */
    public Integer getPqStartVlanId() {
        return pqStartVlanId;
    }

    /**
     * @param pqStartVlanId
     *            the pqStartVlanId to set
     */
    public void setPqStartVlanId(Integer pqStartVlanId) {
        this.pqStartVlanId = pqStartVlanId;
    }

    /**
     * @return the pqEndVlanId
     */
    public Integer getPqEndVlanId() {
        return pqEndVlanId;
    }

    /**
     * @param pqEndVlanId
     *            the pqEndVlanId to set
     */
    public void setPqEndVlanId(Integer pqEndVlanId) {
        this.pqEndVlanId = pqEndVlanId;
    }

    /**
     * @return the pqSVlanId
     */
    public Integer getPqSVlanId() {
        return pqSVlanId;
    }

    /**
     * @param pqSVlanId
     *            the pqSVlanId to set
     */
    public void setPqSVlanId(Integer pqSVlanId) {
        this.pqSVlanId = pqSVlanId;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VlanQinQRule");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", pqStartVlanId=").append(pqStartVlanId);
        sb.append(", pqEndVlanId=").append(pqEndVlanId);
        sb.append(", pqSVlanId=").append(pqSVlanId);
        sb.append(", pqSTagCosDetermine=").append(pqSTagCosDetermine);
        sb.append(", pqSTagCosNewValue=").append(pqSTagCosNewValue);
        sb.append(", pqRowStatus=").append(pqRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
