/***********************************************************************
 * $Id: CommandSendServiceImpl.java,v1.0 2014年7月17日 下午4:04:10 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.CommandSendDao;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.ems.network.domain.SendConfigEntityObject;
import com.topvision.ems.network.domain.SendConfigResult;
import com.topvision.ems.network.service.CommandSendService;
import com.topvision.ems.network.service.CommonConfigService;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.thread.ThreadPoolManager;
import com.topvision.platform.service.SchedulerService;

/**
 * @author loyal
 * @created @2014年7月17日-下午4:04:10
 * 
 */
@Service("commandSendService")
public class CommandSendServiceImpl extends BaseService implements CommandSendService {
    @Autowired
    private CommandSendDao commandSendDao;// 未完成下发设备列表读取 下发状态修改
    @Autowired
    private CommonConfigService commonConfigService;// 读取配置文件 下发参数
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private BeanFactory beanFactory;
    @Value("${batchCommandSend.maxPollSize}")
    private Integer maxPollSize;
    private JobDetail makeSendConfigArrayJob = null;
    private Long sendCommandInterval = 500L;

    private ThreadPoolManager<SendConfigEntityObject> commandSendThreadPoolManager;
    private ArrayBlockingQueue<SendConfigEntityObject> autoSendConfigArray = new ArrayBlockingQueue<SendConfigEntityObject>(
            10000);

    public static Object makeArrayFlag = new Object();

