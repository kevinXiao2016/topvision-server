package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

@Alias("post")
public class Post extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 6965997835178729619L;

    private long postId;
    private long superiorId;
    private String name;
    private String note;

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public long getPostId() {
        return postId;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    @Override
    public String toString() {
        return "Post{" + "name='" + name + '\'' + ", postId=" + postId + ", superiorId=" + superiorId + ", note='"
                + note + '\'' + '}';
    }
}
