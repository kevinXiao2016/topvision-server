/***********************************************************************
 * $Id: ProbeFacade.java,v 1.1 Jun 8, 2009 10:46:48 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade;

import com.topvision.exception.facade.DeviceNotExistException;
import com.topvision.framework.annotation.EngineFacade;

/**
 * @Create Date Jun 8, 2009 10:46:48 PM
 * 
 * @author kelers
 * 
 */
@EngineFacade(serviceName = "ProbeFacade", beanName = "probeFacade")
public interface ProbeFacade extends Facade {
    int DEFAULT_PING_TIMEOUT = 1000;
    int DEFAULT_TCP_TIMEOUT = 1000;
    int DEFAULT_UDP_TIMEOUT = 1000;

    String getHostname(String ip);

    /**
     * 
     * @param ip
     * @param tcpPort
     * @param udpPort
     * @param timeout
     * @return
     * @throws DeviceNotExistException
     * @throws Exception
     */
    @Deprecated
    int _ping(String ip, Integer tcpPort, Integer udpPort, Integer timeout) throws DeviceNotExistException, Exception;

    /**
     * 使用给定的timout执行ping操作, 顺序为ICMP->TCP->UDP.
     * 
     * @param ip
     * @param pingTimeout
     * @param tcpPort
     * @param tcpTimeout
     * @param udpPort
     * @param udpTimeout
     * @return
     * @throws DeviceNotExistException
     * @throws Exception
     */
    @Deprecated
    int _ping(String ip, Integer pingTimeout, Integer tcpPort, Integer tcpTimeout, Integer udpPort, Integer udpTimeout)
            throws DeviceNotExistException, Exception;

    /**
     * @add by Victor@20140421把Ping为从上传timeout和count，弃用上面两个方法
     * @param ip
     * @param timeout
     * @param count
     * @return
     */
    int ping(String ip, Integer timeout, Integer count, Integer retry);
}
