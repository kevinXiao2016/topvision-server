/***********************************************************************
 * $Id: VersionRecord.java,v1.0 2015年5月21日 下午10:13:03 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Victor
 * @created @2015年5月21日-下午10:13:03
 *
 */
@Alias("versionRecord")
public class VersionRecord implements AliasesSuperType, Comparable<VersionRecord> {
    private static final long serialVersionUID = 5088289696618600632L;

    public static final int TABLE_PRIORITY = 0;
    public static final int TRI_PRIORITY = 1;
    public static final int FUNC_PRIORITY = 2;
    public static final int PROC_PRIORITY = 3;
    public static final int DATA_PRIORITY = 4;

    private String moduleName = null;
    private String versionNum = null;
    private Timestamp versionDate = null;
    private String currentVersion;
    private String content;
    private StringBuilder sqlBuilder;
    private Timestamp lastTime;
    private int sqlPriority;

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof VersionRecord)) {
            return false;
        }
        return getId().equals(((VersionRecord) obj).getId());
    }

    public String getId() {
        StringBuilder sb = new StringBuilder(moduleName);
        sb.append("_V").append(versionNum);
        sb.append("_D").append(versionDate);
        return sb.toString();
    }

    public VersionRecord append(String s) {
        if (sqlBuilder == null) {
            sqlBuilder = new StringBuilder();
        }
        sqlBuilder.append(s);
        return this;
    }

    @Override
    public int compareTo(VersionRecord other) {
        if (this.sqlPriority != other.sqlPriority) {
            return this.sqlPriority - other.sqlPriority;
        }

        if (this.versionDate.before(other.versionDate)) {
            return -1;
        } else if (this.versionDate.after(other.versionDate)) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public Timestamp getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(Timestamp versionDate) {
        this.versionDate = versionDate;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getContent() {
        if (content == null) {
            content = sqlBuilder.toString();
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StringBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    public void setSqlBuilder(StringBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    public Timestamp getLastTime() {
        return lastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    public int getSqlPriority() {
        return sqlPriority;
    }

    public void setSqlPriority(int sqlPriority) {
        this.sqlPriority = sqlPriority;
    }

}
