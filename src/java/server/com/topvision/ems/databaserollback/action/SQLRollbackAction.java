/***********************************************************************
 * $Id: SQLRollbackAction.java,v1.0 2016年7月21日 下午4:45:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.version.service.SQLDecompileService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author Bravin
 * @created @2016年7月21日-下午4:45:58
 *
 */
@Controller("sqlRollbackAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SQLRollbackAction extends BaseAction {
    private static final long serialVersionUID = -5875357888839799774L;
    @Autowired
    private SQLDecompileService sqlRollbackService;

    public String execRollback() {
        String sql = "";
        sqlRollbackService.decompileSQL(sql);
        return NONE;
    }
}
