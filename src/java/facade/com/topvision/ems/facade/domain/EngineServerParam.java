/***********************************************************************
 * $Id: EngineServerParam.java,v1.0 2015年3月7日 上午10:01:07 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Victor
 * @created @2015年3月7日-上午10:01:07
 *
 */
public class EngineServerParam implements Serializable {
    private static final long serialVersionUID = -7305867428942748445L;
    private Integer id;
    private String ip;
    private Integer port;
    private String jdbcDriverClassName;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private String serviceIp;
    private Integer servicePort;
    private Set<String> categories;
    private Integer acquireIncrement;
    private Integer checkoutTimeout;
    private Integer initialPoolSize;
    private Integer maxIdleTime;
    private Integer maxIdleTimeExcessConnections;
    private Integer maxPoolSize;
    private Integer minPoolSize;

    private List<String> addCategories;
    private List<String> delCategories;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EngineServerParam [id=");
        builder.append(id);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", port=");
        builder.append(port);
        builder.append(", jdbcDriverClassName=");
        builder.append(jdbcDriverClassName);
        builder.append(", jdbcUrl=");
        builder.append(jdbcUrl);
        builder.append(", jdbcUsername=");
        builder.append(jdbcUsername);
        builder.append(", jdbcPassword=");
        builder.append(jdbcPassword);
        builder.append(", serviceIp=");
        builder.append(serviceIp);
        builder.append(", servicePort=");
        builder.append(servicePort);
        builder.append(", categories=");
        builder.append(categories);
        builder.append(", acquireIncrement=");
        builder.append(acquireIncrement);
        builder.append(", checkoutTimeout=");
        builder.append(checkoutTimeout);
        builder.append(", initialPoolSize=");
        builder.append(initialPoolSize);
        builder.append(", maxIdleTime=");
        builder.append(maxIdleTime);
        builder.append(", maxIdleTimeExcessConnections=");
        builder.append(maxIdleTimeExcessConnections);
        builder.append(", maxPoolSize=");
        builder.append(maxPoolSize);
        builder.append(", minPoolSize=");
        builder.append(minPoolSize);
        builder.append("]");
        return builder.toString();
    }

    public Properties toProperties() {
        Properties prop = new Properties();
        prop.put("id", String.valueOf(id));
        prop.put("ip", ip);
        prop.put("port", String.valueOf(port));
        prop.put("jdbcDriverClassName", jdbcDriverClassName);
        prop.put("jdbcUrl", jdbcUrl);
        prop.put("jdbcUsername", jdbcUsername);
        prop.put("jdbcPassword", jdbcPassword);
        prop.put("serviceIp", serviceIp);
        prop.put("servicePort", String.valueOf(servicePort));
        prop.put("acquireIncrement", String.valueOf(acquireIncrement));
        prop.put("checkoutTimeout", String.valueOf(checkoutTimeout));
        prop.put("initialPoolSize", String.valueOf(initialPoolSize));
        prop.put("maxIdleTime", String.valueOf(maxIdleTime));
        prop.put("maxIdleTimeExcessConnections", String.valueOf(maxIdleTimeExcessConnections));
        prop.put("maxPoolSize", String.valueOf(maxPoolSize));
        prop.put("minPoolSize", String.valueOf(minPoolSize));
        return prop;
    }

    public void load(Properties prop) {
        id = Integer.parseInt(prop.getProperty("id"));
        ip = prop.getProperty("ip");
        port = Integer.parseInt(prop.getProperty("port"));
        jdbcDriverClassName = prop.getProperty("jdbcDriverClassName");
        jdbcUrl = prop.getProperty("jdbcUrl");
        jdbcUsername = prop.getProperty("jdbcUsername");
        jdbcPassword = prop.getProperty("jdbcPassword");
        serviceIp = prop.getProperty("serviceIp");
        servicePort = Integer.parseInt(prop.getProperty("servicePort"));

        acquireIncrement = Integer.parseInt(prop.getProperty("acquireIncrement"));
        checkoutTimeout = Integer.parseInt(prop.getProperty("checkoutTimeout"));
        initialPoolSize = Integer.parseInt(prop.getProperty("initialPoolSize"));
        maxIdleTime = Integer.parseInt(prop.getProperty("maxIdleTime"));
        maxIdleTimeExcessConnections = Integer.parseInt(prop.getProperty("maxIdleTimeExcessConnections"));
        maxPoolSize = Integer.parseInt(prop.getProperty("maxPoolSize"));
        minPoolSize = Integer.parseInt(prop.getProperty("minPoolSize"));
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

    public String getJdbcDriverClassName() {
        return jdbcDriverClassName;
    }

    public void setJdbcDriverClassName(String jdbcDriverClassName) {
        this.jdbcDriverClassName = jdbcDriverClassName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
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

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category) {
        if (categories == null) {
            categories = new HashSet<String>();
        }
        categories.add(category);
    }

    public boolean containsCategory(String category) {
        if (categories == null || categories.isEmpty()) {
            return false;
        }
        return categories.contains(category);
    }

    public Integer getAcquireIncrement() {
        return acquireIncrement;
    }

    public void setAcquireIncrement(Integer acquireIncrement) {
        this.acquireIncrement = acquireIncrement;
    }

    public Integer getCheckoutTimeout() {
        return checkoutTimeout;
    }

    public void setCheckoutTimeout(Integer checkoutTimeout) {
        this.checkoutTimeout = checkoutTimeout;
    }

    public Integer getInitialPoolSize() {
        return initialPoolSize;
    }

    public void setInitialPoolSize(Integer initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    public Integer getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Integer maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public Integer getMaxIdleTimeExcessConnections() {
        return maxIdleTimeExcessConnections;
    }

    public void setMaxIdleTimeExcessConnections(Integer maxIdleTimeExcessConnections) {
        this.maxIdleTimeExcessConnections = maxIdleTimeExcessConnections;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(Integer minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    /**
     * @return the addCategories
     */
    public List<String> getAddCategories() {
        return addCategories;
    }

    /**
     * @param addCategories the addCategories to set
     */
    public void setAddCategories(List<String> addCategories) {
        this.addCategories = addCategories;
    }

    /**
     * @return the delCategories
     */
    public List<String> getDelCategories() {
        return delCategories;
    }

    /**
     * @param delCategories the delCategories to set
     */
    public void setDelCategories(List<String> delCategories) {
        this.delCategories = delCategories;
    }

}
