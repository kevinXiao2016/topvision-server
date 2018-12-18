package com.topvision.ems.cmc.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.performance.domain.ChannelUtilization;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 设备视图 TOP 10
 * 
 * @author bryan
 * @created @2012-7-13-下午01:24:45
 * 
 */
@Alias("portalChannelUtilization")
public class PortalChannelUtilizationShow extends ChannelUtilization implements AliasesSuperType {

    private static final long serialVersionUID = 7585543842014008118L;

    private String channelName = "--";
    private String cmcTypeString;
    private Long cmcType;
    private String cmcName;
    private String name;
    private String cmcMac;
    private String topCcmtsSysMacAddr;
    private String channelLocateOnCC;
    private String ip;// CC8800B或olt8602
    private String direction;
    private String channelNo;
    private Long cmcPortId;
    private Double channelUsed;
    private Long ifType;
    private String ifDescr;
    private String typeName;
    private Timestamp dt;
    private Timestamp collectTime;
    private String uplinkDevice;
    private String cmcPortName;
    private String recentUpdateTime;

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
        this.topCcmtsSysMacAddr = cmcMac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
        this.name = cmcName;
    }

    public String getChannelLocateOnCC() {
        if (this.getChannelIndex() != null) {
            Long channelIndex = this.getChannelIndex();
            channelLocateOnCC = CmcIndexUtils.getSlotNo(channelIndex) + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(channelIndex) + Symbol.SLASH + CmcIndexUtils.getCmcId(channelIndex);
            // this.getIfIndex()
        }
        return channelLocateOnCC;
    }

    public void setChannelLocateOnCC(String channelLocateOnCC) {
        this.channelLocateOnCC = channelLocateOnCC;
    }

    @Deprecated
    public String getCmcTypeString() {
        // if (entityTypeService.isCmc8800B(this.getCmcType())) {
        // cmcTypeString = CmcConstants.CMC_TYPE_8800B_String;
        // } else if (entityTypeService.isCmc8800A(this.getCmcType())) {
        // cmcTypeString = CmcConstants.CMC_TYPE_8800A_String;
        // } else if (entityTypeService.isCmc8800C_A(this.getCmcType())) {
        // cmcTypeString = CmcConstants.CMC_TYPE_8800C_A_String;
        // } else if (entityTypeService.isCmc8800C_B(this.getCmcType())) {
        // cmcTypeString = CmcConstants.CMC_TYPE_8800C_B_String;
        // }else if(entityTypeService.isCmc8800D(getCmcType())){
        // cmcTypeString = CmcConstants.CMC_TYPE_8800D_String;
        // }
        return "" + this.getCmcType();
    }

    public void setCmcTypeString(String cmcTypeString) {
        this.cmcTypeString = cmcTypeString;
    }

    public Long getCmcType() {
        return cmcType;
    }

    public void setCmcType(Long cmcType) {
        this.cmcType = cmcType;
    }

    public String getDirection() {
        Long i = CmcIndexUtils.getChannelType(this.getChannelIndex());
        if (i != 1) {
            direction = "US";
        } else {
            direction = "DS";
        }
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getChannelNo() {
        channelNo = CmcIndexUtils.getChannelId(this.getChannelIndex()).toString();
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public Double getChannelUsed() {
        return channelUsed;
    }

    public void setChannelUsed(Double channelUsed) {
        this.channelUsed = channelUsed;
    }

    public Long getIfType() {
        return ifType;
    }

    public void setIfType(Long ifType) {
        this.ifType = ifType;
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopCcmtsSysMacAddr() {
        return topCcmtsSysMacAddr;
    }

    public void setTopCcmtsSysMacAddr(String topCcmtsSysMacAddr) {
        this.topCcmtsSysMacAddr = topCcmtsSysMacAddr;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
        this.collectTime = dt;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public String getDtStr() {
        return DateUtils.format(this.dt);
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    public String getCmcPortName() {
        return cmcPortName;
    }

    public void setCmcPortName(String cmcPortName) {
        this.cmcPortName = cmcPortName;
    }

    public String getRecentUpdateTime() {
        return recentUpdateTime;
    }

    public void setRecentUpdateTime(String recentUpdateTime) {
        this.recentUpdateTime = recentUpdateTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortalChannelUtilizationShow [channelName=");
        builder.append(channelName);
        builder.append(", cmcTypeString=");
        builder.append(cmcTypeString);
        builder.append(", cmcType=");
        builder.append(cmcType);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", name=");
        builder.append(name);
        builder.append(", cmcMac=");
        builder.append(cmcMac);
        builder.append(", topCcmtsSysMacAddr=");
        builder.append(topCcmtsSysMacAddr);
        builder.append(", channelLocateOnCC=");
        builder.append(channelLocateOnCC);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", direction=");
        builder.append(direction);
        builder.append(", channelNo=");
        builder.append(channelNo);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelUsed=");
        builder.append(channelUsed);
        builder.append(", ifType=");
        builder.append(ifType);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append(", cmcPortName=");
        builder.append(cmcPortName);
        builder.append(", recentUpdateTime=");
        builder.append(recentUpdateTime);
        builder.append("]");
        return builder.toString();
    }

}
