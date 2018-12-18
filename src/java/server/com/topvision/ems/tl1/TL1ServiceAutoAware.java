/***********************************************************************
 * $Id: TL1ServiceAutoAware.java,v1.0 2017年1月13日 上午10:21:34 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.tl1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Service;

import com.topvision.framework.common.ClassUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.nbi.tl1.api.annotation.TL1RemoteCallback;
import com.topvision.platform.ServerBeanFactory;

/**
 * @author Bravin
 * @created @2017年1月13日-上午10:21:34
 *
 */
@Service
public class TL1ServiceAutoAware extends BaseService {
    private List<RmiServiceExporter> exporters = null;
    @Autowired
    private ServerBeanFactory beanFactory;
    @Value("${rmi.port:3002}")
    private int serverPort;
    @Value("${java.rmi.server.hostname:127.0.0.1}")
    private String server;

    @Override
    public void start() {
        List<Class<?>> clazzes = new ClassUtils().findClassWithAnnotation(TL1RemoteCallback.class, "com.topvision");
        exporters = new ArrayList<RmiServiceExporter>(clazzes.size());
        if (logger.isDebugEnabled()) {
            logger.debug("TL1CallbackAutoAware class:{}", clazzes);
        }
        for (Class<?> clazz : clazzes) {
            try {
                TL1RemoteCallback facade = clazz.getAnnotation(TL1RemoteCallback.class);
                export(clazz, facade.value());
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
            exporter.setServiceName(clazz.getSimpleName());
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
