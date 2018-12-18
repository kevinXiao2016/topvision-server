/***********************************************************************
 * $Id: PingParam.java,v1.0 2017年3月17日 下午4:20:03 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author ls
 * @created @2017年3月17日-下午4:20:03
 *
 */

public class PingParam implements AliasesSuperType {

    private static final long serialVersionUID = 2933036687647676326L;
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
