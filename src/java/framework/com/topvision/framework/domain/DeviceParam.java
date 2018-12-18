/***********************************************************************
 * $Id: DeviceParam.java,v1.0 2011-8-17 下午04:44:44 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Victor
 * @created @2011-8-17-下午04:44:44
 * 
 */
public class DeviceParam implements Serializable {
    private static final long serialVersionUID = 2030698730532359989L;
    private Long entityId;
    private String ipAddress;
    private String username;
    private String password;
    //Add by Victor@20131123增加属性，便于扩展
    private Map<String, String> attributes = new HashMap<String, String>();

    public void put(String key, String value) {
        attributes.put(key, value);
    }

    public String get(String key) {
        return attributes.get(key);
    }

    public Map<String, String> attributes() {
        return attributes;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress
     *            the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeviceParam [entityId=");
        builder.append(entityId);
        builder.append(", ipAddress=");
        builder.append(ipAddress);
        builder.append(", username=");
        builder.append(username);
        builder.append(", password=");
        builder.append(password);
        builder.append(", attributes=");
        builder.append(attributes);
        builder.append("]");
        return builder.toString();
    }
}
