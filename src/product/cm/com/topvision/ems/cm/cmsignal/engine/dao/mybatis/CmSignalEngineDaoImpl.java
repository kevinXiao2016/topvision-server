/***********************************************************************
 * $Id: CmSignalEngineDaoImpl.java,v1.0 2015-4-11 下午2:32:02 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmsignal.engine.dao.mybatis;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cm.cmsignal.domain.Cm3Signal;
import com.topvision.ems.cm.cmsignal.domain.CmSignal;
import com.topvision.ems.cm.cmsignal.engine.dao.CmSignalEngineDao;
import com.topvision.ems.engine.dao.EngineDaoSupport;

/**
 * @author fanzidong
 * @created @2015-4-11-下午2:32:02
 *
 */
public class CmSignalEngineDaoImpl extends EngineDaoSupport<CmSignal> implements CmSignalEngineDao {

    @Override
    public void insertCmSignal(CmSignal cmSignal) {
        getSqlSession().insert(getNameSpace("insertCmSignal"), cmSignal);
    }

    @Override
    public void insertCm3Signal(List<Cm3Signal> cm3Signals) {
        // 先找出待删除的cmId，并删除之前的数据
        List<Long> cmIds = new ArrayList<Long>();
        for (Cm3Signal cm3Signal : cm3Signals) {
            if (!cmIds.contains(cm3Signal.getCmId())) {
                cmIds.add(cm3Signal.getCmId());
            }
        }
        if (cmIds.size() > 0) {
            try {
                getSqlSession().delete(getNameSpace("deleteCm3Signals"), cmIds);
            } catch (Exception e) {
                logger.error("deleteCm3Signals error: " + cmIds);
            }
        }

        for (Cm3Signal cm3Signal : cm3Signals) {
            try {
                getSqlSession().insert(getNameSpace("insertOrUpdateCm3Signal"), cm3Signal);
            } catch (Exception e) {
                logger.error("insertCm3Signal error: " + cm3Signal.toString());
            }
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.cmsignal.domain.CmSignal";
    }

}
