/***********************************************************************
 * $Id: CmcAuthService.java,v1.0 2012-10-9 下午01:51:46 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.auth.service;

import com.topvision.ems.cmc.auth.facade.domain.CcmtsAuthManagement;

/**
 * @author dosion
 * @created @2012-10-9-下午01:51:46
 *
 */
public interface CmcAuthService {
    
    /**
     * 从数据库获取全局授权状态
     * @param cmcId Long
     * @return  CcmtsAuthManagement
     */
    public CcmtsAuthManagement getCcmtsAuthInfo(Long cmcId);
    
    /**
     * 从设备获取CCMTS的授权信息
     * @param cmcId Long
     * @return CcmtsAuthManagement
     */
    public CcmtsAuthManagement getCcmtsAuthInfoFromEntity(Long cmcId);
    
    /**
     * 设置授权参数
     * @param cmcId Long
     * @param authMgmt CcmtsAuthManagement
     * @return Boolean
     */
    public Boolean setCcmtsAuthInfo(Long cmcId, CcmtsAuthManagement authMgmt);
}
