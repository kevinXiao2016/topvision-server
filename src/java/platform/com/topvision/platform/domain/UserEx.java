package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

@Alias("userEx")
public class UserEx extends User {
    private static final long serialVersionUID = 4491287528937273726L;

    private String extend = null;
    private String placeName;
    private String roleNames;
    private String roleIds;
    private String departmentName;
    private String userGroupName; 
    private String choose;

    public String getDepartmentName() {
        return departmentName;
    }

    public String getExtend() {
        return extend;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    @Override
    public String toString() {
        return "UserEx{" + "departmentName='" + departmentName + '\'' + ", extend='" + extend + '\'' + ", placeName="
                + placeName + ", roleNames='" + roleNames + '\'' + ", roleIds='" + roleIds + '\'' + ", userGroupName='"
                + userGroupName + '\'' + '}';
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }
}
