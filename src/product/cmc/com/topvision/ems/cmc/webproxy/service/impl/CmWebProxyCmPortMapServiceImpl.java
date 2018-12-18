/***********************************************************************
 * CmWebProxyCmPortMapServiceImpl.java,v1.0 17-4-25 上午11:25 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.service.impl;


import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.webproxy.domain.CmWebPortMap;
import com.topvision.ems.cmc.webproxy.nat.PortProxy;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyCmPortMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jay
 * @created 17-4-25 上午11:25
 */
@Service("cmWebProxyCmPortMapService")
public class CmWebProxyCmPortMapServiceImpl extends CmcBaseCommonService implements CmWebProxyCmPortMapService {
    public static Map<Long,PortProxy> portProxyMap = Collections.synchronizedMap(new HashMap<Long,PortProxy>());
    @Autowired
    private CmService cmService;

    @Value("${webProxy.cmWebPort:80}")
    private Integer cmWebPort;
    private Object checkAndRemoveAndModify = new Object();

    @Override
    public void start() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        for (Long cmcId : portProxyMap.keySet()) {
                            PortProxy portProxy = portProxyMap.get(cmcId);
                            portProxy.checkStatus();
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
    public CmWebPortMap selectPortByCmId(Long cmId) {
        PortProxy portProxy = portProxyMap.get(cmId);
        CmWebPortMap cmWebPortMap;
        if (portProxy == null) {
            cmWebPortMap = new CmWebPortMap();
            cmWebPortMap.setStatus(false);
        } else {
            cmWebPortMap = portProxy.getPortMap();
        }
        return cmWebPortMap;
    }

    @Override
    public CmWebPortMap pickPortByCmId(Long heartbeatId, Long cmId,String natIp, String mangerIp, Integer proxyPort) {
        PortProxy portProxy = portProxyMap.get(cmId);
        if (portProxy == null) {
            portProxy = createPortProxy(cmId,natIp, mangerIp, proxyPort);
            portProxyMap.put(cmId,portProxy);
        }
        portProxy.checkStatus();
        portProxy.addHeartbeatId(heartbeatId);
        return portProxy.getPortMap();
    }

    private PortProxy createPortProxy(Long cmId, String natIp, String mangerIp, Integer proxyPort) {
        CmAttribute cmAttribute = cmService.getCmAttributeByCmId(cmId);
        String ip = cmAttribute.getStatusIpAddress();
        if (ip == null || ip.equalsIgnoreCase("") || ip.equalsIgnoreCase("noSuchObject")) {
            ip = cmAttribute.getStatusInetAddress();
        }
        CmWebPortMap portMap = new CmWebPortMap(0,natIp,ip,cmWebPort,mangerIp,proxyPort);
        portMap.setCmId(cmId);
        PortProxy portProxy = new PortProxy(portMap);
        portProxy.startServer();
        return portProxy;
    }

    @Override
    public void releasePortByCmId(Long heartbeatId, Long cmId) {
        try {
            PortProxy portProxy = portProxyMap.get(cmId);
            if (portProxy != null) {
                portProxy.releaseHeartbeatId(heartbeatId);
                if (portProxy.heartbeatNum() == 0) {
                    portProxy.stopPortProxy();
                    portProxyMap.remove(cmId);
                    logger.debug("releasePortByCmId[{}]",cmId);
                }
            }
        } catch (Exception e) {
        	logger.debug("",e);
        }
    }

    @Override
    public String listInfo() {
        StringBuilder sb = new StringBuilder();
        for (PortProxy portProxy : portProxyMap.values()) {
            portProxy.checkStatus();
            sb.append(portProxy);
            sb.append("<br>");
        }
        return sb.toString();
    }
}
