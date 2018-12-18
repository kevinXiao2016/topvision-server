/***********************************************************************
 * $Id: AbstractEponAction.java,v1.0 2013年10月26日 上午11:34:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.platform.ResourceManager;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author Bravin
 * @created @2013年10月26日-上午11:34:58
 *
 */
public abstract class AbstractEponAction extends BaseAction {
    private static final long serialVersionUID = 2760980656263399347L;
    @Autowired
    protected OltService oltService;
    protected Long entityId;
    protected Integer operationResult;
    protected String source;
    protected String currentId;

    /**
     * 国际化
     * 
     * @param key
     *            key
     * @return String
     */
    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

}
