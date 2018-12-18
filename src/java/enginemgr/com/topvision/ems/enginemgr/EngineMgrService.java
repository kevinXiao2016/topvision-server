/***********************************************************************
 * $Id: EngineMgrService.java,v1.0 2016年6月18日 下午3:43:41 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.enginemgr;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.topvision.framework.EnvironmentConstants;

/**
 * @author Victor
 * @created @2016年6月18日-下午3:43:41
 *
 */
public class EngineMgrService {
    private static Logger logger = LoggerFactory.getLogger(EngineMgrService.class);
    private ApplicationContext applicationContext = null;

    private void startService() {
        try {
            applicationContext = new ClassPathXmlApplicationContext(
                    "classpath:META-INF/spring/applicationContext-engineMgr.xml");
            EngineManage engineManage = (EngineManage) applicationContext.getBean("engineManage");
            if (System.getProperty("java.rmi.server.hostname") == null) {
                EnvironmentConstants.putEnv("java.rmi.server.hostname", engineManage.getHostname());
            }
            RmiServiceExporter exporter = new RmiServiceExporter();
            exporter.setService(engineManage);
            exporter.setServiceName("engineManage");
            exporter.setRegistryPort(engineManage.getEngineMgrPort());
            exporter.setServiceInterface(EngineManage.class);
            exporter.afterPropertiesSet();
            if (logger.isInfoEnabled()) {
                logger.info("Engine manager env:{}", EnvironmentConstants.showEnvs());
                logger.info("Engine manager started!");
            }
        } catch (BeansException e) {
            logger.warn("", e);
        } catch (NumberFormatException e) {
            logger.warn("", e);
        } catch (RemoteException e) {
            logger.warn("", e);
        }
    }

    private void stopService() {
        try {
            applicationContext = new ClassPathXmlApplicationContext(
                    "classpath:META-INF/spring/applicationContext-engineMgr.xml");
            EngineManage engineManage = (EngineManage) applicationContext.getBean("engineManage");
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            StringBuilder serviceUrl = new StringBuilder("rmi://localhost:").append(engineManage.getEngineMgrPort())
                    .append("/engineManage");
            proxy.setServiceUrl(serviceUrl.toString());
            proxy.setServiceInterface(EngineManage.class);
            proxy.afterPropertiesSet();
            EngineManage object = (EngineManage) proxy.getObject();
            object.shutdownMgr();
        } catch (Exception e) {
            logger.info("EngineMgr Stop Service error:", e);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0 || "start".equals(args[0])) {
            new EngineMgrService().startService();
        } else if ("stop".equals(args[0])) {
            new EngineMgrService().stopService();
        }
    }
}
