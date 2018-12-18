/***********************************************************************
 * $Id: RenameTableProcess.java,v1.0 2016年7月20日 下午5:11:40 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午5:11:40
 *
 */
public class RenameTableProcess extends RollbackableProcess {
    public static final String PATTERN = "RENAME .* TO .*";
    private String oldTableName;
    private String newTableName;

    @Override
    public void resolveSQL(String sql) {
        String[] splitSegements = sql.split("RENAME TABLE|TO");
        oldTableName = splitSegements[1];
        newTableName = splitSegements[2];
    }

    @Override
    public void restore() {
        String sql = "RENAME TABLE " + newTableName + " TO " + oldTableName;
        sqlBackupService.outputRestoreSql(sql);
    }

}
