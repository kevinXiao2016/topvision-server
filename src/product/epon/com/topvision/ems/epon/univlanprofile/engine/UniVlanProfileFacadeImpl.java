/***********************************************************************
 * $Id: UniVlanProfileFacadeImpl.java,v1.0 2013-11-28 上午10:16:50 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanRuleTable;
import com.topvision.ems.epon.univlanprofile.facade.UniVlanProfileFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;

/**
 * @author flack
 * @created @2013-11-28-上午10:16:50
 *
 */
public class UniVlanProfileFacadeImpl extends EmsFacade implements UniVlanProfileFacade {
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public List<UniVlanProfileTable> getUniVlanProfiles(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, UniVlanProfileTable.class);
    }

    @Override
    public UniVlanProfileTable addUniVlanProfile(SnmpParam snmpParam, UniVlanProfileTable uniVlanProfile) {
        uniVlanProfile.setProfileRowstatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, uniVlanProfile);
    }

    @Override
    public UniVlanProfileTable deleteUniVlanProfile(SnmpParam snmpParam, UniVlanProfileTable uniVlanProfile) {
        uniVlanProfile.setProfileRowstatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, uniVlanProfile);
    }

    @Override
    public UniVlanProfileTable setUniVlanProfile(SnmpParam snmpParam, UniVlanProfileTable uniVlanProfile) {
        //底层不支持配置0(无模式),此时不下发
        if (uniVlanProfile.getProfileMode() == 0) {
            uniVlanProfile.setProfileMode(null);
        }
        return snmpExecutorService.setData(snmpParam, uniVlanProfile);
    }

    @Override
    public List<UniVlanRuleTable> getUniVlanRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, UniVlanRuleTable.class);
    }

    @Override
    public UniVlanRuleTable addUniVlanRule(SnmpParam snmpParam, UniVlanRuleTable uniVlanRule) {
        uniVlanRule.setRuleRowstatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, uniVlanRule);
    }

    @Override
    public UniVlanRuleTable deleteUniVlanRule(SnmpParam snmpParam, UniVlanRuleTable uniVlanRule) {
        uniVlanRule.setRuleRowstatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, uniVlanRule);
    }

    @Override
    public UniVlanRuleTable setUniVlanRule(SnmpParam snmpParam, UniVlanRuleTable uniVlanRule) {
        return snmpExecutorService.setData(snmpParam, uniVlanRule);
    }

    @Override
    public List<UniVlanBindTable> getUniVlanBindTables(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, UniVlanBindTable.class);
    }

    @Override
    public UniVlanBindTable setUniVlanBindTables(SnmpParam snmpParam, UniVlanBindTable uniVlanBindTable) {
        return snmpExecutorService.setData(snmpParam, uniVlanBindTable);
    }

    @Override
    public UniVlanBindTable getUniVlanBindInfo(SnmpParam snmpParam, UniVlanBindTable uniVlanBindTable) {
        return snmpExecutorService.getTableLine(snmpParam, uniVlanBindTable);
    }

    @Override
    public void replaceBindProfile(SnmpParam snmpParam, UniVlanBindTable uniBindInfo) {
        if (uniBindInfo.getBindProfAttr() == EponConstants.UNIVLAN_PROFILECONFIG) {
            //通过业务模板绑定时直接修改
            snmpExecutorService.setData(snmpParam, uniBindInfo);
        } else {
            //离散配置时先解绑定再修改
            /*先执行解绑定*/
            UniVlanBindTable bindTable = new UniVlanBindTable();
            bindTable.setUniIndex(uniBindInfo.getUniIndex());
            bindTable.setBindProfileId(EponConstants.UNIVALN_UNBIND_PROFILEID);
            snmpExecutorService.setData(snmpParam, bindTable);
            /*再执行绑定*/
            snmpExecutorService.setData(snmpParam, uniBindInfo);
        }
    }

    @Override
    public List<UniVlanBindTable> getUniVlanBindTables(SnmpParam snmpParam, List<UniVlanBindTable> uniVlanBindTables) {
        return snmpExecutorService.getTableLine(snmpParam, uniVlanBindTables);
    }

    @Override
    public void unBindUniProfile(SnmpParam snmpParam, UniVlanBindTable uniVlanBindTable) {
        snmpExecutorService.setData(snmpParam, uniVlanBindTable);
    }
}
