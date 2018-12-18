/***********************************************************************
 * $Id: EngineDB.java,v1.0 2015-3-28 下午1:49:35 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Rod John
 * @created @2015-3-28-下午1:49:35
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicDB {
    
    /**
     * The value of dbKey indicate to the datasource key config in the applicationContext.xml
     * 
     * default value = engine
     * 
     * the value must be one of the entry key of the dynamic datasource
     * 
     * @return
     */
    String dbKey() default "engine";

}
