/***********************************************************************
 * $Id: TableProperty.java,v1.0 2011-10-9 下午01:47:04 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Victor
 * @created @2011-10-9-下午01:47:04
 * 
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface TableProperty {
    String[] tables() default { "default" };
}
