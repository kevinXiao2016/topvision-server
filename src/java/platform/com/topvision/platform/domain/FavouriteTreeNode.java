/***********************************************************************
 * $Id: FavouriteTreeNode.java,v1.0 2017年1月6日 下午5:30:52 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import com.topvision.framework.domain.TreeNode;

/**
 * @author vanzand
 * @created @2017年1月6日-下午5:30:52
 *
 */
public class FavouriteTreeNode extends TreeNode {
    private Boolean expanded;
    private Integer type;
    private Boolean share;
    private Long userId;
    private String folderPath;
    private String url;
    private Boolean isTarget;

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsTarget() {
        return isTarget;
    }

    public void setIsTarget(Boolean isTarget) {
        this.isTarget = isTarget;
    }

}
