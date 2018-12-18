package com.topvision.ems.cmc.perf.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 光机温度domain
 * 
 * @author CWQ
 * @created @2017年10月25日-下午4:55:50
 *
 */
@Alias("cmcOpticalTemp")
public class CmcOpticalTemp implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -3697137680047803202L;

    private String name;
    private Long cmcId;
    private Long portIndex;
    private String location;
    private Float optTempFloat;
    private String optTempFloatStr;
    private Timestamp collectTime;
    private Long typeId;
    private String fromLastTime;
    private String mac;
    private String displayName;
    private String ip;
    private String uplinkDevice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getOptTempFloat() {
        return optTempFloat;
    }

    public void setOptTempFloat(Float optTempFloat) {
        this.optTempFloat = optTempFloat;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getFromLastTime() {
        return fromLastTime;
    }

    public void setFromLastTime(String fromLastTime) {
        this.fromLastTime = fromLastTime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    public String getOptTempFloatStr() {
        return optTempFloatStr;
    }

    public void setOptTempFloatStr(String optTempFloatStr) {
        this.optTempFloatStr = optTempFloatStr;
    }

    public String getDtStr() {
        return DateUtils.format(this.collectTime);
    }

    @Override
    public String toString() {
        return "CmcOpticalTemp [name=" + name + ",cmcId=" + cmcId + ", portIndex=" + portIndex + ", location="
                + location + ", optTempFloat=" + optTempFloat + ", collectTime=" + collectTime + ", typeId=" + typeId
                + ", fromLastTime=" + fromLastTime + ", mac=" + mac + ", displayName=" + displayName + ", ip=" + ip
                + ", uplinkDevice=" + uplinkDevice + "]";
    }
}
