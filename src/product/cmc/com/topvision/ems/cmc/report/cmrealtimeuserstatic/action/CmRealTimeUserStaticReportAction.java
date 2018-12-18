/***********************************************************************
 * $Id: CmRealTimeUserStaticReporAction.java,v1.0 2013-10-30 上午10:10:15 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmrealtimeuserstatic.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.report.cmrealtimeuserstatic.service.CmRealTimeUserStaticReportCreator;
import com.topvision.ems.cmc.report.domain.FolderOltRelation;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-30-上午10:10:15
 * 
 */
@Controller("cmRealTimeUserStaticReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmRealTimeUserStaticReportAction extends BaseAction {
    private static final long serialVersionUID = -8985796650759901524L;
    @Autowired
    private CmRealTimeUserStaticReportCreator cmRealTimeUserStaticReportCreator;
    private Map<String, FolderOltRelation> folderOltRelations;

    public String showCmRealTimeUserStatic() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        folderOltRelations = cmRealTimeUserStaticReportCreator.statCmRealTimeUserStaticReport(queryMap);
        return SUCCESS;
    }

    public String exportCmRealTimeUserStaticReportToExcel() {
        Date date = new Date();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        Map<String, FolderOltRelation> cmRealTimeUserStaticReport = cmRealTimeUserStaticReportCreator
                .statCmRealTimeUserStaticReport(queryMap);
        cmRealTimeUserStaticReportCreator.exportReportToExcel(cmRealTimeUserStaticReport, date);
        return NONE;
    }

    public Map<String, FolderOltRelation> getFolderOltRelations() {
        return folderOltRelations;
    }

    public void setFolderOltRelations(Map<String, FolderOltRelation> folderOltRelations) {
        this.folderOltRelations = folderOltRelations;
    }

}
