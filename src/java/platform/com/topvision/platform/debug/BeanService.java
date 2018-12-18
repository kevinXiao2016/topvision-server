/***********************************************************************
 * $Id: BeanService.java,v1.0 2013-10-28 下午1:20:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.debug;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.topvision.framework.annotation.Engine;

/**
 * @author Victor
 * @created @2013-10-28-下午1:20:35
 * 
 */
@Engine
public class BeanService implements BeanFactoryAware {
    public static BeanService beanService;
    private BeanFactory beanFactory;

    public static BeanService getInstance() {
        return beanService;
    }

    @PostConstruct
    public void init() {
        beanService = this;
    }

    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    public Object getBean(String name, Object... args) {
        return beanFactory.getBean(name, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans
     * .factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory bf) throws BeansException {
        this.beanFactory = bf;
    }

    /**
     * @return the beanFactory
     */
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
