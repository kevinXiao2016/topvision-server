/***********************************************************************
 * $Id: DatabaseDaoImpl.java,v 1.1 Sep 29, 2009 4:59:38 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.domain.DataRecoveryResult;
import com.topvision.framework.domain.TableInfo;
import com.topvision.framework.exception.dao.DaoException;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.dao.DatabaseDao;
import com.topvision.platform.domain.DatabaseInfo;

@Repository("databaseDao")
public class DatabaseDaoImpl extends MyBatisDaoSupport<DatabaseInfo> implements DatabaseDao {
    @Override
    protected String getDomainName() {
        return DatabaseInfo.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.DatabaseDao#getDatabaseInfo()
     */
    @Override
    public DatabaseInfo getDatabaseInfo() throws DaoException {
        DatabaseInfo info = new DatabaseInfo();
        Connection conn = null;
        try {
            conn = getSqlSession().getConnection();
            conn.setAutoCommit(false);
            DatabaseMetaData dme = conn.getMetaData();
            info.setDatabaseProductName(dme.getDatabaseProductName());
            info.setDatabaseProductVersion(dme.getDatabaseProductVersion());
        } catch (SQLException sqle) {
            throw new DaoException(sqle);
        } finally {
            // 不能关闭连接池中的连接
            /*if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                }
            }*/
        }
        return info;
    }

    @Override
    public void cleanHistoryData(Integer keepMonth) {
        getSqlSession().selectOne(getNameSpace("cleanHistoryData"), keepMonth);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.DatabaseDao#jdbcBatchImport(java.util.List)
     */
    @Override
    public DataRecoveryResult jdbcBatchImport(List<String> sqlList) throws SQLException {
        DataRecoveryResult recoveryResult = new DataRecoveryResult();
        Connection connection = getSqlSession().getConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        try {
            for (String sql : sqlList) {
                statement.addBatch(sql);
            }
            int[] result = statement.executeBatch();
            connection.commit();
            recoveryResult.setRecoveryResult(result);
            recoveryResult.setErrorInfo("NOERROR");
            return recoveryResult;
        } catch (java.sql.BatchUpdateException e) {
            connection.rollback();
            e.printStackTrace();
            recoveryResult.setErrorInfo(e.getMessage());
            recoveryResult.setRecoveryResult(e.getUpdateCounts());
            /*
             * VersionConvertException versionConvertException = new
             * VersionConvertException(e.getMessage(), e.getUpdateCounts()); throw
             * versionConvertException;
             */
            return recoveryResult;
            // TODO: handle exception
        } catch (SQLException e) {
            connection.rollback();
            return null;
        } finally {
            statement.close();
            // 不能关闭连接池中的连接
            // connection.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.DatabaseDao#fetchTableCount(java.lang.String)
     */
    @Override
    public List<TableInfo> fetchTableCount(String[] tableName) {
        List<TableInfo> list = new ArrayList<TableInfo>();
        for (String name : tableName) {
            TableInfo tableCount = new TableInfo();
            tableCount.setTableName(name);
            tableCount.setTableCount(getSqlSession().selectOne(getNameSpace("fetchTableCount"), name).toString());
            list.add(tableCount);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.DatabaseDao#jdbcFullImport(java.util.List)
     */
    @Override
    public DataRecoveryResult jdbcFullImport(List<String> sqlList) throws SQLException {
        DataRecoveryResult recoveryResult = new DataRecoveryResult();
        Connection connection = getSqlSession().getConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        CallableStatement proc = null;
        try {
            proc = connection.prepareCall("{call deleteAllTable()}");
            proc.execute();
            for (String sql : sqlList) {
                statement.addBatch(sql);
            }
            int[] result = statement.executeBatch();
            connection.commit();
            recoveryResult.setRecoveryResult(result);
            recoveryResult.setErrorInfo("NOERROR");
            return recoveryResult;
        } catch (java.sql.BatchUpdateException e) {
            connection.rollback();
            e.printStackTrace();
            recoveryResult.setErrorInfo(e.getMessage());
            recoveryResult.setRecoveryResult(e.getUpdateCounts());
            /*
             * VersionConvertException versionConvertException = new
             * VersionConvertException(e.getMessage(), e.getUpdateCounts()); throw
             * versionConvertException;
             */
            return recoveryResult;
            // TODO: handle exception
        } catch (SQLException e) {
            connection.rollback();
            return null;
        } finally {
            statement.close();
            proc.close();
            // 不能关闭连接池中的连接
            // connection.close();
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dao.DatabaseDao#getHistoryKeepMonth()
     */
    @Override
    public Integer getHistoryKeepMonth() {
        return getSqlSession().selectOne(getNameSpace("getHistoryKeepMonth"));
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dao.DatabaseDao#updateHistoryKeepMonth(java.lang.Integer)
     */
    @Override
    public void updateHistoryKeepMonth(Integer keepMonth) {
        getSqlSession().update(getNameSpace("updateHistoryKeepMonth"), keepMonth);
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dao.DatabaseDao#runPartitionScript()
     */
    @Override
    public void runPartitionScript() {
        Connection connection = null;
        try {
            connection = getSqlSession().getConnection();
            connection.setAutoCommit(false);
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setLogWriter(null);
            runner.setDelimiter("$$");
            StringBuilder fileName = new StringBuilder(SystemConstants.ROOT_REAL_PATH);
            fileName.append("META-INF/script/NM3000_Partition_Script.sql");
            runner.runScript(new InputStreamReader(new FileInputStream(fileName.toString()), "utf-8"));
            connection.commit();
        } catch (IOException exception) {
            logger.error("runPartitionScript error:", exception);
        } catch (SQLException exception) {
            logger.error("runPartitionScript error:", exception);
        } finally {
        }

    }
}
