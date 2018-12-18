/***********************************************************************
 * $ SlaTable.java,v1.0 2011-11-23 17:04:07 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-17:04:07
 */
@TableProperty(tables = { "default" })
public class SlaTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 设备索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.4.4.1.1", index = true)
    private Long deviceIndex;
    private Long onuIndex;

    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.4.4.1.2", writable = true, type = "Integer32")
    private Long slaDsFixedBW;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.4.4.1.3", writable = true, type = "Integer32")
    private Long slaDsPeakBW;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.4.4.1.4", writable = true, type = "Integer32")
    private Long slaDsCommittedBW;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.4.4.1.5", writable = true, type = "Integer32")
    private Long slaUsFixedBW;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.4.4.1.6", writable = true, type = "Integer32")
    private Long slaUsPeakBW;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.4.4.1.7", writable = true, type = "Integer32")
    private Long slaUsCommittedBW;

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        deviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(deviceIndex);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getSlaDsCommittedBW() {
        return slaDsCommittedBW;
    }

    public void setSlaDsCommittedBW(Long slaDsCommittedBW) {
        this.slaDsCommittedBW = slaDsCommittedBW;
    }

    public Long getSlaDsFixedBW() {
        return slaDsFixedBW;
    }

    public void setSlaDsFixedBW(Long slaDsFixedBW) {
        this.slaDsFixedBW = slaDsFixedBW;
    }

    public Long getSlaDsPeakBW() {
        return slaDsPeakBW;
    }

    public void setSlaDsPeakBW(Long slaDsPeakBW) {
        this.slaDsPeakBW = slaDsPeakBW;
    }

    public Long getSlaUsCommittedBW() {
        return slaUsCommittedBW;
    }

    public void setSlaUsCommittedBW(Long slaUsCommittedBW) {
        this.slaUsCommittedBW = slaUsCommittedBW;
    }

    public Long getSlaUsFixedBW() {
        return slaUsFixedBW;
    }

    public void setSlaUsFixedBW(Long slaUsFixedBW) {
        this.slaUsFixedBW = slaUsFixedBW;
    }

    public Long getSlaUsPeakBW() {
        return slaUsPeakBW;
    }

    public void setSlaUsPeakBW(Long slaUsPeakBW) {
        this.slaUsPeakBW = slaUsPeakBW;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SlaTable");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", entityId=").append(entityId);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", slaDsFixedBW=").append(slaDsFixedBW);
        sb.append(", slaDsPeakBW=").append(slaDsPeakBW);
        sb.append(", slaDsCommittedBW=").append(slaDsCommittedBW);
        sb.append(", slaUsFixedBW=").append(slaUsFixedBW);
        sb.append(", slaUsPeakBW=").append(slaUsPeakBW);
        sb.append(", slaUsCommittedBW=").append(slaUsCommittedBW);
        sb.append('}');
        return sb.toString();
    }
}
