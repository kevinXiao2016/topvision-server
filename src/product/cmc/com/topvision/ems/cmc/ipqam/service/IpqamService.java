/***********************************************************************
 * $Id: IpqamService.java,v1.0 2016年5月3日 下午2:46:44 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ipqam.domain.Ipqam;
import com.topvision.ems.cmc.ipqam.domain.Program;

/**
 * @author loyal
 * @created @2016年5月3日-下午2:46:44
 * 
 */
public interface IpqamService {
    /**
     * 获取EQAM列表
     * 
     * @param cmcId
     * @return
     */
    List<Ipqam> getEqamList(Long cmcId);

    /**
     * 获取节目流列表
     * 
     * @param queryMap
     * @return
     */
    List<Program> getProgramList(Map<String, Object> queryMap);

    /**
     * 获取节目流列表数量
     * 
     * @param cmcId
     * @return
     */
    Long getProgramListCount(Long cmcId);

    /**
     * 获取指定OLT下的节目流
     * 
     * @param entityId
     * @param queryContent
     * @return
     */
    List<Program> getOltEqamList(Map<String, Object> queryMap);

    Long getOltProgramListCount(Map<String, Object> queryMap);

    /**
     * 判断某OLT下是否存在CC支持EQAM
     * 
     * @param entityId
     * @return
     */
    boolean loadEqamSupportUnderOlt(Long entityId);

    boolean loadEqamSupport(Long entityId);
}
