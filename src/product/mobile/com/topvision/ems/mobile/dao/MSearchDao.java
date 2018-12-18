/***********************************************************************
 * $Id: MSearchDao.java,v1.0 2014-6-21 下午2:06:32 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cm.domain.CcmtsLocation;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.mobile.domain.Location;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author jay
 * @created @2014-6-21-下午2:06:32
 * 
 */
public interface MSearchDao extends BaseEntityDao<Location> {
    /**
     * 根据CMTS的MAC地址搜索CMTS的列表，分页展示
     * 
     * @param map
     * @return
     */
    List<Entity> getEntityListByMac(Map<String, String> map);

    /**
     * 根据CMTS的MAC地址搜索CMTS的数量
     * 
     * @param map
     * @return
     */
    Long getEntityCountByMac(Map<String, String> map);

    /**
     * 根据CMTS的别名搜索CMTS的列表，分页展示
     * 
     * @param map
     * @return
     */
    List<Entity> getEntityListByName(Map<String, String> map);

    /**
     * 根据CMTS的别名搜索CMTS的数量
     * 
     * @param map
     * @return
     */
    Long getEntityCountByName(Map<String, String> map);

    /**
     * 根据CMTS下联CM的MAC地址搜索CMTS列表
     * 
     * @param map
     * @return
     */
    List<Entity> getEntityListByCmMac(Map<String, String> map);

    /**
     * 根据CMTS下联CM的MAC地址搜索CMTS的总数
     * 
     * @param map
     * @return
     */
    Long getEntityCountByCmMac(Map<String, String> map);

    /**
     * 根据CMTS的ID获取定位信息
     * 
     * @param cmcId
     * @return
     */
    CcmtsLocation getCmtsLocation(long cmcId);
}
