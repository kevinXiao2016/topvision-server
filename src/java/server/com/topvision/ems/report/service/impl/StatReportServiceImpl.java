/***********************************************************************
 * $Id: reportServiceImpl.java,v1.0 2013-5-28 上午10:41:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.report.dao.StatReportDao;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.UserEx;
import com.topvision.platform.service.UserService;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author Bravin
 * @created @2013-5-28-上午10:41:43
 * 
 */
@Service("statReportService")
public class StatReportServiceImpl extends BaseService implements StatReportService {
    @Autowired
    private StatReportDao statReportDao;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public List<TopoEntityStastic> getTopoEntityRelation(Long entityType, Map<String, Object> map) {
        Long userId = (Long) map.get("userId");
        UserEx userEx = userService.getUserEx(userId);
        String userGroupName = CurrentRequest.getUserAuthorityFolderName(userEx);
        if (entityType == null) {
            entityType = entityTypeService.getEntitywithipType();
        }
        List<TopoEntityStastic> relations = statReportDao.selectTopoEntityRelation(userGroupName, entityType);
        return relations;
    }

    @Override
    public List<Long> getAuthFolderIds(List<Long> folderIds) {
        return statReportDao.getAuthFolderIds(folderIds);
    }

}
