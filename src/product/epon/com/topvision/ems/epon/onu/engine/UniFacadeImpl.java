/***********************************************************************
 * $Id: OnuFacadeImpl.java,v1.0 2013-10-25 上午11:28:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.epon.onu.facade.UniFacade;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午11:28:54
 *
 */
public class UniFacadeImpl extends EmsFacade implements UniFacade {
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public OltUniAttribute setUniAdminStatus(SnmpParam snmpParam, OltUniAttribute oltUniAttribute) {
        oltUniAttribute = snmpExecutorService.setData(snmpParam, oltUniAttribute);
        return oltUniAttribute;
    }

    @Override
    public Integer setUniAutoNegotiationStatus(SnmpParam snmpParam, Long uniIndex, Integer status) {
        OltUniAttribute oltUniAttribute = new OltUniAttribute();
        oltUniAttribute.setUniIndex(uniIndex);
        oltUniAttribute.setUniAutoNegotiationEnable(status);
        oltUniAttribute = snmpExecutorService.setData(snmpParam, oltUniAttribute);
        return oltUniAttribute.getUniAutoNegotiationEnable();
    }

    @Override
    public Integer setUniIsolationEnable(SnmpParam snmpParam, Long onuIndex, Integer uniIsolationEnable) {
        OltUniExtAttribute oltTopUniAttribute = new OltUniExtAttribute();
        oltTopUniAttribute.setUniIndex(onuIndex);
        oltTopUniAttribute.setIsolationEnable(uniIsolationEnable);
        oltTopUniAttribute = snmpExecutorService.setData(snmpParam, oltTopUniAttribute);
        return oltTopUniAttribute.getIsolationEnable();
    }

    @Override
    public Integer setUniFlowCtrlEnable(SnmpParam snmpParam, Long onuIndex, Integer uniFlowCtrlEnable) {
        OltUniExtAttribute oltTopUniAttribute = new OltUniExtAttribute();
        oltTopUniAttribute.setUniIndex(onuIndex);
        oltTopUniAttribute.setFlowCtrl(uniFlowCtrlEnable);
        oltTopUniAttribute = snmpExecutorService.setData(snmpParam, oltTopUniAttribute);
        return oltTopUniAttribute.getFlowCtrl();
    }

    @Override
    public Integer setUni15minEnable(SnmpParam snmpParam, Long onuIndex, Integer uni15minEnable) {
        OltUniExtAttribute oltTopUniAttribute = new OltUniExtAttribute();
        oltTopUniAttribute.setUniIndex(onuIndex);
        oltTopUniAttribute.setPerfStats15minuteEnable(uni15minEnable);
        oltTopUniAttribute = snmpExecutorService.setData(snmpParam, oltTopUniAttribute);
        return oltTopUniAttribute.getPerfStats15minuteEnable();
    }

    @Override
    public Integer setUni24hEnable(SnmpParam snmpParam, Long onuIndex, Integer uni24hEnable) {
        OltUniExtAttribute oltTopUniAttribute = new OltUniExtAttribute();
        oltTopUniAttribute.setUniIndex(onuIndex);
        oltTopUniAttribute.setPerfStats24hourEnable(uni24hEnable);
        oltTopUniAttribute = snmpExecutorService.setData(snmpParam, oltTopUniAttribute);
        return oltTopUniAttribute.getPerfStats24hourEnable();
    }

    @Override
    public OltUniStormSuppressionEntry modifyUniStormInfo(SnmpParam snmpParam,
            OltUniStormSuppressionEntry oltUniStormSuppressionEntry) {
        return snmpExecutorService.setData(snmpParam, oltUniStormSuppressionEntry);
    }

    @Override
    public void restartUniAutoNego(SnmpParam snmpParam, Long uniIndex) {
        OltUniAttribute oltUniAttribute = new OltUniAttribute();
        oltUniAttribute.setUniIndex(uniIndex);
        oltUniAttribute.setUniAutoNegotiationRestart(1);
        oltUniAttribute = snmpExecutorService.setData(snmpParam, oltUniAttribute);
    }

    @Override
    public Integer setUniAutoNegoEnable(SnmpParam snmpParam, Long uniIndex, Integer uniAutoNegoEnable) {
        OltUniAttribute oltUniAttribute = new OltUniAttribute();
        oltUniAttribute.setUniIndex(uniIndex);
        oltUniAttribute.setUniAutoNegotiationEnable(uniAutoNegoEnable);
        oltUniAttribute = snmpExecutorService.setData(snmpParam, oltUniAttribute);
        return oltUniAttribute.getUniAutoNegotiationEnable();
    }

