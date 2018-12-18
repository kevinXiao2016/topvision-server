/***********************************************************************
 * $Id: DropViewProcess.java,v1.0 2016年7月25日 下午4:21:48 $
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

import net.sf.jsqlparser.JSQLParserException;

/**
 * @author Bravin
 * @created @2016年7月25日-下午4:21:48
 *
 */
public class DropViewProcess extends RollbackableProcess {
    public static final String PATTERN = "DROP VIEW.*";
    private String viewName;
    private String createView;

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) throws JSQLParserException {
        String[] split = sql.split("DROP VIEW");
        viewName = split[1];
    }

    @Override
    public void backupBeforeUpdate() throws SQLException {
        try {
            List<String> list = new ArrayList<String>();
            list.add("Create View");
            List<String[]> data = sqlBackupService.execute("SHOW CREATE VIEW " + viewName, list);
            createView = data.get(0)[0];
        } catch (Exception e) {
            logger.warn(" VIEW:{} not exist!", viewName);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#restore()
     */
    @Override
    public void restore() {
        if (createView != null) {
            sqlBackupService.outputRestoreSql(createView);
        }
    }

}
