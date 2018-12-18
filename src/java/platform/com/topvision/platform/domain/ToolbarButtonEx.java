/***********************************************************************
 * $Id: ToolbarButtonEx.java,v 1.1 Oct 25, 2009 4:09:37 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

/**
 * @author kelers
 * @Create Date Oct 25, 2009 4:09:37 PM
 */
@Alias("toolbarButtonEx")
public class ToolbarButtonEx extends ToolbarButton {
    private static final long serialVersionUID = -7335485439452380046L;

    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "ToolbarButtonEx{" + "roleId=" + roleId + '}';
    }
}
