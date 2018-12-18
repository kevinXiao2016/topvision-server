/***********************************************************************
 * $Id: OperationLogProperty.java,v1.0 2011-12-2 下午12:00:30 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huqiao
 * @created @2011-12-2-下午12:00:30
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogProperty {
  
    String actionName();
    
    String operationName();
    
     
}