    @Override
    public Integer getUniAutoNegoStatus(SnmpParam snmpParam, Long uniIndex) {
        OltUniExtAttribute uni = new OltUniExtAttribute();
        uni.setUniIndex(uniIndex);
        snmpExecutorService.getTableLine(snmpParam, uni);
        return uni.getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger();
    }

    @Override
    public OltUniPortRateLimit modifyUniRateLimitInfo(SnmpParam snmpParam, OltUniPortRateLimit oltUniPortRateLimit) {
        return snmpExecutorService.setData(snmpParam, oltUniPortRateLimit);
    }

    @Override
    public OltUniPortRateLimit refreshUniRateLimitInfo(SnmpParam snmpParam, OltUniPortRateLimit oltUniPortRateLimit) {
        return snmpExecutorService.getTableLine(snmpParam, oltUniPortRateLimit);
    }

    @Override
    public OltUniExtAttribute modifyUniMacAddrLearnMaxNum(SnmpParam snmpParam, OltUniExtAttribute oltTopUniAttribute) {
        return snmpExecutorService.setData(snmpParam, oltTopUniAttribute);
    }

    @Override
    public OltUniExtAttribute updateOltUniAutoNegoMode(SnmpParam snmpParam, OltUniExtAttribute oltUniExtAttribute) {
        return snmpExecutorService.setData(snmpParam, oltUniExtAttribute);
    }

    @Override
    public OltUniExtAttribute updateOltUniMacAge(SnmpParam snmpParam, OltUniExtAttribute oltUniExtAttribute) {
        return snmpExecutorService.setData(snmpParam, oltUniExtAttribute);
    }

    @Override
    public void setUniMacClear(SnmpParam snmpParam, OltUniExtAttribute uniExtAttribute) {
        snmpExecutorService.setData(snmpParam, uniExtAttribute);
    }

    @Override
    public void setUniDSLoopBackEnable(SnmpParam snmpParam, OltUniExtAttribute v) {
        snmpExecutorService.setData(snmpParam, v);
    }

    @Override
    public void setUniLoopDetectEnable(SnmpParam snmpParam, OltUniExtAttribute v) {
        snmpExecutorService.setData(snmpParam, v);
    }

    @Override
    public void setUniUSUtgPri(SnmpParam snmpParam, OltUniExtAttribute v) {
        snmpExecutorService.setData(snmpParam, v);
    }

    @Override
    public List<OltUniStormSuppressionEntry> refreshOnuStormOutPacketRate(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltUniStormSuppressionEntry.class);
    }

    @Override
    public OltUniExtAttribute refreshUniUSUtgPri(SnmpParam snmpParam, OltUniExtAttribute o) {
        return snmpExecutorService.getTableLine(snmpParam, o);
    }

    @Override
    public List<OltUniExtAttribute> getUniListAttribute(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltUniExtAttribute.class);
    }

    @Override
    public OltUniExtAttribute refreshOnuUniExtInfo(SnmpParam snmpParam, OltUniExtAttribute oltUniExtAttribute) {
        return snmpExecutorService.getTableLine(snmpParam, oltUniExtAttribute);
    }

    @Override
    public OltUniAttribute refreshOnuUniAttribute(SnmpParam snmpParam, OltUniAttribute oltUniAttribute) {
        return snmpExecutorService.getTableLine(snmpParam, oltUniAttribute);
    }

    @Override
    public List<OltUniPortRateLimit> getOltUniPortRateLimits(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltUniPortRateLimit.class);
    }

    @Override
    public List<OltUniPortRateLimit> getUniPortRateLimit(SnmpParam snmpParam,
            List<OltUniPortRateLimit> oltUniPortRateLimits) {
        return snmpExecutorService.getTableLine(snmpParam, oltUniPortRateLimits);
    }

    @Override
    public UniVlanBindTable refreshUniVlanBindTable(SnmpParam snmpParam, UniVlanBindTable vlanTable) {
        return snmpExecutorService.getTableLine(snmpParam, vlanTable);
    }

    @Override
    public OltUniExtAttribute fetchOltUniMacAge(SnmpParam snmpParam, OltUniExtAttribute oltUniExtAttribute) {
        return snmpExecutorService.getTableLine(snmpParam, oltUniExtAttribute);
    }
}
