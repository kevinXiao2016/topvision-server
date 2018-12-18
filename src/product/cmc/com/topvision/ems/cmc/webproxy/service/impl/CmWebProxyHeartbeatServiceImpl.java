/***********************************************************************
 * CmWebProxyHeartbeatServiceImpl.java,v1.0 17-4-25 上午11:30 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.service.impl;


import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.webproxy.domain.CmWebCCProxy;
import com.topvision.ems.cmc.webproxy.domain.CmWebPortMap;
import com.topvision.ems.cmc.webproxy.domain.CmWebStatus;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyCCProxyService;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyCmPortMapService;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyConfigService;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyHeartbeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author jay
 * @created 17-4-25 上午11:30
 */
@Service("cmWebProxyHeartbeatService")
public class CmWebProxyHeartbeatServiceImpl extends CmcBaseCommonService implements CmWebProxyHeartbeatService {
    private Logger logger = LoggerFactory.getLogger(CmWebProxyHeartbeatServiceImpl.class);
    @Autowired
    private CmService cmService;
    @Autowired
    private CmWebProxyCmPortMapService cmWebProxyCmPortMapService;
    @Autowired
    private CmWebProxyCCProxyService cmWebProxyCCProxyService;
    @Autowired
    private CmWebProxyConfigService cmWebProxyConfigService;
    @Value("${CmWebProxy.heartbeatTimeout:15000}")
    private Long heartbeatTimeout;

    private Long nextHeartbeatId = 1L;
    private Map<Long,CmWebStatus> heartbeatMap = Collections.synchronizedMap(new HashMap<Long, CmWebStatus>());
    private Object checkAndRemoveAndModify = new Object();

