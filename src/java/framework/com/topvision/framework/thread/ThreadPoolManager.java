package com.topvision.framework.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 2010-9-1
 * Time: 9:36:24
 * To change this template use File | Settings | File Templates.
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service("threadPoolManager")
public class ThreadPoolManager<T> implements BeanFactoryAware {
    private Logger logger = logger = LoggerFactory.getLogger(getClass());
    protected int threadCount;
    protected String threadBeanName;
    private BeanFactory beanFactory;
    public Vector<Poolable<T>> vector;

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getThreadBeanName() {
        return threadBeanName;
    }

    public void setThreadBeanName(String threadBeanName) {
        this.threadBeanName = threadBeanName;
    }

    public void initialize() {
        setThreadCount(threadCount);
        logger.trace("Starting thread pool...["+threadCount+"]");
        vector = new Vector<Poolable<T>>();
        for (int i = 1; i <= threadCount; i++) {
            Poolable<T> poolable = (Poolable<T>) beanFactory.getBean(threadBeanName);
            vector.addElement(poolable);
            poolable.start();
        }
    }

    public boolean process(T argument) {
        int i;
        for (i = 0; i < vector.size(); i++) {
            Poolable<T> currentThread =  vector.elementAt(i);
            if (!currentThread.isRunning()) {
                logger.trace("Thread " + (i + 1) + " is processing:" +
                        argument);
                currentThread.setArgument(argument);
                currentThread.setRunning(true);
                return true;
            }
        }
        logger.trace("pool is full, try in another time.");
        return false;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}