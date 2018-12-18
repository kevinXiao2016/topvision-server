/***********************************************************************
 * $Id: IpRouteDao.java,v1.0 2013-11-16 下午03:02:58 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.iproute.domain.IpRoute;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Rod John
 * @created @2013-11-16-下午03:02:58
 * 
 */
public interface IpRouteDao extends BaseEntityDao<IpRoute> {

    /**
     * 刷新IpRoute的信息
     * 
     * @param entityId
     * @param routes
     */
    void refreshIpRoute(Long entityId, List<IpRoute> routes);
    
    /**
     * 添加IpRoute数据
     * @param ipRoute
     */
    void insertIpRoute(IpRoute ipRoute);

    /**
     * 删除IpRoute数据
     * @param ipRoute
     */
    void deleteIpRoute(IpRoute ipRoute);

    /**
     * 获取IpRoute数据列表
     * @return
     */
    List<IpRoute> queryIpRouteList(Map<String, Object> map);

    /**
     * 获取IpRoute总数
     * @param entityId
     * @return
     */
    int queryIpRouteCount(Map<String, Object> map);

    /**
     * 更新IpRoute数据
     * @param ipRoute
     */
    void updateIpRoute(IpRoute ipRoute);

}
