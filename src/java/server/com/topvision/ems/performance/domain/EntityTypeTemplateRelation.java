/***********************************************************************
 * $Id: EntityTypeTemplateRelation.java,v1.0 2013-9-23 下午4:44:10 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2013-9-23-下午4:44:10
 * 
 */
public class EntityTypeTemplateRelation implements AliasesSuperType {
    private static final long serialVersionUID = -404363435634461688L;
    private Integer typeId;
    private String displayName;
    private Long parentTypeId;
    private Integer defalutTemplateId;
    private Boolean hasDefaultTemplate;
    private Boolean isSubType;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getParentTypeId() {
        return parentTypeId;
    }

    public void setParentTypeId(Long parentTypeId) {
        this.parentTypeId = parentTypeId;
    }

    public Integer getDefalutTemplateId() {
        return defalutTemplateId;
    }

    public void setDefalutTemplateId(Integer defalutTemplateId) {
        this.defalutTemplateId = defalutTemplateId;
        if (defalutTemplateId == null) {
            this.hasDefaultTemplate = false;
        } else {
            this.hasDefaultTemplate = true;
        }
    }

    public Boolean getHasDefaultTemplate() {
        return hasDefaultTemplate;
    }

    public void setHasDefaultTemplate(Boolean hasDefaultTemplate) {
        this.hasDefaultTemplate = hasDefaultTemplate;
    }

    public Boolean getIsSubType() {
        return isSubType;
    }

    public void setIsSubType(Boolean isSubType) {
        this.isSubType = isSubType;
    }

    @Override
    public String toString() {
        return "EntityTypeTemplateRelation [typeId=" + typeId + ", displayName=" + displayName + ", parentTypeId="
                + parentTypeId + ", defalutTemplateId=" + defalutTemplateId + ", hasDefaultTemplate="
                + hasDefaultTemplate + ", isSubType=" + isSubType + "]";
    }

}
