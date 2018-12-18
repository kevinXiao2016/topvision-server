/***********************************************************************
 * $Id: CmTest.java,v1.0 2014-6-16 下午4:46:40 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

/**
 * @author Administrator
 * @created @2014-6-16-下午4:46:40
 * 
 */
public class CmTest {
    private Integer folderId;
    private Integer entityId;
    private Integer ponIndex;
    private Integer ccIndex;
    private String topoFolder;
    private String oltName;
    private String oltIp;
    private String ponIndexStr;
    private String ccmtsName;
    private Integer onlineCm;
    private Integer offlineCm;
    private Integer otherCm;
    private Integer allCm;

    
    /**
     * @param folderId
     * @param entityId
     * @param ponIndex
     * @param ccIndex
     * @param topoFolder
     * @param oltName
     * @param oltIp
     * @param ponIndexStr
     * @param ccmtsName
     * @param onlineCm
     * @param offlineCm
     * @param otherCm
     * @param allCm
     */
    public CmTest(Integer folderId, Integer entityId, Integer ponIndex, Integer ccIndex, String topoFolder,
            String oltName, String oltIp, String ponIndexStr, String ccmtsName, Integer onlineCm, Integer offlineCm,
            Integer otherCm, Integer allCm) {
        this.folderId = folderId;
        this.entityId = entityId;
        this.ponIndex = ponIndex;
        this.ccIndex = ccIndex;
        this.topoFolder = topoFolder;
        this.oltName = oltName;
        this.oltIp = oltIp;
        this.ponIndexStr = ponIndexStr;
        this.ccmtsName = ccmtsName;
        this.onlineCm = onlineCm;
        this.offlineCm = offlineCm;
        this.otherCm = otherCm;
        this.allCm = allCm;
    }

    /**
     * @return the folderId
     */
    public Integer getFolderId() {
        return folderId;
    }

    /**
     * @param folderId
     *            the folderId to set
     */
    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    /**
     * @return the entityId
     */
    public Integer getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the ponIndex
     */
    public Integer getPonIndex() {
        return ponIndex;
    }

    /**
     * @param ponIndex
     *            the ponIndex to set
     */
    public void setPonIndex(Integer ponIndex) {
        this.ponIndex = ponIndex;
    }

    /**
     * @return the ccIndex
     */
    public Integer getCcIndex() {
        return ccIndex;
    }

    /**
     * @param ccIndex
     *            the ccIndex to set
     */
    public void setCcIndex(Integer ccIndex) {
        this.ccIndex = ccIndex;
    }

    /**
     * @return the topoFolder
     */
    public String getTopoFolder() {
        return topoFolder;
    }

    /**
     * @param topoFolder
     *            the topoFolder to set
     */
    public void setTopoFolder(String topoFolder) {
        this.topoFolder = topoFolder;
    }

    /**
     * @return the oltName
     */
    public String getOltName() {
        return oltName;
    }

    /**
     * @param oltName
     *            the oltName to set
     */
    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    /**
     * @return the oltIp
     */
    public String getOltIp() {
        return oltIp;
    }

    /**
     * @param oltIp
     *            the oltIp to set
     */
    public void setOltIp(String oltIp) {
        this.oltIp = oltIp;
    }

    /**
     * @return the ponIndexStr
     */
    public String getPonIndexStr() {
        return ponIndexStr;
    }

    /**
     * @param ponIndexStr
     *            the ponIndexStr to set
     */
    public void setPonIndexStr(String ponIndexStr) {
        this.ponIndexStr = ponIndexStr;
    }

    /**
     * @return the ccmtsName
     */
    public String getCcmtsName() {
        return ccmtsName;
    }

    /**
     * @param ccmtsName
     *            the ccmtsName to set
     */
    public void setCcmtsName(String ccmtsName) {
        this.ccmtsName = ccmtsName;
    }

    /**
     * @return the onlineCm
     */
    public Integer getOnlineCm() {
        return onlineCm;
    }

    /**
     * @param onlineCm
     *            the onlineCm to set
     */
    public void setOnlineCm(Integer onlineCm) {
        this.onlineCm = onlineCm;
    }

    /**
     * @return the offlineCm
     */
    public Integer getOfflineCm() {
        return offlineCm;
    }

    /**
     * @param offlineCm
     *            the offlineCm to set
     */
    public void setOfflineCm(Integer offlineCm) {
        this.offlineCm = offlineCm;
    }

    /**
     * @return the otherCm
     */
    public Integer getOtherCm() {
        return otherCm;
    }

    /**
     * @param otherCm
     *            the otherCm to set
     */
    public void setOtherCm(Integer otherCm) {
        this.otherCm = otherCm;
    }

    /**
     * @return the allCm
     */
    public Integer getAllCm() {
        return allCm;
    }

    /**
     * @param allCm
     *            the allCm to set
     */
    public void setAllCm(Integer allCm) {
        this.allCm = allCm;
    }

}
