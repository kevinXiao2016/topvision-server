/***********************************************************************
 * $Id: AddIndexProcess.java,v1.0 2016年7月20日 下午4:27:40 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午4:27:40
 *
 */
public class AddIndexProcess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* ADD INDEX .*";
    private String tableName;
    private String index;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] splitSegements = sql.split("ALTER TABLE|ADD INDEX");
        tableName = splitSegements[1].trim();
        int c = splitSegements[2].indexOf("(");
        index = splitSegements[2].substring(0, c).trim();
    }

    @Override
    public void restore() {
        String sql = "ALTER TABLE " + tableName + " DROP INDEX " + index;
        sqlBackupService.outputRestoreSql(sql);
    }
}
