/***********************************************************************
 * $Id: CreateTableProcess.java,v1.0 2016年7月21日 下午4:39:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月21日-下午4:39:37
 *
 */
public class CreateTableProcess extends RollbackableProcess {
    public static final String PATTERN = "CREATE TABLE.*";
    private String tableName;

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        int c = sql.indexOf("(");
        String first = sql.substring(0, c);
        tableName = first.replace("CREATE TABLE", "").replace("IF NOT EXISTS", "").trim();
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#restore(com.topvision.framework.databaserollback.service.SQLRestoreService)
     */
    @Override
    public void restore() {
        sqlBackupService.outputRestoreSql("DROP TABLE IF EXISTS " + tableName);
    }

}
