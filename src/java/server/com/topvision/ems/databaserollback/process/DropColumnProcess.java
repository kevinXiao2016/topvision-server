/***********************************************************************
 * $Id: DropColumnProcess.java,v1.0 2016年7月20日 下午3:24:49 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import java.sql.SQLException;
import java.util.List;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午3:24:49
 *
 */
public class DropColumnProcess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* DROP COLUMN .*";
    private String tableName;
    private String column;
    private String columnType;
    private List<String> columns;
    private List<String[]> affectedDatas;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] splitSegements = sql.split("ALTER TABLE|DROP COLUMN");
        tableName = splitSegements[1].trim();
        column = splitSegements[2].trim();
    }

    @Override
    public void restore() {
        sqlBackupService.outputRestoreSql("ALTER TABLE " + tableName + " ADD COLUMN " + column + " " + columnType);
        for (String[] data : affectedDatas) {
            String sql = "UPDATE " + tableName + " SET " + column + "=" + data[data.length - 1] + " WHERE ";
            for (int i = 0; i < columns.size() - 1; i++) {
                sql += columns.get(i) + "=" + data[i];
                sql += " AND ";
            }
            sql = sql.substring(0, sql.length() - 5);
            sqlBackupService.outputRestoreSql(sql);
        }
    }

    @Override
    public void backupBeforeUpdate() throws SQLException {
        String sql = "SELECT column_type FROM information_schema.columns WHERE TABLE_NAME='" + tableName
                + "'  and COLUMN_name='" + column + "'";
        columnType = sqlBackupService.execute2(sql);

        sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE  where TABLE_NAME = '" + tableName
                + "' and  CONSTRAINT_NAME='PRIMARY'";
        columns = sqlBackupService.execute3(sql);
        //如果主键不存在,则查询所有的列
        if (columns.isEmpty()) {
            sql = "select COLUMN_NAME from information_schema.columns where TABLE_NAME='" + tableName + "'";
            columns = sqlBackupService.execute3(sql);
            //将column置于最后一列
            columns.remove(column);
            columns.add(column);
            sql = "SELECT " + join(columns) + " FROM " + tableName;
        } else {
            columns.add(column);
            sql = "SELECT " + join(columns) + " FROM " + tableName;
        }
        affectedDatas = sqlBackupService.execute(sql, columns);
    }
}
