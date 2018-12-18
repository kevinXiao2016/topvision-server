/***********************************************************************
 * $Id: MenuControl.java,v1.0 2013-4-12 上午11:23:58 $
 *
 * @author: lzt
 *
 * (c)Copyright 2013 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.util.List;

/**
 * @author lzt
 * @created @2013-4-12 上午11:23:58
 */
public class MenuControl {

    private String deviceVersion;
    private List<MenuItem> menuItems;

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MenuControl [deviceVersion=");
        builder.append(deviceVersion);
        builder.append(", menuItems=");
        builder.append(menuItems);
        builder.append("]");
        return builder.toString();
    }

}
