/**
 * 
 */
package com.topvision.ems.template.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author kelers
 * 
 */
@Alias("resourceCategory")
public class ResourceCategory implements AliasesSuperType {
    private static final long serialVersionUID = -7707370490569449150L;

    private Long categoryId;
    private Long parentId;
    private String path;

    /**
     * 分类编码.
     */
    private String categoryCode;
    private String name;
    private String displayName;
    private String note;

    /**
     * 是否可监控的.
     */
    private Boolean controllable;

    /**
     * 是否用户自定义的.
     */
    private Boolean custom = Boolean.FALSE;

    /**
     * 树中的显示顺序.
     */
    private Integer seq;

    public String getCategoryCode() {
        return categoryCode;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Boolean getControllable() {
        return controllable;
    }

    public Boolean getCustom() {
        return custom;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getPath() {
        return path;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setControllable(Boolean controllable) {
        this.controllable = controllable;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

}
