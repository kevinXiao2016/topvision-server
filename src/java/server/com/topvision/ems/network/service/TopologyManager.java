package com.topvision.ems.network.service;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.topvision.ems.facade.PingFacade;
import com.topvision.ems.facade.callback.TopologyCallback;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.TopologyParam;
import com.topvision.ems.facade.domain.TopologyResult;
import com.topvision.ems.facade.network.TopologyFacade;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.LinkDao;
import com.topvision.ems.network.dao.PortDao;
import com.topvision.ems.network.dao.TopoFolderDao;
import com.topvision.ems.network.dao.TopologyDao;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.domain.FolderUserGroupRela;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.message.TopologyEvent;
import com.topvision.ems.network.message.TopologyListener;
import com.topvision.ems.template.service.EntityIdentify;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.EventListenerList;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserGroup;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

// TODO 该类中主要剩下线程中的异常处理，先保留。
public class TopologyManager implements TopologyCallback {
    /**
     * 自动发现线程
     * 
     * @Create Date Aug 27, 2009 6:18:51 PM
     * 
     * @author kelers
     * 
     */
    class AutoDiscovery extends Thread {
        private boolean running = false;

        /**
         * 自动发现
         */
        private void autoDiscovery() {
            TopologyParam param = null;
            param = paramService.txGetTopologyParam();
            if (param != null && param.isAutoDiscovery()) {
                if (lastTopologyTime + param.getAutoDiscoveryInterval() > System.currentTimeMillis()) {
                    long interval = 24 * 60 * 60 * 1000;
                    interval = param.getAutoDiscoveryInterval();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Auto discovery interval:" + interval);
                    }
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException ex) {
                        logger.debug(ex.getMessage(), ex);
                    }
                } else {
                    try {
                        running = true;
                        if (logger.isDebugEnabled()) {
                            logger.debug("Auto discovery begin!!!!!!!!!!!!!!!!");
                        }
                        discovery();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Auto discovery end!!!!!!!!!!!!!!!!");
                        }
                    } catch (Exception ex) {
                        logger.error("Topology error.", ex);
                    } finally {
                        running = false;
                    }
                    lastTopologyTime = System.currentTimeMillis();
                }
            } else {
                try {
                    Thread.sleep(Math.min(600000, param.getAutoDiscoveryInterval()));
                } catch (InterruptedException e) {
                    logger.debug(e.getMessage(), e);
                }
            }
        }

        public boolean isRunning() {
            return running;
        }

        @Override
        public void run() {
            setName("AutoDiscovery");
            for (;;) {
                try {
                    autoDiscovery();
                } catch (Exception e) {
                    logger.debug(e.getMessage(), e);
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ie) {
                        logger.debug(ie.getMessage(), ie);
                    }
                }
            }
        }
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private EntityDao entityDao;
    private EntityAddressDao entityAddressDao;
    private LinkDao linkDao;
    private PortDao portDao;
    private TopologyDao topologyDao;
    private TopoFolderDao topoFolderDao;
    private EntityTypeService entityTypeService;
    private EntityIdentify entityIdentify;
    private TopologyParamService paramService;
    private MessageService messageService;
    private final Set<String> ignores;
    private long lastTopologyTime = 0;
    private XMLEncoder topoLog;
    private Set<String> topoedIps;
    private final EventListenerList listenerList = new EventListenerList();
    private final TopologyEvent topologyEvent = new TopologyEvent();
    private final List<Long> physicals = new ArrayList<Long>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    private Map<Long, Entity> deviceMonitorEntities;
    // 异步处理拓扑结果
    private TopologyCallback topologyCallback;
    // 自动发现
    private final AutoDiscovery autoDiscovery = new AutoDiscovery();
    private OnlineService onlineService;
    private LinkedBlockingQueue<String> seeds;
    private LinkedBlockingQueue<TopologyParam> segments;

    private final List<Link> l3Links = new ArrayList<Link>();

    private FacadeFactory facadeFactory;

    /**
     * @param ignores
     */
    public TopologyManager(Set<String> ignores) {
        this.ignores = ignores;
    }

    public void addTopologyListener(TopologyListener l) {
        listenerList.add(TopologyListener.class, l);
    }

    private void copyEntity(Long srcFolderId, Long destFolderId, List<Long> entityIds, TopologyParam param)
            throws Exception {
        // 获取目标文件夹没有的设备ID, 即需要被复制到目标文件夹下的设备ID
        List<Long> ids = entityDao.getEntityIdByFolderId(destFolderId);
        if (ids != null && ids.size() > 0) {
            entityIds.removeAll(ids);
        }
        int size = entityIds.size();
        if (size > 0) {
            // 获取需要被复制的设备信息
            List<Entity> entities = entityDao.getEntityByFolderId(entityIds, srcFolderId);
            for (int i = 0; entities != null && !entities.isEmpty() && i < entities.size(); i++) {
                Entity entity = entities.get(i);
                entity.setFolderId(destFolderId);
            }
            // 插入被复制设备在目的文件夹下的关联
            entityDao.insertEntity(entities);
            entities.clear();
            entities = null;
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
    }

    /**
     * topology
     */
    public void discovery() throws Exception {
        topologyEvent.setProgress(0);
        if (lastTopologyTime == -1) {
            topologyEvent.setMsg(getString("topology.others"));
            topologyEvent.setProgress(100);
            fireTopologyProgressChanged(topologyEvent);
            return;
        }
        try {
            logger.info("=================Begin to Topology=================");
            lastTopologyTime = -1;
            physicals.clear();
            topoedIps.clear();
            seeds.clear();
            segments.clear();
            l3Links.clear();
            topoedIps.addAll(ignores);
            if (topoLog == null && SystemConstants.getInstance().getBooleanParam("Topology.save.log", true)) {
                topoLog = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("../logs/topology."
                        + new SimpleDateFormat("yyyyMMdd.HHmmss").format(new java.util.Date()) + ".xml")));
            }

            topologyEvent.setMsg(getString("topology.begin"));
            fireTopologyProgressChanged(topologyEvent);
            final TopologyParam param = paramService.txGetTopologyParam();
            param.setFolderId(TopoFolder.NETWORK_FOLDER);
            topologyCallback.setOnlyDiscoverySnmp(param.isOnlyDiscoverySnmp());
            if (param.isAutoCreateMonitor()) {
                deviceMonitorEntities = new HashMap<Long, Entity>();
            }
            if (param.getExcludeTarget() != null && param.getExcludeTarget().length() > 0) {
                topoedIps.addAll(IpUtils.parseIp(param.getExcludeTarget()));
            }
            if (param.getSeeds() != null && !param.getSeeds().isEmpty()) {
                for (TopologyParam.Seed seed : param.getSeeds()) {
                    seeds.add(seed.getSeed());
                }
                while (!seeds.isEmpty()) {
                    topoSeed(seeds.take(), param);
                }
            }
            if (param.getTarget() != null && param.getTarget().length() > 0) {
                segments.put(param);
            }
            while (!segments.isEmpty()) {
                TopologyParam segment = segments.take();
                try {
                    Map<String, String> ipMacs = facadeFactory.getDefaultFacade(PingFacade.class).scan(
                            segment.getTarget(), segment.getPingTimeout(), segment.getPingRetry());
                    fireTopologyProgressChanged(new TopologyEvent(getString("topology.discover",
                            String.valueOf(ipMacs.size())), 1));
                    topologyTarget(ipMacs, segment);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            while (!topologyCallback.isFinished()) {
                Thread.sleep(100);
            }
            // 处理终端mac地址
            for (Entity entity : deviceMonitorEntities.values()) {
                if (entity.getMac() == null && entity.getIp() != null) {
                    String mac = onlineService.getMacByIp(entity.getIp());
                    if (mac != null) {
                        entity.setMac(mac);
                        entityDao.updateEntity(entity);
                    }
                }
            }
            copyEntity(TopoFolder.NETWORK_FOLDER, TopoFolder.PHYSICAL_FOLDER, physicals, param);
            topologyEvent.setMsg(getString("topology.create.link"));
            fireTopologyProgressChanged(topologyEvent);
            List<Link> links = new ArrayList<Link>();
            for (Link link1 : l3Links) {
                if (link1.getNote() == null) {
                    continue;
                }
                Entity entity = entityDao.getEntityByIp(link1.getNote());
                if (entity == null) {
                    continue;
                }
                for (Link link2 : l3Links) {
                    if (link2.getSrcEntityId() == entity.getEntityId()) {
                        link2.setNote(null);
                        link1.setDestEntityId(link2.getSrcEntityId());
                        link1.setDestIfIndex(link1.getSrcIfIndex());
                        links.add(link1);
                        break;
                    }
                }
            }
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if (deviceMonitorEntities == null || deviceMonitorEntities.isEmpty()) {
                        return;
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug(deviceMonitorEntities.toString());
                    }
                    for (Iterator<Entity> itr = deviceMonitorEntities.values().iterator(); itr.hasNext();) {
                        try {
                            Entity entity = itr.next();
                            EntityEvent event = new EntityEvent(entity);
                            event.setEntity(entity);
                            event.setActionName("entityAdded");
                            event.setListener(EntityListener.class);

                            messageService.addMessage(event);
                        } catch (Exception ex) {
                            logger.debug(ex.getMessage(), ex);
                        }
                    }
                    deviceMonitorEntities = null;
                }
            });
            topologyEvent.setMsg(getString("topology.successful"));
        } catch (Exception ex) {
            logger.error("discovery error", ex);
            topologyEvent.setMsg(getString("topology.failure"));
        } finally {
            lastTopologyTime = System.currentTimeMillis();
            try {
                if (topoLog != null) {
                    topoLog.close();
                }
            } catch (Exception ex) {
                logger.debug(ex.getMessage(), ex);
            }
            topoedIps.clear();
            topoLog = null;
            topologyEvent.setProgress(100);
            fireTopologyProgressChanged(topologyEvent);
            logger.info("=================Topology ended!=================");
        }
    }

    /**
     * 
     * @param tb
     * @param entityId
     */
    private void doPort(TopologyResult tb, Long entityId) {
        boolean ifXEnable = tb.getIfXTable() != null && tb.getIfXTable().length > 0;
        for (int i = 0; tb.getIfTable() != null && i < tb.getIfTable().length; i++) {
            try {
                Port port;
                if (ifXEnable && tb.getIfXTable().length > i && tb.getIfXTable()[i] != null
                        && !tb.getIfXTable()[i].equals("null")) {
                    port = new Port(entityId, tb.getIfTable()[i], tb.getIfXTable()[i]);
                } else {
                    port = new Port(entityId, tb.getIfTable()[i]);
                }
                port.setCard(0L);
                if (port.getIfName() == null) {
                    port.setIfName("Port " + port.getIfIndex());
                }
                portDao.insertOrUpdateEntity(port);
            } catch (DataAccessException ex) {
                logger.debug(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 
     * @param entityId
     * @param state
     */
    public void fireEntityState(Long entityId, Boolean state) {
        EntityValueEvent event = new EntityValueEvent(entityId);
        event.setState(state);
        event.setActionName("stateChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);
    }

    protected void fireTopologyProgressChanged(TopologyEvent evt) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TopologyListener.class) {
                ((TopologyListener) listeners[i + 1]).topologyProgressChanged(evt);
            }
        }
    }

    /**
     * 添加所有的设备ip
     * 
     * @param entity
     * @param ipAddrTable
     * @return
     */
    private List<EntityAddress> getAllIps(Entity entity, String[][] ipAddrTable) {
        List<EntityAddress> eas = new ArrayList<EntityAddress>();
        EntityAddress ea = new EntityAddress();
        ea.setEntityId(entity.getEntityId());
        ea.setIp(entity.getIp());
        eas.add(ea);
        topoedIps.add(ea.getIp());
        for (int i = 0; ipAddrTable != null && ipAddrTable.length > 0 && i < ipAddrTable.length; i++) {
            if (ipAddrTable[i][0] == null || ignores.contains(ipAddrTable[i][0])) {
                continue;
            }
            ea = new EntityAddress();
            ea.setEntityId(entity.getEntityId());
            ea.setIp(ipAddrTable[i][0]);
            if (!eas.contains(ea)) {
                eas.add(ea);
                topoedIps.add(ipAddrTable[i][0]);
            }
        }
        return eas;
    }

    public EntityAddressDao getEntityAddressDao() {
        return entityAddressDao;
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * @return the entityIdentify
     */
    public EntityIdentify getEntityIdentify() {
        return entityIdentify;
    }

    /**
     * @return the entityTypeService
     */
    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    /**
     * @return the linkDao
     */
    public LinkDao getLinkDao() {
        return linkDao;
    }

    /**
     * 获取entity，如果没有就创建
     * 
     * @param result
     * @param folderId
     * @param onlyDiscoverySnmp
     * @return
     */
    public synchronized Entity getOrCreateEntity(TopologyResult result, Long folderId, Boolean onlyDiscoverySnmp) {
        Entity entity = null;
        try {
            entity = entityDao.getEntityByIp(result.getIp());
            if (entity != null) {
                entity.setFolderId(folderId);
                if (result.getIpAddrTable() != null && result.getIpAddrTable().length > 0) {
                    entityAddressDao.insertOrUpdate(getAllIps(entity, result.getIpAddrTable()));
                }
                entityDao.insertEntity(entity);
                return entity;
            } else if (onlyDiscoverySnmp && result.getError() != null) {
                return null;
            }
        } catch (DataAccessException ex) {
            logger.debug(ex.getMessage(), ex);
            return null;
        }
        entity = new Entity();
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setModifyTime(entity.getCreateTime());
        EntityType et = null;
        if (result.getSystem() != null && result.getSystem().length > 6) {
            et = entityIdentify.identify(result.getSystem()[1], result.getSystem()[6]);
        } else {
            et = entityIdentify.identify(null, null);
        }
        entity.setIp(result.getIp());
        entity.setName(result.getHostName());
        entity.setFolderId(folderId);
        if (et.getTypeId() > 100000) {
            entity.setTypeId(et.getTypeId());
        }
        entity.setSnmpSupport(false);
        entityDao.insertEntity(entity);
        //entityDao.insertEntity(entity);   delete by fanzidong, 猜测是多余的
        try {
            // insert address
            if (result.getIpAddrTable() != null && result.getIpAddrTable().length > 0) {
                entityAddressDao.insertOrUpdate(getAllIps(entity, result.getIpAddrTable()));
            } else {
                EntityAddress ea = new EntityAddress();
                ea.setEntityId(entity.getEntityId());
                ea.setIp(result.getIp());
                entityAddressDao.insertOrUpdate(ea);
            }
        } catch (DataAccessException ex) {
            logger.debug(ex.getMessage(), ex);
        }
        if (autoDiscovery.isRunning()) {
            if (logger.isDebugEnabled()) {
                logger.debug("auto discovery found new device" + entity.getIp());
            }
            Event event = EventSender.getInstance().createEvent(10003, entity.getIp(), entity.getIp());
            event.setEntityId(entity.getEntityId());
            event.setMessage(getString("topology.found.device", entity.getIp(), entity.getName()));
            EventSender.getInstance().send(event);
        }
        return entity;
    }

    /**
     * @return the paramService
     */
    public TopologyParamService getParamService() {
        return paramService;
    }

    /**
     * @return the portDao
     */
    public PortDao getPortDao() {
        return portDao;
    }

    private String getString(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.network.resources").getString(key, strings);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * @return the topoFolderDao
     */
    public TopoFolderDao getTopoFolderDao() {
        return topoFolderDao;
    }

    /**
     * @return the topologyDao
     */
    public TopologyDao getTopologyDao() {
        return topologyDao;
    }

    /**
     * @return the topologyEngineService
     */
    public TopologyFacade getTopologyFacade(String ip) {
        return facadeFactory.getFacade(ip, TopologyFacade.class);
    }

    /**
     * 初始化
     */
    public void initialize() {
        topoedIps = new HashSet<String>();
        seeds = new LinkedBlockingQueue<String>();
        segments = new LinkedBlockingQueue<TopologyParam>();
        try {
            if (true) {
                startAutoDiscovery();
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }

    public void removeTopologyListener(TopologyListener l) {
        listenerList.remove(TopologyListener.class, l);
    }

    public void setEntityAddressDao(EntityAddressDao entityAddressDao) {
        this.entityAddressDao = entityAddressDao;
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @param entityIdentify
     *            the entityIdentify to set
     */
    public void setEntityIdentify(EntityIdentify entityIdentify) {
        this.entityIdentify = entityIdentify;
    }

    /**
     * @param entityTypeService
     *            the entityTypeService to set
     */
    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    /**
     * @param linkDao
     *            the linkDao to set
     */
    public void setLinkDao(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public final void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * @param onlineService
     *            the onlineService to set
     */
    public void setOnlineService(OnlineService onlineService) {
        this.onlineService = onlineService;
    }

    /**
     * @param paramService
     *            the paramService to set
     */
    public void setParamService(TopologyParamService paramService) {
        this.paramService = paramService;
    }

    /**
     * @param portDao
     *            the portDao to set
     */
    public void setPortDao(PortDao portDao) {
        this.portDao = portDao;
    }

    /**
     * @param topoFolderDao
     *            the topoFolderDao to set
     */
    public void setTopoFolderDao(TopoFolderDao topoFolderDao) {
        this.topoFolderDao = topoFolderDao;
    }

    /**
     * @param topologyDao
     *            the topologyDao to set
     */
    public void setTopologyDao(TopologyDao topologyDao) {
        this.topologyDao = topologyDao;
    }

    /**
     * 启用新的自动发现
     * 
     * 立即发现一次
     */
    public void startAutoDiscovery() {
        if (!autoDiscovery.isAlive()) {
            autoDiscovery.start();
        }
    }

    /**
     * topology
     */
    public void topology() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    discovery();
                    startAutoDiscovery();
                } catch (Exception e) {
                    logger.debug(e.getMessage(), e);
                }
            }
        });
    }

    /**
     * topology immediately by entity
     * 
     * @param entityId
     * @throws Exception
     */
    public void topologyImmediately(Long entityId) throws Exception {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        if (logger.isDebugEnabled()) {
            logger.debug("topologyImmediately:" + entity.getIp());
        }
        TopologyResult bean = new TopologyResult();
        bean.setIp(entity.getIp());
        bean = getTopologyFacade(entity.getIp()).topology(bean, snmpParam);
        if (logger.isDebugEnabled()) {
            logger.debug(bean.toString());
        }
        if (bean.getError() != null) {
            throw new Exception(bean.getError());
        }
        updateEntity(entity, bean);
        EntityEvent event = new EntityEvent(entity);
        event.setEntity(entity);
        event.setActionName("entityChanged");
        event.setListener(EntityListener.class);
        messageService.addMessage(event);
        if (logger.isDebugEnabled()) {
            logger.debug("topologyImmediately successful!!!");
        }
    }

    /**
     * 
     * @param ipMacs
     * @param param
     */
    private void topologyTarget(final Map<String, String> ipMacs, TopologyParam param) {
        try {
            TopologyResult tr = null;
            for (String ip : ipMacs.keySet()) {
                if (topoedIps.contains(ip)) {
                    continue;
                }
                topoedIps.add(ip);
                tr = new TopologyResult();
                tr.setIp(ip);
                tr.setUserObject(param.getFolderId());
                if (ipMacs.get(ip) != null && ipMacs.get(ip).length() > 0) {
                    tr.addMac(ipMacs.get(ip));
                }
                topologyCallback.addTask();
                getTopologyFacade(ip).addTopology(tr, param);
            }
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
        }
    }

    /**
     * Topology seed
     * 
     * @param seed
     *            seed's ip
     * @param param
     */
    private void topoSeed(String seed, TopologyParam param) {
        try {
            if (seed == null || topoedIps.contains(seed)) {
                return;
            } else {
                topoedIps.add(seed);
            }
            if (facadeFactory.getFacade(seed, PingFacade.class)
                    .ping(seed, param.getPingTimeout(), param.getPingRetry()) < 0) {
                return;
            }
        } catch (Exception ex1) {
            logger.error(ex1.getMessage(), ex1);
            return;
        }
        try {
            TopologyResult bean = new TopologyResult();
            bean.setIp(seed);
            bean = getTopologyFacade(seed).topology(bean, param);
            if (logger.isDebugEnabled()) {
                logger.debug(bean.toString());
            }
            Entity entity = getOrCreateEntity(bean, param.getFolderId(), param.isOnlyDiscoverySnmp());
            if (entity == null) {
                return;
            }
            bean.setEntityId(entity.getEntityId());
            updateEntity(entity, bean);
            if (deviceMonitorEntities != null) {
                deviceMonitorEntities.put(entity.getEntityId(), entity);
            }
            fireTopologyProgressChanged(new TopologyEvent(getString("topology.seed", seed), 1));
            if (bean.getIpRouteTable() == null || bean.getIpRouteTable().length == 0) {
                return;
            }
            // doSeed
            for (String[] route : bean.getIpRouteTable()) {
                if (ignores.contains(route[0]) || ignores.contains(route[2]) || ignores.contains(route[6])) {
                    continue;
                }
                if ("3".indexOf(route[3]) != -1) {
                    // direct--subnet
                    int mask = IpUtils.getMaskBitSum(route[6]);
                    String target = route[0] + "/" + mask;
                    if (logger.isDebugEnabled()) {
                        logger.debug("subnet:" + target);
                    }
                    TopoFolder folder = new TopoFolder();
                    folder.setName(target);
                    folder.setSuperiorId(TopoFolder.NETWORK_FOLDER);
                    folder.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    TopoFolder tf = topoFolderDao.getTopoFolderByName(folder);
                    if (tf != null) {
                        folder = tf;
                    } else {
                        folder.setType(TopoFolder.TYPE_SUBNET);
                        folder.setIcon(TopoFolder.DEFAULT_SUBNET_ICON);
                        folder.setSubnetIp(route[0]);
                        folder.setSubnetMask(route[6]);
                        topoFolderDao.insertEntity(folder);

                        FolderUserGroupRela rela = new FolderUserGroupRela();
                        rela.setFolderId(folder.getFolderId());
                        rela.setUserGroupId(UserGroup.DEFAULT_SUPER_GROUP);
                        // topoFolderDao.insertFolderUserGroup(rela);
                        fireTopologyProgressChanged(new TopologyEvent(getString("topology.subnet", target), 1));
                    }
                    Link link = new Link();
                    link.setSrcEntityId(bean.getEntityId());
                    link.setSrcIfIndex(Long.parseLong(route[1]));
                    link.setDestEntityId(folder.getFolderId());
                    link.setType(Link.LOGIC_LINK_TYPE);
                    link.setName(bean.getIp() + " - " + target);
                    linkDao.insertOrUpdateEntity(link);
                    int minMask = SystemConstants.getInstance().getIntParam("topology.subnet.mask.min", 24);
                    int maxMask = SystemConstants.getInstance().getIntParam("topology.subnet.mask.max", 32);
                    if (mask >= minMask && mask <= maxMask) {
                        TopologyParam seedParam = param.clone();
                        seedParam.setFolderId(folder.getFolderId());
                        seedParam.setTarget(target);
                        segments.put(seedParam);
                    }
                    continue;
                } else if ("4".indexOf(route[3]) != -1) {
                    // indirect--next hop
                    // if ipRouteProto is bgp(14) then continue;
                    if ("14".contains(route[4])) {
                        continue;
                    }
                    String nexthop = route[2];
                    seeds.put(nexthop);
                    Link link = new Link();
                    link.setSrcEntityId(bean.getEntityId());
                    link.setSrcIfIndex(Long.parseLong(route[1]));
                    link.setType(Link.L3_LINK_TYPE);
                    link.setName(bean.getIp() + " - " + nexthop);
                    link.setNote(nexthop);
                    l3Links.add(link);
                }
            }
        } catch (SnmpException ex) {
            logger.debug(ex.getMessage(), ex);
        } catch (InterruptedException ex) {
            logger.debug(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * 更新Entity
     * 
     * @param entity
     *            old entity
     * @param tb
     *            TopologyResult
     * @return update or not
     * @throws Exception
     */
    public Boolean updateEntity(Entity entity, TopologyResult tb) throws Exception {
        if (topoLog != null && tb != null) {
            try {
                topoLog.writeObject(tb);
                topoLog.flush();
            } catch (Exception ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug(ex.getMessage(), ex);
                }
            }
        }
        if (tb.getError() != null) {
            entity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            entity.setNote(tb.getError());
            entityDao.updateEntity(entity);
            if (logger.isDebugEnabled()) {
                logger.debug("Topology " + tb.getIp() + " error:" + tb.getError());
            }
            return false;
        }
        // TODO 暂时取消更新设备 by victor@2011/6/15
        // EntityType et = entityIdentify.identify(tb.getSystem()[1],
        // tb.getSystem()[6]);
        // if (et.getTypeId() > 100000) {
        // entity.setTypeId(et.getTypeId());
        // }
        // entity.setType(et.getType());
        entity.setCorpId(entityIdentify.getCorp(tb.getSystem()[1]));
        entity.setOs(entityIdentify.getOS(tb.getSystem()[1]));
        SnmpParam snmpParam = tb.getSnmpParam();
        snmpParam.setWriteCommunity(entityDao.getSnmpParamByEntityId(entity.getEntityId()).getWriteCommunity());
        entity.setParam(snmpParam);
        if (tb.getMacList() != null && !tb.getMacList().isEmpty() && entity.getMac() == null) {
            entity.setMac(tb.getMacList().get(0));
        }
        entity.setSnmpSupport(true);
        entity.setModifyTime(new Timestamp(System.currentTimeMillis()));
        entity.setSysDescr(tb.getSystem()[0]);
        entity.setSysObjectID(tb.getSystem()[1]);
        entity.setSysContact(tb.getSystem()[3]);
        entity.setSysName(tb.getSystem()[4]);
        entity.setSysLocation(tb.getSystem()[5]);
        entity.setLocation(tb.getSystem()[5]);
        entity.setSysServices(tb.getSystem()[6]);
        entityDao.updateEntity(entity);
        int sysServices = Integer.parseInt(entity.getSysServices());
        if ((sysServices & 2) == 2 || (sysServices & 4) == 4) {
            if (entity.getCorpId() != 311 && !physicals.contains(entity.getEntityId())) {
                physicals.add(entity.getEntityId());
            }
        }
        // update snap of entity
        fireEntityState(entity.getEntityId(), true);
        // update address
        entityAddressDao.insertOrUpdate(getAllIps(entity, tb.getIpAddrTable()));
        // update ports.
        if (tb.getIfTable() != null && tb.getIfTable().length > 0) {
            doPort(tb, entity.getEntityId());
        }
        return true;
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    //Modify by Victor@20150326把内部类提出来
    private final AtomicInteger tasks = new AtomicInteger();
    private boolean onlyDiscoverySnmp;

    @Override
    public void addTask() {
        tasks.incrementAndGet();
    }

    @Override
    public void clearTasks() {
        tasks.set(0);
    }

    @Override
    public Boolean isFinished() {
        return tasks.get() == 0;
    }

    @Override
    public Boolean onData(TopologyResult result) {
        try {
            if (result == null) {
                return Boolean.FALSE;
            }
            if (logger.isDebugEnabled()) {
                logger.debug(result.toString());
            }
            fireTopologyProgressChanged(new TopologyEvent(getString("topology.device", String.valueOf(result.getIp())),
                    1));
            onlineService.addData(result);
            Entity entity = getOrCreateEntity(result, (Long) result.getUserObject(), onlyDiscoverySnmp);
            if (entity == null) {
                return Boolean.TRUE;
            }
            result.setEntityId(entity.getEntityId());
            updateEntity(entity, result);
            if (deviceMonitorEntities != null) {
                deviceMonitorEntities.put(entity.getEntityId(), entity);
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
            return Boolean.FALSE;
        } finally {
            tasks.decrementAndGet();
        }
    }

    @Override
    public void setOnlyDiscoverySnmp(Boolean onlyDiscoverySnmp) {
        this.onlyDiscoverySnmp = onlyDiscoverySnmp;
    }
}
