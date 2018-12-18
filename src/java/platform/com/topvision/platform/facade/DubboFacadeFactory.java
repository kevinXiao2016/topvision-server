/***********************************************************************
 * $Id: DubboFacadeFactory.java,v1.0 2015年4月9日 上午8:22:15 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.dubbo.ReferenceConfigCache;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.service.ZooKeeperService;

/**
 * @author Victor
 * @created @2015年4月9日-上午8:22:15
 *
 */
@Service("facadeFactory")
public class DubboFacadeFactory extends FacadeFactory {
    @Autowired
    private ZooKeeperService zooKeeperService;
    @Value("${dubbo.timeout:300000}")
    private int timeout;
    @Value("${dubbo.protocol:rmi}")
    private String protocol;

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public <T> T getFacade(Class<T> clazz) {
        try {
            // 引用远程服务
            ReferenceConfig<T> reference = new ReferenceConfig<T>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
            reference.setApplication(zooKeeperService.getApplication());
            reference.setRegistry(zooKeeperService.getRegistry()); // 多个注册中心可以用setRegistries()
            reference.setInterface(clazz);
            reference.setVersion("1.0.0");
            reference.setCheck(true);
            reference.setScope("remote");
            reference.setTimeout(timeout);
            // 和本地bean一样使用xxxService
            ReferenceConfigCache cache = ReferenceConfigCache.getCache("server");
            return cache.get(reference);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    private int index = 0;

    @Override
    public <T> T getPerfFacade(Class<T> clazz) {
        if (servers.size() == 0) {
            return null;
        }
        EngineServer engine = null;
        // Add by Victor@20160908找出下一个
        for (int i = 0; i < servers.size(); i++) {
            if (index >= servers.size()) {
                index = 0;
            }
            engine = servers.get(index++);
            if (engine.hasType(EngineServer.TYPE_PERFORMANCE)) {
                break;
            } else {
                engine = null;
            }
        }
        if (engine == null) {
            return null;
        }
        return getFacade(engine, clazz);
    }

    @Override
    public <T> T getFacade(EngineServer engine, Class<T> clazz) {
        try {
            ReferenceConfig<T> reference = new ReferenceConfig<T>();
            reference.setApplication(zooKeeperService.getApplication());
            StringBuilder url = new StringBuilder(protocol).append("://");
            if ("127.0.0.1".equals(engine.getIp()) || "localhost".equals(engine.getIp())) {
                url.append(EnvironmentConstants.getEnv("java.rmi.server.hostname", engine.getIp()));
            } else {
                url.append(engine.getIp());
            }
            url.append(":").append(engine.getPort());
            String cacheName = url.toString();
            url.append("/").append(clazz.getName());
            reference.setUrl(url.toString());
            reference.setInterface(clazz);
            reference.setVersion("1.0.0");
            reference.setTimeout(timeout);
            // Modify by Victor@20151127
            // 原来使用Server统一缓存会导致CheckFacade多个Engine是同一个Facade，改为按engine的IP的缓存
            ReferenceConfigCache cache = ReferenceConfigCache.getCache(cacheName);
            return cache.get(reference);
        } catch (Exception e) {
            logger.error("Get reference remote facade {} error:{}", clazz.getName(), e.getMessage());
            logger.debug(clazz.getName(), e);
            return null;
        }
    }
}
