/***********************************************************************
 * $Id: TopoHandlerProperty.java,v1.0 2013-1-26 下午04:30:52 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author RodJohn
 * @created @2013-1-26-下午04:30:52
 *
 */
public class TopoHandlerProperty extends BaseEntity {
    private static final long serialVersionUID = 7413778339600021397L;
    public static final int TOPO_OID = 1;
    public static final int TOPO_STRINGS = 2;
    public static final int TOPO_TABLE = 3;
    public static final int TOPO_OBJECT = 4;
    public static final int TOPO_TABLELINE = 5;
    private Object object;
    private Integer porperty;
    
    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }
    /**
     * @param object the object to set
     */
    public void setObject(Object object) {
        this.object = object;
    }
    /**
     * @return the porperty
     */
    public Integer getPorperty() {
        return porperty;
    }
    /**
     * @param porperty the porperty to set
     */
    public void setPorperty(Integer porperty) {
        this.porperty = porperty;
    }
    
    /**
     * @param obj
     * @param porperty
     */
    public TopoHandlerProperty(Object obj, Integer porperty) {
        this.object = obj;
        this.porperty = porperty;
    }

    
    
}
