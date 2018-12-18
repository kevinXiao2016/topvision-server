/***********************************************************************
 * $Id: MenuItem.java,v1.0 2013-4-12 上午11:43:36 $
 *
 * @author: lzt
 *
 * (c)Copyright 2013 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

/**
 * @author lzt
 * @created @2013-4-12 上午11:43:36
 */
public class MenuItem {

    private String menuId;
    private Boolean disabled;
    private Boolean display;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

}
