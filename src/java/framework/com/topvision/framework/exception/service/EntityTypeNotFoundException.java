/***********************************************************************
 * $Id: EntityTypeNotFoundException.java,v1.0 2013-3-6 下午02:31:54 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.service;

/**
 * @author RodJohn
 * @created @2013-3-6-下午02:31:54
 * 
 */
public class EntityTypeNotFoundException extends ServiceException {

    private static final long serialVersionUID = -2997514869485600270L;

    public EntityTypeNotFoundException() {
        super();
    }

    public EntityTypeNotFoundException(String message) {
        super(message);
    }

    public EntityTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityTypeNotFoundException(Throwable cause) {
        super(cause);
    }
}
