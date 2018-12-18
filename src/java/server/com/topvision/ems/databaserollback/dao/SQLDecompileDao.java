/***********************************************************************
 * $Id: DatabaseRollBackDao.java,v1.0 2016年7月20日 下午6:11:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.dao;

import java.sql.SQLException;
import java.util.List;

import com.topvision.framework.dao.Dao;

/**
 * @author Bravin
 * @created @2016年7月20日-下午6:11:58
 *
 */
public interface SQLDecompileDao extends Dao {

    /**
     * @param sql
     * @param columns 
     * @return
     * @throws SQLException 
     */
    List<String[]> execute(String sql, List<String> columns) throws SQLException;

    /**
     * @param sql
     * @return
     */
    String execute2(String sql);

    /**
     * @param sql
     * @return
     */
    List<String> execute3(String sql);

}
