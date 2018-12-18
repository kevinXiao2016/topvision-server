/***********************************************************************
 * $Id: CcmtsCmList.java,v1.0 2013-10-30 上午11:38:00 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.dao;

import java.util.List;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author YangYi
 * @created @2013-10-30-上午11:38:00
 * 
 */
public interface CcmtsCmListDao extends BaseEntityDao<Entity> {
    /**
     * YangYi@20130827,根据Cm的MAC地址查询CmAttribute
     * 
     * @param cmId
     * @return
     */
    public List<CmAttribute> getCmById(Long cmId);

    /**
     * 更新CM状态
     * 
     * @param cmcId
     * @param mac
     * @param status
     */
    void updateCmStatus(Long cmcId, String mac, Integer status);

    /**
     * 根据CPEIP获取cpe所在的CM
     * 
     * @param cpeIp
     * @return List<CmAttribute>
     */
    List<CmAttribute> getCmByCpeIp(String cpeIp);

    /**
     * 获取CmAttribute
     * 
     * @param cmcId
     * @param mac
     * @return
     */
    CmAttribute selectCmAttribute(Long cmcId, String mac);

    /**
     * 插入或者更新CmSignal表
     * 
     * @param map
     */
    void insertOrUpdateCmSignal(CmAttribute cmAttribute);

    public List<CmAttribute> queryCmListByCmcId(Long cmcId);

}
