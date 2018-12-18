/***********************************************************************
 * $Id: TelnetProperty.java,v 1.1 Mar 11, 2009 11:15:07 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Create Date Mar 11, 2009 11:15:07 PM
 * 
 * @author kelers
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface TelnetProperty {
    /**
     * 命令
     * 
     * @return
     */
    String command();
}
