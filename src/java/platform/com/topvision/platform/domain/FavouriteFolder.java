package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

@Alias("favouriteFolder")
public class FavouriteFolder extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 6353663086776913971L;

    public static final byte FOLDER = 0;
    public static final byte LINK = 1;
    private long folderId;
    private long superiorId;
    private String name;
    private long userId;
    private byte type = FOLDER;
    private String url;
    private Boolean shared = Boolean.FALSE;
    private String path;

    public long getFolderId() {
        return folderId;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public byte getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public long getUserId() {
        return userId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    @Override
    public String toString() {
        return "FavouriteFolder{" + "folderId=" + folderId + ", superiorId=" + superiorId + ", name='" + name + '\''
                + ", userId=" + userId + ", type=" + type + ", url='" + url + '\'' + ", shared=" + shared + ", path='"
                + path + '\'' + '}';
    }
}