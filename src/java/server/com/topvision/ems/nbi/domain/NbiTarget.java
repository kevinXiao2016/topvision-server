/***********************************************************************
 * $Id: NbiTarget.java,v1.0 2016-3-20 上午10:12:55 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2016-3-20-上午10:12:55
 *
 */
public class NbiTarget implements AliasesSuperType {

    private static final long serialVersionUID = 9139460370188168755L;
    private Integer groupId;
    private String groupName;
    private Integer perfIndex;
    private String oid;
    private String perfIndexName;
    private String displayName;
    private Integer period;
    private Boolean selected;

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

    public Integer getPerfIndex() {
        return perfIndex;
    }

    public void setPerfIndex(Integer perfIndex) {
        this.perfIndex = perfIndex;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPerfIndexName() {
        return perfIndexName;
    }

    public void setPerfIndexName(String perfIndexName) {
        this.perfIndexName = perfIndexName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

}
