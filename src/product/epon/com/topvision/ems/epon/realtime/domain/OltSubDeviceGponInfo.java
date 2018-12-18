package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * Created by jay on 17-2-21.
 */
public class OltSubDeviceGponInfo implements OltOnuBaseInfo {
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.1", type = "Integer32", index = true)
    private Long onuDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.2", type = "OctetString")
    private String onuName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.3", type = "OctetString")
    private String identifyKey;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.6", type = "OctetString")
    private String onuEquipmentID;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.7", type = "Integer32")
    private Integer operationStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.9", type = "Integer32")
    private Integer testDistance;

    private Integer onuType;

    public Long getOnuDeviceIndex() {
        return onuDeviceIndex;
    }

    public void setOnuDeviceIndex(Long onuDeviceIndex) {
        this.onuDeviceIndex = onuDeviceIndex;
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public String getIdentifyKey() {
        return identifyKey;
    }

    public void setIdentifyKey(String identifyKey) {
        this.identifyKey = identifyKey;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getTestDistance() {
        return testDistance;
    }

    public void setTestDistance(Integer testDistance) {
        this.testDistance = testDistance;
    }

    public String getOnuEquipmentID() {
        return onuEquipmentID;
    }

    public void setOnuEquipmentID(String onuEquipmentID) {
        this.onuEquipmentID = onuEquipmentID;
    }

    public Integer getOnuType() {
        if (onuEquipmentID == null || onuEquipmentID.equalsIgnoreCase("") || !onuEquipmentID.contains("PN")) {
            onuType = 255;
        } else {
            onuType = Integer.parseInt(onuEquipmentID.substring(onuEquipmentID.length() - 2, onuEquipmentID.length()),
                    16);
        }
        return onuType;
    }
}
