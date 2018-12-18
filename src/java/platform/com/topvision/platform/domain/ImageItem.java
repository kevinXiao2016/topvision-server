package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

public class ImageItem extends BaseEntity {
    private static final long serialVersionUID = 1230991192031388528L;

    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ImageItem{" + "name='" + name + '\'' + ", path='" + path + '\'' + '}';
    }
}
