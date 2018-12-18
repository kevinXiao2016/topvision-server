/***********************************************************************
 * $Id: DatabaseManager.java,v 1.1 Sep 26, 2009 11:49:11 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.framework.domain.DataRecoveryResult;
import com.topvision.framework.domain.TableInfo;

/**
 * @Create Date Sep 26, 2009 11:49:11 AM
 * 
 * @author kelers
 * 
 */
public abstract class DatabaseManager {
    protected static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    @Value("${jdbc.driverClassName}")
    protected String driverClass;
    @Value("${jdbc.url}")
    protected String url;
    @Value("${jdbc.username}")
    protected String username;
    @Value("${jdbc.password}")
    protected String password;
    @Resource(name = "versionManager")
    protected VersionManager versionManager;

    /**
     * 如果数据不存在则建立数据库
     */
    public abstract void createDatabase();

    /**
     * 删除数据库
     */
    public abstract void dropDatabase();

    public abstract String exportDataBaseScript(String tableNames) throws Exception;

    public abstract List<String> processRecoveryResult(List<TableInfo> tableInfos, DataRecoveryResult dataRecoveryResult);

    /**
     * 初始化数据库
     */
    public void initDatabase() {
        try {
            versionManager.initialize();
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /**
     * @return the driverClass
     */
    public final String getDriverClass() {
        return driverClass;
    }

    /**
     * @return the password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @return the url
     */
    public final String getUrl() {
        return url;
    }

    /**
     * @return the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * @param driverClass
     *            the driverClass to set
     */
    public final void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * @param password
     *            the password to set
     */
    public final void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param url
     *            the url to set
     */
    public final void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param username
     *            the username to set
     */
    public final void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param versionManager
     *            the versionManager to set
     */
    public final void setVersionManager(VersionManager versionManager) {
        this.versionManager = versionManager;
    }
}
