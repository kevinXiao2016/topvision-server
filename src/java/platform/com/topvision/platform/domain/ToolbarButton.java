/***********************************************************************
 * $Id: ToolbarButton.java,v 1.1 Oct 24, 2009 12:00:06 PM kelers Exp $
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
 * @Create Date Oct 24, 2009 12:00:06 PM
 *         <p/>
 *         界面工具栏按钮.
 */
@Alias("toolbarButton")
public class ToolbarButton extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 1548193300723482115L;

    public static int TYPE_SEPARATOR = 0;
    public static int TYPE_BUTTON = 1;
    private Integer buttonId;
    private String text;
    private String tooltip;
    private String icon;
    private String action = "defaultHandler";
    private int type = TYPE_BUTTON;

    public Integer getButtonId() {
        return buttonId;
    }

    public void setButtonId(Integer buttonId) {
        this.buttonId = buttonId;
    }

    public String getAction() {
        return action;
    }

    public String getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public String getTooltip() {
        return tooltip;
    }

    public int getType() {
        return type;
    }

    public void setAction(String action) {
        if (action != null && !"".equals(action.trim())) {
            this.action = action;
        }
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ToolbarButton{" + "action='" + action + '\'' + ", buttonId=" + buttonId + ", text='" + text + '\''
                + ", tooltip='" + tooltip + '\'' + ", icon='" + icon + '\'' + ", type=" + type + '}';
    }
}
