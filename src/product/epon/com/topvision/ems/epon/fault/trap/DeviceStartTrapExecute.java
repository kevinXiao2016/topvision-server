/***********************************************************************
 * $Id: DeviceStartTrapExecute.java,v1.0 2017年1月9日 上午10:32:20 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2017年1月9日-上午10:32:20
 *
 */
@Service("DeviceStartTrapExecute")
@EventExecute(code = "event.code.{EponCode.DEVICE_START}")
public class DeviceStartTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private SnmpExecutorService snmpExecutorService;
    @Autowired
    private DeviceVersionService deviceVersionService;

    @Override
    public void execute(TrapEvent evt) {
        // 更新OLT的状态和在线时长
        Long entityId = evt.getEntityId();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        String sysUpTime = "-1";
        try {
            Boolean supporttopsysoltuptime = deviceVersionService.isFunctionSupported(snmpParam.getEntityId(),
                    "topsysoltuptime");
            if (supporttopsysoltuptime) {
                sysUpTime = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.2.3.1.2.9.0");
            } else {
                sysUpTime = snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.1.3.0");
            }
            // 判断是不是数字字符串
            if (!StringUtils.isNumeric(sysUpTime)) {
                sysUpTime = "-1";
            }
        } catch (SnmpNoResponseException e) {
            logger.debug(e.toString());
        } catch (Exception e) {
            logger.debug("", e);
        }
        EntitySnap snap = new EntitySnap();
        snap.setEntityId(entityId);
        snap.setDelay(1);
        snap.setSysUpTime(sysUpTime);
        snap.setState(true);
        entityDao.updateEntityState(snap);
    }

}
