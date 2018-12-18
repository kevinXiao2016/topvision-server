package com.topvision.ems.resources.dao;

import java.util.List;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.resources.domain.EntityStastic;
import com.topvision.ems.resources.domain.Resource;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.exception.dao.DaoException;

public interface ResourcesDao extends BaseEntityDao<Resource> {
    /**
     * 得到设备总数.
     * 
     * @return
     * @throws DaoException
     */
    long getEntityCount() throws DaoException;

    long statAgentInstalledEntity() throws DaoException;

    List<EntityType> statEntityByBigCategory(Long type) throws DaoException;

    List<EntityType> statEntityBySmallCategory() throws DaoException;

    List<EntityType> statSnmpEntityByBigCategory() throws DaoException;

    long statSnmpSupportEntity() throws DaoException;

    List<EntityStastic> getEntityStasticByState(Long type);
}
