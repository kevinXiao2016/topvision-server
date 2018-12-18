/***********************************************************************
 * $Id: Batchautodiscoveryips.java,v1.0 2014-5-11 下午2:16:33 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2014-5-11-下午2:16:33
 * 
 */
public class BatchAutoDiscoveryIps implements AliasesSuperType {
    private static final long serialVersionUID = 8129444617025320425L;
    public static final Integer AUTO_DISCOVERY_STATUS_NO_START = 0;
    public static final Integer AUTO_DISCOVERY_STATUS_INPROCESS = 1;
    public static final Integer AUTO_DISCOVERY_STATUS_SUCCESS = 2;
    public static final Integer AUTO_DISCOVERY_STATUS_FAILURE = 3;
    private Long id;
    private String ipInfo;
    private String name;
    private Long folderId;
    private String folderName;
    private Integer autoDiscovery;
    private Timestamp createtime;
    private Timestamp lastDiscoveryTime;
    private Timestamp nextFireTime;
    private Integer autoDiscoveryStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(String ipInfo) {
        this.ipInfo = ipInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Integer getAutoDiscovery() {
        return autoDiscovery;
    }

    public void setAutoDiscovery(Integer autoDiscovery) {
        this.autoDiscovery = autoDiscovery;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Timestamp getLastDiscoveryTime() {
        return lastDiscoveryTime;
    }

    public void setLastDiscoveryTime(Timestamp lastDiscoveryTime) {
        this.lastDiscoveryTime = lastDiscoveryTime;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * @return the autoDiscoveryStatus
     */
    public Integer getAutoDiscoveryStatus() {
        return autoDiscoveryStatus;
    }

    /**
     * @param autoDiscoveryStatus
     *            the autoDiscoveryStatus to set
     */
    public void setAutoDiscoveryStatus(Integer autoDiscoveryStatus) {
        this.autoDiscoveryStatus = autoDiscoveryStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BatchAutoDiscoveryIps [id=");
        builder.append(id);
        builder.append(", ipInfo=");
        builder.append(ipInfo);
        builder.append(", name=");
        builder.append(name);
        builder.append(", folderId=");
        builder.append(folderId);
        builder.append(", folderName=");
        builder.append(folderName);
        builder.append(", autoDiscovery=");
        builder.append(autoDiscovery);
        builder.append(", createtime=");
        builder.append(createtime);
        builder.append(", lastDiscoveryTime=");
        builder.append(lastDiscoveryTime);
        builder.append("]");
        return builder.toString();
    }

    public Timestamp getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Timestamp nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

}
