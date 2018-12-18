/***********************************************************************
 * $ QosDeviceBaseQosPolicyTable.java,v1.0 2011-11-23 16:23:02 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-16:23:02
 */
@TableProperty(tables = { "default" })
public class QosDeviceBaseQosPolicyTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 设备索引号 For OLT, set to corresponding device/slot/port For ONU, set to 0
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.3.1.1", index = true)
    private Long deviceIndex;
    private Long onuIndex;
    /**
     * 调度模式 INTEGER { sp(1), wrr(2), spWrr(3), wfp(4) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.3.1.2", writable = true, type = "Integer32")
    private Integer policyMode;
    /**
     * 权重值。 长度等于队列数目，每字节表示一个队列的权重。 对于 sp，读写该节点无意义，读时始终为全0。 对于
     * wrr，每字节描述一个队列的权重。权重的范围为1-100，全部队列权重之和应等于 100。 对于 spWrr，每字节描述一个队列的权重。权重范围为0-100，权重为0表示该队列用sp
     * 模式，权重值非0的队列权重之和应等于100。 对于 wfp，读写该节点无意义，读时始终为全0。 举例如下： get
     * 1.3.6.1.4.1.17409.2.3.8.4.1.2.deviceBaseQosPolicyDeviceIndex(410100101). = wrr(2) get
     * 1.3.6.1.4.1.17409.2.3.8.4.1.3.deviceBaseQosPolicyDeviceIndex(410100101). = hex(5 5 5 5 10 20
     * 20 30)
     * 
     * 说明：共8个队列 queue 0 权重为 5 queue 1 权重为 5 queue 2 权重为 5 queue 3 权重为 5 queue 4 权重为 10 queue 5 权重为
     * 20 queue 6 权重为 20 queue 7 权重为 30
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.3.1.3", writable = true, type = "OctetString")
    private String weightOctet;
    private List<Integer> deviceBaseQosPolicyWeightOctetList;

    /**
     * Define SP assured bandwidth for each queue. The size of this object is 4*(number of queue).
     * For each queue, use 4 octets to represend the assured bandwidth for the queue. The four-octet
     * could be mapped to an unsigned integer, in units of kbps. The sequence of the map is from
     * queue 0 to queue max (for example, 7). For example, 00 00 00 10 00 00 01 00 00 00 00 00 00 00
     * 00 00 00 00 10 00 00 00 00 01 00 01 01 00 00 00 11 11 It means, 8 queues and the assured
     * bandwidth for each queue as: queue 0 16kbps queue 1 256kbps queue 2 0 queue 3 0 queue 4
     * 4096kbps queue 5 1kbps queue 6 65792kbps queue 7 65535kbps
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.3.1.4", writable = true, type = "OctetString")
    private String spBandwidthRange;
    private List<Integer> deviceBaseQosPolicySpBandwidthRangeList;

    public Integer getPolicyMode() {
        return policyMode;
    }

    public void setPolicyMode(Integer policyMode) {
        this.policyMode = policyMode;
    }

    public String getSpBandwidthRange() {
        return spBandwidthRange;
    }

    public void setSpBandwidthRange(String spBandwidthRange) {
        this.spBandwidthRange = spBandwidthRange;
        deviceBaseQosPolicySpBandwidthRangeList = EponUtil.getFourBitValueFromOcterStringInSymbol(spBandwidthRange);
    }

    public List<Integer> getDeviceBaseQosPolicySpBandwidthRangeList() {
        return deviceBaseQosPolicySpBandwidthRangeList;
    }

    public void setDeviceBaseQosPolicySpBandwidthRangeList(List<Integer> deviceBaseQosPolicySpBandwidthRangeList) {
        this.deviceBaseQosPolicySpBandwidthRangeList = deviceBaseQosPolicySpBandwidthRangeList;
        spBandwidthRange = EponUtil
                .getOcterStringFromFourByteValueListInSymbol(deviceBaseQosPolicySpBandwidthRangeList);
    }

    public String getWeightOctet() {
        return weightOctet;
    }

    public void setWeightOctet(String weightOctet) {
        this.weightOctet = weightOctet;
        deviceBaseQosPolicyWeightOctetList = EponUtil.getEachBitValueFromOcterString(weightOctet);
    }

    public List<Integer> getDeviceBaseQosPolicyWeightOctetList() {
        return deviceBaseQosPolicyWeightOctetList;
    }

    public void setDeviceBaseQosPolicyWeightOctetList(List<Integer> deviceBaseQosPolicyWeightOctetList) {
        this.deviceBaseQosPolicyWeightOctetList = deviceBaseQosPolicyWeightOctetList;
        weightOctet = EponUtil.getOcterStringFromValueList(deviceBaseQosPolicyWeightOctetList);
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(deviceIndex);
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        deviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("QosDeviceBaseQosPolicyTable");
        sb.append("{policyMode=").append(policyMode);
        sb.append(", entityId=").append(entityId);
        sb.append(", deviceIndex=").append(deviceIndex);
        sb.append(", weightOctet='").append(weightOctet).append('\'');
        sb.append(", deviceBaseQosPolicyWeightOctetList=").append(deviceBaseQosPolicyWeightOctetList);
        sb.append(", spBandwidthRange='").append(spBandwidthRange).append('\'');
        sb.append(", deviceBaseQosPolicySpBandwidthRangeList=").append(deviceBaseQosPolicySpBandwidthRangeList);
        sb.append('}');
        return sb.toString();
    }
}
