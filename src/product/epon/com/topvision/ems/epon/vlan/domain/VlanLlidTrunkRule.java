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
import com.topvision.framework.domain.PhysAddress;

/**
 * @author jay
 * @created @2011-10-25-11:19:58
 */
public class VlanLlidTrunkRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -3415654724198336338L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.3.1.1", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.3.1.2", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.3.1.3", index = true)
    private PhysAddress onuMac;
    private String onuMacString;
    /**
     * Trunked Vlan List for the port
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.3.1.4", writable = true, type = "OctetString")
    private String llidVlanTrunkVidBmp;
    private List<Integer> llidVlanTrunkVidBmpAfterSwitch;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.3.1.5", writable = true, type = "Integer32")
    private Integer llidVlanTrunkRowStatus;

    
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
    
    public Integer getLlidVlanTrunkRowStatus() {
        return llidVlanTrunkRowStatus;
    }

    public void setLlidVlanTrunkRowStatus(Integer llidVlanTrunkRowStatus) {
        this.llidVlanTrunkRowStatus = llidVlanTrunkRowStatus;
    }

    public String getLlidVlanTrunkVidBmp() {
        return llidVlanTrunkVidBmp;
    }

    public void setLlidVlanTrunkVidBmp(String llidVlanTrunkVidBmp) {
        this.llidVlanTrunkVidBmp = llidVlanTrunkVidBmp;
        if (llidVlanTrunkVidBmp != null) {
            llidVlanTrunkVidBmpAfterSwitch = EponUtil.getVlanListFromMib(llidVlanTrunkVidBmp);
        }
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
     * @return the llidVlanTrunkVidBmpAfterSwitch
     */
    public List<Integer> getLlidVlanTrunkVidBmpAfterSwitch() {
        return llidVlanTrunkVidBmpAfterSwitch;
    }

    /**
     * @param llidVlanTrunkVidBmpAfterSwitch
     *            the llidVlanTrunkVidBmpAfterSwitch to set
     */
    public void setLlidVlanTrunkVidBmpAfterSwitch(List<Integer> llidVlanTrunkVidBmpAfterSwitch) {
        this.llidVlanTrunkVidBmpAfterSwitch = llidVlanTrunkVidBmpAfterSwitch;
        llidVlanTrunkVidBmp = EponUtil.getVlanBitMapFormList(llidVlanTrunkVidBmpAfterSwitch);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VlanLlidTrunkRule");
        sb.append("{llidVlanTrunkRowStatus=").append(llidVlanTrunkRowStatus);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", onuMac='").append(onuMac).append('\'');
        sb.append(", llidVlanTrunkVidBmp='").append(llidVlanTrunkVidBmp).append('\'');
        sb.append('}');
        return sb.toString();
    }
}