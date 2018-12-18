package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

public class Shortcut extends BaseEntity {
    private static final long serialVersionUID = -3692010039096455907L;

    private String key = null;
    private String name = null;
    private String url = null;
    private int x = 0;
    private int y = 0;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Shortcut{" + "key='" + key + '\'' + ", name='" + name + '\'' + ", url='" + url + '\'' + ", x=" + x
                + ", y=" + y + '}';
    }
}
