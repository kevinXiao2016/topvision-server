/***********************************************************************
 * $Id: MibAction.java,v 1.1 2009-10-5 上午12:50:31 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.realtime.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.realtime.service.MibService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @Create Date 2009-10-5 上午12:50:31
 * 
 * @author kelers
 * 
 */
@Controller("mibAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MibAction extends BaseAction {
    private static final long serialVersionUID = -4907222378253551652L;
    @Autowired
    protected MibService mibService;
    protected String[] headers;
    protected String tableType;
    protected long entityId;

    public long getEntityId() {
        return entityId;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String getTableType() {
        return tableType;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public void setMibService(MibService mibService) {
        this.mibService = mibService;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }
}
