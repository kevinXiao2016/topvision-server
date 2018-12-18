/***********************************************************************
 * $Id: GponOnuFacadeImpl.java,v1.0 2016年12月19日 下午4:50:07 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponOnuInfoSoftware;
import com.topvision.ems.gpon.onu.domain.GponOnuIpHost;
import com.topvision.ems.gpon.onu.domain.GponOnuQualityInfo;
import com.topvision.ems.gpon.onu.domain.GponOnuUniPvid;
import com.topvision.ems.gpon.onu.facade.GponOnuFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2016年12月19日-下午4:50:07
 *
 */
@Facade("gponOnuFacade")
public class GponOnuFacadeImpl extends EmsFacade implements GponOnuFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.facade.OnuFacade#getGponOnuSoftwares(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public List<GponOnuInfoSoftware> getGponOnuSoftwares(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, GponOnuInfoSoftware.class);
    }

    @Override
    public GponOnuInfoSoftware getGponOnuSoftware(SnmpParam snmpParam, GponOnuInfoSoftware software) {
        return snmpExecutorService.getTableLine(snmpParam, software);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.facade.OnuFacade#getGponOnuIpHosts(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public List<GponOnuIpHost> getGponOnuIpHosts(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, GponOnuIpHost.class);
    }

    @Override
    public List<GponOnuIpHost> getGponOnuIpHosts(SnmpParam snmpParam, GponOnuIpHost gponOnuIpHost) {
        return snmpExecutorService.getTableRangeLine(snmpParam, gponOnuIpHost, 1, 64);
    }

    @Override
    public List<GponOnuCapability> getGponOnuCapabilitys(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, GponOnuCapability.class);
    }

    @Override
    public List<GponOnuUniPvid> getGponOnuUniPvidList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, GponOnuUniPvid.class);
    }

    @Override
    public GponOnuUniPvid getGponOnuUniPvid(SnmpParam snmpParam, GponOnuUniPvid gponOnuUniPvid) {
        return snmpExecutorService.getTableLine(snmpParam, gponOnuUniPvid);
    }

    @Override
    public void addGponOnuIpHost(SnmpParam snmpParam, GponOnuIpHost gponOnuIpHost) {
        gponOnuIpHost.setOnuIpHostRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, gponOnuIpHost);
    }

    @Override
    public void modifyGponOnuIpHost(SnmpParam snmpParam, GponOnuIpHost gponOnuIpHost) {
        snmpExecutorService.setData(snmpParam, gponOnuIpHost);
    }

    @Override
    public void deleteGponOnuIpHost(SnmpParam snmpParam, GponOnuIpHost gponOnuIpHost) {
        gponOnuIpHost.setOnuIpHostRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, gponOnuIpHost);
    }

    @Override
    public void resetOnu(SnmpParam snmpParam, Long onuIndex) {
        GponOnuAttribute gponOnuAttribute = new GponOnuAttribute();
        gponOnuAttribute.setOnuIndex(onuIndex);
        gponOnuAttribute.setResetONU(1);
        snmpExecutorService.setData(snmpParam, gponOnuAttribute);
    }

    @Override
    public void deActiveOnu(SnmpParam snmpParam, Long onuIndex, Integer active) {
        GponOnuAttribute gponOnuAttribute = new GponOnuAttribute();
        gponOnuAttribute.setOnuIndex(onuIndex);
        gponOnuAttribute.setOnuDeactive(active);
        snmpExecutorService.setData(snmpParam, gponOnuAttribute);
    }

    @Override
    public void setOnuAdminStatus(SnmpParam snmpParam, Long onuIndex, Integer status) {
        GponOnuAttribute gponOnuAttribute = new GponOnuAttribute();
        gponOnuAttribute.setOnuIndex(onuIndex);
        gponOnuAttribute.setOnuAdminStatus(status);
        snmpExecutorService.setData(snmpParam, gponOnuAttribute);
    }

    @Override
    public void setOnu15minEnable(SnmpParam snmpParam, Long onuIndex, Integer onu15minEnable) {
        GponOnuAttribute gponOnuAttribute = new GponOnuAttribute();
        gponOnuAttribute.setOnuIndex(onuIndex);
        gponOnuAttribute.setOnuPerfStats15minuteEnable(onu15minEnable);
        snmpExecutorService.setData(snmpParam, gponOnuAttribute);
    }

    @Override
    public void setOnu24hEnable(SnmpParam snmpParam, Long onuIndex, Integer onu24hEnable) {
        GponOnuAttribute gponOnuAttribute = new GponOnuAttribute();
        gponOnuAttribute.setOnuIndex(onuIndex);
        gponOnuAttribute.setOnuPerfStats24hourEnable(onu24hEnable);
        snmpExecutorService.setData(snmpParam, gponOnuAttribute);
    }

    @Override
    public GponOnuQualityInfo refreshGponOnuQuality(SnmpParam snmpParam, GponOnuQualityInfo gponOnuQualityInfo) {
        return snmpExecutorService.getTableLine(snmpParam, gponOnuQualityInfo);
    }

}
