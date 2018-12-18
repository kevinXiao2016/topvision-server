/***********************************************************************
 * $Id: TL1CommandCode.java,v1.0 2017年1月7日 下午4:14:33 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.tl1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bravin
 * @created @2017年1月7日-下午4:14:33
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TL1CommandCode {

    String code();
}
