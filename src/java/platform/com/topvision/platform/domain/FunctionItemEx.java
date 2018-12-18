package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

@Alias("functionItemEx")
public class FunctionItemEx extends FunctionItem implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -6629631904324930324L;
    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "FunctionItemEx{" + "roleId=" + roleId + '}';
    }
}
