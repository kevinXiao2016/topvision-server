/***********************************************************************
 * $Id: DropPrimaryKeyProcess.java,v1.0 2016年7月22日 下午8:30:02 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import java.util.List;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月22日-下午8:30:02
 *
 */
public class DropPrimaryKeyProcess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* DROP PRIMARY KEY.*";
    private List<String> columns;

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] splitSegements = sql.split("ALTER TABLE|DROP PRIMARY KEY");
        tableName = splitSegements[1].trim();
    }

    @Override
    public void restore() {
        String sql = "ALTER TABLE " + tableName + " ADD PRIMARY KEY (" + join(columns) + ")";
        /* commit by bravin@20160727 主键是越往后越正确,正确的操作是没必要往前回溯的,因为就算是回溯成功了，其本质还是一个错误 */
        //sqlBackupService.outputRestoreSql(sql);
    }

    @Override
    public void backupBeforeUpdate() {
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE  where TABLE_NAME = '" + tableName
                + "' AND  CONSTRAINT_NAME='PRIMARY'";
        columns = sqlBackupService.execute3(sql);
    }
}
