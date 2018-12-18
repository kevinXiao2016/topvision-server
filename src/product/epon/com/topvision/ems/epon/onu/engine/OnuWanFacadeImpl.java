/***********************************************************************
 * $Id: OnuWanFacadeImpl.java,v1.0 2016年6月28日 上午10:48:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.engine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.onu.domain.OnuWanBind;
import com.topvision.ems.epon.onu.domain.OnuWanConfig;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanConnectStatus;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.ems.epon.onu.facade.OnuWanFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;

/**
 * @author haojie
 * @created @2016年6月28日-上午10:48:30
 *
 */
@Facade("onuWanFacade")
public class OnuWanFacadeImpl extends EmsFacade implements OnuWanFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<OnuWanConfig> getOnuWanConfig(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuWanConfig.class);
    }

    @Override
    public OnuWanConfig getOnuWanConfig(SnmpParam snmpParam, Long onuIndex) {
        OnuWanConfig onuWanConfig = new OnuWanConfig();
        onuWanConfig.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(snmpParam, onuWanConfig);
    }

    @Override
    public List<OnuWanSsid> getOnuWanSsids(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuWanSsid.class);
    }

    @Override
    public List<OnuWanSsid> getOnuWanSsids(SnmpParam snmpParam, Long onuIndex) {
        List<OnuWanSsid> result = new ArrayList<OnuWanSsid>();
        for (int i = 0; i < 4; i++) {
            try {
                OnuWanSsid onuWanSsid = new OnuWanSsid();
                onuWanSsid.setOnuIndex(onuIndex);
                onuWanSsid.setSsid(i + 1);
                onuWanSsid = snmpExecutorService.getTableLine(snmpParam, onuWanSsid);
                result.add(onuWanSsid);
            } catch (Exception e) {
                logger.debug("get onuWanSsid connectId= " + (i + 1) + "error!");
            }
        }
        return result;
    }

    @Override
    public List<OnuWanConnect> getOnuWanConnects(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuWanConnect.class);
    }

    @Override
    public List<OnuWanConnect> getOnuWanConnects(SnmpParam snmpParam, Long onuIndex) {
        List<OnuWanConnect> result = new ArrayList<OnuWanConnect>();
        for (int i = 0; i < 8; i++) {
            try {
                OnuWanConnect onuWanConnect = new OnuWanConnect();
                onuWanConnect.setOnuIndex(onuIndex);
                onuWanConnect.setConnectId(i + 1);
                onuWanConnect = snmpExecutorService.getTableLine(snmpParam, onuWanConnect);
                result.add(onuWanConnect);
            } catch (Exception e) {
                logger.debug("get onuWanConnect connectId= " + (i + 1) + "error!");
            }
        }
        return result;
    }

    @Override
    public List<OnuWanConnectStatus> getOnuWanConnectStatus(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuWanConnectStatus.class);
    }

    @Override
    public List<OnuWanConnectStatus> getOnuWanConnectStatus(SnmpParam snmpParam, Long onuIndex) {
        List<OnuWanConnectStatus> result = new ArrayList<OnuWanConnectStatus>();
        for (int i = 0; i < 8; i++) {
            try {
                OnuWanConnectStatus onuWanConnectStatus = new OnuWanConnectStatus();
                onuWanConnectStatus.setOnuIndex(onuIndex);
                onuWanConnectStatus.setConnectId(i + 1);
                onuWanConnectStatus = snmpExecutorService.getTableLine(snmpParam, onuWanConnectStatus);
                result.add(onuWanConnectStatus);
            } catch (Exception e) {
                logger.debug("get onuWanConnectStatus connectId= " + (i + 1) + "error!");
            }
        }
        return result;
    }

    @Override
    public void restoreWifiOnu(SnmpParam snmpParam, Long onuIndex) {
        Long onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        OnuWanConfig onuWanConfig = new OnuWanConfig();
        onuWanConfig.setOnuMibIndex(onuMibIndex);
        onuWanConfig.setResetWan(1);
        snmpExecutorService.setData(snmpParam, onuWanConfig);
    }

    @Override
    public OnuWanConfig modifyOnuWanConfig(SnmpParam snmpParam, OnuWanConfig onuWanConfig) {
        return snmpExecutorService.setData(snmpParam, onuWanConfig);
    }

    @Override
    public void clearWanConnect(SnmpParam snmpParam, Long onuIndex) {
        Long onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        OnuWanConfig onuWanConfig = new OnuWanConfig();
        onuWanConfig.setOnuMibIndex(onuMibIndex);
        onuWanConfig.setClearWan(1);
        snmpExecutorService.setData(snmpParam, onuWanConfig);
    }

    @Override
    public void addOnuWanSsid(SnmpParam snmpParam, OnuWanSsid onuWanSsid) {
        onuWanSsid.setSsidRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, onuWanSsid);
    }

    @Override
    public void modifyOnuWanSsid(SnmpParam snmpParam, OnuWanSsid onuWanSsid) {
        snmpExecutorService.setData(snmpParam, onuWanSsid);
    }

    @Override
    public void deleteOnuWanSsid(SnmpParam snmpParam, OnuWanSsid onuWanSsid) {
        onuWanSsid.setSsidRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, onuWanSsid);
    }

    @Override
    public OnuWanConnect addOnuWanConnect(SnmpParam snmpParam, OnuWanConnect onuWanConnect) {
        onuWanConnect.setWanConnectRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, onuWanConnect);
    }

    @Override
    public void modifyOnuWanConnect(SnmpParam snmpParam, OnuWanConnect onuWanConnect) {
        snmpExecutorService.setData(snmpParam, onuWanConnect);
    }

    @Override
    public void deleteOnuWanConnect(SnmpParam snmpParam, OnuWanConnect onuWanConnect) {
        onuWanConnect.setWanConnectRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, onuWanConnect);
    }

    @Override
    public void modifyBindInterface(SnmpParam snmpParam, Long onuIndex, Integer connectId, String bindInterface) {
        Long onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        OnuWanBind onuWanBind = new OnuWanBind();
        onuWanBind.setOnuMibIndex(onuMibIndex);
        onuWanBind.setConnectId(connectId);
        onuWanBind.setBindInterface("0x" + bindInterface);
        snmpExecutorService.setData(snmpParam, onuWanBind);
    }

    @Override
    public void modifyServieMode(SnmpParam snmpParam, OnuWanBind onuWanBind) {
        snmpExecutorService.setData(snmpParam, onuWanBind);
    }

}
