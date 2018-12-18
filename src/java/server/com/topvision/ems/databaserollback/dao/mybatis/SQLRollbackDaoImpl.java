/***********************************************************************
 * $Id: SQLRollbackDaoImpl.java,v1.0 2016年7月25日 下午3:34:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.databaserollback.dao.SQLRollbackDao;
import com.topvision.ems.databaserollback.rollback.RollbackStatement;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2016年7月25日-下午3:34:51
 *
 */
@Repository
public class SQLRollbackDaoImpl extends MyBatisDaoSupport<Object> implements SQLRollbackDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.dao.SQLRollbackDao#exec(java.lang.String)
     */
    @Override
    public void exec(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sql", sql);
        getSqlSession().update(getNameSpace("executeRollback"), map);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "DataBaseRollBack";
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.dao.SQLRollbackDao#selectRollbackSQL(java.lang.String)
     */
    @Override
    public List<RollbackStatement> selectRollbackSQL(String currentVersion) {
        return null;
    }

}
