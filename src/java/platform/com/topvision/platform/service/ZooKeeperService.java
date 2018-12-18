/***********************************************************************
 * $Id: ZooKeeperService.java,v1.0 2015年4月8日 上午11:28:37 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.topvision.framework.service.Service;

/**
 * @author Victor
 * @created @2015年4月8日-上午11:28:37
 *
 */
public interface ZooKeeperService extends Service {
    public ApplicationConfig getApplication();

    public RegistryConfig getRegistry();

    public ProtocolConfig getProtocol();
}