    @Override
    public void start() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    synchronized (checkAndRemoveAndModify) {
                        try {
                            Long now = System.currentTimeMillis();
                            for (Iterator<Long> iterator = heartbeatMap.keySet().iterator(); iterator.hasNext(); ) {
                                Long heartbeatId = iterator.next();
                                CmWebStatus cmWebStatus = heartbeatMap.get(heartbeatId);
                                logger.debug("HeartBeat checking " + cmWebStatus);
                                //超过时间没有心跳包过滤的需需要从心跳队列中删除
                                if ((now - cmWebStatus.getHeartbeatTime()) >= heartbeatTimeout) {
                                    logger.debug("HeartBeat [" + heartbeatId + "] timeout ");
                                    cmWebProxyCmPortMapService.releasePortByCmId(heartbeatId, cmWebStatus.getCmId());
                                    cmWebProxyCCProxyService.releaseCCProxyByCmcId(heartbeatId, cmWebStatus.getCmcId());
                                    iterator.remove();
                                }
                            }
                        } catch (Exception e) {
                            logger.error("HeartBeatJob error", e);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    public CmWebStatus addHeartbeat(Long cmId) {
        CmAttribute cmAttribute = cmService.getCmAttributeByCmId(cmId);
        if (cmAttribute == null) {
            logger.debug("cmAttribute is null");
            CmWebStatus cmWebStatus = new CmWebStatus();
            cmWebStatus.setCmId(cmId);
            cmWebStatus.setHeartbeatTime(System.currentTimeMillis());
            cmWebStatus.setHeartbeatStatus(false);
            return cmWebStatus;
        }
        Long heartbeatId = getNextHeartbeatId();
        CmWebStatus cmWebStatus = new CmWebStatus();
        cmWebStatus.setHeartbeatId(heartbeatId);
        cmWebStatus.setCmId(cmId);
        cmWebStatus.setHeartbeatTime(System.currentTimeMillis());
        cmWebStatus.setHeartbeatStatus(true);
        heartbeatMap.put(heartbeatId,cmWebStatus);
        return cmWebStatus;
    }

    @Override
    public CmWebStatus pickCCProxyByCmcId(Long heartbeatId, Long cmId) {
        CmWebStatus cmWebStatus = heartbeatMap.get(heartbeatId);
        CmAttribute cmAttribute = cmService.getCmAttributeByCmId(cmId);
        if (cmWebStatus == null || cmAttribute == null) {
            logger.debug("pickCCProxyByCmcId param is null[{},{}]",cmWebStatus,cmAttribute);
            CmWebStatus status = new CmWebStatus();
            status.setCmId(cmId);
            status.setHeartbeatTime(System.currentTimeMillis());
            status.setHeartbeatStatus(false);
            return status;
        }
        CmWebCCProxy cmWebCCProxy = cmWebProxyCCProxyService.pickCCProxyByCmcId(heartbeatId,cmAttribute.getCmcId());
        if (!cmWebCCProxy.getStatus()) {
            logger.debug("cmWebCCProxy.getStatus is false");
            cmWebProxyCCProxyService.releaseCCProxyByCmcId(heartbeatId,cmAttribute.getCmcId());
            cmWebStatus.setLoginStatus(cmWebCCProxy.getLoginStatus());
            cmWebStatus.setCmcProxyStatus(cmWebCCProxy.getStatus());
            cmWebStatus.setHeartbeatTime(System.currentTimeMillis());
            cmWebStatus.setHeartbeatStatus(false);
        } else {
            cmWebStatus.setCmcId(cmWebCCProxy.getCmcId());
            cmWebStatus.setCmcIp(cmWebCCProxy.getMangerIp());
            cmWebStatus.setCmcPort(cmWebCCProxy.getProxyPort());
            cmWebStatus.setLoginStatus(cmWebCCProxy.getLoginStatus());
            cmWebStatus.setCmcProxyStatus(cmWebCCProxy.getStatus());
            cmWebStatus.setHeartbeatTime(System.currentTimeMillis());
            cmWebStatus.setHeartbeatStatus(true);
        }
        heartbeatMap.put(heartbeatId,cmWebStatus);
        return cmWebStatus;
    }

    @Override
    public CmWebStatus pickPortByCmId(Long heartbeatId, Long cmId, String manageIp, Integer proxyPort) {
        String natIp = cmWebProxyConfigService.loadNatServerIp();
        CmWebStatus cmWebStatus = heartbeatMap.get(heartbeatId);
        CmAttribute cmAttribute = cmService.getCmAttributeByCmId(cmId);
        if (cmWebStatus == null || cmAttribute == null || natIp == null || natIp.equalsIgnoreCase("") || manageIp == null || proxyPort == null) {
            logger.debug("pickPortByCmId param is null[{},{},{},{},{}]",cmWebStatus,cmAttribute,natIp,manageIp,proxyPort);
            CmWebStatus status = new CmWebStatus();
            status.setCmId(cmId);
            status.setHeartbeatTime(System.currentTimeMillis());
            status.setHeartbeatStatus(false);
            return status;
        }
        CmWebPortMap cmWebPortMap = cmWebProxyCmPortMapService.pickPortByCmId(heartbeatId,cmId,natIp,manageIp,proxyPort);
        if (!cmWebPortMap.getStatus()) {
            logger.debug("cmWebPortMap.getStatus is false");
            cmWebProxyCmPortMapService.releasePortByCmId(heartbeatId, cmId);
            cmWebProxyCCProxyService.releaseCCProxyByCmcId(heartbeatId, cmAttribute.getCmcId());
            cmWebStatus.setCmPortMapStatus(cmWebPortMap.getStatus());
            cmWebStatus.setHeartbeatTime(System.currentTimeMillis());
            cmWebStatus.setHeartbeatStatus(false);
        } else {
            cmWebStatus.setCmIp(cmWebPortMap.getIp());
            cmWebStatus.setNatIp(cmWebPortMap.getNatIp());
            cmWebStatus.setNm3000Port(cmWebPortMap.getForwoard());
            cmWebStatus.setCmPortMapStatus(cmWebPortMap.getStatus());
            cmWebStatus.setHeartbeatTime(System.currentTimeMillis());
            cmWebStatus.setHeartbeatStatus(true);
        }
        heartbeatMap.put(heartbeatId,cmWebStatus);
        return cmWebStatus;
    }

    @Override
    public void deleteHeartbeat(Long heartbeatId) {
        synchronized (checkAndRemoveAndModify) {
            CmWebStatus cmWebStatus = heartbeatMap.get(heartbeatId);
            if (cmWebStatus.getCmcId() != null) {
                cmWebProxyCCProxyService.releaseCCProxyByCmcId(heartbeatId, cmWebStatus.getCmcId());
            }
            if (cmWebStatus.getCmId() == null) {
                cmWebProxyCmPortMapService.releasePortByCmId(heartbeatId,cmWebStatus.getCmId());
            }
            heartbeatMap.remove(heartbeatId);
        }
    }

    @Override
    public CmWebStatus heartbeat(Long heartbeatId) {
        CmWebStatus cmWebStatus = heartbeatMap.get(heartbeatId);
        if (cmWebStatus == null) {
            cmWebStatus = new CmWebStatus();
            cmWebStatus.setHeartbeatTime(System.currentTimeMillis());
            cmWebStatus.setHeartbeatStatus(false);
        } else {
            if (cmWebStatus.getCmcId() != null) {
                CmWebCCProxy cmWebCCProxy = cmWebProxyCCProxyService.selectCCProxyByCmcId(cmWebStatus.getCmcId());
                cmWebStatus.setCmcProxyStatus(cmWebCCProxy.getStatus());
            }
            if (cmWebStatus.getCmId() != null) {
                CmWebPortMap cmWebPortMap = cmWebProxyCmPortMapService.selectPortByCmId(cmWebStatus.getCmId());
                cmWebStatus.setCmPortMapStatus(cmWebPortMap.getStatus());
            }
            cmWebStatus.setHeartbeatTime(System.currentTimeMillis());
            cmWebStatus.setHeartbeatStatus(true);
            heartbeatMap.put(heartbeatId,cmWebStatus);
        }
        return cmWebStatus;
    }

    @Override
    public CmWebStatus selectPortByCmId(Long heartbeatId, Long cmId) {
        String natIp = cmWebProxyConfigService.loadNatServerIp();
        CmWebStatus cmWebStatus = heartbeatMap.get(heartbeatId);
        if (cmWebStatus == null || natIp == null || natIp.equalsIgnoreCase("")) {
            logger.debug("selectPortByCmId param is null[{},{}]",cmWebStatus,natIp);
            CmWebStatus status = new CmWebStatus();
            status.setCmId(cmId);
            status.setHeartbeatTime(System.currentTimeMillis());
            status.setHeartbeatStatus(false);
            return status;
        }
        if (cmWebStatus.getCmId() != null) {
            CmWebPortMap cmWebPortMap = cmWebProxyCmPortMapService.selectPortByCmId(cmWebStatus.getCmId());
            cmWebStatus.setCmPortMapStatus(cmWebPortMap.getStatus());
        } else {
            cmWebStatus.setCmPortMapStatus(false);
        }
        return cmWebStatus;
    }

    @Override
    public CmWebStatus selectCCProxyByCmcId(Long heartbeatId, Long cmcId) {
        CmWebStatus cmWebStatus = heartbeatMap.get(heartbeatId);
        if (cmWebStatus == null) {
            logger.debug("selectPortByCmId param is null[{},{},{}]",cmWebStatus,heartbeatId,cmcId);
            CmWebStatus status = new CmWebStatus();
            status.setCmcId(cmcId);
            status.setHeartbeatTime(System.currentTimeMillis());
            status.setHeartbeatStatus(false);
            return status;
        }
        if (cmWebStatus.getCmId() != null) {
            CmWebCCProxy cmWebCCProxy = cmWebProxyCCProxyService.selectCCProxyByCmcId(cmWebStatus.getCmId());
            cmWebStatus.setCmcProxyStatus(cmWebCCProxy.getStatus());
        } else {
            cmWebStatus.setCmcProxyStatus(false);
        }
        return cmWebStatus;
    }

    @Override
    public String listInfo() {
        StringBuilder sb = new StringBuilder();
        for (CmWebStatus cmWebStatus : heartbeatMap.values()) {
            sb.append(cmWebStatus);
            sb.append("<br>");
        }
        return sb.toString();
    }

    public synchronized Long getNextHeartbeatId() {
        return nextHeartbeatId++;
    }
}
