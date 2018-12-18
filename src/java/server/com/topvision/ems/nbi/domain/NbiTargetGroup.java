/***********************************************************************
 * $Id: NbiTargetGroup.java,v1.0 2016年3月28日 上午9:55:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.domain;

import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年3月28日-上午9:55:51
 *
 */
public class NbiTargetGroup implements AliasesSuperType {

    private static final long serialVersionUID = -7688565690046782221L;
    private Integer groupId;
    private String groupName;
    private String displayName;
    private Integer period;
    private Boolean selected = true;
    private List<NbiTarget> nbiTargetList;
    private String groupModule;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<NbiTarget> getNbiTargetList() {
        return nbiTargetList;
    }

    public void setNbiTargetList(List<NbiTarget> nbiTargetList) {
        this.nbiTargetList = nbiTargetList;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getGroupModule() {
        return groupModule;
    }

    public void setGroupModule(String groupModule) {
        this.groupModule = groupModule;
    }

}
