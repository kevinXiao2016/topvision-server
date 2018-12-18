package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;

/**
 * Onu onoff record table
 * 
 * @author w1992wishes
 * @created @2017年6月16日-上午11:30:45
 *
 */
public class OnuEventLogEntry implements Serializable, AliasesSuperType{
    
    private static final long serialVersionUID = -1296303662021666743L;
    
    private Long onuId;
    private Long onuIndex;
    private Long entityId;
    private SnmpParam snmpParam;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.1.1.1.1", type = "Integer32" ,index = true)
    private Integer topOnuEventLogCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.1.1.1.2", type = "Integer32" ,index = true)
    private Integer topOnuEventLogPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.1.1.1.3", type = "Integer32" ,index = true)
    private Integer topOnuEventLogOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.1.1.1.4", type = "OctetString")
    private String topOnuEventLogList;
    
    public OnuEventLogEntry(){}
    
    public OnuEventLogEntry(SnmpParam snmpParam, OltOnuAttribute onuAttribute){
        this.snmpParam = snmpParam;
        this.entityId = onuAttribute.getEntityId();
        this.onuId = onuAttribute.getOnuId();
        this.setOnuIndex(onuAttribute.getOnuIndex());
    }
    
    public Long getOnuId() {
        return onuId;
    }

    public Long getOnuIndex() {
        if (onuIndex == null) 
            onuIndex = new EponIndex(topOnuEventLogCardIndex, topOnuEventLogPonIndex, topOnuEventLogOnuIndex).getOnuIndex();
        return onuIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Integer getTopOnuEventLogCardIndex() {
        return topOnuEventLogCardIndex;
    }

    public Integer getTopOnuEventLogPonIndex() {
        return topOnuEventLogPonIndex;
    }

    public Integer getTopOnuEventLogOnuIndex() {
        return topOnuEventLogOnuIndex;
    }

    public String getTopOnuEventLogList() {
        return topOnuEventLogList;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuEventLogCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
        topOnuEventLogPonIndex = EponIndex.getPonNo(onuIndex).intValue();
        topOnuEventLogOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setTopOnuEventLogCardIndex(Integer topOnuEventLogCardIndex) {
        this.topOnuEventLogCardIndex = topOnuEventLogCardIndex;
    }

    public void setTopOnuEventLogPonIndex(Integer topOnuEventLogPonIndex) {
        this.topOnuEventLogPonIndex = topOnuEventLogPonIndex;
    }

    public void setTopOnuEventLogOnuIndex(Integer topOnuEventLogOnuIndex) {
        this.topOnuEventLogOnuIndex = topOnuEventLogOnuIndex;
    }

    
    public void setTopOnuEventLogList(String topOnuEventLogList) {
        this.topOnuEventLogList = topOnuEventLogList;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuEventLogEntry [onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", topOnuEventLogCardIndex=");
        builder.append(topOnuEventLogCardIndex);
        builder.append(", topOnuEventLogPonIndex=");
        builder.append(topOnuEventLogPonIndex);
        builder.append(", topOnuEventLogOnuIndex=");
        builder.append(topOnuEventLogOnuIndex);
        builder.append(", topOnuEventLogList=");
        builder.append(topOnuEventLogList);
        return builder.toString();
    }
}
