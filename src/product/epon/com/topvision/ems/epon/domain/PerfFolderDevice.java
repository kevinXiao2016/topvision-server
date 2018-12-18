/***********************************************************************
 * $ PerfFolderDevice.java,v1.0 2012-05-21 8:22:12 $
 *
 * @author: yq
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-21-8:22:12
 */
public class PerfFolderDevice implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7164504619518775546L;
    private Long entityId;
    private Long folderId;
    private String name;
    private String sysName;
    private String ip;
    private String mac;
    private String icon;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    @Override
    public String toString() {
        return "PerfFolderDevice [entityId=" + entityId + ", folderId=" + folderId + ", name=" + name + ", ip=" + ip
                + ", mac=" + mac + "]";
    }

}
