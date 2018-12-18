/***********************************************************************
 * $Id: Engine.java,v1.0 2013-9-25 上午9:31:34 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 用于标识engine端的bean的注解，等同于Service、Controller、Repository、Component以及自定义Facade
 * 
 * 
 * @author Victor
 * @created @2013-9-25-上午9:31:34
 * 
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Engine {
    /**
     * The value may indicate a suggestion for a logical component name, to be turned into a Spring
     * bean in case of an autodetected component.
     * 
     * @return the suggested component name, if any
     */
    String value() default "";
}
