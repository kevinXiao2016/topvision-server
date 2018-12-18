/***********************************************************************
 * $Id: BfsxService.java,v1.0 2014年9月23日 下午2:40:23 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2014年9月23日-下午2:40:23
 *
 */
public interface BfsxService extends Service {
    /**
     * 部分刷新设备
     * @param entityId
     */
    void refreshEntity(long entityId);
}
