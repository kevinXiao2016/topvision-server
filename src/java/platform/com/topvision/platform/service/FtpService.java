/***********************************************************************
 * $Id: FtpService.java,v1.0 2011-10-8 下午04:09:41 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import com.topvision.framework.service.Service;

/**
 * @author huqiao
 * @created @2011-10-8-下午04:09:41
 * 
 */
public interface FtpService extends Service {

    /**
     * 获得FTP服务初始化状态
     * 
     * @return
     */
    boolean getFtpServiceStatus();

}
