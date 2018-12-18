/***********************************************************************
 * $Id: OnuDeregisterTable.java,v1.0 2016年11月10日 下午3:52:33 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016年11月10日-下午3:52:33
 *
 */
public class OnuDeregisterTable implements AliasesSuperType {

    private static final long serialVersionUID = -6394986200259455346L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private SnmpParam snmpParam;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.14.1.1.1", index = true)
    private Integer topOnuVerCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.14.1.1.2", index = true)
    private Integer topOnuVerPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.14.1.1.3", index = true)
    private Integer topOnuVerOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.14.1.1.14", type = "Integer32")
    private Long topOnuTimeSinceLastDeregister;

    private Timestamp lastDeregisterTime;

    public OnuDeregisterTable() {
    }

    public OnuDeregisterTable(SnmpParam snmpParam, OltOnuAttribute onuAttribute) {
        this.snmpParam = snmpParam;
        this.entityId = onuAttribute.getEntityId();
        this.onuId = onuAttribute.getOnuId();
        this.setOnuIndex(onuAttribute.getOnuIndex());
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        if (onuIndex == null) {
            onuIndex = new EponIndex(topOnuVerCardIndex, topOnuVerPonIndex, topOnuVerOnuIndex).getOnuIndex();
        }
        return onuIndex;
    }

    /**
     * @param onuIndex the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuVerCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
        topOnuVerPonIndex = EponIndex.getPonNo(onuIndex).intValue();
        topOnuVerOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
    }

    /**
     * @return the topOnuVerCardIndex
     */
    public Integer getTopOnuVerCardIndex() {
        return topOnuVerCardIndex;
    }

    /**
     * @param topOnuVerCardIndex the topOnuVerCardIndex to set
     */
    public void setTopOnuVerCardIndex(Integer topOnuVerCardIndex) {
        this.topOnuVerCardIndex = topOnuVerCardIndex;
    }

    /**
     * @return the topOnuVerPonIndex
     */
    public Integer getTopOnuVerPonIndex() {
        return topOnuVerPonIndex;
    }

    /**
     * @param topOnuVerPonIndex the topOnuVerPonIndex to set
     */
    public void setTopOnuVerPonIndex(Integer topOnuVerPonIndex) {
        this.topOnuVerPonIndex = topOnuVerPonIndex;
    }

    /**
     * @return the topOnuVerOnuIndex
     */
    public Integer getTopOnuVerOnuIndex() {
        return topOnuVerOnuIndex;
    }

    /**
     * @param topOnuVerOnuIndex the topOnuVerOnuIndex to set
     */
    public void setTopOnuVerOnuIndex(Integer topOnuVerOnuIndex) {
        this.topOnuVerOnuIndex = topOnuVerOnuIndex;
    }

    /**
     * @return the topOnuTimeSinceLastDeregister
     */
    public Long getTopOnuTimeSinceLastDeregister() {
        return topOnuTimeSinceLastDeregister;
    }

    /**
     * @param topOnuTimeSinceLastDeregister the topOnuTimeSinceLastDeregister to set
     */
    public void setTopOnuTimeSinceLastDeregister(Long topOnuTimeSinceLastDeregister) {
        this.topOnuTimeSinceLastDeregister = topOnuTimeSinceLastDeregister;
    }

    /**
     * @return the snmpParam
     */
    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    /**
     * @param snmpParam the snmpParam to set
     */
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    /**
     * @return the lastDeregisterTime
     */
    public Timestamp getLastDeregisterTime() {
        return lastDeregisterTime;
    }

    /**
     * @param lastDeregisterTime the lastDeregisterTime to set
     */
    public void setLastDeregisterTime(Timestamp lastDeregisterTime) {
        this.lastDeregisterTime = lastDeregisterTime;
    }

    /**
     * 
     * 
     */
    public void updateLastDeregisterTime() {
        if (topOnuTimeSinceLastDeregister != null) {
            this.lastDeregisterTime = new Timestamp(System.currentTimeMillis() - topOnuTimeSinceLastDeregister * 1000);
        } else {
            this.lastDeregisterTime = null;
        }
    }
}
