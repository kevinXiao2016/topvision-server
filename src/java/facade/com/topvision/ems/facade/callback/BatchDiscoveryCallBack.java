/***********************************************************************
 * $Id: BatchDiscoveryCallBack.java,v1.0 2012-11-16 下午03:25:31 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.callback;

import com.topvision.ems.facade.domain.BatchDiscoveryInfo;
import com.topvision.ems.facade.domain.DwrInfo;
import com.topvision.ems.facade.domain.TopoHandlerProperty;
import com.topvision.framework.annotation.Callback;

/**
 * @author RodJohn
 * @created @2012-11-16-下午03:25:31
 * 
 */
@Callback(beanName = "batchDiscoveryService", serviceName = "batchDiscoveryService")
public interface BatchDiscoveryCallBack {

    /**
     * 通知Service层开始某设备的采集过程
     * 
     * @param option
     */
    String discoveryExporter(BatchDiscoveryInfo option);

 
    /**
     * 通知Service层多设备拓扑过程结束
     * 
     * dwrInfo 包括推送页面的相关信息
     * 
     */
    void discoveryComplete(DwrInfo dwrInfo);


    /**
     * 
     * 
     * @param sysOid
     * @return
     */
    TopoHandlerProperty searchProductTopoInfo(String sysOid);
    
}
