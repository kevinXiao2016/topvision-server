/***********************************************************************
 * $Id: InsertSelectProcess.java,v1.0 2016年7月24日 下午8:28:47 $
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

import net.sf.jsqlparser.JSQLParserException;

/**
 * @author Bravin
 * @created @2016年7月24日-下午8:28:47
 *
 */
public class InsertSelectProcess extends RollbackableProcess {
    public static final String PATTERN = "INSERT INTO .* SELECT.*";
    private boolean support = true;
    private List<String> data;
    private List<String> columns;

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) throws JSQLParserException {
        data = new ArrayList<String>();
        if (sql.contains("FROM") || sql.contains("ON DUPLICATE KEY")) {
            logger.error("INSERT.SELECT.FROM GRAMMER:{}", sql);
            support = false;
            return;
        }
        String[] split = sql.split("INSERT INTO|SELECT");
        tableName = split[1];
        String[] split2 = split[2].split(",");
        for (String o : split2) {
            data.add(o.trim());
        }
    }

    @Override
    public void backupBeforeUpdate() throws SQLException {
        if (support) {
            String sql = "SELECT column_name FROM information_schema.columns WHERE TABLE_NAME='" + tableName
                    + "' ORDER BY ordinal_position";
            columns = sqlBackupService.execute3(sql);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#restore()
     */
    @Override
    public void restore() {
        if (support) {
            String sql = "DELETE FROM " + tableName + " WHERE ";
            for (int i = 0; i < data.size(); i++) {
                sql += columns.get(i) + " = " + data.get(i);
                sql += " AND ";
            }
            sqlBackupService.outputRestoreSql(sql.substring(0, sql.length() - 5));
        }
    }

}
