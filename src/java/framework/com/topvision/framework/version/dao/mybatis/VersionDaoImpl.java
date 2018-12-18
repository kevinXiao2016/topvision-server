/***********************************************************************
 * $Id: VersionDaoImpl.java,v 1.1 May 13, 2008 7:28:46 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.dao.mybatis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.exception.dao.DaoException;
import com.topvision.framework.version.dao.VersionDao;
import com.topvision.framework.version.domain.MysqlVariable;
import com.topvision.framework.version.domain.QueryResult;
import com.topvision.framework.version.domain.Version;
import com.topvision.framework.version.domain.VersionRecord;

/**
 * @Create Date May 13, 2008 7:28:46 PM
 * 
 * @author kelers
 * 
 */
@Repository("versionDao")
public class VersionDaoImpl extends MyBatisDaoSupport<Version> implements VersionDao {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Connection batchCon;
    private SqlSession batchSession;
    private Statement batchStatement;

    @Override
    protected String getDomainName() {
        return Version.class.getName();
    }

    @Override
    public void createVersionRecordTable() {
        try {
            Integer tableNum = getSqlSession().selectOne(getNameSpace("isVersionRecordTableExists"));
            if(tableNum==null || tableNum.equals(0)) {
                getSqlSession().insert(getNameSpace("createVersionRecordTable"));
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void beginBatch() {
        try {
            if (batchSession == null || batchSession.getConnection().isClosed()) {
                batchSession = getBatchSession();
                batchCon = batchSession.getConnection();
                batchStatement = batchCon.createStatement();
            }
        } catch (SQLException e) {
            // batchCon = ((SqlSessionTemplate)
            // getSqlSession()).getSqlSessionFactory().openSession().getConnection();
        }
    }

    @Override
    public void addBatch(String sql) {
        logger.debug("Execute SQL:{}", sql);
        try {
            batchStatement.addBatch(sql);
        } catch (Exception e) {
            logger.debug("Error:{}", sql, e);
            throw new DaoException(e);
        }
    }

    @Override
    public void saveRecordInBatch(VersionRecord versionRecord) {
        batchSession.update(getNameSpace("saveRecord"), versionRecord);
    }

    @Override
    public void executeBatch() throws Exception {
        try {
            batchStatement.executeBatch();
            batchSession.commit();
        } catch (Exception e) {
            logger.error("Connection:{}", e.getMessage());
            throw e;
        } finally {
            batchStatement.close();
            batchCon.close();
            batchSession.close();
            batchSession = null;
        }
    }

    @Override
    public void execute(String sql) {
        Connection con = null;
        Statement statement = null;
        try {
            con = ((SqlSessionTemplate) getSqlSession()).getSqlSessionFactory().openSession().getConnection();
            statement = con.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            logger.debug("----------------{}", sql, e);
            throw new DaoException(e);
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                logger.error("statement:{}", e.getMessage());
            }
            try {
                con.close();
            } catch (Exception e) {
                logger.error("Connection:{}", e.getMessage());
            }
        }
    }

    @Override
    public QueryResult query(String sql) {
        logger.debug("query SQL:{}", sql);
        QueryResult result = new QueryResult();
        result.setQuerySql(sql);
        Connection con = null;
        Statement statement = null;
        try {
            con = ((SqlSessionTemplate) getSqlSession()).getSqlSessionFactory().openSession().getConnection();
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            result.setColumnCount(columnCount);
            List<String> columns = new ArrayList<String>();
            result.setColumnNames(columns);
            for (int i = 1; i <= columnCount; i++) {
                columns.add(metaData.getColumnName(i));
            }
            List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
            result.setDatas(datas);
            while (rs.next()) {
                Map<String, String> data = new HashMap<String, String>();
                for (int i = 1; i <= columnCount; i++) {
                    data.put(metaData.getColumnName(i), rs.getString(i));
                }
                datas.add(data);
            }
        } catch (SQLException e) {
            logger.debug("----------------{}", sql, e);
            throw new DaoException(e);
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                logger.error("statement:{}", e.getMessage());
            }
            try {
                con.close();
            } catch (Exception e) {
                logger.error("Connection:{}", e.getMessage());
            }
        }
        return result;
    }

    @Override
    public Map<String, String> getVariables() {
        List<MysqlVariable> list = getSqlSession().selectList(getNameSpace("getVariables"));
        if (list == null || list.isEmpty()) {
            return null;
        }
        Map<String, String> variables = new HashMap<String, String>(list.size());
        for (MysqlVariable var : list) {
            variables.put(var.getVariable_name(), var.getValue());
        }
        return variables;
    }

    @Override
    public boolean isDBInited() {
        try {
            getSqlSession().selectList(getNameSpace("desc"));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<VersionRecord> getRecords() {
        try {
            return getSqlSession().selectList(getNameSpace("getRecords"));
        } catch (Exception e) {
            return new ArrayList<VersionRecord>();
        }
    }
}
