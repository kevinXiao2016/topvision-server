/***********************************************************************
 * $Id: OnuUniCpe.java,v1.0 2016年7月6日 上午10:15:49 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.domain;

import jnr.ffi.Struct.int16_t;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2016年7月6日-上午10:15:49
 *
 */
public class OnuUniCpe implements AliasesSuperType {
    private static final long serialVersionUID = 2225561684183999577L;
    private Long entityId;
    private Long uniIndex;
    private Long uniNo;
    private Long uniId;
    private String mac;
    private Integer type;
    private String renderType;
    private Long onuId;
    private Integer vlan;
    private String ipAddress;
    private Integer cpeType;

    public String getUniDisplayName() {
        return EponIndex.getUniStringByIndex(uniIndex).toString();
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Long getUniNo() {
        if (this.uniIndex == null) {
            return null;
        }
        return EponIndex.getUniNo(this.uniIndex);
    }

    public void setUniNo(Long uniNo) {
        this.uniNo = uniNo;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getType() {
    	return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	public String getRenderType() {
		return renderType;
	}

	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}

	public Integer getVlan() {
        return vlan;
    }

    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getCpeType() {
        return cpeType;
    }

    public void setCpeType(Integer cpeType) {
        this.cpeType = cpeType;
    }

    @Override
    public String toString() {
        return "OnuUniCpe [entityId=" + entityId + ", uniIndex=" + uniIndex + ", uniNo=" + uniNo + ", uniId=" + uniId
                + ", mac=" + mac + ", type=" + type + ", onuId=" + onuId + ", vlan=" + vlan + ", ipAddress="
                + ipAddress + ", cpeType=" + cpeType + "]";
    }

}
