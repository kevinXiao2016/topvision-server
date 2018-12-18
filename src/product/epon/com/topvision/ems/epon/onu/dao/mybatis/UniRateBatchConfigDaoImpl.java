/***********************************************************************
 * $Id: UniRateBatchConfigDaoImpl.java,v1.0 2014-5-15 下午3:30:16 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.UniRateBatchConfigDao;
import com.topvision.ems.epon.onu.domain.UniRateLimitTemplate;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2014-5-15-下午3:30:16
 *
 */
@Repository("uniRateBatchConfigDao")
public class UniRateBatchConfigDaoImpl extends MyBatisDaoSupport<Object> implements UniRateBatchConfigDao {

    @Override
    public List<UniRateLimitTemplate> queryTemplateList(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("queryTemplateList"), entityId);
    }

    @Override
    public UniRateLimitTemplate selectTemplateById(Long entityId, Integer templateId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("templateId", templateId);
        return this.getSqlSession().selectOne(getNameSpace("selectTemplateById"), params);
    }

    @Override
    public void insertTemplate(UniRateLimitTemplate rateTemplate) {
        this.getSqlSession().insert(getNameSpace("insertTemplate"), rateTemplate);
    }

    @Override
    public void updateTemplate(UniRateLimitTemplate rateTemplate) {
        this.getSqlSession().update(getNameSpace("updateTemplate"), rateTemplate);
    }

    @Override
    public void deleteTemplate(Integer templateId) {
        this.getSqlSession().delete(getNameSpace("deleteTemplate"), templateId);
    }

    @Override
    public void updatePortRateLimit(UniRateLimitTemplate templateId) {
        this.getSqlSession().update(getNameSpace("updateUniPortRateLimit"), templateId);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.UniRateLimitTemplate";
    }

}
