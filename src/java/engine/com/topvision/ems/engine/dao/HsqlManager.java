/***********************************************************************
 * $Id: HsqlManager.java,v1.0 2015年3月12日 上午10:50:02 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.annotation.Engine;

/**
 * @author Victor
 * @created @2015年3月12日-上午10:50:02
 *
 */
@Engine("hsqlManager")
public class HsqlManager {
    protected static final Logger logger = LoggerFactory.getLogger(HsqlManager.class);
    private Connection conn;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            conn = getConn();
            initDatabase();
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            conn.close();
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    protected void initDatabase() {
        logger.info("Init users database(HSQLDB).");
        try {
            Statement st = getConn().createStatement();
            try {
                ResultSet rs = st
                        .executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES where TABLE_NAME='VERSIONS'");
                if (!rs.next()) {
                    st.execute(
                            "CREATE TABLE IF NOT EXISTS VERSIONS(NAME VARCHAR(32) NOT NULL PRIMARY KEY,VERSION VARCHAR(32))");
                }
                rs.close();
            } catch (java.sql.SQLSyntaxErrorException e) {
                logger.debug("not initialized,Initialization version management table");
            }
            st.close();
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        }
    }

    /**
     * @return the conn
     */
    protected Connection getConn() {
        try {
            if (conn == null || conn.isClosed()) {
                // Modify by Victor@20160903由于重启后可以不用以前数据，把文件数据库改为内存数据模式
                conn = DriverManager.getConnection("jdbc:hsqldb:mem:memdb", "root", "ems");
                conn.setAutoCommit(true);
            }
            return conn;
        } catch (SQLException e) {
            logger.debug("",e);
            return null;
        }
    }

    public Statement createStatement() {
        try {
            return getConn().createStatement();
        } catch (SQLException e) {
            logger.debug("",e);
            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql) {
        try {
            return getConn().prepareStatement(sql);
        } catch (SQLException e) {
            logger.debug("",e);
            return null;
        }
    }
}
