/***********************************************************************
 * $Id: ConfigEngineNbiFacade.java,v1.0 2016年3月14日 上午10:42:05 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.nbi;

import com.topvision.framework.annotation.EngineFacade;

/**
 * SERVER-ENGINE的Facade接口
 * @author Bravin
 * @created @2016年3月14日-上午10:42:05
 *
 */
@EngineFacade(serviceName = "ConfigEngineNbiFacade", beanName = "configEngineNbiFacade", category = "Performance")
public interface ConfigEngineNbiFacade {

    /**
     * 设置Engine端的Nbi配置
     */
    void notifyEngineConnectNbi(NbiAddress nbiAddress);
}
