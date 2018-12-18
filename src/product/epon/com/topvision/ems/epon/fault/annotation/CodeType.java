/***********************************************************************
 * $Id: CodeType.java,v1.0 2012-2-2 上午11:18:41 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Victor
 * @created @2012-2-2-上午11:18:41
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CodeType {
    int[] type();
}
