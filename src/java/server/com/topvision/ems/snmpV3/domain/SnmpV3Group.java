/***********************************************************************
 * $Id: SnmpV3EmsGroup.java,v1.0 2013-1-9 上午11:09:54 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.domain;

/**
 * Group 和 Access的合表，下发的时候再分开下发
 * 
 * @author Bravin
 * @created @2013-1-9-上午11:09:54
 * 
 */
public class SnmpV3Group {
    private Long entityId;
    private String snmpContextPrefix;
    private Integer snmpSecurityModel;
    private Integer snmpSecurityLevel;
    private Integer snmpContextMatch;
    private String snmpReadView;
    private String snmpWriteView;
    private String snmpNotifyView;
    private Integer snmpSecurityMode;
    private String snmpSecurityName;
    private String snmpGroupName;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the snmpContextPrefix
     */
    public String getSnmpContextPrefix() {
        return snmpContextPrefix;
    }

    /**
     * @param snmpContextPrefix
     *            the snmpContextPrefix to set
     */
    public void setSnmpContextPrefix(String snmpContextPrefix) {
        this.snmpContextPrefix = snmpContextPrefix;
    }

    /**
     * @return the snmpSecurityModel
     */
    public Integer getSnmpSecurityModel() {
        return snmpSecurityModel;
    }

    /**
     * @param snmpSecurityModel
     *            the snmpSecurityModel to set
     */
    public void setSnmpSecurityModel(Integer snmpSecurityModel) {
        this.snmpSecurityModel = snmpSecurityModel;
    }

    /**
     * @return the snmpSecurityLevel
     */
    public Integer getSnmpSecurityLevel() {
        return snmpSecurityLevel;
    }

    /**
     * @param snmpSecurityLevel
     *            the snmpSecurityLevel to set
     */
    public void setSnmpSecurityLevel(Integer snmpSecurityLevel) {
        this.snmpSecurityLevel = snmpSecurityLevel;
    }

    /**
     * @return the snmpContextMatch
     */
    public Integer getSnmpContextMatch() {
        return snmpContextMatch;
    }

    /**
     * @param snmpContextMatch
     *            the snmpContextMatch to set
     */
    public void setSnmpContextMatch(Integer snmpContextMatch) {
        this.snmpContextMatch = snmpContextMatch;
    }

    /**
     * @return the snmpReadView
     */
    public String getSnmpReadView() {
        return snmpReadView;
    }

    /**
     * @param snmpReadView
     *            the snmpReadView to set
     */
    public void setSnmpReadView(String snmpReadView) {
        this.snmpReadView = snmpReadView;
    }

    /**
     * @return the snmpWriteView
     */
    public String getSnmpWriteView() {
        return snmpWriteView;
    }

    /**
     * @param snmpWriteView
     *            the snmpWriteView to set
     */
    public void setSnmpWriteView(String snmpWriteView) {
        this.snmpWriteView = snmpWriteView;
    }

    /**
     * @return the snmpNotifyView
     */
    public String getSnmpNotifyView() {
        return snmpNotifyView;
    }

    /**
     * @param snmpNotifyView
     *            the snmpNotifyView to set
     */
    public void setSnmpNotifyView(String snmpNotifyView) {
        this.snmpNotifyView = snmpNotifyView;
    }

    /**
     * @return the snmpSecurityMode
     */
    public Integer getSnmpSecurityMode() {
        return snmpSecurityMode;
    }

    /**
     * @param snmpSecurityMode
     *            the snmpSecurityMode to set
     */
    public void setSnmpSecurityMode(Integer snmpSecurityMode) {
        this.snmpSecurityMode = snmpSecurityMode;
    }

    /**
     * @return the snmpSecurityName
     */
    public String getSnmpSecurityName() {
        return snmpSecurityName;
    }

    /**
     * @param snmpSecurityName
     *            the snmpSecurityName to set
     */
    public void setSnmpSecurityName(String snmpSecurityName) {
        this.snmpSecurityName = snmpSecurityName;
    }

    /**
     * @return the snmpGroupName
     */
    public String getSnmpGroupName() {
        return snmpGroupName;
    }

    /**
     * @param snmpGroupName
     *            the snmpGroupName to set
     */
    public void setSnmpGroupName(String snmpGroupName) {
        this.snmpGroupName = snmpGroupName;
    }

}
