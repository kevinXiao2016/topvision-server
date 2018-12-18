/***********************************************************************
 * $Id: RoleFunctionRela.java,v1.0 2015-6-30 上午11:17:42 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2015-6-30-上午11:17:42 角色与function的关联信息
 * 
 */
@Alias("roleFunctionRela")
public class RoleFunctionRela implements AliasesSuperType{
    private static final long serialVersionUID = -7630338943985550737L;
    
    private Long roleId;
    private String roleName;
    private Long functionId;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
