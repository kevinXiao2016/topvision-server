package com.topvision.ems.template.domain;

import java.io.Serializable;

public class ResourceBigCateogry implements Serializable {

    private static final long serialVersionUID = 1496832551402746371L;

    private Integer bigCateogryId;
    private Integer parentId;
    private String bigCateogryCode;
    private String name;
    private Integer type;
    private Boolean custom;
    // 显示顺序
    private Integer seq;

    public String getBigCateogryCode() {
        return bigCateogryCode;
    }

    public Integer getBigCateogryId() {
        return bigCateogryId;
    }

    public Boolean getCustom() {
        return custom;
    }

    public String getName() {
        return name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getSeq() {
        return seq;
    }

    public Integer getType() {
        return type;
    }

    public void setBigCateogryCode(String bigCateogryCode) {
        this.bigCateogryCode = bigCateogryCode;
    }

    public void setBigCateogryId(Integer bigCateogryId) {
        this.bigCateogryId = bigCateogryId;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
