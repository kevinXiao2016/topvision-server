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
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.domain.OltSniMacAddress;
import com.topvision.ems.epon.olt.domain.OltSniRedirect;
import com.topvision.ems.epon.olt.domain.OltSniStormSuppressionEntry;
import com.topvision.ems.epon.olt.facade.OltSniFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午10:48:55
 *
 */
public class OltSniFacadeImpl extends EmsFacade implements OltSniFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public String setSniName(SnmpParam snmpParam, Long sniIndex, String sniName) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setSniPortName(sniName);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute.getSniPortName();
    }

    @Override
    public Integer setSniAdminStatus(SnmpParam snmpParam, Long sniIndex, Integer status) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setSniAdminStatus(status);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute.getSniAdminStatus();
    }

    @Override
    public Integer setSniIsolationStatus(SnmpParam snmpParam, Long sniIndex, Integer status) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setSniIsolationEnable(status);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute.getSniIsolationEnable();
    }

    @Override
    public OltSniAttribute setSniFlowControl(SnmpParam snmpParam, Long sniIndex, Integer ingressRate, Integer egressRate) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setTopSniAttrIngressRate(ingressRate);
        sniAttribute.setTopSniAttrEgressRate(egressRate);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute;
    }

    @Override
    public OltSniAttribute setSniCtrlFlowEnable(SnmpParam snmpParam, Long sniIndex, Integer ctrlFlowEnable) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setTopSniAttrFlowCtrlEnable(ctrlFlowEnable);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute;
    }

    @Override
    public OltSniStormSuppressionEntry modifySniStormSuppression(SnmpParam snmpParam,
            OltSniStormSuppressionEntry oltSniStormSuppressionEntry) {
        return snmpExecutorService.setData(snmpParam, oltSniStormSuppressionEntry);
    }

    @Override
    public Integer modifySniMacAddressAgingTime(SnmpParam snmpParam, OltAttribute oltAttribute) {
        return snmpExecutorService.setData(snmpParam, oltAttribute).getSniMacAddrTableAgingTime();
    }

    @Override
    public Integer setSniAutoNegotiationMode(SnmpParam snmpParam, Long sniIndex, Integer mode) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setSniAutoNegotiationMode(mode);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute.getSniAutoNegotiationMode();
    }

    @Override
    public Long setSniMaxLearnMacNum(SnmpParam snmpParam, Long sniIndex, Long maxLearnMacNum) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setSniMacAddrLearnMaxNum(maxLearnMacNum);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute.getSniMacAddrLearnMaxNum();
    }

    @Override
    public Integer setSni15MinPerfStatus(SnmpParam snmpParam, Long sniIndex, Integer perfStatus) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setSniPerfStats15minuteEnable(perfStatus);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute.getSniPerfStats15minuteEnable();
    }

    @Override
    public Integer setSni24HourPerfStatus(SnmpParam snmpParam, Long sniIndex, Integer perfStatus) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute.setSniPerfStats24hourEnable(perfStatus);
        sniAttribute = snmpExecutorService.setData(snmpParam, sniAttribute);
        return sniAttribute.getSniPerfStats24hourEnable();
    }

    @Override
    public void modifyRedirectSni(SnmpParam snmpParam, OltSniRedirect redirect) {
        snmpExecutorService.setData(snmpParam, redirect);
    }

    @Override
    public void addRedirectSni(SnmpParam snmpParam, OltSniRedirect redirect) {
        redirect.setTopSniRedirectGroupRowstatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, redirect);
    }

    @Override
    public void deleteRedirectSni(SnmpParam snmpParam, OltSniRedirect redirect) {
        redirect.setTopSniRedirectGroupRowstatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, redirect);
    }

    @Override
    public void modifySniMacAddress(SnmpParam snmpParam, OltSniMacAddress sniMacAddress) {
        snmpExecutorService.setData(snmpParam, sniMacAddress);
    }

    @Override
    public OltSniAttribute getOltSniAttributeBySniIndex(SnmpParam snmpParam, Long sniIndex) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniIndex(sniIndex);
        sniAttribute = snmpExecutorService.getTableLine(snmpParam, sniAttribute);
        return sniAttribute;
    }

    @Override
    public List<OltSniAttribute> getSniListAttribute(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSniAttribute.class);
    }

    @Override
    public List<OltSniStormSuppressionEntry> getoltSniStormSuppressionEntries(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSniStormSuppressionEntry.class);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
