/***********************************************************************
 * $Id: AutoDiscoveryCallBack.java,v1.0 2014-5-11 下午4:29:00 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.callback;

import com.topvision.ems.facade.domain.AutoDiscoveryInfo;
import com.topvision.ems.facade.domain.TopoHandlerProperty;
import com.topvision.framework.annotation.Callback;

/**
 * @author Rod John
 * @created @2014-5-11-下午4:29:00
 * 
 */
@Callback(beanName = "autoDiscoveryService", serviceName = "autoDiscoveryService")
public interface AutoDiscoveryCallBack {
    /**
     * 自动发现设备拓扑回调
     * 
     * @param option
     * @return
     */
    void discoveryExporter(AutoDiscoveryInfo option);

    /**
     * 自动发现结束功能回调
     * 
     */
    void discoveryComplete();
    
    /**
     * 获取设备拓扑采集器
     * 
     * @param sysOid
     * @return
     */
    TopoHandlerProperty searchProductTopoInfo(String sysOid);
    
}
