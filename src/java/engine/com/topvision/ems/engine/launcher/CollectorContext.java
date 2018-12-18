/***********************************************************************
 * $Id: CollectorContext.java,v 1.1 Mar 12, 2009 7:37:22 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.launcher;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.topvision.ems.facade.CollectorFacade;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.common.ClassUtils;

/**
 * @Create Date Mar 12, 2009 7:37:22 PM
 * 
 * @author kelers
 * 
 */
public class CollectorContext {
    public static CollectorContext getInstance() {
        if (context == null) {
            context = new CollectorContext();
        }
        return context;
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static CollectorContext context;
    private AbstractApplicationContext applicationContext = null;

    private ClassUtils classUtils = null;
    private SqlSessionTemplate sqlSessionTemplate;

    private CollectorContext() {
        classUtils = new ClassUtils();
    }

    public void startService() {
        if (applicationContext == null) {
            String[] configs = { EnvironmentConstants.getEnv("configFile"),
                    "classpath*:com/topvision/ems/engine/**/engineContext-*.xml",
                    "classpath*:com/topvision/ems/**/engine/**/engineContext-*.xml",
                    "classpath*:com/topvision/ems/engine/**/businessContext-*.xml",
                    "classpath:META-INF/spring/applicationContext-end.xml" };
            applicationContext = new ClassPathXmlApplicationContext(configs);
        }
    }

    public void stopService() {
        try {
            applicationContext = new ClassPathXmlApplicationContext(
                    "classpath:META-INF/spring/applicationContext-shutdown.xml");
            FacadeDubboAutoAware facadeDubboAutoAware = (FacadeDubboAutoAware) applicationContext
                    .getBean("facadeDubboAutoAware");
            EngineFacade engineFacade = CollectorFacade.class.getAnnotation(EngineFacade.class);
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            StringBuilder serviceUrl = new StringBuilder("rmi://localhost:")
                    .append(facadeDubboAutoAware.getServicePort()).append("/").append(engineFacade.serviceName());
            proxy.setServiceUrl(serviceUrl.toString());
            proxy.setServiceInterface(CollectorFacade.class);
            proxy.afterPropertiesSet();
            CollectorFacade object = (CollectorFacade) proxy.getObject();
            object.shutdown();
        } catch (Exception e) {
            logger.info("Engine Stop Service error:", e);
        }
    }

    /**
     * @return the classUtils
     */
    public ClassUtils getClassUtils() {
        return classUtils;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
