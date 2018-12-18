/***********************************************************************
 * $Id: CmcShareSecretServiceImpl.java,v1.0 2013-7-23 下午2:09:47 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sharesecret.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.sharesecret.dao.CmcShareSecretDao;
import com.topvision.ems.cmc.sharesecret.facade.domain.CmcShareSecretConfig;
import com.topvision.ems.cmc.sharesecret.service.CmcShareSecretService;

/**
 * @author dosion
 * @created @2013-7-23-下午2:09:47
 * 
 */
//@Service("cmcShareSecretService")
public class CmcShareSecretServiceImpl extends CmcBaseCommonService implements CmcShareSecretService {
    @Resource(name = "cmcShareSecretDao")
    private CmcShareSecretDao cmcShareSecretDao;

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    @Override
    public CmcShareSecretConfig getCmcShareSecretConfig(Long cmcId) {
        return cmcShareSecretDao.selectCmcShareSecretConfig(cmcId);
    }

    @Override
    public void modifyCmcShareSecretConfig(Long cmcId, CmcShareSecretConfig cmcShareSecretConfig) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        cmcShareSecretConfig.setIfIndex(cmcIndex);
        CmcShareSecretConfig shareSecret = getCmcFacade(snmpParam.getIpAddress()).updateCmcShareSecret(snmpParam,
                cmcShareSecretConfig);
        cmcShareSecretDao.updateCmcShareSecretConfig(cmcId, shareSecret);
    }

    public CmcShareSecretDao getCmcShareSecretDao() {
        return cmcShareSecretDao;
    }

    public void setCmcShareSecretDao(CmcShareSecretDao cmcShareSecretDao) {
        this.cmcShareSecretDao = cmcShareSecretDao;
    }

}
