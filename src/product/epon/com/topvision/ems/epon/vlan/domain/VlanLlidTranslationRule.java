/***********************************************************************
 * $ VlanTranslationRule.java,v1.0 2011-10-25 10:49:11 $
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
 * @created @2011-10-25-10:49:11
 */
public class VlanLlidTranslationRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -3581577506828515234L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.1", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.2", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.3", index = true)
    private PhysAddress onuMac;
    private String onuMacString;
	/**
	 * 端口原VLAN ID
	 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.4", index = true)
    private Integer topLlidTransVidIdx;
	/**
	 * 转换vlanID
	 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.5", writable = true, type = "Integer32")
    private Integer topLlidTransNewVid;
	/**
	 * 转换vlan COS mode
	 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.6", writable = true, type = "Integer32")
    private Integer topLlidTransCosMode;
	/**
	 * 转换vlan COS
	 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.7", writable = true, type = "Integer32")
    private Integer topLlidTransNewCos;
	/**
	 * 转换vlan TPID
	 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.8", writable = true, type = "Integer32")
    private Integer topLlidTransNewTpid;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.1.1.9", writable = true, type = "Integer32")
    private Integer topLlidTransRowStatus;

    /**
     * @return the onuMac
     */
    public PhysAddress getOnuMac() {
        if(onuMac == null){
            onuMac = new PhysAddress(onuMacString);
        }
        return onuMac;
    }

    /**
     * @param onuMac the onuMac to set
     */
    public void setOnuMac(PhysAddress onuMac) {
        this.onuMac = onuMac;
    }

    
    /**
     * @return the onuMacString
     */
    public String getOnuMacString() {
        if(onuMacString == null){
            onuMacString = onuMac.toString();
        }
        return onuMacString;
    }

    /**
     * @param onuMacString the onuMacString to set
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

    /**
     * @return the topLlidTransCosMode
     */
    public Integer getTopLlidTransCosMode() {
        return topLlidTransCosMode;
    }

    /**
     * @param topLlidTransCosMode
     *            the topLlidTransCosMode to set
     */
    public void setTopLlidTransCosMode(Integer topLlidTransCosMode) {
        this.topLlidTransCosMode = topLlidTransCosMode;
    }

    /**
     * @return the topLlidTransNewCos
     */
    public Integer getTopLlidTransNewCos() {
        return topLlidTransNewCos;
    }

    /**
     * @param topLlidTransNewCos
     *            the topLlidTransNewCos to set
     */
    public void setTopLlidTransNewCos(Integer topLlidTransNewCos) {
        this.topLlidTransNewCos = topLlidTransNewCos;
    }

    /**
     * @return the topLlidTransNewTpid
     */
    public Integer getTopLlidTransNewTpid() {
        return topLlidTransNewTpid;
    }

    /**
     * @param topLlidTransNewTpid
     *            the topLlidTransNewTpid to set
     */
    public void setTopLlidTransNewTpid(Integer topLlidTransNewTpid) {
        this.topLlidTransNewTpid = topLlidTransNewTpid;
    }

    public Integer getTopLlidTransRowStatus() {
        return topLlidTransRowStatus;
    }

    public void setTopLlidTransRowStatus(Integer topLlidTransRowStatus) {
        this.topLlidTransRowStatus = topLlidTransRowStatus;
    }

    /**
     * @return the topLlidTransVidIdx
     */
    public Integer getTopLlidTransVidIdx() {
        return topLlidTransVidIdx;
    }

    /**
     * @param topLlidTransVidIdx
     *            the topLlidTransVidIdx to set
     */
    public void setTopLlidTransVidIdx(Integer topLlidTransVidIdx) {
        this.topLlidTransVidIdx = topLlidTransVidIdx;
    }

    /**
     * @return the topLlidTransNewVid
     */
    public Integer getTopLlidTransNewVid() {
        return topLlidTransNewVid;
    }

    /**
     * @param topLlidTransNewVid
     *            the topLlidTransNewVid to set
     */
    public void setTopLlidTransNewVid(Integer topLlidTransNewVid) {
        this.topLlidTransNewVid = topLlidTransNewVid;
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
        sb.append("VlanLlidTranslationRule");
        sb.append("{onuMac='").append(onuMac).append('\'');
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", topLlidTransVidIdx=").append(topLlidTransVidIdx);
        sb.append(", topLlidTransNewVid=").append(topLlidTransNewVid);
        sb.append(", topLlidTransCosMode=").append(topLlidTransCosMode);
        sb.append(", topLlidTransNewCos=").append(topLlidTransNewCos);
        sb.append(", topLlidTransNewTpid=").append(topLlidTransNewTpid);
        sb.append(", topLlidTransRowStatus=").append(topLlidTransRowStatus);
        sb.append('}');
        return sb.toString();
    }
}