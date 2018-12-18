/***********************************************************************
 * $Id: UniRateBatchConfigServiceImpl.java,v1.0 2014-5-15 下午3:39:16 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.dao.UniRateBatchConfigDao;
import com.topvision.ems.epon.onu.domain.UniRateLimitTemplate;
import com.topvision.ems.epon.onu.service.UniRateBatchConfigService;
import com.topvision.framework.service.BaseService;

/**
 * @author flack
 * @created @2014-5-15-下午3:39:16
 *
 */
@Service("uniRateBatchConfigService")
public class UniRateBatchConfigServiceImpl extends BaseService implements UniRateBatchConfigService {
    @Autowired
    private UniRateBatchConfigDao uniRateBatchConfigDao;

    @Autowired
    private UniDao uniDao;

    @Override
    public List<UniRateLimitTemplate> getTemplateList(Long entityId) {
        return uniRateBatchConfigDao.queryTemplateList(entityId);
    }

    @Override
    public UniRateLimitTemplate getTemplateById(Long entityId, Integer templateId) {
        return uniRateBatchConfigDao.selectTemplateById(entityId, templateId);
    }

    @Override
    public void addTemplate(UniRateLimitTemplate rateTemplate) {
        uniRateBatchConfigDao.insertTemplate(rateTemplate);
    }

    @Override
    public void modifyTemplate(UniRateLimitTemplate rateTemplate) {
        uniRateBatchConfigDao.updateTemplate(rateTemplate);
    }

    @Override
    public void deleteTemplate(Integer templateId) {
        uniRateBatchConfigDao.deleteTemplate(templateId);
    }

    @Override
    public void updateProtRateLimit(List<UniRateLimitTemplate> applyList, UniRateLimitTemplate template) {
        for (UniRateLimitTemplate uniRate : applyList) {
            template.setUniId(uniDao.getUniIdByIndex(uniRate.getEntityId(), uniRate.getUniIndex()));
            uniRateBatchConfigDao.updatePortRateLimit(template);
        }
    }
}
