/***********************************************************************
 * $Id: UpgradeCheckServiceImpl.java,v1.0 2014年9月23日 下午6:54:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.network.DiscoveryFacade;
import com.topvision.ems.upgrade.service.UpgradeCheckService;
import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.FtpServerService;
import com.topvision.platform.service.TftpServerService;

/**
 * @author loyal
 * @created @2014年9月23日-下午6:54:20
 * 
 */
@Service("upgradeCheckService")
public abstract class UpgradeCheckServiceImpl extends BaseService implements UpgradeCheckService {
    @Autowired
    private TftpServerService tftpServerService;
    @Autowired
    private FtpServerService ftpServerService;
    @Autowired
    private PingExecutorService pingExecutorService;
    @Autowired
    private FacadeFactory facadeFactory;

    @Override
    public Boolean checkPing(String ip) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        try {
            Future<PingResult> f = pingExecutorService.submit(worker, r);
            return f.get().available();
        } catch (InterruptedException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (ExecutionException e) {
            logger.debug("checkPingReachable" + ip, e);
        }
        return false;
    }

    @Override
    public Boolean checkSnmp(SnmpParam snmpParam) {
        try {
            facadeFactory.getFacade(snmpParam.getIpAddress(), DiscoveryFacade.class).discoverySysObjectID(snmpParam);
            return true;
        } catch (Exception e) {
            logger.debug("checkSnmpReachable" + snmpParam.getIpAddress(), e);
        }
        return false;
    }

    @Override
    public Boolean checkInerFtp() {
        return ftpServerService.getFtpServerStatus().isStarted() && ftpServerService.getFtpServerStatus().isReachable();
    }

    @Override
    public Boolean checkInerTftp() {
        return tftpServerService.isTftpServerStarted();
    }

    @Override
    public abstract Boolean checkVersion(SnmpParam snmpParam, String version);

    @Override
    public abstract Boolean checkOnlineStatus(TelnetUtil telnetUtil);

    @Override
    public abstract Boolean checkUbootVersion(TelnetUtil telnetUtil, String ubootVersion);

}
