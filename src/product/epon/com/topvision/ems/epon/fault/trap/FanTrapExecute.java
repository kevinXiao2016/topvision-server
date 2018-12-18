/***********************************************************************
 * $Id: FanTrapExecute.java,v1.0 2017年1月9日 上午10:58:07 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author lizongtian
 * @created @2017年1月9日-上午10:58:07
 *
 */
@Service("FanTrapExecute")
@EventExecute(code = "event.code.{EponCode.BD_FAN_FAIL},event.code.{EponCode.BD_FAN_INSERT},event.code.{EponCode.BD_FAN_NORMAL},event.code.{EponCode.BD_FAN_REMOVE}")
public class FanTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSlotDao oltSlotDao;
    @Autowired
    private FacadeFactory facadeFactory;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            Thread.sleep(15000);
            List<OltFanAttribute> fanAttributes = facadeFactory.getFacade(evt.getTrap().getAddress(), OltFacade.class)
                    .getDomainInfoList(snmpParam, OltFanAttribute.class);
            for (OltFanAttribute fanAttribute : fanAttributes) {
                fanAttribute.setEntityId(entityId);
            }
            List<OltFanStatus> fanStatusList = facadeFactory.getFacade(evt.getTrap().getAddress(), OltFacade.class)
                    .getDomainInfoList(snmpParam, OltFanStatus.class);
            for (OltFanStatus fanStatus : fanStatusList) {
                fanStatus.setEntityId(entityId);
            }
            oltSlotDao.updateOltFan(entityId, fanAttributes, fanStatusList);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }
}
