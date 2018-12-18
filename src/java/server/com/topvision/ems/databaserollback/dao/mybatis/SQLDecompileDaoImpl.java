/***********************************************************************
 * $Id: DatabaseRollBackDaoImpl.java,v1.0 2016年7月20日 下午6:12:28 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.dao.mybatis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.databaserollback.dao.SQLDecompileDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2016年7月20日-下午6:12:28
 *
 */
@Repository
public class SQLDecompileDaoImpl extends MyBatisDaoSupport<Object> implements SQLDecompileDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "DataBaseRollBack";
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.framework.databaserollback.dao.DatabaseRollBackDao#execute(java.lang.String, java.util.List)
     */
    @Override
    public List<String[]> execute(String sql, List<String> columns) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sql", sql);
        Connection connection = getSqlSession().getConnection();
        List<String[]> data = new ArrayList<String[]>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        int size = columns.size();
        while (resultSet.next()) {
            String[] row = new String[size];
            for (int i = 0; i < size; i++) {
                String value = resultSet.getString(columns.get(i));
                row[i] = value;
            }
            data.add(row);
        }
        /*List<Map<String, Object>> list = getSqlSession().selectList(getNameSpace("execute"), map);
        List<String[]> data = new ArrayList<String[]>();
        for (Map<String, Object> $map : list) {
            Set<String> keySet = $map.keySet();
            String[] $data = new String[keySet.size()];
            Iterator<String> iterator = keySet.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                $data[count++] = $map.get(key).toString();
            }
            data.add($data);
        }*/
        return data;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.dao.DatabaseRollBackDao#execute2(java.lang.String)
     */
    @Override
    public String execute2(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sql", sql);
        return getSqlSession().selectOne(getNameSpace("execute2"), map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.dao.DatabaseRollBackDao#execute3(java.lang.String)
     */
    @Override
    public List<String> execute3(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sql", sql);
        return getSqlSession().selectList(getNameSpace("execute3"), map);
    }

}
