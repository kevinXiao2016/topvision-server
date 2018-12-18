/***********************************************************************
 * $Id: NavigationButtonEx.java,v 1.1 Oct 25, 2009 4:22:38 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

/**
 * @author kelers
 * @Create Date Oct 25, 2009 4:22:38 PM
 */
@Alias("navigationButtonEx")
public class NavigationButtonEx extends NavigationButton {
    private static final long serialVersionUID = -2084088341512877080L;

    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "NavigationButtonEx{" + "roleId=" + roleId + '}';
    }
}
