/***********************************************************************
 * $Id: DeviceSwitchTrapExecute.java,v1.0 2017年1月9日 上午10:34:11 $
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
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2017年1月9日-上午10:34:11
 *
 */
@Service("DeviceSwitchTrapExecute")
@EventExecute(code = "event.code.{EponCode.DEVICE_SWITCH}")
public class DeviceSwitchTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private OltService oltService;
    @Autowired
    private OltSlotDao oltSlotDao;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        // 说明：bAttribute为active(1)表示主用板卡，为standby(2)表示备用板卡，为standalone(3)表示独立工作模式
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 通过数据库获取主用板卡 获得slotIndex
        List<OltSlotAttribute> oltSlotList = oltSlotDao.getOltSlotList(entityId);
        Long masterSlotIndex = 0L;
        Long masterSlotNo = 0L;
        Long masterSlotId = 0L;
        Long slaveSlotIndex = 0L;
        Long slaveSlotNo = 0L;
        Long slaveSlotId = 0L;
        for (OltSlotAttribute sa : oltSlotList) {
            if (sa.getSlotIndex().equals(EponIndex.getSlotIndex(EponConstants.MASTERINDEX.intValue()))) {
                masterSlotIndex = sa.getSlotIndex();
                masterSlotNo = sa.getSlotNo();
                masterSlotId = sa.getSlotId();
            }
            if (sa.getSlotIndex().equals(EponIndex.getSlotIndex(EponConstants.SLAVEINDEX.intValue()))) {
                slaveSlotIndex = sa.getSlotIndex();
                slaveSlotNo = sa.getSlotNo();
                slaveSlotId = sa.getSlotId();
            }
        }

        OltSlotAttribute masterSlotAttribute = new OltSlotAttribute();
        masterSlotAttribute.setSlotNo(masterSlotNo);

        OltSlotAttribute slaveSlotAttribute = new OltSlotAttribute();
        slaveSlotAttribute.setSlotNo(slaveSlotNo);

        //更新主备板卡属性
        try {
            masterSlotAttribute = oltService.getOltSlotAttribute(entityId, snmpParam, masterSlotAttribute,
                    masterSlotId, slaveSlotId);
            oltSlotDao.updateSlotAttribute(entityId, masterSlotAttribute);

            slaveSlotAttribute = oltService.getOltSlotAttribute(entityId, snmpParam, slaveSlotAttribute, masterSlotId,
                    slaveSlotId);
            oltSlotDao.updateSlotAttribute(entityId, slaveSlotAttribute);
        } catch (Exception e) {
            logger.info("SWITCH event update slotAttribute error ", e.getMessage());
        }

        OltSlotStatus masterSlotStatus = new OltSlotStatus();
        masterSlotStatus.setSlotNo(masterSlotNo);

        OltSlotStatus slaveSlotStatus = new OltSlotStatus();
        slaveSlotStatus.setSlotNo(slaveSlotNo);

        //更新主备板卡状态
        try {
            masterSlotStatus = oltService.getOltSlotStatus(entityId, snmpParam, masterSlotStatus, masterSlotId,
                    slaveSlotId);
            oltSlotDao.updateSlotStatusBySlotIndex(entityId, masterSlotStatus);

            slaveSlotStatus = oltService.getOltSlotStatus(entityId, snmpParam, slaveSlotStatus, masterSlotId,
                    slaveSlotId);
            oltSlotDao.updateSlotStatusBySlotIndex(entityId, slaveSlotStatus);
        } catch (Exception e) {
            logger.info("SWITCH event update slotStatus error ", e.getMessage());
        }

    }
}
