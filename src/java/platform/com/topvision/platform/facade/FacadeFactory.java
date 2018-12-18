/***********************************************************************
 * $Id: FacadeFactory.java,v 1.1 Mar 12, 2009 8:39:45 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.facade;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.topvision.ems.enginemgr.EngineManage;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.platform.ServerBeanFactory;
import com.topvision.platform.dao.EngineServerDao;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.message.event.EngineServerEvent;
import com.topvision.platform.message.event.EngineServerListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author kelers
 * 
 * @Create Date Mar 12, 2009 8:39:45 PM
 */
public abstract class FacadeFactory implements EngineServerListener {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected ServerBeanFactory beanFactory;
    @Autowired
    @Qualifier("engineServerDao")
    private EngineServerDao engineServerDao;
    // 需要保证默认EngineServer放在list的第一个
    protected List<EngineServer> servers;
    @Autowired
    private MessageService messageService;
    @Value("${engine.mgr.port:3009}")
    private Integer engineManagePort;

    /**
     * 初始化
     */
    @PostConstruct
    public void initialize() {
        Map<String, String> eMap = new HashMap<String, String>();
        eMap.put("adminStatus", "1");
        servers = engineServerDao.selectByMap(eMap);
        messageService.addListener(EngineServerListener.class, this);
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        servers.clear();
    }

    @Override
    public void statusChanged(EngineServerEvent event) {
        switch (event.getStatus()) {
        case EngineServerEvent.STATUS_ADDED:
        case EngineServerEvent.STATUS_REMOVED:
        case EngineServerEvent.STATUS_CONNECTED:
        case EngineServerEvent.STATUS_DISABLED:
        case EngineServerEvent.STATUS_DISCONNECTED:
        case EngineServerEvent.STATUS_ENABLED:
        default:
            Map<String, String> eMap = new HashMap<String, String>();
            eMap.put("adminStatus", "1");
            servers = engineServerDao.selectByMap(eMap);
        }
    }

    /**
     * 获取CheckFacade，在dubbo模式下CheckFacade是总开，其他Facade只有连接上server后才会开启
     * 
     * @Modify by Rod CheckFacade 不再使用dubbo去管理,系统只有CheckFacade直接使用RMI协议
     * 
     * 
     * @param engine
     *            engine端的参数
     * @return 返回的对应Engine端的Facade实现远程调用
     */
    public CheckFacade getCheckFacade(EngineServer engine) {
        try {
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            StringBuilder serviceUrl = new StringBuilder("rmi://").append(engine.getIp()).append(":")
                    .append(engine.getPort()).append("/CheckFacade");
            proxy.setServiceUrl(serviceUrl.toString());
            proxy.setServiceInterface(CheckFacade.class);
            proxy.setRegistryClientSocketFactory(new RMIClientSocketFactory() {

                @Override
                public Socket createSocket(String host, int port) throws IOException {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(host, port), 5000);
                    return socket;
                }
            });
            proxy.afterPropertiesSet();
            CheckFacade checkFacade = (CheckFacade) proxy.getObject();
            return checkFacade;
        } catch (Exception e) {
            logger.error("RMI Protocol Get CheckFacade Error:", e.getMessage());
            return null;
        }
    }

    /**
     * 
     * 
     * @param ip
     * @return
     */
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
            logger.info("RMI Protocol Get EngineManage Error");
            return null;
        }
    }

    /**
     * @return 所有配置的Engine，包括不能够连接的
     */
    public List<EngineServer> getAllEngineServer() {
        return servers;
    }

    /**
     * 通过EngineId获取所使用的采集端
     *
     * @param engineId
     *            engineId
     * @return 采集端信息
     */
    public EngineServer getEngineServer(Integer engineId) {
        if (engineId != null) {
            for (EngineServer server : servers) {
                if (server.getId().equals(engineId)) {
                    return server;

                }
            }
        }
        return servers.get(0);
    }

    /**
     * @param type
     *            engine的类型，支持Default,CmPoll,Performance
     * @return 所有支持给定类型的engine
     */
    public List<EngineServer> getEngineServerByType(String type) {
        List<EngineServer> list = new ArrayList<EngineServer>();
        for (EngineServer s : servers) {
            if (s.getType().indexOf(type) != -1) {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 负载均衡
     * 
     * @param clazz
     * @return
     */
    public <T> T getDefaultFacade(Class<T> clazz) {
        return getFacade(clazz);
    }

    /**
     * 从负载均衡得到给定的门面.
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    public <T> T getFacade(String ip, Class<T> clazz) {
        return getFacade(clazz);
    }

    /**
     * 负载均衡
     * 
     * @param clazz
     * @return
     */
    public abstract <T> T getFacade(Class<T> clazz);

    /**
     * 不通过zookeeper，直接返回各个engine的facade
     * 
     * @param clazz
     * @return
     */
    public <T> List<T> getAllFacade(Class<T> clazz) {
        List<T> facades = new ArrayList<T>();
        for (EngineServer server : servers) {
            T t = (T) getFacade(server, clazz);
            if (t != null) {
                facades.add(t);
            }
        }
        return facades;
    }

    /**
     * 重置EngineServer缓存
     * 
     */
    public void updateEngineStatusBeforeStart() {
        Map<String, String> eMap = new HashMap<String, String>();
        eMap.put("adminStatus", "1");
        servers = engineServerDao.selectByMap(eMap);
    }

    /**
     * 得到给定Engine上的Facade,非负载均衡
     * 
     * @param <T>
     * @param clazz
     * @param engine
     * @return
     */
    public abstract <T> T getFacade(EngineServer server, Class<T> clazz);

    /**
     * 性能模块专用
     * 
     * @param clazz
     * @return
     */
    public abstract <T> T getPerfFacade(Class<T> clazz);
}
