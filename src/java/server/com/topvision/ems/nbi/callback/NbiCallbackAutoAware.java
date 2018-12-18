/***********************************************************************
 * $Id: NbiCallbackExportService.java,v1.0 2016年3月22日 下午7:54:56 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.callback;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.EnvironmentConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Service;

import com.topvision.framework.common.ClassUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.performance.nbi.api.RemoteCallback;
import com.topvision.platform.ServerBeanFactory;

/**
 * @author Bravin
 * @created @2016年3月22日-下午7:54:56
 *
 */
@Service
public class NbiCallbackAutoAware extends BaseService {
    private List<RmiServiceExporter> exporters = null;
    @Autowired
    private ServerBeanFactory beanFactory;
    @Value("${rmi.port:3002}")
    private int serverPort;
    @Value("${java.rmi.server.hostname:127.0.0.1}")
    private String server;

    @Override
    public void initialize() {
        List<Class<?>> clazzes = new ClassUtils().findClassWithAnnotation(RemoteCallback.class, "com.topvision");
        exporters = new ArrayList<RmiServiceExporter>(clazzes.size());
        if (logger.isDebugEnabled()) {
            logger.debug("NbiCallbackAutoAware class:{}", clazzes);
        }
        for (Class<?> clazz : clazzes) {
            try {
                RemoteCallback facade = clazz.getAnnotation(RemoteCallback.class);
                export(clazz, facade.beanName());
            } catch (Throwable e) {
                logger.error(clazz.getName(), e);
            }
        }
    }

    protected void export(Class<?> clazz, String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("RmiAutoAware class:{}", clazz);
        }
        try {
            RmiServiceExporter exporter = new RmiServiceExporter();
            exporter.setRegistryHost(server);
            exporter.setService(beanFactory.getBean(name));
            exporter.setServiceInterface(clazz);
            exporter.setServiceName(name);
            exporter.setServicePort(serverPort);
            exporter.setRegistryPort(serverPort);
            exporter.afterPropertiesSet();
            exporter.prepare();
            exporters.add(exporter);
            if (logger.isInfoEnabled()) {
                logger.info("Rmi interface [{}] deploy success!!!", clazz);
            }
        } catch (Throwable e) {
            logger.error(clazz.getName(), e);
        }
    }
}
