/***********************************************************************
 * $Id: DropForeignKeyProcess.java,v1.0 2016年7月22日 下午3:16:30 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月22日-下午3:16:30
 *
 */
public class DropForeignKeyProcess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* DROP FOREIGN KEY .*";
    private String constraintName;
    private String tableName;
    private String restoreForeignKey;

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] split = sql.split("ALTER TABLE|DROP FOREIGN KEY");
        tableName = split[1].trim();
        constraintName = split[2].trim();
    }

    public static void main(String[] args) {
        new DropForeignKeyProcess().resolveSQL("alter table OltPortVlan drop FOREIGN KEY FK_vlan_olt_port;"
                .toUpperCase());
    }

    /**
     * @param dispatcher
     * @return
     * @throws SQLException 
     */
    @Override
    public void backupBeforeUpdate() throws SQLException {
        String sql = "SELECT COLUMN_NAME,REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME  FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE"
                + " WHERE CONSTRAINT_NAME='" + constraintName + "'";
        List<String> list = new ArrayList<String>();
        list.add("COLUMN_NAME");
        list.add("REFERENCED_TABLE_NAME");
        list.add("REFERENCED_COLUMN_NAME");
        List<String[]> data = sqlBackupService.execute(sql, list);
        if (data.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sql = "ALTER TABLE " + tableName + " ADD CONSTRAINT " + constraintName + " FOREIGN KEY (";
        for (String[] row : data) {
            sb.append(",");
            sb.append(row[0]);
        }
        sql += sb.substring(1);
        sql += ") references " + data.get(0)[1] + "(";
        sb = new StringBuilder();
        for (String[] row : data) {
            sb.append(",");
            sb.append(row[2]);
        }
        sql += sb.substring(1);
        sql += ") on delete cascade on update cascade";
        restoreForeignKey = sql;
    }

    @Override
    public void restore() {
        sqlBackupService.outputRestoreSql(restoreForeignKey);
    }
}
