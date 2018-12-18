/***********************************************************************
 * $Id: ForeignKeyProess.java,v1.0 2016年7月20日 下午4:46:42 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午4:46:42
 *
 */
public class AddForeignKeyProess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* ADD CONSTRAINT .* FOREIGN KEY.*";
    private String foreignKeyName;
    private String tableName;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] split = sql.split("ALTER TABLE|ADD CONSTRAINT|FOREIGN KEY");
        tableName = split[1].trim();
        foreignKeyName = split[2].trim();
    }

    @Override
    public void restore() {
        String sql = "ALTER TABLE " + tableName + " DROP FOREIGN KEY " + foreignKeyName;
        sqlBackupService.outputRestoreSql(sql);
    }
}
