/***********************************************************************
 * $Id: SpecialSQLHandler.java,v1.0 2016年7月25日 下午4:45:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.rollback;

import com.topvision.ems.databaserollback.dao.SQLRollbackDao;

/**
 * @author Bravin
 * @created @2016年7月25日-下午4:45:16
 *
 */
public abstract class SpecialSQLHandler {
    protected SQLRollbackDao sqlRollbackDao;

    public SQLRollbackDao getSqlRollbackDao() {
        return sqlRollbackDao;
    }

    public void setSqlRollbackDao(SQLRollbackDao sqlRollbackDao) {
        this.sqlRollbackDao = sqlRollbackDao;
    }

}
