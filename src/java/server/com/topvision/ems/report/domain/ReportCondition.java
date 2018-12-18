/***********************************************************************
 * $Id: ReportCondition.java,v1.0 2014-6-18 下午2:48:27 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

/**
 * @author Rod John
 * @created @2014-6-18-下午2:48:27
 * 
 */
public class ReportCondition {
    private String type;
    private String id;
    private String labelName;
    private String value;
    private String placeHolder;

    public ReportCondition() {
        super();
    }

    public ReportCondition(String type, String id, String labelName, String value) {
        super();
        this.type = type;
        this.id = id;
        this.labelName = labelName;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

}
