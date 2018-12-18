/***********************************************************************
 * AutoSendConfigServiceImpl.java,v1.0 17-5-26 下午5:15 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.autosendconfig.service.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.autosendconfig.service.AutoSendConfigService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.network.service.CommandSendService;
import com.topvision.ems.network.service.CommonConfigService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created 17-5-26 下午5:15
 */
@Service("autoSendConfigService")
public class AutoSendConfigServiceImpl extends CmcBaseCommonService implements AutoSendConfigService,
        CmcSynchronizedListener {
    private Logger logger = LoggerFactory.getLogger(AutoSendConfigServiceImpl.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private CommandSendService commandSendService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CommonConfigService commonConfigService;

    private ExecutorService executorService;
    @Value("${AutoSendConfig.poolSize:10}")
    private Integer poolSize;

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
        ThreadFactory threadFactory = new ThreadFactory() {
            private ThreadGroup threadGroup = new ThreadGroup("AutoAddEntityToSendConfig");

            public Thread newThread(Runnable r) {
                return new Thread(threadGroup, r);
            }
        };
        this.executorService = Executors.newFixedThreadPool(poolSize, threadFactory);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        logger.debug("AutoSendConfigServiceImpl insertEntityStates [{},{},{},{}]", event.getEntityId(),
                event.getEntityType(), event.getCmcIndexList(), event.getIpAddress());
        if (entityTypeService.isCcmtsWithAgent(event.getEntityType())) {
            AutoAddSendConfigTask autoAddSendConfigTask = new AutoAddSendConfigTask(event.getEntityId(),
                    commandSendService, commonConfigService);
            executorService.submit(autoAddSendConfigTask);
        }
    }

    /**
     *
     * Auto Refresh Task
     *
     * @created @2014-10-16-上午9:39:24
     *
     */
    private class AutoAddSendConfigTask implements Callable<Long> {
        private Long entityId;
        private CommandSendService commandSendService;
        private CommonConfigService commonConfigService;

        public AutoAddSendConfigTask(Long entityId, CommandSendService commandSendService,
                CommonConfigService commonConfigService) {
            this.entityId = entityId;
            this.commandSendService = commandSendService;
            this.commonConfigService = commonConfigService;
        }

        @Override
        public Long call() throws Exception {
            Boolean autoSendConfigSwitch = commonConfigService.loadAutoSendConfigSwitch();
            if (autoSendConfigSwitch) {
                commandSendService.addAutoSendConfigEntity(entityId);
            }
            return null;
        }

    }

}
