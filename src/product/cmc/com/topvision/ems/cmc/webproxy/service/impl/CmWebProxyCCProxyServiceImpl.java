/***********************************************************************
 * CmWebProxyCCProxyServiceImpl.java,v1.0 17-5-9 上午10:43 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.service.impl;


import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.webproxy.domain.CmWebCCProxy;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyCCProxyService;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.upgrade.telnet.CcmtsTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.snmp.SnmpParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author jay
 * @created 17-5-9 上午10:43
 */
@Service("cmWebProxyCCProxyService")
public class CmWebProxyCCProxyServiceImpl extends CmcBaseCommonService implements CmWebProxyCCProxyService {
    public static Map<Long,CmWebCCProxy> cmWebCCProxyMap = Collections.synchronizedMap(new HashMap<Long,CmWebCCProxy>());
    @Autowired
    private TelnetLoginService telnetLoginService;
    @Autowired
    private TelnetUtilFactory telnetUtilFactory;

    @Value("${webProxy.cmc.proxyPort:33003}")
    private Integer proxyPort;
    private Object checkAndRemoveAndModify = new Object();

    @Override
    public void start() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        for (Long cmcId : cmWebCCProxyMap.keySet()) {
                            CmWebCCProxy cmWebCCProxy = cmWebCCProxyMap.get(cmcId);
                            cmWebCCProxy.checkStatus();
                        }
                    } catch (Exception e) {
                        logger.error("HeartBeatJob error", e);
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }
    @Override
    public CmWebCCProxy selectCCProxyByCmcId(Long cmcId) {
        CmWebCCProxy cmWebCCProxy = cmWebCCProxyMap.get(cmcId);
        if (cmWebCCProxy == null) {
            cmWebCCProxy = new CmWebCCProxy();
            cmWebCCProxy.setStatus(false);
        }
        return cmWebCCProxy;
    }

    @Override
    public CmWebCCProxy pickCCProxyByCmcId(Long heartbeatId, Long cmcId) {
        CmWebCCProxy cmWebCCProxy = cmWebCCProxyMap.get(cmcId);
        if (cmWebCCProxy == null || !cmWebCCProxy.getStatus()) {
            SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
            cmWebCCProxy = createWebCCProxy(cmcId,snmpParam.getIpAddress(),proxyPort);
            cmWebCCProxyMap.put(cmcId,cmWebCCProxy);
        }
        cmWebCCProxy.checkStatus();
        cmWebCCProxy.addHeartbeatId(heartbeatId);
        return cmWebCCProxy;
    }

    @Override
    public void releaseCCProxyByCmcId(Long heartbeatId, Long cmcId) {
        try {
            CmWebCCProxy cmWebCCProxy = cmWebCCProxyMap.get(cmcId);
            if (cmWebCCProxy != null) {
                cmWebCCProxy.releaseHeartbeatId(heartbeatId);
                if (cmWebCCProxy.heartbeatNum() == 0) {
                    cmWebCCProxy.stopProxy();
                    cmWebCCProxyMap.remove(cmcId);
                    logger.debug("releaseCCProxyByCmcId[{}]",cmcId);
                }
            }
        } catch (Exception e) {
        	logger.debug("",e);
        }
    }

    @Override
    public String listInfo() {
        StringBuilder sb = new StringBuilder();
        for (CmWebCCProxy cmWebCCProxy : cmWebCCProxyMap.values()) {
            cmWebCCProxy.checkStatus();
            sb.append(cmWebCCProxy);
            sb.append("\n");
        }
        return sb.toString();
    }

    private CmWebCCProxy createWebCCProxy(Long cmcId, String ip, Integer proxyPort) {
        /*
                Username: admin
                Password: *****
                cc247> en
                cc247# con
                cc247# configure
                cc247# configure terminal
                cc247(config)# su
                Password:
                cc247(config-super)#shell


                BusyBox v1.17.2 (2017-05-17 18:54:43 CST) built-in shell (ash)
                Enter 'help' for a list of built-in commands.

                # ssh -g -D 7000 admin@172.17.2.148
                Could not create directory '/.ssh'.
                The authenticity of host '172.17.2.148 (172.17.2.148)' can't be established.
                RSA key fingerprint is SHA256:Tob0dbzja7G7gmlCZXhO7AZb7Lw41pdAwvb1i7NT1Dk.
                Are you sure you want to continue connecting (yes/no)? yes
                admin@172.17.2.148's password:

                ****************************************************************
                *                                                              *
                *  Topvision software system.                                  *
                *  Copyright 2010-2017,All rights Reserved by Topvision.       *
                *                                                              *
                ****************************************************************
                cc247> en
                cc247# admin
                % Unknown command.
                cc247# configure terminal
                cc247(config)# su
                Password:
                cc247(config-super)#shell


                BusyBox v1.17.2 (2017-05-17 18:54:43 CST) built-in shell (ash)
                Enter 'help' for a list of built-in commands.

                # netstat -nap| grep 7000
                tcp        0      0 0.0.0.0:7000            0.0.0.0:*               LISTEN      10107/ssh
                tcp        0      0 :::7000                 :::*                    LISTEN      10107/ssh
                #
         */
        CmWebCCProxy cmWebCCProxy = makeCmWebCCProxy(cmcId,ip,proxyPort);
        TelnetLogin telnetLogin;
        telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
        if (telnetLogin == null) {
            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        }
        CcmtsTelnetUtil ccmtsTelnetUtil = null;
        try {
            ccmtsTelnetUtil =  (CcmtsTelnetUtil)telnetUtilFactory.getCcmtsTelnetUtil(ip);
            ccmtsTelnetUtil.setPrompt("#,>,:,[n],(yes/no)?", "");
            ccmtsTelnetUtil.connect(ip);
            if (ccmtsTelnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),telnetLogin.getIsAAA())) {
                cmWebCCProxy.setLoginStatus(true);
                String host = telnetLogin.getUserName() + "@" + ip;
                StringBuilder re = new StringBuilder();
                re.append(ccmtsTelnetUtil.execCmd("config terminal"));
                re.append(ccmtsTelnetUtil.execCmd("super"));
                re.append(ccmtsTelnetUtil.execCmd("8ik,(OL>"));
                re.append(ccmtsTelnetUtil.execCmd("shell"));
                re.append(ccmtsTelnetUtil.execCmd("ssh -g -D " + proxyPort + " " + host));
                re.append(ccmtsTelnetUtil.execCmd("yes"));
                re.append(ccmtsTelnetUtil.execCmd(telnetLogin.getPassword()));
                re.append(ccmtsTelnetUtil.execCmd("enable"));
                re.append(ccmtsTelnetUtil.execCmd("config terminal"));
                re.append(ccmtsTelnetUtil.execCmd("super"));
                re.append(ccmtsTelnetUtil.execCmd("8ik,(OL>"));
                re.append(ccmtsTelnetUtil.execCmd("shell"));
                logger.debug("createWebCCProxy{}:{}={}" ,ip,proxyPort,re );
                String tmp = re.toString();
                if (tmp.indexOf("(config)#") != -1
                        && tmp.indexOf("(config-super)#") != -1
                        && tmp.indexOf(host) != -1) {
                    logger.debug("createWebCCProxy[{}:{}] success.", ip, proxyPort);
                    cmWebCCProxy.setTelnetUtil(ccmtsTelnetUtil);
                    cmWebCCProxy.checkStatus();
                    return cmWebCCProxy;
                } else {
                    logger.debug("createWebCCProxy[{}:{}] faild.", ip, proxyPort);
                    telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                    cmWebCCProxy.setStatus(false);
                    return cmWebCCProxy;
                }
            } else {
                logger.info("Telnet error createWebCCProxy[{}]",ip);
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                cmWebCCProxy.setLoginStatus(false);
                cmWebCCProxy.setStatus(false);
                return cmWebCCProxy;
            }
        } catch (Exception e) {
            logger.info("IP is not reachable  createWebCCProxy[{}]",ip);
            if (ccmtsTelnetUtil != null) {
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
            }
            cmWebCCProxy.setLoginStatus(false);
            cmWebCCProxy.setStatus(false);
            return cmWebCCProxy;
        }
    }

    private CmWebCCProxy makeCmWebCCProxy(Long cmcId, String ip, Integer proxyPort) {
        CmWebCCProxy cmWebCCProxy = new CmWebCCProxy();
        cmWebCCProxy.setCmcId(cmcId);
        cmWebCCProxy.setMangerIp(ip);
        cmWebCCProxy.setProxyPort(proxyPort);
        return cmWebCCProxy;
    }
}
