package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class Services extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -7519099531982738372L;
    public static final byte GENERAL_SERVICE = 0;
    public static final byte DATABASE_SERVICE = 1;
    public static final byte MIDDLEWARE_SERVICE = 2;
    public static final byte MAIL_SERVICE = 3;
    public static final byte NEWS_SERVICE = 4;
    public static final byte OTHER_SERVICE = 10;

    private String name;
    private Integer port = 0;
    private Long timeout;
    private String protocol;
    private Boolean scaned;
    private Byte type = GENERAL_SERVICE;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return the scaned
     */
    public Boolean getScaned() {
        return scaned;
    }

    /**
     * @return the timeout
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * @return the type
     */
    public Byte getType() {
        return type;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * @param protocol
     *            the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @param scaned
     *            the scaned to set
     */
    public void setScaned(Boolean scaned) {
        this.scaned = scaned;
    }

    /**
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Byte type) {
        this.type = type;
    }
}
