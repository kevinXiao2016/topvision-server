/***********************************************************************
 * $Id: IpqamDao.java,v1.0 2016年5月7日 上午11:37:01 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ipqam.domain.Ipqam;
import com.topvision.ems.cmc.ipqam.domain.Program;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2016年5月7日-上午11:37:01
 * 
 */
public interface IpqamDao extends BaseEntityDao<Ipqam> {
    /**
     * 获取EQAM信道列表
     * 
     * @param cmcId
     * @return
     */
    List<Ipqam> selectEqamList(Long cmcId);

    /**
     * 获取节目流列表
     * 
     * @param queryMap
     * @return
     */
    List<Program> selectProgramList(Map<String, Object> queryMap);

    /**
     * 获取节目流数量
     * 
     * @param cmcId
     * @return
     */
    Long selectProgramListCount(Long cmcId);

    List<Program> selectOltEqamList(Map<String, Object> queryMap);

    Long selectOltEqamListCount(Map<String, Object> queryMap);

    /**
     * 获取指定OLT下支持EQAM设备数量
     * 
     * @param entityId
     * @return
     */
    Integer selectEqamSupportCountUnderOlt(Long entityId);

    Integer selectEqamSupport(Long entityId);
}
