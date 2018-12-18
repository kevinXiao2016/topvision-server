/***********************************************************************
 * $Id: EntityPonRelation.java,v1.0 2011-11-4 下午01:05:36 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import com.topvision.framework.constants.Symbol;

/**
 * @author loyal
 * @created @2011-11-4-下午01:05:36
 * 
 */
public class EntityPonRelation implements Serializable {
    private static final long serialVersionUID = -2740717984878213275L;
    private Long entityId;
    private String entityIp;
    private Long ponId;
    private Long ponNo;
    private Long ponIndex;
    private String name;

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
        this.ponNo = (ponIndex & 0xff000000L) >> 24;
    }

    /**
     * @return the entityIp
     */
    public String getEntityIp() {
        return entityIp;
    }

    /**
     * @param entityIp
     *            the entityIp to set
     */
    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
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

    public boolean equals(Object arg0) {
        if (this.entityId.equals(((EntityPonRelation) arg0).entityId)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityPonRelation [entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", ponNo=");
        builder.append(ponNo);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", name=");
        builder.append(name);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
