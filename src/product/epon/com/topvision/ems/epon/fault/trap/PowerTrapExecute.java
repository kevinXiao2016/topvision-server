/***********************************************************************
 * $Id: PowerTrapExecute.java,v1.0 2017年1月13日 上午10:14:24 $
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
import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerStatus;
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
 * @created @2017年1月13日-上午10:14:24
 *
 */
@Service("PowerTrapExecute")
@EventExecute(code = "event.code.{EponCode.BD_PWR_FAIL},event.code.{EponCode.BD_PWR_INSERT},event.code.{EponCode.BD_PWR_OK},event.code.{EponCode.BD_PWR_REMOVE}")
public class PowerTrapExecute extends BaseService implements TrapExceutor {
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
        List<OltPowerAttribute> powerAttributes = facadeFactory.getFacade(evt.getTrap().getAddress(), OltFacade.class)
                .getDomainInfoList(snmpParam, OltPowerAttribute.class);
        for (OltPowerAttribute powerAttribute : powerAttributes) {
            powerAttribute.setEntityId(entityId);
        }
        List<OltPowerStatus> powerStatus = facadeFactory.getFacade(evt.getTrap().getAddress(), OltFacade.class)
                .getDomainInfoList(snmpParam, OltPowerStatus.class);
        for (OltPowerStatus poStatus : powerStatus) {
            poStatus.setEntityId(entityId);
        }
        oltSlotDao.updateOltPower(entityId, powerAttributes, powerStatus);
    }

}
