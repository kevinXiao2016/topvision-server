/***********************************************************************
 * $Id: CmtsServiceImpl.java,v1.0 2013-7-20 下午04:30:31 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmts.dao.CmtsBaseDao;
import com.topvision.ems.cmts.service.CmtsBaseService;
import com.topvision.ems.cmts.topology.domain.CmtsDiscoveryData;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.DeviceTypeUtils;
import com.topvision.platform.facade.FacadeFactory;

/**
 * 基本功能实现
 * 
 * @author jay
 * @created @2013-7-20-下午04:30:31
 */
@Service("cmtsBaseService")
public class CmtsBaseServiceImpl extends BaseService implements CmtsBaseService, BeanFactoryAware {
    @Resource(name = "cmtsBaseDao")
    private CmtsBaseDao cmtsBaseDao;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    private BeanFactory beanFactory;
    @Resource(name = "entityDao")
    private EntityDao entityDao;
    @Resource(name = "cmcDao")
    private CmcDao cmcDao;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmts.service.CmtsBaseService#getCmtsSystemBasicInfoByCmcId(java.lang.Long)
     */
    public CmcAttribute getCmtsSystemBasicInfoByCmcId(Long cmcId) {
        return cmtsBaseDao.getCmtsSystemBasicInfoByCmcId(cmcId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmts.service.CmtsBaseService#refreshCmts(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public void refreshCmts(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        CmtsDiscoveryData data;
        Entity entity = entityService.getEntity(entityId);
        EntityType entityType = entityTypeService.getEntityType(entity.getTypeId());
        //EntityType parentType = entityTypeService.getEntityType(entityTypeService.getNetWorkGroupType(entity.getTypeId()).getTypeId());
        // 查找拓扑发现的实现，如果对具体型号有实现，则优先使用具体型号的拓扑发现，否则使用产品类型， 如果产品类型也没有则使用默认的实现（本程序）
        DiscoveryService<CmtsDiscoveryData> ds = null;
        try {
            String discoveryService = String.format("%sDiscoveryService", entityType.getName());
            if (!beanFactory.containsBean(discoveryService)) {
                discoveryService = entityType.getDiscoveryBean();
            }
            ds = (DiscoveryService<CmtsDiscoveryData>) beanFactory.getBean(discoveryService);
        } catch (BeansException e) {
        }
        data = ds.discovery(snmpParam);
        entity.setParam(snmpParam);
        ds.updateEntity(entity, data);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmts.service.CmtsBaseService#getCmtsSnapByEntityId(java.lang.Long)
     */
    @Override
    public EntitySnap getCmtsSnapByEntityId(Long entityId) {
        return cmtsBaseDao.selectCmtsSnapByEntityId(entityId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmts.service.CmtsBaseService#modifyCmtsBasicInfo(java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void modifyCmtsBasicInfo(Long cmcId, String name, String sysLocation, String connectPerson) {
        CmcSystemBasicInfo cmcSystemBasicInfo = new CmcSystemBasicInfo();
        DeviceBaseInfo deviceBaseInfo = new DeviceBaseInfo();
        cmcSystemBasicInfo.setCmcId(cmcId);
        cmcSystemBasicInfo.setTopCcmtsSysName(name);
        cmcSystemBasicInfo.setTopCcmtsSysContact(connectPerson);
        cmcSystemBasicInfo.setTopCcmtsSysLocation(sysLocation);

        deviceBaseInfo.setSysContact(connectPerson);
        deviceBaseInfo.setSysLocation(sysLocation);
        deviceBaseInfo.setSysName(name);

        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmcId);
        getCmcFacade(snmpParam.getIpAddress()).modifySystemBasicInfo(snmpParam, deviceBaseInfo);
        cmcDao.updateCcmtsBasicInfo(cmcSystemBasicInfo);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmts.service.CmtsBaseService#renameEntityName(java.lang.Long, java.lang.String)
     */
    @Override
    public void renameEntityName(Long entityId, String entityName) {
        entityDao.renameEntity(entityId, entityName);
    }

    public List<CmcAttribute> queryCmtsList(Map<String, Object> cmcQueryMap, int start, int limit) {
        Long cmtsType = entityTypeService.getCmtsType();
        cmcQueryMap.put("type", cmtsType);
        return cmtsBaseDao.queryCmtsList(cmcQueryMap, start, limit); //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long queryCmtsNum(Map<String, Object> cmcQueryMap) {
        Long cmtsType = entityTypeService.getCmtsType();
        cmcQueryMap.put("type", cmtsType);
        return cmtsBaseDao.queryCmtsNum(cmcQueryMap); //To change body of implemented methods use File | Settings | File Templates.
    }

    public CmcFacade getCmcFacade(String ip) {
        return facadeFactory.getFacade(ip, CmcFacade.class);
    }

    public CmtsBaseDao getCmtsBaseDao() {
        return cmtsBaseDao;
    }

    public void setCmtsBaseDao(CmtsBaseDao cmtsBaseDao) {
        this.cmtsBaseDao = cmtsBaseDao;
    }

    /**
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * @return the beanFactory
     */
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * @param beanFactory the beanFactory to set
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * @param entityDao the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @return the cmcDao
     */
    public CmcDao getCmcDao() {
        return cmcDao;
    }

    /**
     * @param cmcDao the cmcDao to set
     */
    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    /**
     * @return the facadeFactory
     */
    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    /**
     * @param facadeFactory the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    @Override
    public void analyseSofewareVersion(CmcAttribute cmc) {
        String sysDesc = cmc.getTopCcmtsSysDescr();
        boolean analyseSuccess = false;
        if (DeviceTypeUtils.isBSR2000(cmc.getCmcDeviceStyle())) {// BSR
            int startIndex = sysDesc.indexOf("SW Version:");
            int endIndex = sysDesc.indexOf(">>");
            if (startIndex > 0 && endIndex > 0) {
                String swVersion = sysDesc.substring(startIndex + 11, endIndex).trim();
                if (swVersion.length() > 0) {
                    cmc.setTopCcmtsSysSwVersion(swVersion);
                    analyseSuccess = true;
                }
            }
        } else if (DeviceTypeUtils.isCASA(cmc.getCmcDeviceStyle())) {// CASA
            int startIndex = sysDesc.indexOf("software release version");
            if (startIndex > 0) {
                String swVersion = sysDesc.substring(startIndex + 24).trim();
                if (swVersion.length() > 0) {
                    cmc.setTopCcmtsSysSwVersion(swVersion);
                    analyseSuccess = true;
                }
            }
        }
        if (!analyseSuccess) {
            cmc.setTopCcmtsSysSwVersion("-");
        }
    }

}