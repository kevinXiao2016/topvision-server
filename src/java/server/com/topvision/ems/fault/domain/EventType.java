package com.topvision.ems.fault.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

public class EventType extends BaseEntity implements TreeEntity, AliasesSuperType {
    private static final long serialVersionUID = 6004352679836601646L;
    private Integer typeId;
    private Integer parentId;
    private String name;
    private String displayName;
    private String note;
    private Integer alertTypeId;

    /**
     * 
     * @return alertTypeId
     */
    public Integer getAlertTypeId() {
        return alertTypeId;
    }

    /**
     * 
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.zetaframework.domain.TreeEntity#getId()
     */
    @Override
    public String getId() {
        return String.valueOf(typeId);
    }

    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.zetaframework.domain.TreeEntity#getParentId()
     */
    @Override
    public String getParentId() {
        return String.valueOf(parentId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.zetaframework.domain.TreeEntity#getText()
     */
    @Override
    public String getText() {
        return displayName;
    }

    /**
     * 
     * @return typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * 
     * @param alertTypeId
     */
    public void setAlertTypeId(Integer alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

    /**
     * 
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 
     * @param parentId
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 
     * @param typeId
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventType [typeId=");
        builder.append(typeId);
        builder.append(", parentId=");
        builder.append(parentId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", note=");
        builder.append(note);
        builder.append(", alertTypeId=");
        builder.append(alertTypeId);
        builder.append("]");
        return builder.toString();
    }
}
