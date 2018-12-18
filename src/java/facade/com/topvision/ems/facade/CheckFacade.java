/***********************************************************************
 * $Id: CheckFacade.java,v 1.1 Jul 19, 2009 10:59:12 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade;

import java.util.List;

import org.jruby.embed.ScriptingContainer;

import com.topvision.ems.facade.domain.EngineServerParam;
import com.topvision.ems.facade.domain.EngineServerStatus;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.domain.ThreadPoolMonitor;

/**
 * @Create Date Jul 19, 2009 10:59:12 PM
 * 
 * @author kelers
 * 
 */
@EngineFacade(serviceName = "CheckFacade", beanName = "checkFacade", category = "System")
public interface CheckFacade extends Facade {
    public static final int STATUS_START = 0;
    public static final int STATUS_INIT = 0;
    public static final int STATUS_CONNECTED = 0;
    public static final int STATUS_DISCONNECTED = 0;

    /**
     * 初始化Engine。在每次连接上或者重新连接上engine后调用
     * 
     * @param param
     */
    void initEngine(EngineServerParam param);

    /**
     * 修改Engine Type调用
     * 
     * @param param
     */
    void syncEngine(EngineServerParam param);

    /**
     * 关闭采集器
     * 
     */
    void shutDown();

    /**
     * 获取分布式采集器的实时状态
     */
    EngineServerStatus getEngineServerStatus();

    /**
     * 获取分布式采集器的admin状态
     */
    String getAdminStatus();

    /**
     * 把engine日志修改为默认配置，在目录/conf下找logback-test.xml和logback.xml，优先使用logback-test.xml文件
     */
    boolean resetLogger();

    /**
     * 执行ruby脚本，返回值为HTML格式，用于admin中的debug展示
     */
    String runScripts(String scripts);

    /**
     * 远程执行hsqldb
     * 
     * @param action
     *            execute | query
     * @param sql
     *            SQL
     * @return
     */
    String executeHsql(String action, String sql);

    /**
     * @return 返回各个线程池的信息
     */
    List<ThreadPoolMonitor> getThreadPoolMonitor();

    /**
     * 通过JRuby脚本在网管engine端运行后并返回值
     * 
     * @param jrubyScript
     * @return 由脚本定制返回值
     */
    Object dynamicInvoke(String jrubyScript);
}
