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
import com.topvision.framework.domain.PhysAddress;

/**
 * @author jay
 * @created @2011-10-25-11:24:55
 */
public class VlanLlidQinQRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2526476496229441142L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.1", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.2", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.3", index = true)
    private PhysAddress onuMac;
    private String onuMacString;
    /**
     * The starting VLAN ID
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.4", index = true)
    private Integer topLqVlanStartCVid;
    /**
     * The ending VLAN ID
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.5", index = true)
    private Integer topLqVlanEndCVid;
    /**
     * The outer VLAN id to be QinQed
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.6", writable = true, type = "Integer32")
    private Integer topLqVlanSVlan;
    /**
     * 是否使用新的COS值
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.7", writable = true, type = "Integer32")
    private Integer topLqVlanCosMode;
    public static final Integer REMARK = 1;
    public static final Integer COPYFROMCTAG = 2;

    /**
     * 新的COS值
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.8", writable = true, type = "Integer32")
    private Integer topLqVlanSCos;
    /**
     * 新的TPID
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.9", writable = true, type = "Integer32")
    private Integer topLqVlanOuterTpid;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.2.1.1.10", writable = true, type = "Integer32")
    private Integer topLqVlanRowStatus;

    /**
     * @return the onuMac
     */
    public PhysAddress getOnuMac() {
        if (onuMac == null) {
            onuMac = new PhysAddress(onuMacString);
        }
        return onuMac;
    }

    /**
     * @param onuMac
     *            the onuMac to set
     */
    public void setOnuMac(PhysAddress onuMac) {
        this.onuMac = onuMac;
    }

    /**
     * @return the onuMacString
     */
    public String getOnuMacString() {
        if (onuMacString == null) {
            onuMacString = onuMac.toString();
        }
        return onuMacString;
    }

    /**
     * @param onuMacString
     *            the onuMacString to set
     */
    public void setOnuMacString(String onuMacString) {
        this.onuMacString = onuMacString;
    }

    public Long getPortIndex() {
        if (portIndex == null) {
            portIndex = new EponIndex(slotNo.intValue(), portNo.intValue()).getPonIndex();
        }
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        slotNo = EponIndex.getSlotNo(portIndex);
        portNo = EponIndex.getPonNo(portIndex);
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

    public Integer getTopLqVlanRowStatus() {
        return topLqVlanRowStatus;
    }

    public void setTopLqVlanRowStatus(Integer topLqVlanRowStatus) {
        this.topLqVlanRowStatus = topLqVlanRowStatus;
    }

    /**
     * @return the topLqVlanCosMode
     */
    public Integer getTopLqVlanCosMode() {
        return topLqVlanCosMode;
    }

    /**
     * @param topLqVlanCosMode
     *            the topLqVlanCosMode to set
     */
    public void setTopLqVlanCosMode(Integer topLqVlanCosMode) {
        this.topLqVlanCosMode = topLqVlanCosMode;
    }

    /**
     * @return the topLqVlanSCos
     */
    public Integer getTopLqVlanSCos() {
        return topLqVlanSCos;
    }

    /**
     * @param topLqVlanSCos
     *            the topLqVlanSCos to set
     */
    public void setTopLqVlanSCos(Integer topLqVlanSCos) {
        this.topLqVlanSCos = topLqVlanSCos;
    }

    /**
     * @return the topLqVlanOuterTpid
     */
    public Integer getTopLqVlanOuterTpid() {
        return topLqVlanOuterTpid;
    }

    /**
     * @param topLqVlanOuterTpid
     *            the topLqVlanOuterTpid to set
     */
    public void setTopLqVlanOuterTpid(Integer topLqVlanOuterTpid) {
        this.topLqVlanOuterTpid = topLqVlanOuterTpid;
    }

    /**
     * @return the topLqVlanStartCVid
     */
    public Integer getTopLqVlanStartCVid() {
        return topLqVlanStartCVid;
    }

    /**
     * @param topLqVlanStartCVid
     *            the topLqVlanStartCVid to set
     */
    public void setTopLqVlanStartCVid(Integer topLqVlanStartCVid) {
        this.topLqVlanStartCVid = topLqVlanStartCVid;
    }

    /**
     * @return the topLqVlanEndCVid
     */
    public Integer getTopLqVlanEndCVid() {
        return topLqVlanEndCVid;
    }

    /**
     * @param topLqVlanEndCVid
     *            the topLqVlanEndCVid to set
     */
    public void setTopLqVlanEndCVid(Integer topLqVlanEndCVid) {
        this.topLqVlanEndCVid = topLqVlanEndCVid;
    }

    /**
     * @return the topLqVlanSVlan
     */
    public Integer getTopLqVlanSVlan() {
        return topLqVlanSVlan;
    }

    /**
     * @param topLqVlanSVlan
     *            the topLqVlanSVlan to set
     */
    public void setTopLqVlanSVlan(Integer topLqVlanSVlan) {
        this.topLqVlanSVlan = topLqVlanSVlan;
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
        sb.append("VlanLlidQinQRule");
        sb.append("{onuMac='").append(onuMac).append('\'');
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", topLqVlanStartCVid=").append(topLqVlanStartCVid);
        sb.append(", topLqVlanEndCVid=").append(topLqVlanEndCVid);
        sb.append(", topLqVlanSVlan=").append(topLqVlanSVlan);
        sb.append(", topLqVlanCosMode=").append(topLqVlanCosMode);
        sb.append(", topLqVlanSCos=").append(topLqVlanSCos);
        sb.append(", topLqVlanOuterTpid=").append(topLqVlanOuterTpid);
        sb.append(", topLqVlanRowStatus=").append(topLqVlanRowStatus);
        sb.append('}');
        return sb.toString();
    }
}