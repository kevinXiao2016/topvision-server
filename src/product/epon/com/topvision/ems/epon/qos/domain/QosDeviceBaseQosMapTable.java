/***********************************************************************
 * $ QosDeviceBaseQosMapTable.java,v1.0 2011-11-23 16:17:29 $
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
 * @created @2011-11-23-16:17:29
 */
@TableProperty(tables = { "default" })
public class QosDeviceBaseQosMapTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 设备索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.2.1.1", index = true)
    private Long deviceIndex;
    private Long onuIndex;

    /**
     * 映射规则编号 INTEGER { cos(1), tos(2), diffserv(3) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.2.1.2", index = true)
    private Integer deviceBaseQosMapRuleIndex;
    /**
     * 映射值。 长度为8字节，或64字节。每字节表示一个映射关系。 队 列 编 号 不 能 超 过 对 应 的 qosGlobalSetMaxQueueCount 值 ， 如 果
     * qosGlobalSetMaxQueueCount为8，则队列编号为0-7。 对于 cos，长度为 8字节，每个字节分别表示cos0-7 映射到的队列编号。 对于 tos，长度为
     * 16字节，每个字节分别表示tos0-15 映射到的队列编号。 对于 diffserv，长度为64字节，每个字节分别表示diffserv0-63映射到的队列编号。 举例如下： get
     * 1.3.6.1.4.1.17409.2.3.8.3.1.3.deviceBaseQosMapDeviceIndex(410100101).cos(1) = hex(00 00 01 02
     * 03 03 04 04) 则映射关系为： cos 0 -> queue 0 cos 1 -> queue 0 cos 2 -> queue 1 cos 3 -> queue 2 cos
     * 4 -> queue 3 cos 5 -> queue 3 cos 6 -> queue 4 cos 7 -> queue 4 OCTET STRING (SIZE (8 | 64))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.2.1.3", writable = true, type = "OctetString")
    private String deviceBaseQosMapOctet;
    private List<Integer> deviceBaseQosMapOctetList;

    public String getDeviceBaseQosMapOctet() {
        return deviceBaseQosMapOctet;
    }

    public void setDeviceBaseQosMapOctet(String deviceBaseQosMapOctet) {
        this.deviceBaseQosMapOctet = deviceBaseQosMapOctet;
        deviceBaseQosMapOctetList = EponUtil.getEachBitValueFromOcterString(deviceBaseQosMapOctet);
    }

    public Integer getDeviceBaseQosMapRuleIndex() {
        return deviceBaseQosMapRuleIndex;
    }

    public void setDeviceBaseQosMapRuleIndex(Integer deviceBaseQosMapRuleIndex) {
        this.deviceBaseQosMapRuleIndex = deviceBaseQosMapRuleIndex;
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

    public List<Integer> getDeviceBaseQosMapOctetList() {
        return deviceBaseQosMapOctetList;
    }

    public void setDeviceBaseQosMapOctetList(List<Integer> deviceBaseQosMapOctetList) {
        this.deviceBaseQosMapOctetList = deviceBaseQosMapOctetList;
        deviceBaseQosMapOctet = EponUtil.getOcterStringFromValueList(deviceBaseQosMapOctetList);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("QosDeviceBaseQosMapTable");
        sb.append("{deviceBaseQosMapOctet='").append(deviceBaseQosMapOctet).append('\'');
        sb.append(", entityId=").append(entityId);
        sb.append(", deviceIndex=").append(deviceIndex);
        sb.append(", deviceBaseQosMapRuleIndex=").append(deviceBaseQosMapRuleIndex);
        sb.append(", deviceBaseQosMapOctetList=").append(deviceBaseQosMapOctetList);
        sb.append('}');
        return sb.toString();
    }
}
