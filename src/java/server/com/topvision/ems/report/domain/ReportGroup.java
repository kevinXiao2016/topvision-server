/***********************************************************************
 * $Id: ReportGroup.java,v1.0 2014-6-17 下午5:24:08 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rod John
 * @created @2014-6-17-下午5:24:08
 * 
 */
public class ReportGroup {

    private String relative;
    private String displayName;
    private String groupKey;
    private Boolean link;
    private List<Group> groups;

    /**
     * @return the relative
     */
    public String getRelative() {
        return relative;
    }

    /**
     * @param relative
     *            the relative to set
     */
    public void setRelative(String relative) {
        this.relative = relative;
    }

    /**
     * @return the groups
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * @param groups
     *            the groups to set
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the groupKey
     */
    public String getGroupKey() {
        return groupKey;
    }

    /**
     * @param groupKey
     *            the groupKey to set
     */
    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public void insertGroup(Group group) {
        if (this.groups == null) {
            this.groups = new ArrayList<>();
        }
        this.groups.add(group);
    }

    public Boolean getLink() {
        return link;
    }

    public void setLink(Boolean link) {
        this.link = link;
    }

}
