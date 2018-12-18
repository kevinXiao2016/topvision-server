/***********************************************************************
 * $Id: VersionConvert.java,v1.0 2012-8-2 下午01:29:51 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dbconvert;

import java.util.List;

import com.topvision.framework.service.BaseService;

/**
 * @author Rod John
 * @created @2012-8-2-下午01:29:51
 * 
 */
public abstract class VersionConvert extends BaseService implements Comparable<VersionConvert> {
    public static final List<String> verStrings = null;
    protected String startVersion;
    protected String endVersion;
    protected String module;
    protected int priority;

    /**
     * 解析器优先级，保证解析器按照一定的顺序执行ru解析
     */
    protected int cos = 0;

    @Override
    public abstract void initialize();

    @Override
    public abstract void destroy();

    public abstract void setStartVersion();

    public abstract void setEndVersion();

    public abstract void setPriority();

    public abstract void setModule();

    public abstract List<String> processModule(List<String> sqlList, String nowVersion, String fileVersion);

    public List<String> convertSqlStrings(List<String> sql, String nowVersion, String fileVersion) throws Exception {
        int startFlag = getStartVersionFlag(fileVersion);
        int endFlag = getEndVersionFlag(nowVersion);
        if (startFlag == endFlag) {
            // no need to update
            return sql;
        }
        if (startFlag < endFlag) {
            while (startFlag < endFlag) {
                // this.getClass().getMethod(verMethodList.get(startFlag).methodName,
                // List.class).invoke(this, sql);
                startFlag++;
            }
            return sql;
        } else {
            // 抛出版本异常
            return null;
        }

    }

    public boolean checkVersion(String nowVersion, String fileVersion) {
        return true;
    }

    // 获取需要更新的开始版本
    public int getStartVersionFlag(String version) {
        for (int i = 0; i < verStrings.size(); i++) {
            if (compareVersion(version, verStrings.get(i)) == 0) {
                return i;
            } else if (compareVersion(version, verStrings.get(i)) > 0) {
                return i - 1;
            } else {
                continue;
            }
        }
        return -1;
    }

    // 获取需要更新的最后版本
    public int getEndVersionFlag(String version) {
        for (int i = 0; i < verStrings.size(); i++) {
            if (compareVersion(version, verStrings.get(i)) == 0) {
                return i;
            } else if (compareVersion(version, verStrings.get(i)) > 0) {
                return i - 1;
            } else {
                continue;
            }
        }
        return -1;
    }

    public int compareVersion(String s1, String s2) {
        String[] varray1 = s1.split("\\.");
        String[] varray2 = s2.split("\\.");
        int count = Math.max(varray1.length, varray2.length);
        for (int i = 0; i < count; i++) {
            if (i >= varray1.length)
                // s1的版本低
                return 1;
            else if (i >= varray2.length)
                // s2的版本低
                return -1;
            else if (varray2[i].equals(varray1[i]))
                continue;
            else
                // 正数表示v2的版本高
                return Integer.parseInt(varray2[i]) - Integer.parseInt(varray1[i]);
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(VersionConvert convert) {
        if (this.cos > convert.cos) {
            return 1;
        } else if (this.cos == convert.cos) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * @return the startVersion
     */
    public String getStartVersion() {
        return startVersion;
    }

    /**
     * @return the endVersion
     */
    public String getEndVersion() {
        return endVersion;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 修改列名方法
     * 
     * @param sql
     * @param oldName
     * @param newName
     * @return
     */
    protected List<String> modifyColumnName(List<String> sqlList, String tableName, String oldName, String newName) {
        String insertTemp = "INSERT INTO " + tableName.toUpperCase();
        for (int i = 0; i < sqlList.size(); i++) {
            if (sqlList.get(i).toUpperCase().startsWith(insertTemp)) {
                // 替换第一个,必须保证出现的第一个是修改的字段名
                sqlList.set(i, sqlList.get(i).toUpperCase().replaceFirst(oldName.toUpperCase(), newName.toUpperCase()));
            }
        }
        return sqlList;
    }

    /**
     * 修改表名方法
     * 
     * @param sql
     * @param oldName
     * @param newName
     * @return
     */
    protected String modifyTableName(String sql, String oldName, String newName) {

        return sql;
    }

    /**
     * 删除表方法
     * 
     * @param sql
     * @param tableName
     * @return
     */
    protected String deleteTable(String sql, String tableName) {

        return sql;
    }

    /**
     * 返回insert语句中的表名
     * 
     * @param insertSql
     * @return
     */
    protected String getTableNameFromInsertSql(String insertSql) {
        return insertSql.split(" ")[2].toUpperCase();
    }

}
