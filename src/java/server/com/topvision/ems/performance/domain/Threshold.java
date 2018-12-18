/***********************************************************************
 * $Id: Threshold.java,v 1.1 Sep 4, 2008 3:37:42 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @Create Date Sep 4, 2008 3:37:42 PM
 * 
 * @author kelers
 * 
 */
public class Threshold extends BaseEntity {
    private static final long serialVersionUID = -7427493872491726395L;
    public static final String[] OPERATOR = { ">", ">=", "=", "!=", "<", "<=", "indexOf", "!indexOf", "equals",
            "!equals", "startsWith", "endsWith" };//
    private Long thresholdId;// 阀值ID
    private String name;//
    private Byte type;// 1--数值型，2--字符型
    private String description;// 描述
    private Byte operator1;// //
                           // 0:>,1:>=,2:=,3:!=,4:<,5:<=,6:包含,7:不包含,8:等于,9:不等于,10:起始于,11:终止于
    private String threshold1;//
    private Byte alertLevel1;// 参考Level的告警等级定义
    private Integer count1;//
    private String alertDesc1;//
    private Byte operator2;//
    private String threshold2;//
    private Byte alertLevel2;// 参考Level的告警等级定义
    private Integer count2;//
    private String alertDesc2;//
    private Boolean defaultThreshold;//

    /**
     * @return the thresholdId
     */
    public Long getThresholdId() {
        return thresholdId;
    }

    /**
     * @param thresholdId
     *            the thresholdId to set
     */
    public void setThresholdId(Long thresholdId) {
        this.thresholdId = thresholdId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public Byte getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the operator1
     */
    public Byte getOperator1() {
        return operator1;
    }

    /**
     * @param operator1
     *            the operator1 to set
     */
    public void setOperator1(Byte operator1) {
        this.operator1 = operator1;
    }

    /**
     * @return the threshold1
     */
    public String getThreshold1() {
        return threshold1;
    }

    /**
     * @param threshold1
     *            the threshold1 to set
     */
    public void setThreshold1(String threshold1) {
        this.threshold1 = threshold1;
    }

    /**
     * @return the alertLevel1
     */
    public Byte getAlertLevel1() {
        return alertLevel1;
    }

    /**
     * @param alertLevel1
     *            the alertLevel1 to set
     */
    public void setAlertLevel1(Byte alertLevel1) {
        this.alertLevel1 = alertLevel1;
    }

    /**
     * @return the count1
     */
    public Integer getCount1() {
        return count1;
    }

    /**
     * @param count1
     *            the count1 to set
     */
    public void setCount1(Integer count1) {
        this.count1 = count1;
    }

    /**
     * @return the alertDesc1
     */
    public String getAlertDesc1() {
        return alertDesc1;
    }

    /**
     * @param alertDesc1
     *            the alertDesc1 to set
     */
    public void setAlertDesc1(String alertDesc1) {
        this.alertDesc1 = alertDesc1;
    }

    /**
     * @return the operator2
     */
    public Byte getOperator2() {
        return operator2;
    }

    /**
     * @param operator2
     *            the operator2 to set
     */
    public void setOperator2(Byte operator2) {
        this.operator2 = operator2;
    }

    /**
     * @return the threshold2
     */
    public String getThreshold2() {
        return threshold2;
    }

    /**
     * @param threshold2
     *            the threshold2 to set
     */
    public void setThreshold2(String threshold2) {
        this.threshold2 = threshold2;
    }

    /**
     * @return the alertLevel2
     */
    public Byte getAlertLevel2() {
        return alertLevel2;
    }

    /**
     * @param alertLevel2
     *            the alertLevel2 to set
     */
    public void setAlertLevel2(Byte alertLevel2) {
        this.alertLevel2 = alertLevel2;
    }

    /**
     * @return the count2
     */
    public Integer getCount2() {
        return count2;
    }

    /**
     * @param count2
     *            the count2 to set
     */
    public void setCount2(Integer count2) {
        this.count2 = count2;
    }

    /**
     * @return the alertDesc2
     */
    public String getAlertDesc2() {
        return alertDesc2;
    }

    /**
     * @param alertDesc2
     *            the alertDesc2 to set
     */
    public void setAlertDesc2(String alertDesc2) {
        this.alertDesc2 = alertDesc2;
    }

    /**
     * @return the defaultThreshold
     */
    public Boolean getDefaultThreshold() {
        return defaultThreshold;
    }

    /**
     * @param defaultThreshold
     *            the defaultThreshold to set
     */
    public void setDefaultThreshold(Boolean defaultThreshold) {
        this.defaultThreshold = defaultThreshold;
    }

}
