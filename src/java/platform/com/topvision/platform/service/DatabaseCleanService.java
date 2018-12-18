/***********************************************************************
 * $Id: DatabaseCleanService.java,v1.0 2015-6-3 下午3:32:30 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import com.topvision.framework.annotation.DynamicDB;
import com.topvision.framework.service.Service;

/**
 * @author Rod John
 * @created @2015-6-3-下午3:32:30
 *
 */
public interface DatabaseCleanService extends Service {
    
    
    @DynamicDB
    void txCleanHistoryData(Integer keepMonth);

    @DynamicDB
    void txUpdateCleanTask(Integer keepMonth);
}
