/**
 * 
 */
package com.topvision.ems.facade.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author niejun
 * 
 */
public class HostService extends BaseEntity implements AliasesSuperType {

    private static final long serialVersionUID = -7582048708194883042L;

    /**
     * 使用NMAP定义的6中端口在状态.
     */
    public static byte OPEN = 1;
    public static byte CLOSED = 2;
    public static byte FILTERED = 3;
    public static byte UNFILTERED = 4;
    public static byte OPEN_FILTERED = 5;
    public static byte CLOSED_FILTERED = 6;

    private String ipAddr;

    private long serviceId;
    private long entityId;
    private int port;
    private String serviceName;

    // 端口状态
    private int state = CLOSED;

    // tcp or udp
    private String protocol;

    public long getEntityId() {
        return entityId;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public long getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getState() {
        return state;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setState(int state) {
        this.state = state;
    }

}