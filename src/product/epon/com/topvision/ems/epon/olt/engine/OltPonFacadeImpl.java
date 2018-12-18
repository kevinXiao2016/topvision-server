/***********************************************************************
 * $Id: OltControlFacadeImpl.java,v1.0 2013-10-25 上午10:48:55 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.engine;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPonStormSuppressionEntry;
import com.topvision.ems.epon.olt.domain.TopPonPortRateLimit;
import com.topvision.ems.epon.olt.domain.TopPonPortSpeedEntry;
import com.topvision.ems.epon.olt.facade.OltPonFacade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author flack
 * @created @2013-10-25-上午10:48:55
 *
 */
public class OltPonFacadeImpl extends EmsFacade implements OltPonFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public Integer setPonAdminStatus(SnmpParam snmpParam, Long ponIndex, Integer status) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setPonIndex(ponIndex);
        oltPonAttribute.setPonPortAdminStatus(status);
        oltPonAttribute = snmpExecutorService.setData(snmpParam, oltPonAttribute);
        return oltPonAttribute.getPonPortAdminStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.facade.OltControlFacade#setPonIsolationStatus(
     * com.topvision.framework .snmp.SnmpParam, java.lang.Long, java.lang.Integer)
     */
    @Override
    public Integer setPonIsolationStatus(SnmpParam snmpParam, Long ponIndex, Integer status) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setPonIndex(ponIndex);
        oltPonAttribute.setPonPortIsolationEnable(status);
        oltPonAttribute = snmpExecutorService.setData(snmpParam, oltPonAttribute);
        return oltPonAttribute.getPonPortIsolationEnable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.facade.OltControlFacade#setPonPortEncryptMode(
     * com.topvision.framework .snmp.SnmpParam, java.lang.Long, java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public OltPonAttribute setPonPortEncryptMode(SnmpParam snmpParam, Long ponIndex, Integer encryptMode,
            Integer exchangeTime) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setPonIndex(ponIndex);
        oltPonAttribute.setPonPortEncryptMode(encryptMode);
        oltPonAttribute.setPonPortEncryptKeyExchangeTime(exchangeTime);
        oltPonAttribute = snmpExecutorService.setData(snmpParam, oltPonAttribute);
        return oltPonAttribute;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.facade.OltControlFacade#setPonMaxLearnMacNum(com
     * .topvision.framework .snmp.SnmpParam, java.lang.Long, java.lang.Integer)
     */
    @Override
    public Long setPonMaxLearnMacNum(SnmpParam snmpParam, Long ponIndex, Long maxLearnMacNum) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setPonIndex(ponIndex);
        oltPonAttribute.setPonPortMacAddrLearnMaxNum(maxLearnMacNum);
        oltPonAttribute = snmpExecutorService.setData(snmpParam, oltPonAttribute);
        return oltPonAttribute.getPonPortMacAddrLearnMaxNum();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.facade.OltControlFacade#setPon15MinPerfStatus(
     * com.topvision.framework .snmp.SnmpParam, java.lang.Long, java.lang.Integer)
     */
    @Override
    public Integer setPon15MinPerfStatus(SnmpParam snmpParam, Long ponIndex, Integer perfStatus) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setPonIndex(ponIndex);
        oltPonAttribute.setPerfStats15minuteEnable(perfStatus);
        oltPonAttribute = snmpExecutorService.setData(snmpParam, oltPonAttribute);
        return oltPonAttribute.getPerfStats15minuteEnable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.facade.OltControlFacade#setPon24HourPerfStatus
     * (com.topvision.framework .snmp.SnmpParam, java.lang.Long, java.lang.Integer)
     */
    @Override
    public Integer setPon24HourPerfStatus(SnmpParam snmpParam, Long ponIndex, Integer perfStatus) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setPonIndex(ponIndex);
        oltPonAttribute.setPerfStats24hourEnable(perfStatus);
        oltPonAttribute = snmpExecutorService.setData(snmpParam, oltPonAttribute);
        return oltPonAttribute.getPerfStats24hourEnable();
    }

    @Override
    public List<OltPonStormSuppressionEntry> getOltPonStormSuppressionEntries(SnmpParam snmpParam) {
        List<OltPonStormSuppressionEntry> oltPonStormSuppressionEntries = new ArrayList<OltPonStormSuppressionEntry>();
        oltPonStormSuppressionEntries = snmpExecutorService.execute(new SnmpWorker<List<OltPonStormSuppressionEntry>>(
                snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result.addAll(snmpUtil.getTable(OltPonStormSuppressionEntry.class, true));
            }
        }, oltPonStormSuppressionEntries);
        return oltPonStormSuppressionEntries;
    }

    @Override
    public OltPonStormSuppressionEntry setPonStormInfo(SnmpParam snmpParam,
            OltPonStormSuppressionEntry oltPonStormSuppressionEntry) {
        return snmpExecutorService.setData(snmpParam, oltPonStormSuppressionEntry);
    }

    @Override
    public void setPonRateLimit(SnmpParam snmpParam, TopPonPortRateLimit ponPortRateLimit) {
        snmpExecutorService.setData(snmpParam, ponPortRateLimit);
    }

    @Override
    public void setPonPortSpeedMode(SnmpParam snmpParam, TopPonPortSpeedEntry topPonPortSpeedEntry) {
        snmpExecutorService.setData(snmpParam, topPonPortSpeedEntry);
    }

    @Override
    public List<OltPonAttribute> getPonListAttribute(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltPonAttribute.class);
    }

    @Override
    public OltPonAttribute getPonAttribute(SnmpParam snmpParam, Long ponIndex) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setPonIndex(ponIndex);
        return snmpExecutorService.getTableLine(snmpParam, oltPonAttribute);
    }

    @Override
    public List<TopPonPortSpeedEntry> getPonPortSpeed(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopPonPortSpeedEntry.class);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
