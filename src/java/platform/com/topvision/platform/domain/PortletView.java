package com.topvision.platform.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class PortletView extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -1194732090943344926L;

    private long viewId;
    private long userId;
    private String name;
    private String note;
    private int type;

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public int getType() {
        return type;
    }

    public long getUserId() {
        return userId;
    }

    public long getViewId() {
        return viewId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setViewId(long viewId) {
        this.viewId = viewId;
    }

    @Override
    public String toString() {
        return "PortletView{" + "name='" + name + '\'' + ", viewId=" + viewId + ", userId=" + userId + ", note='"
                + note + '\'' + ", type=" + type + '}';
    }
}
