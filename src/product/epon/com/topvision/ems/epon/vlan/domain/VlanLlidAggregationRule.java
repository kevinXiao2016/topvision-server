/***********************************************************************
 * $ VlanAggregationRule.java,v1.0 2011-10-25 10:54:34 $
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
 * @created @2011-10-25-10:54:34
 */
public class VlanLlidAggregationRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 3580011736723826236L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.1", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.2", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.3", index = true)
    private PhysAddress onuMac;
    private String onuMacString;
    /**
     * 端口VLAN聚合组VLAN号
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.4", index = true)
    private Integer llidVlanAfterAggVid;
    /**
     * 聚合的vlan ID组 位图
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.5", writable = true, type = "OctetString")
    private String llidVlanBeforeAggVidList;
    private List<Integer> llidVlanBeforeAggVidListAfterSwitch;
    /**
     * COS值修改方式
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.6", writable = true, type = "Integer32")
    private Integer llidVlanAggCosMode;
    /**
     * 新的COS值
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.7", writable = true, type = "Integer32")
    private Integer llidVlanAggNewCos;
    /**
     * 新的TPID
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.8", writable = true, type = "Integer32")
    private Integer llidVlanAggNewTpid;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.1.2.1.9", writable = true, type = "Integer32")
    private Integer llidVlanAggRowStatus;

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
    
    public Integer getLlidVlanAfterAggVid() {
        return llidVlanAfterAggVid;
    }

    public void setLlidVlanAfterAggVid(Integer llidVlanAfterAggVid) {
        this.llidVlanAfterAggVid = llidVlanAfterAggVid;
    }

    /**
     * @return the llidVlanAggCosMode
     */
    public Integer getLlidVlanAggCosMode() {
        return llidVlanAggCosMode;
    }

    /**
     * @param llidVlanAggCosMode
     *            the llidVlanAggCosMode to set
     */
    public void setLlidVlanAggCosMode(Integer llidVlanAggCosMode) {
        this.llidVlanAggCosMode = llidVlanAggCosMode;
    }

    /**
     * @return the llidVlanAggNewCos
     */
    public Integer getLlidVlanAggNewCos() {
        return llidVlanAggNewCos;
    }

    /**
     * @param llidVlanAggNewCos
     *            the llidVlanAggNewCos to set
     */
    public void setLlidVlanAggNewCos(Integer llidVlanAggNewCos) {
        this.llidVlanAggNewCos = llidVlanAggNewCos;
    }

    /**
     * @return the llidVlanAggNewTpid
     */
    public Integer getLlidVlanAggNewTpid() {
        return llidVlanAggNewTpid;
    }

    /**
     * @param llidVlanAggNewTpid
     *            the llidVlanAggNewTpid to set
     */
    public void setLlidVlanAggNewTpid(Integer llidVlanAggNewTpid) {
        this.llidVlanAggNewTpid = llidVlanAggNewTpid;
    }

    public Integer getLlidVlanAggRowStatus() {
        return llidVlanAggRowStatus;
    }

    public void setLlidVlanAggRowStatus(Integer llidVlanAggRowStatus) {
        this.llidVlanAggRowStatus = llidVlanAggRowStatus;
    }

    public String getLlidVlanBeforeAggVidList() {
        return llidVlanBeforeAggVidList;
    }

    public void setLlidVlanBeforeAggVidList(String llidVlanBeforeAggVidList) {
        this.llidVlanBeforeAggVidList = llidVlanBeforeAggVidList;
        llidVlanBeforeAggVidListAfterSwitch = EponUtil.getVlanListFromMib(llidVlanBeforeAggVidList);
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
     * @return the llidVlanBeforeAggVidListAfterSwitch
     */
    public List<Integer> getLlidVlanBeforeAggVidListAfterSwitch() {
        return llidVlanBeforeAggVidListAfterSwitch;
    }

    /**
     * @param llidVlanBeforeAggVidListAfterSwitch
     *            the llidVlanBeforeAggVidListAfterSwitch to set
     */
    public void setLlidVlanBeforeAggVidListAfterSwitch(List<Integer> llidVlanBeforeAggVidListAfterSwitch) {
        this.llidVlanBeforeAggVidListAfterSwitch = llidVlanBeforeAggVidListAfterSwitch;
        llidVlanBeforeAggVidList = EponUtil.getVlanBitMapFormList(llidVlanBeforeAggVidListAfterSwitch);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VlanLlidAggregationRule");
        sb.append("{llidVlanAfterAggVid=").append(llidVlanAfterAggVid);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", onuMac=").append(onuMac);
        sb.append(", llidVlanBeforeAggVidList=").append(llidVlanBeforeAggVidList);
        sb.append(", llidVlanAggCosMode=").append(llidVlanAggCosMode);
        sb.append(", llidVlanAggNewCos=").append(llidVlanAggNewCos);
        sb.append(", llidVlanAggNewTpid=").append(llidVlanAggNewTpid);
        sb.append(", llidVlanAggRowStatus=").append(llidVlanAggRowStatus);
        sb.append('}');
        return sb.toString();
    }
}