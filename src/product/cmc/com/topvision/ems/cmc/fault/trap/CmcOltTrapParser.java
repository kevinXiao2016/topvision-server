/***********************************************************************
 * $Id: CmcOltTrapParser.java,v1.0 2013-6-17 下午3:23:10 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.trap;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.alert.dao.CmcAlertDao;
import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.fault.domain.CmcEventSource;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventSource;
import com.topvision.ems.fault.exception.TrapInstanceException;
import com.topvision.ems.fault.parser.AbstractTrapParser;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.ResourceManager;

/**
 * 处理olt告警中需要转换为cc告警的告警
 * 
 * @author loyal
 * @created @2013-6-17-下午3:23:10
 * 
 */
@Service("cmcOltTrapParser")
public class CmcOltTrapParser extends AbstractTrapParser {
    private static final Logger logger = LoggerFactory.getLogger(CmcOltTrapParser.class);
    @Autowired
    private CmcAlertDao cmcAlertDao;
    @Autowired
    private CmcDao cmcDao;
    private static Integer cos = 500;

    @Override
    public boolean match(Long entityId, Trap trap) {
        Integer code = trap.getVarialbleInt(CmcTrapConstants.TRAP_OBJECT_CODE);
        if (code == null) {
            return false;
        }

        if (code == 16393 || code == 310) {
            // ONU断电
            String[] instance = trap.getVarialbleValue(CmcTrapConstants.TRAP_OBJECT_INSTANCE).split(":");
            Long onuIndex = CmcIndexUtils.getIndex(instance);
            // 当llid大于等于65时，为cc断电告警
            return CmcIndexUtils.getOnuNo(onuIndex) > 64;
        } else if (code == 16433 || code == 116433) {
            // ONU断纤
            String[] instance = trap.getVarialbleValue(CmcTrapConstants.TRAP_OBJECT_INSTANCE).split(":");
            Long onuIndex = CmcIndexUtils.getIndex(instance);
            // 当llid大于等于65时，为cc断纤告警
            return CmcIndexUtils.getOnuNo(onuIndex) > 64;
        }

        return false;
    }

    @Override
    public Long parseTrapCode(Trap trap) {
        return Long.valueOf(trap.getVarialbleInt(CmcTrapConstants.TRAP_OBJECT_CODE));
    }

    @Override
    public Integer convertToEmsCode(Long entityId, Trap trap, Long trapCode) {
        if (trapCode.intValue() == 16393 || trapCode.intValue() == 310) {
            return 116393;
        } else if (trapCode.intValue() == 16433 || trapCode.intValue() == 116433) {
            return 16433;
        }
        return trapCode.intValue();
    }

    @Override
    public EventSource parseEventSource(Trap trap, Long trapCode, Long entityId, Integer emsCode)
            throws TrapInstanceException {
        CmcEventSource eventSource = new CmcEventSource(entityId);

        String[] instance = trap.getVarialbleValue(CmcTrapConstants.TRAP_OBJECT_INSTANCE).split(":");
        Long onuIndex = CmcIndexUtils.getIndex(instance);
        Long cmcIndex = CmcIndexUtils.getCmcIndexFromOnuIndex(onuIndex);
        String ccMac = cmcAlertDao.getCmcMacByCmcIndexAndEntityId(cmcIndex, entityId);

        eventSource.setMac(ccMac);

        return eventSource;
    }

    @Override
    public Event buildEvent(Trap trap, Long trapCode, Integer emsCode, EventSource eventSource) {
        CmcEventSource cmcEventSource = (CmcEventSource) eventSource;
        Entity cmcEntity = cmcDao.getCmcByTrapInfo(trap.getAddress(), cmcEventSource.getMac());

        Event event = EventSender.getInstance().createEvent(emsCode, trap.getAddress(), cmcEventSource.getMac());
        event.setEntityId(cmcEntity.getEntityId());
        event.setLevelId(trap.getVarialbleByte(CmcTrapConstants.TRAP_OBJECT_SEVERITY));
        event.setOrginalCode(trapCode);
        event.setOriginMessage(trap.getVarialbleValue(CmcTrapConstants.TRAP_OBJECT_ADDITIONALTEXT));
        event.setCreateTime(
                new Timestamp(parseTrapTime(trap.getVarialbleValue(CmcTrapConstants.TRAP_OBJECT_OCCURTIME))));
        event.setUserObject(trap);
        
        String messageString = "";
        if (emsCode.equals(116393)) {
            messageString = getString("DeviceAlert.ccPowerOff", "[" + cmcEventSource.getMac() + "]",
                    cmcEntity.getName());
        } else if (emsCode.equals(16433)) {
            messageString = getString("DeviceAlert.ccOffline", "[" + cmcEventSource.getMac() + "]",
                    cmcEntity.getName());
        }
        event.setMessage(messageString);
        
        if (logger.isDebugEnabled()) {
            logger.debug("onTrap.toAlert:{}", event);
        }

        return event;
    }

    private String getString(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.cmc.resources").getNotNullString(key, strings);
        } catch (Exception e) {
            return key;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.TrapParser#getTrapCos()
     */
    @Override
    public Integer getTrapCos() {
        return cos;
    }

}
