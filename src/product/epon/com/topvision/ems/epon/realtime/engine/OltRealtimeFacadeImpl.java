/***********************************************************************
 * $Id: OltRealtimeFacadeImpl.java,v1.0 2014-7-12 上午9:53:57 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.realtime.domain.*;
import com.topvision.ems.epon.realtime.facade.OltRealtimeFacade;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2014-7-12-上午9:53:57
 *
 */
public class OltRealtimeFacadeImpl extends EmsFacade implements OltRealtimeFacade {
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public OltBaseInfo getOltBaseInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, OltBaseInfo.class);
    }

    @Override
    public List<OltSlotInfo> getOltSoltInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSlotInfo.class);
    }

    @Override
    public List<OltPonTotalInfo> getPonTotalInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltPonTotalInfo.class);
    }

    @Override
    public List<OltSniInfo> getOltSniInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSniInfo.class);
    }

    @Override
    public List<OltSubTotalInfo> getSubTotalInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSubTotalInfo.class);
    }

    @Override
    public List<OltGponSubTotalInfo> getGponSubTotalInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltGponSubTotalInfo.class);
    }

    @Override
    public List<OltPonInfo> getOltPonInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltPonInfo.class);
    }

    @Override
    public List<OltSubDeviceInfo> getOltSubInfo(SnmpParam snmpParam) {
        snmpParam.setTimeout(60000);
        return snmpExecutorService.getTable(snmpParam, OltSubDeviceInfo.class);
    }

    @Override
    public List<OltSubDeviceGponInfo> getOltSubGponInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSubDeviceGponInfo.class);
    }

    @Override
    public List<OltSubDeviceEponInfo> getOltSubEponInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSubDeviceEponInfo.class);
    }

    @Override
    public OltCmTotalInfo getCmTotalInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, OltCmTotalInfo.class);
    }

    @Override
    public OltPortSpeedInfo getPortSpeedInfo(SnmpParam snmpParam, OltPortSpeedInfo speedInfo) {
        return snmpExecutorService.getTableLine(snmpParam, speedInfo);
    }

    @Override
    public List<OltPortSpeedInfo> getSpeedInfoList(SnmpParam snmpParam, List<OltPortSpeedInfo> speedInfoList) {
        return snmpExecutorService.getTableLine(snmpParam, speedInfoList);
    }

    @Override
    public List<ChannelCmNumInfo> getChannelCmNum(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, ChannelCmNumInfo.class);
    }

    @Override
    public List<OltCurrentCmcInfo> getOltCurrentCmcInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltCurrentCmcInfo.class);
    }

    @Override
    public List<OltSubOpticalInfo> getOltSubOpticalInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSubOpticalInfo.class);
    }

    @Override
    public DeviceBaseInfo getDeviceBaseInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, DeviceBaseInfo.class);
    }

}
