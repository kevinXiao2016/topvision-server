/***********************************************************************
 * $Id: CmcMacDomainDao.java,v1.0 2012-2-13 下午04:19:00 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.macdomain.dao;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainBaseInfo;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainStatusInfo;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * MAC Domain相关功能
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午04:19:00
 * 
 */
public interface CmcMacDomainDao extends BaseEntityDao<CmcEntity> {
    /**
     * 获取mac域基本信息
     * 
     * @param cmcId
     *            cmc设备id
     * @return MacDomainBaseInfo mac域基本信息
     */
    MacDomainBaseInfo getMacDomainBaseInfo(Long cmcId);

    /**
     * 获取mac域状态信息
     * 
     * @param cmcId
     *            cmc设备id
     * @return MacDomainStatusInfo mac域状态信息
     */
    MacDomainStatusInfo getMacDomainStatusInfo(Long cmcId);

    /**
     * 修改Mac Domain基本信息
     * 
     * @param macDomainBaseInfo
     *            mac域基本信息
     */
    void updateMacDomainBaseInfo(MacDomainBaseInfo macDomainBaseInfo);

    /**
     * 插入更新mac域基本信息
     * 
     * @param macDomainStatusInfo
     */
    void insertOrUpdateMacDomainStatusInfo(MacDomainStatusInfo macDomainStatusInfo);
}
