/***********************************************************************
 * $Id: TL1ExecutorRegistryService.java,v1.0 2017年1月7日 下午4:19:25 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.tl1;

/**
 * @author Bravin
 * @created @2017年1月7日-下午4:19:25
 *
 */
public interface TL1ExecutorRegistryService {

    /**
     * @param commandCode
     * @return
     */
    TL1ExecutorService<?> getService(String commandCode);

}
