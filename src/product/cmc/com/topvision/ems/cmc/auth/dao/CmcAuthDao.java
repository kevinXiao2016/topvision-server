package com.topvision.ems.cmc.auth.dao;

import com.topvision.ems.cmc.auth.facade.domain.CcmtsAuthManagement;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.framework.dao.BaseEntityDao;

public interface CmcAuthDao extends BaseEntityDao<CmcEntity> {
    
    /**
     * 获取授权信息
     * @param cmcId
     * @return
     */
    public CcmtsAuthManagement getCcmtsAuthInfo(Long cmcId);

    /**
     * 通过cmcId获取CmcIndex
     * @param cmcId
     * @return
     */
    public Long getCmcIndexByCmcId(Long cmcId);

    /**
     * 更新Cmc认证信息
     * 
     * @create by huangdongsheng
     * @param authMgmt
     * @param cmcId
     */
    void updateCmcAuthInfo(CcmtsAuthManagement authMgmt, Long cmcId);
}
