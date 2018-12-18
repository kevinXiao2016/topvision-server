/***********************************************************************
 * $Id: LongRequestExecutorService.java,v1.0 2016年4月29日 上午10:33:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.message;

/**
 * @author Bravin
 * @created @2016年4月29日-上午10:33:18
 *
 */
public interface LongRequestExecutorService {

    /**
     * @param request
     */
    void executeRequest(CallbackableRequest request);

}
