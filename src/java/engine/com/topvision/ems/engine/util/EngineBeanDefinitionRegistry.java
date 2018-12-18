/***********************************************************************
 * $Id: EngineBeanDefinitionRegistry.java,v1.0 2015年3月11日 下午4:26:24 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import com.topvision.framework.annotation.Engine;

/**
 * @author Victor
 * @created @2015年3月11日-下午4:26:24
 *
 */
@Engine("engineBeanDefinitionRegistry")
public class EngineBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {
    private ConfigurableListableBeanFactory beanFactory;

    /**
     * @param name
     * @param obj
     */
    public void registerSingleton(String name, Object obj) {
        beanFactory.registerSingleton(name, obj);
    }

    public void registerSingleton(String name, Class<?> c) {
        beanFactory.registerSingleton(name, beanFactory.createBean(c));
    }

    /**
     * 根据给定类返回所有实现该类的bean
     * 
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getBeans(Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (String name : beanFactory.getBeanDefinitionNames()) {
            Object o = beanFactory.getBean(name);
            try {
                if (clazz.isAssignableFrom(o.getClass())) {
                    list.add((T) o);
                }
            } catch (Exception e) {
            }
        }
        return list;

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    }

    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
