/***********************************************************************
 * $Id: VersionDao.java,v 1.1 May 13, 2008 7:26:18 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.dao;

import java.util.List;
import java.util.Map;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.version.domain.QueryResult;
import com.topvision.framework.version.domain.Version;
import com.topvision.framework.version.domain.VersionRecord;

/**
 * @Create Date May 13, 2008 7:26:18 PM
 * 
 * @author kelers
 * 
 */
public interface VersionDao extends BaseEntityDao<Version> {
    /**
     * 用于批量执行一批sql语句，要求首先调用beginBatch，然后多次调用addBatch，最后调用executeBatch
     * 注意：不允许单独调用addBatch
     */
    void beginBatch();

    void addBatch(String sql);

    void executeBatch() throws Exception;

    /**
     * 单独执行sql语句
     * @param sql
     */
    void execute(String sql);

    /**
     * 执行数据库查询
     * 
     * @param sql
     */
    QueryResult query(String sql);

    /**
     * @return 返回数据库环境信息，eg.Mysql:show variables;
     */
    Map<String, String> getVariables();

    /**
     * 判断数据库是否初始化
     */
    boolean isDBInited();

    void saveRecordInBatch(VersionRecord versionRecord);

    List<VersionRecord> getRecords();

    void createVersionRecordTable();
}
