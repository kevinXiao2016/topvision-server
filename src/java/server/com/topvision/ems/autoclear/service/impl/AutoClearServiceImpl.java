/***********************************************************************
 * $Id: AutoClearServiceImpl.java,v1.0 2016年11月14日 上午10:24:48 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.autoclear.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.autoclear.dao.AutoClearDao;
import com.topvision.ems.autoclear.domain.AutoClearCmciRecord;
import com.topvision.ems.autoclear.domain.AutoClearRecord;
import com.topvision.ems.autoclear.service.AutoClearService;
import com.topvision.framework.service.BaseService;

/**
 * @author Rod John
 * @created @2016年11月14日-上午10:24:48
 *
 */
@Service("autoClearService")
public class AutoClearServiceImpl extends BaseService implements AutoClearService {
    @Autowired
    private AutoClearDao autoClearDao;

    /* (non-Javadoc)
     * @see com.topvision.ems.autoclear.service.AutoClearService#loadAutoClearRecord(java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<AutoClearRecord> loadAutoClearRecord(Integer start, Integer limit) {
        return autoClearDao.loadAutoClearRecord(start, limit);
    }
    
    @Override
    public List<AutoClearCmciRecord> loadAutoClearCmciRecord(Integer start, Integer limit) {
        return autoClearDao.loadAutoClearCmciRecord(start, limit);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.autoclear.service.AutoClearService#loadAutoClearRecordCount()
     */
    @Override
    public Integer loadAutoClearRecordCount() {
        return autoClearDao.loadAutoClearRecordCount();
    }
    
    @Override
    public Integer loadAutoClearCmciRecordCount() {
        return autoClearDao.loadAutoClearCmciRecordCount();
    }

}
