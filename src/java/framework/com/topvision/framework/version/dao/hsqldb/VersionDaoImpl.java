/***********************************************************************
 * $Id: VersionDaoImpl.java,v 1.1 Sep 23, 2008 11:10:44 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.dao.hsqldb;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.topvision.framework.dao.hsqldb.HsqldbDaoSupport;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.version.dao.VersionDao;
import com.topvision.framework.version.domain.QueryResult;
import com.topvision.framework.version.domain.Version;
import com.topvision.framework.version.domain.VersionRecord;

/**
 * @Create Date Sep 23, 2008 11:10:44 AM
 * 
 * @author kelers
 * 
 */
public class VersionDaoImpl extends HsqldbDaoSupport<Version> implements VersionDao {
    @Override
    public void deleteByMap(Map<String, String> map) {
    }

    @Override
    public void createVersionRecordTable() {
    }

    @Override
    public void execute(String sql) {
        if (logger.isDebugEnabled()) {
            logger.debug("Execute SQL:" + sql);
        }
        execute(sql);
    }

    @Override
    public QueryResult query(String sql) {
        QueryResult result = new QueryResult();
        result.setQuerySql(sql);
        return result;
    }

    @Override
    protected String getKeyName() {
        return "id";
    }

    @Override
    protected String getTableName() {
        return "VersionControl";
    }

    @Override
    public Map<String, String> getVariables() {
        return null;
    }

    @Override
    public void insertEntity(List<Version> entity) {
    }

    @Override
    public void insertEntity(Version entity) {
    }

    @Override
    public boolean isDBInited() {
        Statement st = null;
        try {
            st = getConnection().createStatement();
            st.executeQuery("select * from VersionControl");
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            try {
                st.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public List<Version> selectByMap(Map<String, String> entity) {
        return null;
    }

    @Override
    public Version selectByName(String name) {
        return null;
    }

    @Override
    public Version selectByPrimaryKey(Integer key) {
        return null;
    }

    @Override
    public Version selectByPrimaryKey(Long key) {
        return null;
    }

    @Override
    public Version selectByPrimaryKey(String key) {
        return null;
    }

    @Override
    public void updateEntity(List<Version> entities) {
    }

    @Override
    public void updateEntity(Version entity) {
    }

    @Override
    public void selectByMap(Map<String, String> map, Page page, MyResultHandler handler) {
    }

    @Override
    public void selectByMap(Map<String, String> map, MyResultHandler handler) {
    }

    @Override
    public PageData<Version> selectByMap(Map<String, String> map, Page page) {
        return null;
    }

    @Override
    public void beginBatch() {
    }

    @Override
    public void addBatch(String sql) {
    }

    @Override
    public void executeBatch() {
    }

    @Override
    public List<VersionRecord> getRecords() {
        return null;
    }

    @Override
    public void saveRecordInBatch(VersionRecord versionRecord) {
    }
}
