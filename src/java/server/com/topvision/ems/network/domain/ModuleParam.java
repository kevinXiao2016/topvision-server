/***********************************************************************
 * $ ModuleParam.java,v1.0 2012-3-27 11:47:03 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-3-27-11:47:03
 */
public class ModuleParam implements Serializable, Comparable<ModuleParam>, Cloneable {
    private static final long serialVersionUID = 7342180206202447869L;
    private Integer cos;
    private String moduleName;
    private String beanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getCos() {
        return cos;
    }

    public void setCos(Integer cos) {
        this.cos = cos;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ModuleParam");
        sb.append("{beanName='").append(beanName).append('\'');
        sb.append(", cos=").append(cos);
        sb.append(", moduleName='").append(moduleName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public int compareTo(ModuleParam o) {
        if (cos > o.getCos()) {
            return 1;
        } else if (cos.equals(o.getCos())) {
            return 0;
        } else {
            return -1;
        }
    }

}
