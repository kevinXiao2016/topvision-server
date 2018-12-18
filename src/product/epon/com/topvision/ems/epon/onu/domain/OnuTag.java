package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 标签
 * 
 * @author w1992wishes
 * @created @2017年12月21日-下午1:44:50
 *
 */
public class OnuTag implements AliasesSuperType {

    private static final long serialVersionUID = 8302510249473527156L;

    private Integer id;
    private String tagName;
    private Short tagLevel;

    public Integer getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Short getTagLevel() {
        return tagLevel;
    }

    public void setTagLevel(Short tagLevel) {
        this.tagLevel = tagLevel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("tag [id=]").append(id).append(", tagName=").append(tagName).append(", tagLevel=").append(tagLevel)
                .append("]");
        return sb.toString();
    }

}
