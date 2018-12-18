/***********************************************************************
 * $Id: AlertSound.java,v1.0 2015-1-21 上午10:16:39 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.ResourceManager;

/**
 * @author vanzand
 * @created @2015-1-21-上午10:16:39
 * 
 */
public class AlertSound implements AliasesSuperType {
    private static final long serialVersionUID = 5092297961765895172L;

    private Integer id;
    private String name;
    private String description;
    private Integer deletable;
    private Boolean selected;

    public AlertSound() {
        super();
    }

    public AlertSound(String name, String description, Integer deletable) {
        super();
        this.name = name;
        this.description = description;
        this.deletable = deletable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return getResourceManager().getNotNullString(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeletable() {
        return deletable;
    }

    public void setDeletable(Integer deletable) {
        this.deletable = deletable;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
    }

}
