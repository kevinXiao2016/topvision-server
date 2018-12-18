package com.topvision.ems.epon.ofa.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 
 * @author CWQ
 * @created @2017年10月14日-上午9:52:32
 *
 */
@Alias("commonOptiTrans")
public class CommonOptiTrans implements AliasesSuperType {

    private static final long serialVersionUID = 7861234280642376316L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.1", index = true)
    private Long deviceIndex = 1L;// 设备类型
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.2")
    private String deviceType;// 设备类型
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.3")
    private String deviceName;// 设备名称
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.4")
    private String vendorName;// 生产厂商
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.5")
    private String modelNumber;// 设备型号
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.6")
    private String serialNumber;// 设备序列号
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.7")
    private String ipAddress;// 设备IP
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.8")
    private String macAddress;// 设备MAC
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.9")
    private Integer deviceAcct;// 连续运行时长
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.6.1.1.1.10")
    private String deviceMFD;// 生产日期

    private String deviceAcctStr;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getDeviceAcct() {
        return deviceAcct;
    }

    public void setDeviceAcct(Integer deviceAcct) {
        this.deviceAcct = deviceAcct;
    }

    public String getDeviceMFD() {
        return deviceMFD;
    }

    public void setDeviceMFD(String deviceMFD) {
        this.deviceMFD = deviceMFD;
    }

    public String getDeviceAcctStr() {
        return deviceAcctStr;
    }

    public void setDeviceAcctStr(String deviceAcctStr) {
        this.deviceAcctStr = deviceAcctStr;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CommonOptiTransDeviceEntry [entityId=");
        builder.append(entityId);
        builder.append(", deviceType=");
        builder.append(deviceType);
        builder.append(", deviceName=");
        builder.append(deviceName);
        builder.append(", vendorName=");
        builder.append(vendorName);
        builder.append(", modelNumber=");
        builder.append(modelNumber);
        builder.append(", serialNumber=");
        builder.append(serialNumber);
        builder.append(", ipAddress=");
        builder.append(ipAddress);
        builder.append(", macAddress=");
        builder.append(macAddress);
        builder.append(", deviceAcct=");
        builder.append(deviceAcct);
        builder.append(", deviceMFD=");
        builder.append(deviceMFD);
        builder.append(", deviceAcctStr=");
        builder.append(deviceAcctStr);
        return builder.toString();
    }
}
