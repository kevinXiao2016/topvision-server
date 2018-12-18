/***********************************************************************
 * $ PnmpPollSchedulerServiceImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service.impl;

import com.topvision.ems.cm.pnmp.facade.domain.DelayItem;
import com.topvision.ems.cm.pnmp.domain.PnmpPollCollectParam;
import com.topvision.ems.cm.pnmp.facade.PnmpPollFacade;
import com.topvision.ems.cm.pnmp.facade.domain.*;
import com.topvision.ems.cm.pnmp.message.PnmpPollStateEvent;
import com.topvision.ems.cm.pnmp.message.PnmpPollStateListener;
import com.topvision.ems.cm.pnmp.domain.PnmpPollCollector;
import com.topvision.ems.cm.pnmp.service.PnmpPollConfigService;
import com.topvision.ems.cm.pnmp.service.PnmpSchedulerService;
import com.topvision.ems.cm.pnmp.facade.callback.PnmpPollCallback;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EngineServerEvent;
import com.topvision.platform.message.event.EngineServerListener;
import com.topvision.platform.message.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Service("pnmpSchedulerService")
public class PnmpSchedulerServiceImpl extends BaseService
        implements PnmpSchedulerService, PnmpPollCallback, EngineServerListener {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private PnmpPollConfigService pnmpPollConfigService;
    private Long maxTaskId = 1L;

    /**
     * 采集器列表，记录每个采集器的状态信息，以便调度管理
     */
    private Map<Integer, PnmpPollCollector> pnmpPollCollectors = Collections
            .synchronizedMap(new HashMap<Integer, PnmpPollCollector>());

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

    /***************************************** PnmpPollSchedulerService ****************************************/

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void appendTask(Integer engineId, Long time, List<PnmpPollTask> pnmpPollTasks) {
        if (logger.isTraceEnabled()) {
            logger.trace("engineId:" + engineId + " time:" + sdf.format(new Date(time)) + " task count:"
                    + pnmpPollTasks.size());
        }

        for (PnmpPollTask pnmpPollTask : pnmpPollTasks) {
            if (pnmpPollTask instanceof PnmpPollLowSpeedEndTask
                    || pnmpPollTask instanceof PnmpPollMiddleSpeedEndTask
                    || pnmpPollTask instanceof PnmpPollHighSpeedEndTask) {
                continue;
            }
            pnmpPollTask.setEngineId(engineId);
            pnmpPollTask.setTaskId(nextTaskId());
        }
        PnmpPollFacade pnmpPollFacade = getPnmpPollFacade(engineId);
        boolean b = pnmpPollFacade.appendTesk(time, pnmpPollTasks);
        // 如果engine端还没准备好则等待
        while (!b) {
            try {
                Thread.sleep(5000);
                logger.trace("Engine not prepared" + "----" + engineId + "----");
            } catch (InterruptedException e) {
            }
            b = pnmpPollFacade.appendTesk(time, pnmpPollTasks);
        }
        PnmpPollCollector pnmpPollCollector = pnmpPollCollectors.get(engineId);
        for (PnmpPollTask pnmpPollTask : pnmpPollTasks) {
            if (pnmpPollTask instanceof PnmpPollLowSpeedEndTask
                    || pnmpPollTask instanceof PnmpPollMiddleSpeedEndTask
                    || pnmpPollTask instanceof PnmpPollHighSpeedEndTask) {
                List<PnmpPollFacade> pnmpPollFacades = facadeFactory.getAllFacade(PnmpPollFacade.class);
                for (PnmpPollFacade pollFacade : pnmpPollFacades) {
                    pollFacade.appendEndTask(time, pnmpPollTask);
                }
                continue;
            }
            pnmpPollCollector.getTaskDlayQueue().add(new DelayItem(pnmpPollTask.getTaskId(), 5 * 60 * 1000L));
            pnmpPollCollector.addExecuteTaskCounts();
            pnmpPollCollector.addRoundTotalTaskCounts();
        }
    }

    @Override
    public void roundStart(Long time, PnmpPollTask pnmpPollTask) {
        if (logger.isDebugEnabled()) {
            logger.debug("PnmpPollSchedulerServiceImpl.roundStart" + sdf.format(new Date(time)));
        }
        List<PnmpPollFacade> pnmpPollFacades = facadeFactory.getAllFacade(PnmpPollFacade.class);
        for (PnmpPollFacade pnmpPollFacade : pnmpPollFacades) {
            pnmpPollFacade.roundStart(time,pnmpPollTask);
        }
        logger.debug("PnmpPollSchedulerServiceImpl.roundStart end");
    }

    @Override
    public void roundFinished(Long time,PnmpPollTask pnmpPollTask) {
        if (logger.isDebugEnabled()) {
            logger.debug("PnmpPollSchedulerServiceImpl.roundFinished" + sdf.format(new Date(time)));
        }

        PnmpPollStateEvent pnmpPollStateEvent = new PnmpPollStateEvent(time);
        pnmpPollStateEvent.setActionName("startRoundStatistics");
        if (pnmpPollTask == null) {
            pnmpPollStateEvent.setType(PnmpPollStateEvent.ALL);
        } else if (pnmpPollTask instanceof PnmpPollLowSpeedTask) {
            pnmpPollStateEvent.setType(PnmpPollStateEvent.LOW);
        } else if (pnmpPollTask instanceof PnmpPollMiddleSpeedTask) {
            pnmpPollStateEvent.setType(PnmpPollStateEvent.MIDDLE);
        } else if (pnmpPollTask instanceof PnmpPollHighSpeedTask) {
            pnmpPollStateEvent.setType(PnmpPollStateEvent.HIGH);
        } else {
            pnmpPollStateEvent.setType(PnmpPollStateEvent.ALL);
        }
        pnmpPollStateEvent.setListener(PnmpPollStateListener.class);
        messageService.addMessage(pnmpPollStateEvent);
        logger.debug("PnmpPollSchedulerServiceImpl.roundFinished end");
    }

    @Override
    public Integer isAnyIdle() {
        if (pnmpPollCollectors.size() == 0) {
            return -1;
        }
        List<PnmpPollCollector> cpcs = new ArrayList<PnmpPollCollector>(pnmpPollCollectors.values());
        Collections.sort(cpcs);
        PnmpPollCollector pnmpPollCollector = cpcs.get(0);
        if (pnmpPollCollector.getState() == PnmpPollCollector.OFFLINE || pnmpPollCollector.getIdleCounts() == 0) {
            return -1;
        } else {
            return pnmpPollCollector.getEngineId();
        }
    }

    @Override
    public Integer idleTaskCount(Integer engineId) {
        PnmpPollCollector pnmpPollCollector = pnmpPollCollectors.get(engineId);
        if (pnmpPollCollector == null) {
            return 0;
        } else {
            return pnmpPollCollector.getIdleCounts();
        }
    }

    /***************************************** PnmpPollCallback ****************************************/
    @Override
    public void connectTest() {
    }

    @Override
    public void completeTask(Integer engineId, Long taskId) {
        if (logger.isDebugEnabled()) {
            logger.debug("PnmpPollSchedulerServiceImpl.completeTask engineId:" + engineId + " taskId:" + taskId);
        }
        if (pnmpPollCollectors.containsKey(engineId)) {
            PnmpPollCollector pnmpPollCollector = pnmpPollCollectors.get(engineId);
            pnmpPollCollector.delExecuteTaskCounts();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("PnmpPollSchedulerServiceImpl.completeTask pnmpPollCollector is not exist engineId:" + engineId
                        + " taskId:" + taskId);
            }
        }
        logger.debug("PnmpPollSchedulerServiceImpl.completeTask end");
    }

    @Override
    public void completeRoundStatistics(Integer engineId, Long time, PnmpPollTask pnmpPollTask) {
        if (logger.isDebugEnabled()) {
            logger.debug("PnmpPollSchedulerServiceImpl.completeRoundStatistics " + pnmpPollTask.getClass().getName() + " engineId:" + engineId + " time:"
                    + sdf.format(new Date(time)));
        }
        if (pnmpPollCollectors.containsKey(engineId)) {
            PnmpPollCollector pnmpPollCollector = pnmpPollCollectors.get(engineId);
            pnmpPollCollector.setNextRoundTotalTaskCounts(pnmpPollCollector.getRoundTotalTaskCounts());
            pnmpPollCollector.setRoundTotalTaskCounts(0);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("PnmpPollSchedulerServiceImpl.completeRoundStatistics pnmpPollCollector is not exist engineId:"
                        + engineId);
            }
        }
        removeEngine(engineId,pnmpPollTask);
        logger.debug("PnmpPollSchedulerServiceImpl.completeRoundStatistics end");
    }

    @Override
    public List<PnmpPollCollector> getPnmpPollCollectorList() {
        Iterator<Entry<Integer, PnmpPollCollector>> it = this.pnmpPollCollectors.entrySet().iterator();
        List<PnmpPollCollector> pnmpPollCollectorList = new ArrayList<PnmpPollCollector>();
        while (it.hasNext()) {
            Entry<Integer, PnmpPollCollector> entry = it.next();
            pnmpPollCollectorList.add(entry.getValue());
        }
        return pnmpPollCollectorList;
    }

    public void notifyEngineServerChange() {
        PnmpPollCollectParam pnmpPollCollectParam = pnmpPollConfigService.getPnmpPollCollectParam();

        List<EngineServer> engineServers = facadeFactory.getEngineServerByType(EngineServer.TYPE_PNMP);
        for (EngineServer engineServer : engineServers) {
            PnmpPollCollector pnmpPollCollector = null;
            if (pnmpPollCollectors.containsKey(engineServer.getId())) {
                pnmpPollCollector = pnmpPollCollectors.get(engineServer.getId());
            } else {
                pnmpPollCollector = makePnmpPollCollector(Integer.parseInt(engineServer.getEngineId()));
                pnmpPollCollector.setDesc(engineServer.getName());
                pnmpPollCollectors.put(engineServer.getId(), pnmpPollCollector);
            }
            if (engineServer.getLinkStatus() == EngineServer.CONNECTED
                    && pnmpPollCollector.getState() == PnmpPollCollector.OFFLINE) {
                pnmpPollCollector.setState(PnmpPollCollector.ONLINE);
                pnmpPollCollector.setMaxTaskCounts(pnmpPollCollectParam.getMaxPoolSize());
                PnmpPollFacade pnmpPollFacade = facadeFactory.getFacade(engineServer, PnmpPollFacade.class);
                pnmpPollFacade.initRunAug(pnmpPollCollectParam);
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "PnmpPollSchedulerServiceImpl.notifyEngineServerChange engineServer.getLinkStatus() == EngineServer.CONNECTED\n"
                                    + "&& pnmpPollCollector.getState() == PnmpPollCollector.OFFLINE");
                }
            } else if (engineServer.getLinkStatus() != EngineServer.CONNECTED
                    && pnmpPollCollector.getState() == PnmpPollCollector.ONLINE) {
                pnmpPollCollector.init();
                removeEngine(engineServer.getId(), null);
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "PnmpPollSchedulerServiceImpl.notifyEngineServerChange engineServer.getLinkStatus() != EngineServer.CONNECTED\n"
                                    + "&& pnmpPollCollector.getState() == PnmpPollCollector.ONLINE");
                }
            }
        }
    }

    private void removeEngine(Integer id, PnmpPollTask pnmpPollTask) {
        engineStates.remove(id);
        if (engineStates.isEmpty()) {
            PnmpPollStateEvent pnmpPollStateEvent = new PnmpPollStateEvent(System.currentTimeMillis());
            pnmpPollStateEvent.setActionName("completeRoundStatistics");
            if (pnmpPollTask == null) {
                pnmpPollStateEvent.setType(PnmpPollStateEvent.ALL);
            } else if (pnmpPollTask instanceof PnmpPollLowSpeedEndTask) {
                pnmpPollStateEvent.setType(PnmpPollStateEvent.LOW);
            } else if (pnmpPollTask instanceof PnmpPollMiddleSpeedEndTask) {
                pnmpPollStateEvent.setType(PnmpPollStateEvent.MIDDLE);
            } else if (pnmpPollTask instanceof PnmpPollHighSpeedEndTask) {
                pnmpPollStateEvent.setType(PnmpPollStateEvent.HIGH);
            } else {
                pnmpPollStateEvent.setType(PnmpPollStateEvent.ALL);
            }
            pnmpPollStateEvent.setListener(PnmpPollStateListener.class);
            messageService.addMessage(pnmpPollStateEvent);
        }
    }

    private PnmpPollCollector makePnmpPollCollector(Integer id) {
        PnmpPollCollector pnmpPollCollector = new PnmpPollCollector();
        pnmpPollCollector.init();
        pnmpPollCollector.setEngineId(id);
        return pnmpPollCollector;
    }

    private PnmpPollFacade getPnmpPollFacade(Integer engineId) {
        EngineServer engineServer = facadeFactory.getEngineServer(engineId);
        PnmpPollFacade pnmpPollFacade = facadeFactory.getFacade(engineServer, PnmpPollFacade.class);
        return pnmpPollFacade;
    }

    private synchronized long nextTaskId() {
        return maxTaskId++;
    }

    @Override
    public void statusChanged(EngineServerEvent event) {
        logger.info("PnmpPollSchedulerServiceImpl.statusChanged event:" + event);
        switch (event.getStatus()) {
        case EngineServerEvent.STATUS_CONNECTED:
        case EngineServerEvent.STATUS_DISCONNECTED:
            logger.info("PnmpPollSchedulerServiceImpl.statusChanged event:" + event);
            notifyEngineServerChange();
            logger.info("PnmpPollSchedulerServiceImpl.statusChanged end");
        }
    }
}