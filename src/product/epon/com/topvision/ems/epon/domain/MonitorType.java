/***********************************************************************
 * $Id: MonitorType.java,v1.0 2012-3-23 下午02:03:58 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

/**
 * @author huqiao
 * @created @2012-3-23-下午02:03:58
 * 
 */
public class MonitorType {

    private String fieldText;
    private String fieldTable;
    private String fieldOid;
    private String primaryKey;
    private Long primaryKeyValue;

    /**
     * @return the fieldText
     */
    public String getFieldText() {
        return fieldText;
    }

    /**
     * @param fieldText
     *            the fieldText to set
     */
    public void setFieldText(String fieldText) {
        this.fieldText = fieldText;
    }

    /**
     * @return the fieldTable
     */
    public String getFieldTable() {
        return fieldTable;
    }

    /**
     * @param fieldTable
     *            the fieldTable to set
     */
    public void setFieldTable(String fieldTable) {
        this.fieldTable = fieldTable;
    }

    /**
     * @return the fieldOid
     */
    public String getFieldOid() {
        return fieldOid;
    }

    /**
     * @param fieldOid
     *            the fieldOid to set
     */
    public void setFieldOid(String fieldOid) {
        this.fieldOid = fieldOid;
    }

    /**
     * @return the primaryKey
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey
     *            the primaryKey to set
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the primaryKeyValue
     */
    public Long getPrimaryKeyValue() {
        return primaryKeyValue;
    }

    /**
     * @param primaryKeyValue
     *            the primaryKeyValue to set
     */
    public void setPrimaryKeyValue(Long primaryKeyValue) {
        this.primaryKeyValue = primaryKeyValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MonitorType [fieldText=");
        builder.append(fieldText);
        builder.append(", fieldTable=");
        builder.append(fieldTable);
        builder.append(", fieldOid=");
        builder.append(fieldOid);
        builder.append(", primaryKey=");
        builder.append(primaryKey);
        builder.append(", primaryKeyValue=");
        builder.append(primaryKeyValue);
        builder.append("]");
        return builder.toString();
    }

}
