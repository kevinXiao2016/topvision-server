/***********************************************************************
 * $Id: CmtsBaseDao.java,v1.0 2013-7-20 下午04:31:35 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmts.domain.CmtsBaseInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author jay
 * @created @2013-7-20-下午04:31:35
 */
public interface CmtsBaseDao extends BaseEntityDao<Entity> {

    /**
     * 获取CMTS基本信息
     * 
     * @param cmcId
     *            Long
     * @return CmcAttribute
     */
    CmcAttribute getCmtsSystemBasicInfoByCmcId(Long cmcId);
    
    /**
     * 获取CMTS基本信息
     * 
     * @param entityId
     *            Long
     * @return EntitySnap
     */
    EntitySnap selectCmtsSnapByEntityId(Long entityId);

    /**
     * 查询CMTS列表
     * 
     * @param cmcQueryMap
     * @param start
     * @param limit
     * @return
     */
    List<CmcAttribute> queryCmtsList(Map<String, Object> cmcQueryMap, int start, int limit);

    /**
     * 查询CMTS列表个数
     * 
     * @param cmcQueryMap
     * @return
     */
    Long queryCmtsNum(Map<String, Object> cmcQueryMap);

    /**
     * 更新CMTS基本信息
     * @param cmtsBaseInfo
     */
    void updateCmtsBaseInfo(CmtsBaseInfo cmtsBaseInfo);
}