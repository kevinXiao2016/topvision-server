/***********************************************************************
 * $Id: ResourceModuleNotFoundException.java,v1.0 2013-3-27 下午4:00:04 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.exception;

/**
 * 国际化资源没有被找到
 * @author Bravin
 * @created @2013-3-27-下午4:00:04
 *
 */
public class ResourceModuleNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4290927885126927446L;

    public ResourceModuleNotFoundException() {
        super();
    }

    public ResourceModuleNotFoundException(String message) {
        super(message);
    }

    public ResourceModuleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceModuleNotFoundException(Throwable cause) {
        super(cause);
    }
}
