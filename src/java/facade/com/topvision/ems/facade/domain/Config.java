package com.topvision.ems.facade.domain;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2011-6-19 Time: 9:30:47 To change this
 * template use File | Settings | File Templates.
 */
public class Config implements Serializable {
    private static final long serialVersionUID = -4895997419639805103L;
    private String serverConfigValue;
    private String dbConfigValue;
    private String displayName;
    private Long configId;

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDbConfigValue() {
        return dbConfigValue;
    }

    public void setDbConfigValue(String dbConfigValue) {
        this.dbConfigValue = dbConfigValue;
    }

    public String getServerConfigValue() {
        return serverConfigValue;
    }

    public void setServerConfigValue(String serverConfigValue) {
        this.serverConfigValue = serverConfigValue;
    }

    public String toString() {
        return "Config{" + "configId=" + configId + ", serverConfigValue='" + serverConfigValue + '\''
                + ", dbConfigValue='" + dbConfigValue + '\'' + ", displayName='" + displayName + '\'' + '}';
    }
}