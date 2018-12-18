/***********************************************************************
 * $Id: DemoHsqlDaoImpl.java,v1.0 2015年3月12日 上午11:13:13 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.demo.dao.hsql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.topvision.ems.engine.dao.HsqlAbstractDao;
import com.topvision.ems.engine.demo.dao.DemoHsqlDao;
import com.topvision.framework.annotation.Engine;

/**
 * @author Victor
 * @created @2015年3月12日-上午11:13:13
 *
 */
@Engine("demoHsqlDao")
public class DemoHsqlDaoImpl extends HsqlAbstractDao implements DemoHsqlDao {
    @Override
    public String getVersion() {
        String version = null;
        try {
            Statement st = createStatement();
            ResultSet rs = st.executeQuery("SELECT version FROM VERSIONS WHERE name = 'demo'");
            if (rs.next()) {
                version = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        }
        return version;
    }

    @Override
    protected void initDatabase() {
        logger.info("Init demo database.");
        try {
            String version = null;
            Statement st = createStatement();
            try {
                ResultSet rs = st.executeQuery("SELECT version FROM VERSIONS WHERE name = 'demo'");
                if (rs.next()) {
                    version = rs.getString(1);
                }
                rs.close();
            } catch (java.sql.SQLSyntaxErrorException e) {
                logger.debug("没有初始化，初始化版本管理表", e);
            }
            if (version == null) {
                st.execute(
                        "CREATE TABLE IF NOT EXISTS DEMO1(NO NUMERIC(5) NOT NULL PRIMARY KEY,NAME VARCHAR(32),USERNAME VARCHAR(32),TYPE NUMERIC(1) DEFAULT 1,REGION VARCHAR(32),DEPARTMENT VARCHAR(32),PROJECT VARCHAR(32))");
                st.execute("INSERT INTO VERSIONS VALUES('demo','1.0.0')");
                version = "1.0.0";
            }
            if ("1.0.0".equals(version)) {
                st.execute(
                        "CREATE TABLE IF NOT EXISTS DEMO2(NO VARCHAR(10),TIMEDESC VARCHAR(64),NOTEID INTEGER,NOTEROW VARCHAR(1024),TIME TIMESTAMP)");
                st.execute("UPDATE VERSIONS SET VERSION = '1.1.0' WHERE NAME='demo'");
                version = "1.1.0";
            }
            st.close();
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        }
    }
}
