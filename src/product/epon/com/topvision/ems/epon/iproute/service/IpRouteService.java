/***********************************************************************
 * $Id: IpRouteService.java,v1.0 2013-11-16 下午03:30:41 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.iproute.domain.IpRoute;
import com.topvision.ems.epon.iproute.domain.StaticIpRouteConfig;
import com.topvision.framework.service.Service;


/**
 * @author Rod John
 * @created @2013-11-16-下午03:30:41
 * 
 */
public interface IpRouteService extends Service {

    /**
     * 刷新IpRoute信息
     * 
     * @param entityId
     */
    void refreshIpRoute(Long entityId);
    
    /**
     * 刷新静态路由信息
     * @param entityId
     */
    void refreshStaticIpRoute(Long entityId);

    /**
     * 添加IpRoute配置
     * @param entityId
     * @param ipRouteConfig
     */
    void addIpRouteConfig(Long entityId, StaticIpRouteConfig ipRouteConfig);

    /**
     * 修改IpRoute配置
     * @param entityId
     * @param ipRouteConfig
     */
    void modifyIpRouteConfig(Long entityId, StaticIpRouteConfig ipRouteConfig);

    /**
     * 删除IpRoute配置
     * @param entityId
     * @param ipRouteConfig
     */
    void deleteIpRouteConfig(Long entityId, StaticIpRouteConfig ipRouteConfig);

    /**
     * 获取IpRoute数据
     * @param map
     * @return
     */
    List<IpRoute> getIpRouteList(Map<String, Object> map);
    
    /**
     * 获取IpRoute总数
     * @param entityId
     * @return
     */
    int getIpRouteCount(Map<String, Object> map);

}
