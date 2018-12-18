/***********************************************************************
 * $Id: RollbackSQL.java,v1.0 2016年7月25日 下午3:04:08 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.rollback;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年7月25日-下午3:04:08
 *
 */
public class RollbackStatement implements AliasesSuperType {
    private static final long serialVersionUID = -865641543911710621L;
    private String sql;
    private String currentVersion;
    private String fromVersion;
    private Timestamp backupDate;
    private String module;
    private String sqlVersion;
    private String sqlDate;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getFromVersion() {
        return fromVersion;
    }

    public void setFromVersion(String fromVersion) {
        this.fromVersion = fromVersion;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Timestamp getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(Timestamp backupDate) {
        this.backupDate = backupDate;
    }

    public String getSqlVersion() {
        return sqlVersion;
    }

    public void setSqlVersion(String sqlVersion) {
        this.sqlVersion = sqlVersion;
    }

    public String getSqlDate() {
        return sqlDate;
    }

    public void setSqlDate(String sqlDate) {
        this.sqlDate = sqlDate;
    }

}
