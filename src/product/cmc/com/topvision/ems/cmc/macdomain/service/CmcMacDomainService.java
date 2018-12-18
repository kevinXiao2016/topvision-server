/***********************************************************************
 * $Id: CmcMacDomainService.java,v1.0 2012-2-13 下午02:16:57 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.macdomain.service;

import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainBaseInfo;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainStatusInfo;
import com.topvision.framework.service.Service;

/**
 * MAC Domain功能
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午02:16:57
 * 
 */
public interface CmcMacDomainService extends Service {
    /**
     * 获取mac域基本信息
     * 
     * @param cmcId
     *            CMC ID
     * @return MacDomainBaseInfo mac域基本信息
     */
    MacDomainBaseInfo getMacDomainBaseInfo(Long cmcId);

    /**
     * 获取mac域状态信息
     * 
     * @param cmcId
     *            CMC ID
     * @return MacDomainStatusInfo mac域状态信息
     */
    MacDomainStatusInfo getMacDomainStatusInfo(Long cmcId);

    /**
     * 修改mac域基本信息
     * 
     * @param macDomainBaseInfo
     *            修改信息
     */
    void modifyMacDomainBaseInfo(MacDomainBaseInfo macDomainBaseInfo);

    /**
     * 刷新mac域信息
     * 
     * @param cmcId Long
     */
    void refreshMacDomainInfo(Long cmcId);
}
