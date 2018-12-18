/***********************************************************************
 * $Id: FrontEndLogService.java,v1.0 2017年3月30日 上午9:26:58 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import com.topvision.framework.service.Service;

/**
 * @author vanzand
 * @created @2017年3月30日-上午9:26:58
 *
 */
public interface FrontEndLogService extends Service {
    /**
     * 获取当前客户端的前端日志开启情况
     * 
     * @param sessionId
     * @return
     */
    Boolean getFrontEndLogStatus(String sessionId);
    
    /**
     * 开启对应客户端的前端日志
     * @param sessionId
     */
    void openFrontEndLog(String sessionId);
    
    /**
     * 关闭对应客户端的前端日志
     * @param sessionId
     */
    void closeFrontEndLog(String sessionId);
}
