/***********************************************************************
 * $Id: OnuIgmpConfigFacadeImpl.java,v1.0 2016-6-6 下午4:39:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.ems.epon.igmpconfig.facade.OnuIgmpConfigFacade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2016-6-6-下午4:39:08
 *
 */
public class OnuIgmpConfigFacadeImpl extends EmsFacade implements OnuIgmpConfigFacade {
    public static final int PORT_TYPE_ONU = 10;
    public static final int PORT_TYPE_UNI = 11;

    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public void createBindCtcProfile(SnmpParam snmpParam, IgmpUniBindCtcProfile bindCtcProfile) {
        bindCtcProfile.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, bindCtcProfile);
    }

    @Override
    public void destoryBindCtcProfile(SnmpParam snmpParam, IgmpUniBindCtcProfile bindCtcProfile) {
        bindCtcProfile.setRowStatus(RowStatus.DESTORY);
        try {
            snmpExecutorService.setData(snmpParam, bindCtcProfile);
        } catch (SnmpException e) {
            logger.info("destoryBindCtcProfile {}", e);
            /*
             * 删除失败，网管侧进行重试一次
             */
            snmpExecutorService.setData(snmpParam, bindCtcProfile);
        }
    }

    @Override
    public IgmpOnuConfig getIgmpOnuConfig(SnmpParam snmpParam, Long onuIndex) {
        IgmpOnuConfig onuConfig = new IgmpOnuConfig();
        onuConfig.setPortType(PORT_TYPE_ONU);
        onuConfig.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(snmpParam, onuConfig);
    }

    @Override
    public void modifyIgmpOnuConfig(SnmpParam snmpParam, IgmpOnuConfig onuConfig) {
        onuConfig.setPortType(PORT_TYPE_ONU);
        snmpExecutorService.setData(snmpParam, onuConfig);
    }

    @Override
    public IgmpUniConfig getIgmpUniConfig(SnmpParam snmpParam, Long uniIndex) {
        IgmpUniConfig uniConfig = new IgmpUniConfig();
        uniConfig.setPortType(PORT_TYPE_UNI);
        uniConfig.setUniIndex(uniIndex);
        return snmpExecutorService.getTableLine(snmpParam, uniConfig);
    }

    @Override
    public void modifyIgmpUniConfig(SnmpParam snmpParam, IgmpUniConfig uniConfig) {
        uniConfig.setPortType(PORT_TYPE_UNI);
        snmpExecutorService.setData(snmpParam, uniConfig);
    }

    @Override
    public void createUniVlanTrans(SnmpParam snmpParam, IgmpUniVlanTrans vlanTrans) {
        vlanTrans.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, vlanTrans);
    }

    @Override
    public List<IgmpUniVlanTrans> getUniVlanTransList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpUniVlanTrans.class);
    }

    @Override
    public void destoryUniVlanTrans(SnmpParam snmpParam, IgmpUniVlanTrans vlanTrans) {
        vlanTrans.setPortType(PORT_TYPE_UNI);
        vlanTrans.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanTrans);
    }
}
