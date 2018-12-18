package com.topvision.ems.network.dao;

import com.topvision.ems.facade.domain.EntityAttribute;
import com.topvision.framework.dao.BaseEntityDao;

public interface EntityAttributeDao extends BaseEntityDao<EntityAttribute> {
    void deleteByGroup(Long entityId, String group);
}
