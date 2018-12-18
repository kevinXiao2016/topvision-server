/***********************************************************************
 * $Id: DeviceVersionService.java,v1.0 2017年10月11日 下午3:36:56 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.devicesupport.version.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.topvision.framework.service.Service;

/**
 * @author vanzand
 * @created @2017年10月11日-下午3:36:56
 *
 */
public interface DeviceVersionService extends Service {

    /**
     * 获取指定entity的用于版本控制的版本号（不同设备类型有不同的获取规则）
     * 
     * @param entityId
     * @return
     */
    String getEntityVersion(Long entityId);

    /**
     * 判断指定设备是否支持对应功能
     * 
     * @param entityId
     * @param functionName
     * @return
     */
    boolean isFunctionSupported(Long entityId, String functionName);

    /**
     * 判断指定设备对一批功能的支持情况，返回key-true/false的map结构
     * 
     * @param entityId
     * @param functionName
     * @return
     */
    Map<String, Boolean> isFunctionSupported(Long entityId, String[] functionName);

    /**
     * 获取指定设备指定功能指定参数的值
     * 
     * @param entityId
     * @param functionName
     * @param paramName
     * @return
     */
    String getParamValue(Long entityId, String functionName, String paramName);
    
    JSONObject getVersionControl(Long typeId, String version);
}
