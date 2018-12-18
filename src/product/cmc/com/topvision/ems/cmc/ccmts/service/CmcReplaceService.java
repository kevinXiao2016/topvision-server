/***********************************************************************
 * $Id: CmcReplaceService.java,v1.0 2016-4-18 下午1:55:54 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.CmcReplaceInfo;
import com.topvision.framework.service.Service;

/**
 * @author Rod John
 * @created @2016-4-18-下午1:55:54
 *
 */
public interface CmcReplaceService extends Service {

    /**
     * getOnuMacListByEntityId
     * 
     * @param entityId
     * @return
     */
    Map<String, Map<String, Object>> getOnuMacListByEntityId(Long entityId);
    

    /**
     * Replace CMC-II
     * 
     * @param entityId
     * @param cmcId
     * @param cmcIndex
     * @param cmcMac
     * @param forceReplace
     */
    void replaceCmc(Long entityId, Long cmcId, Long cmcIndex, String cmcMac, Integer forceReplace);

    /**
     * Replace CMC-I
     * 
     * @param cmcId
     * @param replace_cmcId
     * @param configFilePath
     */
    String replaceCmc(Long cmcId, Long replace_cmcId, String configFilePath);

    /**
     * Load Config File
     * 
     * @param cmcId
     * @return
     */
    Map<String, Object> loadConfigFile(Long cmcId);

    /**
     * Load Cmc ReplaceList
     * 
     * @return
     */
    List<CmcReplaceInfo> loadCmcReplaceList(Long cmcId);
    

}
