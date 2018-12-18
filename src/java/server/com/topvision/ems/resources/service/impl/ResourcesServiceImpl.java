/**
 * 
 */
package com.topvision.ems.resources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.resources.dao.ResourcesDao;
import com.topvision.ems.resources.domain.EntityCategoryStat;
import com.topvision.ems.resources.domain.EntityStastic;
import com.topvision.ems.resources.domain.EntityStat;
import com.topvision.ems.resources.service.ResourcesService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author kelers
 * 
 */
@Service("resourcesService")
public class ResourcesServiceImpl extends BaseService implements ResourcesService {
    @Autowired
    private ResourcesDao resourcesDao;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public EntityStat statEntity() throws Exception {
        EntityStat stat = new EntityStat();
        stat.setTotal(resourcesDao.getEntityCount());
        stat.setCountAgentInstalled(resourcesDao.statAgentInstalledEntity());
        stat.setCountSnmpSupport(resourcesDao.statSnmpSupportEntity());
        return stat;
    }

    @Override
    public EntityCategoryStat statEntityCategory() throws Exception {
        EntityCategoryStat stat = new EntityCategoryStat();
        Long type = entityTypeService.getEntitywithipType();
        stat.setParentCategory(resourcesDao.statEntityByBigCategory(type));
        return stat;
    }

    @Override
    public EntityStat statSnmpEntity() throws Exception {
        EntityStat stat = new EntityStat();
        stat.setTotal(resourcesDao.statSnmpSupportEntity());
        stat.setCountSnmpSupport(stat.getTotal());
        return stat;
    }

    @Override
    public EntityCategoryStat statSnmpEntityCategory() throws Exception {
        EntityCategoryStat stat = new EntityCategoryStat();
        stat.setParentCategory(resourcesDao.statSnmpEntityByBigCategory());
        return stat;
    }

    @Override
    public List<EntityStastic> getEntityStasticByState() {
        Long type = entityTypeService.getEntitywithipType();
        return resourcesDao.getEntityStasticByState(type);
    }

}
