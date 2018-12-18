/***********************************************************************
 * $Id: UpgradeCheckService.java,v1.0 2014年9月23日 下午4:05:39 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service;

import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2014年9月23日-下午4:05:39
 * 
 */
public interface UpgradeCheckService extends Service {
    /**
     * 检查ping状态
     * 
     * @param ip
     * @return
     */
    Boolean checkPing(String ip);

    /**
     * 检查snmp状态
     * 
     * @param snmpParam
     * @return
     */
    Boolean checkSnmp(SnmpParam snmpParam);

    /**
     * 检查版本是否一致
     * 
     * @param snmpParam
     * @param version
     * @return
     */
    Boolean checkVersion(SnmpParam snmpParam, String version);

    /**
     * 检查在线状态
     * 
     * @param telnetUtil
     * @return
     */
    Boolean checkOnlineStatus(TelnetUtil telnetUtil);

    /**
     * 检查uboot版本
     * 
     * @param telnetUtil
     * @param ubootVersion
     * @return
     */
    Boolean checkUbootVersion(TelnetUtil telnetUtil, String ubootVersion);

    /**
     * 检查内置ftp状态
     * 
     * @return
     */
    Boolean checkInerFtp();

    /**
     * 检查内置tftp状态
     * 
     * @return
     */
    Boolean checkInerTftp();
    
    /**
     * 升级完成后版本比对
     * 
     * @return
     */
    Boolean checkUpgradeVersion(String cmdResult);

}
