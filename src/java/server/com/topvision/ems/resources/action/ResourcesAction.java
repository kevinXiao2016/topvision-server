/**
 * 
 */
package com.topvision.ems.resources.action;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

/**
 * @author kelers
 * 
 */
@Controller("resourcesAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResourcesAction extends BaseAction {
    private static final long serialVersionUID = -3281747938957216435L;
    private String node;

    public String getNode() {
        return node;
    }

    /**
     * key：properties文件的keymodule：资源文件
     */
    protected String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }


    public void setNode(String node) {
        this.node = node;
    }

}
