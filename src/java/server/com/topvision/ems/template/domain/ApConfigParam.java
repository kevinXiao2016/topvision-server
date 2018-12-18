/***********************************************************************
 * $ ApConfigParam.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
public class ApConfigParam implements Serializable {
    private static final long serialVersionUID = 5938080575415591182L;
    private String modeName;
    private String templateName;
    private Map<String, String> values = new HashMap<String, String>();

    public Boolean contains(String paramKey) {
        return values.containsKey(paramKey);
    }

    public String getValue(String paramKey) {
        return values.get(paramKey);
    }

    public void setValue(String paramKey, String value) {
        values.put(paramKey, value);
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ApConfigParam");
        sb.append("{modeName='").append(modeName).append('\'');
        sb.append(", templateName='").append(templateName).append('\'');
        sb.append(", values=").append(values);
        sb.append('}');
        return sb.toString();
    }
}