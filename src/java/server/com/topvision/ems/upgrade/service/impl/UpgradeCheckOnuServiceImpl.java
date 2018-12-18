/***********************************************************************
 * $Id: UpgradeCheckOnuServiceImpl.java,v1.0 2014年9月23日 下午6:58:33 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service.impl;

import org.springframework.stereotype.Service;

import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2014年9月23日-下午6:58:33
 * 
 */
@Service("upgradeCheckOnuService")
public class UpgradeCheckOnuServiceImpl extends UpgradeCheckServiceImpl {

    @Override
    public Boolean checkVersion(SnmpParam snmpParam, String version) {
        return null;
    }

    @Override
    public Boolean checkOnlineStatus(TelnetUtil telnetUtil) {
        return null;
    }

    @Override
    public Boolean checkUbootVersion(TelnetUtil telnetUtil, String ubootVersion) {
        return null;
    }

    @Override
    public Boolean checkUpgradeVersion(String cmdResult) {
        return null;
    }

}
