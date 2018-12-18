/***********************************************************************
 * $Id: CmcCmNumStatic.java,v1.0 2012-7-25 下午02:36:57 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2012-7-25-下午02:36:57
 * 
 */
@Alias("cmcCmNumStatic")
public class CmcCmNumStatic implements AliasesSuperType {
    private static final long serialVersionUID = 3238121575035101426L;
    private Long entityId;
    private Long cmcId;
    private Integer cmNumTotal;
    private Integer cmNumOnline;
    private Integer cmNumActive;
    private Integer cmNumOffline;
    
    //已注册和未注册不提供了，没有实际意义，20131121  lzs
//    private Integer cmNumUnregistered;
//    private Integer cmNumRregistered;

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
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
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

//    /**
//     * @return the cmNumUnregistered
//     */
//    public Integer getCmNumUnregistered() {
//        return cmNumUnregistered;
//    }
//
//    /**
//     * @param cmNumUnregistered
//     *            the cmNumUnregistered to set
//     */
//    public void setCmNumUnregistered(Integer cmNumUnregistered) {
//        this.cmNumUnregistered = cmNumUnregistered;
//    }

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

//    /**
//     * @return the cmNumRregistered
//     */
//    public Integer getCmNumRregistered() {
//        return cmNumRregistered;
//    }
//
//    /**
//     * @param cmNumRregistered
//     *            the cmNumRregistered to set
//     */
//    public void setCmNumRregistered(Integer cmNumRregistered) {
//        this.cmNumRregistered = cmNumRregistered;
//    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcCmNumStatic [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmNumTotal=");
        builder.append(cmNumTotal);
        builder.append(", cmNumOnline=");
        builder.append(cmNumOnline);
        builder.append(", cmNumActive=");
        builder.append(cmNumActive);
//        builder.append(", cmNumUnregistered=");
//        builder.append(cmNumUnregistered);
        builder.append(", cmNumOffline=");
        builder.append(cmNumOffline);
//        builder.append(", cmNumRregistered=");
//        builder.append(cmNumRregistered);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
    
    
}
