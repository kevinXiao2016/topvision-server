/***********************************************************************
 * $Id: CmtsBaseService.java,v1.0 2013-7-20 下午04:29:45 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.service.Service;

/**
 * 基本功能
 *
 * @author jay
 * @created @2013-7-20-下午04:29:45
 */
public interface CmtsBaseService extends Service {

    /**
     * 获取CMTS基本信息
     *
     * @param cmcId Long
     * @return CmcAttribute
     */
    CmcAttribute getCmtsSystemBasicInfoByCmcId(Long cmcId);
    
    /**
     * 获取CMTS基本信息
     *
     * @param entityId Long
     */
    void refreshCmts(Long entityId);
    
    /**
     * 获取CMTS快照信息
     *
     * @param entityId Long
     */
    EntitySnap getCmtsSnapByEntityId(Long entityId);
    
    /**
     * 修改cmts基本信息
     * @param cmcId
     * @param name
     * @param sysLocation
     * @param connectPerson
     */
    void modifyCmtsBasicInfo(Long cmcId, String name, String sysLocation, String connectPerson);

    /**
     * 查询CMTS列表
     * @param cmcQueryMap
     * @param start
     * @param limit
     * @return
     */
    List<CmcAttribute> queryCmtsList(Map<String, Object> cmcQueryMap, int start, int limit);

    /**
     * 查询CMTS列表个数
     * @param cmcQueryMap
     * @return
     */
    Long queryCmtsNum(Map<String, Object> cmcQueryMap);
    
    /**
     * 修改entityname
     * @param entityId
     * @param entityName
     * @return
     */
    void renameEntityName(Long entityId, String entityName);

    /**
     * 解析出CMTS的软件版本
     * 
     * @param cmc
     */
    void analyseSofewareVersion(CmcAttribute cmc);
}