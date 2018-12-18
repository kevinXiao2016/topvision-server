/***********************************************************************
 * $Id: JobMonitorAction.java,v 1.1 Aug 15, 2009 7:27:49 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;

@Controller("systemMonitorAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SystemMonitorAction extends BaseAction {
    private static final long serialVersionUID = 5768964920535017857L;

    private List<Map<String, String>> jobList;

    /**
     * 获得监视器列表
     * 
     * @return
     */
    public String getJobs() {
        return SUCCESS;
    }

    public List<Map<String, String>> getJobList() {
        return jobList;
    }

    public void setJobList(List<Map<String, String>> jobList) {
        this.jobList = jobList;
    }
}
