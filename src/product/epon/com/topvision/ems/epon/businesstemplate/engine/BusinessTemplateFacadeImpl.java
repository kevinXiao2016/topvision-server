/***********************************************************************
 * $Id: BusinessTemplateFacadeImpl.java,v1.0 2015-12-8 下午2:27:01 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.businesstemplate.domain.OnuCapability;
import com.topvision.ems.epon.businesstemplate.domain.OnuIgmpProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuPortVlanProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvIgmpVlanTrans;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvProfile;
import com.topvision.ems.epon.businesstemplate.facade.BusinessTemplateFacade;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2015-12-8-下午2:27:01
 *
 */
@Engine("businessTemplateFacade")
public class BusinessTemplateFacadeImpl extends EmsFacade implements BusinessTemplateFacade {

    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<OnuSrvProfile> refreshOnuSrvProfile(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuSrvProfile.class);
    }

    @Override
    public List<OnuPortVlanProfile> refreshOnuPortVlanProfile(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuPortVlanProfile.class);
    }

    @Override
    public List<OnuIgmpProfile> refreshOnuIgmpProfile(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuIgmpProfile.class);
    }

    @Override
    public List<OnuSrvIgmpVlanTrans> refreshOnuSrvIgmpVlanTrans(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuSrvIgmpVlanTrans.class);
    }

    @Override
    public void deleteOnuSrvProfile(SnmpParam snmpParam, OnuSrvProfile srvProfile) {
        srvProfile.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, srvProfile);
    }

    @Override
    public void deleteOnuPortVlanProfile(SnmpParam snmpParam, OnuPortVlanProfile portVlanProfile) {
        portVlanProfile.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, portVlanProfile);
    }

    @Override
    public void deleteOnuIgmpProfile(SnmpParam snmpParam, OnuIgmpProfile igmpProfile) {
        igmpProfile.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, igmpProfile);
    }

    @Override
    public OnuSrvProfile modifyOnuSrvProfile(SnmpParam snmpParam, OnuSrvProfile srvProfile) {
        return snmpExecutorService.setData(snmpParam, srvProfile);
    }

    @Override
    public OnuPortVlanProfile modifyOnuPortVlanProfile(SnmpParam snmpParam, OnuPortVlanProfile portVlanProfile) {
        return snmpExecutorService.setData(snmpParam, portVlanProfile);
    }

    @Override
    public OnuIgmpProfile modifyOnuIgmpProfile(SnmpParam snmpParam, OnuIgmpProfile igmpProfile) {
        return snmpExecutorService.setData(snmpParam, igmpProfile);
    }

    @Override
    public void addOnuSrvProfile(SnmpParam snmpParam, OnuSrvProfile srvProfile) {
        srvProfile.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, srvProfile);
    }

    @Override
    public void addOnuPortVlanProfile(SnmpParam snmpParam, OnuPortVlanProfile portVlanProfile) {
        portVlanProfile.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, portVlanProfile);
    }

    @Override
    public void addOnuIgmpProfile(SnmpParam snmpParam, OnuIgmpProfile igmpProfile) {
        igmpProfile.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, igmpProfile);
    }

    @Override
    public List<OnuCapability> refreshOnuCapability(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuCapability.class);
    }

    @Override
    public void deleteOnuCapability(SnmpParam snmpParam, OnuCapability capability) {
        capability.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, capability);
    }

    @Override
    public void addOnuCapability(SnmpParam snmpParam, OnuCapability capability) {
        capability.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, capability);
    }

    @Override
    public void unBindCapability(SnmpParam snmpParam, OnuSrvProfile srvProfile) {
        snmpExecutorService.setData(snmpParam, srvProfile);
    }

    @Override
    public void deleteOnuIgmpVlanTrans(SnmpParam snmpParam, OnuSrvIgmpVlanTrans onuIgmpVlanTrans) {
        onuIgmpVlanTrans.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, onuIgmpVlanTrans);
    }

    @Override
    public void addOnuSrvIgmpVlanTrans(SnmpParam snmpParam, OnuSrvIgmpVlanTrans onuIgmpVlanTrans) {
        onuIgmpVlanTrans.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, onuIgmpVlanTrans);
    }

}
