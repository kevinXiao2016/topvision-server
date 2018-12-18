/***********************************************************************
 * $Id: UpdateProcess.java,v1.0 2016年7月20日 下午1:49:26 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.version.service.RollbackableProcess;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 对于 UDPATE ${table} SET A=${V} WHERE A=${X} AND|OR A={Y} AND|OR (A=${Z}) 这种格式的数据,
 * 想要正确的反解回正确的格式难度太大，所以UPDATE全部修改为查询的时候查询除UPDAT列外的所有的列,更新的时候的条件为所有的列.故列越多的表,升级备份的时候会越慢
 * @author Bravin
 * @created @2016年7月20日-下午1:49:26
 *
 */
public class UpdateProcess extends RollbackableProcess {
    public static final String PATTERN = "UPDATE .* SET .*";
    private List<String[]> affectedDatas;
    private List<String> updateFields;
    private List<String> updateFieldValues;
    private boolean whereFiledConflictWithUpdateField;
    private List<String> columns;

    @Override
    public void restore() {
        for (String[] data : affectedDatas) {
            String sqlString = assembleRestoreSql(data);
            sqlBackupService.outputRestoreSql(sqlString);
        }
    }

    private String assembleRestoreSql(String[] data) {
        int updateFieldStartIndex = updateFields.size();
        String sql = "UPDATE " + tableName;
        sql += " SET ";
        for (int i = 0; i < updateFields.size(); i++) {
            if (data[i] != null) {
                sql += updateFields.get(i) + "='" + data[i];
                sql += "',";
            } else {
                sql += updateFields.get(i) + "= null";
                sql += ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += " WHERE 1=1 " + mergeFieldAndValues();
        if (whereFiledConflictWithUpdateField) {
            for (int i = 0; i < updateFields.size(); i++) {
                sql += " AND ";
                sql += updateFields.get(i) + "=" + updateFieldValues.get(i);
            }
            for (int i = updateFieldStartIndex; i < columns.size(); i++) {
                if (data[i] != null) {
                    sql += " AND ";
                    sql += columns.get(i) + "='" + data[i] + "'";
                } else {
                    sql += " AND ";
                    sql += columns.get(i) + " is NULL ";
                }
            }
        } else if (whereField != null) {
            sql += getWhereFieldWithAnd();
        }
        return sql;
    }

    public List<String[]> getAffectedDatas() {
        return affectedDatas;
    }

    public void setAffectedDatas(List<String[]> affectedDatas) {
        this.affectedDatas = affectedDatas;
    }

    @Override
    public void resolveSQL(String sql) throws JSQLParserException {
        Update update;
        updateFields = new ArrayList<String>();
        updateFieldValues = new ArrayList<String>();
        update = (Update) new CCJSqlParserManager().parse(new StringReader(sql));
        tableName = update.getTables().get(0).getName();
        if (update.getWhere() != null) {
            whereField = update.getWhere().toString();
        }
        List<Column> columns = update.getColumns();
        for (Column column : columns) {
            updateFields.add(column.getColumnName());
            if (whereField != null && whereField.contains(column.getColumnName())) {
                whereFiledConflictWithUpdateField = true;
            }
        }
        List<Expression> expressions = update.getExpressions();
        for (Expression expression : expressions) {
            updateFieldValues.add(expression.toString());
        }
    }

    @Override
    public void backupBeforeUpdate() throws SQLException {
        if (whereFiledConflictWithUpdateField) {
            String sql = "SELECT UPPER(column_name) FROM information_schema.columns WHERE TABLE_NAME='" + tableName
                    + "' ORDER BY ordinal_position";
            columns = sqlBackupService.execute3(sql);
            /**将update段放在最前面*/
            columns.removeAll(updateFields);
            columns.addAll(0, updateFields);
            sql = "SELECT " + join(columns) + " FROM ";
            sql += tableName + " WHERE ";
            sql += "1=1 " + getWhereFieldWithAnd();
            affectedDatas = sqlBackupService.execute(sql, columns);
        } else {
            String sql = "SELECT " + join(updateFields) + " FROM ";
            sql += tableName + " WHERE ";
            sql += "1=1 " + getWhereFieldWithAnd();
            affectedDatas = sqlBackupService.execute(sql, updateFields);
        }
    }

    private String mergeFieldAndValues() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < updateFields.size(); i++) {
            sb.append(" AND ");
            sb.append(updateFields.get(i)).append("=").append(updateFieldValues.get(i));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws JSQLParserException {
        String s = null;
        System.out.println("'" + s + "'");
        String sql = "update perfthresholdrule set clearRules = '5_65#5_55#1_10' where targetId = 'OLT_SNI_OPT_TEMP' and (templateId = 1 or templateId = 1000)";
        UpdateProcess updateProcess = new UpdateProcess();
        updateProcess.resolveSQL(sql);
    }
}
