package com.topvision.ems.network.domain;

import java.util.Date;

import com.topvision.framework.domain.BaseEntity;

public class DeviceResponse extends BaseEntity {
    private static final long serialVersionUID = 1583456667529810076L;
    private String name;
    private String ip;
    private Float delay;
    private String typeName;
    private Long entityId;
    private Date collectTime;

    /**
     * @return the collectTime
     */
    public Date getCollectTime() {
        return collectTime;
    }

    /**
     * @return the delay
     */
    public Float getDelay() {
        return delay;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @param delay
     *            the delay to set
     */
    public void setDelay(Float delay) {
        this.delay = delay;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param typeName
     *            the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
