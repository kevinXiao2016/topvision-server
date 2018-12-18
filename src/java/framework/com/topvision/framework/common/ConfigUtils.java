/***********************************************************************
 * $Id: ConfigUtils.java,v 1.1 2010-1-25 下午03:40:05 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date 2010-1-25 下午03:40:05
 * 
 * @author kelers
 * 
 */
public class ConfigUtils extends Properties {
    private static final long serialVersionUID = -8308021465178291484L;
    protected static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
    public static ConfigUtils config = null;

    public static ConfigUtils getInstance() {
        if (config == null) {
            config = new ConfigUtils();
        }
        return config;
    }

    private Properties defaultProps;

    private Map<String, Set<String>> setCache;

    private ConfigUtils() {
        try {
            load(getClass().getResourceAsStream("/META-INF/tools.properties"));
        } catch (IOException e) {
            logger.error("The config file read error,The system will use default properties.", e);
        }
        setCache = new HashMap<String, Set<String>>();
        defaultProps = new Properties();
        // 配置项含义详见配置文件注释和配置说明文档
        defaultProps.put("jdbc.driver", "com.mysql.jdbc.Driver");
        defaultProps.put("jdbc.url", "jdbc:mysql://localhost:12121/ems");
        defaultProps.put("jdbc.username", "root");
        defaultProps.put("jdbc.password", "ems");
        defaultProps
                .put("backup.tables",
                        "AgentAddressBook,AgentMacBook,DeviceMonitor,Entity,EntityAddress,EntityAttribute,EntityFolderRela,EntitySnap,FolderUserGroupRela,FunctionItem,InterceptorFilter,InterceptorLog,IpAddress,IpAddressBook,LegalAddrBook,Link,MapNode,MenuItem,MacInterceptorFilter,MacWhiteList,NetworkEntity,Port,PortSnap,SnmpParam,SystemPreferences,TopoFolder,TopoLabel,TopologyParam,Users");
    }

    /**
     * 返回整数型配置值。
     * 
     * @param key
     *            参数名称
     * @return 参数值
     */
    public Integer getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.util.Properties#getProperty(java.lang.String)
     */
    @Override
    public String getProperty(String key) {
        String value = super.getProperty(key);
        if (value == null) {
            value = defaultProps.getProperty(key);
        }
        if (value == null) {
            throw new NullPointerException("Unknow key:" + key);
        }
        return value;
    }

    /**
     * 把配置文件中以逗号隔开的多项配置组合到一个HashSet中。
     * 
     * @param key
     *            key name
     * @return HashSet
     */
    public Set<String> getSet(String key) {
        Set<String> sets = setCache.get(key);
        if (sets == null) {
            sets = new HashSet<String>();
            setCache.put(key, sets);
        }
        StringTokenizer token = new StringTokenizer(getProperty(key), ";,");
        while (token.hasMoreTokens()) {
            sets.add(token.nextToken().trim());
        }
        return sets;
    }
}
