/***********************************************************************
 * $Id: AddPrimaryKeyProcess.java,v1.0 2016年7月20日 下午3:56:06 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * 如果升级后修改了主键索引，运行一段时间后由于插入了新的数据，而导致回退时该主键索引不能被删除，怎么办？
 * @author Bravin
 * @created @2016年7月20日-下午3:56:06
 *
 */
public class AddPrimaryKeyProcess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* ADD PRIMARY KEY.*";
    private String tableName;

    //private String[] columns;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] splitSegements = sql.split("ALTER TABLE|ADD PRIMARY KEY");
        tableName = splitSegements[1].trim();
        /*String columnSegment = splitSegements[2].replace("\\(", "").replace("\\)", "").trim();
        columns = columnSegment.split(",");*/
    }

    @Override
    public void restore() {
        String sql = "ALTER TABLE " + tableName + " DROP PRIMARY KEY";
        /* commit by bravin@20160727 主键是越往后越正确,正确的操作是没必要往前回溯的,因为就算是回溯成功了，其本质还是一个错误 */
        //sqlBackupService.outputRestoreSql(sql);
        //需要记录之前存在的主键
        /*sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE  where TABLE_NAME = '" + tableName
                + "' and  CONSTRAINT_NAME=\"PRIMARY\"  ORDER BY  ordinal_position";
        List<String> primaryKeys = sqlRestoreService.execute3(sql);
        //如果之前就存在主键,则还需要将主键恢复
        if (!primaryKeys.isEmpty()) {
            sql = "ALTER TABLE " + tableName + " ADD PRIMARY KEY (" + join(primaryKeys) + ")";
            sqlRestoreService.outputRestoreSql(sql);
        }*/

    }
}
