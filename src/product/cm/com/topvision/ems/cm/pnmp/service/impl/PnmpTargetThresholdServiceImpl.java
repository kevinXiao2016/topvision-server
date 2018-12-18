/***********************************************************************
 * $Id: PnmpTargetThresholdServiceImpl.java,v1.0 2017年8月9日 上午9:56:47 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.pnmp.dao.PnmpTargetThresholdDao;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.ems.cm.pnmp.service.PnmpTargetThresholdService;
import com.topvision.framework.service.BaseService;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午9:56:47
 *
 */
@Service("pnmpTargetThresholdService")
public class PnmpTargetThresholdServiceImpl extends BaseService implements PnmpTargetThresholdService {

    @Autowired
    private PnmpTargetThresholdDao pnmpTargetThresholdDao;

    @Override
    public List<PnmpTargetThreshold> getAllThresholds() {
        return pnmpTargetThresholdDao.selectAllThresholds();
    }

    @Override
    public List<PnmpTargetThreshold> getLevelThresholds(String level) {
        return pnmpTargetThresholdDao.selectLevelThresholds(level);
    }

    @Override
    public List<PnmpTargetThreshold> getThresholdsByTarget(String targetName) {
        return pnmpTargetThresholdDao.selectThresholdsByTarget(targetName);
    }

    @Override
    public PnmpTargetThreshold getThresholdByTargetAndName(String targetName, String thresholdName) {
        return pnmpTargetThresholdDao.selectThresholdByTargetAndName(targetName, thresholdName);
    }

    @Override
    public void updateThresholds(List<PnmpTargetThreshold> thresholds) {
        pnmpTargetThresholdDao.updateThresholds(thresholds);
    }

    @Override
    public void updateThreshold(PnmpTargetThreshold threshold) {
        pnmpTargetThresholdDao.updateThreshold(threshold);
    }
}
