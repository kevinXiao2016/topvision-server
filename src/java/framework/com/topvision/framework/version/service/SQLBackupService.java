/***********************************************************************
 * $Id: SQLRestoreService.java,v1.0 2016年7月20日 下午2:58:28 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Bravin
 * @created @2016年7月20日-下午2:58:28
 *
 */
public interface SQLBackupService {

    /**
     * @param sqlString
     */
    void outputRestoreSql(String sql);

    /**
     * @param sql
     * @param columns 
     * @return 
     * @throws SQLException 
     */
    List<String[]> execute(String sql, List<String> columns) throws SQLException;

    /**
     * @param string
     * @return
     */
    String execute2(String string);

    /**
     * @param sql
     * @return
     */
    List<String> execute3(String sql);

    /**
     * @param currentVersion
     */
    void output();

}
