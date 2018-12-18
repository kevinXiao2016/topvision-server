/***********************************************************************
 * $ QosPortBaseQosMapTable.java,v1.0 2011-11-23 16:27:20 $
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
 * @created @2011-11-23-16:27:20
 */
@TableProperty(tables = { "default" })
public class QosPortBaseQosMapTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 设备索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.4.1.1", index = true)
    private Long deviceIndex;
    /**
     * 板卡索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.4.1.2", index = true)
    private Long slotNo;
    /**
     * 端口索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.4.1.3", index = true)
    private Long portNo;
    private Long portIndex;

    /**
     * 映射规则编号 INTEGER { cos(1), tos(2), diffserv(3) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.4.1.4", index = true)
    private Integer portBaseQosMapRuleIndex;
    /**
     * 映射值。 长度为8字节，或64字节。每字节表示一个映射关系。 队 列 编 号 不 能 超 过 对 应 的 qosGlobalSetMaxQueueCount 值 ， 如 果
     * qosGlobalSetMaxQueueCount为8，则队列编号为0-7。 对于 cos，长度为 8字节，每个字节分别表示cos0-7 映射到的队列编号。 对于 tos，长度为
     * 16字节，每个字节分别表示tos0-16 映射到的队列编号。 对于 diffserv，长度为64字节，每个字节分别表示diffserv0-64映射到的队列编号。 举例如下： get
     * 1.3.6.1.4.1.17409.2.3.8.3.1.3.deviceBaseQosMapDeviceIndex(410100101).cos(1) = hex(00 00 01 02
     * 03 03 04 04) 则映射关系为： cos 0 -> queue 0 cos 1 -> queue 0 cos 2 -> queue 1 cos 3 -> queue 2 cos
     * 4 -> queue 3 cos 5 -> queue 3 cos 6 -> queue 4 cos 7 -> queue 4 OCTET STRING (SIZE (8..64))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.4.1.5", writable = true, type = "OctetString")
    private String portBaseQosMapOctet;
    private List<Integer> portBaseQosMapOctetList;

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
        if (portIndex == 0L) {
            deviceIndex = 16777216L;
        } else {
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
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getPortBaseQosMapOctet() {
        return portBaseQosMapOctet;
    }

    public void setPortBaseQosMapOctet(String portBaseQosMapOctet) {
        this.portBaseQosMapOctet = portBaseQosMapOctet;
        portBaseQosMapOctetList = EponUtil.getEachBitValueFromOcterString(portBaseQosMapOctet);
    }

    public List<Integer> getPortBaseQosMapOctetList() {
        return portBaseQosMapOctetList;
    }

    public void setPortBaseQosMapOctetList(List<Integer> portBaseQosMapOctetList) {
        this.portBaseQosMapOctetList = portBaseQosMapOctetList;
        portBaseQosMapOctet = EponUtil.getOcterStringFromValueList(portBaseQosMapOctetList);
    }

    public Integer getPortBaseQosMapRuleIndex() {
        return portBaseQosMapRuleIndex;
    }

    public void setPortBaseQosMapRuleIndex(Integer portBaseQosMapRuleIndex) {
        this.portBaseQosMapRuleIndex = portBaseQosMapRuleIndex;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("QosPortBaseQosMapTable");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", entityId=").append(entityId);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", portBaseQosMapRuleIndex=").append(portBaseQosMapRuleIndex);
        sb.append(", portBaseQosMapOctet='").append(portBaseQosMapOctet).append('\'');
        sb.append(", portBaseQosMapOctetList=").append(portBaseQosMapOctetList);
        sb.append('}');
        return sb.toString();
    }
}
