/***********************************************************************
 * $Id: TrapListenportsService.java,v1.0 2013-4-1 下午3:25:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.SystemPreferences;

/**
 * @author flack
 * @created @2013-4-1-下午3:25:47
 *
 */
public interface TrapListenportsService extends Service {
    
    /**
     * 将用户设置的Trap监听端口值保存到数据库
     * 同时进行用户设置的Trap监听端口值的配置
     * @param trapListenports
     *          用户设置的Trap监听端口值
     */
    public void saveTrapListenports(String trapListenports);
    
    /**
     * 将用户设置的Trap监听端口值配置到系统中
     * 使用户设置的Trap监听端口值生效
     * @param trapListenports
     *          用户设置的Trap监听端口值
     */
    public void configTrapListenports(String trapListenports);

    /**
     * 根据name获取Trap系统选项
     * @param name
     * @return
     */
    public SystemPreferences getTrapPreferences(String name);

}
