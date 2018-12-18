/***********************************************************************
 * $Id: Area.java,v1.0 2013-6-21 下午3:42:26 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 片区
 * @author loyal
 * @created @2013-6-21-下午3:42:26
 *
 */
@Alias("area")
public class Area implements AliasesSuperType{
    private static final long serialVersionUID = -1783396535118165945L;
    public Long folderId;
    public Long superiorId;
    public Long categoryId;
    public String name;
    public Integer type;
    public Long getFolderId() {
        return folderId;
    }
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
    public Long getSuperiorId() {
        return superiorId;
    }
    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
}
