/***********************************************************************
 * $Id: DropTriggerProcess.java,v1.0 2016年7月20日 下午5:20:26 $
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
 * @created @2016年7月20日-下午5:20:26
 *
 */
public class DropTriggerProcess extends RollbackableProcess {
    public static final String PATTERN = "DROP TRIGGER .*";
    private String triggerName;
    private String createTriggerSql;

    @Override
    public void resolveSQL(String sql) {
        super.sql = sql;
        triggerName = sql.replaceAll("DROP TRIGGER|IF EXISTS", "").trim();
    }

    @Override
    public void restore() {
        if (createTriggerSql != null && createTriggerSql.length() > 1) {
            sqlBackupService.outputRestoreSql("DELIMITER && \r\n" + createTriggerSql + "\r\n&&\r\n DELIMITER ;");
        }
    }

    @Override
    public void backupBeforeUpdate() throws SQLException {
        try {
            List<String> list = new ArrayList<String>();
            list.add("SQL ORIGINAL STATEMENT");
            List<String[]> data = sqlBackupService.execute("show create trigger " + triggerName, list);
            createTriggerSql = data.get(0)[0];
        } catch (Exception e) {
            logger.info("trigger:{} not exist", triggerName);
        }
    }

    public static void main(String[] args) {
        System.out.println("DROP TRIGGER IF EXISTS TRI_CMC_SNR_TOP10".replaceAll("DROP TRIGGER|IF EXISTS", ""));
    }
}
