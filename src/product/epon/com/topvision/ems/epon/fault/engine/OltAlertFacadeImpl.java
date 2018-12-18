/***********************************************************************
 * $Id: OltAlertFacadeImpl.java,v1.0 2013-10-26 上午10:11:50 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.engine;

import java.util.List;

import com.topvision.ems.epon.fault.domain.EponEventLog;
import com.topvision.ems.epon.fault.domain.OltTopAlarmCodeMask;
import com.topvision.ems.epon.fault.domain.OltTopAlarmInstanceMask;
import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.epon.fault.facade.OltAlertFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2013-10-26-上午10:11:50
 *
 */
public class OltAlertFacadeImpl implements OltAlertFacade {

    private SnmpExecutorService snmpExecutorService;

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public List<OltTopAlarmCodeMask> getOltAlertCodeMask(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltTopAlarmCodeMask.class);
    }

    @Override
    public OltTopAlarmCodeMask addOltAlertCodeMask(SnmpParam snmpParam, OltTopAlarmCodeMask oltTopAlarmCodeMask) {
        oltTopAlarmCodeMask.setTopAlarmCodeMaskRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, oltTopAlarmCodeMask);
    }

    @Override
    public void deleteOltAlertCodeMask(SnmpParam snmpParam, OltTopAlarmCodeMask oltTopAlarmCodeMask) {
        oltTopAlarmCodeMask.setTopAlarmCodeMaskRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, oltTopAlarmCodeMask);
    }

    @Override
    public List<OltTopAlarmInstanceMask> getOltAlertInstanceMask(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltTopAlarmInstanceMask.class);
    }

    @Override
    public OltTopAlarmInstanceMask addOltAlertInstanceMask(SnmpParam snmpParam,
            OltTopAlarmInstanceMask oltTopAlarmInstanceMask) {
        oltTopAlarmInstanceMask.setTopAlarmInstanceMaskRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, oltTopAlarmInstanceMask);
    }

    @Override
    public void deleteOltAlertInstanceMask(SnmpParam snmpParam, OltTopAlarmInstanceMask oltTopAlarmInstanceMask) {
        oltTopAlarmInstanceMask.setTopAlarmInstanceMaskRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, oltTopAlarmInstanceMask);
    }

    @Override
    public List<OltTrapConfig> getOltTrapConfig(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltTrapConfig.class);
    }

    @Override
    public OltTrapConfig addOltTrapConfig(SnmpParam snmpParam, OltTrapConfig oltTrapConfig) {
        oltTrapConfig.setEponManagementAddrRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, oltTrapConfig);
    }

    @Override
    public void deleteOltTrapConfig(SnmpParam snmpParam, OltTrapConfig oltTrapConfig) {
        oltTrapConfig.setEponManagementAddrRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, oltTrapConfig);
    }

    @Override
    public Integer getTopEponAlarmMode(SnmpParam snmpParam) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.2.2.11.5.1.0")) % 16;
    }

    @Override
    public void setTopEponAlarmMode(SnmpParam snmpParam, Integer code) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.2.2.11.5.1.0", code.toString());
    }

    @Override
    public List<EponEventLog> getEponEventLogs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, EponEventLog.class);
    }

}
