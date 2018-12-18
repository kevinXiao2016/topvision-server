/***********************************************************************
 * $Id: QueryResult.java,v1.0 2013-10-24 下午3:14:13 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Victor
 * @created @2013-10-24-下午3:14:13
 * 
 */
public class QueryResult implements Serializable {
    private static final long serialVersionUID = 6397397803943754238L;
    // 原始查询语句
    private String querySql;
    // 返回的列数
    private int columnCount;
    // 列名
    private List<String> columnNames;
    // 返回的数据，Map以列名和值为键值对
    private List<Map<String, String>> datas;

    /**
     * @return the columnCount
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * @param columnCount
     *            the columnCount to set
     */
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    /**
     * @return the columnNames
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * @param columnNames
     *            the columnNames to set
     */
    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    /**
     * @return the datas
     */
    public List<Map<String, String>> getDatas() {
        return datas;
    }

    /**
     * @param datas
     *            the datas to set
     */
    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }

    /**
     * @return the querySql
     */
    public String getQuerySql() {
        return querySql;
    }

    /**
     * @param querySql
     *            the querySql to set
     */
    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
}
