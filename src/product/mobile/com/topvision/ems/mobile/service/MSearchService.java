/***********************************************************************
 * $Id: MSearchService.java,v1.0 2014-6-21 下午2:06:32 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.mobile.domain.Location;
import com.topvision.framework.service.Service;

/**
 * @author jay
 * @created @2014-6-21-下午2:06:32
 * 
 */

public interface MSearchService extends Service {
    /**
     * 根据CMTS的MAC地址搜索CMTS的列表，分页展示
     * 
     * @param map
     * @return
     */
    List<Location> queryByCmtsMac(Map<String, String> map);

    /**
     * 根据CMTS的MAC地址搜索CMTS的数量
     * 
     * @param map
     * @return
     */
    Long queryCountByCmtsMac(Map<String, String> map);

    /**
     * 根据CMTS的别名搜索CMTS的列表，分页展示
     * 
     * @param map
     * @return
     */
    List<Location> queryByCmtsName(Map<String, String> map);

    /**
     * 根据CMTS的别名搜索CMTS的数量
     * 
     * @param map
     * @return
     */
    Long queryCountByCmtsName(Map<String, String> map);

    /**
     * 根据CMTS下联CM的MAC地址搜索CMTS列表
     * 
     * @param map
     * @return
     */
    List<Location> queryByCmMac(Map<String, String> map);

    /**
     * 根据CMTS下联CM的MAC地址搜索CMTS的总数
     * 
     * @param map
     * @return
     */
    Long queryCountByCmMac(Map<String, String> map);
}
