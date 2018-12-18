/***********************************************************************
 * $Id: PnmpTargetThresholdService.java,v1.0 2017年8月8日 下午4:36:57 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service;

import java.util.List;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:36:57
 *
 */
public interface PnmpTargetThresholdService {

    List<PnmpTargetThreshold> getAllThresholds();

    List<PnmpTargetThreshold> getLevelThresholds(String level);

    List<PnmpTargetThreshold> getThresholdsByTarget(String targetName);

    PnmpTargetThreshold getThresholdByTargetAndName(String targetName, String thresholdName);

    void updateThresholds(List<PnmpTargetThreshold> thresholds);

    void updateThreshold(PnmpTargetThreshold threshold);

}
