/***********************************************************************
 * $Id: NbiPerfGroupDao.java,v1.0 2016年3月21日 下午2:27:30 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.dao;

import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.performance.nbi.api.NbiFtpConfig;
import com.topvision.performance.nbi.api.PerfGroupRow;

/**
 * @author Bravin
 * @created @2016年3月21日-下午2:27:30
 *
 */
public interface NbiPerfGroupDao extends BaseEntityDao<PerfGroupRow> {

    /**
     * @return
     */
    List<PerfGroupRow> getPerfGroupRow();

    /**
     * @return
     */
    NbiFtpConfig selectNbiFtpConfigParam();

}
