/***********************************************************************
 * $Id: GponOnuAuthFacadeImpl.java,v1.0 2016年12月19日 下午1:15:56 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthMode;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAutoFind;
import com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2016年12月19日-下午1:15:56
 *
 */
@Facade("gponOnuAuthFacade")
public class GponOnuAuthFacadeImpl extends EmsFacade implements GponOnuAuthFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#refershGponOnuAutoAuthList(com.topvision
     * .framework.snmp.SnmpParam, java.lang.Class)
     */
    @Override
    public List<GponAutoAuthConfig> refershGponOnuAutoAuthList(SnmpParam snmpParam, Class<GponAutoAuthConfig> clazz) {
        return snmpExecutorService.getTable(snmpParam, clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#refershGponOnuAutoFindList(com.topvision
     * .framework.snmp.SnmpParam, java.lang.Class)
     */
    @Override
    public List<GponOnuAutoFind> refershGponOnuAutoFindList(SnmpParam snmpParam, Class<GponOnuAutoFind> clazz) {
        return snmpExecutorService.getTable(snmpParam, clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#refershGponOnuAuthConfigList(com.
     * topvision.framework.snmp.SnmpParam, java.lang.Class)
     */
    @Override
    public List<GponOnuAuthConfig> refershGponOnuAuthConfigList(SnmpParam snmpParam, Class<GponOnuAuthConfig> clazz) {
        return snmpExecutorService.getTable(snmpParam, clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#refershGponOnuAuthModeList(com.topvision
     * .framework.snmp.SnmpParam, java.lang.Class)
     */
    @Override
    public List<GponOnuAuthMode> refershGponOnuAuthModeList(SnmpParam snmpParam, Class<GponOnuAuthMode> clazz) {
        return snmpExecutorService.getTable(snmpParam, clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#addGponOnuAuth(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig)
     */
    @Override
    public void addGponOnuAuth(SnmpParam snmpParam, GponOnuAuthConfig gponOnuAuthConfig) {
        gponOnuAuthConfig.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, gponOnuAuthConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#modifyOnuAuthMode(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.gpon.onuauth.domain.GponOnuAuthMode)
     */
    @Override
    public void modifyOnuAuthMode(SnmpParam snmpParam, GponOnuAuthMode gponOnuAuthMode) {
        snmpExecutorService.setData(snmpParam, gponOnuAuthMode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#modifyOnuAuthMode(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig)
     */
    @Override
    public void addGponAutoAuthConfig(SnmpParam snmpParam, GponAutoAuthConfig gponAutoAuthConfig) {
        gponAutoAuthConfig.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, gponAutoAuthConfig);
    }

    @Override
    public void modifyGponAutoAuthConfig(SnmpParam snmpParam, GponAutoAuthConfig gponAutoAuthConfig) {
        snmpExecutorService.setData(snmpParam, gponAutoAuthConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#deleteGponOnuAuth(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig)
     */
    @Override
    public void deleteGponOnuAuth(SnmpParam snmpParam, GponOnuAuthConfig gponOnuAuthConfig) {
        gponOnuAuthConfig.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, gponOnuAuthConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade#deleteGponAutoAuth(com.topvision.
     * framework.snmp.SnmpParam, com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig)
     */
    @Override
    public void deleteGponAutoAuth(SnmpParam snmpParam, GponAutoAuthConfig gponAutoAuthConfig) {
        gponAutoAuthConfig.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, gponAutoAuthConfig);
    }
}
