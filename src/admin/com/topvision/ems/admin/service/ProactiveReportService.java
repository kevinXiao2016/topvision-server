/***********************************************************************
 * $Id: ProactiveReportService.java,v1.0 Sep 20, 2016 5:37:13 PM $
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
 * @created @Sep 20, 2016-5:37:13 PM
 *
 */
public interface ProactiveReportService {
    /**
     * 测试远程是否可用
     */
    void report(String hid);

    /**
     * @param 报告网管基本信息
     */
    void reportEmsInfo(EmsInfo info);

    /**
     * 上报网管的能力集，有proactive(主动报告),passive(被动采集)
     */
    void reportAbility(String hid, Set<String> abilities);

    /**
     * 网管服务器的所有IP
     */
    void reportHostIPs(String hid, List<String> ips);

    /**
     * CPU内存的信息
     * 
     */
    void reportCpuAndMemory(String hid, SystemMonitor sms);

    /**
     * 返回所有线程池的信息
     */
    void reportThreadPoolMonitor(String hid, List<ThreadPoolMonitor> tms);

    /**
     * 按类型上报设备数量
     */
    void reportDeviceCount(String hid, Map<String, Integer> counts);

    /**
     * 通过JRuby脚本在网管Server端运行后并返回值
     * 
     * @param hid
     * @return JRuby脚本
     */
    String getDynamicInvokeScript(String hid);

    /**
     * 通过JRuby脚本在网管Server端运行后并返回值
     * 
     * @param hid
     * @param result
     *            dynamic invoke的返回值
     * @getDynamicInvokeScript 和 @reportDynamicInvokeResult 组合使用
     */
    void reportDynamicInvokeResult(String hid, Object result);
}
