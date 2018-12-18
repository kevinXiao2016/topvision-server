/***********************************************************************
 * $Id: OltTopOnuCapability.java,v1.0 2011-9-26 上午09:18:16 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * Onu扩展能力
 * 
 * @author zhanglongyang
 * 
 */
public class OltTopOnuCapability implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4682843933096808561L;
    private Long entityId;
    private Long onuId;
    private Long ponId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.1", index = true)
    private Long topOnuCapCardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.2", index = true)
    private Long topOnuCapPonNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.3", index = true)
    private Long topOnuCapOnuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.4", writable = true, type = "Integer32")
    private Integer capDeregister;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.5", writable = true, type = "Integer32")
    private Integer capAddrMaxQuantity;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.6", writable = true, type = "Integer32")
    private Integer ponPerfStats15minuteEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.7", writable = true, type = "Integer32")
    private Integer ponPerfStats24hourEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.8", writable = true, type = "Integer32")
    private Integer temperatureDetectEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.9")
    private Integer currentTemperature;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.10", writable = true, type = "Integer32")
    private Integer onuIsolationEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.11", writable = true, type = "Integer32")
    private Integer topOnuAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.12")
    private String topOnuHardwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.13", writable = true, type = "IpAddress")
    private String topOnuMgmtIp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.14", writable = true, type = "IpAddress")
    private String topOnuNetMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.15", writable = true, type = "IpAddress")
    private String topOnuGateway;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.1.1.16")
    private String topOnuExtAttr;
    // 将能力集拼接成字符串的形式
    private String topOnuCapabilityStr;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        if (onuIndex == null) {
            onuIndex = new EponIndex(topOnuCapCardNo.intValue(), topOnuCapPonNo.intValue(), topOnuCapOnuNo.intValue())
                    .getOnuIndex();
        }
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuCapCardNo = EponIndex.getSlotNo(onuIndex);
        topOnuCapPonNo = EponIndex.getPonNo(onuIndex);
        topOnuCapOnuNo = EponIndex.getOnuNo(onuIndex);
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getTopOnuCapCardNo() {
        return topOnuCapCardNo;
    }

    public void setTopOnuCapCardNo(Long topOnuCapCardNo) {
        this.topOnuCapCardNo = topOnuCapCardNo;
    }

    public Integer getCapDeregister() {
        return capDeregister;
    }

    public void setCapDeregister(Integer capDeregister) {
        this.capDeregister = capDeregister;
    }

    public Integer getCapAddrMaxQuantity() {
        return capAddrMaxQuantity;
    }

    public void setCapAddrMaxQuantity(Integer capAddrMaxQuantity) {
        this.capAddrMaxQuantity = capAddrMaxQuantity;
    }

    public Long getTopOnuCapOnuNo() {
        return topOnuCapOnuNo;
    }

    public void setTopOnuCapOnuNo(Long topOnuCapOnuNo) {
        this.topOnuCapOnuNo = topOnuCapOnuNo;
    }

    public Long getTopOnuCapPonNo() {
        return topOnuCapPonNo;
    }

    public void setTopOnuCapPonNo(Long topOnuCapPonNo) {
        this.topOnuCapPonNo = topOnuCapPonNo;
    }

    public Integer getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(Integer currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public Integer getPonPerfStats15minuteEnable() {
        return ponPerfStats15minuteEnable;
    }

    public void setPonPerfStats15minuteEnable(Integer ponPerfStats15minuteEnable) {
        this.ponPerfStats15minuteEnable = ponPerfStats15minuteEnable;
    }

    public Integer getPonPerfStats24hourEnable() {
        return ponPerfStats24hourEnable;
    }

    public void setPonPerfStats24hourEnable(Integer ponPerfStats24hourEnable) {
        this.ponPerfStats24hourEnable = ponPerfStats24hourEnable;
    }

    public Integer getTemperatureDetectEnable() {
        return temperatureDetectEnable;
    }

    public void setTemperatureDetectEnable(Integer temperatureDetectEnable) {
        this.temperatureDetectEnable = temperatureDetectEnable;
    }

    public Integer getOnuIsolationEnable() {
        return onuIsolationEnable;
    }

    public void setOnuIsolationEnable(Integer onuIsolationEnable) {
        this.onuIsolationEnable = onuIsolationEnable;
    }

    public Integer getTopOnuAction() {
        return topOnuAction;
    }

    public void setTopOnuAction(Integer topOnuAction) {
        this.topOnuAction = topOnuAction;
    }

    public String getTopOnuHardwareVersion() {
        return topOnuHardwareVersion;
    }

    public void setTopOnuHardwareVersion(String topOnuHardwareVersion) {
        this.topOnuHardwareVersion = topOnuHardwareVersion;
    }

    public String getTopOnuMgmtIp() {
        return topOnuMgmtIp;
    }

    public void setTopOnuMgmtIp(String topOnuMgmtIp) {
        this.topOnuMgmtIp = topOnuMgmtIp;
    }

    public String getTopOnuNetMask() {
        return topOnuNetMask;
    }

    public void setTopOnuNetMask(String topOnuNetMask) {
        this.topOnuNetMask = topOnuNetMask;
    }

    public String getTopOnuGateway() {
        return topOnuGateway;
    }

    public void setTopOnuGateway(String topOnuGateway) {
        this.topOnuGateway = topOnuGateway;
    }

    /**
     * @return the topOnuExtAttr
     */
    public String getTopOnuExtAttr() {
        return topOnuExtAttr;
    }

    /**
     * @param topOnuExtAttr
     *            the topOnuExtAttr to set
     */
    public void setTopOnuExtAttr(String topOnuExtAttr) {
        this.topOnuExtAttr = topOnuExtAttr;
    }

    public String getTopOnuCapabilityStr() {
        StringBuffer sb = new StringBuffer();
        if (topOnuExtAttr!=null && !topOnuExtAttr.equals("")) {
            // 00:04:00:00:00:00:01:00:01:00:00:00:00:00:00:00
            String[] split = topOnuExtAttr.split(":");
            // 1GE+3FE+CATV+WLAN
            String ge = split[0];
            String fe = split[1];
            String catv = split[8];
            String wlan = split[6];
            if (!ge.equals("00")) {
                sb.append(Integer.valueOf(ge)).append("GE+");
            }
            if (!fe.equals("00")) {
                sb.append(Integer.valueOf(fe)).append("FE+");
            }
            if (!catv.equals("00")) {
                if (catv.equals("01")) {
                    sb.append("CATV+");
                }else {
                    sb.append(Integer.valueOf(catv)).append("CATV+");
                }
                
            }
            if (!wlan.equals("00")) {
                if (catv.equals("01")) {
                    sb.append("WLAN");
                }else {
                    sb.append(Integer.valueOf(wlan)).append("WLAN");
                }
            }
            topOnuCapabilityStr = sb.toString();
            if (topOnuCapabilityStr!=null && !topOnuCapabilityStr.equals("")) {
                if (topOnuCapabilityStr.substring(topOnuCapabilityStr.length() - 1).equals("+")) {
                    topOnuCapabilityStr = topOnuCapabilityStr.substring(0, topOnuCapabilityStr.length() - 1);
                }
            }
        }
        return topOnuCapabilityStr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltTopOnuCapability [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topOnuCapCardNo=");
        builder.append(topOnuCapCardNo);
        builder.append(", topOnuCapPonNo=");
        builder.append(topOnuCapPonNo);
        builder.append(", topOnuCapOnuNo=");
        builder.append(topOnuCapOnuNo);
        builder.append(", capDeregister=");
        builder.append(capDeregister);
        builder.append(", capAddrMaxQuantity=");
        builder.append(capAddrMaxQuantity);
        builder.append(", ponPerfStats15minuteEnable=");
        builder.append(ponPerfStats15minuteEnable);
        builder.append(", ponPerfStats24hourEnable=");
        builder.append(ponPerfStats24hourEnable);
        builder.append(", temperatureDetectEnable=");
        builder.append(temperatureDetectEnable);
        builder.append(", currentTemperature=");
        builder.append(currentTemperature);
        builder.append(", onuIsolationEnable=");
        builder.append(onuIsolationEnable);
        builder.append(", topOnuAction=");
        builder.append(topOnuAction);
        builder.append(", topOnuHardwareVersion=");
        builder.append(topOnuHardwareVersion);
        builder.append(", topOnuMgmtIp=");
        builder.append(topOnuMgmtIp);
        builder.append(", topOnuNetMask=");
        builder.append(topOnuNetMask);
        builder.append(", topOnuGateway=");
        builder.append(topOnuGateway);
        builder.append(", topOnuExtAttr=");
        builder.append(topOnuExtAttr);
        builder.append("]");
        return builder.toString();
    }

}