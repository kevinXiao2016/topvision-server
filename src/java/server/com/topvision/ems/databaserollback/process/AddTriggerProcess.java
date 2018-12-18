/***********************************************************************
 * $Id: AddTriggerProcess.java,v1.0 2016年7月20日 下午5:47:19 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import com.topvision.framework.version.service.RollbackableProcess;

/**
 * @author Bravin
 * @created @2016年7月20日-下午5:47:19
 *
 */
public class AddTriggerProcess extends RollbackableProcess {
    public static final String PATTERN = "CREATE TRIGGER .*";
    private String triggerName;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        String[] split = sql.split("CREATE TRIGGER|BEGIN");
        triggerName = split[1].trim();
        int c = triggerName.indexOf(" ");
        triggerName = triggerName.substring(0, c).trim();
    }

    @Override
    public void restore() {
        sqlBackupService.outputRestoreSql("DROP TRIGGER IF EXISTS " + triggerName);
    }

}
