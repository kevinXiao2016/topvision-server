/***********************************************************************
 * $Id: SQLRollbackDao.java,v1.0 2016年7月25日 下午3:34:34 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.dao;

import java.util.List;

import com.topvision.ems.databaserollback.rollback.RollbackStatement;

/**
 * 
 * @author Bravin
 * @created @2016年7月25日-下午3:34:34
 *
 */
public interface SQLRollbackDao {

    /**
     * @param sql
     */
    void exec(String sql);

    /**
     * @param currentVersion
     * @return
     */
    List<RollbackStatement> selectRollbackSQL(String currentVersion);

}
