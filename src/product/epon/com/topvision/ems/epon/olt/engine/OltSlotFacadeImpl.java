/***********************************************************************
 * $Id: OltControlFacadeImpl.java,v1.0 2013-10-25 上午10:48:55 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotMapTable;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.facade.OltSlotFacade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午10:48:55
 *
 */
public class OltSlotFacadeImpl extends EmsFacade implements OltSlotFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OltSlotAttribute getMpuSlotInfo(SnmpParam snmpParam, Long slotIndex, Long slotNo) {
        OltSlotAttribute slot = new OltSlotAttribute();
        slot.setSlotIndex(slotIndex);
        slot.setSlotNo(slotNo);
        return snmpExecutorService.getTableLine(snmpParam, slot);
    }

    @Override
    public Integer getSlotPreConfigType(SnmpParam snmpParam, Long slotIndex) {
        OltSlotAttribute slot = new OltSlotAttribute();
        slot.setSlotIndex(slotIndex);
        snmpExecutorService.getTableLine(snmpParam, slot);
        return slot.getTopSysBdPreConfigType();
    }

    @Override
    public Integer setSlotPreConfigType(SnmpParam snmpParam, Long slotIndex, Integer preConfigType) {
        OltSlotAttribute oltSlotAttribute = new OltSlotAttribute();
        oltSlotAttribute.setSlotIndex(slotIndex);
        oltSlotAttribute.setTopSysBdPreConfigType(preConfigType);
        oltSlotAttribute = snmpExecutorService.setData(snmpParam, oltSlotAttribute);
        return oltSlotAttribute.getTopSysBdPreConfigType();
    }

    @Override
    public void resetOltBoard(SnmpParam snmpParam, Long slotNo) {
        OltSlotAttribute oltSlotAttribute = new OltSlotAttribute();
        oltSlotAttribute.setSlotNo(slotNo);
        oltSlotAttribute.setTopSysBdReset(1);
        oltSlotAttribute = snmpExecutorService.setData(snmpParam, oltSlotAttribute);
    }

    @Override
    public Integer setOltFanSpeedControl(SnmpParam snmpParam, Long fanCardIndex, Integer fanSpeedLevel) {
        OltFanAttribute oltFanAttribute = new OltFanAttribute();
        oltFanAttribute.setFanCardIndex(fanCardIndex);
        oltFanAttribute.setTopSysFanSpeedControl(fanSpeedLevel);
        oltFanAttribute = snmpExecutorService.setData(snmpParam, oltFanAttribute);
        return oltFanAttribute.getTopSysFanSpeedControl();
    }

    @Override
    public Integer setSlotBdTempDetectEnable(SnmpParam snmpParam, Long slotNo, Integer topSysBdTempDetectEnable) {
        OltSlotAttribute oltSlotAttribute = new OltSlotAttribute();
        oltSlotAttribute.setSlotNo(slotNo);
        oltSlotAttribute.setTopSysBdTempDetectEnable(topSysBdTempDetectEnable);
        oltSlotAttribute = snmpExecutorService.setData(snmpParam, oltSlotAttribute);
        return oltSlotAttribute.getTopSysBdTempDetectEnable();
    }

    @Override
    public Integer getBdTemperature(SnmpParam snmpParam, Long slotNo) {
        OltSlotStatus slot = new OltSlotStatus();
        slot.setSlotNo(slotNo);
        OltSlotStatus tmpSlot = snmpExecutorService.getTableLine(snmpParam, slot);
        return tmpSlot.getTopSysBdCurrentTemperature();
    }

    @Override
    public OltFanStatus getOltFanStatus(SnmpParam snmpParam, Long fanIndex) {
        OltFanStatus fan = new OltFanStatus();
        fan.setFanCardIndex(fanIndex);
        OltFanStatus newFan = snmpExecutorService.getTableLine(snmpParam, fan);
        return newFan;
    }

    @Override
    public Integer setSlotBdAdminStatus(SnmpParam snmpParam, Long slotIndex, Integer boardAdminStatus) {
        OltSlotAttribute oltSlotAttribute = new OltSlotAttribute();
        oltSlotAttribute.setSlotIndex(slotIndex);
        oltSlotAttribute.setbAdminStatus(boardAdminStatus);
        oltSlotAttribute = snmpExecutorService.setData(snmpParam, oltSlotAttribute);
        return oltSlotAttribute.getbAdminStatus();
    }

    @Override
    public List<OltSlotMapTable> getOltSlotMapTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSlotMapTable.class);
    }

    @Override
    public Integer getBdLampStatus(SnmpParam snmpParam, Long slotIndex) {
        OltSlotStatus slot = new OltSlotStatus();
        slot.setSlotIndex(slotIndex);
        OltSlotStatus lampSlot = snmpExecutorService.getTableLine(snmpParam, slot);
        return lampSlot.getTopSysBdLampStatus();
    }

    @Override
    public OltSlotStatus getOltSlotStatus(SnmpParam snmpParam, Long slotIndex) {
        OltSlotStatus slot = new OltSlotStatus();
        slot.setSlotIndex(slotIndex);
        return snmpExecutorService.getTableLine(snmpParam, slot);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
