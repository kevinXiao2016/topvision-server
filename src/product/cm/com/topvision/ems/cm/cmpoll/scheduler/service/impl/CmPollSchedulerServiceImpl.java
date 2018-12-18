/***********************************************************************
 * $ CmPollSchedulerServiceImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.scheduler.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.cmpoll.config.domain.CmPollCollectParam;
import com.topvision.ems.cm.cmpoll.config.service.CmPollConfigService;
import com.topvision.ems.cm.cmpoll.domain.DelayItem;
import com.topvision.ems.cm.cmpoll.facade.CmPollFacade;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollEndTask;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.ems.cm.cmpoll.message.CmPollStateEvent;
import com.topvision.ems.cm.cmpoll.message.CmPollStateListener;
import com.topvision.ems.cm.cmpoll.scheduler.domain.CmPollCollector;
import com.topvision.ems.cm.cmpoll.scheduler.service.CmPollSchedulerService;
import com.topvision.ems.facade.callback.CmPollCallback;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EngineServerEvent;
import com.topvision.platform.message.event.EngineServerListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Service("cmPollSchedulerService")
public class CmPollSchedulerServiceImpl extends BaseService
        implements CmPollSchedulerService, CmPollCallback, EngineServerListener {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Value("CMPoll")
    private String callbackService;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CmPollConfigService cmPollConfigService;
    private Long maxTaskId = 1L;

    /**
     * 采集器列表，记录每个采集器的状态信息，以便调度管理
     */
    private Map<Integer, CmPollCollector> cmPollCollectors = Collections
            .synchronizedMap(new HashMap<Integer, CmPollCollector>());

    private List<Integer> engineStates = Collections.synchronizedList(new ArrayList<Integer>());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    @Override
    public void initialize() {
        messageService.addListener(EngineServerListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
    }

    @Override
    public void start() {
        notifyEngineServerChange();
    }

    /***************************************** CmPollSchedulerService ****************************************/

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void appendTask(Integer engineId, Long time, List<CmPollTask> cmPollTasks) {
        if (logger.isTraceEnabled()) {
            logger.trace("engineId:" + engineId + " time:" + sdf.format(new Date(time)) + " task count:"
                    + cmPollTasks.size());
        }

        for (CmPollTask cmPollTask : cmPollTasks) {
            if (cmPollTask instanceof CmPollEndTask) {
                continue;
            }
            cmPollTask.setEngineId(engineId);
            cmPollTask.setTaskId(nextTaskId());
        }
        CmPollFacade cmPollFacade = getCmPollFacade(engineId);
        boolean b = cmPollFacade.appendTesk(time, cmPollTasks);
        // 如果engine端还没准备好则等待
        while (!b) {
            try {
                Thread.sleep(5000);
                logger.trace("Engine not prepared" + "----" + engineId + "----");
            } catch (InterruptedException e) {
            }
            b = cmPollFacade.appendTesk(time, cmPollTasks);
        }
        CmPollCollector cmPollCollector = cmPollCollectors.get(engineId);
        for (CmPollTask cmPollTask : cmPollTasks) {
            if (cmPollTask instanceof CmPollEndTask) {
                List<CmPollFacade> cmPollFacades = facadeFactory.getAllFacade(CmPollFacade.class);
                for (CmPollFacade pollFacade : cmPollFacades) {
                    pollFacade.appendEndTask(time, cmPollTask);
                }
                continue;
            }
            cmPollCollector.getTaskDlayQueue().add(new DelayItem(cmPollTask.getTaskId(), 5 * 60 * 1000L));
            cmPollCollector.addExecuteTaskCounts();
            cmPollCollector.addRoundTotalTaskCounts();
        }
    }

    @Override
    public void roundStart(Long time) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmPollSchedulerServiceImpl.roundStart" + sdf.format(new Date(time)));
        }
        List<CmPollFacade> cmPollFacades = facadeFactory.getAllFacade(CmPollFacade.class);
        for (CmPollFacade cmPollFacade : cmPollFacades) {
            cmPollFacade.roundStart(time);
        }
        logger.debug("CmPollSchedulerServiceImpl.roundStart end");
    }

    @Override
    public void roundFinished(Long time) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmPollSchedulerServiceImpl.roundFinished" + sdf.format(new Date(time)));
        }
        List<EngineServer> engineServers = facadeFactory.getEngineServerByType(EngineServer.TYPE_CM_POLL);
        for (EngineServer engineServer : engineServers) {
            engineStates.add(engineServer.getId());
        }

        CmPollStateEvent cmPollStateEvent = new CmPollStateEvent(time);
        cmPollStateEvent.setActionName("startRoundStatistics");
        cmPollStateEvent.setListener(CmPollStateListener.class);
        messageService.addMessage(cmPollStateEvent);
        logger.debug("CmPollSchedulerServiceImpl.roundFinished end");
    }

    @Override
    public Integer isAnyIdle() {
        if (cmPollCollectors.size() == 0) {
            return -1;
        }
        List<CmPollCollector> cpcs = new ArrayList<CmPollCollector>(cmPollCollectors.values());
        Collections.sort(cpcs);
        for (CmPollCollector cpc : cpcs) {
            if (cpc.getState() != CmPollCollector.OFFLINE && cpc.getIdleCounts() != 0) {
                return cpc.getEngineId();
            }
        }
        return -1;
    }

    @Override
    public Integer idleTaskCount(Integer engineId) {
        CmPollCollector cmPollCollector = cmPollCollectors.get(engineId);
        if (cmPollCollector == null) {
            return 0;
        } else {
            return cmPollCollector.getIdleCounts();
        }
    }

    /***************************************** CmPollCallback ****************************************/
    @Override
    public void connectTest() {
    }

    @Override
    public void completeTask(Integer engineId, Long taskId) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmPollSchedulerServiceImpl.completeTask engineId:" + engineId + " taskId:" + taskId);
        }
        if (cmPollCollectors.containsKey(engineId)) {
            CmPollCollector cmPollCollector = cmPollCollectors.get(engineId);
            cmPollCollector.delExecuteTaskCounts();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("CmPollSchedulerServiceImpl.completeTask cmPollCollector is not exist engineId:" + engineId
                        + " taskId:" + taskId);
            }
        }
        logger.debug("CmPollSchedulerServiceImpl.completeTask end");
    }

    @Override
    public void completeRoundStatistics(Integer engineId, Long time) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmPollSchedulerServiceImpl.completeRoundStatistics engineId:" + engineId + " time:"
                    + sdf.format(new Date(time)));
        }
        if (cmPollCollectors.containsKey(engineId)) {
            CmPollCollector cmPollCollector = cmPollCollectors.get(engineId);
            cmPollCollector.setNextRoundTotalTaskCounts(cmPollCollector.getRoundTotalTaskCounts());
            cmPollCollector.setRoundTotalTaskCounts(0);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("CmPollSchedulerServiceImpl.completeRoundStatistics cmPollCollector is not exist engineId:"
                        + engineId);
            }
        }
        removeEngine(engineId);
        logger.debug("CmPollSchedulerServiceImpl.completeRoundStatistics end");
    }

    @Override
    public List<CmPollCollector> getCmPollCollectorList() {
        Iterator<Entry<Integer, CmPollCollector>> it = this.cmPollCollectors.entrySet().iterator();
        List<CmPollCollector> cmPollCollectorList = new ArrayList<CmPollCollector>();
        while (it.hasNext()) {
            Entry<Integer, CmPollCollector> entry = it.next();
            cmPollCollectorList.add(entry.getValue());
        }
        return cmPollCollectorList;
    }

    public void notifyEngineServerChange() {
        CmPollCollectParam cmPollCollectParam = cmPollConfigService.getCmPollCollectParam();

        List<EngineServer> engineServers = facadeFactory.getEngineServerByType(EngineServer.TYPE_CM_POLL);
        for (EngineServer engineServer : engineServers) {
            CmPollCollector cmPollCollector = null;
            if (cmPollCollectors.containsKey(engineServer.getId())) {
                cmPollCollector = cmPollCollectors.get(engineServer.getId());
            } else {
                cmPollCollector = makeCmPollCollector(Integer.parseInt(engineServer.getEngineId()));
                cmPollCollector.setDesc(engineServer.getName());
                cmPollCollectors.put(engineServer.getId(), cmPollCollector);
            }
            if (engineServer.getLinkStatus() == EngineServer.CONNECTED
                    && cmPollCollector.getState() == CmPollCollector.OFFLINE) {
                cmPollCollector.setState(CmPollCollector.ONLINE);
                cmPollCollector.setMaxTaskCounts(cmPollCollectParam.getMaxPoolSize());
                CmPollFacade cmPollFacade = facadeFactory.getFacade(engineServer, CmPollFacade.class);
                cmPollFacade.initRunAug(cmPollCollectParam);
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "CmPollSchedulerServiceImpl.notifyEngineServerChange engineServer.getLinkStatus() == EngineServer.CONNECTED\n"
                                    + "&& cmPollCollector.getState() == CmPollCollector.OFFLINE");
                }
            } else if (engineServer.getLinkStatus() != EngineServer.CONNECTED
                    && cmPollCollector.getState() == CmPollCollector.ONLINE) {
                cmPollCollector.init();
                removeEngine(engineServer.getId());
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "CmPollSchedulerServiceImpl.notifyEngineServerChange engineServer.getLinkStatus() != EngineServer.CONNECTED\n"
                                    + "&& cmPollCollector.getState() == CmPollCollector.ONLINE");
                }
            }
        }
    }

    private void removeEngine(Integer id) {
        if (engineStates.contains(id)) {
            engineStates.remove(id);
            if (engineStates.isEmpty()) {
                CmPollStateEvent cmPollStateEvent = new CmPollStateEvent(System.currentTimeMillis());
                cmPollStateEvent.setActionName("completeRoundStatistics");
                cmPollStateEvent.setListener(CmPollStateListener.class);
                messageService.addMessage(cmPollStateEvent);
            }
        }
    }

    private CmPollCollector makeCmPollCollector(Integer id) {
        CmPollCollector cmPollCollector = new CmPollCollector();
        cmPollCollector.init();
        cmPollCollector.setEngineId(id);
        return cmPollCollector;
    }

    private CmPollFacade getCmPollFacade(Integer engineId) {
        EngineServer engineServer = facadeFactory.getEngineServer(engineId);
        CmPollFacade cmPollFacade = facadeFactory.getFacade(engineServer, CmPollFacade.class);
        return cmPollFacade;
    }

    private synchronized long nextTaskId() {
        return maxTaskId++;
    }

    @Override
    public void statusChanged(EngineServerEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmPollSchedulerServiceImpl.statusChanged event:" + event);
        }
        switch (event.getStatus()) {
        case EngineServerEvent.STATUS_CONNECTED:
        case EngineServerEvent.STATUS_DISCONNECTED:
            if (logger.isDebugEnabled()) {
                logger.debug("CmPollSchedulerServiceImpl.statusChanged event:" + event);
            }
            notifyEngineServerChange();
            logger.debug("CmPollSchedulerServiceImpl.statusChanged end");
        }
    }
}