/***********************************************************************
 * $Id: DropTableProcess.java,v1.0 2016年7月20日 下午2:57:36 $
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
 * @created @2016年7月20日-下午2:57:36
 *
 */
public class DropTableProcess extends RollbackableProcess {
    public static final String PATTERN = "DROP TABLE .*";
    private String createTableSql;
    private List<String[]> affectedDatas = new ArrayList<String[]>();
    private List<String> tableColumns;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        tableName = sql.replace("DROP TABLE", "").replace("IF EXISTS", "").trim();
    }

    @Override
    public void restore() {
        if (createTableSql != null) {
            sqlBackupService.outputRestoreSql(createTableSql);
            for (String[] data : affectedDatas) {
                String sql = "INSERT INTO " + tableName;
                sql += "(" + join(tableColumns) + ")";
                List<String> rowList = new ArrayList<String>();
                for (String o : data) {
                    if (o == null) {
                        rowList.add(" null ");
                    } else {
                        rowList.add("'" + o + "'");
                    }
                }
                String valuesString = join(rowList);
                int cursor = tableColumns.size() - data.length;
                while (cursor-- > 0) {
                    valuesString += ",null ";
                }
                sql += " VALUES (" + valuesString + ")";
                sqlBackupService.outputRestoreSql(sql);
            }
        }
    }

    @Override
    public void backupBeforeUpdate() throws SQLException {
        try {
            List<String> columns = new ArrayList<String>();
            columns.add("CREATE TABLE");
            List<String[]> data = sqlBackupService.execute("show create table " + tableName, columns);
            createTableSql = data.get(0)[0];

            String sql = "SELECT column_name FROM information_schema.columns WHERE TABLE_NAME='" + tableName
                    + "' ORDER BY ordinal_position";
            tableColumns = sqlBackupService.execute3(sql);
            sql = "SELECT " + join(tableColumns) + " FROM " + tableName;
            affectedDatas = sqlBackupService.execute(sql, tableColumns);
        } catch (Exception e) {
            logger.info("table:{} not exist", tableName);
        }
    }

    public String getCreateTableSql() {
        return createTableSql;
    }

    public void setCreateTableSql(String createTableSql) {
        this.createTableSql = createTableSql;
    }

}
