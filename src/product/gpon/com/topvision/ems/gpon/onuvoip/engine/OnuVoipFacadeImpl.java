/***********************************************************************
 * $Id: OnuVoipFacadeImpl.java,v1.0 2017年5月4日 上午11:34:55 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.engine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.gpon.onuvoip.domain.TopOnuIfPotsInfo;
import com.topvision.ems.gpon.onuvoip.domain.TopSIPPstnUser;
import com.topvision.ems.gpon.onuvoip.domain.TopVoIPLineStatus;
import com.topvision.ems.gpon.onuvoip.facade.OnuVoipFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2017年5月4日-上午11:34:55
 *
 */
@Facade("onuVoipFacade")
public class OnuVoipFacadeImpl extends EmsFacade implements OnuVoipFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<TopVoIPLineStatus> getTopVoIPLineStatus(SnmpParam snmpParam, Long onuIndex) {
        List<TopVoIPLineStatus> result = new ArrayList<TopVoIPLineStatus>();
        for (int i = 0; i < 2; i++) {
            try {
                TopVoIPLineStatus status = new TopVoIPLineStatus();
                status.setOnuIndex(onuIndex);
                status.setTopVoIPLinePotsIdx(i + 1);
                status = snmpExecutorService.getTableLine(snmpParam, status);
                result.add(status);
            } catch (Exception e) {
                logger.debug("get topVoIPLineStatus pots id = " + (i + 1) + "error!");
            }
        }
        return result;
    }

    @Override
    public List<TopSIPPstnUser> getTopSIPPstnUser(SnmpParam snmpParam, Long onuIndex) {
        List<TopSIPPstnUser> result = new ArrayList<TopSIPPstnUser>();
        for (int i = 0; i < 2; i++) {
            try {
                TopSIPPstnUser user = new TopSIPPstnUser();
                user.setOnuIndex(onuIndex);
                user.setTopSIPPstnUserPotsIdx(i + 1);
                user = snmpExecutorService.getTableLine(snmpParam, user);
                result.add(user);
            } catch (Exception e) {
                logger.debug("get topSIPPstnUser pots id = " + (i + 1) + "error!");
            }
        }
        return result;
    }

    @Override
    public List<TopVoIPLineStatus> getTopVoIPLineStatus(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopVoIPLineStatus.class);
    }

    @Override
    public List<TopSIPPstnUser> getTopSIPPstnUser(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopSIPPstnUser.class);
    }

    @Override
    public void modifyTopSIPPstnUser(SnmpParam snmpParam, TopSIPPstnUser topSIPPstnUser) {
        snmpExecutorService.setData(snmpParam, topSIPPstnUser);
    }

    @Override
    public List<TopOnuIfPotsInfo> getTopOnuIfPotsInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOnuIfPotsInfo.class);
    }

    @Override
    public List<TopOnuIfPotsInfo> getTopOnuIfPotsInfo(SnmpParam snmpParam, Long onuIndex) {
        List<TopOnuIfPotsInfo> result = new ArrayList<TopOnuIfPotsInfo>();
        for (int i = 0; i < 2; i++) {
            try {
                TopOnuIfPotsInfo user = new TopOnuIfPotsInfo();
                user.setOnuIndex(onuIndex);
                user.setTopOnuIfPotsPotsIdx(i + 1);
                user = snmpExecutorService.getTableLine(snmpParam, user);
                result.add(user);
            } catch (Exception e) {
                logger.debug("get topOnuIfPotsInfo pots id = " + (i + 1) + "error!");
            }
        }
        return result;
    }

    @Override
    public void setOnuPotsAdminStatus(SnmpParam snmpParam, TopOnuIfPotsInfo onuIfPotsInfo) {
        snmpExecutorService.setData(snmpParam, onuIfPotsInfo);
    }

}
