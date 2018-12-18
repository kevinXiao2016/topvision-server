package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class TopoLabel extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -7520755379543990803L;
    public static final String TYPE_CPU = "cpu";
    public static final String TYPE_MEM = "mem";
    public static final String TYPE_LINKFLOW = "linkflow";
    public static final String TYPE_LINKRATE = "linkrate";

    private Long value;
    private Long folderId;
    private String labelId;
    private String color;
    private String module;
    private String displayName;

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the folderId
     */
    public Long getFolderId() {
        return folderId;
    }

    /**
     * @return the labelId
     */
    public String getLabelId() {
        return labelId;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @return the value
     */
    public Long getValue() {
        return value;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @param folderId
     *            the folderId to set
     */
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    /**
     * @param labelId
     *            the labelId to set
     */
    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    /**
     * @param module
     *            the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Long value) {
        this.value = value;
    }
}
