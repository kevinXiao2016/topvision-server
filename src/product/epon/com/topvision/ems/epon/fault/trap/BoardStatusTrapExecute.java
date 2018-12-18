/***********************************************************************
 * $Id: BoardInsertTrapExecute.java,v1.0 2017年1月9日 上午10:28:21 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author lizongtian
 * @created @2017年1月9日-上午10:28:21
 * 处理板卡插入、板卡类型不匹配、软件版本不匹配、槽类型错误四种事件
 */
@Service("BoardStatusTrapExecute")
@EventExecute(code = "event.code.{EponCode.BD_INSERT},event.code.{EponCode.BD_TYPE_MISMATCH},event.code.{EponCode.BD_SW_MISMATCH},event.code.{EponCode.BD_SLOT_MISMATCH}")
public class BoardStatusTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSlotDao oltSlotDao;
    @Autowired
    private FacadeFactory facadeFactory;

    @Override
    public void execute(TrapEvent evt) {
        //业务处理逻辑
        Long entityId = evt.getEntityId();
        String source = evt.getSource();
        Long slotIndex = EponIndex.getSlotIndex(Integer.parseInt(source.split(":")[1], 16));
        OltSlotAttribute slotAttribute = new OltSlotAttribute();
        slotAttribute.setDeviceNo(1L);
        slotAttribute.setSlotIndex(slotIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        slotAttribute = facadeFactory.getFacade(evt.getTrap().getAddress(), OltFacade.class).getDomainInfoLine(
                snmpParam, slotAttribute);
        oltSlotDao.updateSlotAttribute(entityId, slotAttribute);
        OltSlotStatus slotStatus = new OltSlotStatus();
        slotStatus.setDeviceNo(1L);
        slotStatus.setSlotIndex(slotIndex);
        slotStatus = facadeFactory.getFacade(evt.getTrap().getAddress(), OltFacade.class).getDomainInfoLine(snmpParam,
                slotStatus);
        oltSlotDao.updateSlotStatusBySlotIndex(entityId, slotStatus);
    }
}
