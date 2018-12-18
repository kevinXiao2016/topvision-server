/***********************************************************************
 * $Id: PerfTargetServiceImpl.java,v1.0 2013-8-1 上午09:08:25 $
 *
 * @author: lizongtian
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.performance.dao.PerfTargetDao;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.framework.service.BaseService;

/**
 * @author lizongtian
 * @created @2013-8-1-上午09:08:25
 */
@Service("perfTargetService")
public class PerfTargetServiceImpl extends BaseService implements PerfTargetService {
    @Autowired
    private PerfTargetDao perfTargetDao;

    @Override
    public List<String> getPerfTargetGroupsByDeviceType(Long entityId) {
        return perfTargetDao.getPerfTargetGroupsByDeviceType(entityId);
    }

    @Override
    public List<String> getOnuPerfTargetGroupsByDeviceType(Long entityType) {
        return perfTargetDao.getOnuPerfTargetGroupsByDeviceType(entityType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.service.PerfTargetService#getPerfTargetNamesByGroup(java.lang
     * .String)
     */
    @Override
    public List<String> getPerfTargetNamesByGroup(String groupName, Long type) {
        return perfTargetDao.getPerfNamesByGroup(groupName, type);
    }

    @Override
    public List<String> getOnuPerfTargetNamesByGroup(String groupName, Long entityType) {
        return perfTargetDao.getOnuPerfNamesByGroup(groupName, entityType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.service.PerfTargetService#getPerfTargetNameList(java.lang.Integer
     * )
     */
    @Override
    public List<String> getPerfTargetNameList(Long entityType) {
        return perfTargetDao.getPerfTargetNameList(entityType);
    }

    @Override
    public Integer loadDevicePerfTargetListNum(Map<String, Object> queryMap) {
        Integer count = perfTargetDao.loadDevicesNumByMap(queryMap);
        if (count == null) {
            count = 0;
        }
        return count;
    }

    @Override
    public String getPerfTargetCategory(String perfTarget, Long typeId) {
        return perfTargetDao.selectPerfTargetCategory(typeId, perfTarget);
    }

}
