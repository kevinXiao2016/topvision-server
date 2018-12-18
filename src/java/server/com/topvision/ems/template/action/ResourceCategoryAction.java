/**
 * 
 */
package com.topvision.ems.template.action;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.template.domain.ResourceCategory;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author kelers
 * 
 */
@Controller("resourceCategoryAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResourceCategoryAction extends BaseAction {
    private static final long serialVersionUID = 3321111802890372496L;
    private ResourceCategory resourceCategory;


    public void setResourceCategory(ResourceCategory resourceCategory) {
        this.resourceCategory = resourceCategory;
    }

    public ResourceCategory getResourceCategory() {
        return resourceCategory;
    }
}
