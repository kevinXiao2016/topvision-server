/***********************************************************************
 * $Id: Repository.java,v 1.1 Jun 28, 2008 11:45:09 AM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 * @Create Date Jun 28, 2008 11:45:09 AM
 */
@Alias("repository")
public class Repository extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -4425615698645754879L;

    private long id;
    private String owner;
    private Timestamp createTime;
    private String key1;
    private String key2;
    private String key3;
    private String key4;
    private String key5;
    private String category;
    private String content;

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the key1
     */
    public String getKey1() {
        return key1;
    }

    /**
     * @return the key2
     */
    public String getKey2() {
        return key2;
    }

    /**
     * @return the key3
     */
    public String getKey3() {
        return key3;
    }

    /**
     * @return the key4
     */
    public String getKey4() {
        return key4;
    }

    /**
     * @return the key5
     */
    public String getKey5() {
        return key5;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @param key1
     *            the key1 to set
     */
    public void setKey1(String key1) {
        this.key1 = key1;
    }

    /**
     * @param key2
     *            the key2 to set
     */
    public void setKey2(String key2) {
        this.key2 = key2;
    }

    /**
     * @param key3
     *            the key3 to set
     */
    public void setKey3(String key3) {
        this.key3 = key3;
    }

    /**
     * @param key4
     *            the key4 to set
     */
    public void setKey4(String key4) {
        this.key4 = key4;
    }

    /**
     * @param key5
     *            the key5 to set
     */
    public void setKey5(String key5) {
        this.key5 = key5;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Repository{" + "category='" + category + '\'' + ", id=" + id + ", owner='" + owner + '\''
                + ", createTime=" + createTime + ", key1='" + key1 + '\'' + ", key2='" + key2 + '\'' + ", key3='"
                + key3 + '\'' + ", key4='" + key4 + '\'' + ", key5='" + key5 + '\'' + ", content='" + content + '\''
                + '}';
    }
}
