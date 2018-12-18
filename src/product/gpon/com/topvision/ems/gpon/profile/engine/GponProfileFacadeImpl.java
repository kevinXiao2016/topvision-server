/***********************************************************************
 * $Id: GponProfilleFacadeImpl.java,v1.0 2016年10月25日 下午12:15:13 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.gpon.profile.facade.GponProfileFacade;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvPotsInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvProfile;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2016年10月25日-下午12:15:13
 *
 */
@Facade("gponProfilleFacade")
public class GponProfileFacadeImpl extends EmsFacade implements GponProfileFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public <T> List<T> getGponProfileTable(SnmpParam snmpParam, Class<T> clazz) {
        return snmpExecutorService.getTable(snmpParam, clazz);
    }

    @Override
    public <T> void setGponProfileTable(SnmpParam snmpParam, T object) {
        snmpExecutorService.setData(snmpParam, object);
    }

    @Override
    public List<GponLineProfileTcont> getTcontInProfile(SnmpParam snmpParam, GponLineProfileTcont tcont) {
        return snmpExecutorService.getTableRangeLine(snmpParam, tcont, 1, 7);
    }

    @Override
    public List<GponLineProfileGem> getGemInProfile(SnmpParam snmpParam, GponLineProfileGem gem) {
        return snmpExecutorService.getTableRangeLine(snmpParam, gem, 1, 64);
    }

    @Override
    public List<GponLineProfileGemMap> getGemMapInProfile(SnmpParam snmpParam, GponLineProfileGemMap gemMap) {
        return snmpExecutorService.getTableRangeLine(snmpParam, gemMap, 1, 8);
    }

    @Override
    public List<GponSrvProfileEthPortConfig> getEthPortConfigInProfile(SnmpParam snmpParam,
            GponSrvProfileEthPortConfig ethPortConfig) {
        return snmpExecutorService.getTableRangeLine(snmpParam, ethPortConfig, 1, 24);
    }

    @Override
    public List<GponSrvProfilePortVlanCfg> getPortVlanCfgInProfile(SnmpParam snmpParam,
            GponSrvProfilePortVlanCfg portVlanCfg) {
        return snmpExecutorService.getTableRangeLine(snmpParam, portVlanCfg, 1, 24);
    }

    @Override
    public GponSrvProfilePortVlanCfg getPortVlanCfg(SnmpParam snmpParam, GponSrvProfilePortVlanCfg portVlanCfg) {
        return snmpExecutorService.getTableLine(snmpParam, portVlanCfg);
    }

    @Override
    public GponSrvProfileCfg getGponSrvProfileCfg(SnmpParam snmpParam, GponSrvProfileCfg cfg) {
        return snmpExecutorService.getTableLine(snmpParam, cfg);
    }

    @Override
    public GponSrvProfilePortNumProfile getPortNumProfile(SnmpParam snmpParam, GponSrvProfilePortNumProfile portNum) {
        return snmpExecutorService.getTableLine(snmpParam, portNum);
    }

    @Override
    public <T> List<T> getGponTableRangeLines(SnmpParam snmpParam, T object, Long startIndex, Long endIndex) {
        return snmpExecutorService.getTableRangeLine(snmpParam, object, startIndex, endIndex);
    }

    @Override
    public <T> T getGponTableLine(SnmpParam snmpParam, T object) {
        return snmpExecutorService.getTableLine(snmpParam, object);
    }

    @Override
    public TopGponSrvProfile getTopGponSrvProfile(SnmpParam snmpParam, TopGponSrvProfile cfg) {
        return snmpExecutorService.getTableLine(snmpParam, cfg);
    }

    @Override
    public TopSIPAgentProfInfo getTopSIPAgentProfInfo(SnmpParam snmpParam, TopSIPAgentProfInfo cfg) {
        return snmpExecutorService.getTableLine(snmpParam, cfg);
    }

    @Override
    public List<TopGponSrvPotsInfo> getTopGponSrvPotsInfo(SnmpParam snmpParam, TopGponSrvPotsInfo pots) {
        return snmpExecutorService.getTableRangeLine(snmpParam, pots, 1, 2);
    }

}
