package com.topvision.ems.facade.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author niejun
 * 
 */
public class EntityAttribute implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -7438749317036736297L;

    private Long entityId = null;
    /**
     * 属性分组
     */
    private String attributeGroup = null;
    /**
     * 属性名
     */
    private String attributeName = null;
    /**
     * 属性值
     */
    private String attributeValue = null;
    /**
     * 属性解释
     */
    private String attributeNote = null;
    /**
     * 属性扩展值
     */
    private String extValue = null;

    public String getAttributeGroup() {
        return attributeGroup;
    }

    public void setAttributeGroup(String attributeGroup) {
        this.attributeGroup = attributeGroup;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeNote() {
        return attributeNote;
    }

    public void setAttributeNote(String attributeNote) {
        this.attributeNote = attributeNote;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getExtValue() {
        return extValue;
    }

    public void setExtValue(String extValue) {
        this.extValue = extValue;
    }

    public String toString() {
        return "EntityAttribute{" + "attributeGroup='" + attributeGroup + '\'' + ", entityId=" + entityId
                + ", attributeName='" + attributeName + '\'' + ", attributeValue='" + attributeValue + '\''
                + ", attributeNote='" + attributeNote + '\'' + ", extValue='" + extValue + '\'' + '}';
    }
}
