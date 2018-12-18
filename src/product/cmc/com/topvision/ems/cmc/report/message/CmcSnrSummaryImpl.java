/***********************************************************************
 * $Id: CmcSnrSummaryServiceImpl.java,v1.0 2013-7-2 下午3:46:11 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.message;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.report.cmcsnr.dao.CmcSnrReportDao;
import com.topvision.ems.report.message.SummaryEvent;
import com.topvision.ems.report.message.SummaryListener;
import com.topvision.ems.report.service.SummaryService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2013-7-2-下午3:46:11
 * 
 */
@Service("CmcSnrSummaryImpl")
public class CmcSnrSummaryImpl extends BaseService implements SummaryListener, SummaryService {
    @Autowired
    private MessageService messageService;
    @Autowired
    private CmcSnrReportDao cmcSnrReportDao;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SummaryListener.class, this);
    }

    @Override
    public void execHourlySummary(SummaryEvent event) {
        cmcSnrReportDao.migrateSnrHourly();
    }

    @Override
    public void execDailySummary(SummaryEvent event) {
        cmcSnrReportDao.summarySnrDaily();
    }

}