    @Override
    public void start() {
        new Thread() {
            public void run() {
                Long pollInterval = commonConfigService.getPollInterval();
                restartMakeSendConfigArrayJob(pollInterval);
                sendCommandInterval = commonConfigService.getSendCommandInterval();
                commandSendThreadPoolManager = (ThreadPoolManager<SendConfigEntityObject>) beanFactory
                        .getBean("threadPoolManager");
                commandSendThreadPoolManager.setThreadBeanName("commandSendPoolable");
                commandSendThreadPoolManager.setThreadCount(maxPollSize);
                commandSendThreadPoolManager.initialize();
                SendConfigEntityObject sendConfigEntityObject = null;
                while (true) {
                    try {
                        sendConfigEntityObject = autoSendConfigArray.take();
                        logger.info("Start send common config:" + sendConfigEntityObject.toString());
                    } catch (Exception e) {
                        logger.debug("", e);
                    }
                    if (sendConfigEntityObject != null) {
                        boolean b;
                        while (true) {
                            b = commandSendThreadPoolManager.process(sendConfigEntityObject);
                            if (!b) {
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public List<Entity> loadEntityList(Map<String, Object> map, int start, int limit) {
        return commandSendDao.selectEntityList(map, start, limit);
    }

    @Override
    public Long loadEntityListNum(Map<String, Object> map) {
        return commandSendDao.selectEntityListNum(map);
    }

    @Override
    public void makeSendConfigArray() {
        commonMakeArray(commandSendDao.getUncompleteEntitys());
    }

    @Override
    public void makeFailedSendConfigArray() {
        commonMakeArray(commandSendDao.getFailedEntitys());
    }

    @Override
    public void makeUnstartSendConfigArray() {
        commonMakeArray(commandSendDao.getUnstartEntitys());
    }

    private void commonMakeArray(List<SendConfigEntity> sendConfigEntities) {
        logger.info("MakeSendConfigArray Start--------------");
        synchronized (makeArrayFlag) {
            Map<Long, SendConfigEntityObject> ipEntityMap = new HashMap<Long, SendConfigEntityObject>();
            for (SendConfigEntity sendConfigEntity : sendConfigEntities) {
                if (ipEntityMap.containsKey(sendConfigEntity.getIp())) {
                    SendConfigEntityObject sendConfigEntityObject = ipEntityMap.get(sendConfigEntity.getIp());
                    sendConfigEntityObject.addSendConfigEntity(sendConfigEntity);
                } else {
                    SendConfigEntityObject sendConfigEntityObject = new SendConfigEntityObject(sendConfigEntity.getIp());
                    sendConfigEntityObject.addSendConfigEntity(sendConfigEntity);
                    ipEntityMap.put(sendConfigEntity.getIp(), sendConfigEntityObject);
                }
            }

            for (Long ip : ipEntityMap.keySet()) {
                SendConfigEntityObject sendConfigEntityObject = ipEntityMap.get(ip);
                if (!autoSendConfigArray.contains(sendConfigEntityObject)) {
                    autoSendConfigArray.add(sendConfigEntityObject);
                }
            }

            logger.info("MakeSendConfigArray End --------------Entity Number = " + autoSendConfigArray.size());
        }
    }

    @Override
    public void recordSendConfigResult(SendConfigResult sendConfigResult) {
        commandSendDao.updateSendConfigEntity(sendConfigResult);
    }

    @Override
    public void restartMakeSendConfigArrayJob(Long interval) {
        if (makeSendConfigArrayJob != null) {
            try {
                schedulerService.deleteJob(makeSendConfigArrayJob.getKey());
            } catch (SchedulerException e) {
                logger.error("Stop Send Config Array Job:", e);
                throw new NetworkException("Stop Send Config Array Job:", e);
            }
        }
        final Integer pollInterval = (int) (interval / 1000);
        try {
            makeSendConfigArrayJob = newJob(MakeSendConfigArrayJob.class).withIdentity("MakeSendConfigArrayJob",
                    "Default").build();
            makeSendConfigArrayJob.getJobDataMap().put("commandSendService", this);
            makeSendConfigArrayJob.getJobDataMap().put("commonConfigService", commonConfigService);

            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(
                    makeSendConfigArrayJob.getKey().getName(), makeSendConfigArrayJob.getKey().getGroup())
                    .withSchedule(repeatSecondlyForever(pollInterval));
            schedulerService.scheduleJob(makeSendConfigArrayJob, builder.build());
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public void modifySendCommandInterval(Long sendCommandInterval) {
        this.sendCommandInterval = sendCommandInterval;
    }

    @Override
    public void addSendConfigEntity(List<Long> entityIdList) {
        commandSendDao.insertSendConfigEntity(entityIdList);
    }

    @Override
    public void deleteSendConfigEntity(List<Long> entityIdList) {
        List<SendConfigEntity> sendConfigEntityList = new ArrayList<SendConfigEntity>();
        Map<Long, SendConfigEntityObject> removeIpEntityMap = new HashMap<Long, SendConfigEntityObject>();
        for (Long anEntityIdList : entityIdList) {
            SendConfigEntity sendConfigEntity = commandSendDao.selectCommandSendEntityByEntityId(anEntityIdList);
            sendConfigEntityList.add(sendConfigEntity);
        }
        commandSendDao.deleteSendConfigEntity(entityIdList);
        synchronized (makeArrayFlag) {
            for (SendConfigEntity sendConfigEntity : sendConfigEntityList) {
                if (removeIpEntityMap.containsKey(sendConfigEntity.getIp())) {
                    SendConfigEntityObject sendConfigEntityObject = removeIpEntityMap.get(sendConfigEntity.getIp());
                    sendConfigEntityObject.addSendConfigEntity(sendConfigEntity);
                } else {
                    SendConfigEntityObject sendConfigEntityObject = new SendConfigEntityObject(sendConfigEntity.getIp());
                    sendConfigEntityObject.addSendConfigEntity(sendConfigEntity);
                    removeIpEntityMap.put(sendConfigEntity.getIp(), sendConfigEntityObject);
                }
            }
            for (Iterator<SendConfigEntityObject> iterator = autoSendConfigArray.iterator(); iterator.hasNext();) {
                SendConfigEntityObject sendConfigEntityObject = iterator.next();
                if (removeIpEntityMap.containsKey(sendConfigEntityObject.getIp())) {
                    SendConfigEntityObject removeEntityObject = removeIpEntityMap.get(sendConfigEntityObject.getIp());
                    sendConfigEntityObject.removeEntity(removeEntityObject.getSendConfigEntitys());
                    if (sendConfigEntityObject.isEmpty()) {
                        iterator.remove();
                    }
                }

            }
        }
    }

    @Override
    public List<SendConfigEntity> getCommandSendEntityList(Map<String, Object> map, int start, int limit) {
        return commandSendDao.selectCommandSendEntityList(map, start, limit);
    }

    @Override
    public Long getCommandSendEntityListNum(Map<String, Object> map) {
        return commandSendDao.selectCommandSendEntityListNum(map);
    }

    @Override
    public List<String> getEntityFolder(Long entityId) {
        return commandSendDao.selectEntityFolder(entityId);
    }

    @Override
    public String getSendConfigResult(Long entityId) {
        return commandSendDao.selectSendConfigResult(entityId);
    }

    @Override
    public void addAutoSendConfigEntity(Long entityId) {
        logger.info("addAutoSendConfigEntity Start--------------");
        SendConfigEntity sendConfigEntity = commandSendDao.selectCommandSendEntityByEntityId(entityId);
        if (sendConfigEntity == null) {
            List<Long> entityIds = new ArrayList<>();
            entityIds.add(entityId);
            commandSendDao.insertSendConfigEntity(entityIds);
            sendConfigEntity = commandSendDao.selectCommandSendEntityByEntityId(entityId);
        }
        synchronized (makeArrayFlag) {
            SendConfigEntityObject sendConfigEntityObject = new SendConfigEntityObject(sendConfigEntity.getIp());
            sendConfigEntityObject.addSendConfigEntity(sendConfigEntity);
            if (!autoSendConfigArray.contains(sendConfigEntityObject)) {
                autoSendConfigArray.add(sendConfigEntityObject);
            }
        }
        logger.info("addAutoSendConfigEntity End --------------Entity Number = " + autoSendConfigArray.size());
    }

    public Integer getMaxPollSize() {
        return maxPollSize;
    }

    public void setMaxPollSize(Integer maxPollSize) {
        this.maxPollSize = maxPollSize;
    }

    public ArrayBlockingQueue<SendConfigEntityObject> getAutoSendConfigArray() {
        return autoSendConfigArray;
    }

    public void setAutoSendConfigArray(ArrayBlockingQueue<SendConfigEntityObject> autoSendConfigArray) {
        this.autoSendConfigArray = autoSendConfigArray;
    }
}
