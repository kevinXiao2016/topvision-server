/***********************************************************************
 * $Id: EmsApplicationListener.java,v1.0 2013-11-29 下午1:29:00 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.topvision.ems.enginemgr.EngineManage;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.platform.dao.EngineServerDao;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.CallbackAutoAware;
import com.topvision.platform.service.EngineServerService;
import com.topvision.platform.service.ThreadPoolService;

/**
 * @author Victor
 * @created @2013-11-29-下午1:29:00
 *
 */
@Service
public class EmsApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent evt) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                }
                logger.info("Spring bean init finished, start the task after startup.");
                ApplicationContext ctx = evt.getApplicationContext();
                // 初始化本地callback接口
                CallbackAutoAware callbackAutoAware = ctx.getBean(CallbackAutoAware.class);
                callbackAutoAware.export();
                // 初始化engine
                FacadeFactory facadeFactory = ctx.getBean(FacadeFactory.class);
                EngineServerDao engineServerDao = ctx.getBean(EngineServerDao.class);
                EngineServerService engineServerService = ctx.getBean(EngineServerService.class);
                List<EngineServer> engineServers = engineServerDao.selectByMap(null);
                
                for (EngineServer es : engineServers) {
                    if (es.getEngineId().equals("1")) {
                        // Default Engine
                        try {
                            logger.info("Starting the engine {} ...", es);
                            engineServerService.initEngine(es);
                            logger.info("The engine {} started!", es);
                        } catch (Exception e) {
                            logger.debug(es.toString(), e);
                            logger.info("The engine server {}:{} is not connected, will init after next connected",
                                    es.getIp(), es.getPort());
                        }
                    } else {
                        if (es.getAdminStatus() == EngineServer.STOP) {
                            continue;
                        }
                        EngineManage engineManage = engineServerService.getEngineManage(es.getIp());
                        if (engineManage != null) {
                            es.setManageStatus(EngineServer.CONNECTED);
                            try {
                                // Shutdown Engine
                                engineManage.shutdownEngine(es.getPort().toString());
                                // Upgrade Engine
                                String serverVersion = new SystemVersion().getBuildVersion();
                                if (es.getVersion() == null || !es.getVersion().equals(serverVersion)) {
                                    engineServerService.upgradeEngine(es, engineManage);
                                    engineServerDao.updateEngineVersion(es.getId(), serverVersion);
                                }
                                // Start Remote Engine
                                engineManage.reStartEngine(es.getPort().toString());
                                engineServerService.initEngine(es);
                                // Update EngineServer
                                es.setLinkStatus(EngineServer.CONNECTED);
                            } catch (Exception e) {
                                es.setLinkStatus(EngineServer.DISCONNECT);
                                logger.debug(es.toString(), e);
                                logger.info("The engine server {}:{} is not connected, will init after next connected",
                                        es.getIp(), es.getPort());
                            }
                        } else {
                            // 主动连接采集器关闭
                            try {
                                CheckFacade checkFacade = facadeFactory.getCheckFacade(es);
                                if (checkFacade != null) {
                                    checkFacade.shutDown();
                                }
                            } catch (Exception e) {
                                logger.info("EmsApplicationListener close collect", e);
                            }
                            es.setManageStatus(EngineServer.DISCONNECT);
                            es.setLinkStatus(EngineServer.DISCONNECT);
                        }
                        engineServerDao.updateManageStatus(es.getIp(), es.getManageStatus());
                        engineServerDao.updateLinkStatus(es);
                    }
                }
                // 更新用到engineServer缓存的对象 
                facadeFactory.updateEngineStatusBeforeStart();
                engineServerService.updateEngineStatusBeforeStart();

                // 启动其他服务
                ThreadPoolService threadPoolService = ctx.getBean(ThreadPoolService.class);
                String[] names = evt.getApplicationContext().getBeanDefinitionNames();
                for (String name : names) {
                    final Object o = evt.getApplicationContext().getBean(name);
                    if (o instanceof com.topvision.framework.service.Service) {
                        Runnable r = new Runnable() {
                            public void run() {
                                try {
                                    ((com.topvision.framework.service.Service) o).start();
                                } catch (Throwable e) {
                                    logger.debug("onApplicationEvent:{}", e.getMessage());
                                }
                            };
                        };
                        threadPoolService.execute(r);
                    }
                }
                logger.info("The task started.");
            }
        }.start();
    }

}
