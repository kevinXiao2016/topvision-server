package com.topvision.ems.fault.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

public class ActionType extends BaseEntity implements TreeEntity, AliasesSuperType {

    private static final long serialVersionUID = -5488434327695770783L;
    private Integer actionTypeId;
    private String name;
    private String displayName;
    private Boolean enabled;
    private String actionClass;
    private byte[] params;

    /**
     * 
     * @return actionClass
     */
    public String getActionClass() {
        return actionClass;
    }

    /**
     * 
     * @return actionTypeId
     */
    public Integer getActionTypeId() {
        return actionTypeId;
    }

    /**
     * 
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getId() {
        return String.valueOf(actionTypeId);
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
     * @return params
     */
    public byte[] getParams() {
        return params;
    }

    @Override
    public String getParentId() {
        return "0";
    }

    @Override
    public String getText() {
        return name;
    }

    /**
     * 
     * @return enabled
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * 
     * @param actionClass
     */
    public void setActionClass(String actionClass) {
        this.actionClass = actionClass;
    }

    /**
     * 
     * @param actionTypeId
     */
    public void setActionTypeId(Integer actionTypeId) {
        this.actionTypeId = actionTypeId;
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
     * @param enabled
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
     * @param params
     */
    public void setParams(byte[] params) {
        this.params = params;
    }
}
