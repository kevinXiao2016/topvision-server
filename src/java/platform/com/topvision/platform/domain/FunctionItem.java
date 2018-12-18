package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

@Alias("functionItem")
public class FunctionItem extends BaseEntity implements TreeEntity,
        com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -5040982981340779110L;

    private long functionId;
    private long superiorId;
    private String name = null;
    private String displayName = null;
    private boolean checked = false;

    private String functionName;
    private String functionAction = null;// function将要执行的动作，用于个性化我的工作台 @author:bravin
    private String icon;// function对应的图标

    public String getDisplayName() {
        return displayName;
    }

    public long getFunctionId() {
        return functionId;
    }

    @Override
    public String getId() {
        return String.valueOf(functionId);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getParentId() {
        return String.valueOf(superiorId);
    }

    public long getSuperiorId() {
        return superiorId;
    }

    @Override
    public String getText() {
        return displayName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setFunctionId(long functionId) {
        this.functionId = functionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the functionAction
     */
    public String getFunctionAction() {
        return functionAction;
    }

    /**
     * @param functionAction
     *            the functionAction to set
     */
    public void setFunctionAction(String functionAction) {
        this.functionAction = functionAction;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the functionName
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * @param functionName
     *            the functionName to set
     */
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

}
