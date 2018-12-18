/**
 * 
 */
package com.topvision.ems.resources.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.resources.dao.ResourcesDao;
import com.topvision.ems.resources.domain.EntityStastic;
import com.topvision.ems.resources.domain.Resource;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.exception.dao.DaoException;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author kelers
 * 
 */
@Repository("resourcesDao")
public class ResourcesDaoImpl extends MyBatisDaoSupport<Resource> implements ResourcesDao {
    @Override
    public String getDomainName() {
        return Resource.class.getName();
    }

    @Override
    public long getEntityCount() throws DaoException {
        return getSqlSession().selectOne(getNameSpace("getEntityCount"));
    }

    @Override
    public long statAgentInstalledEntity() throws DaoException {
        return getSqlSession().selectOne(getNameSpace("statAgentInstalledEntity"));
    }

    @Override
    public List<EntityType> statEntityByBigCategory(Long type) throws DaoException {
        return super.getSqlSession().selectList(getNameSpace("statEntityByBigCategory"), type);
    }

    @Override
    public List<EntityType> statEntityBySmallCategory() throws DaoException {
        return super.getSqlSession().selectList(getNameSpace("statEntityBySmallCategory"));
    }

    @Override
    public List<EntityType> statSnmpEntityByBigCategory() throws DaoException {
        return super.getSqlSession().selectList(getNameSpace("statSnmpEntityByBigCategory"));
    }

    @Override
    public long statSnmpSupportEntity() throws DaoException {
        return getSqlSession().selectOne(getNameSpace("statSnmpSupportEntity"));
    }

    @Override
    public List<EntityStastic> getEntityStasticByState(Long type) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("type", type);
        return getSqlSession().selectList(getNameSpace("getEntityStasticByState"), authorityHashMap);
    }
}
