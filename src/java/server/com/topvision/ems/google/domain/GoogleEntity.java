/***********************************************************************
 * $Id: GoogleEntity.java,v 1.1 Oct 20, 2008 8:38:58 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.google.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

/**
 * @Create Date Oct 20, 2008 8:38:58 PM
 * 
 * @author kelers
 * 
 */
/**
 * @author dosion
 * @created @2013-3-27-下午01:38:25
 * 
 */
@Alias("googleEntity")
public class GoogleEntity extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 1563300445559640684L;
    private long entityId;
    private double longitude = 0;
    private double latitude = 0;
    private byte zoom = 4;
    private byte minZoom;
    private byte maxZoom;
    private int typeId;

    private String icon48;
    private String ip;
    private String name;
    private String url;

    private String text;
    private String modulePath;
    private boolean fixed;
    private String location;

    /*
     * 为了在google地图上支持8800B设备，添加parentId与mac成员 Added by huangdognsheng
     */
    private Long parentId;
    private String mac;

    /**
     * @return the entityId
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * @return the icon48
     */
    public String getIcon48() {
        return icon48;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return the maxZoom
     */
    public byte getMaxZoom() {
        return maxZoom;
    }

    /**
     * @return the minZoom
     */
    public byte getMinZoom() {
        return minZoom;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the zoom
     */
    public byte getZoom() {
        return zoom;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param icon48
     *            the icon48 to set
     */
    public void setIcon48(String icon48) {
        this.icon48 = icon48;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @param maxZoom
     *            the maxZoom to set
     */
    public void setMaxZoom(byte maxZoom) {
        this.maxZoom = maxZoom;
    }

    /**
     * @param minZoom
     *            the minZoom to set
     */
    public void setMinZoom(byte minZoom) {
        this.minZoom = minZoom;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param zoom
     *            the zoom to set
     */
    public void setZoom(byte zoom) {
        this.zoom = zoom;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setType(int typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the modulePath
     */
    public String getModulePath() {
        return modulePath;
    }

    /**
     * @param modulePath
     *            the modulePath to set
     */
    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    /**
     * @return the fixed
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * @param fixed
     *            the fixed to set
     */
    public void setFixed(int fixed) {
        this.fixed = fixed == 0 ? false : true;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
 
}
