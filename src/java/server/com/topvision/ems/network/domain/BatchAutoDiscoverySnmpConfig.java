/***********************************************************************
 * $Id: BatchAutoDiscoverySnmpConfig.java,v1.0 2014-5-11 下午2:23:52 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2014-5-11-下午2:23:52
 *
 */
public class BatchAutoDiscoverySnmpConfig implements AliasesSuperType {
    private static final long serialVersionUID = -8213297209222091578L;

    private Long id;
    private String name;
    private String readCommunity;
    private String writeCommunity;
    private Integer version;
    private String username;
    private String authProtocol;
    private String authPassword;
    private String privProtocol;
    private String privPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthProtocol() {
        return authProtocol;
    }

    public void setAuthProtocol(String authProtocol) {
        this.authProtocol = authProtocol;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getPrivProtocol() {
        return privProtocol;
    }

    public void setPrivProtocol(String privProtocol) {
        this.privProtocol = privProtocol;
    }

    public String getPrivPassword() {
        return privPassword;
    }

    public void setPrivPassword(String privPassword) {
        this.privPassword = privPassword;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BatchAutoDiscoverySnmpConfig [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", readCommunity=");
        builder.append(readCommunity);
        builder.append(", writeCommunity=");
        builder.append(writeCommunity);
        builder.append(", version=");
        builder.append(version);
        builder.append(", username=");
        builder.append(username);
        builder.append(", authProtocol=");
        builder.append(authProtocol);
        builder.append(", authPassword=");
        builder.append(authPassword);
        builder.append(", privProtocol=");
        builder.append(privProtocol);
        builder.append(", privPassword=");
        builder.append(privPassword);
        builder.append("]");
        return builder.toString();
    }

}
