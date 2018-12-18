/***********************************************************************
 * $Id: C3p0ServiceImpl.java,v1.0 2014-5-17 上午10:50:37 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mchange.v2.c3p0.PooledDataSource;
import com.topvision.ems.admin.domain.DataSourceInfo;
import com.topvision.ems.admin.service.C3p0PoolService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ServerBeanFactory;

/**
 * @author Rod John
 * @created @2014-5-17-上午10:50:37
 * 
 */
@Service("c3p0PoolService")
public class C3p0ServiceImpl extends BaseService implements C3p0PoolService {
    @Value("${jdbc.driverClassName}")
    protected String driverClass;
    @Value("${jdbc.url}")
    protected String server_ds_url;
    @Value("${jdbc.engine.url:jdbc:mysql://localhost:3003/ems}")
    protected String engine_ds_url;
    @Value("${jdbc.username}")
    protected String username;
    @Value("${jdbc.password}")
    protected String password;
    @Autowired
    private ServerBeanFactory serverBeanFactory;

    @PostConstruct
    public void init() {
        super.initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.admin.service.C3p0PoolService#databasePool()
     */
    @Override
    public List<String> databasePool() throws SQLException {
        List<String> result = new ArrayList<>();
        DataSource serverDs = (DataSource) serverBeanFactory.getBean("serverDataSource");
        if (serverDs instanceof PooledDataSource) {
            PooledDataSource pds = (PooledDataSource) serverDs;
            result.add(server_ds_url);
            result.add(String.valueOf(pds.getNumConnectionsDefaultUser()));
            result.add(String.valueOf(pds.getNumBusyConnectionsDefaultUser()));
            result.add(String.valueOf(pds.getNumIdleConnectionsDefaultUser()));
        }
        DataSource engineDs = (DataSource) serverBeanFactory.getBean("engineDataSource");
        if (engineDs instanceof PooledDataSource) {
            PooledDataSource pds = (PooledDataSource) engineDs;
            result.add(engine_ds_url);
            result.add(String.valueOf(pds.getNumConnectionsDefaultUser()));
            result.add(String.valueOf(pds.getNumBusyConnectionsDefaultUser()));
            result.add(String.valueOf(pds.getNumIdleConnectionsDefaultUser()));
        }

        /*
         * DataSource ds1 = (DataSource) beanFactory.getBean("dataSourceRead"); if (ds instanceof
         * PooledDataSource) { PooledDataSource pds = (PooledDataSource) ds1; StringBuilder
         * stringBuilder = new StringBuilder(); stringBuilder.append("num_connections: " +
         * pds.getNumConnectionsDefaultUser()); stringBuilder.append("num_busy_connections: " +
         * pds.getNumBusyConnectionsDefaultUser()); stringBuilder.append("num_idle_connections: " +
         * pds.getNumIdleConnectionsDefaultUser()); result.add(pds.getNumConnectionsDefaultUser());
         * result.add(pds.getNumBusyConnectionsDefaultUser());
         * result.add(pds.getNumIdleConnectionsDefaultUser()); }
         */
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.admin.service.C3p0PoolService#getDataSourceInfos()
     */
    @Override
    public List<DataSourceInfo> getDataSourceInfos() {
        Statement st = null;
        Connection conn = null;
        List<DataSourceInfo> result = new ArrayList<>();
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(server_ds_url, username, password);
            st = conn.createStatement();
            ResultSet resultSet = st
                    .executeQuery("select id,user,host,db,command,time,state,info from information_schema.processlist");
            while (resultSet.next()) {
                DataSourceInfo info = new DataSourceInfo();
                info.setId(resultSet.getInt(1));
                info.setUser(resultSet.getString("user"));
                info.setCommand(resultSet.getString("command"));
                info.setDb(resultSet.getString("db"));
                info.setHost(resultSet.getString("host"));
                info.setInfo(resultSet.getString("info"));
                info.setState(resultSet.getString("state"));
                info.setTime(resultSet.getString("time"));
                result.add(info);
            }
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return result;
    }
}
