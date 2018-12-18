/***********************************************************************
 * $ ApWorkParam.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
public class ApWorkParam implements Serializable {
    private static final long serialVersionUID = -45721096224992501L;
    private String paramKey;
    private String paramName;
    private String paramType;
    private List<String> checkRules;
    private List<String> defaultValueList;
    public static final String TEXT = "text";
    public static final String LIST = "list";
    public static final String CHECK = "check";
    public static final String DATE = "date";
    public static final String IP = "ip";

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public List<String> getDefaultValueList() {
        return defaultValueList;
    }

    public void setDefaultValueList(List<String> defaultValueList) {
        this.defaultValueList = defaultValueList;
    }

    public List<String> getCheckRules() {
        return checkRules;
    }

    public void setCheckRules(List<String> checkRules) {
        this.checkRules = checkRules;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ApWorkParam");
        sb.append("{checkRules=").append(checkRules);
        sb.append(", paramKey='").append(paramKey).append('\'');
        sb.append(", paramName='").append(paramName).append('\'');
        sb.append(", paramType='").append(paramType).append('\'');
        sb.append(", defaultValueList=").append(defaultValueList);
        sb.append('}');
        return sb.toString();
    }
}