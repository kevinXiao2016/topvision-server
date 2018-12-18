/***********************************************************************
 * $ QosGlobalSetTable.java,v1.0 2011-11-23 16:10:10 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;

/**
 * @author jay
 * @created @2011-11-23-16:10:10
 */
@TableProperty(tables = { "default" })
public class QosGlobalSetTable implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 设备索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.1.1.1", index = true)
    private Long deviceIndex;

    /**
     * 最大队列数，该设备支持的最大队列数。如果最大队列数为8，则队列编号为0-7。 INTEGER (1..256)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.1.1.2")
    private Integer qosGlobalSetMaxQueueCount;
    /**
     * Define how to set QoS parameter deviceBased(1) - When set, only the table started with
     * deviceBase is applicable portBased(2) - When set, only the table started with portBase is
     * applicable
     * 
     * INTEGER { deviceBased(1), portBased(2) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.8.1.1.3", writable = true, type = "Integer32")
    private Integer qosGlobalSetMode;

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

    public Integer getQosGlobalSetMaxQueueCount() {
        return qosGlobalSetMaxQueueCount;
    }

    public void setQosGlobalSetMaxQueueCount(Integer qosGlobalSetMaxQueueCount) {
        this.qosGlobalSetMaxQueueCount = qosGlobalSetMaxQueueCount;
    }

    public Integer getQosGlobalSetMode() {
        return qosGlobalSetMode;
    }

    public void setQosGlobalSetMode(Integer qosGlobalSetMode) {
        this.qosGlobalSetMode = qosGlobalSetMode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("QosGlobalSetTable");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", entityId=").append(entityId);
        sb.append(", qosGlobalSetMaxQueueCount=").append(qosGlobalSetMaxQueueCount);
        sb.append(", qosGlobalSetMode=").append(qosGlobalSetMode);
        sb.append('}');
        return sb.toString();
    }
}
