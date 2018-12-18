/***********************************************************************
 * $Id: EngineServerServiceImpl.java,v 1.1 Jul 19, 2009 11:02:41 AM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.registry.RegistryService;
import com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistryFactory;
import com.alibaba.dubbo.remoting.zookeeper.zkclient.ZkclientZookeeperTransporter;
import com.topvision.ems.enginemgr.EngineManage;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.ems.facade.domain.EngineServerParam;
import com.topvision.ems.facade.domain.EngineServerStatus;
import com.topvision.ems.facade.nbi.ConfigEngineNbiFacade;
import com.topvision.ems.facade.nbi.NbiAddress;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.exception.engine.EngineDisconnectException;
import com.topvision.framework.exception.engine.EngineMgrDisconnectException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.dao.EngineServerDao;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EngineServerEvent;
import com.topvision.platform.message.event.EngineServerListener;
import com.topvision.platform.message.event.PerformanceShiftEvent;
import com.topvision.platform.message.event.PerformanceShiftListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.EngineServerService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.job.EngineServerJob;

@Service("engineServerService")
public class EngineServerServiceImpl extends BaseService implements EngineServerService {
    private static final URL SUBSCRIBE = new URL(Constants.ADMIN_PROTOCOL, NetUtils.getLocalHost(), 0, "",
            Constants.INTERFACE_KEY, Constants.ANY_VALUE, Constants.GROUP_KEY, Constants.ANY_VALUE,
            Constants.VERSION_KEY, Constants.ANY_VALUE, Constants.CLASSIFIER_KEY, Constants.ANY_VALUE,
            Constants.CATEGORY_KEY,
            Constants.PROVIDERS_CATEGORY + "," + Constants.CONSUMERS_CATEGORY + "," + Constants.ROUTERS_CATEGORY + ","
                    + Constants.CONFIGURATORS_CATEGORY,
            Constants.ENABLED_KEY, Constants.ANY_VALUE, Constants.CHECK_KEY, String.valueOf(false));
    @Value("${engine.check.interval:60000}")
    private Long engineCheckInterval;// 60*1000L
    @Autowired
    private EngineServerDao engineServerDao;
    private Map<Integer, EngineServer> engineMapping = new ConcurrentHashMap<Integer, EngineServer>();
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private MessageService messageService;
    @Value("${engine.mgr.port:3009}")
    private Integer engineManagePort;
    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.engine.url}")
    private String url;
    @Value("${jdbc.engine.username}")
    private String username;
    @Value("${jdbc.engine.password}")
    private String password;
    @Value("${zookeeper.port:3007}")
    private Integer servicePort;
    @Value("${dubbo.timeout:300000}")
    private int timeout;
    @Value("${cpool.engine.acquireIncrement:5}")
    private Integer acquireIncrement;
    @Value("${cpool.engine.checkoutTimeout:0}")
    private Integer checkoutTimeout;
    @Value("${cpool.engine.initialPoolSize:120}")
    private Integer initialPoolSize;
    @Value("${cpool.engine.maxIdleTime:3600}")
    private Integer maxIdleTime;
    @Value("${cpool.engine.maxIdleTimeExcessConnections:1800}")
    private Integer maxIdleTimeExcessConnections;
    @Value("${cpool.engine.maxPoolSize:200}")
    private Integer maxPoolSize;
    @Value("${cpool.engine.minPoolSize:110}")
    private Integer minPoolSize;
    @Value("${java.rmi.server.hostname}")
    private String hostName;

    @Override
    @PostConstruct
    public void initialize() {
        logger.info("EngineServerServiceImpl.hostName:{}",hostName);
        if (hostName != null) {
            EnvironmentConstants.putEnv(EnvironmentConstants.HOSTNAME, hostName);
        }
        List<EngineServer> engines = engineServerDao.selectByMap(null);
        int size = engines == null ? 0 : engines.size();
        if (size > 0) {
            for (EngineServer engine : engines) {
                engineMapping.put(engine.getId(), engine);
            }
        }
    }

    @Override
    @PreDestroy
    public void destroy() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.EngineServerService#getEngineManage(java.lang.String)
     */
    @Override
    public EngineManage getEngineManage(String ip) {
        try {
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            StringBuilder serviceUrl = new StringBuilder("rmi://").append(ip).append(":").append(engineManagePort)
                    .append("/engineManage");
            proxy.setServiceUrl(serviceUrl.toString());
            proxy.setServiceInterface(EngineManage.class);
            proxy.setRegistryClientSocketFactory(new RMIClientSocketFactory() {

                @Override
                public Socket createSocket(String host, int port) throws IOException {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(host, port), 5000);
                    return socket;
                }
            });
            proxy.afterPropertiesSet();
            EngineManage engineManage = (EngineManage) proxy.getObject();
            return engineManage;
        } catch (Exception e) {
            logger.info(ip + "getEngineManage error", e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.EngineServerService#checkEngineServerExist(com.topvision.
     * platform.domain.EngineServer)
     */
    @Override
    public boolean checkEngineServerExist(EngineServer engineServer) {
        for (EngineServer es : engineMapping.values()) {
            if (es.getIp().equals(engineServer.getIp()) && es.getPort().equals(engineServer.getPort())
                    && !es.getEngineId().equals(engineServer.getEngineId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EngineServer getEngineServerById(Integer engineId) {
        return engineMapping.get(engineId);
    }

    @Override
    public List<EngineServer> getEngineServerList() {
        List<EngineServer> servers = new ArrayList<EngineServer>();
        servers.addAll(engineMapping.values());
        return servers;
    }

    private void transferFile(String enginePort, File file, EngineManage engineManage) {
        if (file.isDirectory()) {
            for (File tmp : file.listFiles()) {
                transferFile(enginePort, tmp, engineManage);
            }
        } else {
            byte[] b = FileUtils.fileToByte(file);
            String filePath = file.getAbsolutePath()
                    .substring(SystemConstants.ROOT_REAL_PATH.length() + "META-INF".length() + 1);
            filePath = filePath.replaceFirst("engine", "engine-" + enginePort);
            engineManage.transfer(filePath, b);
        }
    }

    @Override
    public void start() {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(Constants.FILE_KEY, "zookeeper/dubbo-registry-engine-" + servicePort + ".cache");
            URL url = new URL("zookeeper", EnvironmentConstants.getHostAddress(), servicePort, parameters);
            ZkclientZookeeperTransporter zookeeperTransporter = new ZkclientZookeeperTransporter();
            zookeeperTransporter.connect(url);
            ZookeeperRegistryFactory factory = new ZookeeperRegistryFactory();
            factory.setZookeeperTransporter(zookeeperTransporter);
            RegistryService registryService = factory.createRegistry(url);
            registryService.subscribe(SUBSCRIBE, this);

            JobDetail job = newJob(EngineServerJob.class).withIdentity("CheckEngine", "Default").build();
            job.getJobDataMap().put("facadeFactory", facadeFactory);
            job.getJobDataMap().put("engineServerDao", engineServerDao);
            job.getJobDataMap().put("engineMapping", engineMapping);
            job.getJobDataMap().put("engineServerService", this);

            TriggerBuilder<SimpleTrigger> builder = newTrigger()
                    .withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(repeatSecondlyForever((int) (engineCheckInterval / 1000)));
            schedulerService.scheduleJob(job, builder.build());
        } catch (DataAccessException ex) {
            getLogger().error("EngineServerServiceImpl.initialize error", ex);
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.EngineServerService#upgradeEngine(java.lang.String,
     * com.topvision.platform.service.EngineManage)
     */
    @Override
    public void upgradeEngine(EngineServer engineServer, EngineManage engineManage) {
        String enginePort = engineServer.getPort().toString();
        engineManage.deleteEngineDir(enginePort);
        StringBuilder fileName = new StringBuilder(SystemConstants.ROOT_REAL_PATH).append("META-INF")
                .append(File.separatorChar).append("engine");
        File file = new File(fileName.toString());
        transferFile(enginePort, file, engineManage);
        engineManage.updatePort(enginePort, null);
        engineManage.updateMem(enginePort, engineServer.getXmx(), engineServer.getXms());
    }

    @Override
    public void addEngineServer(EngineServer engineServer) {
        for (EngineServer es : engineMapping.values()) {
            if (es.getIp().equals(engineServer.getIp())) {
                engineServer.setManageStatus(es.getManageStatus());
                break;
            }
        }
        engineServerDao.insertEntity(engineServer);
        resetCache();
        sendStatusChangeMsg(engineServer, EngineServerEvent.STATUS_ADDED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.EngineServerService#reStartEngineServer(java.util.List)
     */
    @Override
    public void reStartEngineServer(Integer engineId) {
        EngineServer engineServer = engineMapping.get(engineId);
        if (engineServer.getManageStatus() == EngineServer.DISCONNECT) {
            return;
        }
        if (engineServer.getLinkStatus() == EngineServer.DISCONNECT) {
            return;
        }
        try {
            shutdownEngine(engineServer);
            startEngine(engineServer);
            initEngine(engineServer);
        } catch (Exception e) {
            logger.error(engineServer + " restart error ", e);
            logger.debug(engineServer.toString(), e);
        }
    }

    @Override
    public void deleteEngineServer(Integer engineId) {
        EngineServer engineServer = engineMapping.get(engineId);
        shutdownAndDeleteEngine(engineServer);
        engineServerDao.deleteByPrimaryKey(engineId);
        resetCache();
        sendStatusChangeMsg(engineServer, EngineServerEvent.STATUS_REMOVED);
    }

    @Override
    public void startEngineServer(Integer engineId) {
        List<Integer> engineServerIds = new ArrayList<>();
        engineServerIds.add(engineId);
        engineServerDao.startEngineServers(engineServerIds);
        resetCache();
    }

    @Override
    public void stopEngineServer(Integer engineId) {
        // 更新engineMapping中受影响行的管理状态
        EngineServer engineServer = engineMapping.get(engineId);
        shutdownEngine(engineServer);
        List<Integer> engineServerIds = new ArrayList<>();
        engineServerIds.add(engineId);
        engineServerDao.stopEngineServers(engineServerIds);
        resetCache();
        sendStatusChangeMsg(engineServer, EngineServerEvent.STATUS_DISABLED);
    }

    @Override
    public void modifyEngineServer(EngineServer engineServer) {
        Integer engineId = engineServer.getId();
        // Pervious EngineServer
        EngineServer es = engineMapping.get(engineId);
        EngineManage engineManage = getEngineManage(es.getIp());
        boolean shutdownFlag = false;
        if (!es.getXmx().equals(engineServer.getXmx()) || !es.getXms().equals(engineServer.getXms())) {
            if (engineManage != null) {
                // JAVA PARAM CHANGE
                engineManage.updateMem(es.getPort().toString(), engineServer.getXmx(), engineServer.getXms());
                shutdownFlag = true;
            } else {
                throw new EngineMgrDisconnectException(engineServer.getIp());
            }
        }
        if (!es.getPort().equals(engineServer.getPort())) {
            if (engineManage != null) {
                // PORT CHANGE
                engineManage.updatePort(es.getPort().toString(), engineServer.getPort().toString());
                shutdownFlag = true;
            } else {
                throw new EngineMgrDisconnectException(engineServer.getIp());
            }
        }
        if (!es.getType().equals(engineServer.getType())) {
            if (!shutdownFlag) {
                if (engineManage != null) {
                    // FACACE TYPE CHANGE
                    changeCollectorFacadeType(engineServer);
                } else {
                    throw new EngineMgrDisconnectException(engineServer.getIp());
                }
            }
        }
        engineServerDao.updateEntity(engineServer);
        if (shutdownFlag) {
            engineServer.setLinkStatus(EngineServer.DISCONNECT);
            engineServerDao.updateLinkStatus(engineServer);
        }
        resetCache();
        sendStatusChangeMsg(engineServer, EngineServerEvent.STATUS_MODIFIED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.EngineServerService#modifyLocalEngineServer(com.topvision.
     * platform.domain.EngineServer)
     */
    @Override
    public void modifyLocalEngineServer(EngineServer engineServer) {
        changeCollectorFacadeType(engineServer);
        engineServerDao.updateEntity(engineServer);
        resetCache();
        sendStatusChangeMsg(engineServer, EngineServerEvent.STATUS_MODIFIED);
    }

    /**
     * 修改采集器的采集类型
     * 
     * @param engineServer
     */
    private void changeCollectorFacadeType(EngineServer engineServer) {
        // Pervious EngineServer
        EngineServer es = engineMapping.get(engineServer.getId());
        List<String> previousType = new ArrayList<>(Arrays.asList(es.getType().split(",")));
        List<String> tmp = new ArrayList<>(Arrays.asList(es.getType().split(",")));
        List<String> currentType = new ArrayList<>(Arrays.asList(engineServer.getType().split(",")));
        // remove Type
        previousType.removeAll(currentType);
        // add Type
        currentType.removeAll(tmp);
        EngineServerParam engineServerParam = new EngineServerParam();
        engineServerParam.setAddCategories(currentType);
        engineServerParam.setDelCategories(previousType);
        CheckFacade facade = facadeFactory.getCheckFacade(engineServer);
        if (facade != null) {
            facade.syncEngine(engineServerParam);
            if (previousType.contains("Performance")) {
                sendPerformanceShiftMsg(engineServer.getId());
            }
        } else {
            // throw new EngineDisconnectException(engineServer.getName());
        }
    }

    @Override
    public EngineServerStatus getEngineServerStatus(Integer engineServerId) {
        EngineServer engineServer = getEngineServerById(engineServerId);
        EngineServerStatus engineServerStatus = new EngineServerStatus();
        // 获取指定的分布式采集器的状态信息
        try {
            CheckFacade facade = facadeFactory.getCheckFacade(engineServer);
            engineServerStatus = facade.getEngineServerStatus();
        } catch (Exception e) {
            getLogger().info("get engine: {}'s status failed", engineServer.getName());
            getLogger().debug(engineServer.getName(), e);
        }
        engineServerStatus.setId(engineServerId);
        engineServerStatus.setName(engineServer.getName());
        return engineServerStatus;
    }

    @Override
    public List<EngineServerStatus> getEngineServerStatuss(List<Integer> engineServerIds) {
        List<EngineServerStatus> engineServerStatuss = new ArrayList<EngineServerStatus>();
        for (Integer engineServerId : engineServerIds) {
            EngineServerStatus engineServerStatus = getEngineServerStatus(engineServerId);
            engineServerStatuss.add(engineServerStatus);
        }
        return engineServerStatuss;
    }

    @Override
    public void initEngine(EngineServer engineServer) {
        EngineServerParam param = new EngineServerParam();
        param.setId(engineServer.getId());
        param.setIp(engineServer.getIp());
        param.setPort(engineServer.getPort());
        param.setJdbcDriverClassName(driverClassName);
        /*
         * try { String serverIp = EnvironmentConstants.getHostAddress();
         * param.setJdbcUrl(url.replace("localhost", serverIp)); } catch (Exception e) {
         * param.setJdbcUrl(url); }
         */
        param.setJdbcUrl(url);
        param.setJdbcUsername(username);
        param.setJdbcPassword(password);
        // Add by Victor@20151222增加采集端配置从server端获取
        param.setAcquireIncrement(acquireIncrement);
        param.setCheckoutTimeout(checkoutTimeout);
        param.setInitialPoolSize(initialPoolSize);
        param.setMaxIdleTime(maxIdleTime);
        param.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
        param.setMaxPoolSize(maxPoolSize);
        param.setMinPoolSize(minPoolSize);

        param.setServiceIp(EnvironmentConstants.getHostAddress());
        param.setServicePort(servicePort);
        // Modify by Victor@20151205增加engine的分类管理，system为系统类别，每个engine都必须启用，比如CheckFacade
        param.addCategory("System");
        String[] categories = engineServer.getType().split(",");
        for (String category : categories) {
            param.addCategory(category);
        }
        CheckFacade facade = facadeFactory.getCheckFacade(engineServer);
        if (facade != null) {
            facade.initEngine(param);
            engineServer.setLinkStatus(EngineServer.CONNECTED);
        } else {
            throw new EngineDisconnectException(engineServer.getName());
        }
    }

    /**
     * Delete Engine
     * 
     * @param engineServer
     */
    private void shutdownAndDeleteEngine(EngineServer engineServer) {
        EngineManage engineManage = getEngineManage(engineServer.getIp());
        if (engineManage != null) {
            engineManage.shutdownEngine(engineServer.getPort().toString());
            engineManage.deleteEngineDir(engineServer.getPort().toString());
        } else {
            CheckFacade checkFacade = facadeFactory.getCheckFacade(engineServer);
            if (checkFacade != null) {
                try {
                    checkFacade.shutDown();
                } catch (RemoteConnectFailureException e) {
                }
            } else {
                // throw new EngineMgrDisconnectException(engineServer.getIp());
            }
        }
    }

    /**
     * Stop Engine
     * 
     * @param engineServer
     */
    private void shutdownEngine(EngineServer engineServer) {
        EngineManage engineManage = getEngineManage(engineServer.getIp());
        if (engineManage != null) {
            engineManage.shutdownEngine(engineServer.getPort().toString());
        } else {
            CheckFacade checkFacade = facadeFactory.getCheckFacade(engineServer);
            if (checkFacade != null) {
                try {
                    checkFacade.shutDown();
                } catch (RemoteConnectFailureException e) {
                }
            } else {
                // throw new EngineMgrDisconnectException(engineServer.getIp());
            }
        }
    }

    /**
     * Start Engine
     * 
     * @param engineServer
     */
    private void startEngine(EngineServer engineServer) {
        EngineManage engineManage = getEngineManage(engineServer.getIp());
        if (engineManage != null) {
            engineManage.reStartEngine(engineServer.getPort().toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.EngineServerService#isAllEngineStart()
     */
    @Override
    public boolean isAllEngineStart() {
        for (EngineServer engine : engineMapping.values()) {
            if (engine.getAdminStatus() == EngineServer.STOP) {
                continue;
            }
            if (engine.getLinkStatus() == EngineServer.DISCONNECT) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.EngineServerService#updateEngineStatusBeforeStart()
     */
    @Override
    public void updateEngineStatusBeforeStart() {
        resetCache();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.EngineServerService#updateEngineVersion(java.lang.Integer,
     * java.lang.String)
     */
    @Override
    public void updateEngineVersion(Integer id, String version) {
        engineServerDao.updateEngineVersion(id, version);
        resetCache();
    }

    private void resetCache() {
        engineMapping.clear();
        List<EngineServer> engines = engineServerDao.selectByMap(null);
        int size = engines == null ? 0 : engines.size();
        if (size > 0) {
            for (EngineServer engine : engines) {
                engineMapping.put(engine.getId(), engine);
            }
        }
    }

    private void sendPerformanceShiftMsg(Integer engineId) {
        PerformanceShiftEvent event = new PerformanceShiftEvent(PerformanceShiftListener.class, "performanceShift");
        event.setEngineId(engineId);
        messageService.addMessage(event);
    }

    @Override
    public void sendStatusChangeMsg(EngineServer engineServer, byte status) {
        EngineServerEvent evt = new EngineServerEvent(EngineServerListener.class, "statusChanged");
        evt.setEngineServer(engineServer);
        evt.setStatus(status);
        messageService.addMessage(evt);
    }

    @Override
    public void notify(List<URL> urls) {
        // logger.info("notify:{}", urls);
        // for (URL url : urls) {
        // if (!hosts.contains(url.getAddress())) {
        // hosts.add(url.getAddress());
        // }
        // logger.info("ZooKeeperService.notify.url={}", url);
        // logger.info("ZooKeeperService.notify.url.getAddress={}", url.getAddress());
        // // logger.info("ZooKeeperService.notify.url={}", url.getAuthority());
        // // logger.info("ZooKeeperService.notify.url={}", url.getBackupAddress());
        // // logger.info("ZooKeeperService.notify.url={}", url.getHost());
        // logger.info("ZooKeeperService.notify.url.getIp={}", url.getIp());
        // // logger.info("ZooKeeperService.notify.url={}", url.getPassword());
        // // logger.info("ZooKeeperService.notify.url={}", url.getPath());
        // logger.info("ZooKeeperService.notify.url.getPort={}", url.getPort());
        // // logger.info("ZooKeeperService.notify.url={}", url.getProtocol());
        // logger.info("ZooKeeperService.notify.url.getServiceInterface={}",
        // url.getServiceInterface());
        // // logger.info("ZooKeeperService.notify.url={}", url.getServiceKey());
        // // logger.info("ZooKeeperService.notify.url={}", url.getUsername());
        // }
        // logger.info("ZooKeeperService.notify.address:{}", hosts);
    }

    /**
     * 这是一个动作，而且一般只会发生一次,对于后上线或者重启的Engine，就存在通知不到的情况,所以还需要在消息通知上处理一次,详见NbiConnectionServiceImpl.java
     */
    @Override
    public void notifyEngineConnectNbi(String nbiIpAddress, int nbiPort, boolean isStart) {
        for (EngineServer engine : engineMapping.values()) {
            if (engine.getAdminStatus() == EngineServer.STOP) {
                continue;
            }
            byte linkStatus = engine.getLinkStatus();
            if (EngineServer.CONNECTED == linkStatus) {
                try {
                    notifyEngineConnectNbi(engine, nbiIpAddress, nbiPort, isStart);
                } catch (Exception e) {
                    logger.trace("", e);
                }

            }
        }
    }

    @Override
    public void notifyEngineConnectNbi(EngineServer engineServer, String nbiIpAddress, int nbiPort, boolean isStart) {
        ConfigEngineNbiFacade facade = facadeFactory.getFacade(engineServer, ConfigEngineNbiFacade.class);
        NbiAddress nbiAddress = new NbiAddress();
        nbiAddress.setNbiAddress(nbiIpAddress);
        nbiAddress.setNbiPort(nbiPort);
        nbiAddress.setStartNbi(isStart);
        facade.notifyEngineConnectNbi(nbiAddress);
    }

}
