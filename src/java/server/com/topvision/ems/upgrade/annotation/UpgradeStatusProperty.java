/***********************************************************************
 * $Id: UpgradeStatusProperty.java,v1.0 2014年11月22日 下午1:13:52 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author loyal
 * @created @2014年11月22日-下午1:13:52
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UpgradeStatusProperty {
    /**
     * 状态码ID
     * 
     * @return
     */
    String id() default "-1";

    /**
     * 状态说明
     * 
     * @return
     */
    String displayName() default "";
    
    /**
     * 状态类型（升级完成，升级错误，正在升级）
     * 
     * @return
     */
    String statusType() default "";
}
