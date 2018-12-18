/***********************************************************************
 * $Id: DeleteProcess.java,v1.0 2016年7月20日 下午2:34:21 $
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
 * @created @2016年7月20日-下午2:34:21
 *
 */
public class DeleteProcess extends RollbackableProcess {
    public static final String PATTERN = "DELETE FROM .*";
    private List<String[]> affectedDatas;
    private List<String> columns;

    public static void main(String[] args) {
        new DeleteProcess()
                .resolveSQL("DELETE FROM PERFTHRESHOLDRULE WHERE TARGETID = 'ONU_UNI_IN_SPEED' AND TEMPLATEID = 3");
    }

    @Override
    public void resolveSQL(String sql) {
        String[] splitSegements = sql.split("DELETE FROM|WHERE");
        tableName = splitSegements[1].trim();
        if (sql.contains("WHERE")) {
            whereField = splitSegements[2].trim();
        }
    }

    @Override
    public void restore() {
        for (String[] data : affectedDatas) {
            String sql = "INSERT INTO " + tableName;
            sql += "(" + join(columns) + ")";
            List<String> rowList = new ArrayList<String>();
            for (String o : data) {
                if (o == null) {
                    rowList.add(" null ");
                } else {
                    rowList.add("'" + o + "'");
                }
            }
            String valuesString = join(rowList);
            int cursor = columns.size() - data.length;
            while (cursor-- > 0) {
                valuesString += ",null ";
            }
            sql += " VALUES (" + valuesString + ")";
            sqlBackupService.outputRestoreSql(sql);
        }
    }

    @Override
    public void backupBeforeUpdate() throws SQLException {
        // 查询出该表所有列的列名
        String sql = "SELECT column_name FROM information_schema.columns WHERE TABLE_NAME='" + tableName
                + "' ORDER BY ordinal_position";
        columns = sqlBackupService.execute3(sql);
        sql = "SELECT " + join(columns) + " FROM ";
        sql += tableName + " WHERE ";
        sql += "1=1 " + getWhereFieldWithAnd();
        affectedDatas = sqlBackupService.execute(sql, columns);
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String[]> getAffectedDatas() {
        return affectedDatas;
    }

    public void setAffectedDatas(List<String[]> affectedDatas) {
        this.affectedDatas = affectedDatas;
    }

}
