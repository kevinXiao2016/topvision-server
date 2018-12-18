/***********************************************************************
 * $Id: CmcSnapServiceImpl.java,v1.0 2014年9月23日 上午9:56:33 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.facade.CmcBfsxFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcBfsxSnapInfo;
import com.topvision.ems.network.service.BfsxEntitySnapService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2014年9月23日-上午9:56:33
 *
 */
@Service("cmcSnapService")
public class CmcSnapServiceImpl extends BaseService implements BfsxEntitySnapService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CmcDao cmcDao;

    @Override
    public void refreshSnapInfo(Long entityId, Long typeId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        CmcBfsxFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), CmcBfsxFacade.class);
        List<CmcBfsxSnapInfo> list = facade.getCmcBfsxSnapInfo(snmpParam);
        String sysUptime = facade.getCmcSysUpTime(snmpParam);
        for (CmcBfsxSnapInfo info : list) {
            info.setEntityId(entityId);
            //更新cmc基本信息
            cmcDao.updateCmcBaseInfo(info);
            entityService.updateEntityDeviceName(entityId, info.getCmcSysName());
            //更新cmc snap信息
            EntityValueEvent event = new EntityValueEvent(entityId);
            event.setEntityId(entityId);
            event.setCpu(info.getCmcSysCPURatio() / 100d);
            event.setMem(info.getCmcSysRAMRatio() / 100d);
            event.setDisk(info.getCmcSysFlashRatio() / 100d);
            if (sysUptime != null || "".equals(sysUptime)) {
                event.setSysUpTime(sysUptime);
            }
            event.setActionName("performanceChanged");
            event.setListener(EntityValueListener.class);
            messageService.addMessage(event);
        }
    }
}
