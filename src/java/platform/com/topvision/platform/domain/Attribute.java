/***********************************************************************
 * $Id: Attribute.java,v 1.1 May 24, 2008 3:19:00 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 * @Create Date May 24, 2008 3:19:00 PM
 */
public class Attribute extends BaseEntity {
    private static final long serialVersionUID = -4024410965039931147L;
    private String category = null;
    private String name = null;
    private String value = null;

    public Attribute() {
    }

    public Attribute(String c, String n, String v) {
        category = c;
        name = n;
        value = v;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Attribute{" + "category='" + category + '\'' + ", name='" + name + '\'' + ", value='" + value + '\''
                + '}';
    }
}
