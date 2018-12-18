/***********************************************************************
 * $Id: HsqlAbstractDao.java,v1.0 2015年3月12日 上午11:17:34 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Victor
 * @created @2015年3月12日-上午11:17:34
 *
 */
public abstract class HsqlAbstractDao {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected HsqlManager hsqlManager;

    @PostConstruct
    public void init() {
        initDatabase();
    }

    @PreDestroy
    public void destroy() {
    }

    /**
     * 初始化数据库,需要增加版本控制
     */
    protected abstract void initDatabase();

    /**
     * @return the conn
     */
    protected Connection getConn() {
        return hsqlManager.getConn();
    }

    protected Statement createStatement() {
        return hsqlManager.createStatement();
    }

    protected PreparedStatement prepareStatement(String sql) {
        return hsqlManager.prepareStatement(sql);
    }
}
