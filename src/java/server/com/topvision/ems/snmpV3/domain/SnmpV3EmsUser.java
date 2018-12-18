/***********************************************************************
 * $Id: SnmpEmsUserTable.java,v1.0 2013-1-9 上午10:19:54 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.domain;

/**
 * 网管端使用的SNMP V3 USER
 * 
 * @author Bravin
 * @created @2013-1-9-上午10:19:54
 * 
 */
public class SnmpV3EmsUser {
    private Long entityId;
    private String username;
    private Integer authProtocol;
    private String authKey;
    private Integer privProtocol;
    private String privKey;

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
     * @return the authProtocol
     */
    public Integer getAuthProtocol() {
        return authProtocol;
    }

    /**
     * @param authProtocol
     *            the authProtocol to set
     */
    public void setAuthProtocol(Integer authProtocol) {
        this.authProtocol = authProtocol;
    }

    /**
     * @return the authKey
     */
    public String getAuthKey() {
        return authKey;
    }

    /**
     * @param authKey
     *            the authKey to set
     */
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    /**
     * @return the privProtocol
     */
    public Integer getPrivProtocol() {
        return privProtocol;
    }

    /**
     * @param privProtocol
     *            the privProtocol to set
     */
    public void setPrivProtocol(Integer privProtocol) {
        this.privProtocol = privProtocol;
    }

    /**
     * @return the privKey
     */
    public String getPrivKey() {
        return privKey;
    }

    /**
     * @param privKey
     *            the privKey to set
     */
    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }

}
