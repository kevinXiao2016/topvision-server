/***********************************************************************
 * $Id: DropProcedureProcess.java,v1.0 2016年7月20日 下午5:41:15 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午5:41:15
 *
 */
public class DropProcedureProcess extends RollbackableProcess {
    public static final String PATTERN = "DROP PROCEDURE .*";
    private String procedureName;
    private String createProcedureSql;

    @Override
    public void resolveSQL(String sql) {
        procedureName = sql.replace("DROP PROCEDURE IF EXISTS ", "");
    }

    @Override
    public void restore() {
        if (createProcedureSql != null && createProcedureSql.length() > 1) {
            sqlBackupService.outputRestoreSql("DELIMITER && \r\n" + createProcedureSql + "\r\n&&\r\n DELIMITER ;");
        }
    }

    @Override
    public void backupBeforeUpdate() throws SQLException {
        try {
            List<String> columns = new ArrayList<String>();
            columns.add("CREATE PROCEDURE");
            List<String[]> data = sqlBackupService.execute("show create procedure " + procedureName, columns);
            createProcedureSql = data.get(0)[0];
        } catch (Exception e) {
            logger.info("procedure:{} not exist", procedureName);
        }
    }
}
