/***********************************************************************
 * $Id: CmcClearCmOnTimeServiceImpl.java,v1.0 2017年5月20日 下午4:54:22 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.domain.CmcClearCmOnTime;
import com.topvision.ems.cmc.ccmts.service.CmcClearCmOnTimeService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author ls
 * @created @2017年5月20日-下午4:54:22
 *
 */
@Service("cmcClearCmOnTimeService")
public class CmcClearCmOnTimeServiceImpl extends CmcBaseCommonService implements CmcClearCmOnTimeService,CmcSynchronizedListener {

    @Autowired
    private CmcDao cmcDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);

    }

    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }
    
    @Override
    public int getCmcClearTime(Long cmcId) {
        Integer result=cmcDao.getCmcClearTime(cmcId);
        if(result!=null){
            return result;
        }
        return 0;
    }

    @Override
    public void setClearTimeOnCC(Integer time, Long DeviceId) {
//        Long entityId = cmcDao.getEntityIdByCmcId(DeviceId);
        snmpParam = getSnmpParamByEntityId(DeviceId);
        getCcmtsCmListFacade(snmpParam.getIpAddress()).clearOnTimeCmOfCC(snmpParam, time);
        cmcDao.updateCmcClearTime(time, DeviceId);
    }

    @Override
    public void saveTimeOfCC(Integer time, Long cmcId) {
        cmcDao.updateCmcClearTime(time, cmcId);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        Long entityId=event.getEntityId();
        refreshCmcClearTime(entityId);
    }
    
    public void refreshCmcClearTime(Long entityId){
        Long cmcId=cmcDao.getCmcIdByEntityId(entityId);
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        CmcClearCmOnTime cmcClearCmOnTime = getCcmtsCmListFacade(snmpParam.getIpAddress()).getCmcClearTime(snmpParam);
        cmcDao.updateCmcClearTime(cmcClearCmOnTime.getCmcClearTime(),cmcId);
    }

}
