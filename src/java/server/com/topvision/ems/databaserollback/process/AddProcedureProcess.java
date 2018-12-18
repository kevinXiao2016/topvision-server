/***********************************************************************
 * $Id: AddProcedureProcess.java,v1.0 2016年7月20日 下午5:44:07 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午5:44:07
 *
 */
public class AddProcedureProcess extends RollbackableProcess {
    public static final String PATTERN = "CREATE PROCEDURE .*";
    private String procedureName;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] split = sql.split("CREATE PROCEDURE|BEGIN");
        procedureName = split[1];
        int c = procedureName.indexOf("(");
        procedureName = procedureName.substring(0, c).trim();
    }

    @Override
    public void restore() {
        sqlBackupService.outputRestoreSql("DROP PROCEDURE IF EXISTS " + procedureName);
    }

}
