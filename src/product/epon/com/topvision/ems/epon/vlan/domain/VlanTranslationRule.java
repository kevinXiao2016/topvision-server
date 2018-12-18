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

/**
 * @author jay
 * @created @2011-10-25-10:49:11
 */
public class VlanTranslationRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 4701285363219338544L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.2.1.1", index = true)
    private Long deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.2.1.2", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.2.1.3", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    /**
     * 端口原始VLAN ID索引号
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.2.1.4", index = true)
    private Integer vlanIndex;
    /**
     * 翻译的新vlan号
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.2.1.5", writable = true, type = "Gauge32")
    private Integer translationNewVid;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.2.1.6", writable = true, type = "Integer32")
    private Integer translationRowStatus;

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
            slotNo = EponIndex.getSlotNo(portIndex);
            portNo = EponIndex.getPonNo(portIndex);
        } else {
            slotNo = EponIndex.getOnuCardNo(portIndex);
            portNo = EponIndex.getUniNo(portIndex);
        }
        this.getDeviceIndex();
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

    public Integer getTranslationRowStatus() {
        return translationRowStatus;
    }

    public void setTranslationRowStatus(Integer translationRowStatus) {
        this.translationRowStatus = translationRowStatus;
    }

    /**
     * @return the vlanIndex
     */
    public Integer getVlanIndex() {
        return vlanIndex;
    }

    /**
     * @param vlanIndex
     *            the vlanIndex to set
     */
    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    /**
     * @return the translationNewVid
     */
    public Integer getTranslationNewVid() {
        return translationNewVid;
    }

    /**
     * @param translationNewVid
     *            the translationNewVid to set
     */
    public void setTranslationNewVid(Integer translationNewVid) {
        this.translationNewVid = translationNewVid;
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
        sb.append("VlanTranslationRule");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", vlanIndex=").append(vlanIndex);
        sb.append(", translationNewVid=").append(translationNewVid);
        sb.append(", translationRowStatus=").append(translationRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
