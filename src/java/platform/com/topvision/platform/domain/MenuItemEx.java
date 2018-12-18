/***********************************************************************
 * $Id: MenuItemEx.java,v 1.1 Oct 24, 2009 3:38:34 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

/**
 * @author kelers
 * @Create Date Oct 24, 2009 3:38:34 PM
 */
@Alias("menuItemEx")
public class MenuItemEx extends MenuItem {
    private static final long serialVersionUID = 3242703512522287871L;

    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "MenuItemEx{" + "roleId=" + roleId + '}';
    }
}
