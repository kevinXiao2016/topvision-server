/***********************************************************************
 * $Id: CmSignalTargetThresholdDao.java,v1.0 2017年8月8日 下午2:59:04 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.dao;

import java.util.List;

import com.topvision.ems.cm.pnmp.facade.domain.CmTargetThreshold;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午2:59:04
 *
 */
public interface CmSignalTargetThresholdDao extends BaseEntityDao<Object> {

    List<CmTargetThreshold> selectAllThresholds();

    List<CmTargetThreshold> selectLevelThresholds(String level);

    List<CmTargetThreshold> selectThresholdsByTarget(String targetName);

    CmTargetThreshold selectThresholdByTargetAndName(String targetName, String thresholdName);

    void updateThresholds(List<CmTargetThreshold> thresholds);

    void updateThreshold(CmTargetThreshold threshold);

}
