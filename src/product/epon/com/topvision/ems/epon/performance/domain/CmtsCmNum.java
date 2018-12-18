/***********************************************************************
 * $Id: DsUserNum.java,v1.0 2012-7-11 下午01:36:04 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2012-7-11-下午01:36:04
 * 
 */
@Alias("cmtsCmNum")
public class CmtsCmNum implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 5593175368669182768L;
    private Long entityId;
    private Long cmcId;
    private Timestamp dt;
    private Long ifIndex;
    private Integer cmNumTotal = 0;
    private Integer cmNumOnline = 0;
    private Integer cmNumActive = 0;
    private Integer cmNumUnregistered = 0;
    private Integer cmNumOffline = 0;
    private Integer CmNumRregistered = 0;// 注册cm数
    private String cmtsName;

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
     * @return the cmNumTotal
     */
    public Integer getCmNumTotal() {
        return cmNumTotal;
    }

    /**
     * @param cmNumTotal
     *            the cmNumTotal to set
     */
    public void setCmNumTotal(Integer cmNumTotal) {
        this.cmNumTotal = cmNumTotal;
    }

    /**
     * @return the cmNumOnline
     */
    public Integer getCmNumOnline() {
        return cmNumOnline;
    }

    /**
     * @param cmNumOnline
     *            the cmNumOnline to set
     */
    public void setCmNumOnline(Integer cmNumOnline) {
        this.cmNumOnline = cmNumOnline;
    }

    /**
     * @return the cmNumActive
     */
    public Integer getCmNumActive() {
        return cmNumActive;
    }

    /**
     * @param cmNumActive
     *            the cmNumActive to set
     */
    public void setCmNumActive(Integer cmNumActive) {
        this.cmNumActive = cmNumActive;
    }

    /**
     * @return the cmNumUnregistered
     */
    public Integer getCmNumUnregistered() {
        return cmNumUnregistered;
    }

    /**
     * @param cmNumUnregistered
     *            the cmNumUnregistered to set
     */
    public void setCmNumUnregistered(Integer cmNumUnregistered) {
        this.cmNumUnregistered = cmNumUnregistered;
    }

    /**
     * @return the cmNumOffline
     */
    public Integer getCmNumOffline() {
        return cmNumOffline;
    }

    /**
     * @param cmNumOffline
     *            the cmNumOffline to set
     */
    public void setCmNumOffline(Integer cmNumOffline) {
        this.cmNumOffline = cmNumOffline;
    }

    /**
     * @return the cmNumRregistered
     */
    public Integer getCmNumRregistered() {
        return CmNumRregistered;
    }

    /**
     * @param cmNumRregistered
     *            the cmNumRregistered to set
     */
    public void setCmNumRregistered(Integer cmNumRregistered) {
        CmNumRregistered = cmNumRregistered;
    }

    /**
     * @return the dt
     */
    public Timestamp getDt() {
        return dt;
    }

    /**
     * @param dt
     *            the dt to set
     */
    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmtsName() {
        return cmtsName;
    }

    public void setCmtsName(String cmtsName) {
        this.cmtsName = cmtsName;
    }

}
