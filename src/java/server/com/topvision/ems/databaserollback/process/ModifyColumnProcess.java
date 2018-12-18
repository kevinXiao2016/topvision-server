/***********************************************************************
 * $Id: ModifyColumnProcess.java,v1.0 2016年7月20日 下午5:07:14 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午5:07:14
 *
 */
public class ModifyColumnProcess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* MODIFY .*";
    private String columnName;
    private String tableName;
    //private String newColumnType;
    private String oldColumnType;

    @Override
    public void resolveSQL(String sql) {
        String[] splitSegements = sql.split("ALTER TABLE|MODIFY COLUMN|MODIFY");
        tableName = splitSegements[1].trim();
        String[] split = splitSegements[2].trim().split("\\s+");
        columnName = split[0].trim();
    }

    @Override
    public void restore() {
        String sql = "SELECT column_type FROM information_schema.columns WHERE TABLE_NAME='" + tableName
                + "'  and COLUMN_name='" + columnName + "'";
        oldColumnType = sqlBackupService.execute2(sql);
        sql = "ALTER TABLE " + tableName + " MODIFY COLUMN " + columnName + " " + oldColumnType;
        sqlBackupService.outputRestoreSql(sql);
    }

}
