/***********************************************************************
 * $Id: ConnectivityStrategy.java,v1.0 2017年9月8日 下午1:26:29 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vanzand
 * @created @2017年9月8日-下午1:26:29
 *
 */
public class ConnectivityStrategy implements Serializable, Comparable<ConnectivityStrategy> {
    private static final long serialVersionUID = -7358923609527556227L;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final String CONNECT_STRATEGY = "connectStrategy";
    public static final String ICMP_CONNECT_STRATEGY = "icmp";
    public static final String SNMP_CONNECT_STRATEGY = "snmp";
    public static final String TCP_CONNECT_STRATEGY = "tcp";

    protected String ip;

    private String name;
    // 次序，优先级
    private int order = 0;
    private Properties properties;

    public ConnectivityStrategy(String name) {
        this.name = name;
    }

    public Integer connect() {
        return null;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(ConnectivityStrategy o) {
        return this.order - o.order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    

}
