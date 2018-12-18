/***********************************************************************
 * $Id: EngineFacade.java,v 1.1 Mar 12, 2009 8:06:00 PM kelers Exp $
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
 * 用于定义Engine端的bean访问名称和rmi访问名称
 * 
 * @Create Date Mar 12, 2009 8:06:00 PM
 * 
 * @author kelers
 * 
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EngineFacade {
    /**
     * @return 此名称必须和spring中其实现类的bean名称一致，或者说其实现类在spring中定义的名称必须采用这个名称
     */
    String beanName();

    /**
     * @return 对外RMI或者HTTP暴露的服务名称，可以采用和beanName一样的名称
     */
    String serviceName();

    /**
     * @return 定义的分类，用于对engine进行功能分类，然后不同engine可以配置支持的分类类型
     */
    String category() default "Default";
}
