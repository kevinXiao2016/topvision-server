package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

@Alias("portletCategory")
public class PortletCategory extends BaseEntity implements TreeEntity,
        com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 7749041955603487577L;

    private long categoryId;
    private String name;

    public long getCategoryId() {
        return categoryId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getId()
     */
    @Override
    public String getId() {
        return String.valueOf(categoryId);
    }

    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getParentId()
     */
    @Override
    public String getParentId() {
        return "0";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getText()
     */
    @Override
    public String getText() {
        return name;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PortletCategory{" + "categoryId=" + categoryId + ", name='" + name + '\'' + '}';
    }
}
