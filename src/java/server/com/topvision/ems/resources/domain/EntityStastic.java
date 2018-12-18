/***********************************************************************
 * $Id: EntityStastic.java,v1.0 2013-3-19 下午6:02:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.resources.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-3-19-下午6:02:18
 *
 */
@Alias("entityStastic")
public class EntityStastic implements AliasesSuperType {
    private static final long serialVersionUID = -8152765953573319682L;
    private String displayName;
    private Integer online;
    private Integer offline;

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the online
     */
    public Integer getOnline() {
        return online == null ? 0 : online;
    }

    /**
     * @param online the online to set
     */
    public void setOnline(Integer online) {
        this.online = online;
    }

    /**
     * @return the offline
     */
    public Integer getOffline() {
        return offline == null ? 0 : offline;
    }

    /**
     * @param offline the offline to set
     */
    public void setOffline(Integer offline) {
        this.offline = offline;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EntityStastic");
        sb.append("{displayName=").append(displayName);
        sb.append(", onlineCount=").append(online);
        sb.append(", offlineCount='").append(offline);
        sb.append('}');
        return sb.toString();
    }

}
