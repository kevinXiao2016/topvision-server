/***********************************************************************
 * $Id: OltPortFlowSummaryServiceImpl.java,v1.0 2013-7-2 下午3:48:05 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.message;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.report.oltsniportflow.dao.OltSniPortFlowReportDao;
import com.topvision.ems.report.message.SummaryEvent;
import com.topvision.ems.report.message.SummaryListener;
import com.topvision.ems.report.service.SummaryService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2013-7-2-下午3:48:05
 * 
 */
@Service("oltPortFlowSummary")
public class OltPortFlowSummaryImpl extends BaseService implements SummaryService, SummaryListener {

    @Autowired
    private MessageService messageService;
    @Autowired
    private OltSniPortFlowReportDao oltSniPortFlowReportDao;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SummaryListener.class, this);
    }

    @Override
    public void execHourlySummary(SummaryEvent event) {
        oltSniPortFlowReportDao.migrateFlowHourly();
    }

    @Override
    public void execDailySummary(SummaryEvent event) {
        oltSniPortFlowReportDao.summaryFlowDaily();
    }

}
