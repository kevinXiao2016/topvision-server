/***********************************************************************
 * $Id: CmSignalTargetThresholdService.java,v1.0 2017年8月8日 下午4:37:08 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service;

import java.util.List;

import com.topvision.ems.cm.pnmp.facade.domain.CmTargetThreshold;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:37:08
 *
 */
public interface CmSignalTargetThresholdService {

    List<CmTargetThreshold> getAllThresholds();

    List<CmTargetThreshold> selectLevelThresholds(String level);

    List<CmTargetThreshold> getThresholdsByTargetList(String targetName);

    CmTargetThreshold getThresholdByTargetAndName(String targetName, String thresholdName);

    void updateThresholds(List<CmTargetThreshold> thresholds);

    void updateThreshold(CmTargetThreshold threshold);

}
