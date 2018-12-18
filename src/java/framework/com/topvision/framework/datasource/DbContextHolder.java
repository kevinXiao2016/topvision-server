/***********************************************************************
 * $Id: DbContextHolder.java,v1.0 2015-3-27 上午9:16:54 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.datasource;

/**
 * @author Rod John
 * @created @2015-3-27-上午9:16:54
 *
 */
public class DbContextHolder {

    private static final ThreadLocal<String> dbContextHolder = new ThreadLocal<String>();
    private static final String ENGINE_DB = "engine";

    /**
     * 
     * 
     * @param jdbcType
     */
    public static void setJdbcType(String jdbcType) {
        dbContextHolder.set(jdbcType);
    }

    /**
     * Config Current Thread With Engine DB
     * 
     */
    public static void setEngine() {
        setJdbcType(ENGINE_DB);
    }

    /**
     * Config Current Thread With Default DB(Server DB)
     * 
     */
    public static void setServer() {
        cleanJdbcType();
    }

    /**
     * getJdbcType 
     * 
     * @return
     */
    public static String getJdbcType() {
        return dbContextHolder.get();
    }

    /**
     * cleanJdbcType
     * 
     */
    public static void cleanJdbcType() {
        dbContextHolder.remove();
    }

}
