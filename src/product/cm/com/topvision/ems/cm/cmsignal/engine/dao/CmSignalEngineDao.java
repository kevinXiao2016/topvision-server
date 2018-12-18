/***********************************************************************
 * $Id: CmSignalEngineDao.java,v1.0 2015-4-11 下午2:27:37 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmsignal.engine.dao;

import java.util.List;

import com.topvision.ems.cm.cmsignal.domain.Cm3Signal;
import com.topvision.ems.cm.cmsignal.domain.CmSignal;

/**
 * @author fanzidong
 * @created @2015-4-11-下午2:27:37
 *
 */
public interface CmSignalEngineDao {
    void insertCmSignal(CmSignal cmSignal);

    void insertCm3Signal(List<Cm3Signal> cm3Signals);
}
