/***********************************************************************
 * $Id: EngineDaoFactory.java,v1.0 2015年3月11日 下午3:45:01 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.dao;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.util.EngineBeanDefinitionRegistry;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.common.ClassAware;

/**
 * @author Victor
 * @created @2015年3月11日-下午3:45:01
 *
 */
@Engine("engineDaoFactory")
public class EngineDaoFactory {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EngineBeanDefinitionRegistry beanRegistry;

    public synchronized <T> T getEngineDao(Class<T> c) {
        try {
            return beanRegistry.getBeanFactory().getBean(c);
        } catch (BeansException e) {
            ClassAware classAware = new ClassAware();
            Set<Class<?>> cSet = classAware.scanClass("com.topvision", c);
            logger.debug("Find Class {} match {}", cSet, c);
            for (Class<?> clazz : cSet) {
                if (clazz.isInterface()) {
                    continue;
                }
                beanRegistry.registerSingleton(c.getSimpleName(), clazz);
                return beanRegistry.getBeanFactory().getBean(c);
            }
            return null;
        }
    }
}
