/***********************************************************************
 * $Id: UniFilterTable.java,v1.0 2013年12月10日 上午10:09:25 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.domain;

import java.io.Serializable;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2013年12月10日-上午10:09:25
 *
 */
public class CameraFilterTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6188934784858682015L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.3.1.1.1", index = true)
    private Integer slotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.3.1.1.2", index = true)
    private Integer ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.3.1.1.3", index = true)
    private Integer onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.3.1.1.4", index = true)
    private Integer cameraIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.3.1.1.5", writable = true, type = "OctetString")
    private String mac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.3.1.1.6", writable = true, type = "IpAddress")
    private String ip;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.3.1.1.7", writable = true, type = "Integer32")
    private Integer rowStatus;
    private Long eponIndex;
    private String cameraType;
    private String noteInfo;
    private String onuInfo;
    private String location;
    private String cameraNo;
    private String entityIp;

    public Long getEponIndex() {
        if (eponIndex == null) {
            eponIndex = EponIndex.getOnuIndex(slotIndex, ponIndex, onuIndex);
        }
        return eponIndex;
    }

    public void setEponIndex(Long index) {
        this.eponIndex = index;
        this.slotIndex = EponIndex.getSlotNo(index).intValue();
        this.ponIndex = EponIndex.getPonNo(index).intValue();
        this.onuIndex = EponIndex.getOnuNo(index).intValue();
        this.onuInfo = EponIndex.getStringFromIndex(index);
    }

    public String getCameraNo() {
        return cameraNo;
    }

    public void setCameraNo(String cameraNo) {
        this.cameraNo = cameraNo;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Integer ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Integer getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Integer onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getCameraIndex() {
        return cameraIndex;
    }

    public void setCameraIndex(Integer cameraIndex) {
        this.cameraIndex = cameraIndex;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        if (mac != null) {
            this.mac = EponUtil.getMacStringFromNoISOControl(mac);
        } else {
            this.mac = null;
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getNoteInfo() {
        return noteInfo;
    }

    public void setNoteInfo(String noteInfo) {
        this.noteInfo = noteInfo;
    }

    public String getOnuInfo() {
        return onuInfo;
    }

    public void setOnuInfo(String onuInfo) {
        this.onuInfo = onuInfo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CameraFilterTable [entityId=");
        builder.append(entityId);
        builder.append(", slotIndex=");
        builder.append(slotIndex);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", cameraIndex=");
        builder.append(cameraIndex);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append(", eponIndex=");
        builder.append(eponIndex);
        builder.append(", cameraType=");
        builder.append(cameraType);
        builder.append(", noteInfo=");
        builder.append(noteInfo);
        builder.append(", onuInfo=");
        builder.append(onuInfo);
        builder.append(", location=");
        builder.append(location);
        builder.append(", cameraNo=");
        builder.append(cameraNo);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append("]");
        return builder.toString();
    }

}
