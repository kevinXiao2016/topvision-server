/***********************************************************************
 * $Id: SendConfigEntity.java,v1.0 2014年7月17日 下午4:13:26 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.sql.Timestamp;

import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author loyal
 * @created @2014年7月17日-下午4:13:26
 *
 */
public class SendConfigEntity extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 3928971961716355551L;
    public static Integer NOSTART = 1;
    public static Integer TELNETERROR = 2;
    public static Integer CONFIGERROR = 3;
    public static Integer SUCCESS = 4;
    private Long entityId;
    private Long typeId;
    private Long ip;
    private Long resultId;
    private String name;
    private String ipString;
    private String mac;
    private String typeName;
    private String folderName;
    private Timestamp dt;
    private String dtString;
    private Long cmcIndex;
    private Long onuIndex;
    private Integer state = NOSTART;
    private String uplinkDevice;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getIp() {
        return new IpUtils(ipString).longValue();
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Timestamp getDt() {
        return dt;
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

    public String getIpString() {
        return ipString;
    }

    public void setIpString(String ipString) {
        this.ipString = ipString;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public String getDtString() {
        return dtString;
    }

    public void setDtString(String dtString) {
        this.dtString = dtString;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SendConfigEntity [entityId=");
        builder.append(entityId);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", name=");
        builder.append(name);
        builder.append(", ipString=");
        builder.append(ipString);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", folderName=");
        builder.append(folderName);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", state=");
        builder.append(state);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SendConfigEntity other = (SendConfigEntity) obj;
        if (entityId == null) {
            if (other.entityId != null)
                return false;
        } else if (!entityId.equals(other.entityId))
            return false;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        return true;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }
}
