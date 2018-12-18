/***********************************************************************
 * $Id: CheckFacade.java,v 1.1 Jul 19, 2009 10:59:12 PM kelers Exp $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.enginemgr;


/**
 * @Create Date Jul 19, 2009 10:59:12 PM
 * 
 * @author Rod John
 * 
 */
public interface CheckFacade {
    
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
}
