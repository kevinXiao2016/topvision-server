/***********************************************************************
 * $ ApWorkTemplate.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
public class ApWorkTemplate implements Serializable {
    private static final long serialVersionUID = -4960602304646437943L;
    private String templateName;
    private String workClass;
    private List<ApWorkParam> workParamList = new ArrayList<ApWorkParam>();

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getWorkClass() {
        return workClass;
    }

    public void setWorkClass(String workClass) {
        this.workClass = workClass;
    }

    public List<ApWorkParam> getWorkParamList() {
        return workParamList;
    }

    public void setWorkParamList(List<ApWorkParam> workParamList) {
        this.workParamList = workParamList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ApWorkTemplate");
        sb.append("{templateName='").append(templateName).append('\'');
        sb.append(", workClass='").append(workClass).append('\'');
        sb.append(", workParamList=").append(workParamList);
        sb.append('}');
        return sb.toString();
    }
}
