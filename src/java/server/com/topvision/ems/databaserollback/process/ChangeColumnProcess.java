/***********************************************************************
 * $Id: ChangeOrModifyProcess.java,v1.0 2016年7月20日 下午4:34:15 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午4:34:15
 *
 */
public class ChangeColumnProcess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* CHANGE .* .* .*";
    private String oldColumnName;
    private String newColumnName;
    private String tableName;
    //private String newColumnType;
    private String oldColumnType;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] splitSegements = sql.split("ALTER TABLE|CHANGE COLUMN|CHANGE");
        tableName = splitSegements[1].trim();
        String[] split = splitSegements[2].trim().split("\\s+");
        oldColumnName = split[0].trim();
        newColumnName = split[1].trim();
        //newColumnType = split[2];
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#restore(com.topvision.ems.databaserollback.service.SQLRestoreService)
     */
    @Override
    public void restore() {
        sql = "ALTER TABLE " + tableName + " CHANGE " + newColumnName + " " + oldColumnName + " " + oldColumnType;
        sqlBackupService.outputRestoreSql(sql);
    }

    @Override
    public void backupBeforeUpdate() {
        String sql = "SELECT column_type FROM information_schema.columns WHERE TABLE_NAME='" + tableName
                + "'  and COLUMN_name='" + oldColumnName + "'";
        oldColumnType = sqlBackupService.execute2(sql);
    }

}
