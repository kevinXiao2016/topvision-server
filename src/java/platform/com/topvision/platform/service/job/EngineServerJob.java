/***********************************************************************
 * $Id: EngineServerJob.java,v1.0 2013-12-6 下午2:35:03 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.dao.DataAccessException;

import com.topvision.ems.enginemgr.EngineManage;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.platform.SystemVersion;
import com.topvision.platform.dao.EngineServerDao;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EngineServerEvent;
import com.topvision.platform.service.EngineServerService;

/**
 * @author Victor
 * @created @2013-12-6-下午2:35:03
 *
 */
public class EngineServerJob extends AbstractJob {

    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        @SuppressWarnings("unchecked")
        Map<Integer, EngineServer> engineMapping = (Map<Integer, EngineServer>) jobDataMap.get("engineMapping");
        FacadeFactory facadeFactory = (FacadeFactory) jobDataMap.get("facadeFactory");
        EngineServerDao engineServerDao = (EngineServerDao) jobDataMap.get("engineServerDao");
        EngineServerService engineServerService = (EngineServerService) jobDataMap.get("engineServerService");
        Map<String, List<EngineServer>> engineMap = new HashMap<>();
        for (EngineServer engine : engineMapping.values()) {
            if (engineMap.containsKey(engine.getIp())) {
                engineMap.get(engine.getIp()).add(engine);
            } else {
                List<EngineServer> list = new ArrayList<>();
                list.add(engine);
                engineMap.put(engine.getIp(), list);
            }
        }

        for (Entry<String, List<EngineServer>> entry : engineMap.entrySet()) {
            // Check Engine Manage
            String manageIp = entry.getKey();
            List<EngineServer> engineServers = entry.getValue();
            if (manageIp.equals("127.0.0.1")) {
                continue;
            }
            EngineManage engineManage = facadeFactory.getEngineManage(manageIp);
            if (engineManage == null) {
                for (EngineServer engine : engineServers) {
                    engine.setManageStatus(EngineServer.DISCONNECT);
                }
                engineServerDao.updateManageStatus(manageIp, EngineServer.DISCONNECT);
                logger.info("Check engineManage:The {} disconnected", manageIp);
            } else {
                for (EngineServer engine : engineServers) {
                    engine.setManageStatus(EngineServer.CONNECTED);
                }
                engineServerDao.updateManageStatus(manageIp, EngineServer.CONNECTED);
                logger.info("Check engineManage:The {} connected", manageIp);
            }
            // Check Engine Collect
            for (EngineServer engine : engineServers) {
                if (engine.getAdminStatus() == EngineServer.STOP) {
                    continue;
                }
                try {
                    byte old = engine.getLinkStatus();
                    //engine.setLinkStatus(EngineServer.CONNECTING);
                    // todo
                    try {
                        CheckFacade facade = facadeFactory.getCheckFacade(engine);
                        if (facade == null) {
                            engine.setLinkStatus(EngineServer.DISCONNECT);
                            logger.info("Check engine:The {} disconnected", engine.getName());
                            if (engineManage != null) {
                                String serverVersion = new SystemVersion().getBuildVersion();
                                if (engine.getVersion() == null || !engine.getVersion().equals(serverVersion)) {
                                    engineServerService.upgradeEngine(engine, engineManage);
                                    engineServerService.updateEngineVersion(engine.getId(), serverVersion);
                                }
                                // Start Remote Engine
                                engineManage.reStartEngine(engine.getPort().toString());
                                engineServerService.initEngine(engine);
                            }
                        } else {
                            engine.setLinkStatus(EngineServer.CONNECTED);
                        }
                    } catch (Exception ex) {
                        // ignore
                        engine.setLinkStatus(EngineServer.DISCONNECT);
                        logger.info("Check engine:{}'s status failed:{}", engine.getName(), ex);
                    } finally {
                        if (engine.getLinkStatus() != old) {
                            engineServerDao.updateLinkStatus(engine);
                            engineMapping.put(engine.getId(), engine);
                            if (engine.getLinkStatus() == EngineServer.DISCONNECT) {
                                engineServerService.sendStatusChangeMsg(engine, EngineServerEvent.STATUS_DISCONNECTED);
                            } else if (engine.getLinkStatus() == EngineServer.CONNECTED) {
                                engineServerService.sendStatusChangeMsg(engine, EngineServerEvent.STATUS_CONNECTED);
                            }
                        }
                    }
                } catch (DataAccessException e) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e1) {
                    }
                }
            }
        }
    }
}
