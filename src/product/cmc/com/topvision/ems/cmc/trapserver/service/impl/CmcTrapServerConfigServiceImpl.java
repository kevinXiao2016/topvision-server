/***********************************************************************
 * $Id: TrapServerConfigServiceImpl.java,v1.0 2013-4-23 下午2:39:11 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.trapserver.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.trapserver.dao.CmcTrapServerConfigDao;
import com.topvision.ems.cmc.trapserver.facade.TrapServerConfigFacade;
import com.topvision.ems.cmc.trapserver.facade.domain.CmcTrapServer;
import com.topvision.ems.cmc.trapserver.service.CmcTrapServerConfigService;
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
 * @created @2013-4-23-下午2:39:11
 * 
 */
@Service("cmcTrapServerConfigService")
public class CmcTrapServerConfigServiceImpl extends BaseService implements CmcTrapServerConfigService,
        CmcSynchronizedListener {
    @Resource(name = "cmcTrapServerConfigDao")
    private CmcTrapServerConfigDao cmcTrapServerConfigDao;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;

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

    @Override
    public List<CmcTrapServer> getAllTrapServer(Long entityId) {
        return cmcTrapServerConfigDao.getAllTrapServer(entityId);
    }

    @Override
    public void addTrapServer(CmcTrapServer trapServer) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(trapServer.getEntityId());
        getTrapServerConfigFacade(snmpParam.getIpAddress()).addTrapServerToFacility(snmpParam, trapServer);
        // 添加成功后，插入数据库
        cmcTrapServerConfigDao.insertTrapServer(trapServer);
    }

    @Override
    public void deleteTrapServer(CmcTrapServer trapServer) {
        // 删除设备上TrapServer配置
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(trapServer.getEntityId());
        this.getTrapServerConfigFacade(snmpParam.getIpAddress()).deleteTrapServerFromFacility(snmpParam, trapServer);
        // 删除成功后，删除数据库中保存数据
        cmcTrapServerConfigDao.deleteTrapServer(trapServer);
    }

    @Override
    public void refreshTrapServerFromFacility(Long entityId) {
        // 从设备获取Trap配置信息
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<CmcTrapServer> trapServerList = getTrapServerConfigFacade(snmpParam.getIpAddress())
                .getTrapServerFromFacility(snmpParam);
        if (trapServerList != null) {
            for (CmcTrapServer facilityTrapServer : trapServerList) {
                facilityTrapServer.setEntityId(snmpParam.getEntityId());
            }
            cmcTrapServerConfigDao.batchInsertTrapServer(snmpParam.getEntityId(), trapServerList);
        }

    }

    private TrapServerConfigFacade getTrapServerConfigFacade(String ip) {
        return facadeFactory.getFacade(ip, TrapServerConfigFacade.class);
    }

    public CmcTrapServerConfigDao getCmcTrapServerConfigDao() {
        return cmcTrapServerConfigDao;
    }

    public void setCmcTrapServerConfigDao(CmcTrapServerConfigDao cmcTrapServerConfigDao) {
        this.cmcTrapServerConfigDao = cmcTrapServerConfigDao;
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

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            long entityId = event.getEntityId();
            try {
                refreshTrapServerFromFacility(entityId);
                logger.info("refreshTrapServerFromFacility finish");
            } catch (Exception e) {
                logger.error("refreshTrapServerFromFacility wrong", e);
            }
        }
    }

}
