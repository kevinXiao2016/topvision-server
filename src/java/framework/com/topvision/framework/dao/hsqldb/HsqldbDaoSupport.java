/***********************************************************************
 * $Id: HsqldbDaoSupport.java,v 1.1 Sep 22, 2008 9:28:55 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.dao.hsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.exception.dao.DaoException;

/**
 * @Create Date Sep 22, 2008 9:28:55 PM
 * 
 * @author kelers
 * 
 */
public abstract class HsqldbDaoSupport<T> implements BaseEntityDao<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected DataSource dataSource = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByMap(java.util.Map)
     */
    @Override
    public abstract void deleteByMap(Map<String, String> map);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Integer)
     */
    @Override
    public void deleteByPrimaryKey(Integer key) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getTableName());
        sql.append(" WHERE ").append(getKeyName()).append(" = ").append(key);
        execute(sql.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.util.List)
     */
    @Override
    public void deleteByPrimaryKey(List<?> keys) {
        PreparedStatement pst = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM ").append(getTableName());
            sql.append(" WHERE ").append(getKeyName()).append(" = ?");
            pst = getConnection().prepareStatement(sql.toString());
            for (int i = 0; i < keys.size(); i++) {
                pst.setObject(1, keys.get(i));
                pst.addBatch();
            }
            pst.executeBatch();
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
            throw new DaoException("deleteByPrimaryKey", e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Long)
     */
    @Override
    public void deleteByPrimaryKey(Long key) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getTableName());
        sql.append(" WHERE ").append(getKeyName()).append(" = ").append(key);
        execute(sql.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Object[])
     */
    @Override
    public void deleteByPrimaryKey(Object[] keys) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.String)
     */
    @Override
    public void deleteByPrimaryKey(String key) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getTableName());
        sql.append(" WHERE ").append(getKeyName()).append(" = '").append(key).append("'");
        execute(sql.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Object)
     */
    @Override
    public void deleteByPrimaryKey(T entity) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#empty()
     */
    @Override
    public void empty() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getTableName());
        execute(sql.toString());
    }

    /**
     * @param string
     */
    public void execute(String sql) {
        Statement st = null;
        try {
            st = getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
            throw new DaoException(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    /**
     * @return the hsqldb's connection
     */
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @return 主键名称, eg. id
     */
    protected abstract String getKeyName();

    /**
     * @return database table's name, eg. VersionControlTable
     */
    protected abstract String getTableName();

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(java.util.List)
     */
    @Override
    public abstract void insertEntity(List<T> entity);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(java.lang.Object)
     */
    @Override
    public abstract void insertEntity(T entity);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(T[])
     */
    @Override
    public void insertEntity(T[] entities) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map)
     */
    @Override
    public abstract List<T> selectByMap(Map<String, String> entity);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map,
     * com.topvision.framework.domain.Page)
     */
    @Override
    public abstract PageData<T> selectByMap(Map<String, String> map, Page page);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.Integer)
     */
    @Override
    public abstract T selectByPrimaryKey(Integer key);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.Long)
     */
    @Override
    public abstract T selectByPrimaryKey(Long key);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.String)
     */
    @Override
    public abstract T selectByPrimaryKey(String key);

    /**
     * 返回表中数据行数
     * 
     * @param map
     *            key=value对数据进行过滤
     * @return
     */
    protected int selectCount(Map<String, ?> map) {
        StringBuilder querySql = new StringBuilder("SELECT count(*) FROM ");
        querySql.append(getTableName());
        if (map != null) {
            boolean flag = true;
            for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
                String key = itr.next();
                if (flag) {
                    querySql.append(" WHERE ").append(key).append(" = ").append(map.get(key));
                    flag = false;
                } else {
                    querySql.append(" AND ").append(key).append(" = ").append(map.get(key));
                }

            }
        }
        return selectCount(querySql.toString());
    }

    protected int selectCount(String sql) {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = getConnection().createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                st.close();
            } catch (Exception e) {
            }
        }
        return 0;
    }

    /**
     * @param dataSource
     *            the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 
     * @param sql
     */
    public void update(String sql) {
        Statement st = null;
        try {
            st = getConnection().createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
            throw new DaoException(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#updateEntity(java.util.List)
     */
    @Override
    public abstract void updateEntity(List<T> entities);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#updateEntity(java.lang.Object)
     */
    @Override
    public abstract void updateEntity(T entity);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#updateEntity(T[])
     */
    @Override
    public void updateEntity(T[] entities) {
    }
}
