package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author xiaoyue
 * @created @2018年10月12日-下午3:42:03
 *
 */
public class VlanLlidOnuQinQRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6206731404844177899L;

    private Long entityId;
    private Long ponIndex;
    private String mac;
    private Long ponId;
    private Long onuIndex;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.1", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.2", index = true)
    private Long portNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.3", index = true)
    private Long onuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.4", index = true)
    private Integer topOnuQinQStartVlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.5", index = true)
    private Integer topOnuQinQEndVlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.6", writable = true, type = "Integer32")
    private Integer topOnuQinQSVlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.7", writable = true, type = "Integer32")
    private Integer topOnuQinQSTagCosDetermine;
    public static final Integer REDEFINE = 1;
    public static final Integer COPYFROMCTAG = 2;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.8", writable = true, type = "Integer32")
    private Integer topOnuQinQSTagCosNewValue;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.8.1.1.9", writable = true, type = "Integer32")
    private Integer topOnuQinQRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonIndex() {
        if (ponIndex == null) {
            ponIndex = new EponIndex(slotNo.intValue(), portNo.intValue()).getPonIndex();
        }
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
        slotNo = EponIndex.getSlotNo(ponIndex);
        portNo = EponIndex.getPonNo(ponIndex);
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Long getPortNo() {
        return portNo;
    }

    public void setPortNo(Long portNo) {
        this.portNo = portNo;
    }

    public Long getOnuNo() {
        return onuNo;
    }

    public void setOnuNo(Long onuNo) {
        this.onuNo = onuNo;
    }

    public Integer getTopOnuQinQStartVlanId() {
        return topOnuQinQStartVlanId;
    }

    public void setTopOnuQinQStartVlanId(Integer topOnuQinQStartVlanId) {
        this.topOnuQinQStartVlanId = topOnuQinQStartVlanId;
    }

    public Integer getTopOnuQinQEndVlanId() {
        return topOnuQinQEndVlanId;
    }

    public void setTopOnuQinQEndVlanId(Integer topOnuQinQEndVlanId) {
        this.topOnuQinQEndVlanId = topOnuQinQEndVlanId;
    }

    public Integer getTopOnuQinQSVlanId() {
        return topOnuQinQSVlanId;
    }

    public void setTopOnuQinQSVlanId(Integer topOnuQinQSVlanId) {
        this.topOnuQinQSVlanId = topOnuQinQSVlanId;
    }

    public Integer getTopOnuQinQSTagCosDetermine() {
        return topOnuQinQSTagCosDetermine;
    }

    public void setTopOnuQinQSTagCosDetermine(Integer topOnuQinQSTagCosDetermine) {
        this.topOnuQinQSTagCosDetermine = topOnuQinQSTagCosDetermine;
    }

    public Integer getTopOnuQinQSTagCosNewValue() {
        return topOnuQinQSTagCosNewValue;
    }

    public void setTopOnuQinQSTagCosNewValue(Integer topOnuQinQSTagCosNewValue) {
        this.topOnuQinQSTagCosNewValue = topOnuQinQSTagCosNewValue;
    }

    public Integer getTopOnuQinQRowStatus() {
        return topOnuQinQRowStatus;
    }

    public void setTopOnuQinQRowStatus(Integer topOnuQinQRowStatus) {
        this.topOnuQinQRowStatus = topOnuQinQRowStatus;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getOnuIndex() {
        if (onuIndex == null) {
            onuIndex = new EponIndex(slotNo.intValue(), portNo.intValue(), onuNo.intValue()).getOnuIndex();
        }
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (null != onuIndex) {
            slotNo = EponIndex.getSlotNo(onuIndex);
            portNo = EponIndex.getPonNo(onuIndex);
            onuNo = EponIndex.getOnuNo(onuIndex);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VlanLlidOnuQinQRule [entityId=");
        builder.append(entityId);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", onuNo=");
        builder.append(onuNo);
        builder.append(", topOnuQinQStartVlanId=");
        builder.append(topOnuQinQStartVlanId);
        builder.append(", topOnuQinQEndVlanId=");
        builder.append(topOnuQinQEndVlanId);
        builder.append(", topOnuQinQSVlanId=");
        builder.append(topOnuQinQSVlanId);
        builder.append(", topOnuQinQSTagCosDetermine=");
        builder.append(topOnuQinQSTagCosDetermine);
        builder.append(", topOnuQinQSTagCosNewValue=");
        builder.append(topOnuQinQSTagCosNewValue);
        builder.append(", topOnuQinQRowStatus=");
        builder.append(topOnuQinQRowStatus);
        builder.append("]");
        return builder.toString();
    }

}