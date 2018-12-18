package com.topvision.ems.engine;

import javax.annotation.PreDestroy;

public interface BaseEngineIf {

    /**
     * Spring调用初始化
     */
    void initialize();

    /**
     * Spring销毁bean的时候调用此方法.
     */
    @PreDestroy
    void destroy();

    /**
     * engine被server端连接后调用
     */
    void connected();

    /**
     * engine与server失联后调用
     */
    void disconnected();

    void setServiceIp(String serviceIp);

    void setServicePort(Integer servicePort);

    void setId(Integer id);

    void setIp(String ip);

    void setPort(Integer port);
}
