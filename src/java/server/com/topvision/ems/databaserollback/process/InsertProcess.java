/***********************************************************************
 * $Id: InsertProcess.java,v1.0 2016年7月20日 下午2:12:49 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.version.service.RollbackableProcess;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

/**
 * JSQLParser在同时处理多条VALUES时,无法解析
 * @author Bravin
 * @created @2016年7月20日-下午2:12:49
 *
 */
public class InsertProcess extends RollbackableProcess {
    /*判断时需要将换行符去掉,VALUES和()之间可以不加空格*/
    public static final String PATTERN = "INSERT INTO.* VALUES.*";
    private List<List<String>> insertDatas = new ArrayList<List<String>>();
    private List<String> insertFields;

    @Override
    public void restore() {
        if (insertFields == null || insertFields.isEmpty()) {
            sql = "select COLUMN_NAME from information_schema.columns where TABLE_NAME='" + tableName + "'";
            insertFields = sqlBackupService.execute3(sql);
        }
        for (List<String> rowData : insertDatas) {
            String sql = "DELETE FROM " + tableName;
            sql += " WHERE ";
            for (int i = 0; i < insertFields.size(); i++) {
                if ("NULL".equals(rowData.get(i).toUpperCase())) {
                    sql += insertFields.get(i) + " is null";
                    sql += " AND ";
                } else {
                    sql += insertFields.get(i) + "=" + rowData.get(i);
                    sql += " AND ";
                }
            }
            sqlBackupService.outputRestoreSql(sql.substring(0, sql.length() - 5));
        }
    }

    @Override
    public void resolveSQL(String sql) throws JSQLParserException {
        Statement stmt;
        insertFields = new ArrayList<String>();
        insertDatas = new ArrayList<List<String>>();
        stmt = new CCJSqlParserManager().parse(new StringReader(sql));
        Insert insert = (Insert) stmt;
        tableName = insert.getTable().getName();
        List<Column> columns = insert.getColumns();
        if (columns != null) {
            for (Column column : columns) {
                insertFields.add(column.getColumnName());
            }
        }
        ItemsList itemsList = insert.getItemsList();
        if (itemsList instanceof MultiExpressionList) {
            MultiExpressionList $list = (MultiExpressionList) itemsList;
            for (ExpressionList expressions : $list.getExprList()) {
                handleExpressionList(expressions);
            }
        } else if (itemsList instanceof ExpressionList) {
            handleExpressionList((ExpressionList) itemsList);
        }
        /* String[] splitSegements = sql.split("INSERT INTO|VALUES");
         String tableNameSegment = splitSegements[1];
         if (tableNameSegment.matches(".*\\(.*\\).*")) {
             int f = tableNameSegment.indexOf("(");
             int l = tableNameSegment.indexOf(")");
             tableName = tableNameSegment.substring(0, f).trim();
             insertFields = Arrays.asList(tableNameSegment.substring(f + 1, l).trim().split(","));
         } else {
             tableName = tableNameSegment.trim();
         }
         String insertValues = splitSegements[2];
         foundInsertData(insertValues.trim());*/
    }

    private void handleExpressionList(ExpressionList expressionList) {
        List<String> rowdata = new ArrayList<String>();
        for (Expression expression : expressionList.getExpressions()) {
            rowdata.add(expression.toString());
        }
        insertDatas.add(rowdata);
    }

    /*private void foundInsertData(String data) {
        int leftCh = data.indexOf("(");
        if (leftCh != -1) {
            //**()必须配套出现,但是存在 (sss,ab(b)),(...)的情况,所以还需要检查配套
            int rightCh = findRightCompletedChar(data, leftCh, 0);
            try {
                String insertRow = data.substring(leftCh + 1, rightCh);
                List<String> rowData = Arrays.asList(insertRow.split(","));
                insertDatas.add(rowData);
                foundInsertData(data.substring(rightCh + 1));
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }*/

    /**
     * 寻找和左括号匹配的字符
     * @param data
     * @param counter 
     * @param leftCh 
     * @throws JSQLParserException 
     */
    /*private static int findRightCompletedChar(String data, int leftCh, int rightCh) {
        int nextLeft = data.indexOf("(", leftCh + 1);
        int nextRight = data.indexOf(")", rightCh + 1);
        if (nextLeft < nextRight && nextLeft != -1) {
            return findRightCompletedChar(data, nextLeft, nextRight);
        }
        return nextRight;
    }*/

    public static void main(String[] args) throws JSQLParserException {
        String data = ("insert into systempreferences(name,value,module) values('flowCollectType','43200,43300,43100','cmtsFlowCollect'),('flowCollectType','43200,43300,43100','cmtsFlowCollect')")
                .toUpperCase();
        InsertProcess s = new InsertProcess();
        s.resolveSQL(data);
        System.out.println(1);
    }
}
