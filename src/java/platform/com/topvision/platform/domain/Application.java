package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

public class Application extends BaseEntity {
    private static final long serialVersionUID = -2176943030568181756L;

    private int appId;
    private String name;
    private String description;

    public int getAppId() {
        return appId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Application{" + "appId=" + appId + ", name='" + name + '\'' + ", description='" + description + '\''
                + '}';
    }
}
