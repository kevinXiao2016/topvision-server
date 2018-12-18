/***********************************************************************
 * $Id: EmsCacheService.java,v1.0 2017年9月9日 下午4:49:40 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform;

/**
 * @author vanzand
 * @created @2017年9月9日-下午4:49:40
 *
 */
public interface EmsCacheService {
    Object getCache();
    
    int getCacheSize();
    
    void clearCache();
}
