/***********************************************************************
 * $Id: CmSignalTargetThresholdServiceImpl.java,v1.0 2017年8月9日 上午9:55:53 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.pnmp.dao.CmSignalTargetThresholdDao;
import com.topvision.ems.cm.pnmp.facade.domain.CmTargetThreshold;
import com.topvision.ems.cm.pnmp.service.CmSignalTargetThresholdService;
import com.topvision.framework.service.BaseService;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午9:55:53
 *
 */
@Service("cmSignalTargetThresholdService")
public class CmSignalTargetThresholdServiceImpl extends BaseService implements CmSignalTargetThresholdService {

    @Autowired
    private CmSignalTargetThresholdDao cmSignalTargetThresholdDao;

    @Override
    public List<CmTargetThreshold> getAllThresholds() {
        return cmSignalTargetThresholdDao.selectAllThresholds();
    }

    @Override
    public List<CmTargetThreshold> selectLevelThresholds(String level) {
        return cmSignalTargetThresholdDao.selectLevelThresholds(level);
    }

    @Override
    public List<CmTargetThreshold> getThresholdsByTargetList(String targetName) {
        return cmSignalTargetThresholdDao.selectThresholdsByTarget(targetName);
    }

    @Override
    public CmTargetThreshold getThresholdByTargetAndName(String targetName, String thresholdName) {
        return cmSignalTargetThresholdDao.selectThresholdByTargetAndName(targetName, thresholdName);
    }

    @Override
    public void updateThresholds(List<CmTargetThreshold> thresholds) {
        cmSignalTargetThresholdDao.updateThresholds(thresholds);
    }

    @Override
    public void updateThreshold(CmTargetThreshold threshold) {
        cmSignalTargetThresholdDao.updateThreshold(threshold);
    }

}
