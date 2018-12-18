/***********************************************************************
 * $Id: CmcService.java,v1.0 2011-10-25 下午04:29:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.EntityPonRelation;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.framework.service.Service;

/**
 * 基本功能
 * 
 * @author loyal
 * @created @2011-10-25-下午04:29:45
 * 
 */
public interface CmcListService extends Service {
    /**
     * 获取所有entity设备信息，用作选择设备下拉框
     * 
     * @return List<EntityPonRelation>
     */
    List<EntityPonRelation> getEntityPonInfoList();
    
    /**
     * 根据条件查询cmc 8800b列表
     * 
     * @param cmcQueryMap
     *            cmc查询信息
     * @param start Integer
     *            分页strat
     * @param limit Integer
     *            分页limit
     * @return List<CmcAttribute>
     */
    List<CmcAttribute> queryCmc8800BList(Map<String, Object> cmcQueryMap);
    
    /**
     * 获取cmc 8800b数
     * 
     * @param cmcQueryMap
     *            cmc查询信息
     * @return Long cmc数
     */
    Long queryCmc8800BNum(Map<String, Object> cmcQueryMap);
    
    /**
     * 根据条件查询cmc列表
     * 
     * @param cmcQueryMap
     *            cmc查询信息
     * @param start Integer
     *            分页strat
     * @param limit Integer
     *            分页limit
     * @return List<CmcAttribute>
     */
    List<CmcAttribute> queryCmcList(Map<String, Object> cmcQueryMap);
    
    /**
     * 获取cmc数
     * 
     * @param cmcQueryMap
     *            cmc查询信息
     * @return Long cmc数
     */
    Long getCmcNum(Map<String, Object> cmcQueryMap);
    
    
    /**
     * 获取当前olt设备上的所有CC的nmName
     * @param entityId
     */
    public Map<Object, Object> getAllOnuIdToCmcNmnameMap(Long entityId);
}
