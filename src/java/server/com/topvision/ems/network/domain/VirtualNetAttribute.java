/***********************************************************************
 * $Id: VirtualNetAttribute.java,v1.0 2012-2-24 上午09:31:54 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author loyal
 * @created @2012-2-24-上午09:31:54
 * 
 */
public class VirtualNetAttribute extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -7970561838019393746L;
    private Long virtualNetId;
    private Long folderId;
    private String virtualName;
    private Integer virtualType;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    private Integer zoom;
    private String icon;
    private Integer fixed;
    private Integer visiable;

    /**
     * @return the virtualNetId
     */
    public Long getVirtualNetId() {
        return virtualNetId;
    }

    /**
     * @param virtualNetId
     *            the virtualNetId to set
     */
    public void setVirtualNetId(Long virtualNetId) {
        this.virtualNetId = virtualNetId;
    }

    /**
     * @return the folderId
     */
    public Long getFolderId() {
        return folderId;
    }

    /**
     * @param folderId
     *            the folderId to set
     */
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    /**
     * @return the virtualName
     */
    public String getVirtualName() {
        return virtualName;
    }

    /**
     * @param virtualName
     *            the virtualName to set
     */
    public void setVirtualName(String virtualName) {
        this.virtualName = virtualName;
    }

    /**
     * @return the virtualType
     */
    public Integer getVirtualType() {
        return virtualType;
    }

    /**
     * @param virtualType
     *            the virtualType to set
     */
    public void setVirtualType(Integer virtualType) {
        this.virtualType = virtualType;
    }

    /**
     * @return the x
     */
    public Integer getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public Integer getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return the zoom
     */
    public Integer getZoom() {
        return zoom;
    }

    /**
     * @param zoom
     *            the zoom to set
     */
    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the fixed
     */
    public Integer getFixed() {
        return fixed;
    }

    /**
     * @param fixed
     *            the fixed to set
     */
    public void setFixed(Integer fixed) {
        this.fixed = fixed;
    }

    /**
     * @return the visiable
     */
    public Integer getVisiable() {
        return visiable;
    }

    /**
     * @param visiable
     *            the visiable to set
     */
    public void setVisiable(Integer visiable) {
        this.visiable = visiable;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the modifyTime
     */
    public Timestamp getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     *            the modifyTime to set
     */
    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VirtualNetAttribute [virtualNetId=");
        builder.append(virtualNetId);
        builder.append(", folderId=");
        builder.append(folderId);
        builder.append(", virtualName=");
        builder.append(virtualName);
        builder.append(", virtualType=");
        builder.append(virtualType);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", modifyTime=");
        builder.append(modifyTime);
        builder.append(", x=");
        builder.append(x);
        builder.append(", y=");
        builder.append(y);
        builder.append(", width=");
        builder.append(width);
        builder.append(", height=");
        builder.append(height);
        builder.append(", zoom=");
        builder.append(zoom);
        builder.append(", icon=");
        builder.append(icon);
        builder.append(", fixed=");
        builder.append(fixed);
        builder.append(", visiable=");
        builder.append(visiable);
        builder.append("]");
        return builder.toString();
    }
}
