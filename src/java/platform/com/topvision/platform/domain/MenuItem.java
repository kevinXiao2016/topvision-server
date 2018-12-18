/***********************************************************************
 * $Id: MenuItem.java,v 1.1 Oct 24, 2009 11:59:33 AM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 * @Create Date Oct 24, 2009 11:59:33 AM
 *         <p/>
 *         界面菜单按钮.
 */
@Alias("menuItem")
public class MenuItem extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -8037396046880436917L;

    public static final Integer TYPE_SEPARATOR = 0;
    public static final Integer TYPE_MENUITEM = 1;
    private Integer itemId;
    private Integer parentId;
    private String name;
    private String icon;
    private String action;
    private String target;
    private String mnemonic;
    private Integer type = TYPE_MENUITEM;
    private Long functionId;
    private Long pluginId;
    private List<MenuItem> children;
    /**
     * true表示启用， false表示停用.
     */
    private Boolean status = Boolean.TRUE;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItem> children) {
        this.children = children;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Long getPluginId() {
        return pluginId;
    }

    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String toString() {
        return "MenuItem{" + "action='" + action + '\'' + ", itemId=" + itemId + ", parentId=" + parentId + ", name='"
                + name + '\'' + ", icon='" + icon + '\'' + ", target='" + target + '\'' + ", mnemonic='" + mnemonic
                + '\'' + ", type=" + type + ", functionId=" + functionId + ", pluginId=" + pluginId + ", children="
                + children + ", status=" + status + '}';
    }
}
