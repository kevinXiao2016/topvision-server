/***********************************************************************
 * $Id: CmcDocsisConfigServiceImpl.java,v1.0 2013-4-26 下午8:56:02 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.docsis.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.docsis.dao.CmcDocsisConfigDao;
import com.topvision.ems.cmc.docsis.facade.CmcDocsisFacade;
import com.topvision.ems.cmc.docsis.facade.domain.CmcDocsisConfig;
import com.topvision.ems.cmc.docsis.service.CmcDocsisConfigService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-4-26-下午8:56:02
 *
 */
@Service("cmcDocsisConfigService")
public class CmcDocsisConfigServiceImpl extends BaseService implements CmcDocsisConfigService, CmcSynchronizedListener {
    @Resource(name = "cmcDocsisConfigDao")
    private CmcDocsisConfigDao cmcDocsisConfigDao;
    @Resource(name = "cmcDao")
    private CmcDao cmcDao;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcDocsisConfigService#updateCmcDocsis(com.topvision.ems.cmc
     * .facade.domain.CmcSyslogConfig)
     */
    @Override
    public void updateCmcDocsis(CmcDocsisConfig cmcDocsis) {
        // 将配置更新到设备上
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmcDocsis.getEntityId());
        CmcDocsisConfig newDocsis = new CmcDocsisConfig();
        newDocsis = this.getCmcDocsisFacade(snmpParam.getIpAddress()).updateDocsisToFacility(snmpParam, cmcDocsis);
        // 更新成功后更新数据库
        cmcDocsisConfigDao.updateCmcDocsisConfig(newDocsis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcDocsisConfigService#getCmcDocsis(java.lang.Long)
     */
    @Override
    public CmcDocsisConfig getCmcDocsis(Long entityId, Long ifIndex) {
        return cmcDocsisConfigDao.getCmcDocsisConfig(entityId, ifIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcDocsisConfigService#refreshCmcDocsisFromFacility(com.topvision
     * .ems.cmc.facade.domain.CmcDocsisConfig)
     */
    @Override
    public void refreshCmcDocsisFromFacility(CmcDocsisConfig cmcDocsis) {
        // 从设备上获取数据
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmcDocsis.getEntityId());
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcDocsis.getCmcId().equals(0L) ? cmcDocsis.getEntityId() : cmcDocsis
                .getCmcId());
        CmcDocsisConfig facilityDocsis = this.getCmcDocsisFacade(snmpParam.getIpAddress()).getDocsisFromFacility(
                snmpParam, cmcIndex);
        if (facilityDocsis != null) {
            if (entityTypeService.isCcmtsWithoutAgent(new Long(cmcDocsis.getCmcType()))) {
                facilityDocsis.setCmcId(cmcDocsis.getCmcId());
            } else {
                facilityDocsis.setCmcId(cmcDocsis.getEntityId());
                facilityDocsis.setEntityId(cmcDocsis.getEntityId());
            }
            cmcDocsisConfigDao.updateCmcDocsisConfig(facilityDocsis);
        }
    }

    private CmcDocsisFacade getCmcDocsisFacade(String ip) {
        return facadeFactory.getFacade(ip, CmcDocsisFacade.class);
    }

    public CmcDocsisConfigDao getCmcDocsisConfigDao() {
        return cmcDocsisConfigDao;
    }

    public void setCmcDocsisConfigDao(CmcDocsisConfigDao cmcDocsisConfigDao) {
        this.cmcDocsisConfigDao = cmcDocsisConfigDao;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public CmcDao getCmcDao() {
        return cmcDao;
    }

    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())
                || event.getEntityType().equals(entityTypeService.getCcmtswithoutagentType())) {
            try {
                long entityId = event.getEntityId();
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                List<CmcDocsisConfig> list = getCmcDocsisFacade(snmpParam.getIpAddress())
                        .getDocsisconfigList(snmpParam);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != null) {
                            list.get(i).setEntityId(entityId);
                            CmcDocsisConfig cmcDocsisConfig = cmcDocsisConfigDao.getCmcDocsisConfig(entityId,
                                    list.get(i).getIfIndex());
                            if (cmcDocsisConfig != null) {
                                cmcDocsisConfigDao.updateCmcDocsisConfig(list.get(i));
                            } else {
                                cmcDocsisConfigDao.insertCmcDocsisConfig(list.get(i));
                            }
                        }
                    }
                }
                logger.info("Refresh CmcDocsisConfig finish. ");
            } catch (Exception e) {
                logger.error("Refresh CmcDocsisConfig Wrong. ", e);
            }
        }
    }
}
