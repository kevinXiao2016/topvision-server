/***********************************************************************
 * $Id: DeviceAlertReportItem.java,v1.0 2012-3-19 下午02:49:31 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2012-3-19-下午02:49:31
 * 
 */
public class DeviceAlertReportItem implements AliasesSuperType {
    private static final long serialVersionUID = -2990017069838649046L;
    private Long entityId;
    private Long levelId;
    private Long happenTimes;
    private String host;
    private String deviceIp;
    private Long allAlertNum;
    private Long fatalAlertNum;
    private Long mainAlertNum;
    private Long secondaryAlertNum;
    private Long normalAlertNum;
    private Long promptAlertNum;
    private Long errorAlertNum;

    /**
     * @return the deviceIp
     */
    public String getDeviceIp() {
        return deviceIp;
    }

    /**
     * @param deviceIp
     *            the deviceIp to set
     */
    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    /**
     * @return the allAlertNum
     */
    public Long getAllAlertNum() {
        return allAlertNum;
    }

    /**
     * @param allAlertNum
     *            the allAlertNum to set
     */
    public void setAllAlertNum(Long allAlertNum) {
        this.allAlertNum = allAlertNum;
    }

    /**
     * @return the fatalAlertNum
     */
    public Long getFatalAlertNum() {
        return fatalAlertNum;
    }

    /**
     * @param fatalAlertNum
     *            the fatalAlertNum to set
     */
    public void setFatalAlertNum(Long fatalAlertNum) {
        this.fatalAlertNum = fatalAlertNum;
    }

    /**
     * @return the mainAlertNum
     */
    public Long getMainAlertNum() {
        return mainAlertNum;
    }

    /**
     * @param mainAlertNum
     *            the mainAlertNum to set
     */
    public void setMainAlertNum(Long mainAlertNum) {
        this.mainAlertNum = mainAlertNum;
    }

    /**
     * @return the secondaryAlertNum
     */
    public Long getSecondaryAlertNum() {
        return secondaryAlertNum;
    }

    /**
     * @param secondaryAlertNum
     *            the secondaryAlertNum to set
     */
    public void setSecondaryAlertNum(Long secondaryAlertNum) {
        this.secondaryAlertNum = secondaryAlertNum;
    }

    /**
     * @return the normalAlertNum
     */
    public Long getNormalAlertNum() {
        return normalAlertNum;
    }

    /**
     * @param normalAlertNum
     *            the normalAlertNum to set
     */
    public void setNormalAlertNum(Long normalAlertNum) {
        this.normalAlertNum = normalAlertNum;
    }

    /**
     * @return the promptAlertNum
     */
    public Long getPromptAlertNum() {
        return promptAlertNum;
    }

    /**
     * @param promptAlertNum
     *            the promptAlertNum to set
     */
    public void setPromptAlertNum(Long promptAlertNum) {
        this.promptAlertNum = promptAlertNum;
    }

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
     * @return the levelId
     */
    public Long getLevelId() {
        return levelId;
    }

    /**
     * @param levelId
     *            the levelId to set
     */
    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    /**
     * @return the happenTimes
     */
    public Long getHappenTimes() {
        return happenTimes;
    }

    /**
     * @param happenTimes
     *            the happenTimes to set
     */
    public void setHappenTimes(Long happenTimes) {
        this.happenTimes = happenTimes;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the errorAlertNum
     */
    public Long getErrorAlertNum() {
        return errorAlertNum;
    }

    /**
     * @param errorAlertNum
     *            the errorAlertNum to set
     */
    public void setErrorAlertNum(Long errorAlertNum) {
        this.errorAlertNum = errorAlertNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeviceAlertReportItem [entityId=");
        builder.append(entityId);
        builder.append(", levelId=");
        builder.append(levelId);
        builder.append(", happenTimes=");
        builder.append(happenTimes);
        builder.append(", host=");
        builder.append(host);
        builder.append(", deviceIp=");
        builder.append(deviceIp);
        builder.append(", allAlertNum=");
        builder.append(allAlertNum);
        builder.append(", fatalAlertNum=");
        builder.append(fatalAlertNum);
        builder.append(", mainAlertNum=");
        builder.append(mainAlertNum);
        builder.append(", secondaryAlertNum=");
        builder.append(secondaryAlertNum);
        builder.append(", normalAlertNum=");
        builder.append(normalAlertNum);
        builder.append(", promptAlertNum=");
        builder.append(promptAlertNum);
        builder.append(", errorAlertNum=");
        builder.append(errorAlertNum);
        builder.append("]");
        return builder.toString();
    }
}
