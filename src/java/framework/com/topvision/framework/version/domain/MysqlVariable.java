/***********************************************************************
 * $Id: MysqlVariable.java,v 1.1 2009-10-9 下午01:02:43 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @Create Date 2009-10-9 下午01:02:43
 * 
 * @author kelers
 * 
 */
@Alias("mysqlVariable")
public class MysqlVariable implements AliasesSuperType {
    private static final long serialVersionUID = 9031469220087998274L;
    private String variable_name;
    private String value;

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the variable_name
     */
    public String getVariable_name() {
        return variable_name;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @param variableName
     *            the variable_name to set
     */
    public void setVariable_name(String variableName) {
        variable_name = variableName;
    }
}
