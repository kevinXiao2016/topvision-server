package com.topvision.ems.network.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.domain.FolderRelation;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.ems.network.domain.SendConfigEntityObject;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.CommonConfigService;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.network.util.FolderRelationUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.thread.Poolable;

/**
 * Created by jay on 17-4-21.
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service("commandSendPoolable")
public class CommandSendPoolable extends Poolable<SendConfigEntityObject> implements BeanFactoryAware {
    @Autowired
    private TelnetLoginService telnetLoginService;// 用户名密码读取
    @Autowired
    private TelnetUtilFactory telnetUtilFactory;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CommonConfigService commonConfigService;
    @Autowired
    private TopologyService topologyService;

    @Value("${batchCommandSend.maxPollSize}")
    private Integer maxPollSize;

    private BeanFactory beanFactory;

    public CommandSendPoolable() {
        super();
        setName("CommandSendPoolable-" + getId());
    }

    @Override
    protected void process() {
        SendConfigEntityObject sendConfigEntityObject = getArgument();
        logger.info("Start CommandSendPoolable config:" + sendConfigEntityObject.toString());
        try {
            TelnetLogin telnetLogin = telnetLoginService.txGetTelnetLoginConfigByIp(sendConfigEntityObject.getIp());
            if (telnetLogin == null) {
                telnetLogin = telnetLoginService.txGetGlobalTelnetLogin();
            }
            List<SendConfigEntity> sendConfigEntitys = sendConfigEntityObject.getSendConfigEntitys();

            /**
             * start [EMS-14880] 处在多地域下的设备下发地域优先级高的配置
             */
            Map<Long, List<String>> configsMap = new HashMap<Long, List<String>>();
            for (SendConfigEntity sendConfigEntity : sendConfigEntitys) {
                List<FolderRelation> folderRelations = topologyService.getFolderRelationByEntityId(sendConfigEntity
                        .getEntityId());
                if (folderRelations == null || folderRelations.size() == 0) {
                    continue;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("the entityId is [{}]", sendConfigEntity.getEntityId());
                }
                Long folderId = FolderRelationUtil.getHighestPriorityFolder(folderRelations).getFolderId();
                List<String> configs = new ArrayList<String>();
                if (entityTypeService.isCcmtsWithAgent(sendConfigEntity.getTypeId())) {
                    configs = commonConfigService.txGetConfigs(30000L, folderId);
                } else if (entityTypeService.isCcmtsWithoutAgent(sendConfigEntity.getTypeId())) {
                    configs = commonConfigService.txGetConfigs(330000L, folderId);
                } else if (entityTypeService.isOnu(sendConfigEntity.getTypeId())) {
                    configs = commonConfigService.txGetConfigs(13000L, folderId);
                } else {
                    configs = commonConfigService.txGetConfigs(10000L, folderId);
                }
                configsMap.put(sendConfigEntity.getEntityId(), configs);
            }
            /**
             * end
             */

            if (!sendConfigEntitys.isEmpty()) {
                CommandSendWorker commandSendWorker;
                if (entityTypeService.isCcmtsWithAgent(sendConfigEntitys.get(0).getTypeId())) {
                    logger.info("process.CmtsCommandSendWorkerB");
                    commandSendWorker = new CmtsCommandSendWorkerB();
                } else {
                    logger.info("process.OltCommandSendWorker");
                    commandSendWorker = new OltCommandSendWorker();
                }
                commandSendWorker.setTelnetLogin(telnetLogin);
                commandSendWorker.setSendCommandInterval(commonConfigService.getSendCommandInterval());
                commandSendWorker.setSendConfigEntityObject(sendConfigEntityObject);
                commandSendWorker.setBeanFactory(beanFactory);
                commandSendWorker.setConfigsMap(configsMap);
                commandSendWorker.process();
            } else {
                logger.info("sendConfigEntitys.isEmpty()");
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
