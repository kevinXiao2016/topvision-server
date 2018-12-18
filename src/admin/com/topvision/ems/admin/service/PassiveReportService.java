/***********************************************************************
 * $Id: PassiveReportService.java,v1.0 Sep 20, 2016 8:34:49 AM $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topvision.ems.admin.domain.EmsInfo;
import com.topvision.ems.admin.domain.SystemMonitor;
import com.topvision.framework.domain.ThreadPoolMonitor;

/**
 * @author Victorli
 * @created @Sep 20, 2016-8:34:49 AM
 * 
 *          用于汇报网管信息，支持主动上报和被动采集两种模式
 *
 */
public interface PassiveReportService {
    public static final String ABILITY_PROACTIVE = "proactive";
    public static final String ABILITY_PASSIVE = "passive";

    /**
     * @return 加密狗的硬件ID
     */
    String getHid();

    /**
     * @return NM3000基本信息
     */
    EmsInfo getEmsInfo();

    /**
     * @return 返回网管的能力集，有proactive(主动报告),passive(被动采集)
     */
    Set<String> getAbility();

    /**
     * 由于版本可能不同，所以每个网管可能实现的方法不一样，用于远程调用时先判断
     * 
     * @return 所有方法名称
     */
    Set<String> getMethodNames();

    /**
     * @return 网管服务器的所有IP
     */
    List<String> getHostIPs();

    /**
     * CPU内存的信息
     * 
     * @param map
     * @return
     */
    List<SystemMonitor> getCpuAndMemory(Map<String, Object> map);

    /**
     * @return 返回所有线程池的信息
     */
    List<ThreadPoolMonitor> getThreadPoolMonitor();

    /**
     * 获取所有设备类型及其设备数量
     * 
     * @return Map.key=设备类型名称；Map.value=设备数量
     */
    Map<String, Integer> getDeviceCount();

    /**
     * 通过JRuby脚本在网管Server端运行后并返回值
     * 
     * @param jrubyScript
     * @return 由脚本定制返回值
     */
    Object dynamicInvoke(String jrubyScript);
}
