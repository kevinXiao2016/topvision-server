/***********************************************************************
 * $Id: MyBatisDaoSupport.java,v1.0 2013-9-22 上午8:53:03 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.exception.dao.DaoException;

/**
 * @author Victor
 * @created @2013-9-22-上午8:53:03
 * 
 */
public abstract class MyBatisDaoSupport<T> extends SqlSessionDaoSupport implements BaseEntityDao<T> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param sqlSessionTemplate
     *            the sqlSessionTemplate to set
     */
    @Override
    @Autowired
    @Qualifier("sqlSessionTemplate")
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    /**
     * 
     * @param name
     * @return
     */
    protected String getNameSpace(String name) {
        return new StringBuilder(getDomainName()).append('.').append(name).toString();
    }

    protected String getNameSpace() {
        return new StringBuilder(getDomainName()).append('.').toString();
    }

    /**
     * @return
     */
    protected abstract String getDomainName();

    /**
     * 得到系统运行的日志记录器.
     * 
     * @return Logger
     */
    public final Logger getLogger() {
        return logger;
    }

    /*
     * 通用的根据主键删除一条记录 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.deleteByMap"的命名规范,其中tableName是enity影射的表 名称，同时是sqlMap的namespace</li>
     * </dl> </note> // *
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByMap(java.util.Map)
     */
    @Override
    public void deleteByMap(Map<String, String> map) {
        try {
            getSqlSession().delete(getNameSpace("deleteByMap"), map);
        } catch (Throwable th) {
            String msg = th.getMessage();
            if (msg != null && msg.indexOf("ORA-02292") > -1) {
                throw new DaoException("The records have been cited, can not be deleted!");
            } else {
                throw new DaoException(th);
            }
        }
    }

    /*
     * 通用的根据主键删除一条记录 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.deleteByPrimaryKey"的命名规范,其中tableName是
     * enity影射的表名称，同时是sqlMap的namespace</li> </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Integer)
     */
    @Override
    public void deleteByPrimaryKey(Integer key) {
        getSqlSession().delete(getNameSpace("deleteByPrimaryKey"), key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.util.List)
     */
    @Override
    public void deleteByPrimaryKey(final List<?> keys) {
        delete(getNameSpace("deleteByPrimaryKey"), keys);
    }

    /*
     * 通用的根据主键删除一条记录 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.deleteByPrimaryKey"的命名规范,其中tableName是
     * enity影射的表名称，同时是sqlMap的namespace</li> </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Long)
     */
    @Override
    public void deleteByPrimaryKey(Long key) {
        getSqlSession().delete(getNameSpace("deleteByPrimaryKey"), key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Object[])
     */
    @Override
    public void deleteByPrimaryKey(final Object[] keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        int size = keys.length;
        SqlSession session = getBatchSession();
        try {
            String sql = getNameSpace("deleteByPrimaryKey");
            for (int i = 0; i < size; i++) {
                session.delete(sql, keys[i]);
            }
            session.flushStatements();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * 通用的根据主键删除一条记录 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.deleteByPrimaryKey"的命名规范,其中tableName是
     * enity影射的表名称，同时是sqlMap的namespace</li> </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.String)
     */
    @Override
    public void deleteByPrimaryKey(String key) {
        getSqlSession().delete(getNameSpace("deleteByPrimaryKey"), key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Object)
     */
    @Override
    public void deleteByPrimaryKey(T entity) {
        getSqlSession().delete(getNameSpace("deleteByPrimaryKey"), entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#empty()
     */
    @Override
    public void empty() {
        try {
            getSqlSession().delete(getNameSpace("empty"));
        } catch (Throwable th) {
            String msg = th.getMessage();
            if (msg != null && msg.indexOf("ORA-02292") > -1) {
                throw new DaoException("The records have been cited, can not be deleted!");
            } else {
                throw new DaoException(th);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(java.util.List)
     */
    @Override
    public void insertEntity(final List<T> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                session.insert(getNameSpace("insertEntity"), entities.get(i));
            }
            session.flushStatements();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * 通用的插入 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.insertEntity"的命名规范,其中tableName是 enity影射的表名称，同时是sqlMap的namespace</li>
     * </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(java.lang.Object)
     */
    @Override
    public void insertEntity(T entity) {
        getSqlSession().insert(getNameSpace("insertEntity"), entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(T[])
     */
    @Override
    public void insertEntity(final T[] entities) {
        if (entities == null) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            int size = entities.length;
            for (int i = 0; i < size; i++) {
                session.insert(getNameSpace("insertEntity"), entities[i]);
            }
            session.flushStatements();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * 通用的根据主键查询 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.selectByMap"的命名规范,其中tableName是 enity影射的表名称，同时是sqlMap的namespace</li>
     * </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map)
     */
    @Override
    public List<T> selectByMap(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("selectByMap"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map,
     * com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void selectByMap(Map<String, String> map, MyResultHandler handler) {
        selectWithRowHandler(this.getNameSpace("selectByMap"), map, handler);
    }

    /*
     * 根据map集合查询符合条件的某页实体, <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li>
     * <li>sqlMap select符合:"{tableName}.selectCount" &
     * "{tableName}.selectWithPage"的命名规范,其中tableName是 enity影射的表名称，同时是sqlMap的namespace</li> </dl>
     * </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map,
     * com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<T> selectByMap(Map<String, String> map, Page page) {
        return selectByMap(getNameSpace("selectCount"), getNameSpace("selectWithPage"), map, page);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map,
     * com.topvision.framework.domain.Page, com.topvision.framework.event.RowHandler)
     */
    @Override
    public void selectByMap(Map<String, String> map, Page page, MyResultHandler handler) {
        selectWithRowHandler(this.getNameSpace("selectCount"), this.getNameSpace("selectWithPage"), map, page, handler);
    }

    /*
     * 通用的根据名称查询 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.selectByName"的命名规范,其中tableName是 enity影射的表名称，同时是sqlMap的namespace</li>
     * </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByName(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T selectByName(String name) {
        return (T) getSqlSession().selectOne(getNameSpace("selectByName"), name);
    }

    /*
     * 
     * 通用的根据主键查询 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.selectByPrimaryKey"的命名规范,其中tableName是
     * enity影射的表名称，同时是sqlMap的namespace</li> </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.Integer)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T selectByPrimaryKey(Integer key) {
        return (T) getSqlSession().selectOne(getNameSpace("selectByPrimaryKey"), key);
    }

    /*
     * 通用的根据主键查询 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.selectByPrimaryKey"的命名规范,其中tableName是
     * enity影射的表名称，同时是sqlMap的namespace</li> </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.Long)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T selectByPrimaryKey(Long key) {
        return (T) getSqlSession().selectOne(getNameSpace("selectByPrimaryKey"), key);
    }

    /*
     * 通用的根据主键查询 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * insert符合:"{tableName}.selectByPrimaryKey"的命名规范,其中tableName是
     * enity影射的表名称，同时是sqlMap的namespace</li> </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T selectByPrimaryKey(String key) {
        return (T) getSqlSession().selectOne(getNameSpace("selectByPrimaryKey"), key);
    }

    /**
     * 批量更新记录.
     */
    @Override
    public void updateEntity(final List<T> entities) {
        update(getNameSpace("updateEntity"), entities);
    }

    /*
     * 通用的根据主键更新 <note>对应的sqlMap必须符合以下规范:</br> <dl> <li>entiy必须和数据库表中存在对应关系</li> <li>sqlMap
     * update符合:"{tableName}.updateEntity"的命名规范,其中tableName是 enity影射的表名称，同时是sqlMap的namespace</li>
     * </dl> </note>
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#updateEntity(java.lang.Object)
     */
    @Override
    public void updateEntity(T entity) {
        getSqlSession().update(getNameSpace("updateEntity"), entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#updateEntity(T[])
     */
    @Override
    public void updateEntity(final T[] entities) {
        if (entities == null || entities.length == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            int size = entities.length;
            String sql = getNameSpace("updateEntity");
            for (int i = 0; i < size; i++) {
                session.insert(sql, entities[i]);
            }
            session.flushStatements();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    protected void delete(final String statementName, final List<?> keys) {
        if (keys == null || keys.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            int size = keys.size();
            for (int i = 0; i < size; i++) {
                session.delete(statementName, keys.get(i));
            }
            session.flushStatements();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /**
     * 根据map集合查询符合条件的某页实体, 查询语句可以使用以下参数: page - 当前页, pageSize - 每页大小, startPos - 起始位置, endPos -
     * 终止位置, offset - mysql使用的偏移量.
     * 
     * @param statementCount
     * @param statementPage
     * @param map
     * @param page
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected PageData<T> selectByMap(String statementCount, String statementPage, Map<String, String> map, Page page) {
        Integer count = (Integer) getSqlSession().selectOne(statementCount, map);
        page.setRowCount(count.intValue());

        int pageSize = page.getPageSize();
        int rowCount = page.getRowCount();
        int curPage = page.getPage();
        if (rowCount % pageSize == 0) {
            if (curPage > rowCount / pageSize || curPage <= 0) {
                curPage = rowCount / pageSize;
            }
        } else {
            if (curPage > rowCount / pageSize + 1 || curPage <= 0) {
                curPage = rowCount / pageSize + 1;
            }
        }

        if (curPage < 1) {
            curPage = 1;
        }

        int start = (curPage - 1) * pageSize + 1;

        int topCount = start + pageSize - 1;
        if (rowCount < topCount) {
            pageSize = rowCount - start + 1;
            topCount = rowCount;
        }

        if (pageSize <= 0) {
            return new PageData(page, new ArrayList<Object>());
        }

        if (map == null) {
            map = new HashMap<String, String>();
        }
        map.put("page", String.valueOf(page.getPage()));
        map.put("startPos", String.valueOf(start));
        map.put("endPos", String.valueOf(topCount));
        map.put("offset", String.valueOf(start - 1));
        map.put("pageSize", String.valueOf(page.getPageSize()));
        map.put("groupBy", page.getGroupBy());
        map.put("sortName", page.getSortName());
        map.put("sortDirection", page.getSortDirection());
        return new PageData(page, getSqlSession().selectList(statementPage, map));
    }

    /**
     * 使用回调方式处理结果集的每一行记录, 防止将结果集放入集合后再对集合遍历处理, 用于提高处理效率.
     * 
     * @param statementName
     * @param handler
     */
    protected void selectWithRowHandler(String statementName, MyResultHandler handler) {
        handler.prepare();
        getSqlSession().select(statementName, handler);
        handler.complete();
    }

    /**
     * 使用回调方式处理结果集的每一行记录, 防止将结果集放入集合后再对集合遍历处理, 用于提高处理效率.
     * 
     * @param statementName
     * @param map
     * @param handler
     */
    protected void selectWithRowHandler(String statementName, Map<String, ?> map, MyResultHandler handler) {
        handler.prepare();
        getSqlSession().select(statementName, map, handler);
        handler.complete();

    }

    /**
     * 
     * @param statementName
     * @param obj
     * @param handler
     */
    protected void selectWithRowHandler(String statementName, Object obj, MyResultHandler handler) {
        handler.prepare();
        getSqlSession().select(statementName, obj, handler);
        handler.complete();
    }

    /**
     * 
     * @param statementCount
     * @param statementPage
     * @param map
     * @param page
     * @param handler
     */
    protected void selectWithRowHandler(String statementCount, String statementPage, Map<String, String> map, Page page,
            MyResultHandler handler) {
        Integer count = (Integer) getSqlSession().selectOne(statementCount, map);
        page.setRowCount(count.intValue());

        int pageSize = page.getPageSize();
        int rowCount = page.getRowCount();
        int curPage = page.getPage();
        if (rowCount % pageSize == 0) {
            if (curPage > rowCount / pageSize || curPage <= 0) {
                curPage = rowCount / pageSize;
            }
        } else {
            if (curPage > rowCount / pageSize + 1 || curPage <= 0) {
                curPage = rowCount / pageSize + 1;
            }
        }

        if (curPage < 1) {
            curPage = 1;
        }

        int start = (curPage - 1) * pageSize + 1;

        int topCount = start + pageSize - 1;
        if (rowCount < topCount) {
            pageSize = rowCount - start + 1;
            topCount = rowCount;
        }

        if (pageSize <= 0) {
            return;
        }

        if (map == null) {
            map = new HashMap<String, String>();
        }
        map.put("startPos", String.valueOf(start));
        map.put("endPos", String.valueOf(topCount));
        map.put("offset", String.valueOf(start - 1));
        map.put("pageSize", String.valueOf(page.getPageSize()));
        map.put("page", String.valueOf(page.getPage()));
        map.put("groupBy", page.getGroupBy());
        map.put("sortName", page.getSortName());
        map.put("sortDirection", page.getSortDirection());

        handler.prepare();
        getSqlSession().select(statementPage, map, handler);
        handler.complete();
    }

    /**
     * 针对iBatis的批量更新.
     * 
     * @param statementName
     * @param entities
     * @
     */
    protected void update(final String statementName, final List<T> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                session.insert(statementName, entities.get(i));
            }
            session.flushStatements();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /**
     * 获得MyBatis的批量操作session
     * 
     * 注意此种方式属于程序控制事务 不再托管给Spring进行事务管理
     * 
     * 在使用session时，需要使用try catch finally 使用示例如下
     * 
     * <code>
     *  SqlSession session = getBatchSession();
     *  try {
     *      doBatch();
     *      session.commit();
     *  } catch (Exception e) {
     *      logger.error("", e);
     *      session.rollback();
     *  } finally {
     *      session.close();
     *  }
     * </code>
     * 
     * @return
     */
    protected SqlSession getBatchSession() {
        SqlSessionTemplate sessionTemplate = (SqlSessionTemplate) getSqlSession();
        return sessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    }

    /**
     * 批量处理的方法
     * 
     * @param batch
     *            批量执行类实现，子类一般为匿名类
     */
    protected void exeBatch(BatchExecutor batch) {
        batch.run(getBatchSession());
    }

    /**
     * 将MyBATIS的SelectMap方法进行封装，适合IBATIS的queryByMap 方法调用
     * 
     * @param <K>
     * @param <V>
     * @param statementName
     *            查询语句标示
     * @param parameter
     *            查询参数
     * @param keyName
     *            返回KEY标示
     * @param valueName
     *            返回VALUE标示
     * @return
     */
    protected <K, V> Map<K, V> selectMapByKeyAndValue(String statementName, Object parameter, String keyName,
            String valueName) {
        Map<K, V> reMap = new HashMap<K, V>();
        Map<K, Map<String, V>> databaseMap = getSqlSession().selectMap(statementName, parameter, keyName);
        for (Entry<K, Map<String, V>> entry : databaseMap.entrySet()) {
            Map<String, V> tmp = entry.getValue();
            reMap.put(entry.getKey(), tmp.get(valueName));
        }
        return reMap;
    }

    /**
     * 将MyBATIS的SelectMap方法进行封装，适合IBATIS的queryByMap 方法调用(查询参数为空)
     * 
     * @param <K>
     * @param <V>
     * @param statementName
     * @param keyName
     * @param valueName
     * @return
     */
    protected <K, V> Map<K, V> selectMapByKeyAndValue(String statementName, String keyName, String valueName) {
        return selectMapByKeyAndValue(statementName, null, keyName, valueName);
    }

    /**
     * 用于封装批量处理时对session进行管理，实际使用只用实现本类并实现exe方法,提交、回滚和关闭都由框架完成
     * 
     * 
     * @author Victor
     * @created @2016年8月5日-下午6:49:27
     */
    protected abstract class BatchExecutor {
        /**
         * 封装方法，用于session的管理.
         * 
         * @param session
         *            支持批量提交和回滚的session
         */
        void run(SqlSession session) {
            try {
                exe(session);
                session.commit();
            } catch (Exception e) {
                logger.error("BatchExecutor error:", e);
                session.rollback();
            } finally {
                session.close();
            }
        }

        /**
         * 实际处理批量时需要实现的方法，本方法支持事务管理
         * 
         * @param session
         *            支持批量提交和回滚的session
         */
        protected abstract void exe(SqlSession session);
    }
}
