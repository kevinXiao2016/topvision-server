/***********************************************************************
 * $Id: UpgradeCheckC_BServiceImpl.java,v1.0 2014年9月23日 下午6:58:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.ems.upgrade.utils.VersionUtil;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2014年9月23日-下午6:58:20
 * 
 */
@Service("upgradeCheckC_BService")
public class UpgradeCheckC_BServiceImpl extends UpgradeCheckServiceImpl {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public Boolean checkVersion(SnmpParam snmpParam, String version) {
        String deviceVersion = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.15.142671872");
        long upgradeVersionLong = VersionUtil.getVersionLong(version);
        long deviceVersionLong = VersionUtil.getVersionLong(deviceVersion);
        String upgradeVersionAdditional = VersionUtil.getVersionAdditional(version);
        String deviceVersionAdditional = VersionUtil.getVersionAdditional(deviceVersion);
        if (logger.isDebugEnabled()) {
            logger.debug("upgrade checkVersion version[" + version + "] deviceVersion[" +deviceVersion + "] " +
                    "upgradeVersionLong[" +upgradeVersionLong + "] deviceVersionLong[" +deviceVersionLong + "] " +
                    "upgradeVersionAdditional[" +upgradeVersionAdditional + "] deviceVersionAdditional[" +deviceVersionAdditional +"]");
        }
        if (upgradeVersionLong != deviceVersionLong || !upgradeVersionAdditional.equals(deviceVersionAdditional)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean checkOnlineStatus(TelnetUtil telnetUtil) {
        String result = telnetUtil.execCmd("show ccmts");
        String[] showCCmts = result.trim().split("[\\t\\n\\r]");
        String[] showCCmtsTitle = showCCmts[2].trim().split("\\s+");
        String[] showCCmtsResult = showCCmts[4].trim().split("\\s+");
        for (int i = 0; i < showCCmtsTitle.length; i++) {
            if ("State".equals(showCCmtsTitle[i])) {
                return "online".equals(showCCmtsResult[i]);
            }
        }
        return false;
    }

    @Override
    public Boolean checkUbootVersion(TelnetUtil telnetUtil, String ubootVersion) {
        if (ubootVersion == null || "".equals(ubootVersion)) {
            return true;
        }
        String result1 = telnetUtil.execCmd("show dol-version");
        String[] showDol = result1.split("[\\t\\n\\r]");
        for (int i = 0; i < showDol.length; i++) {
            if (showDol[i].trim().startsWith("u-boot")) {
                return showDol[i].split(":")[1].trim().equals(ubootVersion);
            }
        }
        return false;
    }

    @Override
    public Boolean checkUpgradeVersion(String cmdResult) {
        if (cmdResult == null || "".equals(cmdResult.trim())) {
            return false;
        }
        String[] showDol = cmdResult.split("[\\t\\n\\r]");
        for (int i = 0; i < showDol.length; i++) {
            if (showDol[i].trim().startsWith("%")
                    || showDol[i].trim().startsWith("Error:")
                    || showDol[i].contains("Download image failed.")
                    || showDol[i].contains("Open image file fail.") || showDol[i].contains("File error.")
                    || showDol[i].contains("Bad packet!") || showDol[i].contains("Erase flash failed.")
                    || showDol[i].contains("Cmcapp upgrading, please try again later.")
                    || showDol[i].contains("Cmc Upgrade finished with error!")
                    || showDol[i].contains("The file name is too long.")
                    || showDol[i].contains("Bad IP address!")
                    || showDol[i].contains("Download image packet file failed.")
                    || showDol[i].contains("File open error.")
                    || showDol[i].contains("File stat error.")
                    || showDol[i].contains("File size error.")
                    || showDol[i].contains("File header mmap error.")
                    || showDol[i].contains("Bad packet.")
                    || showDol[i].contains("Write flash failed.")
                    || showDol[i].contains("crc check error! ")
                    || showDol[i].contains("Cmc Upgrade finished with error!")) {
                return false;
            }
        }
        return true;
    }
}
