package com.topvision.ems.cmc.ccmts.facade.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 用于清除离线cmci型设备，因为只是从数据库查出数据进行接收，所以没有用cmcAttribute类，它很重
 * @created @2017年8月7日-上午10:11:52
 *
 */
@Alias("cmciAttribute")
public class CmciAttribute extends CmcEntity implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = -7592715433915533327L;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmcType;
    private Long cmcEntityId;
    private String alias;//别名
    private String typeName;//类型
    private String ipAddress;
    private String macAddr;
    private Timestamp offlineTime;
    
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

    public Timestamp getOfflinetime() {
        return offlineTime;
    }

    public void setOfflinetime(Timestamp offlineTime) {
        this.offlineTime = offlineTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(", cmcId=");
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
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
