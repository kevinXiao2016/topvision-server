/***********************************************************************
 * $Id: VirtualEntityRelation.java,v1.0 2012-2-17 下午03:46:54 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.io.Serializable;

/**
 * @author huqiao
 * @created @2012-2-17-下午03:46:54
 * 
 */
public class VirtualEntityRelation implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4161460719053680009L;
    private Long vnEntityId;
    private Long linkedEntityId;
    private Long linkedType;

    /**
     * @return the vnEntityId
     */
    public Long getVnEntityId() {
        return vnEntityId;
    }

    /**
     * @param vnEntityId
     *            the vnEntityId to set
     */
    public void setVnEntityId(Long vnEntityId) {
        this.vnEntityId = vnEntityId;
    }

    /**
     * @return the linkedEntityId
     */
    public Long getLinkedEntityId() {
        return linkedEntityId;
    }

    /**
     * @param linkedEntityId
     *            the linkedEntityId to set
     */
    public void setLinkedEntityId(Long linkedEntityId) {
        this.linkedEntityId = linkedEntityId;
    }

    /**
     * @return the linkedType
     */
    public Long getLinkedType() {
        return linkedType;
    }

    /**
     * @param linkedType
     *            the linkedType to set
     */
    public void setLinkedType(Long linkedType) {
        this.linkedType = linkedType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VirtualEntityRelation [vnEntityId=");
        builder.append(vnEntityId);
        builder.append(", linkedEntityId=");
        builder.append(linkedEntityId);
        builder.append(", linkedType=");
        builder.append(linkedType);
        builder.append("]");
        return builder.toString();
    }

}
