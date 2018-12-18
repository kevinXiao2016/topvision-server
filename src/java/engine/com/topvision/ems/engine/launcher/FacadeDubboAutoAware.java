/***********************************************************************
 * $Id: FacadeDubboAutoAware.java,v1.0 2015年4月9日 上午8:29:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.launcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.ems.facade.CollectorFacade;
import com.topvision.ems.facade.domain.EngineServerParam;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.annotation.EngineFacade;

/**
 * @author Victor
 * @created @2015年4月9日-上午8:29:35
 *
 */
@Engine
public class FacadeDubboAutoAware implements FacadeAutoAware, BeanFactoryAware {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${engine.port}")
    private int servicePort;
    @Value("${java.rmi.server.hostname}")
    private String hostName;
    @Value("${dubbo.timeout:300000}")
    private int timeout;
    @Value("${dubbo.protocol:rmi}")
    private String dubboProtocol;
    private List<ServiceConfig<?>> exporters = null;
    private BeanFactory beanFactory;
    private ApplicationConfig application;
    private RegistryConfig registry;
    private ProtocolConfig protocol;

    @PostConstruct
    public void init() {
        logger.info("FacadeDubboAutoAware init...");
        servicePort = getServicePort();
        logger.info("FacadeDubboAutoAware.hostName:{}",hostName);
        if (hostName != null) {
            EnvironmentConstants.putEnv(EnvironmentConstants.HOSTNAME, hostName);
        }

        try {
            // Add by Victor@20160809用于engine退出
            EngineFacade engineFacade = CollectorFacade.class.getAnnotation(EngineFacade.class);
            RmiServiceExporter exporter = new RmiServiceExporter();
            exporter.setService(beanFactory.getBean(engineFacade.beanName()));
            exporter.setServiceName(engineFacade.serviceName());
            exporter.setRegistryPort(servicePort);
            exporter.setServiceInterface(CollectorFacade.class);
            exporter.afterPropertiesSet();

            engineFacade = CheckFacade.class.getAnnotation(EngineFacade.class);
            exporter = new RmiServiceExporter();
            exporter.setService(beanFactory.getBean(engineFacade.beanName()));
            exporter.setServiceName(engineFacade.serviceName());
            exporter.setRegistryPort(servicePort);
            exporter.setServiceInterface(CheckFacade.class);
            exporter.afterPropertiesSet();
            logger.info("CheckFacade is ready.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("SystemExit");
            System.exit(0);
        }
        Calendar calendar = Calendar.getInstance();
        EnvironmentConstants.putEnv(EnvironmentConstants.START_TIME, Long.toString(calendar.getTimeInMillis()));
        logger.info("CheckFacade started.");

        exporters = new ArrayList<ServiceConfig<?>>();
        // 当前应用配置
        application = new ApplicationConfig();
        // Modify by Victor@20151207解决采集端多IP的问题，可以通过配置文件解决
        String localhost = EnvironmentConstants.getHostAddress();
        // 服务提供者协议配置
        protocol = new ProtocolConfig();
        protocol.setHost(localhost);
        protocol.setName(dubboProtocol);
        protocol.setPort(servicePort);
        protocol.setThreads(200);

        logger.info("DubboProtocl started.");
    }

    @PreDestroy
    public void destroy() {
        if (exporters == null || exporters.isEmpty()) {
            return;
        }
        for (ServiceConfig<?> exporter : exporters) {
            exporter.unexport();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void facadeAutoAware(EngineServerParam param) {
        logger.info("FacadeDubboAutoAware facadeAutoAware...{}" + param );

        // 连接注册中心配置
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constants.FILE_KEY, "zookeeper/dubbo-registry-engine-.cache");
        registry = new RegistryConfig();
        registry.setAddress("zookeeper://" + param.getServiceIp() + ":" + param.getServicePort());
        registry.setUsername("ems");
        registry.setPassword("nm3000");
        registry.setCheck(false);
        registry.setParameters(parameters);
        logger.info("address:{},client:{},cluster{},server:{},transporter:{},Parameters:{}",registry.getAddress(),registry.getClient(),registry.getCluster(),
                registry.getServer(),registry.getTransporter(),registry.getParameters());

        application.setRegistry(registry);
        application.setName("Engine-" + param.getIp() + "-" + param.getPort());

        List<Class<?>> clazzes = CollectorContext.getInstance().getClassUtils()
                .findClassWithAnnotation(EngineFacade.class, "com.topvision");
        for (Class clazz : clazzes) {
            if (logger.isDebugEnabled()) {
                logger.debug("FacadeDubboAutoAware class:{}", clazz);
            }
            try {
                EngineFacade engineFacade = (EngineFacade) clazz.getAnnotation(EngineFacade.class);
                if (param.containsCategory(engineFacade.category())) {
                    Object obj = beanFactory.getBean(engineFacade.beanName());
                    export(clazz, obj);
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("Facade [{}] not register remote interface by user.", clazz);
                    }
                }
            } catch (Throwable e) {
                logger.error(clazz.getName(), e);
            }
        }
        logger.info("FacadeDubboAutoAware auto aware {} classes end!", clazzes.size());
    }

    private <T> void export(Class<T> clazz, T obj) {
        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
        // 服务提供者暴露服务配置
        ServiceConfig<T> service = new ServiceConfig<T>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        service.setApplication(application);
        service.setRegistry(registry); // 多个注册中心可以用setRegistries()
        service.setProtocol(protocol); // 多个协议可以用setProtocols()
        service.setInterface(clazz);
        service.setRef(obj);
        service.setVersion("1.0.0");
        service.setTimeout(timeout);
        /*
         * if (clazz.getSimpleName().equals("PerformanceFacade")) {
         * service.setLoadbalance("roundrobin"); } else { service.setLoadbalance("random"); }
         */
        // 暴露及注册服务
        service.export();
        exporters.add(service);
        if (logger.isInfoEnabled()) {
            logger.info("Facade [{}] register remote interface success!!!", clazz);
        }
    }

    @SuppressWarnings("rawtypes")
    private void unexport(Class clazz) {
        if (exporters == null || exporters.isEmpty()) {
            return;
        }
        List<ServiceConfig> unexports = new ArrayList<>();
        for (ServiceConfig<?> exporter : exporters) {
            if (exporter.getInterface().equals(clazz.getName())) {
                exporter.unexport();
                unexports.add(exporter);
            }
        }
        exporters.removeAll(unexports);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.BeanFactoryAware#
     *      setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public ApplicationConfig getApplication() {
        return application;
    }

    public RegistryConfig getRegistry() {
        return registry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.engine.launcher.FacadeAutoAware#facadeChangeAware(EngineServerParam)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void facadeChangeAware(EngineServerParam param) {
        List<String> export = param.getAddCategories();
        List<String> unexport = param.getDelCategories();
        List<Class<?>> clazzes = CollectorContext.getInstance().getClassUtils()
                .findClassWithAnnotation(EngineFacade.class, "com.topvision");
        if (unexport != null && unexport.size() > 0) {
            for (Class clazz : clazzes) {
                try {
                    EngineFacade engineFacade = (EngineFacade) clazz.getAnnotation(EngineFacade.class);
                    if (unexport.contains(engineFacade.category())) {
                        unexport(clazz);
                    }
                } catch (Throwable e) {
                    logger.error(clazz.getName(), e);
                }
            }
        }
        if (export != null && export.size() > 0) {
            for (Class clazz : clazzes) {
                try {
                    EngineFacade engineFacade = (EngineFacade) clazz.getAnnotation(EngineFacade.class);
                    if (export.contains(engineFacade.category())) {
                        Object obj = beanFactory.getBean(engineFacade.beanName());
                        export(clazz, obj);
                    }
                } catch (Throwable e) {
                    logger.error(clazz.getName(), e);
                }
            }
        }
    }

    public int getServicePort() {
        return Integer
                .parseInt(EnvironmentConstants.getEnv(EnvironmentConstants.ENGINE_PORT, String.valueOf(servicePort)));
    }
}
