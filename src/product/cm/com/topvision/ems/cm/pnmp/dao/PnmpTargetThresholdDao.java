/***********************************************************************
 * $Id: PnmpTargetThresholdDao.java,v1.0 2017年8月8日 下午2:58:50 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.dao;

import java.util.List;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午2:58:50
 *
 */
public interface PnmpTargetThresholdDao extends BaseEntityDao<Object> {

    List<PnmpTargetThreshold> selectAllThresholds();

    List<PnmpTargetThreshold> selectLevelThresholds(String level);

    List<PnmpTargetThreshold> selectThresholdsByTarget(String targetName);

    PnmpTargetThreshold selectThresholdByTargetAndName(String targetName, String thresholdName);

    void updateThresholds(List<PnmpTargetThreshold> thresholds);

    void updateThreshold(PnmpTargetThreshold threshold);

}
