/***********************************************************************
 * $Id: DatabaseDao.java,v 1.1 Sep 29, 2009 4:59:11 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao;

import java.sql.SQLException;
import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.DataRecoveryResult;
import com.topvision.framework.domain.TableInfo;
import com.topvision.framework.exception.dao.DaoException;
import com.topvision.platform.domain.DatabaseInfo;

/**
 * @author kelers
 * @Create Date Sep 29, 2009 4:59:11 PM
 */
public interface DatabaseDao extends BaseEntityDao<DatabaseInfo> {
    /**
     * 获取数据信息
     * 
     * @return
     * @throws DaoException
     */
    DatabaseInfo getDatabaseInfo() throws DaoException;

    /**
     * 清除历史告警数据
     * 
     * @param time
     *            保存历史告警数据时长
     */
    void cleanHistoryData(Integer keepMonth);
    
    /**
     * 查询历史数据保存时长
     * 
     * @return
     */
    Integer getHistoryKeepMonth();
    
    /**
     * 更新历史数据保存时长
     * 
     * @param keepMonth
     */
    void updateHistoryKeepMonth(Integer keepMonth);
    

    /**
     * 
     * 
     * @param sqlList
     */
    DataRecoveryResult jdbcBatchImport(List<String> sqlList) throws SQLException;

    List<TableInfo> fetchTableCount(String[] tableName);

    DataRecoveryResult jdbcFullImport(List<String> sqlList) throws SQLException;
    
    
    /**
     * Run PartitionScript 
     * 
     */
    void runPartitionScript();

}
