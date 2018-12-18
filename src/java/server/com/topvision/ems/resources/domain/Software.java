/***********************************************************************
 * $Id: Software.java,v 1.1 Oct 23, 2008 1:47:24 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.resources.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * 
 * @Create Date Oct 23, 2008 1:47:24 PM
 * 
 * @author kelers
 * 
 */
public class Software extends BaseEntity {
    private static final long serialVersionUID = -8960177703444788744L;
    private int index;
    private String name;
    private String productId;
    private byte type;
    private String date;

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return the type
     */
    public byte getType() {
        return type;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param index
     *            the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder(index).append(".").append(name).append(" - [").append(date).append("]").toString();
    }
}
