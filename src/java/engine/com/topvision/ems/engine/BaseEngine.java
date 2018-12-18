/***********************************************************************
 * $Id: BaseEngine.java,v1.0 2015年3月16日 下午4:05:00 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.engine.launcher.FacadeDubboAutoAware;
import com.topvision.framework.annotation.Callback;
import com.topvision.framework.dubbo.ReferenceConfigCache;

/**
 * @author Victor
 * @created @2015年3月16日-下午4:05:00
 *
 */
public abstract class BaseEngine extends EmsFacade implements BaseEngineIf{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    //server端的zookeeper ip
    protected String serviceIp;
    //server端的zookeeper端口
    protected Integer servicePort;
    //engine id
    protected Integer id;
    //local ip
    protected String ip;
    //local port
    protected Integer port;
    @Autowired
    private FacadeDubboAutoAware facadeDubboAutoAware;
    @Value("${dubbo.timeout:300000}")
    private int timeout;

    /**
     * Spring调用初始化
     */
    @PostConstruct
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} initialize invoked.", getClass());
        }
    }

    /**
     * Spring销毁bean的时候调用此方法.
     */
    @PreDestroy
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} destroy invoked.", getClass());
        }
    }

    /**
     * engine被server端连接后调用
     */
    public void connected() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} connected invoked.", getClass());
        }
    }

    /**
     * engine与server失联后调用
     */
    public void disconnected() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} disconnected invoked.", getClass());
        }
    }

    protected <T> T getCallback(Class<T> interfaze) {
        T l = null;
        Callback facade = interfaze.getAnnotation(Callback.class);
        l = getDubboCallback(facade, interfaze);
        return l;
    }

    @SuppressWarnings("deprecation")
    private <T> T getDubboCallback(Callback facade, Class<T> interfaze) {
        T l = null;
        // 引用远程服务
        ReferenceConfig<T> reference = new ReferenceConfig<T>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(facadeDubboAutoAware.getApplication());
        reference.setRegistry(facadeDubboAutoAware.getRegistry());
        //reference.setUrl(new StringBuilder("dubbo://").append(serviceIp).append(":").append(servicePort).append("/")
        //  .append(interfaze.getName()).toString());
        reference.setInterface(interfaze);
        reference.setVersion("1.0.0");
        reference.setCheck(true);
        reference.setInjvm(false);
        reference.setTimeout(timeout);

        ReferenceConfigCache cache = ReferenceConfigCache.getCache("engine");
        l = cache.get(reference);
        if (logger.isDebugEnabled()) {
            logger.debug("getListener.Proxy:{}", facade.serviceName());
        }
        return l;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }
}
