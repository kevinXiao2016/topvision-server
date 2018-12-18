/***********************************************************************
 * $Id: EntityPonRelation.java,v1.0 2011-11-4 下午01:05:36 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-11-4-下午01:05:36
 * 
 */
@Alias("entityRelation")
public class EntityRelation implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -676093185931363014L;
    private Long entityId;
    private String ip;// entity ip
    private Long ponId;
    private Long cmcId;
    private Long cmcMac;
    private Long ponNo;
    private Long ponIndex;
    private Long cmcIndex;

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
     * @return the ponId
     */
    public Long getPonId() {
        return ponId;
    }

    /**
     * @param ponId
     *            the ponId to set
     */
    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    /**
     * @return the ponIndex
     */
    public Long getPonIndex() {
        return ponIndex;
    }

    /**
     * @param ponIndex
     *            the ponIndex to set
     */
    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
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
     * @return the cmcMac
     */
    public Long getCmcMac() {
        return cmcMac;
    }

    /**
     * @param cmcMac
     *            the cmcMac to set
     */
    public void setCmcMac(Long cmcMac) {
        this.cmcMac = cmcMac;
    }

    /**
     * @return the ponNo
     */
    public Long getPonNo() {
        return ponNo;
    }

    /**
     * @param ponNo
     *            the ponNo to set
     */
    public void setPonNo(Long ponNo) {
        this.ponNo = ponNo;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityRelation [entityId=");
        builder.append(entityId);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcMac=");
        builder.append(cmcMac);
        builder.append(", ponNo=");
        builder.append(ponNo);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
