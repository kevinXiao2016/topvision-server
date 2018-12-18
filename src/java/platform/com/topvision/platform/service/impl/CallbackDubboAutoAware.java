/***********************************************************************
 * $Id: CallbackDubboAutoAware.java,v1.0 2015年4月11日 上午8:15:38 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.ServiceConfig;
import com.topvision.framework.annotation.Callback;
import com.topvision.framework.common.ClassUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ServerBeanFactory;
import com.topvision.platform.service.CallbackAutoAware;
import com.topvision.platform.service.ZooKeeperService;

/**
 * @author Victor
 * @created @2015年4月11日-上午8:15:38
 *
 */
@Service
public class CallbackDubboAutoAware extends BaseService implements CallbackAutoAware {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private List<ServiceConfig<?>> exporters = null;
    @Autowired
    private ServerBeanFactory beanFactory;
    @Autowired
    private ZooKeeperService zooKeeperService;
    @Value("${dubbo.timeout:300000}")
    private int timeout;

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (exporters == null || exporters.isEmpty()) {
            return;
        }
        for (ServiceConfig<?> exporter : exporters) {
            exporter.unexport();
        }
    }

    /**
     * 在EngineServerService中调用，保证在engine连接之前准备好callback接口
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void export() {
        logger.info("CallbackDubboAutoAware binding service to Dubbo registry!");
        List<Class<?>> clazzes = new ClassUtils().findClassWithAnnotation(Callback.class, "com.topvision");
        exporters = new ArrayList<ServiceConfig<?>>(clazzes.size());
        if (logger.isDebugEnabled()) {
            logger.debug("CallbackDubboAutoAware class:{}", clazzes);
        }
        for (Class clazz : clazzes) {
            try {
                Callback facade = (Callback) clazz.getAnnotation(Callback.class);
                Object obj = beanFactory.getBean(facade.beanName());
                export(clazz, obj);
            } catch (Throwable e) {
                logger.error(clazz.getName(), e);
            }
        }
    }

    private <T> void export(Class<T> clazz, T obj) {
        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
        // 服务提供者暴露服务配置
        ServiceConfig<T> service = new ServiceConfig<T>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        service.setApplication(zooKeeperService.getApplication());
        service.setRegistry(zooKeeperService.getRegistry()); // 多个注册中心可以用setRegistries()
        service.setProtocol(zooKeeperService.getProtocol()); // 多个协议可以用setProtocols()
        service.setInterface(clazz);
        service.setRef(obj);
        service.setVersion("1.0.0");
        service.setTimeout(timeout);

        // 暴露及注册服务
        service.export();
        logger.info("Facade auto aware class:{}", clazz);
        exporters.add(service);
    }

    public void setBeanFactory(ServerBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setZooKeeperService(ZooKeeperService zooKeeperService) {
        this.zooKeeperService = zooKeeperService;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
