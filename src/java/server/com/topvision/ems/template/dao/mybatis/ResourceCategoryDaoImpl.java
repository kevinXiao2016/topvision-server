package com.topvision.ems.template.dao.mybatis;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.topvision.ems.template.dao.ResourceCategoryDao;
import com.topvision.ems.template.domain.ResourceCategory;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.exception.dao.DaoException;

@Repository("resourceCategoryDao")
public class ResourceCategoryDaoImpl extends MyBatisDaoSupport<ResourceCategory> implements ResourceCategoryDao {
    public String getCategoryPath(long categoryId) throws DaoException {
        return ((String) getSqlSession().selectOne(getNameSpace("getCategoryPath"), categoryId));
    }

    @Override
    public String getDomainName() {
        return ResourceCategory.class.getName();
    }

    @Override
    public void insertEntity(ResourceCategory category) throws DataAccessException {
        super.insertEntity(category);
        try {
            String path = getCategoryPath(category.getParentId());
            if (path == null) {
                path = "";
            }
            category.setPath(path + category.getCategoryId() + "/");
            updateCategoryPath(category);
        } catch (DaoException e) {
            getLogger().debug(e.getMessage(), e);
        }
    }

    public void updateCategoryPath(ResourceCategory category) throws DaoException {
        getSqlSession().update(getNameSpace("updateCategoryPath"), category);
    }

}
