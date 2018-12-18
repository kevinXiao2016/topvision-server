/***********************************************************************
 * $Id: PnmpCmtsReportServiceImpl.java,v1.0 2017年8月9日 上午9:56:27 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.pnmp.dao.PnmpCmtsReportDao;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmtsReport;
import com.topvision.ems.cm.pnmp.message.PnmpPollStateEvent;
import com.topvision.ems.cm.pnmp.message.PnmpPollStateListener;
import com.topvision.ems.cm.pnmp.service.PnmpCmtsReportService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午9:56:27
 *
 */
@Service("pnmpCmtsReportService")
public class PnmpCmtsReportServiceImpl extends BaseService implements PnmpCmtsReportService, PnmpPollStateListener {

    @Autowired
    private PnmpCmtsReportDao pnmpCmtsReportDao;
    @Autowired
    private MessageService messageService;

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(PnmpPollStateListener.class, this);
    }

    @Override
    public List<PnmpCmtsReport> selectReports() {
        return pnmpCmtsReportDao.selectReports();
    }

    @Override
    public void summaryCmtsReport() {
        pnmpCmtsReportDao.summaryCmtsReport();
    }

    @Override
    public Integer selectCmtsReportsNums() {
        return pnmpCmtsReportDao.selectCmtsReportsNums();
    }

    @Override
    public Integer getOnlineCmNums() {
        return pnmpCmtsReportDao.selectOnlineCmNums();
    }

    @Override
    public Integer getHealthCmNums() {
        return pnmpCmtsReportDao.selectHealthCmNums();
    }

    @Override
    public Integer getMarginalCmNums() {
        return pnmpCmtsReportDao.selectMarginalCmNums();
    }

    @Override
    public Integer getBadCmNums() {
        return pnmpCmtsReportDao.selectBadCmNums();
    }

    @Override
    public void completeRoundStatistics(PnmpPollStateEvent event) {
        this.summaryCmtsReport();
    }

    @Override
    public void startRoundStatistics(PnmpPollStateEvent event) {
    }

}
