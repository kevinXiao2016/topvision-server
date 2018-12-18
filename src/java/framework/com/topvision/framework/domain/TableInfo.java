/***********************************************************************
 * $Id: TableCount.java,v1.0 2012-8-6 下午03:55:00 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author RodJohn
 * @created @2012-8-6-下午03:55:00
 *
 */
public class TableInfo extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 5226841867994334230L;
    private String tableName;
    private String tableCount;
    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }
    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    /**
     * @return the tableCount
     */
    public String getTableCount() {
        return tableCount;
    }
    /**
     * @param tableCount the tableCount to set
     */
    public void setTableCount(String tableCount) {
        this.tableCount = tableCount;
    }
    
    public TableInfo(){
        
    }
    
    /**
     * @param tableName
     * @param tableCount
     */
    public TableInfo(String tableName, String tableCount) {
        this.tableName = tableName;
        this.tableCount = tableCount;
    }
    
}
