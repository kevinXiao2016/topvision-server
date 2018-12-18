/***********************************************************************
 * $Id: OltPortFlowPerf.java,v1.0 2013-8-7 下午03:54:00 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-8-7-下午03:54:00
 * 
 */
public class OltPortFlowPerf implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 1181513991273581241L;
    public static final Integer SNI_PORT = 1;
    public static final Integer PON_PORT = 2;
    public static final Integer ONUPON_PORT = 3;
    public static final Integer UNI_PORT = 4;
    private Long entityId;
    private Integer portType;
    private Long portIndex;
    private Long sniIndex;
    private Long ponIndex;
    private Long onuPonIndex;
    private Long uniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.1", index = true)
    private Long stats15DeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.2", index = true)
    private Long stats15CardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.3", index = true)
    private Long stats15PortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.4", index = true)
    private Long stats15Index;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.5")
    private Long stats15InOctets;
    private Long portInOctets;
    private Float portInSpeed;
    // 通过portInSpeed转换为Mbps的速率
    private Float portInSpeedUnitM;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.27")
    private Long stats15OutOctets;
    private Long portOutOctets;
    private Float portOutSpeed;
    private Float portOutSpeedUnitM;
    private Timestamp collectTime;
    private Boolean collectStatus;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the portIndex
     */
    public Long getPortIndex() {
        if (portIndex == null) {
            if (EponIndex.getOnuNoByMibDeviceIndex(stats15DeviceIndex) == 0) {
                // 此时代表为OLT
                portIndex = EponIndex.getPonIndexByMibDeviceIndex(stats15DeviceIndex);
            } else {
                // 此时代表为ONU
                portIndex = EponIndex.getUniIndexByMibIndex(stats15DeviceIndex, stats15CardIndex, stats15PortIndex);
            }
        }
        return portIndex;
    }

    /**
     * @param portIndex
     *            the portIndex to set
     */
    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    /**
     * @return the stats15DeviceIndex
     */
    public Long getStats15DeviceIndex() {
        return stats15DeviceIndex;
    }

    /**
     * @param stats15DeviceIndex
     *            the stats15DeviceIndex to set
     */
    public void setStats15DeviceIndex(Long stats15DeviceIndex) {
        this.stats15DeviceIndex = stats15DeviceIndex;
    }

    /**
     * @return the stats15CardIndex
     */
    public Long getStats15CardIndex() {
        return stats15CardIndex;
    }

    /**
     * @param stats15CardIndex
     *            the stats15CardIndex to set
     */
    public void setStats15CardIndex(Long stats15CardIndex) {
        this.stats15CardIndex = stats15CardIndex;
    }

    /**
     * @return the stats15PortIndex
     */
    public Long getStats15PortIndex() {
        return stats15PortIndex;
    }

    /**
     * @param stats15PortIndex
     *            the stats15PortIndex to set
     */
    public void setStats15PortIndex(Long stats15PortIndex) {
        this.stats15PortIndex = stats15PortIndex;
    }

    /**
     * @return the stats15Index
     */
    public Long getStats15Index() {
        return stats15Index;
    }

    /**
     * @param stats15Index
     *            the stats15Index to set
     */
    public void setStats15Index(Long stats15Index) {
        this.stats15Index = stats15Index;
    }

    /**
     * @return the stats15InOctets
     */
    public Long getStats15InOctets() {
        return stats15InOctets;
    }

    /**
     * @param stats15InOctets
     *            the stats15InOctets to set
     */
    public void setStats15InOctets(Long stats15InOctets) {
        this.stats15InOctets = stats15InOctets;
    }

    /**
     * @return the stats15OutOctets
     */
    public Long getStats15OutOctets() {
        return stats15OutOctets;
    }

    /**
     * @param stats15OutOctets
     *            the stats15OutOctets to set
     */
    public void setStats15OutOctets(Long stats15OutOctets) {
        this.stats15OutOctets = stats15OutOctets;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the sniIndex
     */
    public Long getSniIndex() {
        return sniIndex;
    }

    /**
     * @param sniIndex
     *            the sniIndex to set
     */
    public void setSniIndex(Long sniIndex) {
        // 设置SNI口采集对象
        if (sniIndex != null) {
            stats15DeviceIndex = EponIndex.getOnuMibIndexByIndex(sniIndex);
            stats15CardIndex = 0L;
            stats15PortIndex = 0L;
            this.portIndex = sniIndex;
            this.portType = SNI_PORT;
        }
        this.sniIndex = sniIndex;
    }

    /**
     * @return the ponIndex
     */
    public Long getPonIndex() {
        return ponIndex;
    }

    /**
     * @param ponIndex
     *            the ponIndex to set
     */
    public void setPonIndex(Long ponIndex) {
        // 设置PON口采集对象
        if (ponIndex != null) {
            stats15DeviceIndex = EponIndex.getOnuMibIndexByIndex(ponIndex);
            stats15CardIndex = 0L;
            stats15PortIndex = 0L;
            this.portIndex = ponIndex;
            this.portType = PON_PORT;
        }
        this.ponIndex = ponIndex;
    }

    /**
     * @return the onuPonIndex
     */
    public Long getOnuPonIndex() {
        return onuPonIndex;
    }

    /**
     * @param onuPonIndex
     *            the onuPonIndex to set
     */
    public void setOnuPonIndex(Long onuPonIndex) {
        // 设置ONU PON口采集对象
        if (onuPonIndex != null) {
            stats15DeviceIndex = EponIndex.getOnuMibIndexByIndex(onuPonIndex);
            stats15CardIndex = EponIndex.getOnuCardNo(onuPonIndex);
            if (EponIndex.getUniNo(onuPonIndex) == 255) {
                // 这里如果设置正确的ONU PON Index 则只会进入此处
                stats15PortIndex = 0l;
            } else {
                stats15PortIndex = EponIndex.getUniNo(onuPonIndex);
            }
            this.portIndex = onuPonIndex;
            this.portType = ONUPON_PORT;
        }
        this.onuPonIndex = onuPonIndex;
    }

    /**
     * @return the uniIndex
     */
    public Long getUniIndex() {
        return uniIndex;
    }

    /**
     * @param uniIndex
     *            the uniIndex to set
     */
    public void setUniIndex(Long uniIndex) {
        // 设置UNI口采集对象
        if (uniIndex != null) {
            stats15DeviceIndex = EponIndex.getOnuMibIndexByIndex(uniIndex);
            stats15CardIndex = EponIndex.getOnuCardNo(uniIndex);
            if (EponIndex.getUniNo(uniIndex) == 255) {
                stats15PortIndex = 0l;
            } else {
                // 这里如果设置正确的UNI Index 则只会进入此处
                stats15PortIndex = EponIndex.getUniNo(uniIndex);
            }
            this.portIndex = uniIndex;
            this.portType = UNI_PORT;
        }
        this.uniIndex = uniIndex;
    }

    /**
     * @return the portType
     */
    public Integer getPortType() {
        return portType;
    }

    /**
     * @param portType
     *            the portType to set
     */
    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    /**
     * @return the collectStatus
     */
    public Boolean getCollectStatus() {
        return collectStatus;
    }

    /**
     * @param collectStatus
     *            the collectStatus to set
     */
    public void setCollectStatus(Boolean collectStatus) {
        this.collectStatus = collectStatus;
    }

    /**
     * @return the portInOctets
     */
    public Long getPortInOctets() {
        return portInOctets;
    }

    /**
     * @param portInOctets
     *            the portInOctets to set
     */
    public void setPortInOctets(Long portInOctets) {
        this.portInOctets = portInOctets;
    }

    /**
     * @return the portInSpeed
     */
    public Float getPortInSpeed() {
        return portInSpeed;
    }

    /**
     * @param portInSpeed
     *            the portInSpeed to set
     */
    public void setPortInSpeed(Float portInSpeed) {
        if (portInSpeed != null) {
            this.portInSpeedUnitM = portInSpeed / 1024 / 1024;
        }
        this.portInSpeed = portInSpeed;
    }

    /**
     * @return the portOutOctets
     */
    public Long getPortOutOctets() {
        return portOutOctets;
    }

    /**
     * @param portOutOctets
     *            the portOutOctets to set
     */
    public void setPortOutOctets(Long portOutOctets) {
        this.portOutOctets = portOutOctets;
    }

    /**
     * @return the portOutSpeed
     */
    public Float getPortOutSpeed() {
        return portOutSpeed;
    }

    /**
     * @param portOutSpeed
     *            the portOutSpeed to set
     */
    public void setPortOutSpeed(Float portOutSpeed) {
        if (portOutSpeed != null) {
            this.portOutSpeedUnitM = portOutSpeed / 1024 / 1024;
        }
        this.portOutSpeed = portOutSpeed;
    }

    public Float getPortInSpeedUnitM() {
        return portInSpeedUnitM;
    }

    public void setPortInSpeedUnitM(Float portInSpeedUnitM) {
        this.portInSpeedUnitM = portInSpeedUnitM;
    }

    public Float getPortOutSpeedUnitM() {
        return portOutSpeedUnitM;
    }

    public void setPortOutSpeedUnitM(Float portOutSpeedUnitM) {
        this.portOutSpeedUnitM = portOutSpeedUnitM;
    }

}
