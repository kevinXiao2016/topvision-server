/***********************************************************************
 * $Id: EntityTypeRelation.java,v1.0 2014-1-23 下午4:13:21 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

/**
 * @author loyal
 * @created @2014-1-23-下午4:13:21
 *
 */
public class EntityTypeRelation {
    private Long type;
    private Long typeId;
    private String name;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
