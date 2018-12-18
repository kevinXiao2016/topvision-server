/***********************************************************************
 * $Id: OnuCatvConfigServiceImpl.java,v1.0 2016-4-27 上午9:44:27 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuCatvDao;
import com.topvision.ems.epon.onu.domain.OnuCatvConfig;
import com.topvision.ems.epon.onu.domain.OnuCatvInfo;
import com.topvision.ems.epon.onu.facade.OnuCatvFacade;
import com.topvision.ems.epon.onu.service.OnuCatvService;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author haojie
 * @created @2016-4-27-上午9:44:27
 *
 */
@Service
public class OnuCatvServiceImpl extends BaseService implements OnuCatvService, OnuSynchronizedListener {
    @Autowired
    private OnuCatvDao onuCatvDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    protected MessageService messageService;
    public static final Integer ONU_SINGLE_TOPO = 1;

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexlList = event.getOnuIndexList();
        if (onuIndexlList.size() == ONU_SINGLE_TOPO) {
            try {
                OnuCatvConfig onuCatvConfig = new OnuCatvConfig();
                onuCatvConfig.setEntityId(entityId);
                onuCatvConfig.setOnuIndex(onuIndexlList.get(0));
                refreshOnuCatvConfig(onuCatvConfig);
            } catch (Exception e) {
                logger.debug("sigle refreshOnuCatvConfig error!", e);
            }
        } else if (onuIndexlList.size() > ONU_SINGLE_TOPO) {
            try {
                refreshOnuCatvConfigAll(entityId);
            } catch (Exception e) {
                logger.debug("batch refreshOnuCatvConfig error!", e);
            }
        }
    }

    @Override
    public OnuCatvConfig getOnuCatvConfig(Long onuId) {
        return onuCatvDao.getOnuCatvConfig(onuId);
    }

    @Override
    public void modifyOnuCatvConfig(OnuCatvConfig onuCatvConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuCatvConfig.getEntityId());
        OnuCatvFacade onuCatvFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), OnuCatvFacade.class);
        onuCatvFacade.modifyOnuCatvConfig(snmpParam, onuCatvConfig);
        onuCatvDao.insertOrUpdateOnuCatvConfig(onuCatvConfig);
    }

    @Override
    public void refreshOnuCatvConfig(OnuCatvConfig onuCatvConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuCatvConfig.getEntityId());
        OnuCatvFacade onuCatvFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), OnuCatvFacade.class);
        OnuCatvConfig config = onuCatvFacade.getOnuCatvConfig(snmpParam, onuCatvConfig);
        onuCatvDao.insertOrUpdateOnuCatvConfig(config);
    }

    @Override
    public void refreshOnuCatvConfigAll(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OnuCatvFacade onuCatvFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), OnuCatvFacade.class);
        List<OnuCatvConfig> onuCatvConfigList = onuCatvFacade.getOnuCatvConfigAll(snmpParam);
        onuCatvDao.batchInsertOrUpdateOnuCatvConfig(onuCatvConfigList, entityId);
    }

    @Override
    public OnuCatvInfo getOnuCatvInfo(Long onuId) {
        return onuCatvDao.getOnuCatvInfo(onuId);
    }

}
