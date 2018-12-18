/***********************************************************************
 * $Id: DebugService.java,v 1.1 2013-4-27 上午9:54:21 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2006-2008 TopoView All rights reserved.
 ***********************************************************************/
package com.topvision.platform.debug;

import org.springframework.beans.factory.BeanFactory;

/**
 * @Create Date 2013-4-27 上午9:54:21
 * 
 * @author Victor
 * 
 */
public interface DebugService {
    String debug(BeanFactory beanFactory);
}
