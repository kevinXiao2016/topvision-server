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

/**
 * @author jay
 * @created @2011-10-25-10:54:34
 */
public class VlanAggregationRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1131372114948724243L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.3.1.1.1", index = true)
    private Long deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.3.1.1.2", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.3.1.1.3", index = true)
    private Long portNo;
    private Long portIndex;
    private Long portId;
    private Long entityId;
    /**
     * 端口VLAN聚合组VLAN号
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.3.1.1.4", index = true)
    private Integer portAggregationVidIndex;
    /**
     * 聚合的vlan ID组 位图
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.3.1.1.5", writable = true, type = "OctetString")
    private String aggregationVidList;
    private List<Integer> aggregationVidListAfterSwitch;
    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.3.1.1.6", writable = true, type = "Integer32")
    private Integer aggregationRowStatus;

    public Integer getAggregationRowStatus() {
        return aggregationRowStatus;
    }

    public void setAggregationRowStatus(Integer aggregationRowStatus) {
        this.aggregationRowStatus = aggregationRowStatus;
    }

    public String getAggregationVidList() {
        return aggregationVidList;
    }

    public void setAggregationVidList(String aggregationVidList) {
        this.aggregationVidList = aggregationVidList;
        if (aggregationVidList != null) {
            aggregationVidListAfterSwitch = EponUtil.getVlanListFromMib(aggregationVidList);
        }
    }

    /**
     * @return the portAggregationVidIndex
     */
    public Integer getPortAggregationVidIndex() {
        return portAggregationVidIndex;
    }

    /**
     * @param portAggregationVidIndex
     *            the portAggregationVidIndex to set
     */
    public void setPortAggregationVidIndex(Integer portAggregationVidIndex) {
        this.portAggregationVidIndex = portAggregationVidIndex;
    }

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
     * @return the aggregationVidListAfterSwitch
     */
    public List<Integer> getAggregationVidListAfterSwitch() {
        return aggregationVidListAfterSwitch;
    }

    /**
     * @param aggregationVidListAfterSwitch
     *            the aggregationVidListAfterSwitch to set
     */
    public void setAggregationVidListAfterSwitch(List<Integer> aggregationVidListAfterSwitch) {
        this.aggregationVidListAfterSwitch = aggregationVidListAfterSwitch;
        aggregationVidList = EponUtil.getVlanBitMapFormList(aggregationVidListAfterSwitch);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VlanAggregationRule");
        sb.append("{aggregationRowStatus=").append(aggregationRowStatus);
        sb.append(", deviceIndex=").append(deviceIndex);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", portAggregationVidIndex=").append(portAggregationVidIndex);
        sb.append(", aggregationVidList=").append(aggregationVidList);
        sb.append('}');
        return sb.toString();
    }
}
