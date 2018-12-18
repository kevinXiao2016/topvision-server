/**
 * 
 */
package com.topvision.ems.resources.domain;

import java.util.List;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.domain.BaseEntity;

/**
 * 按照设备分类进行统计显示的实体.
 * 
 * @author niejun
 */
public class EntityCategoryStat extends BaseEntity {

    private static final long serialVersionUID = 6580374167159293066L;
    private List<EntityType> parentCategory = null;
    private List<EntityType> leafCategory = null;

    public List<EntityType> getLeafCategory() {
        return leafCategory;
    }

    public List<EntityType> getParentCategory() {
        return parentCategory;
    }

    public void setLeafCategory(List<EntityType> leafCategory) {
        this.leafCategory = leafCategory;
    }

    public void setParentCategory(List<EntityType> parentCategory) {
        this.parentCategory = parentCategory;
    }
}
