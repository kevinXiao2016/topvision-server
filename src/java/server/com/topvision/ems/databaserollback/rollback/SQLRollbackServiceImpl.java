/***********************************************************************
 * $Id: SQLRollbackServiceImpl.java,v1.0 2016年7月25日 下午3:02:29 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.rollback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.databaserollback.dao.SQLRollbackDao;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemVersion;

/**
 * @author Bravin
 * @created @2016年7月25日-下午3:02:29
 *
 */
@Service
public class SQLRollbackServiceImpl extends BaseService implements SQLRollbackService {
    @Autowired
    private SQLRollbackDao sqlRollbackDao;

    public void execRollback() {
        String currentVersion = new SystemVersion().getBuildVersion();
        List<RollbackStatement> sqlList = getRollbackSQL(currentVersion);
        SQLRollbackExecutor sqlRollbackExecutor = new SQLRollbackExecutor();
        sqlRollbackExecutor.setList(sqlList);
        sqlRollbackExecutor.exec();
    }

    public List<RollbackStatement> getRollbackSQL(String currentVersion) {
        return sqlRollbackDao.selectRollbackSQL(currentVersion);
    }
}
