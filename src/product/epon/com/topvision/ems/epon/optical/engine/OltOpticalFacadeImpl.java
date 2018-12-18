/***********************************************************************
 * $Id: OltOpticalFacadeImpl.java,v1.0 2013-10-26 上午09:41:54 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.engine;

import java.util.List;

import com.topvision.ems.epon.optical.domain.OltOnuOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltPonOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.optical.domain.OltSysOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.ems.epon.optical.facade.OltOpticalFacade;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:41:54
 *
 */
public class OltOpticalFacadeImpl implements OltOpticalFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OltSniOptical loadOltSniOptical(SnmpParam snmpParam, Integer slotNo, Integer portNo) {
        OltSniOptical sni = new OltSniOptical();
        sni.setSlotNo(slotNo);
        sni.setPortNo(portNo);
        return snmpExecutorService.getTableLine(snmpParam, sni);
    }

    @Override
    public List<OltSniOptical> getSniOpticalList(SnmpParam snmpParam, List<OltSniOptical> sniList) {
        return snmpExecutorService.getTableLine(snmpParam, sniList);
    }

    @Override
    public List<OltPonOptical> getPonOpticalList(SnmpParam snmpParam, List<OltPonOptical> ponList) {
        return snmpExecutorService.getTableLine(snmpParam, ponList);
    }

    @Override
    public OltPonOptical loadOltPonOptical(SnmpParam snmpParam, Integer slotNo, Integer portNo) {
        OltPonOptical pon = new OltPonOptical();
        pon.setSlotNo(slotNo);
        pon.setPortNo(portNo);
        return snmpExecutorService.getTableLine(snmpParam, pon);
    }

    @Override
    public OnuPonOptical loadOnuPonOptical(SnmpParam snmpParam, Long deviceIndex) {
        OnuPonOptical onuPon = new OnuPonOptical();
        onuPon.setDeviceIndex(deviceIndex);
        onuPon.setCardNo(0);
        onuPon.setPortNo(0);
        return snmpExecutorService.getTableLine(snmpParam, onuPon);
    }

    @Override
    public List<OltSniOptical> getOltSniOptical(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSniOptical.class);
    }

    @Override
    public List<OltPonOptical> getOtlPonOptical(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltPonOptical.class);
    }

    @Override
    public OltSysOpticalAlarm modifySysOpticalAlarm(SnmpParam snmpParam, OltSysOpticalAlarm alarm) {
        return snmpExecutorService.setData(snmpParam, alarm);
    }

    @Override
    public OltPonOpticalAlarm modifyOltPonOptAlarm(SnmpParam snmpParam, OltPonOpticalAlarm thresh) {
        OltPonOpticalAlarm setData = new OltPonOpticalAlarm();
        setData.setTopPonOptAlarmIndex(thresh.getTopPonOptAlarmIndex());
        setData.setTopPonOptCardIndex(thresh.getTopPonOptCardIndex());
        setData.setTopPonOptPonIndex(thresh.getTopPonOptPonIndex());
        setData.setTopPonOptThresholdValue(thresh.getTopPonOptThresholdValue());
        try {
            snmpExecutorService.getTableLine(snmpParam, setData);
        } catch (SnmpNoSuchInstanceException e) {
            setData.setTopPonOptAlarmState(1);
            setData.setTopPonOptAlarmRowStatus(RowStatus.CREATE_AND_GO);
        }
        thresh.setTopPonOptAlarmState(1);
        return snmpExecutorService.setData(snmpParam, thresh);
    }

    @Override
    public OltPonOpticalAlarm deleteOltPonOptAlarm(SnmpParam snmpParam, OltPonOpticalAlarm thresh) {
        thresh.setTopPonOptAlarmRowStatus(RowStatus.DESTORY);
        OltPonOpticalAlarm getData = new OltPonOpticalAlarm();
        getData.setTopPonOptAlarmIndex(thresh.getTopPonOptAlarmIndex());
        getData.setTopPonOptCardIndex(thresh.getTopPonOptCardIndex());
        getData.setTopPonOptPonIndex(thresh.getTopPonOptPonIndex());
        try {
            snmpExecutorService.getTableLine(snmpParam, getData);
        } catch (SnmpNoSuchInstanceException e) {
            getData.setTopPonOptAlarmRowStatus(RowStatus.CREATE_AND_GO);
            return getData;
        }
        return snmpExecutorService.setData(snmpParam, thresh);
    }

    @Override
    public OltOnuOpticalAlarm modifyOltOnuOptAlarm(SnmpParam snmpParam, OltOnuOpticalAlarm thresh) {
        OltOnuOpticalAlarm setData = new OltOnuOpticalAlarm();
        OltOnuOpticalAlarm getData = new OltOnuOpticalAlarm();
        setData.setTopOnuOptAlarmIndex(thresh.getTopOnuOptAlarmIndex());
        setData.setTopOnuOptCardIndex(thresh.getTopOnuOptCardIndex());
        setData.setTopOnuOptPonIndex(thresh.getTopOnuOptPonIndex());
        setData.setTopOnuOptOnuIndex(thresh.getTopOnuOptOnuIndex());
        setData.setTopOnuOptThresholdValue(thresh.getTopOnuOptThresholdValue());

        getData.setTopOnuOptAlarmIndex(thresh.getTopOnuOptAlarmIndex());
        getData.setTopOnuOptCardIndex(thresh.getTopOnuOptCardIndex());
        getData.setTopOnuOptPonIndex(thresh.getTopOnuOptPonIndex());
        getData.setTopOnuOptOnuIndex(thresh.getTopOnuOptOnuIndex());
        try {
            snmpExecutorService.getTableLine(snmpParam, getData);
        } catch (SnmpNoSuchInstanceException e) {
            setData.setTopOnuOptAlarmState(1);
            setData.setTopOnuOptAlarmRowStatus(RowStatus.CREATE_AND_GO);
        }
        return snmpExecutorService.setData(snmpParam, setData);
    }

    @Override
    public OltOnuOpticalAlarm deleteOnuOptAlarm(SnmpParam snmpParam, OltOnuOpticalAlarm thresh) {
        thresh.setTopOnuOptAlarmRowStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, thresh);
    }

    @Override
    public OltOnuOpticalAlarm getOnuOptAlarm(SnmpParam snmpParam, OltOnuOpticalAlarm thresh) {
        return snmpExecutorService.getTableLine(snmpParam, thresh);
    }

    @Override
    public List<OltSysOpticalAlarm> getOltSysOpticalAlarm(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSysOpticalAlarm.class);
    }

    @Override
    public List<OltOnuOpticalAlarm> getOltOnuOpticalAlarm(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuOpticalAlarm.class);
    }

    @Override
    public List<OltPonOpticalAlarm> getOltPonOpticalAlarm(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltPonOpticalAlarm.class);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }
}
