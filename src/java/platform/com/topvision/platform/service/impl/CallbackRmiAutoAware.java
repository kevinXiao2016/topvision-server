/***********************************************************************
 * $Id: CallbackRmiAutoAware.java,v1.0 2015年3月25日 下午4:19:31 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.EnvironmentConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.topvision.framework.annotation.Callback;
import com.topvision.framework.common.ClassUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ServerBeanFactory;
import com.topvision.platform.service.CallbackAutoAware;

/**
 * @author Victor
 * @created @2015年3月25日-下午4:19:31
 *
 */
//暂时不用，使用dubbo替代
public class CallbackRmiAutoAware extends BaseService implements CallbackAutoAware {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${rmi.port}")
    private int servicePort;
    @Value("${java.rmi.server.hostname:127.0.0.1}")
    private String server;
    private List<RmiServiceExporter> exporters = null;
    @Autowired
    protected ServerBeanFactory beanFactory;

    @Override
    public void destroy() {
        super.destroy();
        if (exporters == null || exporters.isEmpty()) {
            return;
        }
        for (RmiServiceExporter exporter : exporters) {
            try {
                exporter.destroy();
            } catch (RemoteException e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    /**
     * 在EngineServerService中调用，保证在engine连接之前准备好callback接口
     */
    public void export() {
        logger.info("CallbackRmiAutoAware binding service to RMI registry!");
        List<Class<?>> clazzes = new ClassUtils().findClassWithAnnotation(Callback.class, "com.topvision");
        exporters = new ArrayList<RmiServiceExporter>(clazzes.size());
        logger.debug("CallbackRmiAutoAware class:{}", clazzes);
        for (Class<?> clazz : clazzes) {
            try {
                Callback facade = clazz.getAnnotation(Callback.class);
                RmiServiceExporter exporter = new RmiServiceExporter();
                exporter.setRegistryHost(server);
                exporter.setService(beanFactory.getBean(facade.beanName()));
                exporter.setServiceName(facade.serviceName());
                exporter.setServicePort(servicePort);
                exporter.setRegistryPort(servicePort);
                exporter.setServiceInterface(clazz);
                exporter.afterPropertiesSet();
                exporters.add(exporter);
                exporter.getService();
                logger.info("Server callback auto aware class:{}", clazz);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
