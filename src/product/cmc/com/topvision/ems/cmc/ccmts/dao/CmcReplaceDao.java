/***********************************************************************
 * $Id: CmcReplaceDao.java,v1.0 2016-4-18 下午2:23:25 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.CmcReplaceInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Rod John
 * @created @2016-4-18-下午2:23:25
 *
 */
public interface CmcReplaceDao extends BaseEntityDao<Entity> {
    
    /**
     * getOnuMacListByEntityId
     * 
     * @param entityId
     * @return
     */
    Map<String, Map<String, Object>> getOnuMacListByEntityId(Long entityId);
    
    /**
     * getCmcAttributeByMacAddress
     * 
     * @param entityId
     * @param mac
     * @return
     */
    CmcAttribute getCmcAttributeByMacAddress(Long entityId, String mac);
    
    /**
     * modifyEntityIpAndMac
     * 
     * @param entityId
     * @param ip
     * @param mac
     */
    void modifyEntityIpAndMac(Long entityId, String ip, String mac);
    
    /**
     * modifyEntityMac
     * 
     * @param entityId
     * @param mac
     */
    void modifyEntityMac(Long entityId, String mac);
    
    /**
     * Load Cmc ReplaceList
     * 
     * @return
     */
    List<CmcReplaceInfo> loadCmcReplaceList(Long cmcId);
    
    
    /**
     * 替换CMC-II后同步信息
     * 
     * @param cmcId
     * @param onuIndex
     * @param entityId
     * @param macAddress
     */
    void syncCmcInfoAfterReplace(Long cmcId, Long onuIndex, Long entityId, String macAddress);
}
