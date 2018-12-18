/***********************************************************************
 * $Id: BillBoardServiceImpl.java,v1.0 2013年9月24日 上午11:26:03 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.billboard.dao.BillBoardDao;
import com.topvision.ems.billboard.domain.Notice;
import com.topvision.ems.billboard.mgmt.BillBoardController;
import com.topvision.ems.billboard.service.BillBoardService;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2013年9月24日-上午11:26:03
 *
 */
@Service("billBoardService")
public class BillBoardServiceImpl extends BaseService implements BillBoardService {
    @Autowired
    private BillBoardController billBoardController;
    @Autowired
    private BillBoardDao billBoardDao;

    @Override
    public void createNotice(Notice notice) throws ParseException {
        billBoardController.dispatchNotice(notice);
    }

    public BillBoardDao getBillBoardDao() {
        return billBoardDao;
    }

    public void setBillBoardDao(BillBoardDao billBoardDao) {
        this.billBoardDao = billBoardDao;
    }

    @Override
    public List<Notice> loadAllValidNotice(Long userId) {
        return billBoardController.loadAllValidNotice(userId);
    }

    @Override
    public void clearNoticeById(Integer noticeId) {
        billBoardController.clearNoticeById(noticeId);
    }
}
