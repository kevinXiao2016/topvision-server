/***********************************************************************
 * $Id: MSearchServiceImpl.java,v1.0 2014-6-21 下午2:06:32 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CmListDao;
import com.topvision.ems.cmc.cm.domain.CcmtsLocation;
import com.topvision.ems.cmc.cm.domain.OltLocation;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.mobile.dao.MSearchDao;
import com.topvision.ems.mobile.domain.Location;
import com.topvision.ems.mobile.service.MSearchService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author jay
 * @created @2014-6-21-下午2:06:32
 * 
 */
@Service("mSearchService")
public class MSearchServiceImpl extends BaseService implements MSearchService {
    @Autowired
    private MSearchDao mSearchDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private CmListDao cmListDao;

    @Override
    public List<Location> queryByCmtsMac(Map<String, String> map) {
        // String formatQueryMac = MacUtils.formatQueryMac(cmtsMac);
        List<Entity> entities = mSearchDao.getEntityListByMac(map);
        List<Location> locations = new ArrayList<>();
        for (Entity entity : entities) {
            Location location = new Location();
            location.setEntityId(entity.getParentId());
            location.setCmcId(entity.getEntityId());
            location.setTypeId(entity.getTypeId());
            location.setTypeName(entity.getTypeName());
            if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
                location.setHasOlt(true);
                OltLocation oltLocation = cmListDao.selectOltLocation(entity.getParentId());
                location.setOltLocation(oltLocation);
                CcmtsLocation cmtsLocation = mSearchDao.getCmtsLocation(entity.getEntityId());
                location.setCcmtsLocation(cmtsLocation);
                locations.add(location);
            } else if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                location.setHasOlt(false);
                CcmtsLocation cmtsLocation = mSearchDao.getCmtsLocation(entity.getEntityId());
                location.setCcmtsLocation(cmtsLocation);
                locations.add(location);
            }
        }
        return locations;
    }

    @Override
    public Long queryCountByCmtsMac(Map<String, String> map) {
        return mSearchDao.getEntityCountByMac(map);
    }

    @Override
    public List<Location> queryByCmtsName(Map<String, String> map) {
        List<Entity> entities = mSearchDao.getEntityListByName(map);
        List<Location> locations = new ArrayList<>();
        for (Entity entity : entities) {
            Location location = new Location();
            location.setEntityId(entity.getParentId());
            location.setCmcId(entity.getEntityId());
            location.setTypeId(entity.getTypeId());
            location.setTypeName(entity.getTypeName());
            if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
                location.setHasOlt(true);
                OltLocation oltLocation = cmListDao.selectOltLocation(entity.getParentId());
                location.setOltLocation(oltLocation);
                CcmtsLocation cmtsLocation = mSearchDao.getCmtsLocation(entity.getEntityId());
                location.setCcmtsLocation(cmtsLocation);
                locations.add(location);
            } else if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                CcmtsLocation cmtsLocation = mSearchDao.getCmtsLocation(entity.getEntityId());
                location.setCcmtsLocation(cmtsLocation);
                locations.add(location);
            }
        }
        return locations;
    }

    @Override
    public Long queryCountByCmtsName(Map<String, String> map) {
        return mSearchDao.getEntityCountByName(map);
    }

    @Override
    public List<Location> queryByCmMac(Map<String, String> map) {
        List<Entity> entities = mSearchDao.getEntityListByCmMac(map);
        List<Location> locations = new ArrayList<>();
        for (Entity entity : entities) {
            Location location = new Location();
            location.setEntityId(entity.getParentId());
            location.setCmcId(entity.getEntityId());
            location.setTypeId(entity.getTypeId());
            location.setTypeName(entity.getTypeName());
            if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
                location.setHasOlt(true);
                OltLocation oltLocation = cmListDao.selectOltLocation(entity.getParentId());
                location.setOltLocation(oltLocation);
                CcmtsLocation cmtsLocation = mSearchDao.getCmtsLocation(entity.getEntityId());
                location.setCcmtsLocation(cmtsLocation);
                locations.add(location);
            } else if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                CcmtsLocation cmtsLocation = mSearchDao.getCmtsLocation(entity.getEntityId());
                location.setCcmtsLocation(cmtsLocation);
                locations.add(location);
            }
        }
        return locations;
    }

    @Override
    public Long queryCountByCmMac(Map<String, String> map) {
        return mSearchDao.getEntityCountByCmMac(map);
    }
}
