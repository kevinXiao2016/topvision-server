/***********************************************************************

 * $Id: NbiPerfGroupService.java,v1.0 2016年3月21日 下午2:25:09 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.nbi.dao.NbiPerfGroupDao;
import com.topvision.ems.nbi.service.NbiCallBackService;
import com.topvision.framework.service.BaseService;
import com.topvision.performance.nbi.api.NbiFtpConfig;
import com.topvision.performance.nbi.api.PerfGroupRow;

/**
 * @author Bravin
 * @created @2016年3月21日-下午2:25:09
 *
 */
@Service
public class NbiCallBackServiceImpl extends BaseService implements NbiCallBackService {
    @Autowired
    private NbiPerfGroupDao nbiPerfGroupDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiPerfGroupService#getPerfGroupRow()
     */
    @Override
    public List<PerfGroupRow> getPerfGroupRow() {
        return nbiPerfGroupDao.getPerfGroupRow();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiPerfGroupService#loadNbiFtpConfigParam()
     */
    @Override
    public NbiFtpConfig loadNbiFtpConfigParam() {
        return nbiPerfGroupDao.selectNbiFtpConfigParam();
    }

}
