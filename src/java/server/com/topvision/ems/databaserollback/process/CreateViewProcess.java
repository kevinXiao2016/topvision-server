/***********************************************************************
 * $Id: CreateViewProcess.java,v1.0 2016年7月25日 下午4:10:38 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import java.io.StringReader;

import com.topvision.framework.version.service.RollbackableProcess;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.view.CreateView;

/**
 * @author Bravin
 * @created @2016年7月25日-下午4:10:38
 *
 */
public class CreateViewProcess extends RollbackableProcess {
    public static final String PATTERN = "CREATE VIEW.*";
    private String viewName;

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) throws JSQLParserException {
        Statement stmt = new CCJSqlParserManager().parse(new StringReader(sql));
        CreateView createView = (CreateView) stmt;
        viewName = createView.getView().getName();
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.process.RollbackableProcess#restore()
     */
    @Override
    public void restore() {
        sqlBackupService.outputRestoreSql("DROP VIEW " + viewName);
    }

}
