/***********************************************************************
 * $Id: OnuSlaServiceImpl.java,v1.0 2013年10月25日 下午5:50:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.qos.dao.OnuSlaDao;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.epon.qos.facade.OnuSlaFacade;
import com.topvision.ems.epon.qos.service.OnuSlaService;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:50:37
 *
 */
@Service("onuSlaService")
public class OnuSlaServiceImpl extends BaseService implements OnuSlaService, OnuSynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OnuSlaDao onuSlaDao;

    public static final Integer ONU_SINGLE_TOPO = 1;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexList = event.getOnuIndexList();
        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                //刷新单个ONU用gettablelinde
                refreshOnuSla(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                //刷新多个onu用gettable，一般是刷新整个OLT
                refreshOnuSla(entityId);
            }
            logger.info("refreshSlaTable finished!");
        } catch (Exception e) {
            logger.error("refreshSlaTable error:", e);
        }
    }

    @Override
    public void refreshOnuSla(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OnuSlaFacade onuSlaFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), OnuSlaFacade.class);
        List<SlaTable> slaTables = onuSlaFacade.getOnuSla(snmpParam);
        if (slaTables != null) {
            for (SlaTable slaTable : slaTables) {
                slaTable.setEntityId(entityId);
            }
            onuSlaDao.saveSlaTable(slaTables);
        }
    }

    @Override
    public void refreshOnuSla(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OnuSlaFacade onuSlaFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), OnuSlaFacade.class);
        SlaTable slaTable = onuSlaFacade.getOnuSla(snmpParam, onuIndex);
        List<SlaTable> slaTables = new ArrayList<SlaTable>();
        if (slaTable != null) {
            slaTable.setEntityId(entityId);
            slaTables.add(slaTable);
        }
        onuSlaDao.saveSlaTable(slaTables);
    }

}
