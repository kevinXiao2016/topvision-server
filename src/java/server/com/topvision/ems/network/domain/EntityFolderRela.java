/***********************************************************************
 * $Id: EntityFolders.java,v1.0 2015-7-22 下午4:53:38 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2015-7-22-下午4:53:38
 * 
 */
public class EntityFolderRela implements AliasesSuperType {
    private static final long serialVersionUID = -3914879128401506979L;
    private Long entityId;
    private Long folderId;
    private String ip;
    private String mac;
    private Double x;
    private Double y;
    private String nameInFolder;
    private String iconInFolder;
    private Boolean fixed;
    private Boolean visible;
    private Long groupId;

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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getNameInFolder() {
        return nameInFolder;
    }

    public void setNameInFolder(String nameInFolder) {
        this.nameInFolder = nameInFolder;
    }

    public String getIconInFolder() {
        return iconInFolder;
    }

    public void setIconInFolder(String iconInFolder) {
        this.iconInFolder = iconInFolder;
    }

    public Boolean getFixed() {
        return fixed;
    }

    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
