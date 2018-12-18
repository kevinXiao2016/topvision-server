package com.topvision.ems.mobile.domain;

import java.sql.Timestamp;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmtsInfo implements AliasesSuperType {
    private static final long serialVersionUID = -3044789665367391499L;

    private String cmtsId;
    private Long typeId;
    private String type; // CMTS设备类型
    private String name; // CMTS别名
    private String onTime; // 在线时长
    private Long sysUpTime;
    private Timestamp dt;
    private Timestamp statusChangeTime;
    private String ip;// CMTS_IP
    private String mac;// CMTS_MAC
    private Integer state;// 在线状态
    private String onStatus; // 在线状态字符串
    private String softVer; // 软件版本
    private String hardVer; // 硬件版本
    private String interfaceInfo; // 机架号
    private Long cmcIndex; // CMC_INDEX
    private Boolean isSupportOptical;// 是否支持光机
    private String topCcmtsSysDorType;
    private Boolean isCcWithoutAgent;
    private String cmtsLocation;
    private Integer usTemp;// 上行放大模块温度
    private Integer dsTemp;// 下行放大模块温度
    private Integer outsideTemp;// DOCSIS MAC温度
    private Boolean isCcWithAgent;
    private Boolean isFOptical;

    public String getInterfaceInfo() {
        return interfaceInfo;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setInterfaceInfo(String interfaceInfo) {
        this.interfaceInfo = interfaceInfo;
    }

    public void setCmcIndex(Long cmcIndex) {
        if (cmcIndex != null) {
            this.cmcIndex = cmcIndex;
            this.interfaceInfo = CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getCmcId(cmcIndex).toString();
        }
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(String cmtsId) {
        this.cmtsId = cmtsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOnStatus() {
        return onStatus;
    }

    public void setOnStatus(String onStatus) {
        this.onStatus = onStatus;
    }

    public String getSoftVer() {
        return softVer;
    }

    public void setSoftVer(String softVer) {
        this.softVer = softVer;
    }

    public String getHardVer() {
        return hardVer;
    }

    public void setHardVer(String hardVer) {
        this.hardVer = hardVer;
    }

    public Timestamp getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(Timestamp statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

    public Boolean getIsSupportOptical() {
        return isSupportOptical;
    }

    public void setIsSupportOptical(Boolean isSupportOptical) {
        this.isSupportOptical = isSupportOptical;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTopCcmtsSysDorType() {
        return topCcmtsSysDorType;
    }

    public void setTopCcmtsSysDorType(String topCcmtsSysDorType) {
        this.topCcmtsSysDorType = topCcmtsSysDorType;
    }

    public Boolean getIsCcWithoutAgent() {
        return isCcWithoutAgent;
    }

    public void setIsCcWithoutAgent(Boolean isCcWithoutAgent) {
        this.isCcWithoutAgent = isCcWithoutAgent;
    }

    public String getCmtsLocation() {
        return cmtsLocation;
    }

    public void setCmtsLocation(String cmtsLocation) {
        this.cmtsLocation = cmtsLocation;
    }

    public Integer getUsTemp() {
        return usTemp;
    }

    public void setUsTemp(Integer usTemp) {
        this.usTemp = usTemp;
    }

    public Integer getDsTemp() {
        return dsTemp;
    }

    public void setDsTemp(Integer dsTemp) {
        this.dsTemp = dsTemp;
    }

    public Integer getOutsideTemp() {
        return outsideTemp;
    }

    public void setOutsideTemp(Integer outsideTemp) {
        this.outsideTemp = outsideTemp;
    }

    public Boolean getIsCcWithAgent() {
        return isCcWithAgent;
    }

    public void setIsCcWithAgent(Boolean isCcWithAgent) {
        this.isCcWithAgent = isCcWithAgent;
    }

    public Boolean getIsFOptical() {
        return isFOptical;
    }

    public void setIsFOptical(Boolean isFOptical) {
        this.isFOptical = isFOptical;
    }

}
