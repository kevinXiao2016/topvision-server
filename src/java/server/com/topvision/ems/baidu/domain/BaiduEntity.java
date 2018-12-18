/***********************************************************************
 * $Id: BaiduEntity.java,v1.0 2015年9月16日 上午10:04:54 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.baidu.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author loyal
 * @created @2015年9月16日-上午10:04:54
 * 
 */
@Alias("baiduEntity")
public class BaiduEntity extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -5266841552644353949L;
    private long entityId;
    private double longitude;
    private double latitude;
    private byte zoom;
    private byte minZoom;
    private byte maxZoom;
    private Long typeId;

    private String icon16;
    private String icon32;
    private String icon48;
    private String ip;
    private String name;
    private String url;

    private String text;
    private String modulePath;
    private boolean fixed;
    private String location;
    private String mac;
    private String displayName;
    private Integer state;

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public byte getZoom() {
        return zoom;
    }

    public void setZoom(byte zoom) {
        this.zoom = zoom;
    }

    public byte getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(byte minZoom) {
        this.minZoom = minZoom;
    }

    public byte getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(byte maxZoom) {
        this.maxZoom = maxZoom;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getIcon32() {
        return icon32;
    }

    public void setIcon32(String icon32) {
        this.icon32 = icon32;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getIcon16() {
        return icon16;
    }

    public void setIcon16(String icon16) {
        this.icon16 = icon16;
    }

    public String getIcon48() {
        return icon48;
    }

    public void setIcon48(String icon48) {
        this.icon48 = icon48;
    }
    
}
