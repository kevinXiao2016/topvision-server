/***********************************************************************
 * $Id: NavigationButton.java,v 1.1 Oct 25, 2009 4:22:14 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 * @Create Date Oct 25, 2009 4:22:14 PM
 */
@Alias("navigationButton")
public class NavigationButton extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -389680858247428645L;

    public static final Integer PLUGIN_ID = 5000000;
    private Integer naviId;
    private String name = null;
    private String displayName = null;
    private int seq = 0;
    private String icon16;
    private String icon24;
    private String action;
    private Boolean checked = Boolean.FALSE;

    public String getAction() {
        return action;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon16() {
        return icon16;
    }

    public String getIcon24() {
        return icon24;
    }

    public String getName() {
        return name;
    }

    public int getSeq() {
        return seq;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIcon16(String icon16) {
        this.icon16 = icon16;
    }

    public void setIcon24(String icon24) {
        this.icon24 = icon24;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getNaviId() {
        return naviId;
    }

    public void setNaviId(Integer naviId) {
        this.naviId = naviId;
    }

    @Override
    public String toString() {
        return "NavigationButton{" + "action='" + action + '\'' + ", naviId=" + naviId + ", name='" + name + '\''
                + ", displayName='" + displayName + '\'' + ", seq=" + seq + ", icon16='" + icon16 + '\'' + ", icon24='"
                + icon24 + '\'' + ", checked=" + checked + '}';
    }
}
