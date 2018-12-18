/***********************************************************************
 * $ CmcBaseCommonService.java,v1.0 2012-11-13 15:18:03 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.cm.facade.CcmtsCmListFacade;
import com.topvision.ems.cmc.cm.facade.CmFacade;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.frequencyhopping.facade.FrequencyHoppingFacade;
import com.topvision.ems.cmc.loadbalance.facade.CmcLoadBalanceFacade;
import com.topvision.ems.cmc.remotequerycm.facade.CmRemoteQueryFacade;
import com.topvision.ems.cmc.spectrum.facade.SpectrumFacade;
import com.topvision.ems.facade.PingFacade;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author jay
 * @created @2012-11-13-15:18:03
 */
@Service("cmcBaseCommonService")
public class CmcBaseCommonService extends BaseService {
    protected SnmpParam snmpParam = new SnmpParam();
    @Resource(name = "cmcDao")
    protected CmcDao cmcDao;
    @Resource(name = "entityDao")
    protected EntityDao entityDao;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;

    public Long getEntityIdByCmcId(Long cmcId) {
        return cmcDao.getEntityIdByCmcId(cmcId);
    }

    public SnmpParam getSnmpParamByCmcId(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        return entityDao.getSnmpParamByEntityId(entityId);
    }

    public SnmpParam getSnmpParamByEntityId(Long entityId) {
        return entityDao.getSnmpParamByEntityId(entityId);
    }

    public CmcFacade getCmcFacade(String ip) {
        return facadeFactory.getFacade(ip, CmcFacade.class);
    }

    public CmcLoadBalanceFacade getCmcLoadBalanceFacade(String ip) {
        return facadeFactory.getFacade(ip, CmcLoadBalanceFacade.class);
    }

    public <T> T getFacade(String ip, Class<T> clazz) {
        return facadeFactory.getFacade(ip, clazz);
    }

    public FrequencyHoppingFacade getFrequencyHoppingFacade(String ip) {
        return facadeFactory.getFacade(ip, FrequencyHoppingFacade.class);
    }

    public SpectrumFacade getSpectrumFacade(String ip) {
        return facadeFactory.getFacade(ip, SpectrumFacade.class);
    }

    public CmFacade getCmFacade(String ip) {
        return facadeFactory.getFacade(ip, CmFacade.class);
    }

    public CcmtsCmListFacade getCcmtsCmListFacade(String ip) {
        return facadeFactory.getFacade(ip, CcmtsCmListFacade.class);
    }

    public PingFacade getPingFacade(String ip) {
        return facadeFactory.getFacade(ip, PingFacade.class);
    }

    public CmRemoteQueryFacade getCmRemoteQueryFacade(String ip) {
        return facadeFactory.getFacade(ip, CmRemoteQueryFacade.class);
    }

    /**
     * 国际化
     * 
     * @param key
     *            key
     * @return String
     */
    public String getString(String key) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.cmc.resources").getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public CmcDao getCmcDao() {
        return cmcDao;
    }

    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    public EntityDao getEntityDao() {
        return entityDao;
    }

    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }
}
