/***********************************************************************
 * $Id: TopologyHandle.java,v1.0 2013-1-16 下午04:34:09 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.parser;

import com.topvision.ems.facade.domain.BatchDiscoveryInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.TopoHandlerProperty;

/**
 * @author RodJohn
 * @created @2013-1-16-下午04:34:09
 * 
 *          接口声明在批量拓扑应实现的方法，拓扑过程中的处理器必须实现该接口
 * 
 */
public interface TopologyHandle {

    public final static String SUCCESS = "SUCCESS";
    public final static String REPLACEENTITY = "ReplaceEntity";
    public final static String ENTITYEXISTS = "EntityExists";

    public final static String UNIQUE_IP = "ip";
    public final static String UNIQUE_MAC = "mac";

    // TODO
    public final static String NULL_IP = null;

    /**
     * 处理提供采集泛型对象的值
     * 
     * @param info
     * @return
     */
    public String handleTopoResult(BatchDiscoveryInfo info, Entity entity);

    /**
     * 需要采集的泛型对象
     * 
     * @param <T>
     * @return
     */
    public TopoHandlerProperty getTopoInfo();

    /**
     * initialize方法，注册处理器到BatchDiscoveryService
     * 
     */
    public void initialize();

    /**
     * destroy方法，解注册处理器到BatchDiscoveryService
     * 
     */
    public void destroy();

}
