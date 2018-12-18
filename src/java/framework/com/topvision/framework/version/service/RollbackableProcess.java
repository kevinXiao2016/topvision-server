/***********************************************************************
 * $Id: SQLProcess.java,v1.0 2016年7月20日 下午1:50:11 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service;

import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bravin
 * @created @2016年7月20日-下午1:50:11
 *
 */
public abstract class RollbackableProcess {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected String sql;
    protected String tableName;
    protected String whereField;
    protected SQLBackupService sqlBackupService;

    protected static String join(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(",").append(str);
        }
        return sb.substring(1);
    }

    protected static String join(String[] list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(",").append(str);
        }
        return sb.substring(1);
    }

    /**
     * 解析原始SQL,注意：
     * 找出位置的时候需要先TOUPPERCASE,实际取段的时候要用原始SQL
     * 例如： "ALTER TABLE XXX  ADD COLUMN YYY".indexOf("ADD COLUMN") -> 15
     * "alter table xxx add column yyy".substring(15)
     * @param sql
     * @throws JSQLParserException 
     */
    public abstract void resolveSQL(String sql) throws JSQLParserException;

    /**
     * @param dispatcher
     * @return
     * @throws SQLException 
     */
    public abstract void restore();

    public void backupBeforeUpdate() throws SQLException {
    }

    protected String getWhereFieldWithAnd() {
        return whereField != null && whereField.length() > 0 ? (" AND " + whereField) : "";
    }

    public SQLBackupService getSqlBackupService() {
        return sqlBackupService;
    }

    public void setSqlBackupService(SQLBackupService sqlBackupService) {
        this.sqlBackupService = sqlBackupService;
    }

}
