/***********************************************************************
 * $Id: VirtualNetEntityRela.java,v1.0 2012-2-23 下午01:18:10 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author loyal
 * @created @2012-2-23-下午01:18:10
 * 
 */
public class VirtualNetEntityRela extends BaseEntity {
    private static final long serialVersionUID = -6749372761238974844L;
    private Long virtualNetId;
    private Long virtualNetEntityId;
    private Long virtualNetEntityType;
    private Long x;
    private Long y;
    private String virtualNetEntityName;
    private String virtualNetEntityIcon;
    private int fixed;
    private int visible;

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
     * @return the virtualNetEntityId
     */
    public Long getVirtualNetEntityId() {
        return virtualNetEntityId;
    }

    /**
     * @param virtualNetEntityId
     *            the virtualNetEntityId to set
     */
    public void setVirtualNetEntityId(Long virtualNetEntityId) {
        this.virtualNetEntityId = virtualNetEntityId;
    }

    /**
     * @return the virtualNetEntityType
     */
    public Long getVirtualNetEntityType() {
        return virtualNetEntityType;
    }

    /**
     * @param virtualNetEntityType
     *            the virtualNetEntityType to set
     */
    public void setVirtualNetEntityType(Long virtualNetEntityType) {
        this.virtualNetEntityType = virtualNetEntityType;
    }

    /**
     * @return the x
     */
    public Long getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(Long x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public Long getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(Long y) {
        this.y = y;
    }

    /**
     * @return the virtualNetEntityName
     */
    public String getVirtualNetEntityName() {
        return virtualNetEntityName;
    }

    /**
     * @param virtualNetEntityName
     *            the virtualNetEntityName to set
     */
    public void setVirtualNetEntityName(String virtualNetEntityName) {
        this.virtualNetEntityName = virtualNetEntityName;
    }

    /**
     * @return the virtualNetEntityIcon
     */
    public String getVirtualNetEntityIcon() {
        return virtualNetEntityIcon;
    }

    /**
     * @param virtualNetEntityIcon
     *            the virtualNetEntityIcon to set
     */
    public void setVirtualNetEntityIcon(String virtualNetEntityIcon) {
        this.virtualNetEntityIcon = virtualNetEntityIcon;
    }

    /**
     * @return the fixed
     */
    public int getFixed() {
        return fixed;
    }

    /**
     * @param fixed
     *            the fixed to set
     */
    public void setFixed(int fixed) {
        this.fixed = fixed;
    }

    /**
     * @return the visible
     */
    public int getVisible() {
        return visible;
    }

    /**
     * @param visible
     *            the visible to set
     */
    public void setVisible(int visible) {
        this.visible = visible;
    }

}
