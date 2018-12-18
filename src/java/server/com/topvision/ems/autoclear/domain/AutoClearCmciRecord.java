package com.topvision.ems.autoclear.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * EMS-14742 CMCI型离线自动清除功能  新增domain类
 * @author w1992wishes
 * @created @2017年8月7日-上午10:03:08
 *
 */
public class AutoClearCmciRecord implements AliasesSuperType {
    private static final long serialVersionUID = -4464303437780096313L;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmcType;
    private Long cmcEntityId;
    private String alias;//别名
    private String typeName;//类型
    private String ipAddress;
    private String macAddr;
    private Timestamp offlineTime;
    private String offlineTimeString;
    private Timestamp clearTime; // 清除时间
    private String clearTimeString;
    
    public static class Builder{
        private final Long cmcId;
        private final Long cmcIndex;
        private final Long cmcType;
        private final Long cmcEntityId;
        private String alias;//别名
        private String typeName;//类型
        private String ipAddress;
        private String macAddr;
        private Timestamp offlineTime;
        private Timestamp clearTime; // 清除时间
        
        public Builder(Long cmcId, Long cmcIndex, Long cmcType, Long cmcEntityId){
            this.cmcId = cmcId;
            this.cmcIndex = cmcIndex;
            this.cmcType = cmcType;
            this.cmcEntityId = cmcEntityId;
        }
        
        public Builder alias(String alias){
            this.alias = alias;
            return this;
        }
        
        public Builder typeName(String typeName){
            this.typeName = typeName;
            return this;
        }
        
        public Builder ipAddress(String ipAddress){
            this.ipAddress = ipAddress;
            return this;
        }
        
        public Builder macAddr(String macAddr){
            this.macAddr = macAddr;
            return this;
        }
        
        public Builder offlineTime(Timestamp offlineTime){
            this.offlineTime = offlineTime;
            return this;
        }
        
        public Builder clearTime(Timestamp clearTime){
            this.clearTime = clearTime;
            return this;
        }
        
        public AutoClearCmciRecord build(){
            return new AutoClearCmciRecord(this);
        }
    }
    
    private AutoClearCmciRecord(Builder builder){
        this.cmcId = builder.cmcId;
        this.cmcIndex = builder.cmcIndex;
        this.cmcType = builder.cmcType;
        this.cmcEntityId = builder.cmcEntityId;
        this.alias = builder.alias;
        this.typeName = builder.typeName;
        this.ipAddress = builder.ipAddress;
        this.macAddr = builder.macAddr;
        this.offlineTime = builder.offlineTime;
        this.clearTime = builder.clearTime;
    }
    
    public AutoClearCmciRecord(){}
    
    public Long getCmcId() {
        return cmcId;
    }
    
    public Long getCmcIndex() {
        return cmcIndex;
    }
    
    public Long getCmcType() {
        return cmcType;
    }
    
    public Long getCmcEntityId() {
        return cmcEntityId;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public String getMacAddr() {
        return macAddr;
    }
    
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }
    
    public void setCmcType(Long cmcType) {
        this.cmcType = cmcType;
    }
    
    public void setCmcEntityId(Long cmcEntityId) {
        this.cmcEntityId = cmcEntityId;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }
    
    public Timestamp getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Timestamp offlineTime) {
        this.offlineTime = offlineTime;
    }
    
    public String getOfflineTimeString() {
        if (offlineTimeString == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            offlineTimeString = sdf.format(offlineTime);
        }
        return offlineTimeString;
    }

    public void setOfflineTimeString(String offlineTimeString) {
        this.offlineTimeString = offlineTimeString;
    }
    
    public Timestamp getClearTime() {
        return clearTime;
    }

    public void setClearTime(Timestamp clearTime) {
        this.clearTime = clearTime;
    }

    public String getClearTimeString() {
        if (clearTimeString == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            clearTimeString = sdf.format(clearTime);
        }
        return clearTimeString;
    }

    public void setClearTimeString(String clearTimeString) {
        this.clearTimeString = clearTimeString;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmcType=");
        builder.append(cmcType);
        builder.append(", cmcEntityId=");
        builder.append(cmcEntityId);
        builder.append(", alias=");
        builder.append(alias);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", ipAddress=");
        builder.append(ipAddress);
        builder.append(", macAddr=");
        builder.append(macAddr);
        builder.append(", offlineTime=");
        builder.append(offlineTime);
        builder.append(", clearTime=");
        builder.append(clearTime);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
    
}
